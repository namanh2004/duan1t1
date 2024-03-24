package com.example.duan1t1.FragmentAdmin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.duan1t1.DAO.DAOQuanTriVien;
import com.example.duan1t1.Model.QuanTriVien;
import com.example.duan1t1.R;
import com.google.android.material.textfield.TextInputEditText;


public class DoiMatKhauFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DoiMatKhauFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DoiMatKhauFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DoiMatKhauFragment newInstance(String param1, String param2) {
        DoiMatKhauFragment fragment = new DoiMatKhauFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    Button btn_cancel,btn_save;
    EditText edt_oldpassword,edt_newpassword,edt_enterthepassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doi_mat_khau, container, false);

        initUI(view);

        btnCancel();
        btnSave();

        return view;
    }

    private void btnSave() {
        DAOQuanTriVien daoQuanTriVien = new DAOQuanTriVien(getContext());
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Bạn có chắc chắn muốn thay đổi mật khẩu không?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String oldPass = edt_oldpassword.getText().toString().trim();
                        String newPass = edt_newpassword.getText().toString().trim();
                        String enterPass = edt_enterthepassword.getText().toString().trim();

                        SharedPreferences preferences = getActivity().getSharedPreferences("USER", Context.MODE_PRIVATE);
                        String user = preferences.getString("username","");
                        if (validate(oldPass,newPass,enterPass) > 0) {
                            QuanTriVien quanTriVien = daoQuanTriVien.getID(user);
                            quanTriVien.setMatKhau(newPass);
                            if (daoQuanTriVien.update(quanTriVien) > 0) {
                                Toast.makeText(getContext(), "Thay đổi mật khẩu thành công !", Toast.LENGTH_SHORT).show();
                                edt_oldpassword.setText("");
                                edt_newpassword.setText("");
                                edt_enterthepassword.setText("");
                            } else {
                                Toast.makeText(getContext(), "Thay đổi mật khẩu thất bại !", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    public int validate(String oldPass,String newPass,String enterPass) {
        int check = 1;
        if (oldPass.length() != 0 && newPass.length() != 0 && enterPass.length() != 0) {
            SharedPreferences preferences = getActivity().getSharedPreferences("USER", Context.MODE_PRIVATE);
            String getPassOld = preferences.getString("password","");
            if (!getPassOld.equals(oldPass)) {
                Toast.makeText(getContext(), "Mật khẩu cũ chưa chính xác !", Toast.LENGTH_SHORT).show();
                edt_oldpassword.requestFocus();
                check = -1;
            } else {
                if (!newPass.equals(enterPass)) {
                    Toast.makeText(getContext(), "Nhập lại mật khẩu chưa chính xác !", Toast.LENGTH_SHORT).show();
                    edt_enterthepassword.setError("Nhập lại mật khẩu chưa chính xác !");
                    edt_enterthepassword.requestFocus();
                    check = -1;
                }
            }
        } else {
            if (oldPass.length() == 0) {
                edt_oldpassword.setError("Không được để trống trường này !");
                edt_oldpassword.requestFocus();
            }
            if (newPass.length() == 0) {
                edt_newpassword.setError("Không được để trống trường này !");
                edt_newpassword.requestFocus();
            }
            if (enterPass.length() == 0) {
                edt_enterthepassword.setError("Không được để trống trường này !");
                edt_enterthepassword.requestFocus();
            }
            check = -1;
        }
        return check;
    }

    private void btnCancel() {
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_oldpassword.setText("");
                edt_newpassword.setText("");
                edt_enterthepassword.setText("");
            }
        });
    }

    private void initUI(View view) {
        btn_cancel = view.findViewById(R.id.btn_cancel);
        btn_save = view.findViewById(R.id.btn_save);

        edt_oldpassword = view.findViewById(R.id.edt_oldpassword);
        edt_newpassword = view.findViewById(R.id.edt_newpassword);
        edt_enterthepassword = view.findViewById(R.id.edt_enterthepassword);
    }
}