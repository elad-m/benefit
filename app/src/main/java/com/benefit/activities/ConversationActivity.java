package com.benefit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.benefit.R;
import com.benefit.drivers.DatabaseDriver;
import com.benefit.model.User;
import com.benefit.services.ChatService;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;

public class ConversationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DatabaseDriver databaseDriver;
    private ChatService chatService;
    private User currentUser;
    private Spinner sortMassagesSpinner;
    private RecyclerView conversationRecyclerView;
    private FirestoreRecyclerAdapter conversationAdapter;
    private int sortChoice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        setHeaderListeners();

        databaseDriver = new DatabaseDriver();


        // initiate user
        currentUser = (User) getIntent().getSerializableExtra(getString(R.string.user_relay));

        //initiate sort spinner
        initiateSortSpinner();

        chatService = ViewModelProviders.of(this).get(ChatService.class);
        chatService.setUser(currentUser);

        //initiate conversation RecyclerView
        initiateConversationRecyclerView();

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (conversationAdapter != null) {
            conversationAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (conversationAdapter != null) {
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
        conversationAdapter = chatService.getConversationRecyclerViewAdaptor(true, true, true);
        conversationRecyclerView = findViewById(R.id.conversation_recyclerView);
        conversationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        conversationRecyclerView.setAdapter(conversationAdapter);
        conversationAdapter.startListening();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (conversationRecyclerView != null) {
            switch (position) {
                case 0:
                   conversationAdapter = chatService.getConversationRecyclerViewAdaptor(false, true, true);
                    break;
                case 1:
                    conversationAdapter = chatService.getConversationRecyclerViewAdaptor(true, false, true);
                    break;
                case 2:
                    conversationAdapter = chatService.getConversationRecyclerViewAdaptor(true, true, true);
                    break;
                case 3:
                    conversationAdapter = chatService.getConversationRecyclerViewAdaptor(true, true, false);
            }
            conversationRecyclerView.setAdapter(conversationAdapter);
            conversationAdapter.startListening();

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setHeaderListeners() {

    }
}