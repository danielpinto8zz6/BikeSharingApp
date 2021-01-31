package com.bikesharing.app.home.rentalHistory.locationHistory;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bikesharing.app.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bikesharing.app.data.Bike;
import com.bikesharing.app.data.Rental;
import com.bikesharing.app.data.TravelEvent;
import com.bikesharing.app.data.payment.Payment;
import com.bikesharing.app.home.HomeActivity;
import com.bikesharing.app.home.HomeFragment;
import com.bikesharing.app.rest.HttpStatus;
import com.bikesharing.app.rest.RestService;
import com.bikesharing.app.rest.RestServiceManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.BaseMarkerOptions;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationHistoryActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;

    private Rental myRental;
    protected String szToken;

    private TextView tvBikeName;
    private TextView tvPaymentValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        window.setStatusBarColor(getColor(R.color.DarkGreen));
        window.setNavigationBarColor(getColor(R.color.White));

        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        // This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.activity_rental_history);

        SharedPreferences mySharedPreferences = getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);
        szToken = mySharedPreferences.getString("token", null);
        if ((szToken == null) ||
                (szToken.isEmpty())) {

            displayErrorLocationHistoryDialog("Missing Token");
            return;
        }

        this.myRental = (Rental) getIntent().getSerializableExtra("rental");

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        TextView tvEndDate = findViewById(R.id.tvEndDate);
        TextView tvStartDate = findViewById(R.id.tvStartDate);
        this.tvPaymentValue = findViewById(R.id.tvPaymentValue);
        this.tvBikeName = findViewById(R.id.tvBikeName);

        tvStartDate.setText(DateUtils.formatDateTime(this, this.myRental.getStartDate().getTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_TIME));
        tvEndDate.setText(DateUtils.formatDateTime(this, this.myRental.getEndDate().getTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_TIME));

        loadPayment();
        loadBike();
    }

    private void loadBike() {
        RestService myRestService = RestServiceManager.getInstance().getRestService();
        Call<Bike> bikeRequest = myRestService.getBikeDetails(this.myRental.getBikeId(), "Bearer " + szToken);

        bikeRequest.enqueue(new Callback<Bike>() {

            @Override
            public void onResponse(Call<Bike> call, Response<Bike> response) {
                if (!response.isSuccessful()) {

                    displayErrorLocationHistoryDialog(HttpStatus.getStatusText(response.code()));
                    return;
                }

                Bike bike = response.body();

                if (bike != null) {
                    tvBikeName.setText(bike.getBrand() + " " + bike.getModel());
                }
            }

            @Override
            public void onFailure(Call<Bike> call, Throwable t) {
                displayErrorLocationHistoryDialog(t.getMessage());
            }
        });
    }

    private void loadPayment() {
        RestService myRestService = RestServiceManager.getInstance().getRestService();
        Call<Payment> paymentRequest = myRestService.getPaymentByRentalId(this.myRental.getId(), "Bearer " + szToken);

        paymentRequest.enqueue(new Callback<Payment>() {

            @Override
            public void onResponse(Call<Payment> call, Response<Payment> response) {
                if (!response.isSuccessful()) {

                    displayErrorLocationHistoryDialog(HttpStatus.getStatusText(response.code()));
                    return;
                }

                Payment payment = response.body();

                if (payment != null) {
                    tvPaymentValue.setText(payment.getValue() + " €");
                }
            }

            @Override
            public void onFailure(Call<Payment> call, Throwable t) {
                displayErrorLocationHistoryDialog(t.getMessage());
            }
        });
    }

    private void displayErrorLocationHistoryDialog(String szMessage) {

        AlertDialog myDialog = new AlertDialog.Builder(this).create();
        myDialog.setTitle("Location History Error!");
        myDialog.setMessage(szMessage);
        myDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK", (dialog, which) -> {

            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
            dialog.dismiss();
        });

        myDialog.show();
    }

    @Override
    public void onMapReady(final MapboxMap mapboxMap) {

        mapboxMap.setStyle(Style.LIGHT, style -> loadLocationHistory(mapboxMap, style));
    }

    private void loadLocationHistory(MapboxMap myMapbox, Style style) {

        List<LatLng> myLatLngs = new ArrayList<>();

        RestService myRestService = RestServiceManager.getInstance().getRestService();
        Call<List<TravelEvent>> myReturnedLocation = myRestService.getAllLocationHistory(this.myRental.getId(), "Bearer " + szToken);

        myReturnedLocation.enqueue(new Callback<List<TravelEvent>>() {
            @Override
            public void onResponse(Call<List<TravelEvent>> call, Response<List<TravelEvent>> response) {

                if (!response.isSuccessful()) {

                    displayErrorLocationHistoryDialog(HttpStatus.getStatusText(response.code()));
                    return;
                }

                for (TravelEvent myTravelEvent : response.body()) {

                    LatLng myLatLng = new LatLng(myTravelEvent.getLatitude(), myTravelEvent.getLongitude());
                    myMapbox.addMarker(new MarkerOptions().position(myLatLng));
                    myLatLngs.add(myLatLng);
                }

                if (myLatLngs.size() > 0) {

                    CameraPosition position = new CameraPosition.Builder()
                            .target(new LatLng(myLatLngs.get(myLatLngs.size()-1).getLatitude(), myLatLngs.get(myLatLngs.size()-1).getLongitude())) // Sets the new camera position
                            .zoom(15)
                            .build();

                    myMapbox.animateCamera(CameraUpdateFactory.newCameraPosition(position));
                }
            }

            @Override
            public void onFailure(Call<List<TravelEvent>> call, Throwable t) {
                displayErrorLocationHistoryDialog(t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}