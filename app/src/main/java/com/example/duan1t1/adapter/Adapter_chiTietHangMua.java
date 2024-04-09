package com.example.duan1t1.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.example.duan1t1.R;
import com.example.duan1t1.model.Don;
import com.example.duan1t1.model.SanPham;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class Adapter_chiTietHangMua extends BaseAdapter {
    List<SanPham> list;  // Danh sách các sản phẩm (SanPham)
    List<Don> list_don;  // Danh sách các đơn hàng (Don)
    Context context;      // Ngữ cảnh của ứng dụng

    // Constructor cho Adapter
    public Adapter_chiTietHangMua(List<SanPham> list, List<Don> list_don, Context context) {
        this.list = list;
        this.list_don = list_don;
        this.context = context;
    }

    // Phương thức để trả về số lượng mục trong danh sách
    @Override
    public int getCount() {
        return list.size();
    }

    // Phương thức để trả về mục tại vị trí được chỉ định
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    // Phương thức để trả về ID của một mục tại vị trí được chỉ định
    @Override
    public long getItemId(int position) {
        return 0;
    }

    // Phương thức để tạo một View cho mỗi mục trong Adapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_list_sanpham_mua,parent,false);  // Inflate layout cho mỗi mục trong danh sách
        TextView Ma = view.findViewById(R.id.tv_Masp_ct);   // Tìm TextView cho mã sản phẩm
        TextView Ten = view.findViewById(R.id.tv_Tensp_ct);  // Tìm TextView cho tên sản phẩm
        TextView Gia = view.findViewById(R.id.tv_Gia_ct);    // Tìm TextView cho giá sản phẩm
        TextView SoLuong = view.findViewById(R.id.tv_Soluong_ct);  // Tìm TextView cho số lượng đặt hàng
        TextView SoLuong_kho = view.findViewById(R.id.tv_SoluongTrongKho_ct);  // Tìm TextView cho số lượng trong kho
        ImageView Anh = view.findViewById(R.id.imv_anh_sp_ct);   // Tìm ImageView cho hình ảnh sản phẩm

        // Tải hình ảnh sản phẩm sử dụng thư viện Glide
        Glide.with(context).load(list.get(position).getAnh()).error(R.drawable.baseline_crop_original_24).into(Anh);
        // Thiết lập mã sản phẩm
        Ma.setText("Mã sản phẩm: "+list.get(position).getMaSp());
        // Thiết lập tên sản phẩm
        Ten.setText("Tên: "+list.get(position).getTenSP());
        // Thiết lập giá sản phẩm, định dạng nó để bao gồm dấu phân cách hàng nghìn
        Gia.setText("Giá: "+ NumberFormat.getNumberInstance(Locale.getDefault()).format(list.get(position).getGia())+" VND");
        // Thiết lập số lượng đặt hàng cho sản phẩm
        SoLuong.setText("Số Lượng mua: "+list_don.get(position).getSoLuong());
        // Thiết lập số lượng trong kho cho sản phẩm
        SoLuong_kho.setText("Số Lượng trong kho: "+list.get(position).getSoLuong());

        return view;  // Trả về view được điền dữ liệu để hiển thị trên màn hình
    }
}
