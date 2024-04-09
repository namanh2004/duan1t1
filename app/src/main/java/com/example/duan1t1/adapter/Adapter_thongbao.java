package com.example.duan1t1.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.duan1t1.R;
import com.example.duan1t1.model.ThongBao;

import java.util.List;

public class Adapter_thongbao extends BaseAdapter {
    List<ThongBao> list;
    Context context;

    public Adapter_thongbao(List<ThongBao> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //thông báo
        convertView = ((Activity)context).getLayoutInflater().inflate(R.layout.item_thongbao,parent,false);
        TextView massage = convertView.findViewById(R.id.tv_thongbao);

        massage.setText(list.get(position).getNoiDung());
        return convertView;
    }
}
//          Constructor: Constructor của Adapter nhận vào một danh sách thông báo và một đối tượng Context. Danh sách này sẽ được hiển thị và Context sẽ được sử dụng để lấy LayoutInflater.
//
//        getCount(): Phương thức này trả về số lượng phần tử trong danh sách thông báo.
//
//        getItem() và getItemId(): Hai phương thức này chưa được implement và không được sử dụng trong code này.
//
//        getView(): Phương thức này tạo và cấu hình mỗi View item trong danh sách. Nó inflate layout từ file item_thongbao.xml, sau đó gán nội dung của thông báo từ danh sách thông báo vào TextView có ID là tv_thongbao trong layout.
