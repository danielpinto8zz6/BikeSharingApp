package com.bikesharing.app.sign;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.bikesharing.app.R;
import com.bikesharing.app.home.HomeActivity;
import com.bikesharing.app.sign.login.LoginFragment;

public class SignActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        if (savedInstanceState == null) {

            SharedPreferences mySharedPreferences = getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);
            String szToken = mySharedPreferences.getString("token", null);

            if ((szToken != null) && (!szToken.isEmpty())) {

                String szEmail = mySharedPreferences.getString("email", null);
                if ((szEmail != null) || (!szEmail.isEmpty())) {

                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                }
            }

            LoginFragment myLoginFragment = new LoginFragment();

            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container, myLoginFragment, SignFragment.FRAGMENT_TAG)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {

        SignFragment myFragment = (SignFragment) getSupportFragmentManager().findFragmentByTag(SignFragment.FRAGMENT_TAG);

        if (myFragment == null) {
            super.onBackPressed();
            return;
        }

        if (myFragment.allowBackPressed()) { // and then you define a method allowBackPressed with the logic to allow back pressed or not

            myFragment.clearForm();
            super.onBackPressed();
        } else {
            displayLogoutDialog();
        }
    }

    private void displayLogoutDialog() {

        AlertDialog myDialog = new AlertDialog.Builder(this).create();
        myDialog.setTitle("Exit?");
        myDialog.setMessage("You want to exit?");
        myDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Ok", (dialog, which) -> {
            dialog.dismiss();
            finishAndRemoveTask();
        });
        myDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Back to app", (dialog, which) -> dialog.dismiss());

        myDialog.show();
    }
}