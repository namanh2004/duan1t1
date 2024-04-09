package com.example.duan1t1.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Fragment_khoanchi extends Fragment {
RecyclerView rcv_list;
TextView tongGia;
Adapter_choduyet adapterChoduyet;
List<DonHang> list;
FirebaseFirestore db;
@SuppressLint("NewApi")
LocalDate dateStart = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
@SuppressLint("NewApi")
LocalDate dateEnd= LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
@SuppressLint("NewApi")
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
@SuppressLint("NewApi")
String ngayStart = formatter.format(dateStart);
@SuppressLint("NewApi")
String ngayEnd=formatter.format(dateEnd);
FirebaseUser user ;
Long tong=0l;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_khoanchi,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        anhXa(view);
    }

    private void anhXa(View view) {
        rcv_list = view.findViewById(R.id.rcv_list_khoanchi);
        tongGia = view.findViewById(R.id.tv_tonggia_khoanchi);
        list = new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        getData();
        adapterChoduyet = new Adapter_choduyet(list,getContext(),2);
        rcv_list.setAdapter(adapterChoduyet);
        rcv_list.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
    }

    private void getData() {
        db.collection("donHangDaDuyet")
                .whereGreaterThanOrEqualTo("ngayMua", ngayStart)
                .whereLessThanOrEqualTo("ngayMua", ngayEnd).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.isComplete()){
                            return;
                        }
                        list.clear();
                        for (QueryDocumentSnapshot dc : task.getResult()){
                            if (user.getUid().equals(dc.toObject(DonHang.class).getMaKhachHang())){
                                list.add(dc.toObject(DonHang.class));
                                tong+=dc.toObject(DonHang.class).getGiaDon();
                                tongGia.setText("Giá: "+ NumberFormat.getNumberInstance(Locale.getDefault()).format(tong)+" VND");
                                adapterChoduyet.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }
}
//        onCreateView(): Phương thức này inflates layout cho Fragment từ tập tin XML và trả về một View. Trong trường hợp này, layout được sử dụng là tab_khoanchi.xml.
//
//        onViewCreated(): Phương thức này được gọi sau khi onCreateView() đã hoàn thành, cho phép thực hiện các thao tác khác liên quan đến giao diện người dùng.
//
//        anhXa(View view): Phương thức này được sử dụng để ánh xạ và cấu hình các thành phần giao diện người dùng như RecyclerView và TextView. Nó cũng khởi tạo một Adapter_choduyet và thiết lập nó cho RecyclerView.
//
//        getData(): Phương thức này thực hiện truy vấn đến cơ sở dữ liệu Firebase để lấy danh sách các đơn hàng đã được duyệt trong khoảng thời gian hiện tại (tính từ ngày đầu tiên của tháng đến ngày cuối cùng của tháng). Các đơn hàng được lọc dựa trên ngày mua nằm trong khoảng thời gian đã chỉ định.
//
//        formatter: Định dạng ngày tháng được sử dụng để chuyển đổi ngày thành chuỗi với định dạng "yyyy/MM/dd".
//
//        dateStart và dateEnd: Các biến này đại diện cho ngày bắt đầu và kết thúc của tháng hiện tại. Chúng được tính bằng cách sử dụng lớp LocalDate của Java 8 và phương thức TemporalAdjusters để lấy ngày đầu tiên và ngày cuối cùng của tháng.
//
//        ngayStart và ngayEnd: Các biến này lưu trữ chuỗi biểu diễn của ngày bắt đầu và kết thúc của tháng hiện tại theo định dạng đã được xác định trước.
//
//        getData(): Phương thức này thực hiện truy vấn đến cơ sở dữ liệu Firebase để lấy danh sách các đơn hàng đã được duyệt trong khoảng thời gian hiện tại (tính từ ngày đầu tiên của tháng đến ngày cuối cùng của tháng). Các đơn hàng được lọc dựa trên ngày mua nằm trong khoảng thời gian đã chỉ định.
//
//        tong: Biến này tính tổng giá trị của các đơn hàng đã được duyệt trong khoảng thời gian hiện tại.
//
//        Adapter_choduyet: Là một Adapter được sử dụng để hiển thị danh sách các đơn hàng đã được duyệt trong RecyclerView.