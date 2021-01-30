package com.bikesharing.app.travel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.bikesharing.app.R;
import com.bikesharing.app.data.Dock;
import com.bikesharing.app.data.Rental;
import com.bikesharing.app.data.TravelEvent;
import com.bikesharing.app.home.HomeActivity;
import com.bikesharing.app.rest.HttpStatus;
import com.bikesharing.app.rest.RestService;
import com.bikesharing.app.rest.RestServiceManager;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.location.Location;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
// Classes needed to initialize the map
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
// Classes needed to handle location permissions
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;

import java.util.Date;
import java.util.List;
// Classes needed to add the location engine
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import java.lang.ref.WeakReference;
// Classes needed to add the location component
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;

public class TravelActivity extends AppCompatActivity implements
        OnMapReadyCallback, PermissionsListener {

    // Variables needed to initialize a map
    private MapboxMap mapboxMap;
    private MapView mapView;
    // Variables needed to handle location permissions
    private PermissionsManager permissionsManager;
    // Variables needed to add the location engine
    private LocationEngine locationEngine;
    private long DEFAULT_INTERVAL_IN_MILLISECONDS = 2000L;
    private long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 2;
    // Variables needed to listen to location updates
    private TravelActivityLocationCallback callback = new TravelActivityLocationCallback(this);

    protected String szToken;
    protected int nRentalId = -1;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        setContentView(R.layout.activity_travel);

        SharedPreferences mySharedPreferences = getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);

        szToken = mySharedPreferences.getString("token", null);
        if ((szToken == null) ||
            (szToken.isEmpty())) {

            displayErrorRentalDialog("Missing Token");
            return;
        }

        nRentalId = mySharedPreferences.getInt("rental", -1);
        if (nRentalId == -1) {

            displayErrorRentalDialog("Missing Rental information");
            return;
        }

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    private void displayErrorRentalDialog(String szMessage){

        AlertDialog myDialog = new AlertDialog.Builder(this).create();
        myDialog.setTitle("Rental Error!");
        myDialog.setMessage(szMessage);
        myDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK", (dialog, which) -> {

            SharedPreferences mySharedPreferences = getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);
            mySharedPreferences.edit().putBoolean("isTravelling", Boolean.FALSE).apply();

            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
            dialog.dismiss();
        });

        myDialog.show();
    }

    @Override
    public void onBackPressed() {
        // Nothing to be done
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        mapboxMap.setStyle(Style.LIGHT,
                style -> enableLocationComponent(style));
    }

    /**
     * Initialize the Maps SDK's LocationComponent
     */
    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

            // Set the LocationComponent activation options
            LocationComponentActivationOptions locationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(this, loadedMapStyle)
                            .useDefaultLocationEngine(false)
                            .build();

            // Activate with the LocationComponentActivationOptions object
            locationComponent.activateLocationComponent(locationComponentActivationOptions);

            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);

            initLocationEngine();

            SharedPreferences mySharedPreferences = getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);
            mySharedPreferences.edit().putBoolean("isTravelling", Boolean.TRUE).apply();
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    /**
     * Set up the LocationEngine and the parameters for querying the device's location
     */
    @SuppressLint("MissingPermission")
    private void initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(this);

        LocationEngineRequest request = new LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build();

        locationEngine.requestLocationUpdates(request, callback, getMainLooper());
        locationEngine.getLastLocation(callback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, "We need your permissions for the app to work", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            if (mapboxMap.getStyle() != null) {
                enableLocationComponent(mapboxMap.getStyle());
            }
        } else {
            displayErrorRentalDialog("We need your permissions for the app to work");
            finish();
        }
    }

    private static class TravelActivityLocationCallback implements LocationEngineCallback<LocationEngineResult> {

        private final WeakReference<TravelActivity> activityWeakReference;

        TravelActivityLocationCallback(TravelActivity activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }

        /**
         * The LocationEngineCallback interface's method which fires when the device's location has changed.
         *
         * @param result the LocationEngineResult object which has the last known location within it.
         */
        @Override
        public void onSuccess(LocationEngineResult result) {
            TravelActivity activity = activityWeakReference.get();

            if (activity != null) {
                Location location = result.getLastLocation();

                if (location == null) {
                    return;
                }

                if ((activity.szToken == null) ||
                    (activity.szToken.isEmpty() ||
                    (activity.nRentalId == -1))) {
                    return;
                }

                TravelEvent myTravelEvent = new TravelEvent(activity.nRentalId, location.getLongitude(), location.getLatitude(), new Date(location.getTime()));
                sendLocation(activity.szToken, myTravelEvent);

                // Pass the new location to the Maps SDK's LocationComponent
                if (activity.mapboxMap != null && result.getLastLocation() != null) {
                    activity.mapboxMap.getLocationComponent().forceLocationUpdate(result.getLastLocation());
                }
            }
        }

        private void sendLocation(String szToken, TravelEvent myTravelEvent) {

            RestService myRestService = RestServiceManager.getInstance().getRestService();
            Call<Void> myReturnedLocation = myRestService.sendLocation(myTravelEvent, "Bearer " + szToken);

            myReturnedLocation.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {

                    System.out.println("Response: " + response);
                }

                @Override
                public void onFailure(Call call, Throwable t) {

                    System.out.println("Response: " + t);
                }
            });
        }

        /**
         * The LocationEngineCallback interface's method which fires when the device's location can not be captured
         *
         * @param exception the exception message
         */
        @Override
        public void onFailure(@NonNull Exception exception) {
            Log.d("LocationChangeActivity", exception.getLocalizedMessage());
            TravelActivity activity = activityWeakReference.get();
            if (activity != null) {
                Toast.makeText(activity, exception.getLocalizedMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Prevent leaks
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates(callback);
        }
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}