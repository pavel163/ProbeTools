package com.ebr163.webserver.manager;

import java.io.IOException;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by mac1 on 02.11.16.
 */

public abstract class TransitionManager extends BaseManager {

    public NanoHTTPD.Response transition(NanoHTTPD.IHTTPSession session) {
        try {
            return getRouter().getManager(AssetsManager.class).assetByPath("tools/views/" + getPage() + ".html");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public abstract String getPage();
}
