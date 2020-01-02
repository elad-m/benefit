package com.benefit;

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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class OfferItem extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;

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


    public void createItem(View view) {

        // the next lines are just to see the text is saved
        mItemTitle = mEdTextTitle.getText().toString();
        mItemDescription = mEdTextDescription.getText().toString();
    }


}