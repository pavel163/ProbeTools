package com.ebr163.webserver.manager;

import com.ebr163.webserver.Router;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

/**
 * Created by mac1 on 02.11.16.
 */

public abstract class BaseManager {

    private Router router;

    public Router getRouter() {
        return router;
    }

    public BaseManager setRouter(Router router) {
        this.router = router;
        return this;
    }

    public String getBodyFromSession(NanoHTTPD.IHTTPSession session){
        Map<String, String> files = new HashMap<String, String>();
        try {
            session.parseBody(files);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if(NanoHTTPD.Method.POST.equals(session.getMethod())) {
            if(!files.containsKey("postData")){
                return null;
            }
            return files.get("postData");
        } else if(NanoHTTPD.Method.PUT.equals(session.getMethod())) {
            if(!files.containsKey("content")){
                return null;
            }
            File file = new File(files.get("content"));
            byte[] data = new byte[(int) file.length()];
            try {
                new FileInputStream(file).read(data);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return new String(data);
        }

        return null;
    }

    public NanoHTTPD.Response responseStringAsJson(String response) {
        if(response == null) {
            response = "null";
        } else if (response.length() == 0) {
            response = "\"\"";
        }
        return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "application/json; charset=UTF-8", response);
    }
}
