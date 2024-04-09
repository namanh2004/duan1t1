package com.example.duan1t1.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.duan1t1.R;

import java.util.List;

public class Adapter_diachi extends BaseAdapter {
    List<String> list; // Danh sách địa chỉ
    Context context; // Ngữ cảnh của ứng dụng

    // Constructor của Adapter
    public Adapter_diachi(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size(); // Trả về số lượng mục trong danh sách
    }

    @Override
    public Object getItem(int position) {
        return list.get(position); // Trả về mục tại vị trí được chỉ định
    }

    @Override
    public long getItemId(int position) {
        return 0; // Trả về ID của mục tại vị trí được chỉ định
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_list_hang,parent,false); // Inflate layout cho mỗi mục trong danh sách

        // Nếu danh sách không rỗng
        if (list != null) {
            TextView ten = view.findViewById(R.id.tv_ten_hang); // Tìm TextView cho tên hàng
            ten.setText(list.get(position)); // Thiết lập tên hàng cho TextView
        }

        return view; // Trả về view được điền dữ liệu để hiển thị trên màn hình
    }
}
