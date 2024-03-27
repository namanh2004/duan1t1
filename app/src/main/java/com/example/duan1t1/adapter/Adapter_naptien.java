package com.example.duan1t1.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1t1.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Adapter_naptien extends RecyclerView.Adapter<Adapter_naptien.ViewHolder> {
    List<HashMap<String,Object>> list_naptien;
    Context context;
    FirebaseFirestore db;




    public Adapter_naptien(List<HashMap<String, Object>> list, Context context) {
        this.list_naptien = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(((Activity) context).getLayoutInflater().
                inflate(R.layout.item_lichsugg, parent, false));
    }

    @SuppressLint({"RecyclerView", "ResourceAsColor"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String Xanh = "#44cc00";
        String Cam = "#FFC107";
        holder.ma.setText(list_naptien.get(position).get("maGG").toString());
        holder.sotien.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(list_naptien.get(position).get("sotien"))+" VND");
        holder.ngay.setText(list_naptien.get(position).get("time").toString());
        if (Long.parseLong(list_naptien.get(position).get("trangThai").toString())==0){
            holder.trangthai.setText("Đang chờ");
            holder.trangthai.setTextColor(Color.parseColor(Cam));
        }else {
            holder.trangthai.setText("Đã duyêt");
            holder.trangthai.setTextColor(Color.parseColor(Xanh));
        }
        Log.e("TAG", "onBindViewHolder: "+ list_naptien);
    }

    @Override
    public int getItemCount() {
        return list_naptien.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ma, sotien, ngay,trangthai;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ma = itemView.findViewById(R.id.tv_maGG);
            sotien = itemView.findViewById(R.id.tv_sotien_gg);
            ngay = itemView.findViewById(R.id.tv_thoigiangg);
            trangthai = itemView.findViewById(R.id.tv_trangThai_gg);
        }
    }
}
