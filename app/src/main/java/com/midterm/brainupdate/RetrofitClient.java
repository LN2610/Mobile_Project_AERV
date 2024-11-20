package com.midterm.brainupdate;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://app-backend-a5zm.onrender.com/api/";

    // Phương thức này khởi tạo Retrofit instance nếu chưa có và trả về đối tượng Retrofit
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) // Địa chỉ API của bạn
                    .addConverterFactory(GsonConverterFactory.create()) // Để chuyển đổi JSON thành đối tượng Java
                    .build();
        }
        return retrofit;
    }

    // Phương thức này trả về FolderService để thực hiện các API liên quan đến Folder
    public static FolderService getFolderService() {
        return getRetrofitInstance().create(FolderService.class);
    }

    public static UserService getUserService() {
        return getRetrofitInstance().create(UserService.class);
    }
    // Tạo thêm các phương thức khác tương tự cho các API như UserService, ProductService, v.v. nếu có
}
