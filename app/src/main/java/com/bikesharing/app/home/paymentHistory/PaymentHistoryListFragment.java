package com.bikesharing.app.home.paymentHistory;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bikesharing.app.R;
import com.bikesharing.app.data.Page.Page;
import com.bikesharing.app.data.payment.Payment;
import com.bikesharing.app.home.HomeActivity;
import com.bikesharing.app.home.HomeFragment;
import com.bikesharing.app.rest.HttpStatus;
import com.bikesharing.app.rest.RestService;
import com.bikesharing.app.rest.RestServiceManager;
import com.bikesharing.app.utils.PaginationScrollListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentHistoryListFragment extends Fragment implements HomeFragment {

    private PaymentHistoryRecyclerViewAdapter myPaymentHistoryRecyclerViewAdapter;
    private SwipeRefreshLayout mySwipeRefreshLayout;

    private int nPage = 0;
    private static final int nSize = 10;
    private int nTotalPages = 1;

    private boolean isLoading = false;

    private String szToken;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_list, container, false);
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

        RecyclerView myRecyclerView = view.findViewById(R.id.dock_recycler_view);
        this.mySwipeRefreshLayout = view.findViewById(R.id.swipeRefresh);

        this.mySwipeRefreshLayout.setOnRefreshListener(this::onLoad);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        myRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(getActivity());
        myRecyclerView.setLayoutManager(myLayoutManager);

        this.myPaymentHistoryRecyclerViewAdapter = new PaymentHistoryRecyclerViewAdapter((HomeActivity) getActivity());

        // specify an adapter (see also next example)
//            this.myDockRecyclerViewAdapter = new DockRecyclerViewAdapter((HomeActivity) getActivity());
        myRecyclerView.setAdapter(this.myPaymentHistoryRecyclerViewAdapter);

        myRecyclerView.addOnScrollListener(new PaginationScrollListener(myLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                mySwipeRefreshLayout.setRefreshing(true);

                nPage += 1;

                loadHistory(nPage, nSize, true, szToken);
            }

            @Override
            public int getTotalPageCount() {
                return nTotalPages;
            }

            @Override
            public boolean isLastPage() {
                return nPage == nTotalPages;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        myRecyclerView.addItemDecoration(new DividerItemDecoration(myRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        loadHistory(nPage, nSize, true, szToken);
    }

    private void onLoad() {
        nPage = 0;
        myPaymentHistoryRecyclerViewAdapter.set(new ArrayList<>());

        loadHistory(nPage, nSize, true, szToken);

        mySwipeRefreshLayout.setRefreshing(false); // Disables the refresh icon
    }

    private void loadHistory(int nPage, int nSize, boolean bOnlyBikes, String szToken) {

        RestService myRestService = RestServiceManager.getInstance().getRestService();
        Call<Page<Payment>> myReturnedUser = myRestService.getAllPaymentHistory(nPage, PaymentHistoryListFragment.nSize,  "Bearer " + szToken);

        myReturnedUser.enqueue(new Callback<Page<Payment>>() {

            @Override
            public void onResponse(@NotNull Call<Page<Payment>> call, @NotNull Response<Page<Payment>> response) {

                if (!response.isSuccessful()) {

                    ((HomeActivity) getActivity()).displayErrorExitDialog("Error", HttpStatus.getStatusText(response.code()));
                    return;
                }

                nTotalPages = (int) response.body().getTotalPages();
                myPaymentHistoryRecyclerViewAdapter.addAll((ArrayList<Payment>) response.body().getContent());

                isLoading = false;
                mySwipeRefreshLayout.setRefreshing(isLoading);

                Toast.makeText(getContext(), "Dock list updated", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Page<Payment>> call, Throwable t) {
                ((HomeActivity) getActivity()).displayErrorExitDialog("Error", t.getMessage());
            }
        });
    }

    @Override
    public boolean allowBackPressed() {
        return false;
    }

    @Override
    public int getFragmentType() {
        return HomeFragment.FRAGMENT_TYPE_PAYMENT_HISTORY;
    }
}