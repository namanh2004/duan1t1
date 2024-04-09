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
//    onCreateView(): Phương thức này được gọi để tạo và trả về giao diện người dùng của Fragment. Trong phương thức này:
//
//        Khởi tạo và ánh xạ các thành phần giao diện như RecyclerView và SearchView.
//        Gọi phương thức loatData() để tải dữ liệu và hiển thị danh sách khách hàng.
//        loatData(): Phương thức này được sử dụng để tải dữ liệu và hiển thị danh sách khách hàng:
//
//        Thiết lập RecyclerView và Adapter để hiển thị danh sách.
//        Gọi phương thức getNguoiDung() để lấy dữ liệu từ Firebase Firestore và cập nhật giao diện người dùng.
//        getNguoiDung(): Phương thức này được sử dụng để lấy danh sách khách hàng từ Firebase Firestore:
//
//        Sử dụng whereEqualTo("chucVu", 3) để lọc danh sách khách hàng với chức vụ là 3 (người dùng thông thường).
//        Thêm các sự kiện để lắng nghe các thay đổi trong danh sách khách hàng và cập nhật giao diện người dùng tương ứng.
//        SearchView.OnQueryTextListener(): Đây là một giao diện lắng nghe sự kiện khi người dùng nhập văn bản vào ô tìm kiếm. Trong phương thức này:
//
//        Thực hiện tìm kiếm trong danh sách khách hàng dựa trên văn bản được nhập vào ô tìm kiếm.
//        Gọi phương thức getTen() trong adapter để thực hiện việc lọc và hiển thị kết quả tìm kiếm.