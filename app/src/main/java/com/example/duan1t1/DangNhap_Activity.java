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

    // xử lý đăng nhập bằng Google (Google Sign-In) trong ứng dụng Android

    //biến để khởi tạo một ActivityResultLauncher
    private final ActivityResultLauncher<Intent> activityResultLauncher

            //đăng ký một callback được gọi sau khi hoạt động kết thúc và trả về kết quả cho hoạt động gọi
            = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {

        //phương thức được gọi khi kết quả từ hoạt động khởi chạy bằng Intent trả về.
        @Override
        public void onActivityResult(ActivityResult o) {
            if (o.getResultCode() == RESULT_OK) {
                Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(o.getData());
                try {
                    GoogleSignInAccount signInAccount = accountTask.getResult(ApiException.class);
                    // Phương thức này trích xuất thông tin tài khoản Google từ kết quả Intent trả về sau khi người dùng đã đăng nhập thành công.
                    AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
                    auth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isComplete()) {
                                //Nếu quá trình đăng nhập thành công, phương thức này chuyển người dùng đến Màn hình Khách hàng
                                auth = FirebaseAuth.getInstance();
                                chuyen(ManHinhKhachHang.class);
                                Toast.makeText(DangNhap_Activity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(DangNhap_Activity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (ApiException e) {
                    Toast.makeText(DangNhap_Activity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                    throw new RuntimeException(e);
                }
            }
        }
    });

    //Tạo sự kiện khi click vào các nút
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        anhxa();

        //nút đăng ký
        dangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chuyen(DangKy_Activity.class);
            }
        });
        //nút đăng nhập
        dangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangnhap();
            }
        });
        //nút quên mật khẩu
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

    //quen mat khau
    private void quenMK() {
        //Tạo một đối tượng AlertDialog.Builder với context là activity hiện tại.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Lấy một đối tượng LayoutInflater để inflate (nạp) layout từ tệp xml.
        LayoutInflater layoutInflater = getLayoutInflater();
        //Inflate layout từ tệp xml dialog_quenpass.xml để tạo ra một đối tượng View.
        View view = layoutInflater.inflate(R.layout.dialog_quenpass, null, false);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        EditText email = view.findViewById(R.id.edt_email_quen);
        Button gui = view.findViewById(R.id.btn_quen);

        gui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quenPass(email, dialog);

            }
        });
    }

    //có nhiệm vụ gửi yêu cầu khôi phục mật khẩu đến email được cung cấp bởi người dùng
    private void quenPass(EditText email, Dialog dialog) {
        // Lấy một thể hiện của FirebaseAuth để thực hiện các hoạt động liên quan đến xác thực người dùng trong Firebase.
        FirebaseAuth auth = FirebaseAuth.getInstance();
        //Lấy địa chỉ email từ EditText được truyền vào qua tham số email.
        String emailAddress = email.getText().toString();
        if (emailAddress.isEmpty()) {
            Toast.makeText(this, "Không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Sẽ mất một lúc vui lòng chờ");
        progressDialog.show();//Hiển thị hộp thoại tiến trình lên màn hình.

        // Gửi yêu cầu khôi phục mật khẩu đến địa chỉ email đã được người dùng cung cấp
        auth.sendPasswordResetEmail(emailAddress)

                // Thêm một listener để xử lý kết quả của việc gửi yêu cầu khôi phục mật khẩu.
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressDialog.cancel();
                            Toast.makeText(DangNhap_Activity.this, "Đã gửi link khôi phục hãy kiểm tra email", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
    }

    // ánh xạ ánh xạ các thành phần giao diện
    private void anhxa() {
        dangky = findViewById(R.id.btn_dangky);
        dangNhap = findViewById(R.id.btn_dangnhap);
        email = findViewById(R.id.edt_email_dangnhap);
        matKhau = findViewById(R.id.edt_matkhau_dangnhap);
        quenMK = findViewById(R.id.tv_quenpass);
        progressDialog = new ProgressDialog(this);

        db = FirebaseFirestore.getInstance();//Lấy một thể hiện của Firestore Database bằng cách sử dụng phương thức getInstance().

        //Tạo các tùy chọn cho đăng nhập Google, bao gồm việc yêu cầu token ID, email.
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                .requestEmail()
                .build();
        client = GoogleSignIn.getClient(DangNhap_Activity.this, googleSignInOptions);
        auth = FirebaseAuth.getInstance();
    }

    //dang nhap
    public void dangnhap() {
        String mEmail = email.getText().toString().trim();//Lấy địa chỉ email từ EditText "email" và loại bỏ các khoảng trắng ở đầu và cuối chuỗi.

        String mPass = matKhau.getText().toString().trim();// Lấy mật khẩu từ EditText "mật khẩu" và loại bỏ các khoảng trắng ở đầu và cuối chuỗi.

        FirebaseAuth mAuth = FirebaseAuth.getInstance();//Lấy một thể hiện của FirebaseAuth để thực hiện quá trình đăng nhập.

        if (email.getText().toString().isEmpty() || matKhau.getText().toString().isEmpty()) {
            Toast.makeText(this, "Không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Sẽ mất một lúc vui lòng chờ");
        progressDialog.show();

        //Thực hiện đăng nhập bằng email và mật khẩu đã nhập. Một listener được thêm vào để xử lý kết quả của việc đăng nhập.
        mAuth.signInWithEmailAndPassword(mEmail, mPass)
                //Thêm một listener để xử lý kết quả của việc đăng nhập.
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
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

    // kiểm tra trạng thái của tài khoản sau khi người dùng đăng nhập
    private void checkBan(FirebaseUser user) {

        //Truy vấn trong collection "user" để tìm các tài khoản có mã người dùng (maUser) trùng khớp với ID của người dùng hiện tại.
        db.collection("user").whereEqualTo("maUser", user.getUid()).get()

                //Thêm một listener để xử lý kết quả của truy vấn. Khi truy vấn hoàn thành, sẽ được gọi hàm onComplete()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.isComplete()) {
                            Toast.makeText(DangNhap_Activity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //Kiểm tra xem truy vấn có thành công không. Nếu thành công, tiếp tục xử lý kết quả.
                        if (task.isSuccessful()) {
                            if (task.getResult().toObjects(User.class).get(0).getTrangThai() == 1) {
                                //nếu thành công gọi phương thức đăng nhập và truyền một số đối tượng
                                DangNhap(task.getResult().toObjects(User.class).get(0));
                            } else {
                                //nếu không thành công thì đăng xuất người dùng khỏi fire base
                                Toast.makeText(DangNhap_Activity.this, "Tài khoản bạn đã bị đình chỉ vui lòng liên hệ", Toast.LENGTH_SHORT).show();
                                FirebaseAuth.getInstance().signOut();
                                finish();
                            }

                        }
                    }
                });
    }

    //chuyển màn hình
    private void chuyen(Class a) {
        Intent intent = new Intent(this, a);
        startActivity(intent);
    }


    //xác định loại tài khoản của người dùng sau khi đăng nhập và chuyển hướng đến màn hình tương ứng với loại tài khoản đó.
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
        // Kiểm tra xem activity hiện tại có đang được hoàn thành (finishing) không.
        // Nếu không, phương thức sẽ kết thúc ở đây và không thực hiện các lệnh tiếp theo.
        if (!isFinishing()) {
            return;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    // thực hiện các công việc dọn dẹp và giải phóng tài nguyên cần thiết trước khi activity bị hủy.
    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }
}