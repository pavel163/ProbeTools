package com.ebr163.webserver.manager;

import android.content.Context;

import java.io.FileInputStream;
import java.io.InputStream;

import fi.iki.elonen.NanoHTTPD;

import static fi.iki.elonen.NanoHTTPD.newChunkedResponse;

public class DBManager extends BaseManager {

    private Context context;

    public DBManager(Context context) {
        this.context = context;
    }

    public NanoHTTPD.Response download(NanoHTTPD.IHTTPSession session){
        try {
            InputStream is = new FileInputStream(context.getDatabasePath(getRouter().getDbName()));
            return newChunkedResponse(NanoHTTPD.Response.Status.OK, "application/octet-stream", is);
        } catch (Exception exception) {
            return null;
        }
    }
}
