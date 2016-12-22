package com.ebr163.probeapp.service.http;

import android.util.Log;

import com.ebr163.probetools.okhttp3.ProbeHttpInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mac1 on 13.12.16.
 */

public class HttpService {

    private HttpApi httpApi;

    public HttpService() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new ProbeHttpInterceptor());
        OkHttpClient client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://developerslife.ru")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        httpApi = retrofit.create(HttpApi.class);
    }

    public void getGifModels() {
        Call<Object> result = httpApi.getGifModels("latest", 1, 10, "gif");
        result.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d("getGifModels", this.getClass().getSimpleName());
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
            }
        });
    }
}
