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


        nextActivityAdmin();
        nextActivityNhanVien();
//        nextActivityKhachHang();

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

//    private void nextActivityKhachHang() {
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user == null){
//
//            Intent intent = new Intent(this, DangNhap_Activity.class);
//            startActivity(intent);
//        }else {
//            //da co tai khoan khach hang
//            Intent intent = new Intent(this, ManHinhKhachHang.class);
//            startActivity(intent);
//        }
//        finish();
//    }


}