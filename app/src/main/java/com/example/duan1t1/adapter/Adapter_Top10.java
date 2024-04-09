package com.example.duan1t1.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.duan1t1.R;
import com.example.duan1t1.model.SanPham;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

public class Adapter_Top10 extends RecyclerView.Adapter<Adapter_Top10.viewHolder> {

    Context context;
    List<SanPham> list_sanPham;
    FirebaseFirestore db;
    List<HashMap<String, Object>> list_top10;

    public Adapter_Top10(Context context, List<SanPham> list_sanPham, List<HashMap<String, Object>> list_top10) {
        this.context = context;
        this.list_sanPham = list_sanPham;
        this.list_top10 = list_top10;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_top10sp, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        SanPham sanPham = maSP(list_top10.get(position).get("maSP").toString());
        Log.e("TAG",""+sanPham.getAnh());
        if (sanPham==null){
            return;
        }
        Log.e("TAG",""+sanPham.getTenSP());
        Glide.with(context).load(sanPham.getAnh()).error(R.drawable.baseline_crop_original_24).into(holder.anh);
        holder.tv_tenSp.setText("Tên sản phẩm: "+sanPham.getTenSP());
        holder.tv_thuonghieu.setText("Hãng: "+sanPham.getTenHang());
        holder.tv_soLuong.setText("Số lượng: "+list_top10.get(position).get("soLuong").toString());
    }

    public SanPham maSP(String masp) {
        SanPham sanPham = new SanPham();
        for (SanPham sp : list_sanPham) {
            if (masp.equals(sp.getMaSp())) {
                return sp;
            }

        }
        return sanPham;
    }


    @Override
    public int getItemCount() {
        return list_top10.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView anh;
        TextView tv_tenSp, tv_thuonghieu, tv_soLuong;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            anh = itemView.findViewById(R.id.imgv_anhtop10);
            tv_tenSp = itemView.findViewById(R.id.tv_TENSP);
            tv_thuonghieu = itemView.findViewById(R.id.tv_THUONGHIEU);
            tv_soLuong = itemView.findViewById(R.id.tv_SOLUONG);
        }
    }
}
//        Constructor: Adapter này nhận vào một Context, một danh sách các đối tượng SanPham (sản phẩm), và một danh sách HashMap chứa thông tin về top 10 sản phẩm. Constructor này được sử dụng để khởi tạo Adapter và các biến cần thiết.
//
//        onCreateViewHolder: Phương thức này tạo và trả về một ViewHolder mới cho mỗi item trong RecyclerView. Nó sử dụng LayoutInflater để inflate layout item_top10sp.
//
//        onBindViewHolder: Phương thức này được gọi khi RecyclerView yêu cầu một ViewHolder để hiển thị dữ liệu tại một vị trí cụ thể. Nó gán dữ liệu từ danh sách top 10 sản phẩm vào ViewHolder. Đầu tiên, nó lấy thông tin về sản phẩm từ danh sách SanPham dựa trên mã sản phẩm. Sau đó, nó sử dụng thư viện Glide để tải ảnh sản phẩm từ URL và gán vào ImageView trong ViewHolder. Cuối cùng, nó hiển thị tên sản phẩm, hãng và số lượng bán được của sản phẩm đó.
//
//        maSP: Phương thức này được sử dụng để tìm kiếm và trả về đối tượng SanPham từ danh sách dựa trên mã sản phẩm.
//
//        getItemCount: Phương thức này trả về số lượng item trong danh sách top 10 sản phẩm.
//
//        ViewHolder class: Lớp ViewHolder để giữ các thành phần giao diện cho mỗi item trong RecyclerView. Nó chứa các thành phần như ImageView cho ảnh sản phẩm và TextView cho tên sản phẩm, hãng và số lượng bán được.
