package com.ebr163.webserver.manager;

import android.content.res.AssetManager;
import android.webkit.MimeTypeMap;

import java.io.IOException;
import java.io.InputStream;

import fi.iki.elonen.NanoHTTPD;

import static fi.iki.elonen.NanoHTTPD.newChunkedResponse;

public class AssetsManager extends BaseManager {

    private final AssetManager assets;
    private final MimeTypeMap mymeTypesMap;

    public AssetsManager(AssetManager assets) {
        this.assets = assets;
        mymeTypesMap = MimeTypeMap.getSingleton();
    }

    public NanoHTTPD.Response asset(NanoHTTPD.IHTTPSession session) throws Exception {
        try {
            final String path = session.getUri().replace("/assets/", "tools/");
            return assetByPath(path);
        } catch (Exception exception) {
            throw new Exception("Нет контроллера");
        }
    }

    public NanoHTTPD.Response assetByPath(String path) throws IOException {
        final InputStream is = assets.open(path);
        return newChunkedResponse(NanoHTTPD.Response.Status.OK, getMymeType(path), is);
    }

    private String getMymeType(String path) {
        final String ext = mymeTypesMap.getFileExtensionFromUrl(path);
        if ("js".equals(ext))
            return "application/javascript";
        else
            return mymeTypesMap.getMimeTypeFromExtension(ext) + "; charset=UTF-8";

    }
}
