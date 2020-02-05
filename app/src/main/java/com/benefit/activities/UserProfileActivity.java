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
import com.benefit.adapters.ProductRecyclerAdapter;
import com.benefit.model.Product;
import com.benefit.model.User;
import com.benefit.services.ProductService;
import com.benefit.services.UserService;
import com.benefit.utilities.Factory;
import com.benefit.utilities.HeaderClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Present to the user its items (i.e. products). User can add, remove, open chat and edit its items.
 */
public class UserProfileActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private ProductRecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ProductService productService;
    private UserService userService;
    private User user;


    ArrayList<Product> products = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        createServices();
        extractExtras();
        createActionBar();
        getUserProducts();

    }


    private void extractExtras() {
        Bundle bundle = getIntent().getExtras();
        String userKey = getString(R.string.user_relay);
        if (bundle != null) {
            Set<String> bundleKeySet = bundle.keySet();
            if (bundleKeySet.contains(userKey)) {
                User userFromExtra = (User) bundle.getSerializable(userKey);
                if (userFromExtra != null) {
                    user = userFromExtra;
                } else {
                    makeToast(this.getString(R.string.intent_extra_null_user_toast));
                }
            } else {
                makeToast(this.getString(R.string.intent_extra_no_user_key_toast));
            }
        } else {
            makeToast(this.getString(R.string.intent_extra_no_bundle_toast));
        }
    }

    private void createServices() {
        this.productService = ViewModelProviders.of(this,
                Factory.getProductServiceFactory()).get(ProductService.class);
        this.userService = ViewModelProviders.of(this,
                Factory.getUserServiceFactory()).get(UserService.class);

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
        setHeaderListeners();
    }

    private void setHeaderListeners() {
        HeaderClickListener.setHeaderListeners(this);
    }

    @Override
    public void startActivity(Intent intent) {
        intent.putExtra(getString(R.string.user_relay), user);
        super.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }


    private void getUserProducts() {
        final List<Product> userProducts = new ArrayList<>();
        final Observer<List<Product>> userProductsObserver = new Observer<List<Product>>() {

            @Override
            public void onChanged(List<Product> observedProducts) {
                userProducts.addAll(observedProducts);
                products.addAll(userProducts);
                buildRecyclerView();
            }
        };
        productService.getProductsBySellerId(user.getUid())
                .observe(this, userProductsObserver);

    }

    private void setGreeting() {
        TextView slogan = findViewById(R.id.slogan);
        ((ViewManager) slogan.getParent()).removeView(slogan);
        TextView userGreeting = findViewById(R.id.user_greeting);
        final Observer<User> userObserver = new Observer<User>() {
            @Override
            public void onChanged(User user) {
                UserProfileActivity.this.user = user;
                String username = user.getNickname();
                userGreeting.setText("Hello " + username + "!");
                userGreeting.setVisibility(View.VISIBLE);
            }
        };
        userService.getUserById(user.getUid()).observe(this, userObserver);

    }


    private void makeToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    private void buildRecyclerView() {
        // recycler itself
        recyclerView = findViewById(R.id.items_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 2);
        adapter = new ProductRecyclerAdapter(products);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ProductRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Product clickedProduct = (Product) view.getTag();
                Intent intent = new Intent(getApplicationContext(), ProductPageActivity.class);
                intent.putExtra("product", clickedProduct);
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(int position, View deleteButtonView) {
                Product productToDelete = getProductFromButtonView(deleteButtonView);
                openDeleteDialog(productToDelete, position);
            }

            @Override
            public void onEditClick(int position, View editButtonView) {
                Product productToEdit = getProductFromButtonView(editButtonView);
                Intent intent = new Intent(getApplicationContext(), GiveItemActivity.class);
                intent.putExtra(getApplicationContext().getString(R.string.product_relay),
                        productToEdit);
                startActivity(intent);
            }
        });
    }


    private Product getProductFromButtonView(View view) {
        View constraintLayout = (View) view.getParent();
        View cardview = (View) constraintLayout.getParent();
        return (Product) cardview.getTag();
    }

    public void removeItem(int position) {
        products.remove(position);
        adapter.notifyItemRemoved(position);
    }

    private void openDeleteDialog(Product productToDelete, int position) {
        long productId = productToDelete.getId();
        String productTitle = productToDelete.getTitle();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(String.format(getString(R.string.deletion_message), productTitle))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.deletion_yes_option),
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        productService.deleteProduct(productId);
                        removeItem(position);
                        makeToast(getString(R.string.item_deleted_toast));
                    }
                })
                .setNegativeButton(getString(R.string.deletion_no_option), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle(this.getString(R.string.alert_dialog_delete_item_title));
        alertDialog.show();
    }


}
