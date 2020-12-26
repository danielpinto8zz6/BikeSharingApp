package com.bikesharing.app.sign.forgot.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bikesharing.app.R;
import com.bikesharing.app.sign.login.ui.login.LoginFragment;

public class ForgotPasswordFragment extends Fragment implements View.OnClickListener {

    public ForgotPasswordFragment() {
        super(R.layout.fragment_forgot_password);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        Window window = getActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        window.setStatusBarColor(getActivity().getColor(R.color.DarkGreen));
        window.setNavigationBarColor(getActivity().getColor(R.color.DarkGreen));

        ImageView myRegisterButton = view.findViewById(R.id.login_button);
        myRegisterButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.login_button:

                onLoginClick();
                break;
        }
    }

    public void onLoginClick(){

        LoginFragment myLoginFragment = new LoginFragment();

        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, android.R.anim.fade_out)
                .replace(R.id.fragment_container, myLoginFragment)
                .addToBackStack(null)
                .commit();
    }
}