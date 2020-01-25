package com.benefit.utilities.staticClasses;

import android.content.Intent;
import android.view.View;

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
    public static void setHeaderListeners(View view) {
        view.findViewById(R.id.give_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGiveActivity(view);
            }
        });

        view.findViewById(R.id.user_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUserProfileActivity(view);
            }
        });

        view.findViewById(R.id.search_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearchActivity(view);
            }
        });

        view.findViewById(R.id.message_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMessageActivity(view);
            }
        });
    }

    private static void startMessageActivity(View view) {
        Intent intent = new Intent(view.getContext(), ConversationActivity.class);
        view.getContext().startActivity(intent);
    }

    private static void startSearchActivity(View view) {
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        view.getContext().startActivity(intent);
    }

    private static void startUserProfileActivity(View view) {
        Intent intent = new Intent(view.getContext(), UserProfileActivity.class);
        view.getContext().startActivity(intent);
    }

    private static void startGiveActivity(View view) {
        Intent intent = new Intent(view.getContext(), GiveItemActivity.class);
        view.getContext().startActivity(intent);
    }
}
