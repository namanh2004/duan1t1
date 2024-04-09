package com.example.duan1t1.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;


import com.example.duan1t1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class frg_DoiMatKhau extends Fragment {



    public frg_DoiMatKhau() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog__doi_mat_khau, null);
        EditText edt_passCu, edt_passMoi, edt_xacNhan;
        AppCompatButton btn_doiMK;
        edt_passCu = view.findViewById(R.id.edt_nhapmkcu);
        edt_passMoi = view.findViewById(R.id.edt_nhapmkmoi);
        edt_xacNhan = view.findViewById(R.id.edt_xacnhanmk);
        btn_doiMK = view.findViewById(R.id.btn_doiMK);

        btn_doiMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pasCu = edt_passCu.getText().toString();
                String pasMoi = edt_passMoi.getText().toString();
                String xacNhan = edt_xacNhan.getText().toString();

                if (pasCu.isEmpty() || pasMoi.isEmpty() || xacNhan.isEmpty()) {
                    Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else if (xacNhan.equals(pasMoi)) {
                    // Sau khi check pass cũ = với pass xác nhận thì thực hiện thay đổi mật khẩu
                    doiMK(pasCu, pasMoi);
                } else {
                    Toast.makeText(getContext(), "Xác nhận mật khẩu mới sai", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public void doiMK(String pasCu, String pasMoi) {
        //Gọi đến user hiện tại đang đăng nhập và thực hiện sửa đổi trong authention và firestore
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
                                Toast.makeText(getContext(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Mật khẩu cũ sai vui lòng nhập lại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
//        onCreateView(): Phương thức này inflates layout cho Fragment từ tập tin XML và trả về một View. Trong trường hợp này, layout được sử dụng là dialog__doi_mat_khau.xml.
//
//        btn_doiMK.setOnClickListener(): Phương thức này được gắn vào nút "Đổi mật khẩu" để xử lý sự kiện khi người dùng nhấn vào nút. Nó kiểm tra xem người dùng đã nhập đủ thông tin và xác nhận mật khẩu mới hay chưa. Nếu thông tin cần thiết đã được nhập đúng, nó gọi đến phương thức doiMK() để thực hiện việc đổi mật khẩu.
//
//        doiMK(String pasCu, String pasMoi): Phương thức này được sử dụng để thực hiện việc đổi mật khẩu của người dùng. Đầu tiên, nó xác thực lại người dùng bằng cách sử dụng mật khẩu hiện tại của họ thông qua phương thức reauthenticate(). Sau đó, nếu xác thực thành công, nó cập nhật mật khẩu mới cho người dùng thông qua phương thức updatePassword(). Cuối cùng, nó hiển thị một thông báo phản hồi cho người dùng về kết quả của quá trình đổi mật khẩu.
//
//        Tóm lại, frg_DoiMatKhau cho phép người dùng thay đổi mật khẩu của họ thông qua giao diện người dùng và cập nhật thông tin mới vào cơ sở dữ liệu Firebase Authentication.