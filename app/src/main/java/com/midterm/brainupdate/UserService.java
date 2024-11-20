package com.midterm.brainupdate;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserService {



    @POST("users/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);


    // Lấy thông tin người dùng theo ID
//    @GET("users/{id}")
//    Call<Users> getUserById(@Path("id") int id);
}
