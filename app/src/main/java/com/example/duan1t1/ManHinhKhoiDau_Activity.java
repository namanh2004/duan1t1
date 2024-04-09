package com.example.duan1t1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ManHinhKhoiDau_Activity extends AppCompatActivity {
Button dangNhap;
private TextView dangKy;
private  Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_hinh_khoi_dau);
        dangNhap = findViewById(R.id.btn_dangNhap_begin);
        dangKy = findViewById(R.id.tv_dangky_begin);


        nextActivityAdmin(); //admin
        nextActivityNhanVien();//nhanvien
        nextActivityKhachHang();//khachhang

        dangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chuyen(DangNhap_Activity.class);
            }
        });
        dangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chuyen(DangKy_Activity.class);
            }
        });
    }

    private void chuyen(Class aClass) {
        intent = new Intent(ManHinhKhoiDau_Activity.this, aClass);
        startActivity(intent);
        finish();
    }

    private void nextActivityAdmin() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){

            Intent intent = new Intent(this, DangNhap_Activity.class);
            startActivity(intent);
        }else {
            //da co tai khoan admin
            Intent intent = new Intent(this, ManHinhAdmin.class);
            startActivity(intent);
        }
        finish();
    }

    private void nextActivityNhanVien() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){

            Intent intent = new Intent(this, DangNhap_Activity.class);
            startActivity(intent);
        }else {
            //da co tai khoan nhan vien
            Intent intent = new Intent(this, ManHinhNhanVien.class);
            startActivity(intent);
        }
        finish();
    }

    private void nextActivityKhachHang() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){

            Intent intent = new Intent(this, DangNhap_Activity.class);
            startActivity(intent);
        }else {
            //da co tai khoan khach hang
            Intent intent = new Intent(this, ManHinhKhachHang.class);
            startActivity(intent);
        }
        finish();
    }


}
//    onCreate(): Phương thức này được gọi khi Activity được tạo ra. Trong phương thức này:
//
//        Thiết lập giao diện cho Activity.
//        Khởi tạo và thiết lập các thành phần giao diện như nút "Đăng nhập" và "Đăng ký".
//        Gọi các phương thức nextActivityAdmin(), nextActivityNhanVien(), và nextActivityKhachHang() để xác định loại người dùng và chuyển họ đến màn hình tương ứng.
//        nextActivityAdmin(), nextActivityNhanVien(), nextActivityKhachHang(): Đây là các phương thức được sử dụng để kiểm tra xem người dùng hiện tại đã đăng nhập và là admin, nhân viên hay khách hàng. Nếu người dùng chưa đăng nhập, họ sẽ được chuyển đến màn hình đăng nhập (DangNhap_Activity). Nếu đã đăng nhập, họ sẽ được chuyển đến màn hình tương ứng (ManHinhAdmin, ManHinhNhanVien, ManHinhKhachHang). Cuối cùng, phương thức finish() được gọi để kết thúc Activity hiện tại sau khi chuyển đến màn hình mới.
//
//        chuyen(Class aClass): Phương thức này được sử dụng để chuyển đến một Activity mới dựa trên lớp Activity được chỉ định. Phương thức này tạo một Intent mới và gọi phương thức startActivity() để chuyển đến Activity mới. Sau đó, phương thức finish() được gọi để kết thúc Activity hiện tại và ngăn người dùng quay lại màn hình này sau khi đã chuyển đi.