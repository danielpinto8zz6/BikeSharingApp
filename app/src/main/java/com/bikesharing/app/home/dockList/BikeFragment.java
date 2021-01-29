package com.bikesharing.app.home.dockList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bikesharing.app.R;
import com.bikesharing.app.data.Dock;
import com.bikesharing.app.data.Bike;
import com.bikesharing.app.data.Rental;
import com.bikesharing.app.gps.GpsActivity;
import com.bikesharing.app.home.HomeActivity;
import com.bikesharing.app.home.HomeFragment;
import com.bikesharing.app.rental.RentalActivity;
import com.bikesharing.app.rest.HttpStatus;
import com.bikesharing.app.rest.RestService;
import com.bikesharing.app.rest.RestServiceManager;

import org.jetbrains.annotations.NotNull;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BikeFragment extends Fragment implements View.OnClickListener, HomeFragment{

    private Dock myDock;

    private String szToken;

    private TextView myBikeBrandModel;
    private TextView myBikeLocation;
    private TextView myBikeChargeLevel;

    public BikeFragment(Dock myDock) {
        this.myDock = myDock;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bike_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState == null) {

            SharedPreferences mySharedPreferences = getActivity().getSharedPreferences("com.mycompany.myAppName", Context.MODE_PRIVATE);
            this.szToken = mySharedPreferences.getString("token", null);

            if ((this.szToken == null) ||
                (this.szToken.isEmpty())) {

                ((HomeActivity) getActivity()).displayErrorExitDialog("Token", "Missing Token");
                return;
            }

            loadDockDetails();
        }

        myBikeBrandModel= view.findViewById(R.id.bikeBrandModel);
        myBikeLocation = view.findViewById(R.id.bikeLocation);
        myBikeChargeLevel = view.findViewById(R.id.bikeChargeLevel);

        CircularProgressButton myTravelButton = view.findViewById(R.id.cirGoToBikeButton);
        myTravelButton.setOnClickListener(this);

        CircularProgressButton myUnlockButton = view.findViewById(R.id.cirInsertCodeButton);
        myUnlockButton.setOnClickListener(this);
    }

    private void loadDockDetails() {

        RestService myRestService = RestServiceManager.getInstance().getRestService();
        Call<Bike> myReturnedUser = myRestService.getBikeDetails(this.myDock.getBikeId(), "Bearer " + this.szToken);

        myReturnedUser.enqueue(new Callback<Bike>() {

            @Override
            public void onResponse(@NotNull Call<Bike> call, @NotNull Response<Bike> response) {

                if (!response.isSuccessful()) {

                    ((HomeActivity) getActivity()).displayErrorExitDialog("Error", HttpStatus.getStatusText(response.code()));
                    return;
                }

                myBikeBrandModel.setText(response.body().getBrand() + response.body().getModel());
                myBikeLocation.setText(myDock.getLocation());
                myBikeChargeLevel.setText(String.valueOf(response.body().getChargeLevel()));
            }

            @Override
            public void onFailure(Call<Bike> call, Throwable t) {
                ((HomeActivity) getActivity()).displayErrorExitDialog("Error", t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.cirGoToBikeButton:

                onGpsClick();
                break;

            case R.id.cirInsertCodeButton:
                onInsertCodeClick();
                break;
        }
    }

    private void onGpsClick() {

        Intent myIntent = new Intent(getActivity().getApplicationContext(), GpsActivity.class);
        myIntent.putExtra("Dock", this.myDock);

        getActivity().startActivity(myIntent);
        getActivity().finish();
    }

    @SuppressLint("ResourceAsColor")
    private void onInsertCodeClick() {

        AlertDialog.Builder alertUnlockCode = new AlertDialog.Builder(getActivity());
        final EditText editTextName1 = new EditText(getActivity());
        editTextName1.setHint("Write unlock code here");

        alertUnlockCode.setTitle("Unlock code");
        // titles can be used regardless of a custom layout or not
        alertUnlockCode.setView(editTextName1);
        LinearLayout layoutName = new LinearLayout(getActivity());
        layoutName.setOrientation(LinearLayout.VERTICAL);
        layoutName.addView(editTextName1); // displays the user input bar
        alertUnlockCode.setView(layoutName);

        alertUnlockCode.setPositiveButton("Continue", (dialog, whichButton) -> {

            Intent myIntent = new Intent(getActivity().getApplicationContext(), RentalActivity.class);
            myIntent.putExtra("Dock", this.myDock);
            myIntent.putExtra("bikeCode", editTextName1.getText().toString());

            getActivity().startActivity(myIntent);
            getActivity().finish();
        });

        alertUnlockCode.setNegativeButton("Cancel", (dialog, whichButton) -> {
            dialog.cancel(); // closes dialog
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

    @Override
    public boolean allowBackPressed() {
        return true;
    }

    @Override
    public int getFragmentType() {
        return HomeFragment.FRAGMENT_TYPE_BIKE_INFO;
    }
}