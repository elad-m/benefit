package com.benefit.ui.fragments;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.benefit.R;
import com.benefit.model.Product;
import com.benefit.model.User;
import com.benefit.services.ProductService;
import com.benefit.services.UserService;
import com.benefit.utilities.Factory;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

public class ProductFragment extends Fragment {


    public static ProductFragment newInstance(Product product, User user) {
        ProductFragment fragment = new ProductFragment();
        fragment.product = product;
        fragment.user = user;
        return fragment;
    }

    private Product product;
    private User user;
    private ProductService productService;
    private UserService userService;

    private GoogleMap mMap;
    private Location mapLocation;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.productService = ViewModelProviders.of(this, Factory.getProductServiceFactory()).get(ProductService.class);
        this.userService = ViewModelProviders.of(this, Factory.getUserServiceFactory()).get(UserService.class);
        if (savedInstanceState != null){
            getProduct(savedInstanceState);
        }
//        final Observer<Product> productObserver = this::displayProductOnPage;
//        productService.getProductById(this.productId).observe(this, productObserver);
        displayProductOnPage(product);
    }

    private void getProduct(Bundle savedInstanceState){
        product = (Product) savedInstanceState.getSerializable("product");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void displayProductOnPage(Product product) {
        TextView textViewTitle = getActivity().findViewById(R.id.textviewProductPageTitle);
        TextView textViewDescription = getActivity().findViewById(R.id.textviewProductPageDescription);
        TextView textViewProperties = getActivity().findViewById(R.id.textviewProductPageProperties);
        ImageView imageView = getActivity().findViewById(R.id.prpImagePlaceholder);
        textViewTitle.setText(product.getTitle());
        textViewDescription.setText(product.getDescription());
        textViewProperties.setText(getPropertiesString(product.getProperties()));
        Picasso.get().load(product.getImagesUrls().get(0)).into(imageView);
        final Observer<User> userObserver = this::displayMap;
        userService.getUserById(product.getSellerId()).observe(this, userObserver);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getPropertiesString(Map<String, List<String>> propertiesMap) {
        StringBuilder propertiesString = new StringBuilder();
        for (String name : propertiesMap.keySet()) {
            propertiesString
                    .append(name)
                    .append(":")
                    .append(" ")
                    .append(String.join(getString(R.string.productPagePropertiesValuesSeparator) + " ", propertiesMap.get(name)));
            propertiesString
                    .append(" ")
                    .append(getString(R.string.productPagePropertiesSeparator))
                    .append(" ");
        }
        return propertiesString.toString();
    }

    private void displayMap(User user) {
        this.mapLocation = new Location(LocationManager.GPS_PROVIDER);
        this.mapLocation.setLongitude(user.getLocationLongitude());
        this.mapLocation.setLatitude(user.getLocationLatitude());
        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.productPageMapFragment);
//        Objects.requireNonNull(mapFragment).getMapAsync(this);

    }

    //    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        LatLng coordinates = new LatLng(mapLocation.getLatitude(), mapLocation.getLongitude());
        mMap.addMarker(new MarkerOptions()
                .position(coordinates)
                .title("Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 15));
    }

    @Override
    public void startActivity(Intent intent) {
        intent.putExtra(getString(R.string.user_relay), user);
        super.startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("product", product);

    }


}
