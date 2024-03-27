package com.example.duan1t1.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1t1.R;
import com.example.duan1t1.adapter.AdapterUser;
import com.example.duan1t1.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class QuanLyKhachHang extends Fragment {
    List<User> list ;
    AppCompatButton btn_Luu, btn_Huy;
    RecyclerView recyclerView;
    ImageButton button;
    AdapterUser adapterUser;
    SearchView searchView;
    String id;
    User user = new User();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth;
    String email,matkhau,hoten,sdt,cv;
    Dialog dialog;
    public QuanLyKhachHang() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quan_ly_khach_hang, null);
        recyclerView = view.findViewById(R.id.rcv_qlkh);
        searchView = view.findViewById(R.id.search_KH);
        //Gọi đến load data để có thể tạo list và thêm dữ liệu vào list
        loatData();
        firebaseAuth = FirebaseAuth.getInstance();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Thực hiện tìm kiếm bằng cách gọi tới hàm getten trong adapter
                adapterUser.getTen().filter(newText);
                return true;
            }
        });
        return view;
    }
    public void loatData() {
        //Khởi tạo list danh sách và thêm dữ liệu vào list danh sách người dùng
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list =new ArrayList<>();
        getNguoiDung();
        adapterUser = new AdapterUser(getContext(), list);
        recyclerView.setAdapter(adapterUser);
    }




    private void getNguoiDung() {
        //Lấy dữ liệu của khách hàng từ trên firebase
        db.collection("user").whereEqualTo("chucVu", 3).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }
                if (value != null) {
                    for (DocumentChange dc : value.getDocumentChanges()) {
                        Log.e("TAG", "onEvent: "+ dc.getType() );
                        switch (dc.getType()) {
                            case ADDED:
                                dc.getDocument().toObject(User.class);
                                list.add(dc.getDocument().toObject(User.class));
                                adapterUser.notifyDataSetChanged();
                                break;
                            case MODIFIED:
                                User user1 = dc.getDocument().toObject(User.class);
                                if (dc.getOldIndex() == dc.getNewIndex()) {
                                    list.set(dc.getOldIndex(), user1);
                                    adapterUser.notifyDataSetChanged();
                                } else {
                                    list.remove(dc.getOldIndex());
                                    list.add(user1);
                                    adapterUser.notifyDataSetChanged();
                                }
                                break;
                            case REMOVED:
                                dc.getDocument().toObject(User.class);
                                list.remove(dc.getOldIndex());
                                adapterUser.notifyDataSetChanged();
                        }
                    }
                }
            }
        });

    }

}