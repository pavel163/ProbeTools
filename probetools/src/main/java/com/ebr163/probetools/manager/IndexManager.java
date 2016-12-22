package com.ebr163.probetools.manager;

import java.io.IOException;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by Ergashev on 02.11.16.
 */

public final class IndexManager extends TransitionManager {

    IndexManager() {
    }

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
