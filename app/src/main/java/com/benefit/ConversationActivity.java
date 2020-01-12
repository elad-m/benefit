package com.benefit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.benefit.adapters.ConversationAdapter;
import com.benefit.drivers.DatabaseDriver;
import com.benefit.model.User;
import com.benefit.services.ChatService;
import com.benefit.services.UserService;

public class ConversationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DatabaseDriver databaseDriver;
    private UserService userService;
    private ChatService chatService;
    private User currentUser;
    private Spinner sortMassagesSpinner;
    private RecyclerView conversationRecyclerView;
    private ConversationAdapter conversationAdapter;
    private int sortChoice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        databaseDriver = new DatabaseDriver();


        // initiate user
        userService = ViewModelProviders.of(this).get(UserService.class);
        userService.getCurrentUser().observe(this, user -> {
            currentUser = user;
            Toast.makeText(this, "welcome user " + currentUser.getFirstName() + "!", Toast.LENGTH_LONG).show();
            //initiate conversation RecyclerView
            initiateConversationRecyclerView();
        });

        //initiate sort spinner
        initiateSortSpinner();

        chatService = ViewModelProviders.of(this).get(ChatService.class);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (userService.shouldSignIn()) {
            userService.startSignIn(this);
            return;
        }

        if(conversationAdapter != null){
            conversationAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(conversationAdapter !=null){
            conversationAdapter.stopListening();
        }
    }

    private void initiateSortSpinner() {
        sortMassagesSpinner = findViewById(R.id.sort_spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.chat_sort_options, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortMassagesSpinner.setAdapter(spinnerAdapter);
        sortMassagesSpinner.setOnItemSelectedListener(this);
    }

    private void initiateConversationRecyclerView() {
        conversationAdapter = chatService.getConversationAdaptor(true, true, true);
        conversationRecyclerView = findViewById(R.id.conversation_recyclerView);
        conversationRecyclerView.setAdapter(conversationAdapter);
        conversationAdapter.startListening();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                conversationRecyclerView.setAdapter(chatService.getConversationAdaptor(false, true, true));
                break;
            case 1:
                conversationRecyclerView.setAdapter(chatService.getConversationAdaptor(true, false, true));
                break;
            case 2:
                conversationRecyclerView.setAdapter(chatService.getConversationAdaptor(true, true, true));
                break;
            case 3:
                conversationRecyclerView.setAdapter(chatService.getConversationAdaptor(true, true, false));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        userService.handleOnSignInResult(requestCode, resultCode, data, this);
    }
}
