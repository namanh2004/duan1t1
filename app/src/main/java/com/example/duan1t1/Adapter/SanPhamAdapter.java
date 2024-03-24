package com.example.duan1t1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1t1.DAO.DaoSanPham;
import com.example.duan1t1.EventBus.IClickItemCTSPKH;
import com.example.duan1t1.EventBus.IClickItemRCV;
import com.example.duan1t1.Model.SanPham;
import com.example.duan1t1.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.ViewHolder>{
    private Context context;
    private List<SanPham> lstSP;


    IClickItemRCV clickItemRCV;
    private IClickItemCTSPKH itemClick;
    public SanPhamAdapter(Context context, List<SanPham> lstSP,IClickItemRCV itemRCV) {
        this.context = context;
        this.lstSP = lstSP;
        this.clickItemRCV = itemRCV;
    }

    public void setItemClick(IClickItemCTSPKH itemClick) {
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sanpham,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,final int position) {
        DaoSanPham daoSP = new DaoSanPham(context);
        SanPham sanPham = lstSP.get(position);


        SanPham sp = daoSP.getID(String.valueOf(sanPham.getMaSP()));

        holder.sp_maSP.setText("Mã Sản phẩm:SP " + sanPham.getMaSP());
        holder.sp_tenHang.setText("Tên hàng: " + sanPham.getTenHang());
        holder.sp_tensanpham.setText("Tên sản phẩm: " + sanPham.getTenSP());
        holder.sp_giatien.setText("Giá tiền:$"+ sanPham.getGiaTien());

        Picasso.get().load(sanPham.getImages()).into(holder.images);

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickItemRCV.iclickItem(holder, sanPham.getMaSP(), 1);
            }
        });


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                clickItemRCV.iclickItem(holder, holder.getAdapterPosition(), 0);
                return false;
            }
        });



    }

    @Override
    public int getItemCount() {
        return lstSP != null ? lstSP.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sp_maSP,sp_tenHang,sp_tensanpham,sp_giatien;
        ImageButton btn_delete;
        ImageView images;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sp_maSP = itemView.findViewById(R.id.sp_maSP);
            sp_tenHang = itemView.findViewById(R.id.sp_tenHang);
            sp_tensanpham = itemView.findViewById(R.id.sp_tensanpham);
            sp_giatien = itemView.findViewById(R.id.sp_giatien);
            btn_delete = itemView.findViewById(R.id.btn_delete_spAdmin);

            images = itemView.findViewById(R.id.images_sp);
        }
    }

}
