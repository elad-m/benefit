package com.benefit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.benefit.R;
import com.benefit.model.Chat;
import com.benefit.model.Match;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

/**
 * An adapter for conversation queries
 */
public class ConversationAdapter extends FirestoreRecyclerAdapter<Match, ConversationAdapter.ConversationHolder> {

    static class ConversationHolder extends RecyclerView.ViewHolder {
        TextView productTitle, productOwner, lastChatDate;
        ConversationHolder(View v) {
            super(v);
            productTitle = itemView.findViewById(R.id.product_title);
            productOwner = itemView.findViewById(R.id.owner_name);
            lastChatDate = itemView.findViewById(R.id.last_chat_time);
        }
    }

    public ConversationAdapter(@NonNull FirestoreRecyclerOptions<Match> options) {
        super(options);
    }

    @NonNull
    @Override
    public ConversationHolder onCreateViewHolder(@NonNull ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.conversation, group, false);
        return new ConversationHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ConversationHolder chatMassageHolder, int i, @NonNull Match match) {

    }

}
