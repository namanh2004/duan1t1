package com.example.duan1t1;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import com.example.duan1t1.adapter.Adapter_thongbao;
import com.example.duan1t1.fragment.Frg_QuanLyKH_LS;
import com.example.duan1t1.fragment.QuanLyGiay;
import com.example.duan1t1.fragment.QuanLyKhachHang;
import com.example.duan1t1.fragment.QuanLyNhanVien;
import com.example.duan1t1.fragment.frg_ThongKe;
import com.example.duan1t1.model.ThongBao;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ManHinhAdmin extends AppCompatActivity {

    Toolbar toolbar;
    FragmentContainerView viewPager;
    BottomNavigationView bottomNavigationView;

    QuanLyNhanVien quanLyNhanVien = new QuanLyNhanVien();
    QuanLyKhachHang quanLyKhachHang = new QuanLyKhachHang();
    QuanLyGiay quanLyGiay = new QuanLyGiay(0);
    frg_ThongKe thongKe = new frg_ThongKe();
    Frg_QuanLyKH_LS frgQuanLyKHLs = new Frg_QuanLyKH_LS();
    FragmentManager manager;
    Uri uri;
    List<ThongBao> list_thongBao;
    Adapter_thongbao adapterThongbao;
    String TAG = "TAG";
    FirebaseUser user;

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == RESULT_OK) {
                        Intent intent = o.getData();
                        if (intent == null) {
                            return;
                        }
                        uri = intent.getData();
                        quanLyGiay.hienthiAnh(uri);

                    }

                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_hinh_admin);
        toolbar = findViewById(R.id.toolbar_admin);
        viewPager = findViewById(R.id.fcv_Admin);
        bottomNavigationView = findViewById(R.id.bnv_Admin);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Quản Lý Nhân Viên");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.fcv_Admin, quanLyNhanVien).commit();
        list_thongBao = new ArrayList<>();
        getThongBao();
        adapterThongbao = new Adapter_thongbao(list_thongBao, ManHinhAdmin.this);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menu_admin_qlkh) {
                    relaceFrg(frgQuanLyKHLs);
                    getSupportActionBar().setTitle("Quản Lý Khách Hàng");
                } else if (item.getItemId() == R.id.menu_admin_qlnv) {
                    relaceFrg(quanLyNhanVien);
                    getSupportActionBar().setTitle("Quản Lý Nhân Viên");
                } else if (item.getItemId() == R.id.menu_admin_qlsp) {
                    relaceFrg(quanLyGiay);
                    getSupportActionBar().setTitle("Quản Lý Sản Phẩm");
                } else if (item.getItemId() == R.id.menu_admin_thongke) {
                    relaceFrg(thongKe);
                    getSupportActionBar().setTitle("Thống kê");
                } else if (item.getItemId() == R.id.menu_admin_resetpass) {
                    doipass(ManHinhAdmin.this);
                    return false;
                } else {
                    Toast.makeText(ManHinhAdmin.this, "Lỗi", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });

        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                AlertDialog.Builder builder = new AlertDialog.Builder(ManHinhAdmin.this);
                builder.setTitle("Thông báo");
                builder.setIcon(R.drawable.user1);
                builder.setMessage("Bạn có muốn đăng xuất");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(ManHinhAdmin.this, DangNhap_Activity.class);
                        startActivity(intent);
                        finishAffinity();
                        Toast.makeText(ManHinhAdmin.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
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

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_thongBao) {
            item.setIcon(R.drawable.belldis);
            xemThongBao();
        }
        return super.onOptionsItemSelected(item);
    }

    Menu menu_thongBao;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar, menu);
        menu_thongBao = menu;
        return super.onCreateOptionsMenu(menu);
    }

    public void relaceFrg(Fragment fragment) {
        manager.beginTransaction().replace(R.id.fcv_Admin, fragment).commit();
    }

    public void layAnh() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        launcher.launch(intent);
    }

    public void yeucauquyen(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            layAnh();
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            String[] quyen = new String[]{Manifest.permission.READ_MEDIA_IMAGES};
            requestPermissions(quyen, CODE_QUYEN);
            return;
        }
        if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // xử lý sau
            layAnh();
        } else {
            String[] quyen = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(quyen, CODE_QUYEN);
        }
    }

    private static final int CODE_QUYEN = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODE_QUYEN) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                layAnh();
            } else {
                Toast.makeText(this, "Bạn cần cấp quyền để sử dụng tính năng này", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void doipass(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog__doi_mat_khau, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        EditText edt_passCu, edt_passMoi, edt_xacNhan;
        AppCompatButton btn_doiMK;
        edt_passCu = view.findViewById(R.id.edt_nhapmkcu);
        edt_passMoi = view.findViewById(R.id.edt_nhapmkmoi);
        edt_xacNhan = view.findViewById(R.id.edt_xacnhanmk);
        btn_doiMK = view.findViewById(R.id.btn_doiMK);
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Sẽ mất một lúc vui lòng chờ");
        btn_doiMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String pasCu = edt_passCu.getText().toString();
                String pasMoi = edt_passMoi.getText().toString();
                String xacNhan = edt_xacNhan.getText().toString();

                if (pasCu.isEmpty() || pasMoi.isEmpty() || xacNhan.isEmpty()) {
                    Toast.makeText(activity, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else if (xacNhan.equals(pasMoi)) {
                    doiMK(pasCu, pasMoi, dialog, activity, progressDialog);
                } else {
                    Toast.makeText(activity, "Xác nhận mật khẩu mới sai", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void doiMK(String pasCu, String pasMoi, Dialog dialog, Context context, ProgressDialog progressDialog) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential authenticator = EmailAuthProvider.getCredential(user.getEmail(), pasCu);
        user.reauthenticate(authenticator).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    user.updatePassword(pasMoi).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                progressDialog.cancel();
                            } else {
                                Toast.makeText(context, "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(context, "Mật khẩu cũ sai vui lòng nhập lại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void xemThongBao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_them_hang, null, false);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();

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

    }

    private void getThongBao() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("thongBao").whereEqualTo("chucVu", 2).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
//        onCreate(): Phương thức này được gọi khi Activity được tạo ra. Trong phương thức này:
//
//        Thiết lập layout cho Activity.
//        Khởi tạo toolbar và các thành phần UI khác.
//        Thiết lập sự kiện cho BottomNavigationView để chuyển đổi giữa các Fragment khác nhau (Quản lý nhân viên, quản lý khách hàng, quản lý sản phẩm, thống kê).
//        Đăng ký sự kiện khi nhấn nút back trên điện thoại để hiển thị hộp thoại xác nhận đăng xuất.
//        onOptionsItemSelected(): Phương thức này được gọi khi một item trong menu được chọn. Trong phương thức này:
//
//        Xử lý sự kiện khi người dùng chọn xem thông báo.
//        onCreateOptionsMenu(): Phương thức này được gọi để tạo menu của Activity. Trong phương thức này:
//
//        Đổ menu từ file resource menu_toolbar.xml.
//        Lưu menu vào biến menu_thongBao để thay đổi icon khi có thông báo mới.
//        relaceFrg(): Phương thức này được sử dụng để thay thế Fragment hiện tại bằng một Fragment mới.
//
//        layAnh(): Phương thức này được sử dụng để mở activity để chọn hình ảnh từ bộ nhớ của thiết bị.
//
//        yeucauquyen(): Phương thức này được sử dụng để yêu cầu quyền truy cập bộ nhớ để chọn hình ảnh từ bộ nhớ của thiết bị.
//
//        onRequestPermissionsResult(): Phương thức này được gọi khi kết quả của quá trình yêu cầu quyền được trả về.
//
//        doipass(): Phương thức này được sử dụng để hiển thị hộp thoại cho phép người dùng đổi mật khẩu.
//
//        doiMK(): Phương thức này được sử dụng để xác thực mật khẩu hiện tại và đổi mật khẩu mới của người dùng.
//
//        xemThongBao(): Phương thức này được sử dụng để hiển thị hộp thoại cho phép người dùng xem các thông báo.
//
//        doiIcon(): Phương thức này được sử dụng để đổi icon của menu thông báo khi có thông báo mới.
//
//        getThongBao(): Phương thức này được sử dụng để lắng nghe sự kiện từ bảng "thongBao" trong cơ sở dữ liệu Firebase và cập nhật danh sách thông báo khi có sự thay đổi.





