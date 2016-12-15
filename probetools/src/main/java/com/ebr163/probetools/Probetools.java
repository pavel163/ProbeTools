package com.ebr163.probetools;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.ebr163.probetools.http.ProbeHttpInterceptor;

import java.io.IOException;

/**
 * Created by Ergashev on 21.11.16.
 */

public class Probetools {

    private final static String TAG_ERROR = "Probetools error";

    private static AndroidWebServer androidWebServer;

    private Probetools() {
    }

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

    public static void setPreferences(SharedPreferences preferences) {
        if (androidWebServer != null) {
            androidWebServer.setPreferences(preferences);
        } else {
            Log.e(TAG_ERROR, "setPreferences be called before the init");
        }
    }

    public static void putDatabase(String dbName, int dbVersion) {
        if (androidWebServer != null) {
            androidWebServer.putDatabase(dbName, dbVersion);
        } else {
            Log.e(TAG_ERROR, "putDatabase be called before the init");
        }
    }

    public static ProbeHttpInterceptor getProbeHttpInterceptor() {
        return androidWebServer.getProbeHttpInterceptor();
    }
}
