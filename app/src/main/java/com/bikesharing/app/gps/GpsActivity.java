package com.bikesharing.app.gps;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bikesharing.app.R;
import com.bikesharing.app.data.Dock;


public class GpsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gps);

        Dock myDock = (Dock) getIntent().getSerializableExtra("Dock");
    }
}