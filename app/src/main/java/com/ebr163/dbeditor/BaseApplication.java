package com.ebr163.dbeditor;

import android.app.Application;
import android.content.SharedPreferences;
import android.database.SQLException;

import com.ebr163.dbeditor.service.DataBaseHelper;

import java.io.IOException;

public class BaseApplication extends Application {

    public static DataBaseHelper myDbHelper;
    public static SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putInt("test1", 1);
        ed.putBoolean("test2", true);
        ed.putFloat("test3", 1.44f);
        ed.putString("test4", "4");
        ed.putString("test5", "4");
        ed.putString("test6", "4");
        ed.putString("test7", "4");
        ed.putString("test8", "4");
        ed.putString("test9", "4");
        ed.putString("test10", "4");
        ed.apply();

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
