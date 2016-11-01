package com.ebr163.webserver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.net.wifi.WifiManager;

import java.io.IOException;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by mac1 on 01.11.16.
 */
public class AndroidWebServer extends NanoHTTPD {

    private int port;

    public AndroidWebServer(int port) {
        super(port);
        this.port = port;
    }

    public AndroidWebServer(String hostname, int port) {
        super(hostname, port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        String msg = "<html><body><h1>Hello server</h1>\n";
        Map<String, String> parms = session.getParms();
        if (parms.get("username") == null) {
            msg += "<form action='?' method='get'>\n";
            msg += "<p>Your name: <input type='text' name='username'></p>\n";
            msg += "</form>\n";
        } else {
            msg += "<p>Hello, " + parms.get("username") + "!</p>";
        }
        return newFixedLengthResponse(msg + "</body></html>\n");
    }

    public void start(Context context) throws IOException {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder notificationBuilder = new Notification.Builder(context)
                .setSmallIcon(R.drawable.server)
                .setContentTitle("IP Address:")
                .setContentText(getIpAccess(context));
        // TODO: 01.11.16 поменять deprecated метод
        notificationManager.notify(0, notificationBuilder.getNotification());

        start();
    }

    public void stop(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }


    private String getIpAccess(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
        final String formatedIpAddress = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
        return "http://" + formatedIpAddress + ":" + port;
    }
}
