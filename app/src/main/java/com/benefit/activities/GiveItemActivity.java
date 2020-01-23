package com.benefit.activities;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.benefit.R;
import com.benefit.drivers.StorageDriver;
import com.benefit.model.Category;
import com.benefit.model.Product;
import com.benefit.model.PropertyName;
import com.benefit.services.CategoryService;
import com.benefit.services.ProductService;
import com.benefit.utilities.Factory;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This activity is used when the user wants to give/upload/offer item to his profile.
 */
public class GiveItemActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private static final int SNAP_PHOTO = 2;
    private static final int FIRST_CHILD_INDEX_CHIP = 1;

    private Dialog thankYouDailog;
    private Dialog dialogReturnsToActivity;
    private LinearLayout activityRootLinearLayout;
    private LayoutInflater inflater;

    private StorageDriver storageDriver;
    private CategoryService categoryService;
    private ProductService productService;

    private boolean haveCategoriesBeenInflatedOnce = false;

    // the following group are fields for creating a Product
    private Uri mImageUri;
    private String mImageUrl;
    private ImageButton mImageButton;
    private EditText mEdTextTitle;
    private EditText mEdTextBrand;
    private LinearLayout mBrandLayout;
    private int mMetaCategory;
    private int mCategoryGroupIndexInLayout;
    private int mCategory;
    private Map<String, List<String>> mProperties = new HashMap<>(); // to create a Product object
    private SortedMap<String, ChipGroup> mPropertiesChipGroupsBuffer = new TreeMap<>(); // to create UI


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_item);

        createActionBar();
        instantiateDataMembers();
        createServicesAndDrivers();

        createAttributeChips();
    }

    private void createServicesAndDrivers() {
        this.productService = ViewModelProviders.of(this,
                Factory.getProductServiceFactory()).get(ProductService.class);
        this.categoryService = ViewModelProviders.of(this,
                Factory.getCategoryServiceFactory()).get(CategoryService.class);
        this.storageDriver = ViewModelProviders.of(this,
                Factory.getStorageDriverFactory()).get(StorageDriver.class);

    }


    private void updatePropertyOnCheckedChanged(Chip chip) {
        String chipPropertyName = ((PropertyName) chip.getTag()).getName();
        mProperties.put(chipPropertyName,
                Collections.singletonList(chip.getText().toString()));
    }

    private void createChipGroupForPropertyName(PropertyName propertyName) {

        ChipGroup propertyGroup = createChipGroup(propertyName.getName());
        if (propertyName.getValidValues() == null) {
            return; // brand has free text input so we create it elsewhere
        }
        List<String> propertyValues = propertyName.getValidValues();
        for (String value : propertyValues) {
            propertyGroup.addView(createPropertyChipAsView(value, propertyName));
        }

        propertyGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                Chip chip = chipGroup.findViewById(i);
                if (chip != null) {
                    updatePropertyOnCheckedChanged(chip);
                }
            }
        });

        Chip firstChip = (Chip) propertyGroup.getChildAt(FIRST_CHILD_INDEX_CHIP);
        firstChip.setChecked(true);
        updatePropertyOnCheckedChanged(firstChip);

        mPropertiesChipGroupsBuffer.put(propertyName.getName(), propertyGroup);
    }

    private void createPropertiesChips(int categoryId, ChipGroup chipGroup) {

        final List<PropertyName> observedPropertyNames = new LinkedList<>();
        final Observer<List<PropertyName>> propertyNamesObserver = new Observer<List<PropertyName>>() {
            @Override
            public void onChanged(List<PropertyName> propertyNames) {
                observedPropertyNames.addAll(propertyNames);
                for (PropertyName propertyName : observedPropertyNames) {
                    createChipGroupForPropertyName(propertyName);
                }
                addCategoriesChipGroupToLayout(chipGroup);
            }
        };
        categoryService.getAllPropertiesByCategoryId(categoryId).observe(this, propertyNamesObserver);
    }


    private View createCategoryChipAsView(Category category) {
        View chipAsView = inflater.inflate(R.layout.chip_layout, null);
        Chip chip = (Chip) chipAsView;
        chip.setText(category.getName());
        chip.setTag(category);
        return chipAsView;
    }

    private View createPropertyChipAsView(String propertyValue, PropertyName propertyName) {
        View chipAsView = inflater.inflate(R.layout.chip_layout, null);
        Chip chip = (Chip) chipAsView;
        chip.setText(propertyValue);
        chip.setTag(propertyName);
        return chipAsView;
    }


    private ChipGroup createMetaCategoriesObserver() {
        ChipGroup chipGroup = createChipGroup(this.getString(R.string.metacategory_chip_group_heading));
        setOnCheckedChangeListenerForMetaCategories(chipGroup);
        final List<Category> metaCategories = new LinkedList<>();
        final Observer<List<Category>> metaCategoriesObserver = new Observer<List<Category>>() {

            @Override
            public void onChanged(List<Category> categories) {
                metaCategories.addAll(categories);
                for (Category category : metaCategories) {
                    chipGroup.addView(createCategoryChipAsView(category));
                }
                ((Chip) chipGroup.getChildAt(FIRST_CHILD_INDEX_CHIP)).setChecked(true);
            }
        };
        categoryService.getAllMetaCategories().observe(this, metaCategoriesObserver);
        return chipGroup;
    }

    private void setOnCheckedChangeListenerForMetaCategories(ChipGroup chipGroup) {
        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                Chip chip = chipGroup.findViewById(i);
                if (chip != null) {
                    mMetaCategory = ((Category) chip.getTag()).getId();
                    createCategories();
                }
            }
        });
    }

    private void createAttributeChips() {
        ChipGroup chipGroup = createMetaCategoriesObserver();
        activityRootLinearLayout.addView(chipGroup);
        mCategoryGroupIndexInLayout = activityRootLinearLayout.getChildCount();
    }


    private void createCategories() {
        ChipGroup chipGroup = createChipGroup(this.getString(R.string.categories_chip_group_heading));

        setOnCheckedChangeListenerForCategories(chipGroup);
        final List<Category> categories = new LinkedList<>();
        final Observer<List<Category>> categoriesObserver = new Observer<List<Category>>() {

            @Override
            public void onChanged(List<Category> inputCategories) {
                categories.addAll(inputCategories);
                for (Category category : categories) {
                    chipGroup.addView(createCategoryChipAsView(category));
                }
                ((Chip) chipGroup.getChildAt(FIRST_CHILD_INDEX_CHIP)).setChecked(true);
            }
        };
        categoryService.getChildrenByParentId(mMetaCategory).observe(this, categoriesObserver);
    }

    private void setOnCheckedChangeListenerForCategories(ChipGroup chipGroup) {
        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                Chip chip = chipGroup.findViewById(i);
                if (chip != null) {
                    Category checkedCategory = (Category) chip.getTag();
                    mCategory = checkedCategory.getId();
                    createPropertiesChips(checkedCategory.getId(), chipGroup);
                }
            }
        });
    }

    private void addPropertiesChipGroupsToLayout() {
        for (Map.Entry<String, ChipGroup> entry : mPropertiesChipGroupsBuffer.entrySet()) {
            activityRootLinearLayout.addView(entry.getValue());
        }
        activityRootLinearLayout.addView(mBrandLayout);
        mPropertiesChipGroupsBuffer.clear();
    }

    private void addCategoriesChipGroupToLayout(ChipGroup chipGroup) {
        if (haveCategoriesBeenInflatedOnce) {
            activityRootLinearLayout.removeViewsInLayout(mCategoryGroupIndexInLayout,
                    activityRootLinearLayout.getChildCount() - mCategoryGroupIndexInLayout);
            activityRootLinearLayout.addView(chipGroup, mCategoryGroupIndexInLayout);
            addPropertiesChipGroupsToLayout();
        } else {
            activityRootLinearLayout.addView(chipGroup, mCategoryGroupIndexInLayout);
            addPropertiesChipGroupsToLayout();
            haveCategoriesBeenInflatedOnce = true;
        }
    }


    private ChipGroup createChipGroup(String groupName) {
        View chipGroupAsView = inflater.inflate(R.layout.chip_group_layout, null);
        ChipGroup chipGroup = (ChipGroup) chipGroupAsView;
        ((TextView) chipGroup.getChildAt(0)).setText(groupName + ": ");
        return chipGroup;
    }


    private void instantiateDataMembers() {
        mEdTextTitle = findViewById(R.id.item_title_text);
        activityRootLinearLayout = findViewById(R.id.activity_root_linear_layout);
        inflater = LayoutInflater.from(this);
        View brandLayout = inflater.inflate(R.layout.brand_text_input_layout, null);
        mEdTextBrand = brandLayout.findViewById(R.id.item_brand);
        mBrandLayout = (LinearLayout) brandLayout;
        mImageButton = findViewById(R.id.image_button_choose_image);
    }


    public void openFileChooser(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // switch bellow will add a camera capture option in the future
        switch (requestCode) {
            case PICK_IMAGE:
                loadImageFromGallery(resultCode, data);
                break;
            default:
                Log.d("ImplicitIntents", "Can't handle this intent!");
                break;
        }
    }

    private void loadImageFromGallery(int resultCode, @Nullable Intent data) {
        ImageButton mImageButtonUpload = findViewById(R.id.image_button_choose_image);

        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            loadUriAsUrlToButton();
            Picasso.get()
                    .load(mImageUri)
                    .centerCrop()
                    .fit().into(mImageButtonUpload);

        }
    }

    private void loadUriAsUrlToButton() {
        // todo: this doesn't work for now
        final String retUrl = "";
        final Observer<String> urlObserver = new Observer<String>() {

            @Override
            public void onChanged(String url) {
                mImageUrl = url;

            }
        };
        storageDriver.uploadImage(mImageUri).observe(this, urlObserver);
    }

    public int getProductId() {
        return ThreadLocalRandom.current().nextInt(500, 1000 + 1);
    }

    private boolean isThereNoTitle() {
        return (mEdTextTitle.getText() == null || mEdTextTitle.getText().toString().equals(""));
    }

    public void onClickGive(View view) {
        if (mImageUri == null || isThereNoTitle()) {
            createNoPhotoOrTitleDialog();
        } else {
            String itemTitle = mEdTextTitle.getText().toString();
            String itemDescription = this.getString(R.string.no_description_value);
            Date date = Calendar.getInstance().getTime();
            mProperties.put(this.getString(R.string.brand_property_name),
                    Collections.singletonList(mEdTextBrand.getText().toString()));
            List<String> imagesUrls = loadImagesUrls();
            Product product = new Product(mCategory, "jHbxY9G5pdO7Qo5k58ulwPsY1fG2",
                    itemTitle, itemDescription, 0, 0, date, mProperties, imagesUrls);
            productService.addProduct(product);
            createThankYouDailog();
        }
    }

    private List<String> loadImagesUrls() {
        List<String> imagesUrls = new LinkedList<>();
        imagesUrls.add(mImageUrl);
        return imagesUrls;
    }

    private void createThankYouDailog() {
        thankYouDailog = new Dialog(this);
        thankYouDailog.setContentView(R.layout.dialog_thank_you);
        thankYouDailog.show();
    }


    public void doneButton(View view) {
        thankYouDailog.dismiss();
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }

    private void paintRedMissingFields() {
        if (isThereNoTitle()) {
            mEdTextTitle.setBackgroundResource(R.drawable.rounded_edittext_error);
            mEdTextTitle.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    mEdTextTitle.setBackgroundResource(R.drawable.rounded_edittext);
                }
            });
        }
        if (mImageUri == null) {
            mImageButton.setImageResource(R.drawable.ic_add_image_error);
        }
    }


    private void createNoPhotoOrTitleDialog() {
        dialogReturnsToActivity = new Dialog(this);
        dialogReturnsToActivity.setContentView((R.layout.dialog_no_photo_or_title));
        Button gotItButton = dialogReturnsToActivity.findViewById(R.id.got_it_button);
        gotItButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintRedMissingFields();
                dialogReturnsToActivity.dismiss();
            }
        });
        dialogReturnsToActivity.show();
    }


    public void popTipsDialog(View view) {
        Intent intent = new Intent(getApplicationContext(), TipsActivity.class);
        startActivity(intent);
    }


    private void createActionBar() {
        View view = findViewById(R.id.chosen_view);
        view.setVisibility(View.INVISIBLE);
        Button givePlusButton = findViewById(R.id.give_icon);
        givePlusButton.setBackground(getResources().getDrawable(R.drawable.ic_give_colored));
        setActionBarOnClicks();
    }

    private void setActionBarOnClicks() {
        Button giveItemButton = findViewById(R.id.give_icon);
        Button searchButton = findViewById(R.id.search_icon);
        Button chatButton = findViewById(R.id.message_icon);
        Button userButton = findViewById(R.id.user_icon);

        giveItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GiveItemActivity.class);
                startActivity(intent);
            }
        });

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(intent);
            }
        });

        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        com.benefit.activities.UserProfileActivity.class);
                startActivity(intent);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void startMessageActivity() {
        Intent intent = new Intent(this, ConversationActivity.class);
        startActivity(intent);
    }

    private void startSearchActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void startUserProfileActivity() {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }

    private void startGiveActivity() {
        Intent intent = new Intent(this, GiveItemActivity.class);
        startActivity(intent);
    }

}