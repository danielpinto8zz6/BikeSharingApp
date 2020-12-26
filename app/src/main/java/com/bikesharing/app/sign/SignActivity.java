package com.bikesharing.app.sign;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bikesharing.app.R;
import com.bikesharing.app.sign.login.ui.login.LoginFragment;

public class SignActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        if (savedInstanceState == null) {

            LoginFragment myLoginFragment = new LoginFragment();

            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container, myLoginFragment, null)
                    .commit();
        }
    }
}