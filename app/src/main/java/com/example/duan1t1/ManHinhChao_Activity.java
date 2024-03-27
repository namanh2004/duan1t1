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