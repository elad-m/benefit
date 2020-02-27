package com.benefit.ui.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.benefit.R;
import com.benefit.activities.MainActivity2;
import com.benefit.activities.TipsActivity;
import com.benefit.drivers.StorageDriver;
import com.benefit.model.Category;
import com.benefit.model.Product;
import com.benefit.model.PropertyName;
import com.benefit.model.User;
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

import static android.app.Activity.RESULT_OK;


/**
 * This fragment is used when the user wants to give/upload/offer item to his profile.
 */
public class GiveFragment extends Fragment {

    public static GiveFragment newInstance(User user, Product productToEdit) {
        GiveFragment fragment = new GiveFragment();
        fragment.mUser = user;
        return fragment;
    }

    private static final int PICK_IMAGE = 1;
    private static final int FIRST_CHILD_INDEX = 0;

    private StorageDriver storageDriver;
    private CategoryService categoryService;
    private ProductService productService;
    private LayoutInflater mInflater;
    private View mFragmentRootView;

    private Dialog mThankYouDailog;
    private Dialog mNoPhotoOrTitleAlertrDialog;
    private LinearLayout mActivityRootLinearLayout;

    private boolean mHaveCategoriesBeenInflatedOnce = false;

    // the following group are fields for creating a Product

    private User mUser;
    private Uri mImageUri;
    private String mImageUrl;
    private EditText mEdTextTitle;
    private EditText mEdTextBrand;
    private ImageButton mImageButtonUpload;
    private LinearLayout mBrandLayout;
    private int mMetaCategory;
    private int mCategoryGroupIndexInLayout;
    private int mCategory;
    private Map<String, List<String>> mProperties = new HashMap<>(); // to create a Product object
    private SortedMap<String, ChipGroup> mPropertiesChipGroupsBuffer = new TreeMap<>(); // to create UI


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mInflater = inflater;
        return inflater.inflate(R.layout.fragment_give, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        createServicesAndDrivers();
        instantiateDataMembers();
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


    private void instantiateDataMembers() {
        mFragmentRootView = getView();
        mActivityRootLinearLayout = mFragmentRootView.findViewById(R.id.activity_root_linear_layout);
        mEdTextTitle = mFragmentRootView.findViewById(R.id.item_title_text);
        createClickables();
        createBrandLayout();

    }

    private void createBrandLayout() {
        View brandLayout = mInflater.inflate(R.layout.brand_text_input_layout, null);
        mEdTextBrand = brandLayout.findViewById(R.id.item_brand);
        mBrandLayout = (LinearLayout) brandLayout;
    }

    private void createClickables(){
        mImageButtonUpload = mFragmentRootView.findViewById(R.id.image_button_choose_image);
        TextView mGiveButton = mFragmentRootView.findViewById(R.id.give_button);
        LinearLayout  mTipButton = mFragmentRootView.findViewById(R.id.give_item_tips_layout);
        mImageButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser(v);
            }
        });

        mGiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickGive(v);
            }
        });

        mTipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTipsActivity(v);
            }
        });
    }


    /**
     * =========================== Creating All Chip Groups ======================================
     */

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createAttributeChips() {
        ChipGroup chipGroup = createMetaCategoriesObserver();
        mActivityRootLinearLayout.addView(chipGroup);
        mCategoryGroupIndexInLayout = mActivityRootLinearLayout.getChildCount();
    }

    private ChipGroup createMetaCategoriesObserver() {
        ChipGroup chipGroup = createChipGroup(this.getString(R.string.metacategory_chip_group_heading));
        setOnCheckedChangeListenerForMetaCategories(chipGroup);
        final List<Category> metaCategories = new LinkedList<>();
        final Observer<List<Category>> metaCategoriesObserver = new Observer<List<Category>>() {

            @Override
            public void onChanged(List<Category> observedCategories) {
                metaCategories.addAll(observedCategories);
                createChipGroupFromCategories(metaCategories, chipGroup);
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
                    mMetaCategory = ((Category) chip.getTag()).getIdAsInt();
                    createCategoriesObserver();
                }
            }
        });
    }

    private void createChipGroupFromCategories(List<Category> categories, ChipGroup chipGroup) {
        int chipIndexToCheck = FIRST_CHILD_INDEX;
        for (Category category : categories) {
            addCategoryAsChipToChipGroup(category, chipGroup);
            if (isChipMatchingProductToEdit(category.getLevel(), category.getId())) {
                chipIndexToCheck = categories.indexOf(category);
            }
        }
        checkChipOfIndex(chipGroup, chipIndexToCheck);
    }

    private void createCategoriesObserver() {
        ChipGroup chipGroup = createChipGroup(this.getString(R.string.categories_chip_group_heading));
        setOnCheckedChangeListenerForCategories(chipGroup);
        final List<Category> categories = new LinkedList<>();
        final Observer<List<Category>> categoriesObserver = new Observer<List<Category>>() {

            @Override
            public void onChanged(List<Category> observedCategories) {
                categories.addAll(observedCategories);
                createChipGroupFromCategories(categories, chipGroup);
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
                    mCategory = checkedCategory.getIdAsInt();
                    createPropertiesChips(checkedCategory.getIdAsInt(), chipGroup);
                }
            }
        });
    }

    private void addCategoryAsChipToChipGroup(Category category, ChipGroup chipGroup) {
        View chipAsView = mInflater.inflate(R.layout.chip_layout, null);
        Chip chip = (Chip) chipAsView;
        chip.setText(category.getName());
        chip.setTag(category);
        chipGroup.addView(chip);
    }


    private boolean isChipMatchingProductToEdit(int level, long categoryId) {
        // if it is a meta category and match or a category and match
        return ((level == 0 && categoryId == mMetaCategory) ||
                (level == 1 && categoryId == mCategory));
    }

    private void createPropertiesChips(int categoryId, ChipGroup chipGroup) {

        final List<PropertyName> observedPropertyNames = new LinkedList<>();
        final Observer<List<PropertyName>> propertyNamesObserver = new Observer<List<PropertyName>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(List<PropertyName> propertyNames) {
                observedPropertyNames.addAll(propertyNames);
                createChipGroupsFromProperties(observedPropertyNames, chipGroup);
            }
        };
        categoryService.getAllPropertiesByCategoryId(categoryId).observe(this, propertyNamesObserver);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createChipGroupsFromProperties(List<PropertyName> propertyNames,
                                                ChipGroup categoryChipGroup) {
        for (PropertyName propertyName : propertyNames) {
            createChipGroupForPropertyName(propertyName);
        }
        addCategoriesAndPropertiesChipGroupsToLayout(categoryChipGroup);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addPropertiesAsChipsToChipGroup(PropertyName propertyName,
                                                 ChipGroup propertyGroup) {

        List<String> propertyValues = propertyName.getValidValues();
        for (String value : propertyValues) {
            addPropertyAsChipToChipGroup(value, propertyName, propertyGroup);
        }
    }

    private void addPropertyAsChipToChipGroup(String propertyValue, PropertyName propertyName,
                                              ChipGroup propertyGroup) {
        View chipAsView = mInflater.inflate(R.layout.chip_layout, null);
        Chip chip = (Chip) chipAsView;
        chip.setText(propertyValue);
        chip.setTag(propertyName);
        propertyGroup.addView(chip);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createChipGroupForPropertyName(PropertyName propertyName) {

        if (propertyName.getValidValues() == null)
            return; // brand, for example, has free text input so we create it in instantiation

        ChipGroup propertyGroup = createChipGroup(propertyName.getName());
        mPropertiesChipGroupsBuffer.put(propertyName.getName(), propertyGroup);

        addPropertiesAsChipsToChipGroup(propertyName, propertyGroup);
        setPropertyGroupOnCheckedChangeListener(propertyGroup);
        checkChipOfIndex(propertyGroup, FIRST_CHILD_INDEX);
    }

    private void setPropertyGroupOnCheckedChangeListener(ChipGroup propertyGroup) {
        propertyGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                Chip chip = chipGroup.findViewById(i);
                if (chip != null) {
                    updatePropertyOnCheckedChanged(chip);
                }
            }
        });
    }

    private void updatePropertyOnCheckedChanged(Chip chip) {
        String chipPropertyName = ((PropertyName) chip.getTag()).getName();
        mProperties.put(chipPropertyName,
                Collections.singletonList(chip.getText().toString()));
    }


    private void checkChipOfIndex(ChipGroup chipGroup, int chipIndexToCheck) {
        // "chipIndexToCheck + 1": because of chipgroup holding textView first
        ((Chip) chipGroup.getChildAt(chipIndexToCheck + 1)).setChecked(true);
    }


    private void addPropertiesChipGroupsToLayout() {
        for (Map.Entry<String, ChipGroup> entry : mPropertiesChipGroupsBuffer.entrySet()) {
            mActivityRootLinearLayout.addView(entry.getValue());
        }
        mActivityRootLinearLayout.addView(mBrandLayout);
        mPropertiesChipGroupsBuffer.clear();
    }

    private void addCategoriesAndPropertiesChipGroupsToLayout(ChipGroup chipGroup) {
        if (mHaveCategoriesBeenInflatedOnce) {
            mActivityRootLinearLayout.removeViewsInLayout(mCategoryGroupIndexInLayout,
                    mActivityRootLinearLayout.getChildCount() - mCategoryGroupIndexInLayout);
            mActivityRootLinearLayout.addView(chipGroup, mCategoryGroupIndexInLayout);
            addPropertiesChipGroupsToLayout();
        } else {
            mActivityRootLinearLayout.addView(chipGroup, mCategoryGroupIndexInLayout);
            addPropertiesChipGroupsToLayout();
            mHaveCategoriesBeenInflatedOnce = true;
        }
    }


    private ChipGroup createChipGroup(String groupName) {
        View chipGroupAsView = mInflater.inflate(R.layout.chip_group_layout, null);
        ChipGroup chipGroup = (ChipGroup) chipGroupAsView;
        ((TextView) chipGroup.getChildAt(0)).setText(groupName + ": ");
        return chipGroup;
    }


    /**
     * =========================== Methods for loading images ======================================
     */

    // todo: what to do with this one?

    public void openFileChooser(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            loadUriAsUrlToButton();
            loadImageIntoButtonImage();
        }
    }

    private void loadImageIntoButtonImage() {
        Picasso.get()
                .load(mImageUri)
                .centerCrop()
                .fit()
                .into(mImageButtonUpload);
    }

    private void loadUriAsUrlToButton() {
        final Observer<String> urlObserver = new Observer<String>() {
            @Override
            public void onChanged(String url) {
                mImageUrl = url;

            }
        };
        storageDriver.uploadImage(mImageUri).observe(this, urlObserver);
    }


    /**
     * =========================== Defining Activity Click events ================================
     */

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
            Product productToAdd = new Product(mCategory, mUser.getUid(),
                    itemTitle, itemDescription, 0, 0, date, mProperties, imagesUrls);

            productService.addProduct(productToAdd);
            createThankYouDailog();
        }
    }


    private boolean isThereNoTitle() {
        return (mEdTextTitle.getText() == null || mEdTextTitle.getText().toString().equals(""));
    }


    private List<String> loadImagesUrls() {
        List<String> imagesUrls = new LinkedList<>();
        imagesUrls.add(mImageUrl);
        return imagesUrls;
    }

    private void createThankYouDailog() {
        mThankYouDailog = new Dialog(getActivity());
        mThankYouDailog.setContentView(R.layout.dialog_thank_you);
        Button doneButton = mThankYouDailog.findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mThankYouDailog.dismiss();
                ((MainActivity2) getActivity()).startProfileFragment();
            }
        });
        mThankYouDailog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mThankYouDailog.show();
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
            mImageButtonUpload.setImageResource(R.drawable.ic_add_image_error);
        }
    }

    private void createNoPhotoOrTitleDialog() {
        mNoPhotoOrTitleAlertrDialog = new Dialog(getActivity());
        mNoPhotoOrTitleAlertrDialog.setContentView((R.layout.dialog_no_photo_or_title));
        mNoPhotoOrTitleAlertrDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button gotItButton = mNoPhotoOrTitleAlertrDialog.findViewById(R.id.got_it_button);
        gotItButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintRedMissingFields();
                mNoPhotoOrTitleAlertrDialog.dismiss();
            }
        });
        mNoPhotoOrTitleAlertrDialog.show();
    }


    public void startTipsActivity(View view) {
        Intent intent = new Intent(getContext(), TipsActivity.class);
        startActivity(intent);
    }

//
//    private void createActionBar() {
//        View view = mFragmentRootView.findViewById(R.id.chosen_view);
//        view.setVisibility(View.INVISIBLE);
//        Button givePlusButton = mFragmentRootView.findViewById(R.id.give_icon);
//        givePlusButton.setBackground(getResources().getDrawable(R.drawable.ic_give_colored));
//        setHeaderListeners();
//    }
//
//    private void setHeaderListeners() {
//        // todo: change the argument from appcompatactivity to fragment activity
//        HeaderClickListener.setHeaderListeners(getActivity());
//    }


}
