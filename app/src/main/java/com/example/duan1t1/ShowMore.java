package com.example.duan1t1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1t1.adapter.Adapter_itemCuaHang;
import com.example.duan1t1.model.SanPham;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShowMore extends AppCompatActivity {
    RecyclerView rcv_list;
    Adapter_itemCuaHang itemCuaHang;
    List<SanPham> list;
    List<SanPham> list_Moi;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_more);
        Intent intent = getIntent();
        String[] a = intent.getStringArrayExtra("list");
        // adapter cua hang

        nghe(a[0]);
        list = new ArrayList<>();
        list_Moi = new ArrayList<>();
        rcv_list = findViewById(R.id.rcv_list_sanPham_more);
        searchView = findViewById(R.id.seach_sp_KH);
        TextView tenhang = findViewById(R.id.tv_ten_hang_show);
        ImageView close = findViewById(R.id.iv_close);
        tenhang.setText(a[1]);
        itemCuaHang = new Adapter_itemCuaHang(list, ShowMore.this);
        rcv_list.setAdapter(itemCuaHang);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(ShowMore.this, 2);
        rcv_list.setLayoutManager(gridLayoutManager);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
              itemCuaHang.getFilter().filter(newText);
              itemCuaHang.notifyDataSetChanged();
                return true;
            }
        });
    }

    FirebaseFirestore db;

    //lắng nghe các sự kiện thêm sưar xóa
    private void nghe(String a) {
        db = FirebaseFirestore.getInstance();
        //Dòng này chỉ định bộ sưu tập "sanPham" và thiết lập một truy vấn để lọc các tài liệu trong đó trường "maHang" bằng chuỗi được cung cấp a.
        db.collection("sanPham").whereEqualTo("maHang", a).
                addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value == null) {
                            return;
                        }
                        if (error != null) {
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            //Đoạn mã sử dụng một vòng lặp for để duyệt qua danh sách các thay đổi và thực hiện các hành động phù hợp với từng loại thay đổi (thêm, sửa đổi hoặc loại bỏ) trên các tài liệu tương ứng.
                            switch (dc.getType()) {
                                case ADDED:
                                    list.add(dc.getDocument().toObject(SanPham.class));
                                    itemCuaHang.notifyDataSetChanged();
                                    break;
                                case MODIFIED:
                                    SanPham user1 = dc.getDocument().toObject(SanPham.class);
                                    if (dc.getOldIndex() == dc.getNewIndex()) {
                                        list.set(dc.getOldIndex(), user1);
                                        itemCuaHang.notifyDataSetChanged();
                                    } else {
                                        list.remove(dc.getOldIndex());
                                        list.add(user1);
                                        itemCuaHang.notifyDataSetChanged();
                                    }
                                    break;
                                case REMOVED:
                                    dc.getDocument().toObject(SanPham.class);
                                    list.remove(dc.getOldIndex());
                                    itemCuaHang.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }
}
//        onCreate(): Phương thức này được gọi khi Activity được tạo ra. Trong phương thức này:
//
//        Thiết lập giao diện cho Activity.
//        Lấy dữ liệu từ Intent, bao gồm danh sách mã cửa hàng hoặc danh mục cửa hàng và tiêu đề của danh sách.
//        Khởi tạo các thành phần giao diện như RecyclerView để hiển thị danh sách sản phẩm, SearchView để tìm kiếm sản phẩm và TextView để hiển thị tiêu đề của danh sách.
//        Thiết lập sự kiện onClick cho ImageView close để kết thúc Activity khi người dùng nhấn vào.
//        nghe(String a): Phương thức này được sử dụng để lắng nghe sự kiện từ cơ sở dữ liệu Firebase khi có sự thay đổi trong danh sách sản phẩm của cửa hàng hoặc danh mục. Trong đó:
//
//        Kết nối đến cơ sở dữ liệu Firebase và lắng nghe sự kiện thay đổi trong bộ sưu tập "sanPham" dựa trên điều kiện đã được chỉ định (ví dụ: mã cửa hàng hoặc danh mục).
//        Xử lý các sự kiện thêm, sửa đổi và xóa sản phẩm trong danh sách và cập nhật RecyclerView để hiển thị các thay đổi đó.
//        searchView.setOnQueryTextListener(): Phương thức này được sử dụng để xử lý sự kiện tìm kiếm khi người dùng nhập văn bản vào SearchView. Khi có sự thay đổi trong văn bản tìm kiếm, Adapter của RecyclerView sẽ được cập nhật để lọc danh sách sản phẩm theo văn bản tìm kiếm và hiển thị kết quả lọc.