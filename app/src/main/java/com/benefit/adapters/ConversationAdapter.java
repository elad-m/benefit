package com.benefit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.benefit.R;
import com.benefit.drivers.AuthenticationDriver;
import com.benefit.drivers.DatabaseDriver;
import com.benefit.model.Match;
import com.benefit.model.Product;
import com.benefit.model.User;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * An adapter for conversation queries
 */
public class ConversationAdapter extends FirestoreRecyclerAdapter<Match, ConversationAdapter.ConversationHolder> {

    static class ConversationHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView productTitle, productOwnerName, lastChatDate, giveOrWantText;
        private Product product;
        private Match match;
        private final DateFormat dateFormat = new SimpleDateFormat("h:mm a yy.MM.dd");

        ConversationHolder(View v) {
            super(v);
            productTitle = itemView.findViewById(R.id.product_title);
            productOwnerName = itemView.findViewById(R.id.owner_name);
            lastChatDate = itemView.findViewById(R.id.last_chat_time);
            giveOrWantText = itemView.findViewById(R.id.give_or_want);
            itemView.setOnClickListener(this);
        }

        void setProduct(Product product) {
            this.product = product;
            this.productTitle.setText(product.getName());
            //set picture
        }

        void setProductOwnerName(String productOwner) {
            this.productOwnerName.setText(productOwner);
        }

        void setLastChatDate(Date date) {
            lastChatDate.setText(dateFormat.format(date));
        }

        void setMatch(Match match) {
            this.match = match;
        }

        @Override
        public void onClick(View v) {

        }
    }

    private DatabaseDriver databaseDriver;
    private AuthenticationDriver authenticationDriver;

    public ConversationAdapter(@NonNull FirestoreRecyclerOptions<Match> options, DatabaseDriver databaseDriver, AuthenticationDriver authenticationDriver) {
        super(options);
        this.databaseDriver = databaseDriver;
        this.authenticationDriver = authenticationDriver;
    }

    @NonNull
    @Override
    public ConversationHolder onCreateViewHolder(@NonNull ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.conversation, group, false);

        return new ConversationHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ConversationHolder conversationHolder, int i, @NonNull Match match) {
        conversationHolder.setMatch(match);
        conversationHolder.setLastChatDate(match.getTimestamp());
        databaseDriver.getCollectionReferenceByName("products")
                .whereEqualTo("id", match.getProductId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            conversationHolder.setProduct(document.toObject(Product.class));
                        }
                    }
                });
        if (match.getSellerId().equals(authenticationDriver.getUserUid())) {
            conversationHolder.setProductOwnerName(conversationHolder.itemView.getContext().getString(R.string.user_item));
        } else {
            databaseDriver.getCollectionReferenceByName("users")
                    .whereEqualTo("uid", match.getSellerId())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                User sellerUser = document.toObject(User.class);
                                conversationHolder.setProductOwnerName(sellerUser.getNickname());
                            }
                        }
                    });
        }
    }

}
