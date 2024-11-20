package com.midterm.brainupdate;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;

public interface FolderService {
    @GET("directory/getDirectoriesAll") // Endpoint để lấy danh sách folder
    Call<List<Folder>> getFolders();

    @POST("directory/createDirectories") // Endpoint để tạo mới một folder
    Call<Folder> createFolder(@Body String folderName); // Chỉ gửi tên thư mục


    // Bạn có thể thêm các endpoint khác liên quan đến Folder tại đây nếu cần
}
