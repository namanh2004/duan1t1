package com.example.duan1t1.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.duan1t1.R;
import com.example.duan1t1.ThongTinTaiKhoan;
import com.example.duan1t1.fragment.Fragment_gioHang;
import com.example.duan1t1.model.Don;
import com.example.duan1t1.model.DonHang;
import com.example.duan1t1.model.GioHang;
import com.example.duan1t1.model.Hang;
import com.example.duan1t1.model.SanPham;
import com.example.duan1t1.model.ThongBao;
import com.example.duan1t1.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class Adapter_giohang extends RecyclerView.Adapter<Adapter_giohang.ViewHolder> {
    List<GioHang> list_gio;
    List<SanPham> list_sanPham;
    List<Hang> list_hang;
    Context context;
    Fragment_gioHang gioHang;
    FirebaseFirestore db;
    FirebaseUser user;
    SanPham sp;

    public Adapter_giohang(List<GioHang> list_gio, List<SanPham> list_sanPham, List<Hang> list_hang, Context context, Fragment_gioHang gioHang) {
        this.list_gio = list_gio;
        this.list_sanPham = list_sanPham;
        this.list_hang = list_hang;
        this.context = context;
        this.gioHang = gioHang;
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(((Activity) context).getLayoutInflater().
                inflate(R.layout.item_giohang, parent, false));
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String link = laylink(list_gio.get(position).getMaSanPham());
        gioHang.tinhTong();
        if (link.isEmpty()) {
            return;
        }
        Glide.with(context).load(link).
                error(R.drawable.baseline_crop_original_24).into(holder.anh);
        sp = getSanPham(list_gio.get(position).getMaSanPham());
        if (sp == null) {
            return;
        }
        holder.tenSP.setText(sp.getTenSP());
        holder.giaSP.setText("Giá: " + NumberFormat.getNumberInstance(Locale.getDefault()).format(sp.getGia()) + " VND");
        String tenHang = getTenLoai(sp.getMaHang());
        if (tenHang == null) {
            return;
        }
        holder.loaiSP.setText("Loại: " + tenHang + "");
        holder.soLuong.setText("Số lượng: " + list_gio.get(position).getSoLuong() + "");
        holder.kichCo.setText("Kích cỡ: " + list_gio.get(position).getKichCo() + "");


        holder.xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.baseline_question_mark_24);
                builder.setTitle("Cảnh báo !");
                builder.setIcon(R.drawable.warning);
                builder.setMessage("Bạn có muốn xóa sản phẩm này khỏi giỏ hàng không ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        xoa(list_gio.get(position).getMaGio());
                        dialog.dismiss();
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

        holder.mua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diaLogDatHang(position);
            }
        });

    }

    private void diaLogDatHang(int p) {
        final boolean[] check = {false};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.baseline_question_mark_24);
        builder.setTitle("Yêu cầu xác thực");
        builder.setMessage("Bạn có muốn xác nhận đơn hàng ?");

        builder.setNegativeButton("Đặt hàng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getThongTin(p);
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }

    public User getThongTin(int p) {
        final User[] user1 = new User[1];
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (!task.isComplete()) {
                    return;
                }
                user1[0] = task.getResult().toObject(User.class);
                if (user1[0] == null) {
                    Toast.makeText(context, "Lỗi xảy ra", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (user1[0].getHoTen() == null || user1[0].getSDT() == null) {
                    Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin để đặt hàng ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, ThongTinTaiKhoan.class);
                    ((Activity) context).startActivity(intent);
                    return;
                }
                if (user1[0].getChonDiaCHi() == null || user1[0].getChonDiaCHi().isEmpty()) {
                    Toast.makeText(context, "Vui lòng chọn địa chỉ để tiếp tục", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, ThongTinTaiKhoan.class);
                    ((Activity) context).startActivity(intent);
                    return;
                }
                if (user1[0].getSoDu() >= sp.getGia()) {
                    them(p);
                } else {
                    Toast.makeText(context, "Số dư tài khoản của bạn không đủ", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        return user1[0];
    }

    private void them(int p) {
        List<Don> listDon = new ArrayList<>();
        listDon.add(new Don(list_gio.get(p).getMaSanPham(), list_gio.get(p).getSoLuong()));
        String maDon = UUID.randomUUID().toString();
        Calendar lich = Calendar.getInstance();
        int ngay = lich.get(Calendar.DAY_OF_MONTH);
        int thang = lich.get(Calendar.MONTH) + 1;
        int nam = lich.get(Calendar.YEAR);
        String ngayMua = String.format("%02d/%02d/%02d",nam,thang,ngay);
        db.collection("donHang").document(maDon).set(new DonHang(maDon, user.getUid(), listDon, new Date().getTime(), 0, TongGiaSP(p), ngayMua))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                            Toast.makeText(context, "Đơn hàng đang chờ nhân viên xác nhận", Toast.LENGTH_SHORT).show();
                            xoa(list_gio.get(p).getMaGio());
                        } else {
                            Toast.makeText(context, "Lỗi", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private Long TongGiaSP(int p) {
        for (SanPham g : list_sanPham) {
            if (list_gio.get(p).getMaSanPham().equals(g.getMaSp())) {
                return (list_gio.get(p).getSoLuong() * g.getGia());
            }
        }
        return 0l;
    }

    private void xoa(String maGio) {
        db.collection("gioHang").document(maGio).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    Toast.makeText(context, "Thành công", Toast.LENGTH_SHORT).show();
                    guiThongBao();
                } else {
                    Toast.makeText(context, "Lỗi", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void guiThongBao() {
        String id = UUID.randomUUID().toString();
        db.collection("thongBao").document(id).set(new ThongBao(id, user.getUid(), "Có đơn hàng mới của " + user.getUid(), 2, new Date().getTime())).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                }
            }
        });
    }

    private String getTenLoai(String maHang) {
        for (Hang s : list_hang) {
            if (maHang.equals(s.getMaHang())) {
                return s.getTenHang();
            }
        }
        return null;
    }

    private SanPham getSanPham(String maSP) {
        for (SanPham s : list_sanPham) {
            if (maSP.equals(s.getMaSp())) {
                return s;
            }
        }
        return null;
    }

    private String laylink(String maSP) {
        String link = "";
        for (SanPham s : list_sanPham) {
            if (maSP.equals(s.getMaSp())) {
                link = s.getAnh();
                return link;
            }
        }
        return link;
    }

    @Override
    public int getItemCount() {
        return list_gio.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView anh;
        TextView tenSP, loaiSP, kichCo, soLuong, giaSP, mua, xoa;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            anh = itemView.findViewById(R.id.imv_anh_sp_gioHang);
            tenSP = itemView.findViewById(R.id.tv_tensp_gioHang);
            giaSP = itemView.findViewById(R.id.tv_giasp_giohang);
            loaiSP = itemView.findViewById(R.id.tv_thuonghieu_gioHang);
            kichCo = itemView.findViewById(R.id.tv_kichcosp_giohang);
            soLuong = itemView.findViewById(R.id.tv_soluongsp_giohang);
            mua = itemView.findViewById(R.id.tv_mua_giohang);
            xoa = itemView.findViewById(R.id.tv_xoa_giohang);
        }
    }
}
