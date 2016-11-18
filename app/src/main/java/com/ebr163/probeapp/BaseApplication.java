package com.ebr163.probeapp;

import android.app.Application;
import android.content.SharedPreferences;
import android.database.SQLException;

import com.ebr163.probeapp.service.DataBaseHelper;

import java.io.IOException;

public class BaseApplication extends Application {

    public static DataBaseHelper myDbHelper;
    public static SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.apply();

        myDbHelper = new DataBaseHelper(this);

        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        try {
            myDbHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }
    }


}
