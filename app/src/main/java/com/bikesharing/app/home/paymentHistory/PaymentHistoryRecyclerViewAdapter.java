package com.bikesharing.app.home.paymentHistory;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bikesharing.app.R;
import com.bikesharing.app.data.payment.Payment;
import com.bikesharing.app.home.HomeActivity;
import com.bikesharing.app.payment.PaymentActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PaymentHistoryRecyclerViewAdapter extends RecyclerView.Adapter<PaymentHistoryRecyclerViewAdapter.MyViewHolder> {

    private HomeActivity myHomeActivity;

    private RecyclerView myRecyclerView;

    private ArrayList<Payment> myPaymentHistoryDataset = new ArrayList<>();

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView myPaymentAmount;
        public TextView myPaymentStatus;
        public TextView myPaymentDate;

        public MyViewHolder(View myOptionDock) {

            super(myOptionDock);

            this.myPaymentAmount = myOptionDock.findViewById(R.id.paymentAmount);
            this.myPaymentStatus = myOptionDock.findViewById(R.id.paymentStatus);
            this.myPaymentDate = myOptionDock.findViewById(R.id.paymentDate);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        this.myRecyclerView = recyclerView;
    }

    public PaymentHistoryRecyclerViewAdapter(HomeActivity myHomeActivity) {
        this.myHomeActivity = myHomeActivity;
    }

    public void addAll(ArrayList<Payment> myPaymentHistoryDataset) {

        for (Payment myPaymentHistory : myPaymentHistoryDataset) {
            this.add(myPaymentHistory);
        }
    }

    public void set(ArrayList<Payment> myPaymentHistoryDataset) {
        this.myPaymentHistoryDataset = myPaymentHistoryDataset;
        notifyDataSetChanged();
    }

    public void add(Payment myPaymentHistoryDataset) {
        this.myPaymentHistoryDataset.add(myPaymentHistoryDataset);
        notifyItemInserted(this.myPaymentHistoryDataset.size() - 1);
    }

    private final View.OnClickListener myDockClickListener = myView -> {

        int nPosition = this.myRecyclerView.getChildLayoutPosition(myView);
        Payment myPayment = myPaymentHistoryDataset.get(nPosition);

        Intent myIntent = new Intent(myHomeActivity.getApplicationContext(), PaymentActivity.class);
        myIntent.putExtra("Payment", myPayment);

        myHomeActivity.startActivity(myIntent);
        myHomeActivity.finish();
    };

    // Create new views (invoked by the layout manager)
    @NotNull
    @Override
    public PaymentHistoryRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View myOptionDock = LayoutInflater.from(parent.getContext()).inflate(R.layout.option_payment_history, parent, false);
        myOptionDock.setOnClickListener(myDockClickListener);
        return new MyViewHolder(myOptionDock);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder myOptionDock, int position) {

        Payment myPaymentHistoryDataset = this.myPaymentHistoryDataset.get(position);

        myOptionDock.myPaymentAmount.setText(String.valueOf(myPaymentHistoryDataset.getValue()));
        myOptionDock.myPaymentStatus.setText("Status: " + String.valueOf(myPaymentHistoryDataset.getStatus()));
        myOptionDock.myPaymentDate.setText("Date: " + myPaymentHistoryDataset.getTimestamp().toString());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return myPaymentHistoryDataset.size();
    }
}
