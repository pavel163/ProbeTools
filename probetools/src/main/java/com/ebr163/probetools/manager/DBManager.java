package com.ebr163.probetools.manager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.FileInputStream;
import java.io.IOException;
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
        String table = session.getParameters().get(TABLE).get(0);
        String[] columnTypes = getAllColumnTypes(table);

        SQLiteDatabase sqLiteDatabase = getRouter().getSqLiteOpenHelper().getWritableDatabase();
        Cursor dbCursor = sqLiteDatabase.query(table, null, null, null, null, null, null);

        Map<String, List<Map<String, String>>> result = new HashMap<>();
        result.put(COLUMN, dumpColumnNames(dbCursor, columnTypes));
        result.put(DATA, dumpData(dbCursor));

        dbCursor.close();
        getRouter().getSqLiteOpenHelper().close();
        return responseStringAsJson(gson.toJson(result));
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

    public NanoHTTPD.Response runSQL(NanoHTTPD.IHTTPSession session) {
        try {
            session.parseBody(new HashMap<String, String>());
        } catch (IOException | NanoHTTPD.ResponseException e) {
            e.printStackTrace();
        }

        String sql = session.getParameters().get("sql").get(0).toLowerCase();
        SQLiteDatabase sqLiteDatabase = getRouter().getSqLiteOpenHelper().getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        Map<String, Object> result = new HashMap<>();
        result.put(COLUMN, dumpColumnNames(cursor, null));
        result.put(DATA, dumpData(cursor));

        sqLiteDatabase.close();
        return responseStringAsJson(gson.toJson(result));
    }

    private List<Map<String, String>> dumpData(Cursor cursor) {
        List<Map<String, String>> datas = new ArrayList<>();
        String[] columnNames = cursor.getColumnNames();
        if (cursor.moveToFirst()) {
            do {
                Map<String, String> data = new HashMap<>();
                for (String columnName : columnNames) {
                    // TODO: 16.11.16 сделать обработку BLOB
                    data.put(columnName, cursor.getString(cursor.getColumnIndex(columnName)));
                }
                datas.add(data);
            } while (cursor.moveToNext());
        } else
            Log.d("cursor", "0 rows");
        return datas;
    }

    private List<Map<String, String>> dumpColumnNames(Cursor cursor, String[] columnTypes) {
        String[] columnNames = cursor.getColumnNames();
        List<Map<String, String>> colums = new ArrayList<>();
        for (int i = 0; i < columnNames.length; i++) {
            Map<String, String> column = new HashMap<>();
            column.put(FIELD, columnNames[i]);
            if (columnTypes != null && columnTypes[i] != null) {
                column.put(TITLE, columnNames[i] + " (" + columnTypes[i] + ") ");
            } else {
                column.put(TITLE, columnNames[i]);
            }
            colums.add(column);
        }
        return colums;
    }
}
