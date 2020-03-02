package com.benefit.ui.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.benefit.R;
import com.benefit.activities.EditItemActivity;
import com.benefit.activities.MainActivity;
import com.benefit.adapters.ProductRecyclerAdapter;
import com.benefit.model.Product;
import com.benefit.model.User;
import com.benefit.services.ProductService;
import com.benefit.utilities.Factory;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {


    public static ProfileFragment newInstance(User user) {
        ProfileFragment fragment = new ProfileFragment();
        fragment.user = user;
        return fragment;
    }

    private ProductService productService;
    private RecyclerView recyclerView;
    private ProductRecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout swipeContainer;
    private View fragmentRootView;
    private User user;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        createServices();
        getUserProducts();

    }

    private void createServices() {
        this.productService = ViewModelProviders.of(this,
                Factory.getProductServiceFactory()).get(ProductService.class);
    }

    @Override
    public void startActivity(Intent intent) {
        intent.putExtra(getString(R.string.user_relay), user);
        super.startActivity(intent);
    }


    private void getUserProducts() {
        final List<Product> userProducts = new ArrayList<>();
        final Observer<List<Product>> userProductsObserver = new Observer<List<Product>>() {

            @Override
            public void onChanged(List<Product> observedProducts) {
                userProducts.addAll(observedProducts);
                buildRecyclerView(userProducts);
                setGreeting();
            }
        };
        productService.getProductsBySellerId(user.getUid())
                .observe(this, userProductsObserver);

    }

    private void getUserProductsSwipe() {
        final List<Product> userProducts = new ArrayList<>();
        final Observer<List<Product>> userProductsObserver = new Observer<List<Product>>() {

            @Override
            public void onChanged(List<Product> observedProducts) {
                userProducts.addAll(observedProducts);
                adapter.addAll(userProducts);
                swipeContainer.setRefreshing(false);
            }
        };
        productService.getProductsBySellerId(user.getUid())
                .observe(this, userProductsObserver);

    }

    private void setSwipeContainer(){
        swipeContainer = fragmentRootView.findViewById(R.id.swipe_item_recycler);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                adapter.clear();
                getUserProductsSwipe();
            }
        });
        swipeContainer.setColorSchemeResources(R.color.colorBenefitBlue);

    }


    private void buildRecyclerView(List<Product> products) {
        fragmentRootView = getView();
        setSwipeContainer();

        recyclerView = fragmentRootView.findViewById(R.id.items_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        adapter = new ProductRecyclerAdapter(products);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        setAdapterClickListeners();
    }

    private void setAdapterClickListeners() {
        adapter.setOnItemClickListener(new ProductRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Product clickedProduct = (Product) view.getTag();
                ((MainActivity) getActivity()).startProductFragment(clickedProduct);
            }

            @Override
            public void onDeleteClick(int position, View deleteButtonView) {
                Product productToDelete = getProductFromButtonView(deleteButtonView);
                openDeleteDialog(productToDelete, position);
            }

            @Override
            public void onEditClick(int position, View editButtonView) {
                Product productToEdit = getProductFromButtonView(editButtonView);
                Intent intent = new Intent(getActivity(), EditItemActivity.class);
                intent.putExtra(getActivity().getString(R.string.product_relay), productToEdit);
                startActivity(intent);
            }
        });
    }


    private void setGreeting() {
        TextView userGreeting = fragmentRootView.findViewById(R.id.user_greeting);
        String username = user.getNickname();
        userGreeting.setText("Hello " + username + "!");
        userGreeting.setVisibility(View.VISIBLE);
    }


    private void makeToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }


    private Product getProductFromButtonView(View view) {
        View constraintLayout = (View) view.getParent();
        View cardview = (View) constraintLayout.getParent();
        return (Product) cardview.getTag();
    }

    public void removeItem(int position) {
        adapter.notifyItemRemoved(position);
    }

    private void openDeleteDialog(Product productToDelete, int position) {
        long productId = productToDelete.getId();
        String productTitle = productToDelete.getTitle();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
