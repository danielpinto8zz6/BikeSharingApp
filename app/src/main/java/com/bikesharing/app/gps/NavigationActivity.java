package com.bikesharing.app.gps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.bikesharing.app.R;
import com.bikesharing.app.home.HomeActivity;
import com.bikesharing.app.travel.TravelActivity;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.services.android.navigation.ui.v5.NavigationView;
import com.mapbox.services.android.navigation.ui.v5.NavigationViewOptions;
import com.mapbox.services.android.navigation.ui.v5.OnNavigationReadyCallback;
import com.mapbox.services.android.navigation.v5.routeprogress.ProgressChangeListener;
import com.mapbox.services.android.navigation.v5.routeprogress.RouteProgressState;

public class NavigationActivity extends AppCompatActivity implements OnNavigationReadyCallback {

    private NavigationView myNavigationView;
    private DirectionsRoute myDirectionsRoute;

    AlertDialog.Builder alertUnlockCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navigation);

        myDirectionsRoute = (DirectionsRoute) getIntent().getSerializableExtra("Route");

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        window.setStatusBarColor(getColor(R.color.DarkGreen));
        window.setNavigationBarColor(getColor(R.color.White));

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        myNavigationView = findViewById(R.id.naviagtionView);
        myNavigationView.onCreate(savedInstanceState);
        myNavigationView.initialize(this);
    }

    @SuppressLint("ResourceAsColor")
    private void displayUnlockCodeDialog() {

        alertUnlockCode = new AlertDialog.Builder(NavigationActivity.this);
        final EditText editTextName1 = new EditText(NavigationActivity.this);
        editTextName1.setHint("Write unlock code here");

        alertUnlockCode.setTitle("Unlock code");
        // titles can be used regardless of a custom layout or not
        alertUnlockCode.setView(editTextName1);
        LinearLayout layoutName = new LinearLayout(NavigationActivity.this);
        layoutName.setOrientation(LinearLayout.VERTICAL);
        layoutName.addView(editTextName1); // displays the user input bar
        alertUnlockCode.setView(layoutName);

        alertUnlockCode.setPositiveButton("Continue", (dialog, whichButton) -> {

            Intent myIntent = new Intent(getApplicationContext(), TravelActivity.class);
            myIntent.putExtra("Dock", getIntent().getSerializableExtra("Dock"));

            startActivity(myIntent);
            finish();
        });

        alertUnlockCode.setNegativeButton("Cancel", (dialog, whichButton) -> {

            dialog.cancel(); // closes dialog

            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        });

        AlertDialog myAlertDialog = alertUnlockCode.create();

        //2. now setup to change color of the button
        myAlertDialog.setOnShowListener(arg0 -> {
            myAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(R.color.DarkGreen);
            myAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(R.color.Red);
        });

        myAlertDialog.setCancelable(false);
        myAlertDialog.setCanceledOnTouchOutside(false);

        myAlertDialog.show();
    }

    private ProgressChangeListener myProgressChangeListener = (location, routeProgress) -> {

        if (routeProgress.currentState() == RouteProgressState.ROUTE_ARRIVED) {
            if (alertUnlockCode == null) {
                displayUnlockCodeDialog();
            }
        }
    };

    @Override
    public void onNavigationReady(boolean isRunning) {

        NavigationViewOptions myNavigationViewOptions = NavigationViewOptions.builder()
                .progressChangeListener(myProgressChangeListener)
                .directionsRoute(myDirectionsRoute)
                .shouldSimulateRoute(true)
                .build();

        myNavigationView.startNavigation(myNavigationViewOptions);
    }

    @Override
    public void onStart() {
        super.onStart();
        myNavigationView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        myNavigationView.onResume();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        myNavigationView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        if (savedInstanceState != null) {
            myNavigationView.onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        myNavigationView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        myNavigationView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        myNavigationView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myNavigationView.onDestroy();
    }
}