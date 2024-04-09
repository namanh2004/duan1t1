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
    List<HashMap<String,Object>> list_naptien; // Danh sách các bản ghi nạp tiền
    Context context; // Context của ứng dụng
    FirebaseFirestore db; // Firestore để tương tác với cơ sở dữ liệu

    // Constructor để khởi tạo Adapter_naptien với danh sách nạp tiền và context
    public Adapter_naptien(List<HashMap<String, Object>> list, Context context) {
        this.list_naptien = list;
        this.context = context;
    }

    // Phương thức tạo ViewHolder mới khi RecyclerView cần
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(((Activity) context).getLayoutInflater().
                inflate(R.layout.item_lichsugg, parent, false));
    }

    // Phương thức để gán dữ liệu vào ViewHolder khi RecyclerView yêu cầu
    @SuppressLint({"RecyclerView", "ResourceAsColor"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String Xanh = "#44cc00"; // Màu xanh
        String Cam = "#FFC107"; // Màu cam

        // Hiển thị thông tin của mỗi bản ghi nạp tiền vào ViewHolder
        holder.ma.setText(list_naptien.get(position).get("maGG").toString()); // Hiển thị mã giao dịch
        holder.sotien.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(list_naptien.get(position).get("sotien"))+" VND"); // Hiển thị số tiền
        holder.ngay.setText(list_naptien.get(position).get("time").toString()); // Hiển thị thời gian
        // Kiểm tra trạng thái của giao dịch và hiển thị tương ứng
        if (Long.parseLong(list_naptien.get(position).get("trangThai").toString()) == 0) {
            holder.trangthai.setText("Đang chờ"); // Hiển thị trạng thái là "Đang chờ"
            holder.trangthai.setTextColor(Color.parseColor(Cam)); // Đổi màu chữ sang màu cam
        } else {
            holder.trangthai.setText("Đã duyệt"); // Hiển thị trạng thái là "Đã duyệt"
            holder.trangthai.setTextColor(Color.parseColor(Xanh)); // Đổi màu chữ sang màu xanh
        }
        Log.e("TAG", "onBindViewHolder: "+ list_naptien);
    }

    // Phương thức trả về số lượng item trong danh sách nạp tiền
    @Override
    public int getItemCount() {
        return list_naptien.size();
    }

    // ViewHolder để lưu trữ view item
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ma, sotien, ngay, trangthai;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ma = itemView.findViewById(R.id.tv_maGG); // TextView hiển thị mã giao dịch
            sotien = itemView.findViewById(R.id.tv_sotien_gg); // TextView hiển thị số tiền
            ngay = itemView.findViewById(R.id.tv_thoigiangg); // TextView hiển thị thời gian
            trangthai = itemView.findViewById(R.id.tv_trangThai_gg); // TextView hiển thị trạng thái
        }
    }
}
