package com.example.duan1t1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.duan1t1.EventBus.DangNhapActivity;

public class PhanQuyen extends AppCompatActivity {
    Button btnAdmin, btnKH;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_phan_quyen);
        btnAdmin = findViewById(R.id.admin);
        btnKH  = findViewById(R.id.kh);



        btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PhanQuyen.this, DangNhapActivity.class);
                startActivity(i);
            }
        });
    }
}
