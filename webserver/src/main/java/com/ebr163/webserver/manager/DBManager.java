package com.ebr163.webserver.manager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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

    public NanoHTTPD.Response loadAllTableNames(NanoHTTPD.IHTTPSession session) {
        SQLiteDatabase sqLiteDatabase = getRouter().getSqLiteOpenHelper().getWritableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        List<String> tables = new ArrayList<>();
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                tables.add(c.getString(0));
                c.moveToNext();
            }
        }

        getRouter().getSqLiteOpenHelper().close();
        return responseStringAsJson(gson.toJson(tables));
    }
}
