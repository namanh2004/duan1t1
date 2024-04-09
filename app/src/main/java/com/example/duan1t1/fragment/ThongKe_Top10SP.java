package com.example.duan1t1.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1t1.R;
import com.example.duan1t1.adapter.Adapter_Top10;
import com.example.duan1t1.model.DonHang;
import com.example.duan1t1.model.Hang;
import com.example.duan1t1.model.SanPham;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ThongKe_Top10SP extends Fragment {

    RecyclerView recyclerView;
    List<SanPham> list_SanPham;
    List<DonHang> list_DonHang;
    List<Hang> list_Hang;
    Adapter_Top10 adapterTop10;
    FirebaseFirestore db;
    List<HashMap<String, Object>> list_top10;

    public ThongKe_Top10SP() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thong_ke__top10_s_p, container, false);
        recyclerView = view.findViewById(R.id.rcv_Top10sp);
        list_SanPham = new ArrayList<>();
        list_top10 = new ArrayList<>();
        getTop10();
        getSP();
        adapterTop10 = new Adapter_Top10(getContext(),list_SanPham,list_top10);
        recyclerView.setAdapter(adapterTop10);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        return view;
    }



    public void getSP() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("sanPham").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isComplete()) {
                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
                        list_SanPham.add(snapshot.toObject(SanPham.class));
                    }
                    adapterTop10.notifyDataSetChanged();
                }
            }
        });
    }

    public void getTop10() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("top10").orderBy("soLuong", Query.Direction.DESCENDING).limit(10).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isComplete()) {
                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
                        HashMap<String, Object> top10 = new HashMap<>();
                        top10.put("soLuong", snapshot.getLong("soLuong"));
                        top10.put("maSP", snapshot.get("maSP"));
                        list_top10.add(top10);
                        Log.e("TaG",""+top10.get("soLuong"));

                    }
                    adapterTop10.notifyDataSetChanged();
                }
            }
        });
    }

}
//        onCreateView(): Phương thức này được gọi để tạo và trả về giao diện người dùng của Fragment. Trong phương thức này:
//
//        Ánh xạ RecyclerView để hiển thị danh sách sản phẩm.
//        Khởi tạo các danh sách list_SanPham và list_top10 để lưu trữ thông tin về sản phẩm và top 10 sản phẩm bán chạy nhất.
//        Gọi các phương thức getSP() và getTop10() để lấy dữ liệu từ Firestore về danh sách sản phẩm và top 10 sản phẩm.
//        getSP(): Phương thức này được sử dụng để lấy danh sách sản phẩm từ Firestore:
//
//        Sử dụng đối tượng FirebaseFirestore để truy vấn tất cả các tài liệu trong bộ sưu tập "sanPham".
//        Khi dữ liệu được trả về, mỗi tài liệu được chuyển đổi thành đối tượng SanPham và thêm vào danh sách list_SanPham.
//        getTop10(): Phương thức này được sử dụng để lấy top 10 sản phẩm bán chạy nhất từ Firestore:
//
//        Sử dụng đối tượng FirebaseFirestore để truy vấn tất cả các tài liệu trong bộ sưu tập "top10", được sắp xếp theo số lượng giảm dần và giới hạn chỉ lấy 10 tài liệu đầu tiên.
//        Khi dữ liệu được trả về, mỗi tài liệu được chuyển đổi thành một HashMap chứa thông tin về số lượng sản phẩm và mã sản phẩm tương ứng, sau đó được thêm vào danh sách list_top10.
//        Adapter_Top10: Adapter Adapter_Top10 được sử dụng để hiển thị danh sách top 10 sản phẩm bán chạy nhất trong RecyclerView. Adapter này nhận danh sách list_SanPham chứa thông tin về tất cả các sản phẩm và danh sách list_top10 chứa thông tin về top 10 sản phẩm. Adapter sẽ hiển thị thông tin của các sản phẩm trong top 10 dựa trên thông tin được cung cấp từ hai danh sách này.
//
//        notifyDataSetChanged(): Sau khi lấy dữ liệu từ Firestore, phương thức notifyDataSetChanged() được gọi trên adapter để thông báo cho RecyclerView cập nhật giao diện hiển thị với dữ liệu mới.