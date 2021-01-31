package com.bikesharing.app.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.bikesharing.app.R;
import com.bikesharing.app.data.EmailToken;
import com.bikesharing.app.data.User;
import com.bikesharing.app.home.rentalHistory.RentalHistoryListFragment;
import com.bikesharing.app.home.dockList.DockListFragment;
import com.bikesharing.app.home.paymentHistory.PaymentHistoryListFragment;
import com.bikesharing.app.home.settings.SettingsFragment;
import com.bikesharing.app.rest.HttpStatus;
import com.bikesharing.app.rest.RestService;
import com.bikesharing.app.rest.RestServiceManager;
import com.bikesharing.app.sign.SignActivity;
import com.bikesharing.app.travel.TravelActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private User myUserInfo;
    private String szToken;
    private String szEmail;
    private static final String TAG = "HomeActivity";

    private final int LOCATION_REQUEST_ID = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        window.setStatusBarColor(getColor(R.color.DarkGreen));
        window.setNavigationBarColor(getColor(R.color.White));

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        if (savedInstanceState == null) {

            SharedPreferences mySharedPreferences = getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);

            this.szToken = mySharedPreferences.getString("token", null);
            if ((this.szToken == null) ||
                    (this.szToken.isEmpty())) {

                displayErrorExitDialog("Token", "Missing Token");
                return;
            }

            this.szEmail = mySharedPreferences.getString("email", null);
            if ((this.szEmail == null) ||
                (this.szEmail.isEmpty())) {

                displayErrorExitDialog("Email", "Email Missing");
                return;
            }

            getRestUserInfo(this.szEmail, this.szToken);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_ID);
            } else {

                if (mySharedPreferences.getBoolean("isTravelling", Boolean.FALSE) == Boolean.TRUE) {

                    Intent myIntent = new Intent(getApplicationContext(), TravelActivity.class);
                    myIntent.putExtra("Email", this.szEmail);

                    startActivity(myIntent);
                    finish();
                }

                BottomNavigationView myBottomNavigationView = findViewById(R.id.bottomMenu);
                myBottomNavigationView.setOnNavigationItemSelectedListener(new OnNavigationItemSelected());

                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, android.R.anim.fade_out)
                        .add(R.id.fragment_home_container, new DockListFragment(), HomeFragment.FRAGMENT_TAG)
                        .addToBackStack(null)
                        .commit();
            }
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {

                    // Get new FCM registration token
                    String token = task.getResult();

                    RestService myRestService = RestServiceManager.getInstance().getRestService();
                    Call<Void> myReturnedUser = myRestService.saveToken(new EmailToken(szEmail, token), "Bearer " + szToken);

                    myReturnedUser.enqueue(new Callback<Void>() {

                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                        }
                    });

                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_ID:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&  grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    SharedPreferences mySharedPreferences = getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);
                    if (mySharedPreferences.getBoolean("isTravelling", Boolean.FALSE) == Boolean.TRUE) {

                        Intent myIntent = new Intent(getApplicationContext(), TravelActivity.class);
                        myIntent.putExtra("Email", this.szEmail);

                        startActivity(myIntent);
                        finish();
                    }

                    BottomNavigationView myBottomNavigationView = findViewById(R.id.bottomMenu);
                    myBottomNavigationView.setOnNavigationItemSelectedListener(new OnNavigationItemSelected());

                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_right, android.R.anim.fade_out)
                            .add(R.id.fragment_home_container, new DockListFragment(), HomeFragment.FRAGMENT_TAG)
                            .addToBackStack(null)
                            .commit();
                } else {
                    displayErrorExitDialog("Email", "Email Missing");
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {

        HomeFragment myHomeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(HomeFragment.FRAGMENT_TAG);

        if (myHomeFragment == null) {
            displayLogoutDialog();
            return;
        }

        if (!myHomeFragment.allowBackPressed()) { // and then you define a method allowBackPressed with the logic to allow back pressed or not

            displayLogoutDialog();
            return;
        }

        super.onBackPressed();
    }

    private void getRestUserInfo(String szEmail, String szToken) {

        RestService myRestService = RestServiceManager.getInstance().getRestService();
        Call<User> myReturnedUser = myRestService.getUserByEmail(szEmail, "Bearer " + szToken);

        myReturnedUser.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (!response.isSuccessful()) {

                    displayErrorExitDialog("Error", HttpStatus.getStatusText(response.code()));
                    return;
                }

                myUserInfo = response.body();
                TextView myUsernameNameTextView = findViewById(R.id.welcomeUserName);
                myUsernameNameTextView.setText(myUserInfo.getName());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                displayErrorExitDialog("Error", t.getMessage());
            }
        });
    }

    public String getToken() {
        return szToken;
    }

    public User getUserInfo() {
        return myUserInfo;
    }

    public void displayErrorExitDialog(String szTitle, String szMessage) {

        AlertDialog myDialog = new AlertDialog.Builder(this).create();
        myDialog.setTitle(szTitle);
        myDialog.setMessage(szMessage);
        myDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK", (dialog, which) -> {

            SharedPreferences mySharedPreferences = getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);
            mySharedPreferences.edit().clear().apply();

            mySharedPreferences.edit().putString("token", this.szToken).apply();
            mySharedPreferences.edit().putString("email", this.szEmail).apply();
            mySharedPreferences.edit().putBoolean("isFirstRun", false).apply();

            startActivity(new Intent(getApplicationContext(), SignActivity.class));

            finish();
            dialog.dismiss();
        });

        myDialog.show();
    }

    private void displayLogoutDialog() {

        AlertDialog myDialog = new AlertDialog.Builder(this).create();
        myDialog.setTitle("Exit?");
        myDialog.setMessage("You want to exit?");
        myDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Yes", (dialog, which) -> {
            dialog.dismiss();
            finishAndRemoveTask();
        });

        myDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Logout", (dialog, which) -> {

            dialog.dismiss();

            SharedPreferences mySharedPreferences = getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);

            mySharedPreferences.edit().remove("token").apply();
            mySharedPreferences.edit().remove("email").apply();

            startActivity(new Intent(getApplicationContext(), SignActivity.class));
            finish();
        });

        myDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Back to app", (dialog, which) -> dialog.dismiss());

        myDialog.show();
    }

    private class OnNavigationItemSelected implements BottomNavigationView.OnNavigationItemSelectedListener {

        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            int nId = menuItem.getItemId();
            int nFragmentType;

            switch (nId) {
                case R.id.menuMain:
                    nFragmentType = HomeFragment.FRAGMENT_TYPE_DOCK_LIST;
                    break;

                case R.id.paymentHistory:
                    nFragmentType = HomeFragment.FRAGMENT_TYPE_PAYMENT_HISTORY;
                    break;

                case R.id.bikeHistory:
                    nFragmentType = HomeFragment.FRAGMENT_TYPE_BIKE_HISTORY;
                    break;

                case R.id.menuSettings:
                    nFragmentType = HomeFragment.FRAGMENT_TYPE_SETTINGS;
                    break;
                case R.id.menuLogout:
                    displayLogoutDialog();
                    return false;
                default:
                    throw new IllegalStateException("Unexpected value: " + nId);
            }

            HomeFragment myHomeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(HomeFragment.FRAGMENT_TAG);

            if ((myHomeFragment != null) &&
                (myHomeFragment.getFragmentType() == nFragmentType)) {
                return true;
            }

            Fragment myNewFragment;

            switch (nFragmentType) {

                case HomeFragment.FRAGMENT_TYPE_PAYMENT_HISTORY:
                    myNewFragment = new PaymentHistoryListFragment();
                    break;

                case HomeFragment.FRAGMENT_TYPE_BIKE_HISTORY:
                    myNewFragment = new RentalHistoryListFragment();
                    break;

                case HomeFragment.FRAGMENT_TYPE_SETTINGS:
                    myNewFragment = new SettingsFragment();
                    break;

                case HomeFragment.FRAGMENT_TYPE_DOCK_LIST:
                case HomeFragment.FRAGMENT_TYPE_BIKE_INFO:
                default:
                    myNewFragment = new DockListFragment();
                    break;

            }

            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, android.R.anim.fade_out)
                    .replace(R.id.fragment_home_container, myNewFragment, HomeFragment.FRAGMENT_TAG)
                    .addToBackStack(null)
                    .commit();

            return true;
        }
    }

}