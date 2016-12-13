package com.ebr163.probeapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ebr163.probeapp.service.http.HttpService;

public class MainActivity extends AppCompatActivity {

    private HttpService httpService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        httpService = new HttpService();
    }

    public void onClick(View view) {
        httpService.getGifModels();
    }
}
