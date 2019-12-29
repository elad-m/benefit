package com.benefit;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OfferItem extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAPTURE_PHOTO = 2;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    private ImageButton mImageButtonUpload1;
    private ImageButton mImageButtonUpload2;
    private ImageButton mImageButtonUpload3;
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

        mEdTextTitle =  findViewById(R.id.item_title_text);
        mEdTextDescription =  findViewById(R.id.item_description_text);

        mImageButtonUpload1 = findViewById(R.id.image_button_choose_image1);
        mImageButtonUpload2 = findViewById(R.id.image_button_choose_image2);
        mImageButtonUpload3 = findViewById(R.id.image_button_choose_image3);

        // TODO: Have the sizes loaded programatically, they depend on the item category (shoes, pants)
        // TODO: Add permissions to camera, fix the horrible scaling (Picasso?)

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


    public void openFileChooser(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    public void startDialog(View v) {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
        myAlertDialog.setTitle("Upload Pictures Option");
        myAlertDialog.setMessage("How do you want to set your picture?");

        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent pictureActionIntent = null;

                        pictureActionIntent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult( pictureActionIntent, PICK_IMAGE_REQUEST);
                    }
                });

        myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {

                    @RequiresApi(api = Build.VERSION_CODES.M)
                    public void onClick(DialogInterface arg0, int arg1) {
//                        Log.d("NAME PARENT ACTIVITY" ,getParent().getLocalClassName());
                        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                        {
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                        } else {
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                                // Create the File where the photo should go
                                File photoFile = null;
                                try {
                                    photoFile = createImageFile();
                                } catch (IOException ex) {
                                    // Error occurred while creating the File
                                    Log.i(ex.getMessage(), "IOException");
                                }
                                // Continue only if the File was successfully created
                                if (photoFile != null) {
                                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                                    startActivityForResult(cameraIntent, CAPTURE_PHOTO);
                                }
                            }
                        }

                    }
                });
        myAlertDialog.show();
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getApplicationContext().getExternalFilesDir( Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
//    {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == MY_CAMERA_PERMISSION_CODE)
//        {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
//            {
//                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
//                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, CAPTURE_PHOTO);
//            }
//            else
//            {
//                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
//            }
//        }
//    }

    private void loadImageFromGallery(int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            if (mImageButtonUpload2.getDrawable() == null) {
                Picasso.get().load(mImageUri).into(mImageButtonUpload2);
            } else {
                if (mImageButtonUpload1.getDrawable() == null) {
                    Picasso.get().load(mImageUri).into(mImageButtonUpload1);
                } else {
                    Picasso.get().load(mImageUri).into(mImageButtonUpload3);
                }
            }
        }
    }

    private void takePhotoWithCamera(int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            if (mImageButtonUpload2.getDrawable() == null) {
                Picasso.get().load(mImageUri).into(mImageButtonUpload2);
            } else {
                if (mImageButtonUpload1.getDrawable() == null) {
                    Picasso.get().load(mImageUri).into(mImageButtonUpload1);
                } else {
                    Picasso.get().load(mImageUri).into(mImageButtonUpload3);
                }
            }
        }
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


    public void createItem(View view) {
        //TODO: implement this, mainly taking all data members and sending to server
        // the next lines are just to see the text is saved
        mItemTitle = mEdTextTitle.getText().toString();
        mItemDescription= mEdTextDescription.getText().toString();

    }
}