package com.benefit.ui.fragments;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.benefit.R;
import com.benefit.model.Product;
import com.benefit.model.User;
import com.benefit.services.UserService;
import com.benefit.utilities.Factory;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

public class ProductFragment extends Fragment implements OnMapReadyCallback {


    public static ProductFragment newInstance(Product product, User user) {
        ProductFragment fragment = new ProductFragment();
        fragment.product = product;
        fragment.user = user;
        return fragment;
    }

    private Product product;
    private User user;
    private UserService userService;
    private static final String TAG = "ProductPageActivity";
    MapView mapView;
    private GoogleMap mMap;
    private Location mapLocation;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product, container, false);
        mapView = v.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.userService = ViewModelProviders.of(this, Factory.getUserServiceFactory()).get(UserService.class);
        if (savedInstanceState != null) {
            getProduct(savedInstanceState);
        }
        displayProductOnPage(product);
        ImageView backButton = getActivity().findViewById(R.id.product_page_back_arrow);
        backButton.setOnClickListener(v -> getActivity().onBackPressed());
    }

    private void getProduct(Bundle savedInstanceState) {
        product = (Product) savedInstanceState.getSerializable("product");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void displayProductOnPage(Product product) {
        TextView textViewTitle = getActivity().findViewById(R.id.textviewProductPageTitle);
        TextView textViewDescription = getActivity().findViewById(R.id.textviewProductPageDescription);
        TextView textViewProperties = getActivity().findViewById(R.id.textviewProductPageProperties);
        ImageView imageView = getActivity().findViewById(R.id.prpImagePlaceholder);

        LinearLayout backButton = getView().findViewById(R.id.product_page_back_button);
        backButton.setVisibility(View.INVISIBLE);

        textViewTitle.setText(product.getTitle());
        textViewDescription.setText(product.getDescription());
        textViewProperties.setText(getPropertiesString(product.getProperties()));
        Picasso.get().load(product.getImagesUrls().get(0)).into(imageView);
        final Observer<User> userObserver = this::userActions;
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

    private void userActions(User user) {
        this.mapLocation = new Location(LocationManager.GPS_PROVIDER);
        this.mapLocation.setLongitude(user.getLocationLongitude());
        this.mapLocation.setLatitude(user.getLocationLatitude());
        setMapLocation(mapLocation.getLatitude(), mapLocation.getLongitude());
        final Button contactGiverButton = getView().findViewById(R.id.contact_giver_button);
        contactGiverButton.setOnClickListener(v -> openSms(user.getPhoneNumber()));

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        setMapLocation(0, 0);
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

    private void openSms(String numero) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("smsto:" + numero)); // This ensures only SMS apps respond
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public void setMapLocation(double latitude, double longitude) {
        LatLng coordinates = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions()
                .position(coordinates)
                .title("Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 15));
    }

}
