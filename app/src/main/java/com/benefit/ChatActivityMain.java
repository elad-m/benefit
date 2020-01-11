package com.benefit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.benefit.drivers.DatabaseDriver;
import com.benefit.model.User;
import com.benefit.services.ChatService;
import com.benefit.services.UserService;

public class ChatActivityMain extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DatabaseDriver databaseDriver;
    private UserService userService;
    private ChatService chatService;
    private User currentUser;
    private Spinner sortMassagesSpinner;
    private RecyclerView conversationRecyclerView;
    private int sortChoice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);

        databaseDriver = new DatabaseDriver();


        // initiate user
        userService = ViewModelProviders.of(this).get(UserService.class);
        userService.getCurrentUser().observe(this, user -> {
                    currentUser = user;
                    Toast.makeText(this,"welcome user " + currentUser.getFirstName() + "!", Toast.LENGTH_LONG).show();
                });

        //initiate sort spinner
        initiateSortSpinner();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (userService.shouldSignIn()){
            userService.startSignIn(this);
        }
    }

    private void initiateSortSpinner(){
        sortMassagesSpinner = findViewById(R.id.sort_spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.chat_sort_options, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortMassagesSpinner.setAdapter(spinnerAdapter);
        sortMassagesSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        parent.getItemAtPosition(position);
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
