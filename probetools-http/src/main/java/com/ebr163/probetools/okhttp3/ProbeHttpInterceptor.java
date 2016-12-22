package com.ebr163.probetools.okhttp3;

import android.util.Log;

import com.ebr163.probetools.Probetools;
import com.ebr163.probetools.http.HttpData;
import com.ebr163.probetools.http.IProbeHttpInterceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * Created by Ergashev on 13.12.16.
 */

public class ProbeHttpInterceptor implements Interceptor, IProbeHttpInterceptor {

    private HttpDataImpl requestData;
    private HttpDataImpl responseData;

    public ProbeHttpInterceptor() {
        requestData = new HttpDataImpl();
        responseData = new HttpDataImpl();
        Probetools.setHttpInterceptor(this);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Log.d("probetools", this.getClass().getSimpleName() + " intercept");
        requestData.clear();
        Request request = chain.request();
        String url = request.url().scheme() + "://" + request.url().host();

        requestData.addHeaders(request.headers());
        requestData.body = bodyToString(request);
        requestData.url = url;
        requestData.addQueryParams(request.url().query());

        Response response = chain.proceed(request);
        MediaType contentType = null;
        if (response.body() != null) {
            contentType = response.body().contentType();
            responseData.body = response.body().string();
        }

        responseData.addHeaders(response.headers());
        responseData.url = url;
        if (response.body() != null) {
            ResponseBody body = ResponseBody.create(contentType, responseData.body);
            return response.newBuilder().body(body).build();
        } else {
            return response;
        }
    }

    private static String bodyToString(final Request request) {

        try {
            final Request copy = request.newBuilder().build();
            if (copy.body() == null) {
                return "";
            }
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    @Override
    public HttpData getRequestData() {
        return requestData;
    }

    @Override
    public HttpData getResponseData() {
        return responseData;
    }
}
