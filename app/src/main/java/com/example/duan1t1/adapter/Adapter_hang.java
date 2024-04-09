package com.example.duan1t1.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.duan1t1.R;
import com.example.duan1t1.model.Hang;

import java.util.List;

public class Adapter_hang extends BaseAdapter {
    List<Hang> list;
    Context context;

    public Adapter_hang(List<Hang> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_list_hang,parent,false);
        TextView ten = view.findViewById(R.id.tv_ten_hang);
        ten.setText("Tên: "+list.get(position).getTenHang());
        return view;
    }
}
//        Constructor:
//        Chú thích về mục đích của constructor và các đối số nó nhận.
//        getCount():
//        Chú thích rằng phương thức này trả về số lượng phần tử trong danh sách list.
//        getItem():
//        Chú thích rằng phương thức này trả về đối tượng Hang ở vị trí cụ thể trong danh sách list.
//        getItemId():
//        Chú thích rằng phương thức này trả về ID của phần tử tại vị trí cụ thể trong danh sách.
//        Giải thích rằng trong trường hợp này, nó trả về 0 vì không có ID được sử dụng trong Adapter.
//        getView():
//        Chú thích về cách phương thức này tạo và trả về một View để hiển thị dữ liệu tại vị trí cụ thể trong danh sách.
//        Giải thích rằng nó sử dụng LayoutInflater để inflate layout từ file XML.
//        Chú thích về việc gắn dữ liệu từ đối tượng Hang vào các thành phần UI trong layout (ví dụ: TextView).
//        Mô tả về cách dữ liệu được hiển thị hoặc xử lý trong mỗi View.