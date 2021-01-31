package com.bikesharing.app.home.rentalHistory;

import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bikesharing.app.R;
import com.bikesharing.app.data.Rental;
import com.bikesharing.app.data.payment.Payment;
import com.bikesharing.app.home.HomeActivity;
import com.bikesharing.app.home.rentalHistory.locationHistory.LocationHistoryActivity;
import com.bikesharing.app.payment.PaymentActivity;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    private final View.OnClickListener myLocationClickListener = myView -> {

        int nPosition = this.myRecyclerView.getChildLayoutPosition(myView);
        Rental myRental = myRentalHistoryDataset.get(nPosition);

        Intent myIntent = new Intent(myHomeActivity.getApplicationContext(), LocationHistoryActivity.class);
        myIntent.putExtra("rental", myRental);

        myHomeActivity.startActivity(myIntent);
        myHomeActivity.finish();
    };

    // Create new views (invoked by the layout manager)
    @NotNull
    @Override
    public RentalHistoryRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View myOptionDock = LayoutInflater.from(parent.getContext()).inflate(R.layout.option_rental_history, parent, false);
        myOptionDock.setOnClickListener(myLocationClickListener);
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


        if (difference_In_Years > 0) {

            return difference_In_Years
                    + " years, "
                    + difference_In_Days
                    + " days, "
                    + difference_In_Hours
                    + ":"
                    + difference_In_Minutes
                    + ":"
                    + difference_In_Seconds;
        } else if (difference_In_Days > 0) {

            return difference_In_Days
                    + " days, "
                    + difference_In_Hours
                    + ":"
                    + difference_In_Minutes
                    + ":"
                    + difference_In_Seconds;
        } else {
            return difference_In_Hours
                    + ":"
                    + difference_In_Minutes
                    + ":"
                    + difference_In_Seconds;
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder myOptionDock, int position) {

        Rental myRentalHistoryDataset = this.myRentalHistoryDataset.get(position);

        myOptionDock.myRentalId.setText("Rental nº" + myRentalHistoryDataset.getId());
        try {

            myOptionDock.myRentalStartDate.setText("Started: " + DateUtils.formatDateTime(myHomeActivity.getApplicationContext(), myRentalHistoryDataset.getStartDate().getTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_TIME));
            myOptionDock.myRentalEndDate.setText("Ended: " + DateUtils.formatDateTime(myHomeActivity.getApplicationContext(), myRentalHistoryDataset.getEndDate().getTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_TIME));
            myOptionDock.myRentalTime.setText("Time: " + findDifference(myRentalHistoryDataset.getStartDate(), myRentalHistoryDataset.getEndDate()));

        } catch (Exception e) {
            myOptionDock.myRentalStartDate.setText("Started: Unknown");
            myOptionDock.myRentalEndDate.setText("Ended: Unknown");
            myOptionDock.myRentalTime.setText("Time: Unknown");
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return myRentalHistoryDataset.size();
    }
}
