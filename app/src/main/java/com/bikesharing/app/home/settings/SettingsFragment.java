package com.bikesharing.app.home.settings;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bikesharing.app.R;
import com.bikesharing.app.data.User;
import com.bikesharing.app.home.HomeActivity;
import com.bikesharing.app.home.HomeFragment;
import com.bikesharing.app.rest.HttpStatus;
import com.bikesharing.app.rest.RestService;
import com.bikesharing.app.rest.RestServiceManager;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsFragment extends Fragment implements HomeFragment, View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        MaterialButton myNewNameButton = view.findViewById(R.id.new_name_button);
        myNewNameButton.setOnClickListener(this);

        MaterialButton myNewPasswordButton = view.findViewById(R.id.new_password_button);
        myNewPasswordButton.setOnClickListener(this);
    }

    @Override
    public boolean allowBackPressed() {
        return false;
    }

    @Override
    public int getFragmentType() {
        return HomeFragment.FRAGMENT_TYPE_SETTINGS;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.new_name_button:

                displayNewNameDialog();
                break;

            case R.id.new_password_button:
                displayNewPasswordDialog();
                break;
        }
    }

    private void displayErrorSettingsDialog(String szMessage){

        AlertDialog myDialog = new AlertDialog.Builder(getActivity()).create();
        myDialog.setTitle("Setting Error!");
        myDialog.setMessage(szMessage);
        myDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        myDialog.show();
    }

    private void displayNewNameDialog() {

        AlertDialog.Builder alertName = new AlertDialog.Builder(getContext());
        final EditText editTextName1 = new EditText(getContext());
        editTextName1.setHint("Write the new name here");

        alertName.setTitle("New Name");
        // titles can be used regardless of a custom layout or not
        alertName.setView(editTextName1);
        LinearLayout layoutName = new LinearLayout(getContext());
        layoutName.setOrientation(LinearLayout.VERTICAL);
        layoutName.addView(editTextName1); // displays the user input bar
        alertName.setView(layoutName);

        alertName.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String szNewName =  editTextName1.getText().toString(); // variable to collect user input

                // ensure that user input bar is not empty
                if (szNewName == null || szNewName.trim().equals("")){
                    Toast.makeText(getContext(), "Name field was empty!", Toast.LENGTH_LONG).show();
                }
                // add input into an data collection arraylist
                else {
                    onNewNameClick(szNewName);
                }
            }
        });

        alertName.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel(); // closes dialog
            }
        });

        alertName.show();
    }

    //TODO newName
    private void onNewNameClick(String szNewName) {

        User myUserInfo = ((HomeActivity) getActivity()).getUserInfo();

        String szToken = ((HomeActivity) getActivity()).getToken();

        RestService myRestService = RestServiceManager.getInstance().getRestService();
        Call<User> myReturnedUser = myRestService.newUserName(new User(myUserInfo.getUsername(), myUserInfo.getPassword()), szToken);

        myReturnedUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (!response.isSuccessful()) {

                    displayErrorSettingsDialog("Error setting new name: " + HttpStatus.getStatusText(response.code()));
                    return;
                }

                ((HomeActivity) getActivity()).setUserInfo(response.body());

                Toast toast = Toast.makeText(getContext(), "New name was set", Toast.LENGTH_LONG);
                toast.show();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                displayErrorSettingsDialog("Error setting new name: " + t.getMessage());
            }
        });
    }

    private void displayNewPasswordDialog() {

        AlertDialog.Builder alertName = new AlertDialog.Builder(getContext());
        final EditText editTextName1 = new EditText(getContext());
        editTextName1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editTextName1.setHint("Write the new password here");

        final EditText editTextName2 = new EditText(getContext());
        editTextName2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editTextName2.setHint("Repeat password here to revalidate");

        alertName.setTitle("New Password");
        // titles can be used regardless of a custom layout or not
        alertName.setView(editTextName1);
        LinearLayout layoutName = new LinearLayout(getContext());
        layoutName.setOrientation(LinearLayout.VERTICAL);
        layoutName.addView(editTextName1); // displays the user input bar
        layoutName.addView(editTextName2);
        alertName.setView(layoutName);

        alertName.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String szNewPassword =  editTextName1.getText().toString(); // variable to collect user input

                if (szNewPassword == null || szNewPassword.trim().equals("")){
                    Toast.makeText(getContext(), "Password is empty!", Toast.LENGTH_LONG).show();
                } else if (szNewPassword.contains(" ")) {
                    Toast.makeText(getContext(), "No Spaces Allowed!", Toast.LENGTH_LONG).show();
                } else if (szNewPassword.length() < 6 || szNewPassword.length() > 12) {
                    Toast.makeText(getContext(), "Password can't be less than 6 digit", Toast.LENGTH_LONG).show();
                } else if (szNewPassword.length() > 12) {
                    Toast.makeText(getContext(), "Password can't have more than 12 digit", Toast.LENGTH_LONG).show();
                } else{

                    String szCheckPassword =  editTextName2.getText().toString();
                    if(!szNewPassword.equalsIgnoreCase(szCheckPassword)){
                        Toast.makeText(getContext(), "Passwords Not Equal!", Toast.LENGTH_LONG).show();
                    } else {
                        onNewPasswordClick(szNewPassword);
                    }
                }
            }
        });

        alertName.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel(); // closes dialog
            }
        });

        alertName.show();
    }

    //TODO recoverPassword
    private void onNewPasswordClick(String szPassword) {

        User myUserInfo = ((HomeActivity) getActivity()).getUserInfo();

        String szToken = ((HomeActivity) getActivity()).getToken();

        RestService myRestService = RestServiceManager.getInstance().getRestService();
        Call<User> myReturnedUser = myRestService.newPassword(new User(myUserInfo.getUsername(), myUserInfo.getPassword()), szToken);

        myReturnedUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (!response.isSuccessful()) {

                    displayErrorSettingsDialog("Error setting new password: " + HttpStatus.getStatusText(response.code()));
                    return;
                }

                ((HomeActivity) getActivity()).setUserInfo(response.body());

                Toast toast = Toast.makeText(getContext(), "New password was set", Toast.LENGTH_LONG);
                toast.show();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                displayErrorSettingsDialog("Error setting new password: " + t.getMessage());
            }
        });
    }
}