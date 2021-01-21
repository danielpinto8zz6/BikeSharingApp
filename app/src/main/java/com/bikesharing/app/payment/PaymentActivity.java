package com.bikesharing.app.payment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bikesharing.app.R;
import com.bikesharing.app.data.payment.Payment;
import com.bikesharing.app.data.payment.PaymentRequest;
import com.bikesharing.app.feedback.FeedbackActivity;
import com.bikesharing.app.rest.RestService;
import com.bikesharing.app.rest.RestServiceManager;
import com.bikesharing.app.sign.SignActivity;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener {

    private CircularProgressButton myPaymentButton;
    private TextView paymentValue;
    private TextInputLayout creditName;
    private TextInputLayout creditNumber;
    private TextInputLayout creditDate;
    private TextInputLayout creditCVV;
    private TextInputLayout companyName;
    private TextInputLayout taxNumber;

    private String szToken;

    private Payment myPayment;
    private int nRentalId  = -1;

    private boolean bIsPaymentValid = true;
    private boolean bIsCreditNameValid = false;
    private boolean bIsCreditNumberValid = false;
    private boolean bIsCreditDateValid = false;
    private boolean bIsCreditCVVValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(getColor(R.color.White));
        window.setNavigationBarColor(getColor(R.color.DarkGreen));

        SharedPreferences mySharedPreferences = getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);
        this.szToken = mySharedPreferences.getString("token", null);

        if ((this.szToken == null) && (this.szToken.isEmpty())) {

            startActivity(new Intent(getApplicationContext(), SignActivity.class));
            finish();
        }

        this.nRentalId = mySharedPreferences.getInt("rental", -1);
        if (this.nRentalId == -1) {

            startActivity(new Intent(getApplicationContext(), SignActivity.class));
            finish();
        }

        this.myPayment = (Payment) getIntent().getSerializableExtra("payment");
        if (this.myPayment == null) {

            startActivity(new Intent(getApplicationContext(), SignActivity.class));
            finish();
        }

        //mySharedPreferences.edit().remove("rental").apply();

        this.myPaymentButton = findViewById(R.id.cirPaymentButton);
        this.myPaymentButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_background_light_green));
        this.myPaymentButton.setOnClickListener(this);

        this.paymentValue = findViewById(R.id.paymentValue);
        this.paymentValue.setText(this.myPayment.getValue() + "€");

        this.creditName = findViewById(R.id.creditNameLayout);
        this.creditName.getEditText().addTextChangedListener(new PaymentTextWatcher(this.creditName));

        this.creditNumber = findViewById(R.id.creditNumberLayout);
        this.creditNumber.getEditText().addTextChangedListener(new PaymentTextWatcher(this.creditNumber));

        this.creditDate = findViewById(R.id.creditDateLayout);
        this.creditDate.getEditText().addTextChangedListener(new PaymentTextWatcher(this.creditDate));

        this.creditCVV = findViewById(R.id.creditCVVLayout);
        this.creditCVV.getEditText().addTextChangedListener(new PaymentTextWatcher(this.creditCVV));

        this.companyName = findViewById(R.id.companyNameLayout);

        this.taxNumber = findViewById(R.id.taxNumberLayout);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.cirPaymentButton) {
            onPaymentClick();
        }
    }

    private void onPaymentClick() {

        if (!bIsPaymentValid) {

            AlertDialog myDialog = new AlertDialog.Builder(this).create();
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

        int nTaxNumber = -1;
        if (!taxNumber.getEditText().getText().toString().isEmpty()) {
            nTaxNumber = Integer.parseInt(taxNumber.getEditText().getText().toString());
        }

        RestService myRestService = RestServiceManager.getInstance().getRestService();
        Call<Void> myReturnedUser = myRestService.donePayment(new PaymentRequest(this.myPayment.getId(),
                creditName.getEditText().getText().toString(),
                nTaxNumber,
                companyName.getEditText().getText().toString(),
                0,
                creditNumber.getEditText().getText().toString(),
                creditDate.getEditText().getText().toString(),
                creditCVV.getEditText().getText().toString()),
                "Bearer " + szToken);

        myReturnedUser.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println("Payment teste");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("Payment exception: " + t.getMessage());
            }
        });

        Intent myIntent = new Intent(getApplicationContext(), FeedbackActivity.class);
        myIntent.putExtra("rental", this.nRentalId);
        startActivity(myIntent);
        finish();
    }

    public class PaymentTextWatcher implements TextWatcher {

        private View myTextInputLayout;

        public PaymentTextWatcher (View myTextInputLayout) {
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
                case R.id.creditNameLayout:
                    validateCardName();
                    break;
                case R.id.creditNumberLayout:
                    validateCardNumber();
                    break;
                case R.id.creditDateLayout:
                    validateCardDate();
                    break;
                case R.id.creditCVVLayout:
                    validateCardCVV();
                    break;
            }

            if (bIsCreditNameValid && bIsCreditNumberValid && bIsCreditDateValid && bIsCreditCVVValid) {

                myPaymentButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_background_dark_green));
                bIsPaymentValid = true;
            } else {
                myPaymentButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_background_light_green));
                bIsPaymentValid = false;
            }
        }

        private void validateCardName() {

            if (creditName.getEditText().getText().toString().trim().isEmpty()) {

                creditName.setError("Card Name Required!");
                bIsCreditNameValid = false;
                return;
            }

            creditName.setErrorEnabled(false);
            bIsCreditNameValid = true;
        }

        private void validateCardNumber() {

            if (creditNumber.getEditText().getText().toString().contains(" ")) {

                creditNumber.setError("No Spaces Allowed!");
                bIsCreditNumberValid = false;
                return;
            }

            if (creditNumber.getEditText().getText().toString().trim().isEmpty()) {

                creditNumber.setError("Card Number Required!");
                bIsCreditNumberValid = false;
                return;
            }

            if(creditNumber.getEditText().getText().toString().length() != 16){

                creditNumber.setError("Card Number need to have 16 digit only");
                bIsCreditNumberValid = false;
                return;
            }

            creditNumber.setErrorEnabled(false);
            bIsCreditNumberValid = true;
        }

        private void validateCardDate() {

            if (creditDate.getEditText().getText().toString().contains(" ")) {

                creditDate.setError("No Spaces Allowed!");
                bIsCreditDateValid = false;
                return;
            }

            if (creditDate.getEditText().getText().toString().trim().isEmpty()) {

                creditDate.setError("Card Date Required!");
                bIsCreditDateValid = false;
                return;
            }

            if(creditDate.getEditText().getText().toString().length() != 7){

                creditDate.setError("Card Date need to have 7 digit only");
                bIsCreditDateValid = false;
                return;
            }

            if (!Pattern.matches("^\\d{2}\\/\\d{4}$", creditDate.getEditText().getText().toString())) {

                creditDate.setError("Card Date regex invalid");
                bIsCreditDateValid = false;
                return;
            }

            creditDate.setErrorEnabled(false);
            bIsCreditDateValid = true;
        }

        private void validateCardCVV() {

            if (creditCVV.getEditText().getText().toString().contains(" ")) {

                creditCVV.setError("No Spaces Allowed!");
                bIsCreditCVVValid = false;
                return;
            }

            if (creditCVV.getEditText().getText().toString().trim().isEmpty()) {

                creditCVV.setError("Card CVV Required!");
                bIsCreditCVVValid = false;
                return;
            }

            if(creditCVV.getEditText().getText().toString().length() != 3){

                creditCVV.setError("Card CVV need to have 3 digit only");
                bIsCreditCVVValid = false;
                return;
            }

            creditCVV.setErrorEnabled(false);
            bIsCreditCVVValid = true;
        }
    }
}