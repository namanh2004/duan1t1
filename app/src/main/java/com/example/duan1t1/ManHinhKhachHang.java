package com.example.duan1t1;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentManager;


import com.example.duan1t1.adapter.Adapter_thongbao;
import com.example.duan1t1.adapter.MyNotification;
import com.example.duan1t1.fragment.Frag_cuahang;
import com.example.duan1t1.fragment.Fragment_choxacnhan;
import com.example.duan1t1.fragment.Fragment_gioHang;
import com.example.duan1t1.model.DonHang;
import com.example.duan1t1.model.ThongBao;
import com.example.duan1t1.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ManHinhKhachHang extends AppCompatActivity {
    Frag_cuahang fragCuahang;
    String TAG = "TAG";
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    FragmentManager manager;
    FirebaseFirestore db;
    FirebaseUser user;
    List<DonHang> list;
    List<ThongBao> list_thongBao;
    Adapter_thongbao adapterThongbao;
    User user1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_hinh_khach_hang);
        fragCuahang = new Frag_cuahang();
        bottomNavigationView = findViewById(R.id.bnv_khachhang);
        toolbar = findViewById(R.id.toolbar_khachhang);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cửa hàng");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.fcv_KhachHang, fragCuahang).commit();
        list = new ArrayList<>();
        list_thongBao = new ArrayList<>();
        getThongBao();
        yeuCauMoThongBao();
        adapterThongbao = new Adapter_thongbao(list_thongBao, ManHinhKhachHang.this);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menu_khachhang_danhsachsp) {
                    getSupportActionBar().setTitle("Cửa hàng");
                    manager.beginTransaction().replace(R.id.fcv_KhachHang, fragCuahang).commit();
                } else if (item.getItemId() == R.id.menu_khachhang_giohang) {
                    getSupportActionBar().setTitle("Giỏ hàng");
                    manager.beginTransaction().replace(R.id.fcv_KhachHang, new Fragment_gioHang()).commit();
                } else if (item.getItemId() == R.id.menu_khachhang_hotro) {
                    hotro();
                    return false;
                } else if (item.getItemId() == R.id.menu_khachhang_hoadon) {
                    manager.beginTransaction().replace(R.id.fcv_KhachHang, new Fragment_choxacnhan()).commit();
                    getSupportActionBar().setTitle("Hóa đơn");
                } else if (item.getItemId() == R.id.menu_khachhang_thongtincanhan) {
                    Intent intent = new Intent(ManHinhKhachHang.this, ThongTinTaiKhoan.class);
                    startActivity(intent);
                    return false;
                }
                return true;
            }
        });
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                AlertDialog.Builder builder = new AlertDialog.Builder(ManHinhKhachHang.this);
                builder.setTitle("Thông báo");
                builder.setIcon(R.drawable.user1);
                builder.setMessage("Bạn có muốn đăng xuất");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(ManHinhKhachHang.this, DangNhap_Activity.class);
                        startActivity(intent);
                        finishAffinity();
                        Toast.makeText(ManHinhKhachHang.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();

            }
        });
    }


    Menu menu_thongBao;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        menu_thongBao = menu;
        Log.e(TAG, "onCreateOptionsMenu: " + menu_thongBao);
        return true;
    }
//dddd

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_thongBao) {
            item.setIcon(R.drawable.belldis);
            xemThongBao();
        }
        return super.onOptionsItemSelected(item);
    }

    private void xemThongBao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_them_hang, null, false);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ListView listView = view.findViewById(R.id.list_hang);
        TextView tittle = view.findViewById(R.id.tv_tittle2);
        EditText editText = view.findViewById(R.id.edt_themhang_);
        ImageButton imageButton = view.findViewById(R.id.ibtn_addhang);

        editText.setVisibility(View.GONE);
        imageButton.setVisibility(View.GONE);
        tittle.setText("Thông báo");
        listView.setAdapter(adapterThongbao);
    }

    public void doiIcon() {
        if (menu_thongBao == null) {
            return;
        }
        MenuItem item = menu_thongBao.findItem(R.id.menu_thongBao);
        if (item == null) {
            return;
        }
        item.setIcon(R.drawable.bell_dis_);
        sendNotifi();
    }

    private void yeuCauMoThongBao() {
        boolean notificationEnabled = kiemTra();
        if (notificationEnabled) {

        } else {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ManHinhKhachHang.this);
            builder.setTitle("Thông báo");
            builder.setMessage("Bạn có muốn bật thông báo không?");
            builder.setPositiveButton("Cài đặt", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Chuyển đến màn hình cài đặt thông báo
                    Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("Bỏ qua", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            android.app.AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private boolean kiemTra() {
        // Kiểm tra trạng thái thông báo hiện tại
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        return notificationManager.areNotificationsEnabled();
    }

    private void sendNotifi() {
        Notification notification = new NotificationCompat.Builder(this, MyNotification.CHANNEL_ID)
                .setContentTitle("Thông báo")
                .setContentText("Đơn hàng đã được xác nhận") //Đặt nội dung của thông báo
                .setSmallIcon(R.drawable.logo3)
                .setColor(getColor(R.color.xanhla))
                .build();
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(getNotificationId(), notification); //được sử dụng để lấy một ID duy nhất cho mỗi thông báo được hiển thị.
    }

    private int getNotificationId() {
        return (int) new Date().getTime();
    }

    private void hotro() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_hotro, null, false);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        EditText sdt = view.findViewById(R.id.edt_sdt_hotro);
        Button gui = view.findViewById(R.id.btn_hotro);

        gui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guiHotro(sdt.getText().toString());
                dialog.dismiss();
            }
        });
    }

    private void guiHotro(String sdt) {
        db.collection("user").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user1 = documentSnapshot.toObject(User.class);
                user1.setSDT(sdt);
                user1.setTrangThai(0);
                db.collection("hoTro").document(user.getUid()).set(user1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                            Toast.makeText(ManHinhKhachHang.this, "Gửi hỗ trợ thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ManHinhKhachHang.this, "Lỗi", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }




    private void getThongBao() {
        db.collection("thongBao").whereEqualTo("chucVu", 3).whereEqualTo("maKhachHang", user.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e(TAG, "onEvent: " + 1);
                    return;
                }
                if (value == null) {
                    Log.e(TAG, "onEvent: " + 2);
                    return;
                }

                for (DocumentChange dc : value.getDocumentChanges()) {
                    user1=dc.getDocument().toObject(User.class);
                    switch (dc.getType()) {
                        case ADDED:
                            list_thongBao.add(dc.getDocument().toObject(ThongBao.class));
                            doiIcon();
                            adapterThongbao.notifyDataSetChanged();
                            break;
                        case MODIFIED:
                            ThongBao tb = dc.getDocument().toObject(ThongBao.class);
                            if (dc.getOldIndex() == dc.getNewIndex()) {
                                list_thongBao.set(dc.getOldIndex(), tb);

                            } else {
                                list_thongBao.remove(dc.getOldIndex());
                                list_thongBao.add(tb);
                            }
                            adapterThongbao.notifyDataSetChanged();
                            break;
                        case REMOVED:
                            list_thongBao.remove(dc.getOldIndex());
                            adapterThongbao.notifyDataSetChanged();
                            break;
                    }

                }
            }
        });
    }

}