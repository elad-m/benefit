package com.benefit.activities;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.benefit.R;
import com.benefit.drivers.DatabaseDriver;
import com.benefit.model.Category;
import com.benefit.model.Product;
import com.benefit.model.User;
import com.benefit.services.CategoryService;
import com.benefit.services.ProductService;
import com.benefit.utilities.HeaderClickListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This activity is used when the user wants to give/upload/offer item to his profile.
 */
public class GiveItemActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private static final int SNAP_PHOTO = 2;
    private static final int CHILD_INDEX_CHIP = 1;

    private Dialog thankYouDailog;
    private Dialog dialogReturnsToActivity;
    private LinearLayout activityRootLinearLayout;
    private LayoutInflater inflater;
    private List<String> conditions = Arrays.asList("New", "Used");
    private List<String> sizes = Arrays.asList("S", "M", "L", "XL");

    private DatabaseDriver databaseDriver = new DatabaseDriver();
    private CategoryService categoryService;
    private ProductService productService;

    private boolean categoriesInflatedOnce = false;

    // the following group are fields for creating a Product
    private Uri mImageUri;
    private EditText mEdTextTitle;
    private int mMetaCategory;
    private int mCategory;
    private String mSize;
    private String mCondition;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_item);
        createActionBar();
        instantiateDataMembers();
        createCategoryService();
        createProductService();
        createPropertiesChips();
        createMetaCategoriesChips();
        setHeaderListeners();
    }

    private void createConditionChips() {
        ChipGroup conditionGroup = createChipGroup(getString(R.string.condition_chip_group_heading));
        for (String condition : conditions) {
            View chipAsView = inflater.inflate(R.layout.chip_layout, null);
            Chip chip = (Chip) chipAsView;
            chip.setText(condition);
            chip.setTag(condition);
            conditionGroup.addView(chipAsView);
        }
        ((Chip) conditionGroup.getChildAt(CHILD_INDEX_CHIP)).setChecked(true);
        mCondition = conditions.get(0);
        conditionGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                Chip chip = chipGroup.findViewById(i);
                if (chip != null) {
                    mCondition = chip.getText().toString();
                }
            }
        });
        activityRootLinearLayout.addView(
                conditionGroup, activityRootLinearLayout.getChildCount() - 1);
    }

    private void createPropertiesChips() {
        createConditionChips();
        createSizeChips();
    }

    private void createSizeChips() {
        ChipGroup sizeGroup = createChipGroup(this.getString(R.string.size_chip_group_heading));
        for (String size : sizes) {
            View chipAsView = inflater.inflate(R.layout.chip_layout, null);
            Chip chip = (Chip) chipAsView;
            chip.setText(size);
            chip.setTag(size);
            sizeGroup.addView(chipAsView);
        }
        ((Chip) sizeGroup.getChildAt(CHILD_INDEX_CHIP)).setChecked(true);
        mSize = sizes.get(0);
        sizeGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                Chip chip = chipGroup.findViewById(i);
                if (chip != null) {
                    mSize = chip.getText().toString();
                }
            }
        });
        activityRootLinearLayout.addView(
                sizeGroup, activityRootLinearLayout.getChildCount() - 1);
    }

    private void createProductService() {
        ViewModelProvider.Factory productServiceFactory = new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new ProductService(databaseDriver);
            }
        };
        this.productService = ViewModelProviders
                .of(this, productServiceFactory)
                .get(ProductService.class);
    }

    private void createCategoryService() {
        ViewModelProvider.Factory categoryServiceFactory = new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new CategoryService(databaseDriver);
            }
        };
        this.categoryService = ViewModelProviders
                .of(this, categoryServiceFactory)
                .get(CategoryService.class);
    }

    private View createChipAsView(Category category) {
        View chipAsView = inflater.inflate(R.layout.chip_layout, null);
        Chip chip = (Chip) chipAsView;
        chip.setText(category.getName());
        chip.setTag(category);
        return chipAsView;
    }

    private ChipGroup createMetaCategoriesObserver() {
        ChipGroup chipGroup = createChipGroup(this.getString(R.string.metacategory_chip_group_heading));
        final List<Category> metaCategories = new LinkedList<>();
        final Observer<List<Category>> metaCategoriesObserver = new Observer<List<Category>>() {

            @Override
            public void onChanged(List<Category> categories) {
                metaCategories.addAll(categories);
                for (Category category : metaCategories) {
                    chipGroup.addView(createChipAsView(category));
                }
                ((Chip) chipGroup.getChildAt(CHILD_INDEX_CHIP)).setChecked(true);
                mMetaCategory = (int)metaCategories.get(0).getId();
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
                    mMetaCategory = (int)((Category) chip.getTag()).getId();
                    createCategoriesChipsFromMetaCategory();
                }
            }
        });

    }

    private void createMetaCategoriesChips() {
        ChipGroup chipGroup = createMetaCategoriesObserver();
        setOnCheckedChangeListenerForMetaCategories(chipGroup);
        activityRootLinearLayout.addView(
                chipGroup, activityRootLinearLayout.getChildCount() - 1);
    }


    private ChipGroup createCategoriesObserver() {
        ChipGroup chipGroup = createChipGroup(this.getString(R.string.categories_chip_group_heading));
        final List<Category> categories = new LinkedList<>();
        final Observer<List<Category>> categoriesObserver = new Observer<List<Category>>() {

            @Override
            public void onChanged(List<Category> inputCategories) {
                categories.addAll(inputCategories);
                for (Category category : categories) {
                    chipGroup.addView(createChipAsView(category));
                }
                ((Chip) chipGroup.getChildAt(CHILD_INDEX_CHIP)).setChecked(true);
                mCategory = (int)categories.get(0).getId();
            }
        };
        categoryService.getChildrenByParentId(mMetaCategory).observe(this, categoriesObserver);
        return chipGroup;
    }

    private void setOnCheckedChangeListenerForCategories(ChipGroup chipGroup) {
        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                Chip chip = chipGroup.findViewById(i);
                if (chip != null) {
                    mCategory = (int)((Category) chip.getTag()).getId();
                }
            }
        });
    }

    private void createCategoriesChipsFromMetaCategory() {
        ChipGroup chipGroup = createCategoriesObserver();
        setOnCheckedChangeListenerForCategories(chipGroup);
        addCategoriesChipGroupToLayout(chipGroup);
        categoriesInflatedOnce = true;
    }

    private void addCategoriesChipGroupToLayout(ChipGroup chipGroup) {
        if (categoriesInflatedOnce) {
            View chipGroupView = activityRootLinearLayout
                    .getChildAt(activityRootLinearLayout.getChildCount() - 2);
            activityRootLinearLayout.removeView(chipGroupView);
            activityRootLinearLayout.addView(
                    chipGroup, activityRootLinearLayout.getChildCount() - 1);

        } else {
            activityRootLinearLayout.addView(
                    chipGroup, activityRootLinearLayout.getChildCount() - 1);
        }
    }


    private ChipGroup createChipGroup(String groupName) {
        View chipGroupAsView = inflater.inflate(R.layout.chip_group_layout, null);
        ChipGroup chipGroup = (ChipGroup) chipGroupAsView;
        ((TextView) chipGroup.getChildAt(0)).setText(groupName);
        return chipGroup;
    }


    private void instantiateDataMembers() {
        mEdTextTitle = findViewById(R.id.item_title_text);
        activityRootLinearLayout = findViewById(R.id.activity_root_linear_layout);
        inflater = LayoutInflater.from(this);
        user = (User) getIntent().getSerializableExtra(getString(R.string.user_relay));
    }

    private void createActionBar() {
        View view = findViewById(R.id.chosen_view);
        view.setVisibility(View.INVISIBLE);
        Button givePlusButton = findViewById(R.id.give_icon);
        givePlusButton.setBackground(getResources().getDrawable(R.drawable.ic_give_colored));
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
            Picasso.get()
                    .load(mImageUri)
                    .centerCrop().rotate(90).fit().into(mImageButtonUpload);

        }
    }


    private Map<String, List<String>> getProductProperties() {
        Map<String, List<String>> properties = new HashMap<>();
        properties.put(this.getString(R.string.size_property_name),
                Collections.singletonList(mSize));
        properties.put(this.getString(R.string.condition_property_name),
                Collections.singletonList(mCondition));
        return properties;
    }

    private String getTitleText() {
        String itemTitle;
        if (mEdTextTitle != null) {
            itemTitle = mEdTextTitle.getText().toString();
        } else {
            itemTitle = this.getString(R.string.no_title_value);
        }
        return itemTitle;
    }


    public void onClickGive(View view) {
        if (mImageUri == null) {
            createNoPhotoDialog();
        } else {
            String itemTitle = getTitleText();
            String itemDescription = this.getString(R.string.no_description_value);
            Date date = Calendar.getInstance().getTime();
            Map<String, List<String>> properties = getProductProperties();
            List<String> imagesUrls = loadImagesUrls();
            Product product = new Product(411, mCategory, user.getUid(),
                    itemTitle, itemDescription, 0, 0, date, properties, imagesUrls);
            productService.addProduct(product);
            createThankYouDailog();
        }
    }

    private List<String> loadImagesUrls() {
        List<String> imagesUrls = new LinkedList<>();
        imagesUrls.add(mImageUri.toString());
        return imagesUrls;
    }

    private void createThankYouDailog() {
        thankYouDailog = new Dialog(this);
        thankYouDailog.setContentView(R.layout.thank_you_dialog);
        thankYouDailog.show();
    }


    public void doneButton(View view) {
        thankYouDailog.dismiss();
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }


    public void gotItButton(View view) {
        dialogReturnsToActivity.dismiss();
    }

    private void createNoPhotoDialog() {
        dialogReturnsToActivity = new Dialog(this);
        dialogReturnsToActivity.setContentView((R.layout.no_photo_dialog));
        dialogReturnsToActivity.show();
    }


    public void popTipsDialog(View view) {
        dialogReturnsToActivity = new Dialog(this);
        dialogReturnsToActivity.setContentView(R.layout.photoshooting_tips_dialog);
        dialogReturnsToActivity.show();
    }

    private void setHeaderListeners() {
        HeaderClickListener.setHeaderListeners(findViewById(android.R.id.content).getRootView());
    }

    private void startGiveActivity() {
        Intent intent = new Intent(this, GiveItemActivity.class);
        startActivity(intent);
    }

}