package com.bikesharing.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.bikesharing.app.home.HomeActivity;
import com.bikesharing.app.onboarding.OnboardingAdapter;
import com.bikesharing.app.onboarding.OnboardingItem;
import com.bikesharing.app.sign.SignActivity;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity {

    private OnboardingAdapter myOnboardingAdapter;
    private LinearLayout myLinearLayoutIndicators;
    private MaterialButton myConfirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        SharedPreferences mySharedPreferences = getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);
        if (!mySharedPreferences.getBoolean("isFirstRun", true)) {

            String szToken = mySharedPreferences.getString("token", null);
            if ((szToken == null) ||
                (szToken.isEmpty())) {

                startActivity(new Intent(getApplicationContext(), SignActivity.class));
            } else {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }

            finish();
        }

        setContentView(R.layout.activity_welcome);

        getWindow().setStatusBarColor(getColor(R.color.Black));
        getWindow().setNavigationBarColor(getColor(R.color.Black));

        this.myLinearLayoutIndicators = findViewById(R.id.layoutIndicator);
        this.myConfirmButton = findViewById(R.id.confirm_button);

        setupOnboardingItems();

        ViewPager2 myOnboardingViewPager = findViewById(R.id.scrollView);
        myOnboardingViewPager.setAdapter(this.myOnboardingAdapter);

        setupOnboardingIndicators();
        setCurrentOnboardingIndicator(0);

        myOnboardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageSelected(int position) {

                super.onPageSelected(position);
                setCurrentOnboardingIndicator(position);
            }
        });

        this.myConfirmButton.setOnClickListener(v -> {

            if (myOnboardingViewPager.getCurrentItem() + 1 < myOnboardingAdapter.getItemCount()) {
                myOnboardingViewPager.setCurrentItem(myOnboardingViewPager.getCurrentItem() + 1);
            } else {

                mySharedPreferences.edit().putBoolean("isFirstRun", false).apply();

                startActivity(new Intent(getApplicationContext(), SignActivity.class));
                finish();
            }
        });
    }

    private void setupOnboardingItems() {

        List<OnboardingItem> myOnboardingItems = new ArrayList<>();

        OnboardingItem mySearchItem = new OnboardingItem(R.drawable.findride,
                "FIND RIDE",
                "Use the app to locate available bicycles. " +
                        "Check the vehicle before riding and adjust height as needed.");

        OnboardingItem myScanItem = new OnboardingItem(R.drawable.scan,
                "SCAN TO UNLOCK",
                "Scan the QR code or enter the bike’s or scooter’s ID number to unlock it. " +
                        "The ID can be found below the QR code.");

        OnboardingItem myTravelItem = new OnboardingItem(R.drawable.travel,
                "ENJOY THE RIDE",
                "Have fun but ride carefully. Wear a helmet and follow all traffic laws");

        OnboardingItem myParkItem = new OnboardingItem(R.drawable.park,
                "PARK RESPONSIBLY",
                "Park in a designated parking zone shown on the app when you’ve reached your destination. " +
                        "Just slide the lock on the bike and we’ll take care of the rest");

        OnboardingItem myPayment = new OnboardingItem(R.drawable.payment,
                "EASY PAYMENT",
                "You will need only to introduce the respective payment parameters in your bank app or in any ATM");

        OnboardingItem myFeedback = new OnboardingItem(R.drawable.feedback,
                "FEEDBACK",
                "In the final of all of this steps we´ll ask to you for send one little feedback parameters for us to " +
                        "improve our services");

        myOnboardingItems.add(mySearchItem);
        myOnboardingItems.add(myScanItem);
        myOnboardingItems.add(myTravelItem);
        myOnboardingItems.add(myParkItem);
        myOnboardingItems.add(myPayment);
        myOnboardingItems.add(myFeedback);

        this.myOnboardingAdapter = new OnboardingAdapter(this.getApplicationContext(), myOnboardingItems);
    }

    private void setupOnboardingIndicators() {

        ImageView[] myIndicators = new ImageView[this.myOnboardingAdapter.getItemCount()];
        LinearLayout.LayoutParams myLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        myLayoutParams.setMargins(8, 0, 8, 0);

        for (int nIndex = 0; nIndex < myIndicators.length; nIndex++) {

            myIndicators[nIndex] = new ImageView(getApplicationContext());
            myIndicators[nIndex].setImageDrawable(
                    ContextCompat.getDrawable(getApplicationContext(),
                            R.drawable.onboarding_indicator_inactive)
            );
            myIndicators[nIndex].setLayoutParams(myLayoutParams);
            this.myLinearLayoutIndicators.addView(myIndicators[nIndex]);
        }
    }

    private void setCurrentOnboardingIndicator(int nCurrentIndex) {

        int nChildCount = this.myLinearLayoutIndicators.getChildCount();
        for (int nIndex = 0; nIndex < nChildCount; nIndex++) {

            ImageView myImageView = (ImageView) this.myLinearLayoutIndicators.getChildAt(nIndex);
            if (nIndex == nCurrentIndex) {

                myImageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(),
                                R.drawable.onboarding_indicator_active)
                );

            } else {

                myImageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(),
                                R.drawable.onboarding_indicator_inactive)
                );
            }
        }

        if (nCurrentIndex == this.myOnboardingAdapter.getItemCount() - 1) {
            this.myConfirmButton.setText("Start");
        } else {
            this.myConfirmButton.setText("Next");
        }
    }
}