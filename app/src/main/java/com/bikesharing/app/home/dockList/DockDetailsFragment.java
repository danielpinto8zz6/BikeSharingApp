package com.bikesharing.app.home.dockList;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bikesharing.app.R;
import com.bikesharing.app.home.HomeActivity;
import com.bikesharing.app.home.HomeFragment;
import com.bikesharing.app.home.travel.TravelFragment;
import com.bumptech.glide.Glide;

public class DockDetailsFragment extends Fragment implements HomeFragment {

    private String szToken;

    private Integer dockId;
    private Integer bikeId;
    private Double latitude;
    private Double longitude;
    private String location;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        dockId = this.getArguments().getInt("dockId");
        bikeId = this.getArguments().getInt("bikeId");
        latitude = this.getArguments().getDouble("latitude");
        longitude = this.getArguments().getDouble("longitude");
        location = this.getArguments().getString("location");

        return inflater.inflate(R.layout.fragment_dock_details, container, false);
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
        }

        Button btn = (Button) view.findViewById(R.id.unlockBike);

        btn.setOnClickListener(this::onUnlockBike);

        TextView bikeIdTextView = (TextView) view.findViewById(R.id.bikeId);
        bikeIdTextView.setText("Bike id: " + bikeId);
        TextView dockIdTextView = (TextView) view.findViewById(R.id.dockId);
        dockIdTextView.setText("Dock id: " + dockId);
        TextView locationTextView = (TextView) view.findViewById(R.id.location);
        locationTextView.setText("Location id: " + bikeId);

        ImageView imageView = (ImageView) view.findViewById(R.id.maps_view);
        Glide.with(this).load("https://api.mapbox.com/styles/v1/mapbox/streets-v11/static/-122.4241,37.78,14.25,0,60/600x400?access_token=pk.eyJ1IjoidGlhZ29waW50byIsImEiOiJja2pjeDQ1M3AwbGJlMnNuejU1MG5pM2swIn0.HZVePQ2iI5JduGHGlu-xLA").into(imageView);
    }

    public void onUnlockBike(View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this.getContext());
        alert.setTitle("Unlock bike");
        alert.setMessage("Insert dock identifier");

        final EditText input = new EditText(this.getContext());
        alert.setView(input);

        alert.setPositiveButton("Ok", (dialog, whichButton) -> {
            String value = input.getText().toString();

            Toast.makeText(this.getContext(), "Unlocking bike..." + value, Toast.LENGTH_LONG);

            TravelFragment travelFragment = new TravelFragment();

            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.dock_details, travelFragment).addToBackStack(null).commit();
        });

        alert.setNegativeButton("Cancel", (dialog, which) -> {
        });

        alert.show();
    }

    @Override
    public boolean allowBackPressed() {
        return true;
    }

    @Override
    public int getFragmentType() {
        return HomeFragment.FRAGMENT_TYPE_DOCK_DETAILS;
    }
}
