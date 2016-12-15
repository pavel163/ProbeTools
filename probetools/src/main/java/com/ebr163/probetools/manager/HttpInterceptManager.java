package com.ebr163.probetools.manager;

import com.ebr163.probetools.http.HttpData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by Ergashev on 02.11.16.
 */
public final class HttpInterceptManager extends BaseManager {

    private static final String HEADERS = "headers";
    private static final String URL = "url";
    private static final String QUERY_PARAMS = "query";

    public NanoHTTPD.Response getRequestData(NanoHTTPD.IHTTPSession session) throws JSONException {
        JSONObject request = createAnswer(getRouter().getProbeHttpInterceptor().getRequestData());
        return responseStringAsJson(request != null ? request.toString() : "");
    }

    public NanoHTTPD.Response getResponseData(NanoHTTPD.IHTTPSession session) throws JSONException {
        JSONObject response = createAnswer(getRouter().getProbeHttpInterceptor().getResponseData());
        return responseStringAsJson(response != null ? response.toString() : "");
    }

    private JSONObject createAnswer(HttpData data) throws JSONException {
        JSONObject answer = new JSONObject();
        if (data.url.isEmpty()) {
            return null;
        }
        answer.put(URL, data.url);
        answer.put(HEADERS, new JSONObject(data.getHeaders()));
        answer.put(QUERY_PARAMS, new JSONArray(data.getQueryParams()));
        return answer;
    }
}