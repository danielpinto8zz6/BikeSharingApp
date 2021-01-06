package com.bikesharing.app.home.dockList;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bikesharing.app.R;
import com.bikesharing.app.data.Dock;
import com.bikesharing.app.data.Page.Page;
import com.bikesharing.app.home.HomeActivity;
import com.bikesharing.app.home.HomeFragment;
import com.bikesharing.app.rest.HttpStatus;
import com.bikesharing.app.rest.RestService;
import com.bikesharing.app.rest.RestServiceManager;
import com.bikesharing.app.utils.PaginationScrollListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DockListFragment extends Fragment implements HomeFragment {

    private RecyclerView myRecyclerView;
    private DockRecyclerViewAdapter myDockRecyclerViewAdapter;

    private int nPage = 0;
    private static final int nSize = 10;
    private int nTotalPages = 1;

    private boolean isLoading = false;
    private boolean isLastPage = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_dock_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState == null) {

            SharedPreferences mySharedPreferences = getActivity().getSharedPreferences("com.mycompany.myAppName", Context.MODE_PRIVATE);
            String szToken = mySharedPreferences.getString("token", null);

            if ((szToken == null) ||
                    (szToken.isEmpty())) {

                ((HomeActivity) getActivity()).displayErrorExitDialog("Token", "Missing Token");
                return;
            }

            this.myRecyclerView = view.findViewById(R.id.dock_recycler_view);

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            this.myRecyclerView.setHasFixedSize(true);

            // use a linear layout manager
            LinearLayoutManager myLayoutManager = new LinearLayoutManager(getActivity());
            this.myRecyclerView.setLayoutManager(myLayoutManager);

            // specify an adapter (see also next example)
            this.myDockRecyclerViewAdapter = new DockRecyclerViewAdapter();
            this.myRecyclerView.setAdapter(this.myDockRecyclerViewAdapter);

            myRecyclerView.addOnScrollListener(new PaginationScrollListener(myLayoutManager) {
                @Override
                protected void loadMoreItems() {
                    isLoading = true;
                    nPage += 1;

                    loadDocks(nPage, nSize, true, szToken);
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

            this.myRecyclerView.setItemAnimator(new DefaultItemAnimator());
            this.myRecyclerView.addItemDecoration(new DividerItemDecoration(myRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

            loadDocks(this.nPage, this.nSize, true, szToken);
        }


    }

    private void loadDocks(int nPage, int nSize, boolean bOnlyBikes, String szToken) {
        Toast.makeText(getContext(), "Loading docks", Toast.LENGTH_SHORT);

        RestService myRestService = RestServiceManager.getInstance().getRestService();
        Call<Page<Dock>> myReturnedUser = myRestService.getAllDocks(nPage, nSize, bOnlyBikes, "Bearer " + szToken);

        myReturnedUser.enqueue(new Callback<Page<Dock>>() {

            @Override
            public void onResponse(Call<Page<Dock>> call, Response<Page<Dock>> response) {

                if (!response.isSuccessful()) {

                    ((HomeActivity) getActivity()).displayErrorExitDialog("Error", HttpStatus.getStatusText(response.code()));
                    return;
                }

                nTotalPages = (int) response.body().getTotalPages();
                myDockRecyclerViewAdapter.addAll(response.body().getContent());

                isLoading = false;
            }

            @Override
            public void onFailure(Call<Page<Dock>> call, Throwable t) {
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
        return HomeFragment.FRAGMENT_TYPE_DOCK_LIST;
    }
}