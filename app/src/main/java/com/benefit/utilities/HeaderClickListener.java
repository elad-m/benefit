package com.benefit.utilities;

import android.content.Intent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.benefit.R;
import com.benefit.activities.ConversationActivity;
import com.benefit.activities.GiveItemActivity;
import com.benefit.activities.MainActivity;
import com.benefit.activities.UserProfileActivity;

/**
 * A static class that has all of the header listeners
 */
public class HeaderClickListener {

    /**
     * sets the on click listeners for the header
     */
    public static void setHeaderListeners(AppCompatActivity activity) {
        activity.findViewById(R.id.give_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGiveActivity(activity);
            }
        });

        activity.findViewById(R.id.user_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUserProfileActivity(activity);
            }
        });

        activity.findViewById(R.id.search_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearchActivity(activity);
            }
        });

        activity.findViewById(R.id.message_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMessageActivity(activity);
            }
        });
    }

    private static void startMessageActivity(AppCompatActivity activity) {
        Intent intent = new Intent(activity, ConversationActivity.class);
        activity.startActivity(intent);
    }

    private static void startSearchActivity(AppCompatActivity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    private static void startUserProfileActivity(AppCompatActivity activity) {
        Intent intent = new Intent(activity, UserProfileActivity.class);
        activity.startActivity(intent);
    }

    private static void startGiveActivity(AppCompatActivity activity) {
        Intent intent = new Intent(activity, GiveItemActivity.class);
        activity.startActivity(intent);
    }
}
