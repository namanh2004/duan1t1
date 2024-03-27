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
    List<SanPham> list;
    List<Don> list_don;
    Context context;



    public Adapter_chiTietHangMua(List<SanPham> list, List<Don> list_don, Context context) {
        this.list = list;
        this.list_don = list_don;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_list_sanpham_mua,parent,false);
        TextView Ma = view.findViewById(R.id.tv_Masp_ct);
        TextView Ten = view.findViewById(R.id.tv_Tensp_ct);
        TextView Gia = view.findViewById(R.id.tv_Gia_ct);
        TextView SoLuong = view.findViewById(R.id.tv_Soluong_ct);
        TextView SoLuong_kho = view.findViewById(R.id.tv_SoluongTrongKho_ct);
        ImageView Anh = view.findViewById(R.id.imv_anh_sp_ct);

        Glide.with(context).load(list.get(position)
                .getAnh()).error(R.drawable.baseline_crop_original_24).into(Anh);
        Ma.setText("Mã sản phẩm: "+list.get(position).getMaSp());
        Ten.setText("Tên: "+list.get(position).getTenSP());
        Gia.setText("Giá: "+ NumberFormat.getNumberInstance(Locale.getDefault()).format(list.get(position).getGia())+" VND");
        SoLuong.setText("Số Lượng mua: "+list_don.get(position).getSoLuong());
        SoLuong_kho.setText("Số Lượng trong kho: "+list.get(position).getSoLuong());

        return view;
    }
}
