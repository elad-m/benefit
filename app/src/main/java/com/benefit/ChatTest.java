package com.benefit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.benefit.model.Match;
import com.benefit.model.Product;
import com.benefit.model.User;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ChatTest extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "ChatTest";

    private FirebaseFirestore mFirestore;
    private FirebaseUser mFirebaseUser;
    private User user;
    private CollectionReference usersCollectionReference;
    private CollectionReference productCollectionReference;
    private CollectionReference matchCollectionReference;
    private boolean isSignIn;
    private List<AuthUI.IdpConfig> authProviders;
    private Random random;
    private Product randomProduct;
    private Match matchForChat;
    private RecyclerView mMessageRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_test);

        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true);
        // Initialize Firestore
        mFirestore = FirebaseFirestore.getInstance();
        // Initialize Firestore collection references
        usersCollectionReference = mFirestore.collection("users");
        productCollectionReference = mFirestore.collection("products");
        matchCollectionReference = mFirestore.collection("matches");

        isSignIn = false;

        // Choose authentication providers
        authProviders = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        //create random for the test.
        random = new Random();
        //add random product
        productCollectionReference.document("7l0bPsE0fae3DXbF9dhs").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        randomProduct = document.toObject(Product.class);
                    }
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        //initiate chat view
        mMessageRecyclerView = findViewById(R.id.messageRecyclerView);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!isSignIn){
            startSignIn();
            return;
        }
    }

    public void startSignIn(){
        Intent intent =  AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(authProviders)
                .build();

        startActivityForResult(intent, RC_SIGN_IN);
        isSignIn = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            isSignIn = false;

            if (resultCode != RESULT_OK && FirebaseAuth.getInstance().getCurrentUser() == null) {
                startSignIn();
            }
            else {
                mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                Query getUserQuery = usersCollectionReference.whereEqualTo("UID", mFirebaseUser.getUid());
                getUserQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()){
                                Log.d(TAG, "User is not on database. adding user to data base.");
                                user = new User(mFirebaseUser.getUid());
                                usersCollectionReference.add(user);
                            }
                            else {
                                for (QueryDocumentSnapshot document : task.getResult()){
                                    user = document.toObject(User.class);
                                }
                            }

                        } else {
                            Log.d(TAG, "Error getting users documents: ", task.getException());
                        }
                    }
                });
            }
        }
    }

    public void addRandomProduct(View view){
        randomProduct = new Product(random.nextInt(100), random.nextInt(3) + 1, "8LO0hWnFQ10uiBHI7WcE", "test product", "desc", 0,0, null,null);
        productCollectionReference.add(randomProduct);
    }

    public void addMatchToProduct(View view){
        matchForChat = new Match(random.nextInt(100), "8LO0hWnFQ10uiBHI7WcE", mFirebaseUser.getUid(), randomProduct.getId());
        matchCollectionReference.add(matchForChat);
    }

}
