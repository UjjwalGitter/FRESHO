package com.freshokartz;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ProfileApi {

    String DJANGO_SITE = "http://10.0.2.2:8000/";

    @GET("api/v1/businesspartners/")
    Call<BpResponseBody> getDetail(@Header("Authorization") String djangoToken);


}
