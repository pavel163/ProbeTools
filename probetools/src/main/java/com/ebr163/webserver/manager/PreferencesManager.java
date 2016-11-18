package com.ebr163.webserver.manager;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public class PreferencesManager extends TransitionManager {


    public NanoHTTPD.Response loadAllPreferences(NanoHTTPD.IHTTPSession session) {
        Map<String, ?> params = getRouter().getPreferences().getAll();
        List<Map<String, String>> preferences = new ArrayList<>();
        for (Map.Entry<String, ?> param : params.entrySet()) {
            Map<String, String> preference = new HashMap<>();
            preference.put("key", param.getKey());
            preference.put("value", String.valueOf(param.getValue()));
            preference.put("type", param.getValue().getClass().getSimpleName());
            preferences.add(preference);
        }

        return responseStringAsJson(gson.toJson(preferences));
    }

    public NanoHTTPD.Response addPreference(NanoHTTPD.IHTTPSession session) {
        try {
            session.parseBody(new HashMap<String, String>());
            SharedPreferences.Editor ed = getRouter().getPreferences().edit();

            String type = session.getParameters().get("type").get(0);
            String value = session.getParameters().get("value").get(0);
            String key = session.getParameters().get("key").get(0);
            if ("String".equals(type)) {
                ed.putString(key, value);
            } else if ("Integer".equals(type)) {
                ed.putInt(key, Integer.parseInt(value));
            } else if ("Float".equals(type)) {
                ed.putFloat(key, Float.parseFloat(value));
            } else if ("Long".equals(type)) {
                ed.putLong(key, Long.parseLong(value));
            } else if ("Boolean".equals(type)) {
                ed.putBoolean(key, Boolean.parseBoolean(value));
            }
            ed.apply();

        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }

        return responseStringAsJson(gson.toJson(createSuccessAnswer(session)));
    }

    private Map<String, String> createSuccessAnswer(NanoHTTPD.IHTTPSession session) {
        Map<String, String> preference = new HashMap<>();
        preference.put("key", session.getParameters().get("key").get(0));
        preference.put("value", session.getParameters().get("value").get(0));
        preference.put("type", session.getParameters().get("type").get(0));
        return preference;
    }
}