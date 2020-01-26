package com.benefit.activities;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.benefit.R;
import com.benefit.model.Product;
import com.benefit.model.User;
import com.benefit.utilities.HeaderClickListener;
import com.benefit.utilities.Factory;
import com.benefit.services.ProductService;
import com.benefit.services.UserService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * An activity for the product page
 */
public class ProductPageActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Product product;
    private User user;
    private ProductService productService;
    private UserService userService;

    private GoogleMap mMap;
    private Location mapLocation;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extractExtras();
        setContentView(R.layout.activity_product_page);
        findViewById(R.id.search_icon).setBackground(getResources().getDrawable(R.drawable.ic_search_icon_color));
        this.productService = ViewModelProviders.of(this, Factory.getProductServiceFactory()).get(ProductService.class);
        this.userService = ViewModelProviders.of(this, Factory.getUserServiceFactory()).get(UserService.class);
//        final Observer<Product> productObserver = this::displayProductOnPage;
//        productService.getProductById(this.productId).observe(this, productObserver);
        displayProductOnPage(product);
        setHeaderListeners();
    }

    private void extractExtras() {
        user = (User) getIntent().getSerializableExtra(getString(R.string.user_relay));
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            product = (Product) bundle.getSerializable("product");
        } else {
            product = null;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void displayProductOnPage(Product product) {
        TextView textViewTitle = findViewById(R.id.textviewProductPageTitle);
        TextView textViewDescription = findViewById(R.id.textviewProductPageDescription);
        TextView textViewProperties = findViewById(R.id.textviewProductPageProperties);
        ImageView imageView = findViewById(R.id.prpImagePlaceholder);
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
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.productPageMapFragment);
        Objects.requireNonNull(mapFragment).getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        LatLng coordinates = new LatLng(mapLocation.getLatitude(), mapLocation.getLongitude());
        mMap.addMarker(new MarkerOptions()
                .position(coordinates)
                .title("Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 15));
    }

    private void setHeaderListeners() {
        HeaderClickListener.setHeaderListeners(findViewById(android.R.id.content).getRootView());
    }

    @Override
    public void startActivity(Intent intent) {
        intent.putExtra(getString(R.string.user_relay), user);
        super.startActivity(intent);
    }
}
