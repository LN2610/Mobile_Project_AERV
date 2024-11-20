package com.midterm.brainupdate;

import static com.midterm.brainupdate.BottomNavigationView.CREATE_COURSE_REQUEST_CODE;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LibraryActivity extends AppCompatActivity {

    private RecyclerView recyclerViewFolders;
    private RecyclerView recyclerViewCourses;
    private FolderAdapter folderAdapter;
    private FlashcardAdapter courseAdapter;
    private RadioGroup radioGroup;
    private RadioButton radioFolders, radioCourses;
    // Tạo danh sách courses
    List<Flashcard> flashcardList = new ArrayList<>();
    // Tạo danh sách folders
    List<Folder> folderList = new ArrayList<>();

    Button addButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_library);

        BottomNavigationView bottomNavigation = new BottomNavigationView(this, findViewById(R.id.bottom_navigation_container));

        recyclerViewFolders = findViewById(R.id.recyclerViewFolders);
        recyclerViewCourses = findViewById(R.id.recyclerViewCourses);
        radioGroup = findViewById(R.id.radioGroup);
        radioFolders = findViewById(R.id.radioFolders);
        radioCourses = findViewById(R.id.radioCourses);
        fetchFoldersFromApi();
        // Thiết lập layout manager
        recyclerViewFolders.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCourses.setLayoutManager(new LinearLayoutManager(this));

        addButton = findViewById(R.id.addFolderButton);

        flashcardList.add(new Flashcard("UI UX Design", 10));
        flashcardList.add(new Flashcard("Graphic Design", 20));
        flashcardList.add(new Flashcard("C＃", 20));
        flashcardList.add(new Flashcard("Tiếng Nghệ An", 20));
        flashcardList.add(new Flashcard("React-Native", 20));
        flashcardList.add(new Flashcard("Node JS", 20));
        flashcardList.add(new Flashcard("HTML - CSS", 20));
        flashcardList.add(new Flashcard("PHP", 20));


        folderAdapter = new FolderAdapter(folderList);
        courseAdapter = new FlashcardAdapter(flashcardList);

        recyclerViewFolders.setAdapter(folderAdapter);
        recyclerViewCourses.setAdapter(courseAdapter);

        // Lắng nghe sự thay đổi trong RadioGroup
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioFolders) {
                recyclerViewFolders.setVisibility(View.VISIBLE);
                recyclerViewCourses.setVisibility(View.GONE);
                addButton.setVisibility(View.VISIBLE);  // Hiển thị nút "Thêm"
            } else if (checkedId == R.id.radioCourses) {
                recyclerViewFolders.setVisibility(View.GONE);
                recyclerViewCourses.setVisibility(View.VISIBLE);
                addButton.setVisibility(View.GONE);  // Ẩn nút "Thêm"
            }
        });

        addButton.setOnClickListener(v -> {
            // Tạo một EditText để nhập tên folder
            EditText folderNameInput = new EditText(LibraryActivity.this);
            folderNameInput.setHint("Nhập tên thư mục");

            // Tạo một AlertDialog để hiển thị EditText
            new AlertDialog.Builder(LibraryActivity.this)
                    .setTitle("Tạo Thư Mục Mới")
                    .setMessage("Nhập tên thư mục")
                    .setView(folderNameInput)
                    .setPositiveButton("Tạo", (dialog, which) -> {
                        // Lấy tên thư mục từ EditText
                        String folderName = folderNameInput.getText().toString().trim();

                        // Kiểm tra xem tên thư mục có trống không
                        if (folderName.isEmpty()) {
                            // Hiển thị thông báo nếu tên trống
                            Toast.makeText(LibraryActivity.this, "Tên thư mục không được trống", Toast.LENGTH_SHORT).show();
                        } else {
                            // Tiến hành gọi API để tạo thư mục nếu tên hợp lệ

                            createFolder(folderName);
                        }
                    })
                    .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())  // Đóng dialog nếu chọn hủy
                    .show();
        });

        courseAdapter.setOnItemClickListener(flashcard -> {
            // Thực hiện hành động khi nhấn vào một flashcard
            Toast.makeText(LibraryActivity.this, "Đã nhấn vào " + flashcard.getTitle(), Toast.LENGTH_SHORT).show();

            // Bạn có thể chuyển sang Activity khác và truyền dữ liệu flashcard nếu cần
             Intent intent = new Intent(LibraryActivity.this, Exam.class);
             intent.putExtra("flashcard_title", flashcard.getTitle());
             startActivity(intent);
        });

        // Mặc định hiển thị danh sách học phần
        recyclerViewFolders.setVisibility(View.GONE);
        recyclerViewCourses.setVisibility(View.VISIBLE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_COURSE_REQUEST_CODE && resultCode == RESULT_OK) {
            String newFlashcardTitle = data.getStringExtra("flashcard_title");
            if (newFlashcardTitle != null) {
                flashcardList.add(new Flashcard(newFlashcardTitle, 20));
                courseAdapter.notifyItemInserted(flashcardList.size() - 1);
            }
        }
    }
    private void fetchFoldersFromApi() {
        // Thay vì RetrofitClient.getFolders()
        Call<List<Folder>> call = RetrofitClient.getFolderService().getFolders();


        call.enqueue(new Callback<List<Folder>>() {
            @Override
            public void onResponse(Call<List<Folder>> call, Response<List<Folder>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    folderList.clear();  // Xóa danh sách thư mục cũ
                    folderList.addAll(response.body());  // Thêm thư mục mới từ API
                    folderAdapter.notifyDataSetChanged();  // Cập nhật adapter
                } else {
                    Toast.makeText(LibraryActivity.this, "Failed to load folders", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Folder>> call, Throwable t) {
                Log.e("LibraryActivity", "API call failed", t);
                Toast.makeText(LibraryActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createFolder(String folderName) {
        // Gọi API với chỉ tên thư mục
        Call<Folder> call = RetrofitClient.getFolderService().createFolder(folderName);

        call.enqueue(new Callback<Folder>() {
            @Override
            public void onResponse(Call<Folder> call, Response<Folder> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Folder createdFolder = response.body();
                    folderList.add(createdFolder);
                    folderAdapter.notifyItemInserted(folderList.size() - 1);
                    Toast.makeText(LibraryActivity.this, "Thư mục đã được tạo", Toast.LENGTH_SHORT).show();
                } else {
                    // In ra mã lỗi và thông báo lỗi
                    Log.e("LibraryActivity", "Error response code: " + response.code());
                    if (response.errorBody() != null) {
                        try {
                            // Hiển thị chi tiết lỗi từ server
                            String errorMessage = response.errorBody().string();
                            Log.e("LibraryActivity", "Error body: " + errorMessage);
                        } catch (IOException e) {
                            Log.e("LibraryActivity", "Error reading error body", e);
                        }
                    }
                    Toast.makeText(LibraryActivity.this, "Lỗi khi tạo thư mục", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Folder> call, Throwable t) {
                String errorMessage = "Lỗi kết nối: " + t.getMessage();
                if (t instanceof IOException) {
                    errorMessage = "Lỗi kết nối mạng. Vui lòng kiểm tra lại kết nối Internet.";
                }
                Log.e("LibraryActivity", "API call failed", t);
                Toast.makeText(LibraryActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }




}