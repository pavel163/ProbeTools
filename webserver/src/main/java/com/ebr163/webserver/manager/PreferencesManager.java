package com.ebr163.webserver.manager;

import fi.iki.elonen.NanoHTTPD;

public class PreferencesManager extends TransitionManager {

    @Override
    public String getPage() {
        return "preferences";
    }

    public NanoHTTPD.Response loadAllPreferences(NanoHTTPD.IHTTPSession session) {
        return responseStringAsJson(gson.toJson(getRouter().getPreferences().getAll()));
    }
}