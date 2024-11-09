package com.example.universalyogaadmin.network;

import com.example.universalyogaadmin.model.api.ResponseBody;
import com.example.universalyogaadmin.model.api.YogaCourseRequestBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiRoute {
    @POST("/api/yoga")
    Call<ResponseBody> sendYogaCourse(@Body YogaCourseRequestBody yogaCourse);
}
