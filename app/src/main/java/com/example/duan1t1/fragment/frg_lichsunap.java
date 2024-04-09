package com.example.duan1t1.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.duan1t1.R;
import com.example.duan1t1.adapter.Adapter_dsYeuCauNap;
import com.example.duan1t1.model.User;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class frg_lichsunap extends Fragment {

    RecyclerView recyclerView;
    Adapter_dsYeuCauNap adapter_dsYeuCauNap;
    List<HashMap<String, Object>> list;
    List<User> list_use;
    FirebaseFirestore db;

    public frg_lichsunap() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frg_lichsunap, container, false);
        recyclerView = view.findViewById(R.id.rcv_nap);
        list =new ArrayList<>();
        list_use = new ArrayList<>();
        getListYC();
        getSoDU();
        Log.e("TAG","soDU"+list_use);
        adapter_dsYeuCauNap = new Adapter_dsYeuCauNap(getContext(), list, list_use);
        recyclerView.setAdapter(adapter_dsYeuCauNap);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    private List<HashMap<String, Object>> getListYC() {
        db = FirebaseFirestore.getInstance();
        db.collection("naptien").orderBy("timeSort", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    return;
                }
                if (value==null){
                    return;

                }
                for (DocumentChange dc : value.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            list.add((HashMap<String, Object>) dc.getDocument().getData());
                            adapter_dsYeuCauNap.notifyDataSetChanged();
                            break;
                        case MODIFIED:
                            HashMap<String,Object> dtoq = (HashMap<String, Object>) dc.getDocument().getData();
                            if (dc.getOldIndex() == dc.getNewIndex()) {
                                list.set(dc.getOldIndex(), dtoq);
                                adapter_dsYeuCauNap.notifyDataSetChanged();
                            } else {
                                list.remove(dc.getOldIndex());
                                list.add(dtoq);
                                adapter_dsYeuCauNap.notifyDataSetChanged();
                            }
                            break;
                        case REMOVED:
                            list.remove(dc.getOldIndex());
                            adapter_dsYeuCauNap.notifyDataSetChanged();
                            break;
                    }

                }
            }
        });
        return list;
    }

    public void getSoDU() {
        db.collection("user").whereEqualTo("chucVu", 3).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    return;
                }
                if (value==null){
                    return;
                }

                for (DocumentChange dc : value.getDocumentChanges()) {
                        switch (dc.getType()) {
                            case ADDED:
                                list_use.add(dc.getDocument().toObject(User.class));

                                break;
                            case MODIFIED:
                                User dtoq = dc.getDocument().toObject(User.class);
                                if (dc.getOldIndex() == dc.getNewIndex()) {
                                    list_use.set(dc.getOldIndex(), dtoq);
                                } else {
                                    list_use.remove(dc.getOldIndex());
                                    list_use.add(dtoq);
                                }
                                break;
                            case REMOVED:
                                list_use.remove(dc.getOldIndex());
                                break;
                    }
                    adapter_dsYeuCauNap.notifyDataSetChanged();
                }
            }
        });
    }
}
//        onCreateView(): Phương thức này inflates layout cho Fragment từ tập tin XML và trả về một View. Trong trường hợp này, layout được sử dụng là fragment_frg_lichsunap.xml, chứa một RecyclerView để hiển thị danh sách yêu cầu nạp tiền.
//
//        getListYC(): Phương thức này thực hiện lắng nghe sự thay đổi trong collection "naptien" trên Firestore để lấy danh sách yêu cầu nạp tiền. Khi có sự thay đổi (thêm, sửa, xoá), phương thức này được gọi và xử lý dữ liệu dựa trên các loại sự kiện.
//
//        Trong sự kiện ADDED: Nếu có một tài liệu được thêm vào collection, Fragment sẽ thêm dữ liệu của tài liệu đó vào danh sách list, sau đó cập nhật adapter để cập nhật giao diện.
//
//        Trong sự kiện MODIFIED: Nếu có một tài liệu được sửa đổi trong collection, Fragment sẽ kiểm tra xem tài liệu đã bị sửa đổi có cùng vị trí với trước đó hay không. Nếu cùng vị trí, nó sẽ cập nhật dữ liệu tại vị trí đó trong danh sách. Nếu không, nó sẽ xoá dữ liệu cũ và thêm dữ liệu mới vào danh sách.
//
//        Trong sự kiện REMOVED: Nếu có một tài liệu bị xoá khỏi collection, Fragment sẽ xoá dữ liệu tương ứng khỏi danh sách.
//
//        getSoDU(): Phương thức này thực hiện lắng nghe sự thay đổi trong collection "user" trên Firestore để lấy danh sách người dùng có chức vụ là 3 (có thể là người dùng cần nạp tiền). Khi có sự thay đổi (thêm, sửa, xoá), phương thức này được gọi và xử lý dữ liệu tương tự như trong phương thức getListYC().