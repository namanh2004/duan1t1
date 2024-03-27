package com.example.duan1t1.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.duan1t1.R;
import com.example.duan1t1.ShowMore;
import com.example.duan1t1.model.Hang;
import com.example.duan1t1.model.SanPham;

import java.util.ArrayList;
import java.util.List;

public class Adapter_cuahang extends RecyclerView.Adapter<Adapter_cuahang.ViewHolder> implements Filterable {
    List<Hang> list;
    Context context;
    Adapter_itemCuaHang itemCuaHang;


    List<Hang> list_hangSearch;

    public Adapter_cuahang(List<Hang> list, Context context) {
        this.list = list;
        this.context = context;
        this.list_hangSearch = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(((Activity) context).getLayoutInflater().inflate(R.layout.item_cuahang, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (list.get(position).getSanPham().size() == 0) {
            return;
        }


        holder.tenHang.setText(list.get(position).getTenHang());
        itemCuaHang = new Adapter_itemCuaHang(list.get(position).getSanPham(), context);
        holder.rcv_list.setAdapter(itemCuaHang);
        LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.rcv_list.setLayoutManager(manager);
        holder.xemthem.setText("Xem thêm");
        holder.xemthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowMore.class);
                List<SanPham> phamList = list.get(position).getSanPham();
                String[] s = new String[]{list.get(position).getMaHang(), list.get(position).getTenHang()};
                intent.putExtra("list", s);
                ((Activity) context).startActivity(intent);
            }
        });
    }

    private List<SanPham> getList_sp(Hang hang) {
        List<SanPham> list1 = new ArrayList<>();
        for (SanPham sanPham : hang.getSanPham()) {
            list1.add(sanPham);
        }
        return list1;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint.toString().isEmpty()) {
                    list = list_hangSearch;
                } else {
                    List<Hang> list1_hangMoi = new ArrayList<>();
                    for (Hang hang : list_hangSearch) {
                        if (hang.getTenHang().toLowerCase().trim().contains(constraint.toString().toLowerCase().trim())) {
                            list1_hangMoi.add(hang);
                            Log.e("TAG","con"+constraint.toString().toLowerCase().trim());
                            Log.e("TAG","hang"+hang.getTenHang());
                        }

                    }
                    list = list1_hangMoi;
                }
                FilterResults results = new FilterResults();
                results.values = list;

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (List<Hang>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tenHang;
        TextView xemthem;
        RecyclerView rcv_list;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tenHang = itemView.findViewById(R.id.tv_tenhang);
            xemthem = itemView.findViewById(R.id.ll_xemthem_moi);
            rcv_list = itemView.findViewById(R.id.rcv_list_sp_khach);
        }
    }
}
