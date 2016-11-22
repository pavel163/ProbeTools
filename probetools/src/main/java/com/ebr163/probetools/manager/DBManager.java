package com.ebr163.probetools.manager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

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

        JSONArray tables = new JSONArray();
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                tables.put(c.getString(0));
                c.moveToNext();
            }
        }

        getRouter().getSqLiteOpenHelper().close();
        return responseStringAsJson(tables.toString());
    }

    public NanoHTTPD.Response loadTable(NanoHTTPD.IHTTPSession session) throws JSONException {
        String table = session.getParameters().get(TABLE).get(0);
        String[] columnTypes = getAllColumnTypes(table);

        SQLiteDatabase sqLiteDatabase = getRouter().getSqLiteOpenHelper().getWritableDatabase();
        Cursor dbCursor = sqLiteDatabase.query(table, null, null, null, null, null, null);

        JSONObject result = new JSONObject();
        result.put(COLUMN, getColumnNames(dbCursor, columnTypes));
        result.put(DATA, getData(dbCursor));

        dbCursor.close();
        getRouter().getSqLiteOpenHelper().close();
        return responseStringAsJson(result.toString());
    }

    private String[] getAllColumnTypes(String table) {
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

    public NanoHTTPD.Response runSQL(NanoHTTPD.IHTTPSession session) throws JSONException {
        try {
            session.parseBody(new HashMap<String, String>());
        } catch (IOException | NanoHTTPD.ResponseException e) {
            e.printStackTrace();
        }

        String sql = session.getParameters().get("sql").get(0).toLowerCase();
        SQLiteDatabase sqLiteDatabase = getRouter().getSqLiteOpenHelper().getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        JSONObject result = new JSONObject();
        result.put(COLUMN, getColumnNames(cursor, null));
        result.put(DATA, getData(cursor));

        sqLiteDatabase.close();
        return responseStringAsJson(result.toString());
    }

    private JSONArray getData(Cursor cursor) throws JSONException {
        JSONArray datas = new JSONArray();
        String[] columnNames = cursor.getColumnNames();
        if (cursor.moveToFirst()) {
            do {
                JSONObject data = new JSONObject();
                for (String columnName : columnNames) {
                    // TODO: 16.11.16 сделать обработку BLOB
                    data.put(columnName, cursor.getString(cursor.getColumnIndex(columnName)));
                }
                datas.put(data);
            } while (cursor.moveToNext());
        } else
            Log.d("cursor", "0 rows");
        return datas;
    }

    private JSONArray getColumnNames(Cursor cursor, String[] columnTypes) throws JSONException {
        String[] columnNames = cursor.getColumnNames();
        JSONArray colums = new JSONArray();
        for (int i = 0; i < columnNames.length; i++) {
            JSONObject column = new JSONObject();
            column.put(FIELD, columnNames[i]);
            if (columnTypes != null && columnTypes[i] != null) {
                column.put(TITLE, columnNames[i] + " (" + columnTypes[i] + ") ");
            } else {
                column.put(TITLE, columnNames[i]);
            }
            colums.put(column);
        }
        return colums;
    }
}
