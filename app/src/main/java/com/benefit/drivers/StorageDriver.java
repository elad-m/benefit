package com.benefit.drivers;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
                    .addOnSuccessListener((UploadTask.TaskSnapshot taskSnapshot) -> {
                        Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl();
                        if (downloadUri.isSuccessful()) {
                            resultsLiveData.setValue(downloadUri.getResult().toString());
                        }
                    })
                    .addOnFailureListener(e -> Log.w(TAG, "Error on uploadImage", e));
        }
        return resultsLiveData;
    }
}
