package com.benefit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.benefit.R;
import com.benefit.model.Chat;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

/**
 * An adapter for chat queries
 */
public class ChatAdapter extends FirestoreRecyclerAdapter<Chat, ChatAdapter.ChatMassageHolder> {

    static class ChatMassageHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;

        ChatMassageHolder(View v) {
            super(v);
        }
    }

    public ChatAdapter(@NonNull FirestoreRecyclerOptions<Chat> options) {
        super(options);
    }

    @NonNull
    @Override
    public ChatMassageHolder onCreateViewHolder(@NonNull ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.chat_massage, group, false);
        return new ChatMassageHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatMassageHolder chatMassageHolder, int i, @NonNull Chat chat) {
        chatMassageHolder.messageTextView.setText(chat.getMassageText());
    }

}
