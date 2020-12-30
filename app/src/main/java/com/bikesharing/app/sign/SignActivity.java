package com.bikesharing.app.sign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.bikesharing.app.R;
import com.bikesharing.app.sign.login.LoginFragment;

public class SignActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        SharedPreferences mySharedPreferences = getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);
        String szToken = mySharedPreferences.getString("token", null);

        if ((savedInstanceState == null) &&
            ((szToken == null) ||
             (szToken.isEmpty()))) {

            LoginFragment myLoginFragment = new LoginFragment();

            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container, myLoginFragment, null)
                    .commit();
        }
    }
}