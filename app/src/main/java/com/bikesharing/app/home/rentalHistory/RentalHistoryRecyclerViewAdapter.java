package com.bikesharing.app.home.rentalHistory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bikesharing.app.R;
import com.bikesharing.app.data.Rental;
import com.bikesharing.app.home.HomeActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;

public class RentalHistoryRecyclerViewAdapter extends RecyclerView.Adapter<RentalHistoryRecyclerViewAdapter.MyViewHolder> {

    private HomeActivity myHomeActivity;

    private RecyclerView myRecyclerView;

    private ArrayList<Rental> myRentalHistoryDataset = new ArrayList<>();

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView myRentalId;
        public TextView myRentalStartDate;
        public TextView myRentalEndDate;
        public TextView myRentalTime;

        public MyViewHolder(View myOptionDock) {

            super(myOptionDock);

            this.myRentalId = myOptionDock.findViewById(R.id.rentalId);
            this.myRentalStartDate = myOptionDock.findViewById(R.id.rentalStartDate);
            this.myRentalEndDate = myOptionDock.findViewById(R.id.rentalEndDate);
            this.myRentalTime = myOptionDock.findViewById(R.id.rentalTime);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        this.myRecyclerView = recyclerView;
    }

    public RentalHistoryRecyclerViewAdapter(HomeActivity myHomeActivity) {
        this.myHomeActivity = myHomeActivity;
    }

    public void addAll(ArrayList<Rental> myRentalHistoryDataset) {

        for (Rental myRentalHistory : myRentalHistoryDataset) {
            this.add(myRentalHistory);
        }
    }

    public void set(ArrayList<Rental> myRentalHistoryDataset) {
        this.myRentalHistoryDataset = myRentalHistoryDataset;
        notifyDataSetChanged();
    }

    public void add(Rental myRentalHistory) {
        this.myRentalHistoryDataset.add(myRentalHistory);
        notifyItemInserted(this.myRentalHistoryDataset.size() - 1);
    }

    // Create new views (invoked by the layout manager)
    @NotNull
    @Override
    public RentalHistoryRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View myOptionDock = LayoutInflater.from(parent.getContext()).inflate(R.layout.option_rental_history, parent, false);
        return new MyViewHolder(myOptionDock);
    }

    // Function to print difference in
    // time start_date and end_date
    private String findDifference(Date d1, Date d2) {

        // Try Block

        // Calucalte time difference
        // in milliseconds
        long difference_In_Time
                = d2.getTime() - d1.getTime();

        // Calucalte time difference in
        // seconds, minutes, hours, years,
        // and days
        long difference_In_Seconds
                = (difference_In_Time
                / 1000)
                % 60;

        long difference_In_Minutes
                = (difference_In_Time
                / (1000 * 60))
                % 60;

        long difference_In_Hours
                = (difference_In_Time
                / (1000 * 60 * 60))
                % 24;

        long difference_In_Years
                = (difference_In_Time
                / (1000l * 60 * 60 * 24 * 365));

        long difference_In_Days
                = (difference_In_Time
                / (1000 * 60 * 60 * 24))
                % 365;

        return difference_In_Years
                        + " years, "
                        + difference_In_Days
                        + " days, "
                        + difference_In_Hours
                        + ":"
                        + difference_In_Minutes
                        + ":"
                        + difference_In_Seconds;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder myOptionDock, int position) {

        Rental myRentalHistoryDataset = this.myRentalHistoryDataset.get(position);

        myOptionDock.myRentalId.setText("Rental nº" + myRentalHistoryDataset.getId());
        myOptionDock.myRentalStartDate.setText("Started:" + myRentalHistoryDataset.getStartDate().toString());
        myOptionDock.myRentalEndDate.setText("Ended:" + myRentalHistoryDataset.getEndDate().toString());
        myOptionDock.myRentalTime.setText("Time:" + findDifference(myRentalHistoryDataset.getStartDate(), myRentalHistoryDataset.getEndDate()));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return myRentalHistoryDataset.size();
    }
}
