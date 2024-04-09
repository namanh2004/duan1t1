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
private EditText email,matKhau,reMatKhau;
private Button dangKy;
private ProgressDialog progressDialog;
private FirebaseFirestore db ;
    private Intent intent;
    private DocumentReference reference;

    //thiết lập giao diện cho Activity và khởi tạo các thành phần cần thiết.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
       anhXa(); // gọi hàm ánh xạ
        dangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangKy(); // thực hiện chức năng đăng ký
            }
        });
    }

    //anh xa
    private void anhXa(){
        //ánh xạ edt đăng ký
        email = findViewById(R.id.edt_email_dangnky);
        //ánh xạ edt mật khẩu
        matKhau = findViewById(R.id.edt_matkhau_dangky);
        //ánh xạ edt nhập lại mk
        reMatKhau = findViewById(R.id.edt_rematkhau_dangky);
        //ánh xạ button đăng ký
        dangKy = findViewById(R.id.btn_dangky_on);

        //// tạo một đối tượng ProgressDialog mới. ProgressDialog là một hộp thoại hiển thị
        // thông báo cho người dùng về tiến trình đang diễn ra hoặc chờ đợi.
        progressDialog = new ProgressDialog(this);

    }

   //code dang ky
   public void dangKy(){
        // Lấy email và password từ EditTexts
       String email1 = email.getText().toString().trim();
       String pass1 = matKhau.getText().toString().trim();
       //Khởi tạo một đối tượng FirebaseAuth (mAuth) để sử dụng các phương thức của Firebase Authentication.
       FirebaseAuth mAuth = FirebaseAuth.getInstance();
       progressDialog.setTitle("Loading");
       progressDialog.setMessage("Sẽ mất một lúc vui lòng chờ");
       progressDialog.show();
       // phần này check các lỗi
       if (email1.isEmpty()||pass1.isEmpty()){
           Toast.makeText(this, "Không được để trống", Toast.LENGTH_SHORT).show();
           progressDialog.cancel();
           return;
       }
       if (!pass1.equals(reMatKhau.getText().toString().trim())){
           Toast.makeText(this, "Mật khẩu nhập lại phải giống nhau", Toast.LENGTH_SHORT).show();
           progressDialog.cancel();
           return;
       }
       if (!(pass1.length()>=6)){
           Toast.makeText(this, "Mật khẩu phải dài hơn 6 ký tụ", Toast.LENGTH_SHORT).show();
           return;
       }
       mAuth.createUserWithEmailAndPassword(email1, pass1)
               .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       //kiểm tra nếu quá trình đăng ký thành công
                       if (task.isSuccessful()) {
                           Intent intent = new Intent(DangKy_Activity.this,ManHinhKhachHang.class);
                           startActivity(intent);
                           taoUser();
                       } else {
                           Toast.makeText(DangKy_Activity.this, "Đăng ký tài khoản thất bại", Toast.LENGTH_SHORT).show();
                       }
                   }
               });
   }


   //phương thức này là tạo một tài khoản người dùng mới trong cơ sở dữ liệu
   // Firestore của Firebase sau khi người dùng đã đăng ký thành công thông qua Firebase Authentication.
   private void taoUser(){
        // Lấy ra thông tin của người dùng hiện tại đã đăng ký từ Firebase Authentication.
       FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
       // Khởi tạo một đối tượng Firestore để thao tác với cơ sở dữ liệu Firestore.
       db=FirebaseFirestore.getInstance();
       // Kiểm tra xem có người dùng nào đăng nhập hay không
       if (user==null){
           return;
       }
       //Tạo một tài liệu mới trong bộ sưu tập "user" của Firestore với ID của người dùng là UID của người dùng hiện tại
       db.collection("user").document(user.getUid()).set(new User(user.getUid(),user.getEmail(),"",1,0l,3)).addOnSuccessListener(new OnSuccessListener<Void>() {
           @Override
           public void onSuccess(Void unused) {
               Toast.makeText(DangKy_Activity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
               progressDialog.cancel();
               finishAffinity();
           }
           //Đăng ký các lắng nghe cho kết quả thành công và thất bại của việc thiết lập dữ liệu
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               Toast.makeText(DangKy_Activity.this, "Lỗi", Toast.LENGTH_SHORT).show();
           }
       });
   }}
//        onCreate(): Phương thức này được gọi khi Activity được tạo ra. Trong phương thức này:
//
//        Gọi phương thức anhXa() để ánh xạ các thành phần giao diện và khởi tạo ProgressDialog.
//        Thiết lập sự kiện onClick cho nút "Đăng ký" (dangKy) để gọi phương thức dangKy() khi được nhấn.
//        anhXa(): Phương thức này được sử dụng để ánh xạ các thành phần giao diện:
//
//        Ánh xạ các EditText (email, matKhau, reMatKhau) và Button (dangKy).
//        Khởi tạo ProgressDialog để hiển thị thông báo tiến trình đăng ký.
//        dangKy(): Phương thức này được sử dụng để thực hiện quá trình đăng ký người dùng mới:
//
//        Lấy email và mật khẩu từ các EditText.
//        Kiểm tra các trường hợp lỗi như trường rỗng, mật khẩu không khớp hoặc mật khẩu quá ngắn.
//        Sử dụng FirebaseAuth để tạo người dùng mới bằng phương thức createUserWithEmailAndPassword().
//        Nếu quá trình đăng ký thành công, chuyển hướng người dùng đến Activity ManHinhKhachHang và gọi phương thức taoUser() để tạo một tài khoản người dùng trong Firestore.
//        Nếu quá trình đăng ký thất bại, hiển thị thông báo lỗi.
//        taoUser(): Phương thức này được sử dụng để tạo một tài khoản người dùng mới trong Firestore:
//
//        Lấy thông tin của người dùng hiện tại từ FirebaseAuth.
//        Sử dụng FirebaseFirestore để thêm một tài liệu mới vào bộ sưu tập "user" với UID của người dùng làm ID.
//        Nếu quá trình tạo thành công, hiển thị thông báo đăng ký thành công và kết thúc Activity.
//        Nếu quá trình tạo thất bại, hiển thị thông báo lỗi.
