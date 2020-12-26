package com.bikesharing.app.sign.login.ui.login;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bikesharing.app.R;
import com.bikesharing.app.sign.forgot.ui.ForgotPasswordFragment;
import com.bikesharing.app.sign.register.ui.RegisterFragment;

public class LoginFragment extends Fragment implements View.OnClickListener {

    public LoginFragment() {
        super(R.layout.fragment_login);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        Window window = getActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR  | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        window.setStatusBarColor(getActivity().getColor(R.color.White));
        window.setNavigationBarColor(getActivity().getColor(R.color.White));

        ImageView myRegisterButton = view.findViewById(R.id.regist_button);
        myRegisterButton.setOnClickListener(this);

        TextView myRegisterText =  view.findViewById(R.id.regist_text);
        myRegisterText.setOnClickListener(this);

        ImageView myForgotPasswordButton = view.findViewById(R.id.forgotPassword_button);
        myForgotPasswordButton.setOnClickListener(this);

        TextView myForgotPasswordText =  view.findViewById(R.id.forgotPassword);
        myForgotPasswordText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.regist_button:
            case R.id.regist_text:

                onRegistClick();
                break;

            case R.id.forgotPassword:
            case R.id.forgotPassword_button:
                onForgotClick();
                break;
        }
    }

    public void onRegistClick(){

        RegisterFragment myRegisterFragment = new RegisterFragment();

        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, android.R.anim.fade_out)
                .replace(R.id.fragment_container, myRegisterFragment)
                .addToBackStack(null)
                .commit();
    }

    public void onForgotClick(){

        ForgotPasswordFragment myForgotFragment = new ForgotPasswordFragment();

        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_left, android.R.anim.fade_out)
                .replace(R.id.fragment_container, myForgotFragment)
                .addToBackStack(null)
                .commit();
    }
}