package com.example.duan1t1.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;


import com.example.duan1t1.R;
import com.example.duan1t1.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class Adapter_hoTro extends BaseAdapter {
    List<User> list_us;
    Context context;

    public Adapter_hoTro(List<User> list_us, Context context) {
        this.list_us = list_us;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list_us.size();
    }

    @Override
    public Object getItem(int position) {

        return list_us.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = ((Activity) context).getLayoutInflater().inflate(R.layout.item_quan_ly_khach_hang, parent,false);
        TextView ten, sdt, trangThai;
        ImageButton btn_xao;
        ten = view.findViewById(R.id.tv_Ten);
        sdt = view.findViewById(R.id.tv_Email);
        trangThai = view.findViewById(R.id.tv_Trangthai);
        btn_xao = view.findViewById(R.id.ibtn_xoa);
        btn_xao.setVisibility(View.GONE);

        ten.setText("Họ tên: " + list_us.get(position).getHoTen());
        sdt.setText("SDT: " + list_us.get(position).getSDT());
        if (list_us.get(position).getTrangThai() == 0) {
            trangThai.setText("Chưa hỗ trợ");
        }

        trangThai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Bạn đã hỗ trợ khách " + list_us.get(position).getHoTen() + "?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        trangThai.setText("Đã hỗ trợ ");
                        xoa(position);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });


        return view;
    }

    private void xoa(int p) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("hoTro").document(list_us.get(p).getMaUser()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.e("TAG","ban da ho tro khach hanh tc");
            }
        });
    }
}
