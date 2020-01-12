package com.benefit.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.benefit.ChatActivity;
import com.benefit.R;
import com.benefit.drivers.DatabaseDriver;
import com.benefit.model.Chat;
import com.benefit.model.Match;
import com.benefit.model.Product;
import com.benefit.model.User;
import com.benefit.services.ProductService;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import io.opencensus.resource.Resource;

/**
 * An adapter for conversation queries
 */
public class ConversationAdapter extends FirestoreRecyclerAdapter<Match, ConversationAdapter.ConversationHolder> {

    static class ConversationHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView productTitle, productOwnerName, lastChatDate, giveOrWantText;
        Product product;
        Match match;
        final DateFormat dateFormat = new SimpleDateFormat("h:mm a yy.MM.dd");

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
            this.productTitle.setText(product.getTitle());
            //set picture
        }

        void setProductOwnerName(String productOwner) {
            this.productOwnerName.setText(productOwner);
        }

        void setLastChatDate(Date date){
            lastChatDate.setText(dateFormat.format(date));
        }

        void setMatch(Match match){
            this.match = match;
        }

        @Override
        public void onClick(View v) {
            Context context = v.getContext();
            Intent intent = new Intent(context, ChatActivity.class);

        }
    }

    private DatabaseDriver databaseDriver;

    public ConversationAdapter(@NonNull FirestoreRecyclerOptions<Match> options, DatabaseDriver databaseDriver) {
        super(options);
        this.databaseDriver = databaseDriver;
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
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            conversationHolder.setProduct(document.toObject(Product.class));
                        }
                    }
                });
        if (match.getSellerId().equals(databaseDriver.getAuth().getUid())){
            conversationHolder.setProductOwnerName(Resources.getSystem().getString(R.string.user_item));
        }
        else {
            databaseDriver.getCollectionReferenceByName("users")
                    .whereEqualTo("uid", match.getSellerId())
                    .get()
                    .addOnCompleteListener(task -> {
                       if (task.isSuccessful()){
                           for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                               User sellerUser = document.toObject(User.class);
                               conversationHolder.setProductOwnerName(sellerUser.getFirstName() + " " + sellerUser.getLastName());
                           }
                       }
                    });
        }
    }

}
