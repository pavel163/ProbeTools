package com.ebr163.probetools.manager;

import java.io.IOException;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by mac1 on 02.11.16.
 */

public class IndexManager extends TransitionManager {

    @Override
    public NanoHTTPD.Response transition(NanoHTTPD.IHTTPSession session) {
        try {
            return getRouter().getManager(AssetsManager.class).assetByPath("tools/views/index.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
