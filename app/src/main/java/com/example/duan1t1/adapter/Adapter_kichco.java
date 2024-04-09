package com.example.duan1t1.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.duan1t1.R;
import com.example.duan1t1.SeeSanPham;

import java.util.List;

public class Adapter_kichco extends RecyclerView.Adapter<Adapter_kichco.ViewHolder> {
    List<String> list;
    Context context;
    int i = -1; // Biến i dùng để lưu vị trí của mục đã chọn

    public Adapter_kichco(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Tạo ViewHolder mới cho mỗi item trong RecyclerView
        return new ViewHolder(((Activity) context).getLayoutInflater().inflate(R.layout.item_kichco, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // Gán dữ liệu vào ViewHolder
        holder.ten.setText(list.get(position));
        String cam = "#FF4800"; // Màu cam
        String vang = "#FFC107"; // Màu vàng
        // Nếu position trùng với vị trí đã chọn (i), đổi màu nền thành màu cam
        if (position == i) {
            SeeSanPham sanPham = (SeeSanPham) context;
            sanPham.setKichCo(list.get(i)); // Cập nhật kích cỡ đã chọn
            holder.mau.setBackgroundColor(Color.parseColor(cam));
        } else {
            // Nếu không, đổi màu nền thành màu vàng
            holder.mau.setBackgroundColor(Color.parseColor(vang));
        }

        // Xử lý sự kiện khi nhấn vào một item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = position; // Lưu vị trí item đã chọn
                notifyDataSetChanged(); // Cập nhật lại RecyclerView
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // ViewHolder cho mỗi item trong RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ten;
        LinearLayout mau; // Layout để đổi màu nền

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ten = itemView.findViewById(R.id.tv_kichco_show1); // TextView hiển thị kích cỡ
            mau = itemView.findViewById(R.id.cv_kichco); // Layout để đổi màu nền
        }
    }
}
