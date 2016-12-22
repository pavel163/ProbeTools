package com.ebr163.probetools.manager;

import java.io.IOException;

import fi.iki.elonen.NanoHTTPD;

public class TransitionManager extends BaseManager {

    public NanoHTTPD.Response transition(NanoHTTPD.IHTTPSession session) {
        try {
            return getRouter().getManager(AssetsManager.class).assetByPath("tools/views" + session.getUri());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public NanoHTTPD.Response transition(String page) {
        try {
            return getRouter().getManager(AssetsManager.class).assetByPath("tools/views" + page + ".html");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
