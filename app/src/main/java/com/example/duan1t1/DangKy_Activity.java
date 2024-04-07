package com.example.duan1t1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.duan1t1.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class DangKy_Activity extends AppCompatActivity {
    //Khai báo các biến
    private EditText email, matKhau, reMatKhau;
    private Button dangKy;
    private ProgressDialog progressDialog;
    private FirebaseFirestore db;
    private Intent intent;
    private DocumentReference reference;

    // sự kiện ấn nút đăng ký
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        //gọi ánh xạ để
        anhXa();
        dangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangKy();
            }
        });
    }

    //anh xa
    private void anhXa() {
        //ánh xạ email
        email = findViewById(R.id.edt_email_dangnky);
        //ánh xạ mật khẩu
        matKhau = findViewById(R.id.edt_matkhau_dangky);
        //ánh xạ nhập lại mật khẩu
        reMatKhau = findViewById(R.id.edt_rematkhau_dangky);
        //ánh xạ đăng ký
        dangKy = findViewById(R.id.btn_dangky_on);
        //hộp thoại ProgressDialog sẽ được tạo ra và gắn liền với activity hiện tại.
        progressDialog = new ProgressDialog(this);

    }

    //code dang ky
    public void dangKy() {
        //Dòng này lấy giá trị nhập vào từ trường email
        String email1 = email.getText().toString().trim();

        //dòng này lấy giá trị nhập vào từ trường mật khẩu
        String pass1 = matKhau.getText().toString().trim();

        //Dòng này khởi tạo một thể hiện của lớp FirebaseAuth
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        //Hiển thị ProgressDialog để thông báo việc đang tải dữ liệu.
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Sẽ mất một lúc vui lòng chờ");
        progressDialog.show();

        //Kiểm tra tính hợp lệ của dữ liệu đầu vào:
        if (email1.isEmpty() || pass1.isEmpty()) {
            Toast.makeText(this, "Không được để trống", Toast.LENGTH_SHORT).show();
            progressDialog.cancel();
            return;
        }
        if (!pass1.equals(reMatKhau.getText().toString().trim())) {
            Toast.makeText(this, "Mật khẩu nhập lại phải giống nhau", Toast.LENGTH_SHORT).show();
            progressDialog.cancel();
            return;
        }
        if (!(pass1.length() >= 6)) {
            Toast.makeText(this, "Mật khẩu phải dài hơn 6 ký tụ", Toast.LENGTH_SHORT).show();
            return;
        }

        //Nếu dữ liệu đầu vào hợp lệ, thực hiện đăng ký người dùng mới bằng phương thức
        mAuth.createUserWithEmailAndPassword(email1, pass1)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(DangKy_Activity.this, ManHinhKhachHang.class);
                            startActivity(intent);
                            taoUser();
                        } else {
                            Toast.makeText(DangKy_Activity.this, "Đăng ký tài khoản thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    //Đoạn code này thực hiện việc tạo một tài khoản người dùng mới trong cơ sở dữ liệu Firestore
    // sau khi người dùng đã đăng ký thành công bằng Firebase Authentication
    private void taoUser() {
        //Dòng này lấy thông tin về người dùng hiện tại từ Firebase Authentication. Nếu không có người
        // dùng nào đang đăng nhập, user sẽ bằng null.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        if (user == null) {
            return;
        }

        // Thực hiện việc tạo một tài khoản người dùng mới trong collection "user" của
        // Firestore với ID của người dùng là UID của người dùng hiện tại
        db.collection("user").document(user.getUid()).set(new User(user.getUid(), user.getEmail(), "", 1, 0l, 3))
                //Xử lý sự kiện khi tạo tài khoản người dùng thành công.
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(DangKy_Activity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                        finishAffinity();
                    }

                    // Xử lý sự kiện khi có lỗi xảy ra trong quá trình tạo tài khoản người dùng trong Firestore.
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DangKy_Activity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
