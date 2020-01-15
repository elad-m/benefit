package com.benefit.drivers;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class StorageDriver extends ViewModel {
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private static final String TAG = "StorageDriver";

    public StorageDriver() {
        this.storage = FirebaseStorage.getInstance();
        this.storageReference = storage.getReference();
    }

    public LiveData<String> uploadImage(Uri imageUri) {
        final MutableLiveData<String> resultsLiveData = new MutableLiveData<>();
        if (imageUri != null) {
            String destFileName = "images/" + System.currentTimeMillis();
            StorageReference ref = this.storageReference.child(destFileName);
            ref.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> resultsLiveData.setValue(destFileName))
                    .addOnFailureListener(e -> Log.w(TAG, "Error on uploadImage", e));
        }
        return resultsLiveData;
    }

    private String getFileExtension(Uri uri) {
        return uri.toString().substring(uri.toString().lastIndexOf("."));
    }
}
