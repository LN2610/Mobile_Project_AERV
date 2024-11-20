package com.midterm.brainupdate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton, registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Khởi tạo các View
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        // Xử lý sự kiện nhấn nút Đăng nhập
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Kiểm tra nếu email hoặc password rỗng
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Nhập tài khoản và mật khẩu đầy đủ!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Gọi API đăng nhập
                loginUser(email, password);
            }
        });

        // Sự kiện nhấn nút Đăng ký
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser(String email, String password) {
        // Lấy UserService từ RetrofitClient
        UserService userService = RetrofitClient.getUserService();
        LoginRequest loginRequest = new LoginRequest(email, password);
        // Gọi API đăng nhập
        Call<LoginResponse> call = userService.loginUser(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse != null && loginResponse.getMessage().equals("Đăng nhập thành công")) {
                        int userId = loginResponse.getId();  // Lấy id người dùng từ response
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Thông tin không hợp lệ!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // In ra mã trạng thái HTTP và lý do lỗi để kiểm tra chi tiết hơn
                    Log.e("LoginError", "Error code: " + response.code() + " - " + response.message());
                    Toast.makeText(LoginActivity.this, "Đăng nhập thất bại, thử lại!", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Lỗi kết nối, thử lại sau!", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void getUserDetails(int userId) {
//        // Lấy UserService từ RetrofitClient
//        UserService userService = RetrofitClient.getUserService();
//
//        // Gọi API lấy thông tin người dùng chi tiết
//        Call<Users> call = userService.getUserById(userId);
//        call.enqueue(new Callback<Users>() {
//            @Override
//            public void onResponse(Call<Users> call, Response<Users> response) {
//                if (response.isSuccessful()) {
//                    Users user = response.body();
//                    if (user != null) {
//                        // Đăng nhập thành công và lấy được thông tin người dùng
//                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//                        intent.putExtra("user_id", user.getId());
//
//                        intent.putExtra("user_phone", user.getPhone());  // Thêm thông tin phone
//                        startActivity(intent);
//                        finish();  // Đóng LoginActivity
//                    } else {
//                        Toast.makeText(LoginActivity.this, "Không thể lấy thông tin người dùng!", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(LoginActivity.this, "Lỗi khi lấy thông tin người dùng!", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Users> call, Throwable t) {
//                Toast.makeText(LoginActivity.this, "Lỗi kết nối khi lấy thông tin người dùng!", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}
