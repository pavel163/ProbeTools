package com.ebr163.webserver.manager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

import static fi.iki.elonen.NanoHTTPD.newChunkedResponse;

public class DBManager extends BaseManager {

    private Context context;
    private static final String TABLE = "table";
    private static final String FIELD = "field";
    private static final String TITLE = "title";
    private static final String COLUMN = "column";
    private static final String DATA = "data";

    public DBManager(Context context) {
        this.context = context;
    }

    public NanoHTTPD.Response download(NanoHTTPD.IHTTPSession session) {
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

    public NanoHTTPD.Response loadTable(NanoHTTPD.IHTTPSession session) {
        String table = session.getParameters().get(TABLE).get(0).toString();
        String[] columnTypes = getColumnTypes(table);

        SQLiteDatabase sqLiteDatabase = getRouter().getSqLiteOpenHelper().getWritableDatabase();
        Cursor dbCursor = sqLiteDatabase.query(table, null, null, null, null, null, null);

        String[] columnNames = dbCursor.getColumnNames();

        Map<String, List<Map<String, String>>> result = new HashMap<>();

        List<Map<String, String>> colums = new ArrayList<>();
        for (int i = 0; i < columnNames.length; i++) {
            Map<String, String> column = new HashMap<>();
            column.put(FIELD, columnNames[i]);
            column.put(TITLE, columnNames[i] + " (" + columnTypes[i] + ") ");
            colums.add(column);
        }
        result.put(COLUMN, colums);

        List<Map<String, String>> datas = new ArrayList<>();
        if (dbCursor.moveToFirst()) {
            do {
                Map<String, String> data = new HashMap<>();
                for (int i = 0; i < columnNames.length; i++) {
                    // TODO: 16.11.16 сделать обработку BLOB
                    data.put(columnNames[i], dbCursor.getString(dbCursor.getColumnIndex(columnNames[i])));
                }
                datas.add(data);
            } while (dbCursor.moveToNext());
        } else
            Log.d("cursor", "0 rows");

        result.put(DATA, datas);
        dbCursor.close();
        getRouter().getSqLiteOpenHelper().close();
        return responseStringAsJson(gson.toJson(result));
    }

    private String[] getColumnTypes(String table) {
        SQLiteDatabase sqLiteDatabase = getRouter().getSqLiteOpenHelper().getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("PRAGMA table_info(" + table + ")", null);
        String[] columnTypes = new String[cursor.getCount()];
        int i = 0;
        while (cursor.moveToNext() && i < columnTypes.length) {
            columnTypes[i] = cursor.getString(cursor.getColumnIndexOrThrow("type"));
            i++;
        }
        cursor.close();
        sqLiteDatabase.close();
        return columnTypes;
    }
}
