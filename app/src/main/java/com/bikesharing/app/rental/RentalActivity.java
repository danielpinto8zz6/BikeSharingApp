package com.bikesharing.app.rental;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bikesharing.app.R;
import com.bikesharing.app.data.Dock;
import com.bikesharing.app.data.Rental;
import com.bikesharing.app.home.HomeActivity;
import com.bikesharing.app.rest.HttpStatus;
import com.bikesharing.app.rest.RestService;
import com.bikesharing.app.rest.RestServiceManager;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RentalActivity extends AppCompatActivity {

    protected String szToken;
    protected int nRentalId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR  | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        window.setStatusBarColor(getColor(R.color.White));
        window.setNavigationBarColor(getColor(R.color.White));

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        SharedPreferences mySharedPreferences = getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);

        szToken = mySharedPreferences.getString("token", null);
        if ((szToken == null) ||
            (szToken.isEmpty())) {

            displayErrorRentalDialog("Missing Token");
            return;
        }

        String szEmail = mySharedPreferences.getString("email", null);
        if ((szEmail == null) || (szEmail.isEmpty())) {

            displayErrorRentalDialog("Email info is missing");
            return;
        }

        Dock myDock = (Dock) getIntent().getSerializableExtra("Dock");
        if (myDock == null) {
            displayErrorRentalDialog("Dock info is missing");
        }

        String myBikeCode = getIntent().getStringExtra("bikeCode");
        if (myBikeCode != null) {
            startRental(szToken, szEmail, myDock, myBikeCode);
        } else {
            displayErrorRentalDialog("Bike code is missing");
        }
    }

    private void displayErrorRentalDialog(String szMessage){

        AlertDialog myDialog = new AlertDialog.Builder(this).create();
        myDialog.setTitle("Rental Error!");
        myDialog.setMessage(szMessage);
        myDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK", (dialog, which) -> {

            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
            dialog.dismiss();
        });

        myDialog.show();
    }

    private void startRental(String szToken, String szEmail, Dock myDock, String myBikeCode) {

        RestService myRestService = RestServiceManager.getInstance().getRestService();
        Call<Rental> myReturnedUser = myRestService.newRental(new Rental(myDock.getId(), myDock.getBikeId(), szEmail, myBikeCode), "Bearer " + szToken);

        myReturnedUser.enqueue(new Callback<Rental>() {
            @Override
            public void onResponse(Call<Rental> call, Response<Rental> response) {

                if (!response.isSuccessful()) {
                    displayErrorRentalDialog(HttpStatus.getStatusText(response.code()));
                    return;
                }

                nRentalId = response.body().getId();
                SharedPreferences mySharedPreferences = getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);
                mySharedPreferences.edit().putInt("rental", nRentalId).apply();
            }

            @Override
            public void onFailure(Call<Rental> call, Throwable t) {

                if (t instanceof SocketTimeoutException) {

                    displayErrorRentalDialog("Service unavailable, try again later.");
                    return;
                }

                displayErrorRentalDialog("Unknown error");
            }
        });
    }
}