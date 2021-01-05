package com.bikesharing.app.sign.forgot;

import android.content.DialogInterface;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bikesharing.app.R;
import com.bikesharing.app.data.User;
import com.bikesharing.app.home.HomeFragment;
import com.bikesharing.app.rest.HttpStatus;
import com.bikesharing.app.rest.RestService;
import com.bikesharing.app.rest.RestServiceManager;
import com.bikesharing.app.sign.SignFragment;
import com.bikesharing.app.sign.login.LoginFragment;
import com.google.android.material.textfield.TextInputLayout;

import java.net.SocketTimeoutException;
import java.util.Objects;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordFragment extends Fragment implements View.OnClickListener, SignFragment {

    private TextInputLayout textInputEmail;

    CircularProgressButton myRecoverButton;

    private boolean bIsEmailValid = false;

    private boolean bIsRecoverDisabled = false;

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
    public void onResume() {

        clearForm();
        super.onResume();
    }


    @Override
    public boolean allowBackPressed() {
        return true;
    }

    @Override
    public void clearForm() {

        Objects.requireNonNull(textInputEmail.getEditText()).getText().clear();
        textInputEmail.setErrorEnabled(false);
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

        myRecoverButton =  view.findViewById(R.id.cirRecoverButton);
        myRecoverButton.setBackgroundDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.button_background_light_green));
        myRecoverButton.setOnClickListener(this);

        textInputEmail = view.findViewById(R.id.textInputEmail);
        textInputEmail.getEditText().addTextChangedListener(new ForgotPasswordFragment.RecoverTextWatcher(textInputEmail));
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.login_button:

                onLoginClick();
                break;

            case R.id.cirRecoverButton:

                onRecoverClick();
                break;
        }
    }

    private void displayErrorForgotPasswordDialog(String szMessage){

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

    private void onRecoverClick() {

        if (!bIsRecoverDisabled) {

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

        myRecoverButton.revertAnimation();
        myRecoverButton.startAnimation();

        RestService myRestService = RestServiceManager.getInstance().getRestService();
        Call<String> myReturnedUser = myRestService.forgetPassword(new User(textInputEmail.getEditText().getText().toString()));

        myReturnedUser.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (!response.isSuccessful()) {

                    myRecoverButton.doneLoadingAnimation(R.color.Red, BitmapFactory.decodeResource(getResources(), R.drawable.error_cloud));

                    displayErrorForgotPasswordDialog(HttpStatus.getStatusText(response.code()));

                    myRecoverButton.setClickable(true);
                    return;
                }

                myRecoverButton.doneLoadingAnimation(R.color.DarkGreen, BitmapFactory.decodeResource(getResources(), R.drawable.cloud_checked));

                Toast toast = Toast.makeText(getContext(), "Recover done", Toast.LENGTH_LONG);
                toast.show();

                onLoginClick();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                myRecoverButton.doneLoadingAnimation(R.color.Red, BitmapFactory.decodeResource(getResources(), R.drawable.error_cloud));
                myRecoverButton.setClickable(true);

                if (t instanceof SocketTimeoutException) {

                    displayErrorForgotPasswordDialog("Service unavailable, try again later.");
                    return;
                }

                displayErrorForgotPasswordDialog("Unknown error");
            }
        });
    }

    public void onLoginClick(){

        LoginFragment myLoginFragment = new LoginFragment();

        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, android.R.anim.fade_out)
                .replace(R.id.fragment_container, myLoginFragment, SignFragment.FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public class RecoverTextWatcher implements TextWatcher {

        private View myTextInputLayout;

        public RecoverTextWatcher (View myTextInputLayout) {
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
            }

            if (bIsEmailValid) {

                myRecoverButton.setBackgroundDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.button_background_dark_green));
                bIsRecoverDisabled = true;
            } else {
                myRecoverButton.setBackgroundDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.button_background_light_green));
                bIsRecoverDisabled = false;
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
    }
}