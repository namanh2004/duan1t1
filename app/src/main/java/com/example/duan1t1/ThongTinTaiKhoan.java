package com.example.duan1t1;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.duan1t1.adapter.Adapter_diachi;
import com.example.duan1t1.adapter.Adapter_thongtin;
import com.example.duan1t1.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ThongTinTaiKhoan extends AppCompatActivity {
    ViewPager2 pager2;
    TabLayout tabLayout;
    TabLayoutMediator mediator;
    Adapter_thongtin adapterThongtin;
    CircleImageView avatar;
    ImageView edit_profile, cancel;
    TextView ten, tien;
    FirebaseFirestore db;
    User us;
    FirebaseUser user;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongtintaikhoan);
        user = FirebaseAuth.getInstance().getCurrentUser();
        Log.e("TAG", "onCreate: "+user.getPhotoUrl() );
        adapterThongtin = new Adapter_thongtin(this);
        pager2 = findViewById(R.id.viewPage2_thongtin_khach);
        avatar = findViewById(R.id.imv_avatar);
        edit_profile = findViewById(R.id.imv_updatethongtin);
        cancel = findViewById(R.id.imv_cancel);
        ten = findViewById(R.id.tv_username_khach);
        tien = findViewById(R.id.tv_sodu_khach);
        LinearLayout nap = findViewById(R.id.ll_naptien);
        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(ThongTinTaiKhoan.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Sẽ mất một lúc vui lòng chờ");
        nghe();
        pager2.setAdapter(adapterThongtin);
        tabLayout = findViewById(R.id.tabLayout_thongtinkhach);

        nap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                naptien();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suaProFile();
            }
        });

        mediator = new TabLayoutMediator(tabLayout, pager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0) {
                    tab.setText("Thông tin cá nhân");
                } else {
                    tab.setText("Khoản chi trong tháng");
                }
            }
        });

        mediator.attach();
    }
    String maGG = "";
    ImageView anhGG;
    private void naptien() {
        kiemtra = 2;
        linkAnhGiaoDich="";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_naptien, null, false);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        EditText email,sotien;
        Button gui,huy;

        email = view.findViewById(R.id.edt_email_naptien);
        sotien = view.findViewById(R.id.edt_sotien);
        anhGG = view.findViewById(R.id.imv_anhchupmanhinh);
        gui = view.findViewById(R.id.btn_gui);
        huy = view.findViewById(R.id.btn_Huy_yc);
        maGG = UUID.randomUUID().toString();

        anhGG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yeucauquyen(ThongTinTaiKhoan.this);
            }
        });
      gui.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if (email.getText().toString().trim().isEmpty()){
                  Toast.makeText(ThongTinTaiKhoan.this, "Không được để trống Email", Toast.LENGTH_SHORT).show();
                  return;
              }
              if (sotien.getText().toString().trim().isEmpty()){
                  Toast.makeText(ThongTinTaiKhoan.this, "Không được để trống số tiền", Toast.LENGTH_SHORT).show();
                  return;
              }
              if (linkAnhGiaoDich.isEmpty()){
                  Toast.makeText(ThongTinTaiKhoan.this, "Vui lòng thêm ảnh", Toast.LENGTH_SHORT).show();
                  return;
              }
              String time =String.format("%02d/%02d/%02d",Calendar.getInstance().get(Calendar.DAY_OF_MONTH), (Calendar.getInstance().get(Calendar.MONTH)+1), Calendar.getInstance().get(Calendar.YEAR)) +"/ - "+
                      String.format("%02d:%02d:%02d",new Date().getHours(),new Date().getMinutes(),new Date().getSeconds());

              HashMap<String , Object> map = new HashMap<>();
              map.put("maGG",maGG);
              map.put("maND",us.getMaUser());
              map.put("email",email.getText().toString().trim());
              map.put("sotien",Long.parseLong(sotien.getText().toString().trim()));
              map.put("anh",linkAnhGiaoDich);
              map.put("time",time);
              map.put("timeSort",new Date().getTime());
              map.put("trangThai",0);

              db.collection("naptien").document(maGG).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                  @Override
                  public void onComplete(@NonNull Task<Void> task) {
                      if (task.isComplete()){
                          Toast.makeText(ThongTinTaiKhoan.this, "Vui lòng đợi vài phút để hệ thống kiểm tra", Toast.LENGTH_SHORT).show();
                          linkAnhGiaoDich="";
                          dialog.dismiss();
                      }else {
                          Toast.makeText(ThongTinTaiKhoan.this, "Lỗi", Toast.LENGTH_SHORT).show();
                      }
                  }
              });
          }
      });
huy.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        linkAnhGiaoDich="";
        dialog.dismiss();
    }
});
    }

    ImageView anh;

    public void suaProFile() {
        kiemtra = 1;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_updateprofile, null, false);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        anh = view.findViewById(R.id.imv_addAnh_edit);
        EditText ten = view.findViewById(R.id.edt_hoten_edit);
        EditText email = view.findViewById(R.id.edt_email_edit);
        EditText sdt = view.findViewById(R.id.edt_sdt_edit);
        Button sua = view.findViewById(R.id.btn_edit);

        anh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yeucauquyen(ThongTinTaiKhoan.this);
            }
        });
        if (us == null && user == null) {
            return;
        }
        Glide.with(this).load(user.getPhotoUrl()).
                error(R.drawable.baseline_crop_original_24).into(anh);
        if (user.getPhotoUrl()!=null){
            linkMoi = user.getPhotoUrl().toString();
        }

        ten.setText(us.getHoTen());
        email.setText(us.getEmail());
        sdt.setText(us.getSDT());
        sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (us == null || user == null || email.getText().toString().isEmpty() || ten.getText().toString().isEmpty() || sdt.getText().toString().isEmpty()||linkMoi==null) {
                    Toast.makeText(ThongTinTaiKhoan.this, "Không được để trống", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isValidatePhone(sdt.getText().toString())||sdt.getText().toString().length()<10){
                    Toast.makeText(ThongTinTaiKhoan.this, "Số điện thoại không đúng", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.show();
                us.setEmail(email.getText().toString());
                us.setHoTen(ten.getText().toString());
                us.setSDT(sdt.getText().toString());
                setData();
                seTaiKhoan(Uri.parse(linkMoi));

                dialog.dismiss();
            }
        });


    }
    public boolean isValidatePhone(CharSequence e) {
        return !TextUtils.isEmpty(e) && Patterns.PHONE.matcher(e).matches();
    }
    public User getUser(){
        return us;
    }

    private void seTaiKhoan(Uri uri) {
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(us.getHoTen())
                .setPhotoUri(uri)
                .build();
        user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    user.updateEmail(us.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isComplete()){
                                Log.e("TAG", "onComplete: "+task.getException() );
                                Toast.makeText(ThongTinTaiKhoan.this, "Hoàn tất", Toast.LENGTH_SHORT).show();
                                ten.setText(user.getDisplayName());
                                Glide.with(ThongTinTaiKhoan.this).load(user.getPhotoUrl()).error(R.drawable.baseline_crop_original_24).into(avatar);
                            }else {
                                Toast.makeText(ThongTinTaiKhoan.this, "Lỗi", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(ThongTinTaiKhoan.this, "Lỗi", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setData() {
        db.collection("user").document(user.getUid()).set(us).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    Toast.makeText(ThongTinTaiKhoan.this, "Thay đổi sẽ được thực hiện vào lần đăng nhập sau", Toast.LENGTH_SHORT).show();
                    progressDialog.cancel();
                } else {
                    Toast.makeText(ThongTinTaiKhoan.this, "Lỗi", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void nghe() {

        db.collection("user").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (!task.isComplete()) {
                    Toast.makeText(ThongTinTaiKhoan.this, "Lỗi", Toast.LENGTH_SHORT).show();
                    return;
                }
                us = task.getResult().toObject(User.class);
                adapterThongtin.data(us);
                if (us == null) {
                    Toast.makeText(ThongTinTaiKhoan.this, "Lỗi", Toast.LENGTH_SHORT).show();
                    return;
                }
                Glide.with(ThongTinTaiKhoan.this).
                        load(user.getPhotoUrl()).error(R.drawable.user1).into(avatar);
                if (us.getHoTen() == null) {
                    return;
                }
                if (us.getHoTen().isEmpty()||us.getSDT()==null){
                    suaProFile();
                    return;
                }
                ten.setText(us.getHoTen());
                tien.setText("Số dư: " +  NumberFormat.getNumberInstance(Locale.getDefault()).format(us.getSoDu()) + " VND");
                if (us.getDiachi()==null||us.getDiachi().size()==0){
                    addDiaChi();
                    return;
                }
                if (us.getChonDiaCHi()==null||us.getChonDiaCHi().isEmpty()){
                    Toast.makeText(ThongTinTaiKhoan.this, "Vui lòng chọn địa chỉ trong list", Toast.LENGTH_SHORT).show();
                    addDiaChi();
                    return;
                }

            }
        });
    }

    public void layAnh() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        launcher.launch(intent);
        progressDialog.show();
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

    Uri uri;
    int kiemtra = 0;
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
                       if (kiemtra == 1){
                           upAnh(uri);
                       }else {
                          anhManhinh(uri,maGG,anhGG);
                       }

                    }

                }
            });
    String linkMoi;
    ProgressDialog progressDialog;

    public void upAnh(Uri imageUri) {
        StorageReference storageReference;
        storageReference = FirebaseStorage.getInstance().getReference("images").child(user.getUid());
        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri uri = task.getResult();
                                    linkMoi = uri.toString();
                                    Glide.with(ThongTinTaiKhoan.this).load(linkMoi).into(anh);
                                    progressDialog.cancel();
                                } else {
                                    Toast.makeText(ThongTinTaiKhoan.this, "Lỗi khi lấy đường dẫn", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ThongTinTaiKhoan.this, "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    List<String> list_diaChi = new ArrayList<>();
    public void addDiaChi() {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_them_hang, null, false);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ListView listView = view.findViewById(R.id.list_hang);
        EditText edt_diachi = view.findViewById(R.id.edt_themhang_);
        TextView tv = view.findViewById(R.id.tv_tittle2);
        ImageButton themHang = view.findViewById(R.id.ibtn_addhang);
        edt_diachi.setHint("Số nhà,Ngõ,Đường,Quận,Thành Phố");
        tv.setText("Địa chỉ");
        edt_diachi.setVisibility(View.GONE);

        if (us.getDiachi()==null){
            list_diaChi = new ArrayList<>();
        }else {
            list_diaChi = us.getDiachi();
        }

        Adapter_diachi adapter = new Adapter_diachi(list_diaChi, this);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(ThongTinTaiKhoan.this);
                builder1.setTitle("Cảnh báo").setIcon(R.drawable.cancel).setMessage("Nếu bạn xác nhận dữ liệu sẽ mất mãi mãi");
                builder1.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder1.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // xóa phần tử list ở đây
                        list_diaChi.remove(position);
                        us.setDiachi(list_diaChi);
                        db.collection("user").document(us.getMaUser()).set(us).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete()) {
                                    Toast.makeText(ThongTinTaiKhoan.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                    adapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(ThongTinTaiKhoan.this, "Lỗi", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                builder1.create().show();

                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // chọn địa chỉ
                us.setChonDiaCHi(list_diaChi.get(position));
                db.collection("user").document(user.getUid()).set(us).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                       if (task.isComplete()){
                           Toast.makeText(ThongTinTaiKhoan.this, "Đơn hàng của bạn sẽ được giao tới địa chỉ này", Toast.LENGTH_SHORT).show();
                       }else {
                           Toast.makeText(ThongTinTaiKhoan.this, "Lỗi", Toast.LENGTH_SHORT).show();
                       }
                    }
                });
                dialog.dismiss();
            }
        });
        final int[] change = {0};
        themHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (change[0] == 0) {
                    edt_diachi.setVisibility(View.VISIBLE);
                    change[0] = 1;
                } else {

                    if(edt_diachi.getText().toString().isEmpty()){
                        Toast.makeText(ThongTinTaiKhoan.this, "Người dùng không được để trống", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    edt_diachi.setVisibility(View.GONE);
                    change[0] = 0;
                    list_diaChi.add(edt_diachi.getText().toString());
                    us.setDiachi(list_diaChi);
                    db.collection("user").document(us.getMaUser()).set(us).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isComplete()) {
                                Toast.makeText(ThongTinTaiKhoan.this, "Thêm địa chỉ thành công", Toast.LENGTH_SHORT).show();
                                edt_diachi.setText("");
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(ThongTinTaiKhoan.this, "Lỗi", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
String linkAnhGiaoDich="";
    public void anhManhinh(Uri imageUri,String magiaodich,ImageView anhGG) {
        StorageReference storageReference;
        storageReference = FirebaseStorage.getInstance().getReference("images").child(magiaodich);
        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri uri = task.getResult();
                                    linkAnhGiaoDich = uri.toString();
                                    Glide.with(ThongTinTaiKhoan.this).load(linkAnhGiaoDich).into(anhGG);
                                    progressDialog.cancel();
                                } else {
                                    Toast.makeText(ThongTinTaiKhoan.this, "Lỗi khi lấy đường dẫn", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ThongTinTaiKhoan.this, "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
