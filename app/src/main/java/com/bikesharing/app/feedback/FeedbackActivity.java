package com.bikesharing.app.feedback;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bikesharing.app.R;
import com.bikesharing.app.data.Feedback;
import com.bikesharing.app.data.User;
import com.bikesharing.app.home.HomeActivity;
import com.bikesharing.app.home.settings.SettingsFragment;
import com.bikesharing.app.rest.HttpStatus;
import com.bikesharing.app.rest.RestService;
import com.bikesharing.app.rest.RestServiceManager;
import com.bikesharing.app.sign.SignActivity;
import com.bikesharing.app.sign.SignFragment;
import com.bikesharing.app.sign.forgot.ForgotPasswordFragment;
import com.bikesharing.app.sign.login.LoginFragment;

import java.net.SocketTimeoutException;
import java.util.Scanner;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener {

    private String szToken;
    private String szEmail;
    private int nRentalId;

    private CircularProgressButton myFeedbackProgressButton;
    private EditText myEditTextFeedback;
    private RadioGroup myRadioGroup;
    private ImageView myImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR  | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        window.setStatusBarColor(getColor(R.color.DarkGreen));
        window.setNavigationBarColor(getColor(R.color.DarkGreen));

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        SharedPreferences mySharedPreferences = getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);
        this.szToken = mySharedPreferences.getString("token", null);
        this.szEmail = mySharedPreferences.getString("email", null);
        this.nRentalId = getIntent().getIntExtra("rental" , -1);

        if (((this.szToken == null) || (this.szToken.isEmpty())) ||
            ((this.szEmail == null) || (this.szEmail.isEmpty())) ||
            (nRentalId == -1)) {

            startActivity(new Intent(getApplicationContext(), SignActivity.class));
            finish();
        }

        myFeedbackProgressButton = findViewById(R.id.cirFeedbackButton);
        myFeedbackProgressButton.setOnClickListener(this);

        myRadioGroup = findViewById(R.id.feedbackRadioGroup);

        myEditTextFeedback = findViewById(R.id.editTextFeedback);

        myImageView = findViewById(R.id.exit_feedback_button);
        myImageView.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.cirFeedbackButton:

                onFeedbackClick();
                break;

            case R.id.exit_feedback_button:

                onSkipClick();
                break;
        }
    }

    private void onFeedbackClick() {

        String szDescription = myEditTextFeedback.getText().toString();
        if ((szDescription.isEmpty()) &&
            (myRadioGroup.getCheckedRadioButtonId() == -1)) {

            AlertDialog myDialog = new AlertDialog.Builder(this).create();
            myDialog.setTitle("Missing Inputs!");
            myDialog.setMessage("At least one option (Level or description) need to be used");
            myDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            myDialog.show();
            return;
        }

        int nRating = 1;
        switch (myRadioGroup.getCheckedRadioButtonId())
        {
            case R.id.feedbackBad:
                nRating = 1;
                break;
            case R.id.feedbackNormal:
                nRating = 2;
                break;
            case R.id.feedbackGreat:
                nRating = 3;
                break;
        }

        RestService myRestService = RestServiceManager.getInstance().getRestService();
        Call<Void> myReturnedUser = myRestService.sendfeedback(new Feedback(this.szEmail, this.nRentalId, szDescription, nRating), "Bearer " + szToken);

        myReturnedUser.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });

        onSkipClick();
    }

    public void onSkipClick(){

        SharedPreferences mySharedPreferences = getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);
        mySharedPreferences.edit().clear().apply();

        mySharedPreferences.edit().putString("token", this.szToken).apply();
        mySharedPreferences.edit().putString("email", this.szEmail).apply();
        mySharedPreferences.edit().putBoolean("isFirstRun", false).apply();

        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finish();
    }
}