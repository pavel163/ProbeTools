package com.ebr163.probetools.manager;

import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public final class PreferencesManager extends TransitionManager {

    public NanoHTTPD.Response loadAllPreferences(NanoHTTPD.IHTTPSession session) throws JSONException {
        Map<String, ?> params = getRouter().getPreferences().getAll();
        JSONArray preferences = new JSONArray();
        for (Map.Entry<String, ?> param : params.entrySet()) {
            JSONObject preference = new JSONObject();
            preference.put("key", param.getKey());
            preference.put("value", String.valueOf(param.getValue()));
            preference.put("type", param.getValue().getClass().getSimpleName());
            preferences.put(preference);
        }

        return responseStringAsJson(preferences.toString());
    }

    public NanoHTTPD.Response addPreference(NanoHTTPD.IHTTPSession session) throws JSONException {
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

        return responseStringAsJson(createSuccessAnswer(session).toString());
    }

    private JSONObject createSuccessAnswer(NanoHTTPD.IHTTPSession session) throws JSONException {
        JSONObject preference = new JSONObject();
        preference.put("key", session.getParameters().get("key").get(0));
        preference.put("value", session.getParameters().get("value").get(0));
        preference.put("type", session.getParameters().get("type").get(0));
        return preference;
    }
}