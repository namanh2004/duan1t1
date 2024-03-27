package com.example.duan1t1.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.duan1t1.R;
import com.example.duan1t1.SeeSanPham;
import com.example.duan1t1.model.SanPham;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Adapter_itemCuaHang extends RecyclerView.Adapter<Adapter_itemCuaHang.ViewHolder> {
    List<SanPham> list;
    List<SanPham> list_search;
    Context context;

    public Adapter_itemCuaHang(List<SanPham> list, Context context) {
        this.list = list;
        this.context = context;
        this.list_search = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(((Activity) context).getLayoutInflater()
                .inflate(R.layout.item_cuahang_sanpham, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(list.get(position).getAnh()).
                error(R.drawable.baseline_crop_original_24).into(holder.anh);
        holder.ten.setText(list.get(position).getTenSP());
        holder.gia.setText("Giá: " + NumberFormat.getNumberInstance(Locale.getDefault()).format(list.get(position).getGia()) +" VND");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SeeSanPham.class);
                intent.putExtra("sanpham",list.get(position).getMaSp());
                ((Activity)context).startActivity(intent);
            }
        });
    }

    public Filter getFilter(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint.toString().isEmpty()) {
                    list = list_search;
                } else {
                    List<SanPham> list1_spMoi = new ArrayList<>();
                    for (SanPham sp : list_search) {
                        if (sp.getTenSP().toLowerCase().trim().contains(constraint.toString().toLowerCase().trim())) {
                            list1_spMoi.add(sp);
                        }

                    }
                    list = list1_spMoi;
                }
                FilterResults results = new FilterResults();
                results.values = list;

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (List<SanPham>) results.values;
                notifyDataSetChanged();
            }
        };
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView anh;
        TextView ten, gia;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            anh = itemView.findViewById(R.id.imv_anh_sp_cuahang);
            ten = itemView.findViewById(R.id.tv_tensp_cuahang);
            gia = itemView.findViewById(R.id.tv_giasp_cuahang);
        }
    }
}
