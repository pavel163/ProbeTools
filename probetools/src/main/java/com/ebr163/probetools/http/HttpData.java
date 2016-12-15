package com.ebr163.probetools.http;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;

/**
 * Created by Ergashev on 13.12.16.
 */

public class HttpData {

    private Map<String, String> headers;
    public String body;
    public String url;
    private List<String> queryParams;

    HttpData() {
        headers = new HashMap<>();
    }

    void addHeaders(Headers headers) {
        if (headers.size() != 0) {
            for (int i = 0; i < headers.size(); i++) {
                this.headers.put(headers.name(i), headers.value(i));
            }
        }
    }

    void addQueryParams(String query) {
        queryParams = new ArrayList<>(Arrays.asList(query.split("&")));
    }

    void addHeader(String name, String value) {
        headers.put(name, value);
    }

    void clear() {
        headers.clear();
        body = "";
        url = "";
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public List<String> getQueryParams() {
        return queryParams;
    }
}
