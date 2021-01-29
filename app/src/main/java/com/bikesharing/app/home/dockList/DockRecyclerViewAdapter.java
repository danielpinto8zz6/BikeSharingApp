package com.bikesharing.app.home.dockList;

import android.annotation.SuppressLint;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bikesharing.app.R;
import com.bikesharing.app.data.Dock;
import com.bikesharing.app.home.HomeActivity;
import com.bikesharing.app.home.HomeFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Period;
import java.util.ArrayList;

public class DockRecyclerViewAdapter extends RecyclerView.Adapter<DockRecyclerViewAdapter.MyViewHolder> {

    private HomeActivity myHomeActivity;

    private RecyclerView myRecyclerView;

    private ArrayList<Dock> myDockDataset = new ArrayList<>();

    private FusedLocationProviderClient fusedLocationClient;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView myLocation;
        public TextView myDistance;
        public TextView myDistanceTime;

        public MyViewHolder(View myOptionDock) {

            super(myOptionDock);

            this.myLocation = myOptionDock.findViewById(R.id.dockLocation);
            this.myDistance = myOptionDock.findViewById(R.id.distance);
            this.myDistanceTime = myOptionDock.findViewById(R.id.distance_time);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        this.myRecyclerView = recyclerView;
    }

    public DockRecyclerViewAdapter(HomeActivity myHomeActivity) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(myHomeActivity);
        this.myHomeActivity = myHomeActivity;
    }

    public void addAll(ArrayList<Dock> myDockDataset) {

        for (Dock myDock : myDockDataset) {
            this.add(myDock);
        }
    }

    public void set(ArrayList<Dock> myDockDataset) {
        this.myDockDataset = myDockDataset;
        notifyDataSetChanged();
    }

    public void add(Dock myDock) {
        this.myDockDataset.add(myDock);
        notifyItemInserted(this.myDockDataset.size() - 1);
    }

    private final View.OnClickListener myDockClickListener = myView -> {

        int nPosition = this.myRecyclerView.getChildLayoutPosition(myView);
        Dock myDock = myDockDataset.get(nPosition);

        this.myHomeActivity.getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, android.R.anim.fade_out)
                .replace(R.id.fragment_home_container, new BikeFragment(myDock), HomeFragment.FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();
    };

    // Create new views (invoked by the layout manager)
    @NotNull
    @Override
    public DockRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View myOptionDock = LayoutInflater.from(parent.getContext()).inflate(R.layout.option_dock, parent, false);
        myOptionDock.setOnClickListener(myDockClickListener);
        return new MyViewHolder(myOptionDock);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("MissingPermission")
    @Override
    public void onBindViewHolder(MyViewHolder myOptionDock, int position) {

        Dock myDock = this.myDockDataset.get(position);

        myOptionDock.myLocation.setText(myDock.getLocation());

        fusedLocationClient.getLastLocation().addOnSuccessListener(myHomeActivity, myLocation -> {

            // Got last known location. In some rare situations this can be null.
            if (myLocation == null) {
                myOptionDock.myDistance.setText("Distance: unknown");
                myOptionDock.myDistanceTime.setText("Estimated Time: unknown");
                return;
            }

            Location myDockLocation = new Location("");
            myDockLocation.setLatitude(myDock.getLatitude());
            myDockLocation.setLongitude(myDock.getLongitude());

            float distanceInMeters = myDockLocation.distanceTo(myLocation);

            myOptionDock.myDistance.setText("Distance: " + parseDistance(distanceInMeters));
            myOptionDock.myDistanceTime.setText("Estimated Time: " + parseDistanceTime(distanceInMeters));
        });
    }

    private String parseDistance(float distanceInMeters) {

        String doubleAsString = String.valueOf(distanceInMeters);
        int indexOfDecimal = doubleAsString.indexOf(".");
        int nInt = Integer.parseInt(doubleAsString.substring(0, indexOfDecimal));

        if (nInt > 1000) {
            return nInt / 1000 + " kilometers";
        }

        return nInt + " meters";
    }

    private String parseDistanceTime(float distanceInMeters) {

        int timeInSeconds = (int) (distanceInMeters / 1.3);

        int hours = timeInSeconds / 3600;
        int secondsLeft = timeInSeconds - hours * 3600;
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft - minutes * 60;

        String formattedTime = "";
        if (hours < 10)
            formattedTime += "0";
        formattedTime += hours + ":";

        if (minutes < 10)
            formattedTime += "0";
        formattedTime += minutes + ":";

        if (seconds < 10)
            formattedTime += "0";
        formattedTime += seconds ;

        return formattedTime;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return myDockDataset.size();
    }
}
