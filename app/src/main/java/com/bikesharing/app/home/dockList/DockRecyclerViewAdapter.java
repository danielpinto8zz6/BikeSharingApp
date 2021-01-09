package com.bikesharing.app.home.dockList;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bikesharing.app.R;
import com.bikesharing.app.data.Dock;
import com.bikesharing.app.gps.GpsActivity;
import com.bikesharing.app.home.HomeActivity;

import java.util.ArrayList;

public class DockRecyclerViewAdapter extends RecyclerView.Adapter<DockRecyclerViewAdapter.MyViewHolder> {

    private HomeActivity myHomeActivity;

    private RecyclerView myRecyclerView;

    private ArrayList<Dock> myDockDataset = new ArrayList<>();

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView myLocation;
        public TextView myNumberOfBikes;
        public TextView myDistance;
        public TextView myDistanceTime;

        public MyViewHolder(View myOptionDock) {

            super(myOptionDock);

            this.myLocation = myOptionDock.findViewById(R.id.dockLocation);
            this.myNumberOfBikes = myOptionDock.findViewById(R.id.numberOfBikes);
            this.myDistance = myOptionDock.findViewById(R.id.distance);
            this.myDistanceTime = myOptionDock.findViewById(R.id.distance_time);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        this.myRecyclerView = recyclerView;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public DockRecyclerViewAdapter(HomeActivity myHomeActivity) {
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

        Intent myIntent = new Intent(myHomeActivity.getApplicationContext(), GpsActivity.class);
        myIntent.putExtra("Dock", myDock);

        myHomeActivity.startActivity(myIntent);
        myHomeActivity.finish();
    };

    // Create new views (invoked by the layout manager)
    @Override
    public DockRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View myOptionDock = LayoutInflater.from(parent.getContext()).inflate(R.layout.option_dock, parent, false);
        myOptionDock.setOnClickListener(myDockClickListener);
        return new MyViewHolder(myOptionDock);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder myOptionDock, int position) {

        Dock myDock = this.myDockDataset.get(position);

        myOptionDock.myLocation.setText(myDock.getLocation());
        myOptionDock.myNumberOfBikes.setText(String.valueOf(myDock.getNumberOfBikes()));
        myOptionDock.myDistance.setText(String.valueOf(this.myDockDataset.get(position).getDistance()));
        myOptionDock.myDistanceTime.setText(String.valueOf(this.myDockDataset.get(position).getDistanceTime()));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return myDockDataset.size();
    }
}
