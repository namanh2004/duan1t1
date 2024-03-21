package com.example.duan1t1.EventBus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.duan1t1.DAO.DAOQuanTriVien;
import com.example.duan1t1.R;

public class DangKyActivity extends AppCompatActivity {
    EditText edTaiKhoan, edMatKhau, edConf;

    Button btnout, btnDK;

    DAOQuanTriVien daoQuanTriVien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dang_ky);

        edTaiKhoan = findViewById(R.id.edTaiKhoan_dk);
        edMatKhau = findViewById(R.id.edMatKhau_dk);
        edConf = findViewById(R.id.edConf_dk);
        btnDK = findViewById(R.id.btnDk_dk);
        btnout = findViewById(R.id.btnOut_dk);

        // Khởi tạo DAOQuanTriVien
        daoQuanTriVien = new DAOQuanTriVien(this);

        btnout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DangKyActivity.this, DangNhapActivity.class));
            }
        });

        btnDK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = edTaiKhoan.getText().toString();
                String pass = edMatKhau.getText().toString();
                String conf = edConf.getText().toString();


                if (daoQuanTriVien != null) {
                    boolean kt = daoQuanTriVien.dangKy(user, pass, conf);

                    if (user.isEmpty() || pass.isEmpty() || conf.isEmpty()) {
                        Toast.makeText(DangKyActivity.this, "Hãy nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    } else if (pass.length() < 4) {
                        Toast.makeText(DangKyActivity.this, "Mật khẩu phải lớn hơn 4 ký tự", Toast.LENGTH_SHORT).show();
                    } else if (!pass.equals(conf)) {
                        Toast.makeText(DangKyActivity.this, "Trường xác nhận lại mật khẩu và trường mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DangKyActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DangKyActivity.this, DangNhapActivity.class));
                    }
                } else {
                    Toast.makeText(DangKyActivity.this, "Đã xảy ra lỗi khi khởi tạo DAOQuanTriVien", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}