package com.example.duan1t1.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.duan1t1.R;
import com.example.duan1t1.ThongTinTaiKhoan;
import com.example.duan1t1.adapter.Adapter_giohang;
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
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


public class Fragment_gioHang extends Fragment {
    RecyclerView rcv_list;
    TextView tongGia;
    LinearLayout mua;
    Adapter_giohang adapterGiohang;
    List<GioHang> list_gio;
    List<SanPham> list_sanPham;
    ArrayList<User> list_User;
    List<Hang> list_hang;
    FirebaseFirestore db;
    List<Don> listMaSP;
    FirebaseUser user;
    String TAG = "TAG";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gio_hang, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        anhXa(view);
    }

    private void anhXa(View view) {
        list_gio = new ArrayList<>();
        list_hang = new ArrayList<>();
        list_sanPham = new ArrayList<>();
        getData();
        rcv_list = view.findViewById(R.id.rcv_listgio);
        tongGia = view.findViewById(R.id.tv_gio_gia);
        mua = view.findViewById(R.id.ll_themgio);
        user = FirebaseAuth.getInstance().getCurrentUser();
        adapterGiohang = new Adapter_giohang(list_gio, list_sanPham, list_hang, getContext(), this);
        rcv_list.setAdapter(adapterGiohang);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcv_list.setLayoutManager(manager);
        tinhTong();

        mua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diaLogDatHang();
            }
        });

    }
    public User getThongTin() {
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
                    Toast.makeText(getContext(), "Lỗi xảy ra", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (user1[0].getHoTen() == null || user1[0].getSDT() == null) {
                    Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin để đặt hàng ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), ThongTinTaiKhoan.class);
                    startActivity(intent);
                    return;
                }
                if (user1[0].getChonDiaCHi() == null || user1[0].getChonDiaCHi().isEmpty()) {
                    Intent intent = new Intent(getContext(), ThongTinTaiKhoan.class);
                    startActivity(intent);
                    return;
                }
                if (user1[0].getSoDu() >= tinhTong()) {
                    mua();
                } else {

                    Toast.makeText(getContext(), "Số dư tài khoản của bạn không đủ", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        return user1[0];
    }

    private void diaLogDatHang() {
        final boolean[] check = {false};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setIcon(R.drawable.baseline_question_mark_24);
        builder.setTitle("Yêu cầu xác thực");
        builder.setMessage("Bạn có muốn xác nhận đơn hàng ?");

        builder.setNegativeButton("Đặt hàng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getThongTin();
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

    private void mua() {
        List<String> listMaGio = getListMa();
        if (listMaGio.size() <= 0) {
            Toast.makeText(getContext(), "Vui lòng thêm sản phẩm vào giỏ", Toast.LENGTH_SHORT).show();
            return;
        }
        String maDon = UUID.randomUUID().toString();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Calendar lich = Calendar.getInstance();
        int ngay = lich.get(Calendar.DAY_OF_MONTH);
        int thang = lich.get(Calendar.MONTH) + 1;
        int nam = lich.get(Calendar.YEAR);
        String ngayMua =String.format("%02d/%02d/%02d",nam,thang,ngay);
        db.collection("donHang").document(maDon).set(new DonHang(maDon, user.getUid(), listMaSP, new Date().getTime(), 0, tinhTong(), ngayMua))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                            Toast.makeText(getContext(), "Đơn hàng đang chờ nhân viên xác nhận", Toast.LENGTH_SHORT).show();
                            guiThongBao();
                        } else {
                            Toast.makeText(getContext(), "Lỗi", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        for (String s : listMaGio) {
            db.collection("gioHang").document(s).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isComplete()) {
                        tongGia.setText("0 VND");
                        adapterGiohang.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        Toast.makeText(getContext(), "Thành công", Toast.LENGTH_SHORT).show();

    }

    private void guiThongBao() {
        String id = UUID.randomUUID().toString();
        db.collection("thongBao").document(id).set(new ThongBao(id, user.getUid(), "Có đơn hàng mới của " + user.getUid(), 2, new Date().getTime())).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    Log.e(TAG, "onComplete: " + "Thành công");
                }
            }
        });
    }


    private List<String> getListMa() {
        List<String> listGio = new ArrayList<>();
        listMaSP = new ArrayList<>();
        for (GioHang gh : list_gio) {
            listGio.add(gh.getMaGio());
            listMaSP.add(new Don(gh.getMaSanPham(), gh.getSoLuong()));
        }
        return listGio;
    }


    private void getData() {
        db = FirebaseFirestore.getInstance();
        getGio();
        getSP();
        getHang();

    }

    private void getHang() {
        db.collection("hang").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(getContext(), "Lỗi không có dữ liệu", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {
                    for (DocumentChange dc : value.getDocumentChanges()) {
                        switch (dc.getType()) {
                            case ADDED:
                                dc.getDocument().toObject(Hang.class);
                                list_hang.add(dc.getDocument().toObject(Hang.class));
                                adapterGiohang.notifyDataSetChanged();
                                break;
                            case MODIFIED:
                                Hang dtoq = dc.getDocument().toObject(Hang.class);
                                if (dc.getOldIndex() == dc.getNewIndex()) {
                                    list_hang.set(dc.getOldIndex(), dtoq);
                                } else {
                                    list_hang.remove(dc.getOldIndex());
                                    list_hang.add(dtoq);
                                }
                                adapterGiohang.notifyDataSetChanged();
                                break;
                            case REMOVED:
                                dc.getDocument().toObject(Hang.class);
                                list_hang.remove(dc.getOldIndex());
                                adapterGiohang.notifyDataSetChanged();
                                break;
                        }
                    }
                }
            }
        });
    }

    private void getSP() {
        db.collection("sanPham").orderBy("time").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(getContext(), "Lỗi không có dữ liệu", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {
                    for (DocumentChange dc : value.getDocumentChanges()) {
                        switch (dc.getType()) {
                            case ADDED:
                                dc.getDocument().toObject(SanPham.class);
                                list_sanPham.add(dc.getDocument().toObject(SanPham.class));
                                adapterGiohang.notifyDataSetChanged();
                                break;
                            case MODIFIED:
                                SanPham dtoq = dc.getDocument().toObject(SanPham.class);
                                if (dc.getOldIndex() == dc.getNewIndex()) {
                                    list_sanPham.set(dc.getOldIndex(), dtoq);
                                } else {
                                    list_sanPham.remove(dc.getOldIndex());
                                    list_sanPham.add(dtoq);
                                }
                                adapterGiohang.notifyDataSetChanged();
                                break;
                            case REMOVED:
                                dc.getDocument().toObject(SanPham.class);
                                list_sanPham.remove(dc.getOldIndex());
                                adapterGiohang.notifyDataSetChanged();
                                break;
                        }
                    }
                }
            }
        });
    }

    private void getGio() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("gioHang").whereEqualTo("maKhachHang", user.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(getContext(), "Lỗi không có dữ liệu", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {
                    for (DocumentChange dc : value.getDocumentChanges()) {
                        switch (dc.getType()) {
                            case ADDED:
                                list_gio.add(dc.getDocument().toObject(GioHang.class));
                                adapterGiohang.notifyDataSetChanged();
                                break;
                            case MODIFIED:
                                GioHang dtoq = dc.getDocument().toObject(GioHang.class);
                                if (dc.getOldIndex() == dc.getNewIndex()) {
                                    list_gio.set(dc.getOldIndex(), dtoq);
                                } else {
                                    list_gio.remove(dc.getOldIndex());
                                    list_gio.add(dtoq);
                                }
                                adapterGiohang.notifyDataSetChanged();
                                break;
                            case REMOVED:
                                dc.getDocument().toObject(GioHang.class);
                                list_gio.remove(dc.getOldIndex());
                                adapterGiohang.notifyDataSetChanged();
                                break;
                        }
                    }
                }
            }
        });
    }

    public Long tinhTong() {
        Long tong = 0l;
        for (GioHang s : list_gio) {
            for (SanPham a : list_sanPham) {
                if (s.getMaSanPham().equals(a.getMaSp())) {
                    tong = Long.parseLong(s.getSoLuong() + "") * Long.parseLong(a.getGia() + "") + tong;
                }
            }
        }
        tongGia.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(tong) + " VND");
        return tong;
    }
}