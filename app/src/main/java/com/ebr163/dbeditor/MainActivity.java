package com.ebr163.dbeditor;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ebr163.dbeditor.databinding.ActivityMainBinding;
import com.ebr163.webserver.AndroidWebServer;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean isStarted = false;
    private ActivityMainBinding binding;
    private AndroidWebServer androidWebServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        androidWebServer = new AndroidWebServer(8080);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initUI();
    }

    private void initUI() {
        binding.onOfServer.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        isStarted = !isStarted;
        if (isStarted) {
            startServer();
        } else {
            stopServer();
        }
    }

    private void startServer() {
        try {
            androidWebServer.start(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopServer() {
        androidWebServer.stop(this);
    }
}
