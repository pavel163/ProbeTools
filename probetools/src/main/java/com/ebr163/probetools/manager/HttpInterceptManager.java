package com.ebr163.probetools.manager;

import com.ebr163.probetools.http.HttpData;

import org.json.JSONException;
import org.json.JSONObject;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by mac1 on 02.11.16.
 */

public final class HttpInterceptManager extends BaseManager {

    private static final String HEADERS = "headers";

    public NanoHTTPD.Response getRequestData(NanoHTTPD.IHTTPSession session) throws JSONException {
        JSONObject request = new JSONObject();
        HttpData requestData = getRouter().getProbeHttpInterceptor().getRequestData();
        if (requestData.url.isEmpty()) {
            return responseStringAsJson("");
        }
        request.put(HEADERS, new JSONObject(requestData.getHeaders()));
        return responseStringAsJson(request.toString());
    }

    public NanoHTTPD.Response getResponseData(NanoHTTPD.IHTTPSession session) throws JSONException {
        JSONObject response = new JSONObject();
        HttpData responseData = getRouter().getProbeHttpInterceptor().getResponseData();
        if (responseData.url.isEmpty()) {
            return responseStringAsJson("");
        }
        response.put(HEADERS, new JSONObject(responseData.getHeaders()));
        return responseStringAsJson(response.toString());
    }
}