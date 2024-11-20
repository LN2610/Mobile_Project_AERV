package com.midterm.brainupdate;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;

public interface MyModelApi {

    // Lấy danh sách các mô hình
    @GET("path/to/your/api/endpoint") // Thay đổi endpoint phù hợp
    Call<List<MyModel>> getMyModels();

    // Tạo một mô hình mới
    @POST("path/to/your/api/endpoint") // Thay đổi endpoint phù hợp
    Call<MyModel> createMyModel(@Body MyModel myModel);


}
