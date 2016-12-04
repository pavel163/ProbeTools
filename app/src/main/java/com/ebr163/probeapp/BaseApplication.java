package com.ebr163.probeapp;

import android.app.Application;

import com.ebr163.probeapp.service.DataBaseHelper;
import com.ebr163.probetools.Probetools;

import java.io.IOException;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        DataBaseHelper myDbHelper = new DataBaseHelper(this);

        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        myDbHelper.openDataBase();

        Probetools.init(this);
        Probetools.setPreferences(getSharedPreferences("pref", MODE_PRIVATE));
        Probetools.setDBName(DataBaseHelper.DB_NAME, 11);
//        Probetools.setSQLiteOpenHelper(myDbHelper);
    }


}
