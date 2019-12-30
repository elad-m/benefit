package com.benefit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.benefit.adapters.ChatAdapter;
import com.benefit.model.Chat;
import com.benefit.model.Match;
import com.benefit.model.Product;
import com.benefit.model.User;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
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
    private static final String TEST_PRODUCT_ID = "FdvMPG5w0Af58QgYWo9e";
    private static final String TEST_SELLER_UID = "jHbxY9G5pdO7Qo5k58ulwPsY1fG2";
    private static final int TEST_MATCH_ID = 81;

    private FirebaseFirestore mFirestore;
    private FirebaseUser mFirebaseUser;
    private User user;
    private CollectionReference usersCollectionReference;
    private CollectionReference productCollectionReference;
    private CollectionReference matchCollectionReference;
    private boolean isSignIn;
    private List<AuthUI.IdpConfig> authProviders;
    private Product testProduct;
    private Match matchForChat;
    private DocumentReference matchDocumentReference;
    private RecyclerView mMessageRecyclerView;
    private FirestoreRecyclerOptions<Chat> mMessageRecyclerViewOptions;
    private ChatAdapter mMessageRecyclerViewAdapter;
    private EditText mMessageEditText;
    private Button mSendButton;



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
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build());

        //gat test product
        productCollectionReference.document(TEST_PRODUCT_ID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        testProduct = document.toObject(Product.class);
                    }
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        //add test match
        matchCollectionReference.whereEqualTo("id", TEST_MATCH_ID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()){
                        matchForChat = document.toObject(Match.class);
                        matchDocumentReference = document.getReference();
                        Query chatQuery = matchDocumentReference.collection("chat").orderBy("timestamp", Query.Direction.DESCENDING);
                        mMessageRecyclerViewOptions = new FirestoreRecyclerOptions.Builder<Chat>().setQuery(chatQuery, Chat.class).build();
                        mMessageRecyclerViewAdapter = new ChatAdapter(mMessageRecyclerViewOptions);
                        mMessageRecyclerView.setAdapter(mMessageRecyclerViewAdapter);
                        mMessageRecyclerViewAdapter.startListening();
                    }
                } else {
                    Log.d(TAG, "Error getting users documents: ", task.getException());
                }
            }
        });

        //initiate chat view elements
        mMessageRecyclerView = findViewById(R.id.messageRecyclerView);
        mMessageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMessageEditText = findViewById(R.id.messageEditText);
        mSendButton = findViewById(R.id.sendButton);

        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!isSignIn){
            startSignIn();
            return;
        }
        if (mMessageRecyclerViewAdapter != null) {
            mMessageRecyclerViewAdapter.startListening();
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
    protected void onStop() {
        super.onStop();
        if (mMessageRecyclerViewAdapter != null) {
            mMessageRecyclerViewAdapter.startListening();
        }
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

    public void addMatchToProduct(View view){
        matchForChat = new Match(new Random().nextInt(100), TEST_SELLER_UID, mFirebaseUser.getUid(), testProduct.getId());
        matchCollectionReference.add(matchForChat).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                matchDocumentReference = documentReference;
                if (mMessageRecyclerViewAdapter != null) {
                    mMessageRecyclerViewAdapter.stopListening();
                }
                Query chatQuery = matchDocumentReference.collection("chat").orderBy("timestamp", Query.Direction.DESCENDING);
                mMessageRecyclerViewOptions = new FirestoreRecyclerOptions.Builder<Chat>().setQuery(chatQuery, Chat.class).build();
                mMessageRecyclerViewAdapter = new ChatAdapter(mMessageRecyclerViewOptions);
                mMessageRecyclerView.setAdapter(mMessageRecyclerViewAdapter);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }


    public void sendMassage(View view){
        if (mMessageRecyclerViewAdapter != null) {
            Chat massage = new Chat(matchForChat.getId(), matchForChat.getBuyerId(), matchForChat.getSellerId(), mMessageEditText.getText().toString());
            matchDocumentReference.collection("chat").add(massage);
            mMessageEditText.setText("");
        }
        else {
            Toast.makeText(this, "Error - No match for chat", Toast.LENGTH_SHORT).show();
        }
    }
}
