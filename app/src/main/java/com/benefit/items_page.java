package com.benefit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class items_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_page);
    }


    public void openFilter(View view) {
        int popupWidth = (int) (getResources().getDisplayMetrics().widthPixels / 1.2);
        int popupHeight = (int) (getResources().getDisplayMetrics().heightPixels / 1.1);
        // Inflate the popup_layout.xml
        LinearLayout viewGroup =  findViewById(R.id.filter_popup);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.filter, viewGroup);
        // Creating the PopupWindow
        final PopupWindow popup = new PopupWindow(this);
        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);
        popup.setFocusable(true);
        // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
        int OFFSET_X = 0;
        int OFFSET_Y = 0;

        popup.showAtLocation(layout, Gravity.RIGHT, OFFSET_X, OFFSET_Y);

        Button reset =  layout.findViewById(R.id.reset_button);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });

        Button done = layout.findViewById(R.id.done_button);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });
    }

    public void openSort(View view) {
        int popupWidth = StaticFunctions.convertDpToPx(150);
        int popupHeight = StaticFunctions.convertDpToPx(200);
        // Inflate the popup_layout.xml
        LinearLayout viewGroup =  findViewById(R.id.sort_popup);
        int[] location = new int[2];
        findViewById(R.id.sort).getLocationOnScreen(location);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.sort, viewGroup);
        // Creating the PopupWindow
        final PopupWindow popup = new PopupWindow(this);
        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);
        popup.setFocusable(true);
        // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
        int OFFSET_X = location[0];
        int OFFSET_Y = location[1] +  StaticFunctions.convertDpToPx(50);
        // Clear the default translucent background
//        popup.setBackground (new BitmapDrawable());
        // Displaying the popup at the specified location, + offsets.
        popup.showAtLocation(layout, Gravity.NO_GRAVITY, OFFSET_X, OFFSET_Y);
        // Getting a reference to Close button, and close the popup when clicked.
//        Button reset =  layout.findViewById(R.id.reset_button);
//        reset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                popup.dismiss();
//            }
//        });
//
//        Button done = layout.findViewById(R.id.done_button);
//        done.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                popup.dismiss();
//            }
//        });
    }

}
