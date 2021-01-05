package com.bikesharing.app.sign;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.bikesharing.app.R;
import com.bikesharing.app.home.HomeActivity;
import com.bikesharing.app.sign.login.LoginFragment;

import java.util.List;

public class SignActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        if (savedInstanceState == null) {

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

    private void displayLogoutDialog(){

        AlertDialog myDialog = new AlertDialog.Builder(this).create();
        myDialog.setTitle("Exit?");
        myDialog.setMessage("You want to exit?");
        myDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finishAndRemoveTask();
                    }
                });
        myDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Back to app",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        myDialog.show();
    }
}