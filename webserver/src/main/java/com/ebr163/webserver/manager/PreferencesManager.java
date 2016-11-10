package com.ebr163.webserver.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public class PreferencesManager extends TransitionManager {

    @Override
    public String getPage() {
        return "preferences";
    }

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
}