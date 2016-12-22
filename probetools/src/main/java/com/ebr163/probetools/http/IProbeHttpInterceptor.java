package com.ebr163.probetools.http;

/**
 * Created by Ergashev on 22.12.16.
 */

public interface IProbeHttpInterceptor {
    HttpData getRequestData();

    HttpData getResponseData();
}
