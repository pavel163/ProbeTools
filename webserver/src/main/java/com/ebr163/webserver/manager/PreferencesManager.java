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
        for (Map.Entry<String, ?> param: params.entrySet()){
            Map<String, String> preference = new HashMap<>();
            preference.put("key", param.getKey());
            preference.put("value", String.valueOf(param.getValue()));
            preference.put("type", param.getValue().getClass().getSimpleName());
            preferences.add(preference);
        }

        return responseStringAsJson(gson.toJson(preferences));
    }

    public NanoHTTPD.Response addPreference(NanoHTTPD.IHTTPSession session) {
        SharedPreferences.Editor ed = getRouter().getPreferences().edit();
        ed.putString("test", "test");
        ed.apply();
        Map<String, String> preference = new HashMap<>();
        preference.put("key", "key");
        preference.put("value", "value");
        preference.put("type", "type");
        return responseStringAsJson(gson.toJson(preference));
    }
}