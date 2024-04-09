package com.example.duan1t1.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.duan1t1.R;
import com.example.duan1t1.adapter.Adapter_choduyet;
import com.example.duan1t1.model.DonHang;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Fragment_choxacnhan extends Fragment {
RecyclerView rcv_list;
Adapter_choduyet adapterChoduyet;
List<DonHang> list;
FirebaseFirestore db;
FirebaseUser user ;
String TAG = "TAG";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frangment_choxacnhan,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        anhXa(view);
    }

    private void anhXa(View view) {
        rcv_list = view.findViewById(R.id.rcv_list_doncho);
        user = FirebaseAuth.getInstance().getCurrentUser();
        getData();

    }

    private void getData() {
        db = FirebaseFirestore.getInstance();
        db.collection("donHang").whereEqualTo("maKhachHang",user.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.isComplete()){
                    return;
                }
                list= new ArrayList<>();
                adapterChoduyet = new Adapter_choduyet(list,getContext(),1);
                rcv_list.setAdapter(adapterChoduyet);
                rcv_list.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                for (QueryDocumentSnapshot dc : task.getResult()){
                        list.add(dc.toObject(DonHang.class));
                        adapterChoduyet.notifyDataSetChanged();
                }
            }
        });
    }
}
//        onCreateView(): Phương thức này inflates layout cho Fragment từ tập tin XML và trả về một View. Trong trường hợp này, layout được sử dụng là frangment_choxacnhan.xml.
//
//        onViewCreated(): Phương thức này được gọi sau khi onCreateView() đã hoàn thành, cho phép thực hiện các thao tác khác liên quan đến giao diện người dùng.
//
//        anhXa(View view): Phương thức này được sử dụng để ánh xạ và cấu hình các thành phần giao diện người dùng như RecyclerView. Nó cũng khởi tạo một Adapter_choduyet và thiết lập nó cho RecyclerView.
//
//        getData(): Phương thức này thực hiện truy vấn đến cơ sở dữ liệu Firebase để lấy danh sách các đơn hàng mà người dùng đã tạo và chưa được xác nhận. Nó sử dụng whereEqualTo("maKhachHang", user.getUid()) để chỉ lấy các đơn hàng của người dùng hiện tại. Sau đó, nó tạo một Adapter_choduyet mới và thiết lập nó cho RecyclerView. Khi kết quả trả về, nó lặp qua mỗi tài liệu trong tập kết quả và thêm nó vào danh sách đơn hàng. Cuối cùng, nó gọi notifyDataSetChanged() để thông báo cho Adapter rằng dữ liệu đã thay đổi.
