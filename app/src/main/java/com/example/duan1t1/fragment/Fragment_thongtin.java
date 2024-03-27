package com.example.duan1t1.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.duan1t1.DangNhap_Activity;
import com.example.duan1t1.ManHinhAdmin;
import com.example.duan1t1.R;
import com.example.duan1t1.ThongTinTaiKhoan;
import com.example.duan1t1.adapter.Adapter_choduyet;
import com.example.duan1t1.adapter.Adapter_naptien;
import com.example.duan1t1.model.DonHang;
import com.example.duan1t1.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Fragment_thongtin extends Fragment {
    LinearLayout thongtin, diachi, lichsu, doimatkhau, dangxuat, lichuGG;
    FirebaseFirestore db;
    User us;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_thongtincanhan, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        anhxa(view);
        ThongTinTaiKhoan tk = (ThongTinTaiKhoan) getActivity();
        dangxuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangxuat();
            }
        });
        thongtin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tk.suaProFile();
            }
        });
        diachi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tk.addDiaChi();
            }
        });
        doimatkhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ManHinhAdmin.doipass(getActivity());
            }
        });

        lichsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XemLichSu();
            }
        });
        lichuGG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lichuGG();
            }
        });
    }
    Adapter_naptien adapterNaptien;
    private void lichuGG() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_lichsu,null,false);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        RecyclerView rcv_list_lichsu = view.findViewById(R.id.rcv_list_lichsu);
        TextView textView = view.findViewById(R.id.tv_thongbao_rong);
        List<HashMap<String,Object>> list = new ArrayList<>();
        adapterNaptien  = new Adapter_naptien(list,getContext());
        rcv_list_lichsu.setAdapter(adapterNaptien);
        rcv_list_lichsu.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        getListGG(list);
    }
    private void getListGG(List<HashMap<String,Object>> listA) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("naptien").whereEqualTo("maND",user.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.isComplete()){
                    return;
                }
                for (QueryDocumentSnapshot dc : task.getResult()){
                    listA.add((HashMap<String, Object>) dc.getData());
                    listA.sort(new Comparator<HashMap<String, Object>>() {
                        @Override
                        public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2) {
                            return (int) (Long.parseLong(o2.get("timeSort").toString())-Long.parseLong(o1.get("timeSort").toString()));
                        }
                    });
                    adapterNaptien.notifyDataSetChanged();
                }
            }
        });

    }

    private void XemLichSu() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_lichsu,null,false);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        RecyclerView rcv_list_lichsu = view.findViewById(R.id.rcv_list_lichsu);
        List<DonHang> list = new ArrayList<>();
        Adapter_choduyet adapterChoduyet = new Adapter_choduyet(list,getContext(),2);
        getData(list,adapterChoduyet,rcv_list_lichsu);
        rcv_list_lichsu.setAdapter(adapterChoduyet);
        rcv_list_lichsu.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
    }

    private void getData(List<DonHang> list,Adapter_choduyet adapterChoduyet,RecyclerView rcv_list) {
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("donHang").whereEqualTo("trangThai",1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.isComplete()){
                    return;
                }
                for (QueryDocumentSnapshot dc : task.getResult()){
                    if (user.getUid().equals(dc.toObject(DonHang.class).getMaKhachHang())) {
                        list.add(dc.toObject(DonHang.class));
                        adapterChoduyet.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void dangxuat() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Thông báo");
        builder.setIcon(R.drawable.user1);
        builder.setMessage("Bạn có muốn đăng xuất");
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), DangNhap_Activity.class);
                getActivity().finishAffinity();
                if (!getActivity().isFinishing()) {
                    return;
                }
                startActivity(intent);
            }
        });
        builder.create().show();

    }
    private void anhxa(View view) {
        thongtin = view.findViewById(R.id.ll_thongtincanhan);
        diachi = view.findViewById(R.id.ll_diaChi);
        lichsu = view.findViewById(R.id.ll_lichsumua);
        doimatkhau = view.findViewById(R.id.ll_doimatkhau);
        dangxuat = view.findViewById(R.id.ll_dangxuat);
        lichuGG = view.findViewById(R.id.ll_lichnap);
        db = FirebaseFirestore.getInstance();
    }


    public void setUs(User us) {
        this.us = us;
    }
}
