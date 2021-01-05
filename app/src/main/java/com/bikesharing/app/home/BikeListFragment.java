package com.bikesharing.app.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bikesharing.app.R;
import com.bikesharing.app.data.Dock;
import com.bikesharing.app.data.User;
import com.bikesharing.app.rest.HttpStatus;
import com.bikesharing.app.rest.RestService;
import com.bikesharing.app.rest.RestServiceManager;
import com.bikesharing.app.sign.login.LoginFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BikeListFragment extends Fragment implements HomeFragment {

    private int nPage = 0;
    private int nSize = 10;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_bike_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        SharedPreferences mySharedPreferences = getActivity().getSharedPreferences("com.mycompany.myAppName", getContext().MODE_PRIVATE);
        String szToken = mySharedPreferences.getString("token", null);

        if (savedInstanceState == null) {

            if ((szToken == null) ||
                (szToken.isEmpty())) {

                ((HomeActivity)getActivity()).displayErrorExitDialog("Token", "Missing Token");
                return;
            }

            getDockList(nPage,nSize, true, szToken);
        }

    }

    private void getDockList(int nPage, int nSize, boolean bOnlyBikes, String szToken) {

        RestService myRestService = RestServiceManager.getInstance().getRestService();
        Call<List<Dock>> myReturnedUser = myRestService.getAllDocks(nPage, nSize, bOnlyBikes, szToken);

        myReturnedUser.enqueue(new Callback<List<Dock>>() {

            @Override
            public void onResponse(Call<List<Dock>> call, Response<List<Dock>> response) {

                if (!response.isSuccessful()) {

                    ((HomeActivity)getActivity()).displayErrorExitDialog("Error", HttpStatus.getStatusText(response.code()));
                    return;
                }

            }

            @Override
            public void onFailure(Call<List<Dock>> call, Throwable t) {
                ((HomeActivity)getActivity()).displayErrorExitDialog("Error", t.getMessage());
            }
        });
    }

    @Override
    public boolean allowBackPressed() {
        return true;
    }

    @Override
    public int getFragmentType() {
        return HomeFragment.FRAGMENT_TYPE_BIKE_LIST;
    }
}