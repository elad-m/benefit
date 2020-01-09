package com.benefit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity implements OnMapReadyCallback, OnMarkerDragListener {

    private static final String TAG = SignUpActivity.class.getSimpleName();
    public static final String USER_FIRST_NAME_REPLY = "firstName";
    public static final String USER_LAST_NAME_REPLY = "lastName";
    public static final String USER_ADDRESS_NAME_REPLY = "address";
    public static final String USER_LOCATION_REPLY = "location";
    private GoogleMap mMap;
    private Location chosenLocation;
    private CameraPosition mCameraPosition;

    private EditText firstNameField, lastNameField, addressField;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Jerusalem, Israel) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(31.771959, 35.217018);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;
    private Marker mSelectedLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        //initiate chosenLocation
        chosenLocation = new Location(LocationManager.GPS_PROVIDER);
        chosenLocation.setLatitude(mDefaultLocation.latitude);
        chosenLocation.setLongitude(mDefaultLocation.longitude);

        setContentView(R.layout.activity_sign_up);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.googleMapFragment);
        mapFragment.getMapAsync(this);

        //set views objects
        firstNameField = findViewById(R.id.first_name_text);
        lastNameField = findViewById(R.id.last_name_text);
        addressField = findViewById(R.id.address_text);

    }


    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device
                            // and add a marker.
                            mLastKnownLocation = task.getResult();
                            chosenLocation = mLastKnownLocation;
                            LatLng currentLocation = new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude());
                            mSelectedLocation = mMap.addMarker(new MarkerOptions()
                                                                .position(currentLocation)
                                                                .draggable(true));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, DEFAULT_ZOOM));
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
    }

    @Override
    public void onMarkerDrag(Marker marker) {
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        if (marker == mSelectedLocation){
            chosenLocation.setLatitude(marker.getPosition().latitude);
            chosenLocation.setLongitude(marker.getPosition().longitude);
        }
    }

    public void onDoneClicked(View view){
        String firstName = firstNameField.getText().toString();
        String lastName= lastNameField.getText().toString();
        String address = addressField.getText().toString();
        if (firstName.isEmpty()){
            if(lastName.isEmpty()){
                Toast.makeText(this, getString(R.string.enter_first_and_last_name), Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, getString(R.string.enter_first_name), Toast.LENGTH_LONG).show();
            }
        }
        else {
            if(lastName.isEmpty()){
                Toast.makeText(this, getString(R.string.enter_last_name), Toast.LENGTH_LONG).show();
            }
            else {
                Intent replyIntent  = new Intent();
                replyIntent.putExtra(USER_FIRST_NAME_REPLY, firstName);
                replyIntent.putExtra(USER_LAST_NAME_REPLY, lastName);
                replyIntent.putExtra(USER_ADDRESS_NAME_REPLY, address);
                ArrayList<Parcelable> locationList = new ArrayList<>();
                locationList.add(chosenLocation);
                replyIntent.putParcelableArrayListExtra(USER_LOCATION_REPLY, locationList);
                setResult(RESULT_OK, replyIntent);
                finish();
            }
        }
    }
}
