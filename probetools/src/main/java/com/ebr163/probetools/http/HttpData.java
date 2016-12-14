package com.ebr163.probetools.http;

import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Headers;

/**
 * Created by mac1 on 13.12.16.
 */

public class HttpData {

    private Map<String, String> headers;
    public String body;
    public String url;

    HttpData() {
        headers = new LinkedHashMap<>();
    }

    void addHeaders(Headers headers) {
        if (headers.size() != 0) {
            for (int i = 0; i < headers.size(); i++) {
                this.headers.put(headers.name(i), headers.value(i));
            }
        }
    }

    void addHeader(String name, String value) {
        headers.put(name, value);
    }

    void clear() {
        headers.clear();
        body = "";
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
