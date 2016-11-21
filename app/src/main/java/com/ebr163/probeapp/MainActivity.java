package com.ebr163.probeapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ebr163.probeapp.databinding.ActivityMainBinding;
import com.ebr163.probetools.AndroidWebServer;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private AndroidWebServer androidWebServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
