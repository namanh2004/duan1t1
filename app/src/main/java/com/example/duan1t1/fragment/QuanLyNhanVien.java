package com.example.duan1t1.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.duan1t1.R;
import com.example.duan1t1.adapter.AdapterUser;
import com.example.duan1t1.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class QuanLyNhanVien extends Fragment {
    List<User> list;
    EditText edt_maNV, edt_Email, edt_hoTen, edt_sdt, edt_cv;
    AppCompatButton btn_Luu, btn_Huy;
    RecyclerView recyclerView;
    ImageButton button;
    AdapterUser adapterUser;
    SearchView searchView;
    String id;
    User user = new User();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth;
    String email, matkhau, hoten, sdt, cv;
    Dialog dialog;

    public QuanLyNhanVien() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quan_ly_nhan_vien, null);
        recyclerView = view.findViewById(R.id.rcv_nhanVien);
        button = view.findViewById(R.id.ibtn_them_nv);
        searchView = view.findViewById(R.id.search_NV);
        loatData();

        firebaseAuth = FirebaseAuth.getInstance();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterUser.getTen().filter(newText);
                adapterUser.notifyDataSetChanged();
                return true;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater2 = getLayoutInflater();
                View view1 = inflater2.inflate(R.layout.dialog_nhanvien, null);
                builder.setView(view1);
                dialog = builder.create();
                dialog.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                edt_Email = view1.findViewById(R.id.edt_email);
                edt_hoTen = view1.findViewById(R.id.edt_hoTen);
                edt_sdt = view1.findViewById(R.id.edt_sdt);
                btn_Luu = view1.findViewById(R.id.btn_Luu);
                btn_Huy = view1.findViewById(R.id.btn_Huy);

                btn_Luu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        email = edt_Email.getText().toString();
                        hoten = edt_hoTen.getText().toString();
                        sdt = edt_sdt.getText().toString();
                        id = UUID.randomUUID().toString();


                        if (email.isEmpty() || hoten.isEmpty() || sdt.isEmpty()) {
                            Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        } else if (!isValidateEmail(email)) {
                            Toast.makeText(getContext(), "Không đúng định dạng của email", Toast.LENGTH_SHORT).show();
                        } else if (!isValidatePhone(sdt) || sdt.length() < 10) {
                            Toast.makeText(getContext(), "Số điện thoại không đúng", Toast.LENGTH_SHORT).show();
                        } else {
                            themTK();


                        }
                    }
                });


                btn_Huy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });


        return view;
    }

    public boolean isValidateEmail(CharSequence e) {
        return !TextUtils.isEmpty(e) && Patterns.EMAIL_ADDRESS.matcher(e).matches();
    }

    public boolean isValidatePhone(CharSequence e) {
        return !TextUtils.isEmpty(e) && Patterns.PHONE.matcher(e).matches();
    }

    public void loatData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        nghe();
        adapterUser = new AdapterUser(getContext(), list);
        recyclerView.setAdapter(adapterUser);
    }

    public void themTK() {
        firebaseAuth.createUserWithEmailAndPassword(email, UUID.randomUUID().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                user.setMaUser(user1.getUid());
                user.setEmail(email);
                user.setHoTen(hoten);
                user.setSDT(sdt);
                user.setChucVu(2);
                user.setTrangThai(1);
                db.collection("user").document(user.getMaUser()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                            Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                            guiMail(user.getEmail());
                            FirebaseAuth.getInstance().signOut();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getContext(), "Lỗi", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Toast.makeText(getContext(), "Thành công", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void guiMail(String email) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = email;
        if (emailAddress.isEmpty()) {
            return;
        }
        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Yêu cầu đặt đến email của nhân viên", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void nghe() {
        db.collection("user").whereEqualTo("chucVu", 2).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }
                if (value != null) {
                    for (DocumentChange dc : value.getDocumentChanges()) {
                        switch (dc.getType()) {
                            case ADDED:
                                dc.getDocument().toObject(User.class);
                                list.add(dc.getDocument().toObject(User.class));
                                adapterUser.notifyDataSetChanged();
                                Log.e("TAG", "loi" + dc.getDocument().toObject(User.class));
                                break;
                            case MODIFIED:
                                User user1 = dc.getDocument().toObject(User.class);
                                if (dc.getOldIndex() == dc.getNewIndex()) {
                                    list.set(dc.getOldIndex(), user1);
                                    adapterUser.notifyDataSetChanged();
                                } else {
                                    list.remove(dc.getOldIndex());
                                    list.add(user1);
                                    adapterUser.notifyDataSetChanged();
                                }
                                break;
                            case REMOVED:
                                dc.getDocument().toObject(User.class);
                                list.remove(dc.getOldIndex());
                                adapterUser.notifyDataSetChanged();
                        }
                    }
                }
            }
        });

    }

}
//        onCreateView(): Phương thức này được gọi để tạo và trả về giao diện người dùng của Fragment. Trong phương thức này:
//
//        Khởi tạo và ánh xạ các thành phần giao diện như RecyclerView, SearchView và nút thêm nhân viên.
//        Gọi phương thức loatData() để tải dữ liệu và hiển thị danh sách nhân viên.
//        Thiết lập sự kiện cho nút thêm nhân viên để mở một dialog cho phép người dùng thêm nhân viên mới.
//        loatData(): Phương thức này được sử dụng để tải dữ liệu và hiển thị danh sách nhân viên:
//
//        Thiết lập RecyclerView và Adapter để hiển thị danh sách nhân viên.
//        Gọi phương thức nghe() để lắng nghe sự kiện và cập nhật danh sách nhân viên từ Firebase Firestore.
//        nghe(): Phương thức này được sử dụng để lắng nghe sự kiện và cập nhật danh sách nhân viên từ Firebase Firestore:
//
//        Sử dụng whereEqualTo("chucVu", 2) để lọc danh sách nhân viên với chức vụ là 2 (nhân viên).
//        Thêm các sự kiện để lắng nghe các thay đổi trong danh sách nhân viên và cập nhật giao diện người dùng tương ứng.
//        isValidateEmail() và isValidatePhone(): Đây là hai phương thức kiểm tra tính hợp lệ của email và số điện thoại được nhập vào, sử dụng các biểu thức chính quy để kiểm tra.
//
//        themTK(): Phương thức này được sử dụng để thêm một tài khoản người dùng mới vào Firebase Authentication và thông tin tương ứng vào Firestore:
//
//        Sử dụng createUserWithEmailAndPassword() để tạo một tài khoản mới với email và mật khẩu được cung cấp.
//        Thêm thông tin người dùng mới vào Firestore.
//        Gửi một email xác nhận đến địa chỉ email của người dùng mới.
//        guiMail(): Phương thức này được sử dụng để gửi một email xác nhận đặt lại mật khẩu đến địa chỉ email được cung cấp.