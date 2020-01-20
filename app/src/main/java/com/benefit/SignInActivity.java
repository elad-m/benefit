package com.benefit;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.benefit.ui.WorkaroundMapFragment;
import com.benefit.viewmodel.SignInViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
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
import com.google.android.material.textfield.TextInputEditText;

/**
 * This activity handle sign in and adding new user to database.
 */
public class SignInActivity extends AppCompatActivity implements OnMapReadyCallback, OnMarkerDragListener {

    public enum LoginState{NOT_SIGN_IN, LOGGING_IN, SIGN_IN_GET_USER, NEW_USER_SIGN_UP, FINISH};
    private static final String TAG = SignInActivity.class.getSimpleName();
    private static final int RC_GOOGLE_SIGN_IN = 9001;
    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private GoogleMap mMap;
    private CameraPosition mCameraPosition;
    private SignInViewModel viewModel;
    private Observer<Boolean> gettingNewUserSucceeded;

    //view elements
    private ScrollView scrollView;
    private LinearLayout signInButtons, signUpForm;
    private SignInButton googleSignInButton;
    private Button mailSignInButton, phoneSignInButton;
    private TextView title;
    private TextInputEditText firstNameField, lastNameField, addressField;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FirebaseApp.initializeApp(this);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        setContentView(R.layout.activity_sign_in);

        //initiate viewmodle
        viewModel = ViewModelProviders.of(this).get(SignInViewModel.class);

        //set views objects
        setUpViewElements();

        //set getting user from database observer
        setUpGettingNewUserSucceeded();

    }

    private void setUpViewElements(){
        scrollView = findViewById(R.id.sign_in_scrollview);
        signInButtons = findViewById(R.id.sign_in_buttons);
        signUpForm = findViewById(R.id.sign_up_form);
        title = findViewById(R.id.sign_up_title_text_view);
        googleSignInButton = findViewById(R.id.google_sign_in_button);
        googleSignInButton.setOnClickListener(this::onGoogleSignInClicked);
        mailSignInButton = findViewById(R.id.mail_sign_in_button);
        phoneSignInButton = findViewById(R.id.phone_sign_in_button);
        firstNameField = findViewById(R.id.first_name_input_text);
        lastNameField = findViewById(R.id.last_name_input_text);
        addressField = findViewById(R.id.address_input_text);
    }

    private void setUpGettingNewUserSucceeded(){
        gettingNewUserSucceeded = success -> {
            if (success){
                if (viewModel.getUser() != null){
                    viewModel.setLoginState(LoginState.FINISH);
                }
                else {
                    viewModel.createNewUser();
                    viewModel.setLoginState(LoginState.NEW_USER_SIGN_UP);
                }
            }
            else {
                Toast.makeText(this, R.string.sign_in_fail_massage, Toast.LENGTH_LONG).show();
                viewModel.setLoginState(LoginState.NOT_SIGN_IN);
            }
            updateAccordingToLoginState();
        };
    }

    private void initiateGoogleMap(){
        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.googleMapFragment);
        mapFragment.getMapAsync(this);
    }

    private void updateAccordingToLoginState(){
        switch (viewModel.getLoginState()){
            case NOT_SIGN_IN:
                signInButtons.setVisibility(LinearLayout.VISIBLE);
                signUpForm.setVisibility(LinearLayout.GONE);
                title.setText(R.string.sign_in_title);
                break;
            case LOGGING_IN:
                signInButtons.setVisibility(LinearLayout.GONE);
                signUpForm.setVisibility(LinearLayout.GONE);
                //can add logging in animation
                title.setText(R.string.logging_in_title);
                break;
            case SIGN_IN_GET_USER:
                signInButtons.setVisibility(LinearLayout.GONE);
                signUpForm.setVisibility(LinearLayout.GONE);
                //can add logging in animation
                title.setText(R.string.sign_in_getting_user_title);
                if (!viewModel.getmAccessingDatabase()) {
                    viewModel.getUserFromDatabase().observe(this, gettingNewUserSucceeded);
                }
                break;
            case NEW_USER_SIGN_UP:
                signInButtons.setVisibility(LinearLayout.GONE);
                signUpForm.setVisibility(LinearLayout.VISIBLE);
                title.setText(R.string.sign_up_title);
                initiateGoogleMap();
                break;
            case FINISH:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(getString(R.string.user_relay), viewModel.getUser());
                startActivity(intent);
                finish();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        updateAccordingToLoginState();
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

    public void onGoogleSignInClicked(View view){
        if (viewModel.getLoginState() == LoginState.NOT_SIGN_IN){
            // Configure Google Sign In
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            // Build a GoogleSignInClient with the options specified by gso.
            GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            //start google signin ui
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            viewModel.setLoginState(LoginState.LOGGING_IN);
            startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_GOOGLE_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                LiveData<Boolean> success = viewModel.signInWithGoogle(task.getResult(ApiException.class));
                success.observe(this, signInSucceeded -> {
                    if(signInSucceeded){
                        viewModel.setLoginState(LoginState.SIGN_IN_GET_USER);
                    }
                    else {
                        Toast.makeText(this, R.string.sign_in_fail_massage, Toast.LENGTH_LONG).show();
                        viewModel.setLoginState(LoginState.NOT_SIGN_IN);
                    }
                    updateAccordingToLoginState();
                });
            } catch (ApiException e) {
                // Google Sign In failed, update ui appropriately
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(this, R.string.google_sign_in_fail_massage, Toast.LENGTH_LONG).show();
                viewModel.setLoginState(LoginState.NOT_SIGN_IN);
            }
            updateAccordingToLoginState();
        }

        //for the future - mail and phone signin
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
     * Updates the map's ui settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mMap.getUiSettings().setZoomControlsEnabled(true);
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
                            LatLng currentLocation = new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude());
                            viewModel.getUser().setLocationLatitude(currentLocation.latitude);
                            viewModel.getUser().setLocationLongitude(currentLocation.longitude);
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

        // disable scrollview when touching the map
        ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMapFragment))
                .setListener(new WorkaroundMapFragment.OnTouchListener() {
                    @Override
                    public void onTouch()
                    {
                        scrollView.requestDisallowInterceptTouchEvent(true);
                    }
                });
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
            viewModel.getUser().setLocationLatitude(marker.getPosition().latitude);
            viewModel.getUser().setLocationLongitude(marker.getPosition().longitude);
        }
    }

    public void onDoneClicked(View view) {
        if (viewModel.getLoginState() == LoginState.NEW_USER_SIGN_UP) {
            firstNameField.setError(null);
            lastNameField.setError(null);
            String firstName = firstNameField.getText().toString();
            String lastName = lastNameField.getText().toString();
            String address = addressField.getText().toString();
            if (firstName.isEmpty()) {
                if (lastName.isEmpty()) {
                    Toast.makeText(this, getString(R.string.enter_first_and_last_name), Toast.LENGTH_LONG).show();
                    firstNameField.setError(getString(R.string.error_field_massge));
                    lastNameField.setError(getString(R.string.error_field_massge));
                } else {
                    Toast.makeText(this, getString(R.string.enter_first_name), Toast.LENGTH_LONG).show();
                    firstNameField.setError(getString(R.string.error_field_massge));
                }
            } else {
                if (lastName.isEmpty()) {
                    Toast.makeText(this, getString(R.string.enter_last_name), Toast.LENGTH_LONG).show();
                    firstNameField.setError(getString(R.string.error_field_massge));
                } else {
                    viewModel.getUser().setFirstName(firstName);
                    viewModel.getUser().setLastName(lastName);
                    viewModel.getUser().setAddress(address);
                    viewModel.addNewUserToDatabase();
                    viewModel.setLoginState(LoginState.FINISH);
                    updateAccordingToLoginState();
                }
            }
        }
    }
}
