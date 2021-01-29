package com.bikesharing.app.sign.register;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bikesharing.app.R;
import com.bikesharing.app.data.User;
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

public class RegisterFragment extends Fragment implements View.OnClickListener, SignFragment {

    private TextInputLayout textInputName;
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputPassword;
    private TextInputLayout textInputPasswordCheck;

    CircularProgressButton myRegisterButton;

    private boolean bIsRegisterDisabled = false;

    private boolean bIsNameValid = false;
    private boolean bIsEmailValid = false;
    private boolean bIsPasswordValid = false;
    private boolean bIsPasswordCheckValid = false;

    public RegisterFragment() {
        super(R.layout.fragment_register);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        Window window = getActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        window.setStatusBarColor(getActivity().getColor(R.color.DarkGreen));
        window.setNavigationBarColor(getActivity().getColor(R.color.DarkGreen));

        ImageView myLoginButton = view.findViewById(R.id.login_button);
        myLoginButton.setOnClickListener(this);

        TextView myLoginText =  view.findViewById(R.id.login_text);
        myLoginText.setOnClickListener(this);

        myRegisterButton =  view.findViewById(R.id.cirRegisterButton);
        myRegisterButton.setBackgroundDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.button_background_light_green));
        myRegisterButton.setOnClickListener(this);

        textInputName = view.findViewById(R.id.textInputName);
        textInputName.getEditText().addTextChangedListener(new RegisterTextWatcher(textInputName));

        textInputEmail = view.findViewById(R.id.textInputEmail);
        textInputEmail.getEditText().addTextChangedListener(new RegisterTextWatcher(textInputEmail));

        textInputPassword = view.findViewById(R.id.textInputPassword);
        textInputPassword.getEditText().addTextChangedListener(new RegisterTextWatcher(textInputPassword));

        textInputPasswordCheck = view.findViewById(R.id.textInputPasswordCheck);
        textInputPasswordCheck.getEditText().addTextChangedListener(new RegisterTextWatcher(textInputPasswordCheck));
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

        Objects.requireNonNull(textInputName.getEditText()).getText().clear();
        textInputName.setErrorEnabled(false);

        Objects.requireNonNull(textInputEmail.getEditText()).getText().clear();
        textInputEmail.setErrorEnabled(false);

        Objects.requireNonNull(textInputPassword.getEditText()).getText().clear();
        textInputPassword.setErrorEnabled(false);

        Objects.requireNonNull(textInputPasswordCheck.getEditText()).getText().clear();
        textInputPasswordCheck.setErrorEnabled(false);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.login_button:
            case R.id.login_text:

                onLoginClick();
                break;

            case R.id.cirRegisterButton:
                onRegistClick();
                break;
        }
    }

    private void displayErrorRegisterDialog(String szMessage){

        AlertDialog myDialog = new AlertDialog.Builder(getActivity()).create();
        myDialog.setTitle("Register Error!");
        myDialog.setMessage(szMessage);
        myDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        myDialog.show();
    }

    private void onRegistClick() {

        if (!bIsRegisterDisabled) {

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

        myRegisterButton.revertAnimation();
        myRegisterButton.startAnimation();

        RestService myRestService = RestServiceManager.getInstance().getRestService();
        Call<Void> myReturnedUser = myRestService.addUser(new User(textInputName.getEditText().getText().toString(), textInputEmail.getEditText().getText().toString(), textInputPassword.getEditText().getText().toString()));

        myReturnedUser.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (!response.isSuccessful()) {

                    myRegisterButton.doneLoadingAnimation(R.color.Red, BitmapFactory.decodeResource(getResources(), R.drawable.error_cloud));
                    displayErrorRegisterDialog(HttpStatus.getStatusText(response.code()));

                    myRegisterButton.setClickable(true);
                    return;
                }

                myRegisterButton.doneLoadingAnimation(R.color.DarkGreen, BitmapFactory.decodeResource(getResources(), R.drawable.cloud_checked));

                Toast toast = Toast.makeText(getContext(), "Regist done", Toast.LENGTH_LONG);
                toast.show();

                onLoginClick();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

                myRegisterButton.doneLoadingAnimation(R.color.Red, BitmapFactory.decodeResource(getResources(), R.drawable.error_cloud));
                myRegisterButton.setClickable(true);

                if (t instanceof SocketTimeoutException) {

                    displayErrorRegisterDialog("Service unavailable, try again later.");
                    return;
                }

                displayErrorRegisterDialog("Unknown error");
            }
        });
    }

    public void onLoginClick(){

        LoginFragment myLoginFragment = new LoginFragment();

        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_left, android.R.anim.fade_out)
                .replace(R.id.fragment_container, myLoginFragment, SignFragment.FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public class RegisterTextWatcher implements TextWatcher {

        private View myTextInputLayout;

        public RegisterTextWatcher (View myTextInputLayout) {
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
                case R.id.textInputName:
                    validateName();
                    break;
                case R.id.textInputEmail:
                    validateEmail();
                    break;
                case R.id.textInputPassword:
                    validatePassword();
                    break;
                case R.id.textInputPasswordCheck:
                    validatePasswordCheck(false);
                    break;
            }

            if (bIsNameValid && bIsEmailValid && bIsPasswordValid && bIsPasswordCheckValid) {

                myRegisterButton.setBackgroundDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.button_background_dark_green));
                bIsRegisterDisabled = true;
            } else {
                myRegisterButton.setBackgroundDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.button_background_light_green));
                bIsRegisterDisabled = false;
            }
        }

        private void validateName() {

            if (textInputName.getEditText().getText().toString().trim().isEmpty()) {

                textInputName.setError("Name Required!");
                requestFocus(textInputName.getEditText());
                bIsNameValid = false;
                return;
            }

            textInputName.setErrorEnabled(false);
            bIsNameValid = true;
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

            validatePasswordCheck(false);
        }

        private void validatePasswordCheck(boolean bIsFocus) {

            if (!textInputPassword.getEditText().getText().toString().trim().equalsIgnoreCase(textInputPasswordCheck.getEditText().getText().toString().trim())) {

                textInputPasswordCheck.setError("Passwords Not Equal!");
                if (bIsFocus) {
                    requestFocus(textInputPasswordCheck.getEditText());
                }
                bIsPasswordCheckValid = false;
            }  else {

                textInputPasswordCheck.setErrorEnabled(false);
                bIsPasswordCheckValid = true;
            }
        }
    }
}