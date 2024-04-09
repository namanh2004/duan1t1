package com.example.duan1t1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

public class ManHinhChao_Activity extends AppCompatActivity {
    private SharedPreferences preferences;
    private FirebaseFirestore db;
    private DocumentReference reference;
    private ListenerRegistration registration;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(ManHinhChao_Activity.this);
        setContentView(R.layout.activity_man_hinh_chao);
        Log.e("TAG", "onCreate: " + "Đang mở activity");
        preferences = getSharedPreferences("begin", MODE_PRIVATE);
        int i = preferences.getInt("only", 0);
        db = FirebaseFirestore.getInstance();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (i == 0) {
                    Intent intent = new Intent(ManHinhChao_Activity.this, ManHinhKhoiDau_Activity.class);
                    startActivity(intent);

                    finish();
                } else {
                    vaomanhinh();
                }

            }
        }, 3000);
    }




    private void vaomanhinh() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            intent = new Intent(this, DangNhap_Activity.class);
            startActivity(intent);
        } else {
            intent = new Intent(ManHinhChao_Activity.this, ManHinhKhachHang.class);
            startActivity(intent);
        }

    }



}
//        onCreate(): Phương thức này được gọi khi Activity được tạo ra. Trong phương thức này:
//
//        Khởi tạo và thiết lập giao diện cho Activity.
//        Khởi tạo SharedPreferences để lưu trạng thái khởi đầu của ứng dụng.
//        Kiểm tra xem ứng dụng đã được mở lần đầu tiên hay không bằng cách lấy giá trị từ SharedPreferences.
//        Tạo một Handler để chờ trong 3 giây trước khi chuyển đến Activity tiếp theo.
//        vaomanhinh(): Phương thức này được sử dụng để xác định xem người dùng đã đăng nhập hay chưa và chuyển hướng đến Activity phù hợp dựa trên kết quả kiểm tra.
//
//        Nếu người dùng chưa đăng nhập, chuyển hướng đến màn hình đăng nhập (DangNhap_Activity).
//        Nếu người dùng đã đăng nhập, chuyển hướng đến màn hình khách hàng (ManHinhKhachHang).
//        FirebaseApp.initializeApp(): Phương thức này được sử dụng để khởi tạo FirebaseApp trong Activity.
//
//        SharedPreferences: Được sử dụng để lưu trạng thái ban đầu của ứng dụng, giúp xác định xem ứng dụng đã được mở lần đầu tiên hay không.
//
//        FirebaseFirestore: Được sử dụng để tương tác với cơ sở dữ liệu Firestore của Firebase.
//
//        Handler: Được sử dụng để tạo một độ trễ 3 giây trước khi chuyển đến Activity tiếp theo. Điều này cho phép màn hình chào hiển thị trong khoảng thời gian nhất định trước khi chuyển hướng.