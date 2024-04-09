package com.example.duan1t1.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.duan1t1.R;
import com.example.duan1t1.adapter.Adapter_hoTro;
import com.example.duan1t1.model.Hang;
import com.example.duan1t1.model.User;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class frg_hotro extends Fragment {

    Adapter_hoTro adapterHoTro;
    List<User> list_us;

    public frg_hotro() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frg_hotro, container, false);
        ListView listView = view.findViewById(R.id.lits_hotro);
        list_us = new ArrayList<>();
        adapterHoTro = new Adapter_hoTro(list_us, getContext());
        listView.setAdapter(adapterHoTro);
        nghe();
        return view;
    }

    private void nghe() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("hoTro").addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                                list_us.add(dc.getDocument().toObject(User.class));
                                adapterHoTro.notifyDataSetChanged();
                                break;
                            case MODIFIED:
                                User dtoq = dc.getDocument().toObject(User.class);
                                if (dc.getOldIndex() == dc.getNewIndex()) {
                                    list_us.set(dc.getOldIndex(), dtoq);
                                    adapterHoTro.notifyDataSetChanged();
                                } else {
                                    list_us.remove(dc.getOldIndex());
                                    list_us.add(dtoq);
                                    adapterHoTro.notifyDataSetChanged();
                                }
                                break;
                            case REMOVED:
                                list_us.remove(dc.getOldIndex());
                                adapterHoTro.notifyDataSetChanged();
                                break;
                        }
                    }
                }
            }
        });
    }
}
//        onCreateView(): Phương thức này inflates layout cho Fragment từ tập tin XML và trả về một View. Trong trường hợp này, layout được sử dụng là fragment_frg_hotro.xml, chứa một ListView để hiển thị danh sách người dùng cần hỗ trợ.
//
//        nghe(): Phương thức này thực hiện lắng nghe sự thay đổi trong collection "hoTro" trên Firestore. Khi có sự thay đổi (thêm, sửa, xoá), phương thức này được gọi và xử lý dữ liệu dựa trên các loại sự kiện.
//
//        Trong sự kiện ADDED: Nếu có một tài liệu được thêm vào collection, Fragment sẽ tạo một đối tượng User từ dữ liệu của tài liệu đó và thêm vào danh sách list_us. Sau đó, cập nhật adapter để cập nhật giao diện.
//
//        Trong sự kiện MODIFIED: Nếu có một tài liệu được sửa đổi trong collection, Fragment sẽ kiểm tra xem tài liệu đã bị sửa đổi có cùng vị trí với trước đó hay không. Nếu cùng vị trí, nó sẽ cập nhật đối tượng User tại vị trí đó trong danh sách. Nếu không, nó sẽ xoá đối tượng cũ và thêm đối tượng mới vào danh sách.
//
//        Trong sự kiện REMOVED: Nếu có một tài liệu bị xoá khỏi collection, Fragment sẽ xoá đối tượng User tương ứng khỏi danh sách.
//
//        Adapter_hoTro: Là một adapter được sử dụng để hiển thị danh sách người dùng cần hỗ trợ trong ListView.