package com.example.duan1t1.EventBus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.example.duan1t1.DAO.DAOQuanTriVien;
import com.example.duan1t1.Model.MainActivity;
import com.example.duan1t1.PhanQuyen;
import com.example.duan1t1.R;
import com.google.android.material.textfield.TextInputLayout;

public class DangNhapActivity extends AppCompatActivity {
    Button btn_login, btnOut;
    EditText txt_username,txt_password;
    TextInputLayout w_username,w_password;
    CheckBox chk_rememberAccount;
    String username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);

        btnOut = findViewById(R.id.btnOut);
        btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DangNhapActivity.this, PhanQuyen.class));
            }
        });




        initUI();
        btnLogin();
        checkSharedPreferences();
    }

    private void initUI() {
        btn_login = findViewById(R.id.btn_login);

        w_username = findViewById(R.id.w_username);
        w_password = findViewById(R.id.w_password);

        txt_username = findViewById(R.id.txt_username);
        txt_password = findViewById(R.id.txt_password);

        chk_rememberAccount = findViewById(R.id.chk_rememberAccount);
    }
    private void btnLogin() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getUsername = txt_username.getText().toString().trim();
                String getPassword = txt_password.getText().toString().trim();
                if (getUsername.isEmpty() || getPassword.isEmpty()) {
                    if (getUsername.isEmpty()) {
                        w_username.setError("Không được để trống trường này !");
                    } else {
                        w_username.setErrorEnabled(false);
                    }
                    if (getPassword.isEmpty()) {
                        w_password.setError("Không được để trống trường này !");
                    } else {
                        w_password.setErrorEnabled(false);
                    }
                } else {
                    DAOQuanTriVien quanTriVien = new  DAOQuanTriVien(DangNhapActivity.this);
                    if (quanTriVien.checkLogin(getUsername,getPassword) > 0) {
                        Toast.makeText(DangNhapActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        rememberAccount(chk_rememberAccount.isChecked());
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("username",getUsername);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(DangNhapActivity.this, "Tài khoản hoặc mật khẩu chưa chính xác !", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private void rememberAccount(Boolean chkRemember) {
        SharedPreferences sharedPreferences = getSharedPreferences("USER",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", txt_username.getText().toString().trim());
        editor.putString("password", txt_password.getText().toString().trim());
        editor.putBoolean("chkRemember", chkRemember);
        editor.apply();
    }
    private void checkSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        password = sharedPreferences.getString("password","");
        Boolean chkRemember = sharedPreferences.getBoolean("chkRemember",false);
        chk_rememberAccount.setChecked(chkRemember);
        if (chk_rememberAccount.isChecked()) {
            txt_username.setText(username);
            txt_password.setText(password);
        }
    }
}