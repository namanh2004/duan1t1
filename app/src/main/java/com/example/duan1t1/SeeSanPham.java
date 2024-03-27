package com.example.duan1t1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duan1t1.adapter.Adapter_kichco;
import com.example.duan1t1.model.GioHang;
import com.example.duan1t1.model.SanPham;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class SeeSanPham extends AppCompatActivity {
    RecyclerView rcv_list;
    SanPham sanPham = new SanPham();
    ;
    List<String> list_co;
    TextView ten, gia, nam;
    ImageView anh;
    Button them, tru, cong;
    EditText hienThi;
    Adapter_kichco adapterKichco;
    FirebaseFirestore db;
    int so = 0;
    String kichCo="";
    GestureDetector gestureDetector;

    public void setKichCo(String kichCo) {
        this.kichCo = kichCo;
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sanpham_show);
        Intent intent = getIntent();
        String s = intent.getStringExtra("sanpham");

        nghe(s);
        rcv_list = findViewById(R.id.rcv_listco);
        ten = findViewById(R.id.tv_tensp_show);
        gia = findViewById(R.id.tv_giasp_show);
        them = findViewById(R.id.btn_themgio);
        nam = findViewById(R.id.tv_mamsp_show);
        anh = findViewById(R.id.imv_anh_sp_lon);
        tru = findViewById(R.id.bnt_tru_soluong);
        cong = findViewById(R.id.bnt_cong_soluong);
        hienThi = findViewById(R.id.edt_soluong_show);
        ScrollView scv = findViewById(R.id.scv_xem);

        hienThi.setText(so + "");
        hienThi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()){
                   s="0";
                }
                if (Integer.parseInt(s.toString())>30){
                    Toast.makeText(SeeSanPham.this, "Không sản phẩm không được vượt quá 30", Toast.LENGTH_SHORT).show();
                    so=30;
                    hienThi.setText(so+"");
                    return;
                }
                so=Integer.parseInt(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(hienThi.getText().toString())==0){
                    Toast.makeText(SeeSanPham.this, "Bạn phải chọn ít nhất 1 sản phẩm ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (kichCo.isEmpty()){
                    Toast.makeText(SeeSanPham.this, "Hãy chọn kích cỡ", Toast.LENGTH_SHORT).show();
                    return;
                }
              checkHangDaThem(kichCo,sanPham.getMaSp(),FirebaseAuth.getInstance().getUid().toString());

            }
        });

        tru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tinh("-");
            }
        });
        cong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tinh("+");
            }
        });

    }

    private void checkHangDaThem(String kichCo, String maSp, String maKH) {
        final GioHang[] gioHang = {new GioHang()};
        db.collection("gioHang").whereEqualTo("kichCo",kichCo).
                whereEqualTo("maKhachHang",maKH).whereEqualTo("maSanPham",maSp).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isComplete()){
                           for (QueryDocumentSnapshot dc : task.getResult()){
                               gioHang[0]=dc.toObject(GioHang.class);
                           }
                           if (gioHang[0].getMaGio()==null){
                               themGio(gioHang[0],null);
                           }else {
                               themGio(gioHang[0],gioHang[0].getMaGio());
                           }
                        }
                    }
                });
    }

    private void themGio(GioHang hang,String maGio) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (hang.getSoLuong()==null){
            hang.setSoLuong(0l);
        }
        if (maGio==null){
            maGio = UUID.randomUUID()+"";
        }
        if (hang.getKichCo()==null){
            hang.setKichCo(kichCo);
        }
        Long soLuongMoi = hang.getSoLuong()+so;
        db.collection("gioHang").document(maGio).
                set(new GioHang(maGio, user.getUid(), sanPham.getMaSp(),hang.getKichCo(), soLuongMoi))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                            Toast.makeText(SeeSanPham.this, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SeeSanPham.this, "Lỗi", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void tinh(String dau) {
        if ("-".equals(dau)) {
            so -= 1;
            if (so == -1) {
                so = 0;
            }
        } else {
            so += 1;
        }
        if (so>30){
            Toast.makeText(this, "Sản phẩm mua không được vượt quá 30 sản phẩm", Toast.LENGTH_SHORT).show();
            so=30;
            hienThi.setText(so+"");
            return;
        }
        hienThi.setText(so + "");
    }


    private void nghe(String s) {
        db = FirebaseFirestore.getInstance();
        db.collection("sanPham").document(s).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isComplete()) {
                    Log.e("TAG", "onComplete: " + task.getResult().toObject(SanPham.class).getGia());
                    sanPham.setAnh(task.getResult().toObject(SanPham.class).getAnh());
                    sanPham.setMaSp(task.getResult().toObject(SanPham.class).getMaSp());
                    sanPham.setGia(task.getResult().toObject(SanPham.class).getGia());
                    sanPham.setTenSP(task.getResult().toObject(SanPham.class).getTenSP());
                    sanPham.setKichCo(task.getResult().toObject(SanPham.class).getKichCo());
                    sanPham.setNamSX(task.getResult().toObject(SanPham.class).getNamSX());
                    ten.setText(sanPham.getTenSP());
                    nam.setText("Năm sản xuất: " + sanPham.getNamSX());
                    gia.setText("Giá: " +  NumberFormat.getNumberInstance(Locale.getDefault()).format(sanPham.getGia())+" VND");
                    Glide.with(SeeSanPham.this).load(sanPham.getAnh()).error(R.drawable.baseline_crop_original_24).into(anh);
                    list_co = sanPham.getKichCo();
                    adapterKichco = new Adapter_kichco(list_co, SeeSanPham.this);
                    rcv_list.setAdapter(adapterKichco);
                    LinearLayoutManager manager = new LinearLayoutManager(SeeSanPham.this, LinearLayoutManager.HORIZONTAL, false);
                    rcv_list.setLayoutManager(manager);
                } else {
                    Toast.makeText(SeeSanPham.this, "Sản phẩm đã bị xóa", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}