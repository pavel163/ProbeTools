package com.ebr163.probetools;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.wifi.WifiManager;

import java.io.IOException;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by mac1 on 01.11.16.
 */
final class AndroidWebServer extends NanoHTTPD {

    private int port;
    private Context context;
    private Router router;

    AndroidWebServer(Context context, int port) {
        super(port);
        init(context, port);
    }

    private void init(Context context, int port) {
        this.port = port;
        this.context = context;
        router = new Router(context);
    }

    @Override
    public Response serve(IHTTPSession session) {
        try {
            return router.route(session);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    void startSever() throws IOException {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder notificationBuilder = new Notification.Builder(context)
                .setSmallIcon(R.drawable.server)
                .setContentTitle("IP Address:")
                .setContentText(getIpAccess());
        // TODO: 01.11.16 поменять deprecated метод
        notificationManager.notify(0, notificationBuilder.getNotification());

        start();
    }

    void stopServer() {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
        stop();
    }

    private String getIpAccess() {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
        final String formatedIpAddress = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
        return "http://" + formatedIpAddress + ":" + port;
    }

    void setDBName(String dbName) {
        router.setDBName(dbName);
    }

    public void setPreferences(SharedPreferences preferences) {
        router.setPreferences(preferences);
    }

    void setSQLiteOpenHelper(SQLiteOpenHelper sqLiteOpenHelper) {
        router.setSqLiteOpenHelper(sqLiteOpenHelper);
    }
}
