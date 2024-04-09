package com.example.duan1t1.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.duan1t1.R;
import com.example.duan1t1.adapter.Adapter_quanlyhoadon;
import com.example.duan1t1.model.DonHang;
import com.example.duan1t1.model.GioHang;
import com.example.duan1t1.model.SanPham;
import com.example.duan1t1.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class Frg_quanLyHoaDon extends Fragment {

    RecyclerView recyclerView;
    Adapter_quanlyhoadon adapterQuanlyhoadon;
    List<GioHang> list_gioHang;
    List<SanPham> list_sp;
    List<DonHang> list_dh;

    List<User> list_User;
    FirebaseFirestore db;

    public Frg_quanLyHoaDon() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frg_quan_ly_hoa_don, container, false);
        recyclerView = view.findViewById(R.id.rcv_quanLyHoaDon);
        loatData();
        return view;
    }

    public void loatData() {
        list_User = new ArrayList<>();
        list_sp = new ArrayList<>();
        list_gioHang = new ArrayList<>();
        list_dh = new ArrayList<>();
        getAll();
        adapterQuanlyhoadon = new Adapter_quanlyhoadon(list_sp, list_User, list_dh, getContext());
        recyclerView.setAdapter(adapterQuanlyhoadon);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

    }


    public void getAnh() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("sanPham").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isComplete()) {
                    for (QueryDocumentSnapshot snapshot: task.getResult()){
                        list_sp.add(snapshot.toObject(SanPham.class));
                    }
                    adapterQuanlyhoadon.notifyDataSetChanged();
                }
            }
        });
    }

    private void getAll() {
        db = FirebaseFirestore.getInstance();
        getSP();
        getKH();
        getHoaDon();
        getAnh();

    }

    public void getHoaDon() {

        db.collection("donHang").whereEqualTo("trangThai", 0).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                                dc.getDocument().toObject(DonHang.class);
                                list_dh.add(dc.getDocument().toObject(DonHang.class));
                                adapterQuanlyhoadon.notifyDataSetChanged();
                                break;
                            case MODIFIED:
                                DonHang dtoq = dc.getDocument().toObject(DonHang.class);
                                if (dc.getOldIndex() == dc.getNewIndex()) {
                                    list_dh.set(dc.getOldIndex(), dtoq);
                                } else {
                                    list_dh.remove(dc.getOldIndex());
                                    list_dh.add(dtoq);
                                }
                                adapterQuanlyhoadon.notifyDataSetChanged();
                                break;
                            case REMOVED:
                                dc.getDocument().toObject(DonHang.class);
                                list_dh.remove(dc.getOldIndex());
                                adapterQuanlyhoadon.notifyDataSetChanged();
                                break;
                        }
                    }
                }
            }
        });
    }

    public void getKH() {
        db.collection("user").whereEqualTo("chucVu", 3).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }
                if (value != null) {
                    for (DocumentChange dc : value.getDocumentChanges()) {
                        Log.e("TAG", "onEvent: " + dc.getType());
                        switch (dc.getType()) {
                            case ADDED:
                                dc.getDocument().toObject(User.class);
                                list_User.add(dc.getDocument().toObject(User.class));
                                break;
                            case MODIFIED:
                                User user1 = dc.getDocument().toObject(User.class);
                                if (dc.getOldIndex() == dc.getNewIndex()) {
                                    list_User.set(dc.getOldIndex(), user1);
                                } else {
                                    list_User.remove(dc.getOldIndex());
                                    list_User.add(user1);
                                }
                                break;
                            case REMOVED:
                                dc.getDocument().toObject(User.class);
                                list_User.remove(dc.getOldIndex());
                        }
                    }
                }
            }
        });
    }


    public void getSP() {
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
                                list_sp.add(dc.getDocument().toObject(SanPham.class));
                                break;
                            case MODIFIED:
                                SanPham dtoq = dc.getDocument().toObject(SanPham.class);
                                if (dc.getOldIndex() == dc.getNewIndex()) {
                                    list_sp.set(dc.getOldIndex(), dtoq);
                                } else {
                                    list_sp.remove(dc.getOldIndex());
                                    list_sp.add(dtoq);
                                }
                                break;
                            case REMOVED:
                                dc.getDocument().toObject(SanPham.class);
                                list_sp.remove(dc.getOldIndex());
                                break;
                        }
                    }
                }
            }
        });
    }


}
//        onCreateView(): Phương thức này inflates layout cho Fragment từ tập tin XML và trả về một View. Trong trường hợp này, layout được sử dụng là fragment_frg_quan_ly_hoa_don.xml, chứa một RecyclerView để hiển thị danh sách đơn hàng.
//
//        loatData(): Phương thức này khởi tạo và load dữ liệu cho Fragment. Cụ thể:
//
//        Tạo mới các danh sách để lưu trữ dữ liệu đơn hàng (list_dh), sản phẩm (list_sp), và người dùng (list_User).
//        Gọi các phương thức getAll(), getSP(), getKH(), và getHoaDon() để lấy dữ liệu từ Firestore và hiển thị lên RecyclerView.
//        getAll(): Phương thức này gọi các phương thức getSP(), getKH(), và getHoaDon() để lấy dữ liệu sản phẩm, khách hàng, và đơn hàng từ Firestore.
//
//        getHoaDon(): Phương thức này lắng nghe sự thay đổi trong collection "donHang" trên Firestore để lấy danh sách các đơn hàng. Khi có sự thay đổi (thêm, sửa, xoá), phương thức này được gọi và xử lý dữ liệu dựa trên các loại sự kiện.
//
//        getKH(): Phương thức này lắng nghe sự thay đổi trong collection "user" trên Firestore để lấy danh sách khách hàng. Tương tự như getHoaDon(), khi có sự thay đổi, phương thức này được gọi và xử lý dữ liệu tương tự.
//
//        getSP(): Phương thức này lắng nghe sự thay đổi trong collection "sanPham" trên Firestore để lấy danh sách sản phẩm. Tương tự như getHoaDon(), khi có sự thay đổi, phương thức này được gọi và xử lý dữ liệu tương tự.
//
//        getAnh(): Phương thức này lấy danh sách sản phẩm từ Firestore và thêm vào danh sách list_sp.


