package com.benefit.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.benefit.R;
import com.benefit.ui.profile.ClothingItem;
import com.benefit.adapters.ClothingRecyclerAdapter;
import com.benefit.model.Product;
import com.benefit.model.User;
import com.benefit.services.ProductService;
import com.benefit.services.UserService;
import com.benefit.utilities.Factory;

import java.util.ArrayList;
import java.util.List;

/**
 * Present to the user its items (i.e. products). User can add, remove, open chat and edit its items.
 */
public class UserProfileActivity extends AppCompatActivity {

    private String userIdTesting = "jHbxY9G5pdO7Qo5k58ulwPsY1fG2";

    private RecyclerView mRecyclerView;
    private ClothingRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProductService productService;
    private UserService userService;


    ArrayList<ClothingItem> mClothingItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        createServices();
        createActionBar();
        getUserProducts();

    }


    private void getUserProducts() {
        final List<Product> userProducts = new ArrayList<>();
        final Observer<List<Product>> userProductsObserver = new Observer<List<Product>>() {

            @Override
            public void onChanged(List<Product> products) {
                userProducts.addAll(products);
                for (Product product : products) {
                    if (product.getImagesUrls() != null) {
                        if (!product.getImagesUrls().isEmpty()) {
                            mClothingItems.add(new ClothingItem(product.getImagesUrls().get(0),
                                    product.getName(),
                                    product.getId()));
                        }
                    }
                }
                buildRecyclerView();
            }
        };
        productService.getProductsBySellerId(userIdTesting)
                .observe(this, userProductsObserver);

    }

    private void setGreeting() {
        TextView slogan = findViewById(R.id.slogan);
        ((ViewManager) slogan.getParent()).removeView(slogan);
        TextView userGreeting = findViewById(R.id.user_greeting);
        userGreeting.setText("Hello You!");
        final Observer<User> userObserver = new Observer<User>() {
            @Override
            public void onChanged(User user) {
                String username = user.getFirstName();
                userGreeting.setText("Hello " + username + "!");
                userGreeting.setVisibility(View.VISIBLE);
            }
        };

    }

    private void createActionBar() {
        ConstraintLayout constraintLayout = findViewById(R.id.user_profile_page_header);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.connect(R.id.chosen_view, ConstraintSet.RIGHT, R.id.user_icon, ConstraintSet.RIGHT);
        constraintSet.connect(R.id.chosen_view, ConstraintSet.LEFT, R.id.user_icon, ConstraintSet.LEFT);
        constraintSet.applyTo(constraintLayout);

        Button userIcon = findViewById(R.id.user_icon);
        userIcon.setBackground(getResources().getDrawable(R.drawable.ic_user_colored));

        setGreeting();
        setActionBarOnClicks();
    }


    private void setActionBarOnClicks() {
        Button giveItemButton = findViewById(R.id.give_icon);
        Button searchButton = findViewById(R.id.search_icon);
        Button chatButton = findViewById(R.id.message_icon);
        Button userButton = findViewById(R.id.user_icon);

        giveItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GiveItemActivity.class);
                startActivity(intent);
            }
        });

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(intent);
            }
        });

        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
                startActivity(intent);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void makeToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    private void buildRecyclerView() {
        // recycler itself
        mRecyclerView = findViewById(R.id.items_recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this, 2);
        mAdapter = new ClothingRecyclerAdapter(mClothingItems);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ClothingRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                long productId = ((ClothingItem) view.getTag()).getmProductId();
                Intent intent = new Intent(getApplicationContext(), ProductPageActivity.class);
                intent.putExtra("productId", productId);
                startActivity(intent);

            }

            @Override
            public void onDeleteClick(int position, View deleteButtonView) {
                ClothingItem itemToDelete = getClothingItemFromButtonView(deleteButtonView);
                openDeleteDialog(itemToDelete, position);
            }

            @Override
            public void onEditClick(int position, View editButtonView) {
                long productId = getClothingItemFromButtonView(editButtonView).getmProductId();
                Intent intent = new Intent(getApplicationContext(), GiveItemActivity.class);
                intent.putExtra(getApplicationContext().getString(R.string.product_id_extras_key), productId);
                startActivity(intent);
            }
        });

    }


    private ClothingItem getClothingItemFromButtonView(View view) {
        View constraintLayout = (View) view.getParent();
        View cardview = (View) constraintLayout.getParent();
        return (ClothingItem) cardview.getTag();
    }

    public void removeItem(int position) {
        mClothingItems.remove(position);
        mAdapter.notifyItemRemoved(position);
    }

    private void openDeleteDialog(ClothingItem clothingItem, int position) {
        long productId = clothingItem.getmProductId();
        String productTitle = clothingItem.getmTitle();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete \"" + productTitle + "\" ?")
                .setCancelable(false)
                .setPositiveButton("Yes, I am sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        productService.deleteProduct(productId);
                        removeItem(position);
                        makeToast(getString(R.string.item_deleted_toast));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle(this.getString(R.string.alert_dialog_delete_item_title));
        alertDialog.show();
    }

    private void createServices() {
        this.productService = ViewModelProviders.of(this,
                Factory.getProductServiceFactory()).get(ProductService.class);
        this.userService = ViewModelProviders.of(this,
                Factory.getUserServiceFactory()).get(UserService.class);

    }
}
