package com.example.duan1t1;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.duan1t1.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;


public class DangNhap_Activity extends AppCompatActivity {
    private Button dangky, dangNhap;
    private EditText email, matKhau;
    private TextView quenMK;
    private ProgressDialog progressDialog;
    private FirebaseFirestore db;
    private DocumentReference reference;
    private Intent intent;
    private ListenerRegistration registration;
    LinearLayout btnDangNhapGG;
    GoogleSignInClient client;
    FirebaseAuth auth;
    // sử dụng để xử lý kết quả trả về từ một hoạt động khác (activity) mà bạn đã khởi chạy bằng phương thức startActivityForResult().
    private final ActivityResultLauncher<Intent> activityResultLauncher
            = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        //Phương thức này được gọi khi một kết quả trả về từ hoạt động đã được hoàn thành.
        public void onActivityResult(ActivityResult o) {
            // kiểm tra xem kết quả trả về có thành công không (RESULT_OK).
            if (o.getResultCode() == RESULT_OK) {
                //để lấy thông tin tài khoản đã đăng nhập.
                Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(o.getData());
                try {
                    GoogleSignInAccount signInAccount = accountTask.getResult(ApiException.class);
                    //Nếu lấy thông tin tài khoản thành công, sử dụng GoogleAuthProvider.getCredential() để lấy thông tin xác thực từ tài khoản Google.
                    AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
                    //Tiếp theo, sử dụng thông tin xác thực để đăng nhập vào Firebase Authentication bằng phương thức signInWithCredential().
                    auth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //Trong trường hợp đăng nhập thành công, chuyển đến màn hình ManHinhKhachHang và hiển thị thông báo "Đăng nhập thành công".
                            if (task.isComplete()) {
                                auth = FirebaseAuth.getInstance();
                                chuyen(ManHinhKhachHang.class);
                                Toast.makeText(DangNhap_Activity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(DangNhap_Activity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //Trong trường hợp xảy ra lỗi, hiển thị thông báo "Lỗi".
                } catch (ApiException e) {
                    Toast.makeText(DangNhap_Activity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                    throw new RuntimeException(e);
                }
            }
        }
    });

    //Phương thức này được gọi khi Activity được tạo ra và có tác dụng khởi tạo
    // giao diện của Activity và thiết lập các sự kiện lắng nghe cho các thành phần trong giao diện.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        anhxa();// gọi ánh xạ các nút

        //Gán một sự kiện nhấn cho nút "Đăng ký" để chuyển đến màn hình đăng ký (DangKy_Activity).
        dangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chuyen(DangKy_Activity.class);
            }
        });

        //Gán một sự kiện nhấn cho nút "Đăng nhập" để thực hiện phương thức dangnhap().
        dangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangnhap();
            }
        });
        //Gán một sự kiện nhấn cho TextView "Quên mật khẩu" để mở hộp thoại quên mật khẩu (quenMK()).
        quenMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quenMK();
            }
        });

    }

    private void DangNhapGG() {
        Intent intent = client.getSignInIntent();
        activityResultLauncher.launch(intent);
    }

    //
    //Đoạn mã này định nghĩa phương thức quenMK(), có tác dụng mở
    // một hộp thoại (dialog) cho phép người dùng nhập địa chỉ email để yêu cầu đặt lại mật khẩu.
    private void quenMK() {
        //Tạo một đối tượng AlertDialog.Builder để xây dựng hộp thoại.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Lấy ra một instance của LayoutInflater, được sử dụng để inflate (nạp) layout từ tệp XML.
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_quenpass, null, false);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        EditText email = view.findViewById(R.id.edt_email_quen);
        Button gui = view.findViewById(R.id.btn_quen);
        // Gán một sự kiện nhấn cho nút "Gửi" trong hộp thoại. Khi người dùng nhấn nút này,
        // phương thức onClick() được gọi và gọi phương thức quenPass() để xử lý yêu cầu đặt lại mật khẩu.
        gui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quenPass(email, dialog);

            }
        });
    }

    //Đoạn mã này định nghĩa phương thức quenPass(EditText email, Dialog dialog),
    // được gọi khi người dùng nhấn nút "Gửi" trong hộp thoại quên mật khẩu
    private void quenPass(EditText email, Dialog dialog) {
        // Lấy ra một thể hiện của lớp FirebaseAuth, được sử dụng để quản lý xác thực người dùng
        FirebaseAuth auth = FirebaseAuth.getInstance();
        //Lấy địa chỉ email mà người dùng đã nhập từ EditText và chuyển thành chuỗi.
        String emailAddress = email.getText().toString();
        if (emailAddress.isEmpty()) {
            Toast.makeText(this, "Không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Sẽ mất một lúc vui lòng chờ");
        progressDialog.show();
        //Đặt một OnCompleteListener để xử lý kết quả của việc gửi email.
        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //kiểm tra xem việc gửi email đã hoàn thành thành công hay không. Nếu thành công (task.isSuccessful()),
                        // hiển thị một thông báo cho người dùng thông báo rằng email đã được gửi và đóng hộp thoại quên mật khẩu (dialog.dismiss()).
                        if (task.isSuccessful()) {
                            progressDialog.cancel();
                            Toast.makeText(DangNhap_Activity.this, "Đã gửi link khôi phục hãy kiểm tra email", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
    }

    //được sử dụng để ánh xạ (mapping) các thành phần giao diện từ tệp layout XML
    // sang các biến trong mã Java và khởi tạo một số đối tượng cần thiết cho việc đăng nhập.
    private void anhxa() {
        dangky = findViewById(R.id.btn_dangky);
        dangNhap = findViewById(R.id.btn_dangnhap);
        email = findViewById(R.id.edt_email_dangnhap);
        matKhau = findViewById(R.id.edt_matkhau_dangnhap);
        quenMK = findViewById(R.id.tv_quenpass);
        // Khởi tạo một đối tượng ProgressDialog, được sử dụng để hiển thị tiến trình cho người dùng trong quá trình đăng nhập.
        progressDialog = new ProgressDialog(this);
        // Khởi tạo một thể hiện của lớp FirebaseFirestore, được sử dụng để truy cập và tương tác với cơ sở dữ liệu Firestore của Firebase.
        db = FirebaseFirestore.getInstance();
        //Xây dựng các tùy chọn cho việc đăng nhập bằng Google, bao gồm yêu cầu token xác thực (requestIdToken) và yêu cầu thông tin email (requestEmail).
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                .requestEmail()
                .build();
        client = GoogleSignIn.getClient(DangNhap_Activity.this, googleSignInOptions);
        auth = FirebaseAuth.getInstance();
    }

    //được gọi khi người dùng nhấn nút "Đăng nhập".
    public void dangnhap() {
        String mEmail = email.getText().toString().trim();
        String mPass = matKhau.getText().toString().trim();
        // Lấy ra một thể hiện của lớp FirebaseAuth, được sử dụng để quản lý xác thực người dùng.
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (email.getText().toString().isEmpty() || matKhau.getText().toString().isEmpty()) {
            Toast.makeText(this, "Không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Sẽ mất một lúc vui lòng chờ");
        progressDialog.show();


//Sử dụng phương thức signInWithEmailAndPassword() của đối tượng mAuth để thực hiện xác thực đăng nhập bằng email và mật khẩu mà người dùng đã nhập.
        mAuth.signInWithEmailAndPassword(mEmail, mPass)
                // Đặt một OnCompleteListener để xử lý kết quả của việc xác thực đăng nhập.
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //kiểm tra xem việc đăng nhập đã thành công hay không. Nếu thành công (task.isSuccessful()),
                        // lấy thông tin người dùng hiện tại và gọi phương thức checkBan()
                        // để kiểm tra tình trạng của tài khoản người dùng. Nếu không thành công,
                        // hiển thị một thông báo lỗi và ẩn hộp thoại tiến trình (progressDialog.cancel()).
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            checkBan(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(DangNhap_Activity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        }

                    }
                });
    }

    // được sử dụng để kiểm tra trạng thái của tài khoản người dùng trong cơ sở dữ liệu Firebase trước khi cho phép họ đăng nhập
    private void checkBan(FirebaseUser user) {
        // Truy vấn cơ sở dữ liệu Firestore để lấy thông tin về người dùng dựa trên mã người dùng (maUser) đã được cung cấp.
        db.collection("user").whereEqualTo("maUser", user.getUid()).get()
                //Đặt một OnCompleteListener để xử lý kết quả của truy vấn.
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                // kiểm tra xem việc truy vấn đã hoàn thành thành công hay không (task.isSuccessful()).
                // Nếu không thành công, hiển thị một thông báo lỗi.
                if (!task.isComplete()) {
                    Toast.makeText(DangNhap_Activity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (task.isSuccessful()) {
                    if (task.getResult().toObjects(User.class).get(0).getTrangThai() == 1) {
                        DangNhap(task.getResult().toObjects(User.class).get(0));
                    } else {
                        Toast.makeText(DangNhap_Activity.this, "Tài khoản bạn đã bị đình chỉ vui lòng liên hệ", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        finish();
                    }

                }
            }
        });
    }

    //được sử dụng để chuyển từ hoạt động hiện tại sang một hoạt động mới được
    // chỉ định bởi đối số a, là một lớp (class) hoạt động trong ứng dụng Android.
    private void chuyen(Class a) {
        Intent intent = new Intent(this, a);
        startActivity(intent);
    }


    //được sử dụng để xác định loại người dùng
    // (admin, nhân viên, hoặc khách hàng) và chuyển hướng đến màn hình tương ứng sau khi đăng nhập thành công
    private void DangNhap(User user) {
        if (user.getChucVu() == 1) {
            intent = new Intent(DangNhap_Activity.this, ManHinhAdmin.class);
        } else if (user.getChucVu() == 2) {
            intent = new Intent(DangNhap_Activity.this, ManHinhNhanVien.class);
        } else if (user.getChucVu() == 3) {
            intent = new Intent(DangNhap_Activity.this, ManHinhKhachHang.class);
        } else {
            Toast.makeText(DangNhap_Activity.this, "Lỗi", Toast.LENGTH_SHORT).show();
        }
        finishAffinity();
        if (!isFinishing()) {
            return;
        }
        //Thêm cờ để xác định cách mà hoạt động mới sẽ được thêm vào stack. FLAG_ACTIVITY_CLEAR_TASK
        // xóa tất cả các hoạt động trước đó khỏi stack khi hoạt động mới được khởi chạy,
        // và FLAG_ACTIVITY_NEW_TASK tạo một task mới cho hoạt động mới.
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }


}