package com.bikesharing.app.sign.login;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bikesharing.app.R;
import com.bikesharing.app.data.Token;
import com.bikesharing.app.data.User;
import com.bikesharing.app.home.HomeActivity;
import com.bikesharing.app.rest.HttpStatus;
import com.bikesharing.app.rest.RestService;
import com.bikesharing.app.rest.RestServiceManager;
import com.bikesharing.app.sign.SignFragment;
import com.bikesharing.app.sign.forgot.ForgotPasswordFragment;
import com.bikesharing.app.sign.register.RegisterFragment;
import com.google.android.material.textfield.TextInputLayout;

import java.net.SocketTimeoutException;
import java.util.Objects;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class LoginFragment extends Fragment implements View.OnClickListener, SignFragment {

    private TextInputLayout textInputEmail;
    private TextInputLayout textInputPassword;

    CircularProgressButton myLoginButton;

    private boolean bIsEmailValid = false;
    private boolean bIsPasswordValid = false;

    private boolean bIsLoginDisabled = false;

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

        myLoginButton =  view.findViewById(R.id.cirLoginButton);
        myLoginButton.setBackgroundDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.button_background_light_green));
        myLoginButton.setOnClickListener(this);

        textInputEmail = view.findViewById(R.id.textInputEmail);
        textInputEmail.getEditText().addTextChangedListener(new LoginFragment.LoginTextWatcher(textInputEmail));

        textInputPassword = view.findViewById(R.id.textInputPassword);
        textInputPassword.getEditText().addTextChangedListener(new LoginFragment.LoginTextWatcher(textInputPassword));
    }

    @Override
    public boolean allowBackPressed() {
        return false;
    }

    @Override
    public void clearForm() {

        Objects.requireNonNull(textInputEmail.getEditText()).getText().clear();
        textInputEmail.setErrorEnabled(false);

        Objects.requireNonNull(textInputPassword.getEditText()).getText().clear();
        textInputPassword.setErrorEnabled(false);
    }

    @Override
    public void onResume() {

        clearForm();
        super.onResume();
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

            case R.id.cirLoginButton:
                onLoginClick();
                break;
        }
    }

    private void displayErrorLoginDialog(String szMessage){

        AlertDialog myDialog = new AlertDialog.Builder(getActivity()).create();
        myDialog.setTitle("Login Error!");
        myDialog.setMessage(szMessage);
        myDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        myDialog.show();
    }

    private void onLoginClick() {

        if (myLoginButton.isAnimating()) {
            return;
        }

        if (!bIsLoginDisabled) {

            AlertDialog myDialog = new AlertDialog.Builder(getActivity()).create();
            myDialog.setTitle("Wrong Inputs!");
            myDialog.setMessage("Input is missing or not correctly inserted");
            myDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            myDialog.show();
            return;
        }

        myLoginButton.revertAnimation();
        myLoginButton.startAnimation();

        RestService myRestService = RestServiceManager.getInstance().getRestService();
        Call<Token> myReturnedUser = myRestService.authenticator(new User(textInputEmail.getEditText().getText().toString(), textInputPassword.getEditText().getText().toString()));

        myReturnedUser.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {

                if (!response.isSuccessful()) {

                    myLoginButton.doneLoadingAnimation(R.color.Red, BitmapFactory.decodeResource(getResources(), R.drawable.error_cloud));

                    displayErrorLoginDialog(HttpStatus.getStatusText(response.code()));

                    myLoginButton.setClickable(true);
                    return;
                }

                myLoginButton.doneLoadingAnimation(R.color.DarkGreen, BitmapFactory.decodeResource(getResources(), R.drawable.cloud_checked));

                Toast toast = Toast.makeText(getContext(), "Login done", Toast.LENGTH_LONG);
                toast.show();

                SharedPreferences mySharedPreferences = getActivity().getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);
                mySharedPreferences.edit().putString("token", response.body().getSzToken()).apply();
                mySharedPreferences.edit().putString("email", textInputEmail.getEditText().getText().toString()).apply();

                Intent myIntent = new Intent(getActivity().getApplicationContext(), HomeActivity.class);

                startActivity(myIntent);
                getActivity().finish();
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {

                myLoginButton.doneLoadingAnimation(R.color.Red, BitmapFactory.decodeResource(getResources(), R.drawable.error_cloud));
                myLoginButton.setClickable(true);

                if (t instanceof SocketTimeoutException) {

                    displayErrorLoginDialog("Service unavailable, try again later.");
                    return;
                }

                displayErrorLoginDialog("Unknown error");
            }
        });
    }

    public void onRegistClick(){

        RegisterFragment myRegisterFragment = new RegisterFragment();

        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, android.R.anim.fade_out)
                .replace(R.id.fragment_container, myRegisterFragment, SignFragment.FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();
    }

    public void onForgotClick(){

        clearForm();

        ForgotPasswordFragment myForgotFragment = new ForgotPasswordFragment();

        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_left, android.R.anim.fade_out)
                .replace(R.id.fragment_container, myForgotFragment, SignFragment.FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public class LoginTextWatcher implements TextWatcher {

        private View myTextInputLayout;

        public LoginTextWatcher (View myTextInputLayout) {
            this.myTextInputLayout = myTextInputLayout;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //Nothing to be done
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //Nothing to be done
        }

        @Override
        public void afterTextChanged(Editable s) {

            switch (this.myTextInputLayout.getId()) {
                case R.id.textInputEmail:
                    validateEmail();
                    break;
                case R.id.textInputPassword:
                    validatePassword();
                    break;
            }

            if (bIsEmailValid && bIsPasswordValid) {

                myLoginButton.setBackgroundDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.button_background_dark_green));
                bIsLoginDisabled = true;
            } else {
                myLoginButton.setBackgroundDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.button_background_light_green));
                bIsLoginDisabled = false;
            }
        }

        private void validateEmail() {

            if (textInputEmail.getEditText().getText().toString().contains(" ")) {

                textInputEmail.setError("No Spaces Allowed!");
                requestFocus(textInputEmail.getEditText());
                bIsEmailValid = false;
                return;
            }

            if (textInputEmail.getEditText().getText().toString().trim().isEmpty()) {

                textInputEmail.setError("Email Required!");
                requestFocus(textInputEmail.getEditText());
                bIsEmailValid = false;
                return;
            }

            String emailId = textInputEmail.getEditText().getText().toString();
            Boolean  isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(emailId).matches();
            if (!isValid) {

                textInputEmail.setError("Invalid Email Address! ex: abc@example.com");
                requestFocus(textInputEmail.getEditText());
                bIsEmailValid = false;
                return;
            }

            textInputEmail.setErrorEnabled(false);
            bIsEmailValid = true;
        }

        private void validatePassword() {

            if (textInputPassword.getEditText().getText().toString().contains(" ")) {

                textInputPassword.setError("No Spaces Allowed!");
                requestFocus(textInputPassword.getEditText());
                bIsPasswordValid = false;
                return;
            }

            if (textInputPassword.getEditText().getText().toString().trim().isEmpty()) {

                textInputPassword.setError("Password Required!");
                requestFocus(textInputPassword.getEditText());
                bIsPasswordValid = false;
                return;
            }

            if(textInputPassword.getEditText().getText().toString().length() < 6){

                textInputPassword.setError("Password can't be less than 6 digit");
                requestFocus(textInputPassword.getEditText());
                bIsPasswordValid = false;
                return;
            }

            if(textInputPassword.getEditText().getText().toString().length() > 12){

                textInputPassword.setError("Password can't be bigger than 12 digit");
                requestFocus(textInputPassword.getEditText());
                bIsPasswordValid = false;
                return;
            }

            textInputPassword.setErrorEnabled(false);
            bIsPasswordValid = true;
        }
    }
}