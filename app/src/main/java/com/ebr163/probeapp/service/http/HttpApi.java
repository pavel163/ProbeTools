package com.ebr163.probeapp.service.http;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by mac1 on 13.12.16.
 */

public interface HttpApi {

    @GET("/{section}/{page}?json=true")
    Call<Object> getGifModels(@Path("section") String section, @Path("page") int page,
                              @Query("pageSize") int pageSize, @Query("types") String type);

}
