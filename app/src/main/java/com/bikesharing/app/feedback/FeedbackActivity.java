package com.bikesharing.app.feedback;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import com.bikesharing.app.data.User;
import com.bikesharing.app.home.HomeActivity;
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

        SharedPreferences mySharedPreferences = getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);
        this.szToken = mySharedPreferences.getString("token", null);

        if ((this.szToken == null) && (this.szToken.isEmpty())) {

            startActivity(new Intent(getApplicationContext(), SignActivity.class));
            finish();
        }

        myFeedbackProgressButton = findViewById(R.id.cirFeedbackButton);
        myFeedbackProgressButton.setOnClickListener(this);

        myRadioGroup = findViewById(R.id.feedbackRadioGroup);

        myEditTextFeedback = findViewById(R.id.editTextFeedback);

        myImageView = findViewById(R.id.exit_feedback_button);
        myImageView.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_background_light_green));
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

        //TODO feedback
        //RestService myRestService = RestServiceManager.getInstance().getRestService();
        //Call<String> myReturnedUser = myRestService.sendFeedback(new Feedback(myRadioGroup.getCheckedRadioButtonId(), szDescription);

        //myReturnedUser.enqueue(new Callback<String>() {
            //@Override
            //public void onResponse(Call<String> call, Response<String> response) {
            //}

            //@Override
            //public void onFailure(Call<String> call, Throwable t) {
            //}
        //});

        onSkipClick();
    }

    public void onSkipClick(){

        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finish();
    }
}