package com.benefit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.benefit.drivers.DatabaseDriver;
import com.benefit.services.ChatService;
import com.benefit.viewmodel.ChatActivityViewModel;

public class ChatActivityMain extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DatabaseDriver databaseDriver;
    private ChatService chatService;
    private ChatActivityViewModel cViewModel;
    private Spinner sortMassagesSpinner;
    private RecyclerView conversationRecyclerView;
    private int sortChoice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);

        databaseDriver = new DatabaseDriver();

        // View model
        cViewModel = ViewModelProviders.of(this).get(ChatActivityViewModel.class);

        //initiate sort spinner
        initiateSortSpinner();



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
}
