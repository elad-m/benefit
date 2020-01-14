package com.benefit;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.benefit.drivers.DatabaseDriver;
import com.benefit.model.Category;
import com.benefit.model.Product;
import com.benefit.services.CategoryService;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
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

    private Dialog thankYouMessage;
    private Button giveButton;
    private LinearLayout activityRootLinearLayout;

    private DatabaseDriver databaseDriver = new DatabaseDriver();
    private CategoryService categoryService;

    // the following group are fields for creating a Product
    private Uri mImageUri;
    private EditText mEdTextTitle;
    private EditText mEdTextDescription;
    private int mMetaCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_item);

        createActionBar();
        initiateDataMembers();
        createMetaCategoriesChips();

//        createChipGroups();


    }
    private void createCategoryService(){
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

    private void createMetaCategoriesChips() {
        createCategoryService();
        LayoutInflater inflater = LayoutInflater.from(this);
        ChipGroup chipGroup = createChipGroup(inflater);

        final List<Category> metaCategories = new LinkedList<>();
        final Observer<List<Category>> metaCategoriesObserver = new Observer<List<Category>>() {

            @Override
            public void onChanged(List<Category> categories) {
                metaCategories.addAll(categories);
                for (Category category: metaCategories){
                    View chipAsView = inflater.inflate(R.layout.chip_layout, null);
                    Chip chip = (Chip) chipAsView;
                    chip.setText(category.getName());
                    chipGroup.addView(chipAsView);
                }
                View firstChip = chipGroup.getChildAt(1);  // because the first child is textView
                ((Chip) firstChip).setChecked(true);
            }
        };
        categoryService.getAllMetaCategories().observe(this, metaCategoriesObserver);

        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                Chip chip = chipGroup.findViewById(i);
                if (chip != null)
                    Toast.makeText(getApplicationContext(), "Chip is " + chip.getText(),
                            Toast.LENGTH_SHORT).show();
            }
        });
        
        activityRootLinearLayout.addView(
                chipGroup,activityRootLinearLayout.getChildCount() - 1);
    }

    private ChipGroup createChipGroup(LayoutInflater inflater) {
        View chipGroupAsView = inflater.inflate(R.layout.chip_group_layout, null);
        ChipGroup chipGroup = (ChipGroup) chipGroupAsView;
        return chipGroup;
    }

    private void addTextToGroup(ChipGroup metaCategoryChipGroup, String text) {
        TextView groupNameView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        groupNameView.setLayoutParams(params);
        groupNameView.setText(text);
        groupNameView.setTextSize(15);
        groupNameView.setTextColor(Color.parseColor("#000000"));
        groupNameView.setPadding(6,6,6,6);
        metaCategoryChipGroup.addView(groupNameView);
    }

    private void createChip(String name, ChipGroup group) {
        Chip chip = new Chip(this);
        ChipGroup.LayoutParams params = new ChipGroup.LayoutParams(
                ChipGroup.LayoutParams.WRAP_CONTENT, ChipGroup.LayoutParams.WRAP_CONTENT);

        chip.setLayoutParams(params);
        chip.setText(name);
        chip.setTextSize(15);
        // todo: style and id
        group.addView(chip);
    }

    private ChipGroup createMetaCategoryChipGroup() {
        // todo: id?
        ChipGroup metaCategoryChipGroup = new ChipGroup(this);
        ChipGroup.LayoutParams params = new ChipGroup.LayoutParams(
                ChipGroup.LayoutParams.MATCH_PARENT, ChipGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(5,5,5,5);
        metaCategoryChipGroup.setLayoutParams(params);
        metaCategoryChipGroup.setChipSpacing(25);
        metaCategoryChipGroup.setSingleLine(true);
        metaCategoryChipGroup.setSelectionRequired(true);
        return metaCategoryChipGroup;
    }

    private void initiateDataMembers() {
        mEdTextTitle = findViewById(R.id.item_title_text);
        mEdTextDescription = findViewById(R.id.item_description_text);
        giveButton = findViewById(R.id.give_button);
        activityRootLinearLayout = findViewById(R.id.activity_root_linear_layout);
    }

    private void createActionBar() {
        View view = findViewById(R.id.chosen_view);
        view.setVisibility(View.INVISIBLE);

        Button givePlusButton = findViewById(R.id.give_icon);
        givePlusButton.setBackground(getResources().getDrawable(R.drawable.ic_give_colored));
    }

    private void createChipGroups() {
        createCategoryGroup();
        createMetaCategoryGroup();
    }

    private void createCategoryGroup() {
        ChipGroup categoryGroup = findViewById(R.id.category_group);
        categoryGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {

                Chip chip = chipGroup.findViewById(i);
                if (chip != null)
                    Toast.makeText(getApplicationContext(), "Chip is " + chip.getText(), Toast.LENGTH_SHORT).show();


            }
        });
    }

    private void createMetaCategoryGroup() {
        ChipGroup demographicGroup = findViewById(R.id.demographic_group);
        demographicGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {

                Chip chip = chipGroup.findViewById(i);
                if (chip != null)
                    Toast.makeText(getApplicationContext(), "Chip is " + chip.getText(), Toast.LENGTH_SHORT).show();


            }
        });
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
                    .centerCrop().fit().into(mImageButtonUpload);

        }
    }


    private Map<String, List<String>> getProductProperties() {
        Map<String, List<String>> properties = new HashMap<>();
        // todo: add actual list? WHY LIST?
        properties.put("Size", (new LinkedList<String>()));
        properties.put("Category", new LinkedList<String>());
        properties.put("Demographic", new LinkedList<String>());
        return properties;
    }


    public void onClickGive(View view) {
        String mItemTitle;
        if (mEdTextTitle != null) {
            mItemTitle = mEdTextTitle.getText().toString();
        } else {
            mItemTitle = "No title written";
        }
        String mItemDescription;
        if (mEdTextDescription != null) {
            mItemDescription = mEdTextDescription.getText().toString();
        } else {
            mItemDescription = "No item Description";
        }
        Date date = Calendar.getInstance().getTime();
        Map<String, List<String>> properties = getProductProperties();
        List<String> imagesUrls = new LinkedList<>();
        if (mImageUri != null) {
            imagesUrls.add(mImageUri.toString());  // todo: should prompt a message that you must take a picture
        }
        // todo: change to real id

        Product product = new Product(64, 3, "sellerID", mItemTitle, mItemDescription,
                0, 0, date, properties, imagesUrls);
        // Now serializing should start, of product (and user)

        // Thank you message
        thankYouMessage = new Dialog(this);
        thankYouMessage.setContentView(R.layout.thank_you_message);
        thankYouMessage.show();
    }

    public void done(View view) {
        thankYouMessage.dismiss();
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }


}