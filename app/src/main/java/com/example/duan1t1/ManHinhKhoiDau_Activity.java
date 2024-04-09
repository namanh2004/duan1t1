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




}