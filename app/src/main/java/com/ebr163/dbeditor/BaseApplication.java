package com.ebr163.dbeditor;

import android.app.Application;
import android.database.SQLException;

import com.ebr163.dbeditor.service.DataBaseHelper;

import java.io.IOException;

public class BaseApplication extends Application {

    public static DataBaseHelper myDbHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        myDbHelper = new DataBaseHelper(this);

        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        try {
            myDbHelper.openDataBase();
        }catch(SQLException sqle){
            throw sqle;
        }
    }



}
