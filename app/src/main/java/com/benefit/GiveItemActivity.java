package com.benefit;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.benefit.model.Product;
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

    private ImageButton mImageButtonUpload;
    private Uri mImageUri;


    private ChipGroup categoryGroup;
    private ChipGroup demographicGroup;

    private EditText mEdTextTitle;
    private EditText mEdTextDescription;

    private String mItemTitle;
    private String mItemDescription;

    private Dialog mThankYouMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_item);

        mEdTextTitle = findViewById(R.id.item_title_text);

        mEdTextDescription = findViewById(R.id.item_description_text);

        mImageButtonUpload = findViewById(R.id.image_button_choose_image);

        createChipGroups();
    }


    private void createChipGroups() {
        createCateforyGroup();
        createDemographicGroup();
    }

    private void createCateforyGroup() {
        categoryGroup = findViewById(R.id.category_group);
        categoryGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {

                Chip chip = chipGroup.findViewById(i);
                if (chip != null)
                    Toast.makeText(getApplicationContext(), "Chip is " + chip.getText(), Toast.LENGTH_SHORT).show();


            }
        });
    }

    private void createDemographicGroup() {
        demographicGroup = findViewById(R.id.demographic_group);
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
        if (mEdTextTitle != null) {
            mItemTitle = mEdTextTitle.getText().toString();
        } else {
            mItemTitle = "No title written";
        }
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
        mThankYouMessage = new Dialog(this);
        mThankYouMessage.setContentView(R.layout.thank_you_message);
        mThankYouMessage.show();
    }

    public void done(View view) {
        mThankYouMessage.dismiss();
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }


}