package com.benefit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class OfferItem extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAPTURE_PHOTO = 2;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    private static final int STORAGE_PERMISSION_CODE = 3;
    private static final int CAMERA_AND_GALLERY_PERMISSION_CODE = 10;

    private ImageButton mImageButtonUpload;
    private Uri mImageUri;

    RadioGroup sizeRadioGroup;
    RadioButton sizeRadioButton;
    RadioGroup categoryRadioGroup;
    RadioButton categoryRadioButton;
    RadioGroup clothinDemographicRadioGroup;
    RadioButton clothinDemographicRadioButton;

    EditText mEdTextTitle;
    EditText mEdTextDescription;

    private String mItemTitle;
    private String mItemDescription;

    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_item);

        sizeRadioGroup = findViewById(R.id.item_size_radio_group);
        categoryRadioGroup = findViewById(R.id.item_category_radio_group);
        clothinDemographicRadioGroup = findViewById(R.id.item_clothing_demographic_radio_group);

        mEdTextTitle = findViewById(R.id.item_title_text);
        mEdTextDescription = findViewById(R.id.item_description_text);

        mImageButtonUpload = findViewById(R.id.image_button_choose_image);

    }

    public void openFileChooser(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, CAPTURE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PICK_IMAGE_REQUEST:
                loadImageFromGallery(resultCode, data);
                break;
            case CAPTURE_PHOTO:
                takePhotoWithCamera(resultCode, data);
                break;
            default:
                Log.d("ImplicitIntents", "Can't handle this intent!");
                break;
        }
    }


    public void setSizeRadioButton(View v) {
        int radioId = sizeRadioGroup.getCheckedRadioButtonId();
        sizeRadioButton = findViewById(radioId);

        Toast.makeText(this, "selected size: " + sizeRadioButton.getText(),
                Toast.LENGTH_SHORT).show();
    }

    public void setCategoryRadioButton(View v) {
        int radioId = categoryRadioGroup.getCheckedRadioButtonId();
        categoryRadioButton = findViewById(radioId);

        Toast.makeText(this, "selected category: " + categoryRadioButton.getText(),
                Toast.LENGTH_SHORT).show();
    }


    public void setClothinDemographicRadioButton(View v) {
        int radioId = clothinDemographicRadioGroup.getCheckedRadioButtonId();
        clothinDemographicRadioButton = findViewById(radioId);

        Toast.makeText(this, "selected for: " + clothinDemographicRadioButton.getText(),
                Toast.LENGTH_SHORT).show();
    }


    private void loadImageFromGallery(int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            if (mImageButtonUpload.getDrawable() == null) {
                Picasso.get().load(mImageUri).into(mImageButtonUpload);
            }
        }
    }

    private void takePhotoWithCamera(int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            if (mImageButtonUpload.getDrawable() == null) {
                Picasso.get().load(mImageUri).into(mImageButtonUpload);
            }
        }
    }


    public void createItem(View view) {
        //TODO: implement this, mainly taking all data members and sending to server
        // the next lines are just to see the text is saved
        mItemTitle = mEdTextTitle.getText().toString();
        mItemDescription = mEdTextDescription.getText().toString();
    }


}