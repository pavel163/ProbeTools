package com.ebr163.probetools;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.IOException;

/**
 * Created by mac1 on 21.11.16.
 */

public class Probetools {

    private final static String TAG_ERROR = "Probetools error";

    private static AndroidWebServer androidWebServer;

    public static void init(Context context) {
        androidWebServer = new AndroidWebServer(context, 8080);
        startServer();
    }

    private static void startServer() {
        if (androidWebServer != null){
            try {
                androidWebServer.startSever();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.e(TAG_ERROR, "setSQLiteOpenHelper be called before the init");
        }
    }

    public static void setSQLiteOpenHelper(SQLiteOpenHelper sqLiteOpenHelper) {
        if (androidWebServer != null) {
            androidWebServer.setSQLiteOpenHelper(sqLiteOpenHelper);
        } else {
            Log.e(TAG_ERROR, "setSQLiteOpenHelper be called before the init");
        }
    }

    public static void setPreferences(SharedPreferences preferences) {
        if (androidWebServer != null) {
            androidWebServer.setPreferences(preferences);
        } else {
            Log.e(TAG_ERROR, "setPreferences be called before the init");
        }
    }

    public static void setDBName(String dbName) {
        if (androidWebServer != null) {
            androidWebServer.setDBName(dbName);
        } else {
            Log.e(TAG_ERROR, "setDBName be called before the init");
        }
    }
}
