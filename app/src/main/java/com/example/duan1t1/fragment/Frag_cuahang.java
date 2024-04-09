package com.example.duan1t1.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.duan1t1.ManHinhKhachHang;
import com.example.duan1t1.R;
import com.example.duan1t1.adapter.Adapter_cuahang;
import com.example.duan1t1.model.Hang;
import com.example.duan1t1.model.SanPham;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Frag_cuahang extends Fragment {
    private RecyclerView rcv_list;
    private Adapter_cuahang adapterCuahang;
    private FirebaseFirestore db;
    List<Hang> list_hang;
    List<SanPham> list_sp;
    String TAG = "TAG";
    ManHinhKhachHang manHinhKhachHang;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return getActivity().getLayoutInflater().inflate(R.layout.fragment_cuahang, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        anhXa(view);
    }

    private void anhXa(View view) {
        rcv_list = view.findViewById(R.id.rcv_cuaHang);
        SearchView searchView = view.findViewById(R.id.seach_Hang_KH);
        list_hang = new ArrayList<>();
        list_sp = new ArrayList<>();
        manHinhKhachHang = (ManHinhKhachHang) getActivity();
        nghe();
        adapterCuahang = new Adapter_cuahang(list_hang, getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcv_list.setAdapter(adapterCuahang);
        rcv_list.setLayoutManager(manager);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterCuahang.getFilter().filter(newText);
                adapterCuahang.notifyDataSetChanged();
                return true;
            }
        });
        Log.e("TAG", "List hang" + list_hang);
    }


    private void nghe() {
        db = FirebaseFirestore.getInstance();
        db.collection("hang").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }
                if (value == null) {
                    return;
                }
                list_sp.clear();
                list_hang.clear();
                for (DocumentSnapshot dc : value.getDocuments()) {
                    AddListSP(dc.getString("maHang"), dc.getString("tenHang"));
                }
            }
        });
    }

    private void AddListSP(String maHang, String name) {
        db.collection("sanPham").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }
                if (value == null) {
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (maHang.equals(dc.getDocument().get("maHang").toString())) {
                        list_sp.add(dc.getDocument().toObject(SanPham.class));
                    }
                    switch (dc.getType()) {
                        case MODIFIED:
//                            manHinhKhachHang.getSupportFragmentManager().beginTransaction().replace(R.id.fcv_KhachHang, new Frag_cuahang()).commit();
                            return;
                    }
                }
                if (list_sp.size()==0){
                    return;
                }
                list_hang.add(new Hang(maHang, name, list_sp));
                list_hang.sort(new Comparator<Hang>() {
                    @Override
                    public int compare(Hang o1, Hang o2) {
                        return (o2.getSanPham().size() - o1.getSanPham().size());
                    }
                });
                adapterCuahang.notifyDataSetChanged();
                list_sp = new ArrayList<>();

            }
        });

    }

}
//        onCreateView(): Phương thức này inflates layout cho Fragment từ tập tin XML và trả về một View. Trong trường hợp này, layout được sử dụng là fragment_cuahang.xml.
//
//        onViewCreated(): Phương thức này được gọi sau khi onCreateView() đã hoàn thành, cho phép thực hiện các thao tác khác liên quan đến giao diện người dùng.
//
//        anhXa(View view): Phương thức này được sử dụng để ánh xạ và cấu hình các thành phần giao diện người dùng như RecyclerView và SearchView. Nó cũng khởi tạo một Adapter_cuahang và thiết lập nó cho RecyclerView.
//
//        nghe(): Phương thức này sử dụng để lắng nghe sự thay đổi trong tập dữ liệu từ cơ sở dữ liệu Firebase. Nó sử dụng addSnapshotListener để theo dõi các thay đổi trong collection "hang" và "sanPham". Khi có sự thay đổi, nó gọi phương thức AddListSP() để thêm danh sách sản phẩm vào danh sách hàng và cập nhật RecyclerView.
//
//        AddListSP(String maHang, String name): Phương thức này thực hiện việc thêm danh sách sản phẩm vào danh sách hàng dựa trên mã hàng và tên hàng. Nó lắng nghe sự thay đổi trong collection "sanPham" và thêm các sản phẩm tương ứng vào danh sách sản phẩm của hàng. Sau đó, danh sách hàng được sắp xếp theo số lượng sản phẩm và RecyclerView được cập nhật.
