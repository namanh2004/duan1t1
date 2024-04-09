package com.example.duan1t1.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.duan1t1.R;
import com.example.duan1t1.adapter.viewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;



public class frg_ThongKe extends Fragment {

    ViewPager2 viewPager2;
    TabLayout tabLayout;

    public frg_ThongKe() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frg__thong_ke, container, false);
        viewPager2 = view.findViewById(R.id.view_pager2);
        tabLayout = view.findViewById(R.id.tab_layout);

        viewPagerAdapter pagerAdapter = new viewPagerAdapter(getActivity());
        viewPager2.setAdapter(pagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Doanh thu");
                        break;
                    case 1:
                        tab.setText("Top 10 sản phẩm bán chạy");
                        break;
                    default:
                        tab.setText("Doanh thu");
                        break;
                }
            }
        }).attach();
        return view;
    }
}
//        onCreateView(): Phương thức này inflates layout cho Fragment từ tập tin XML và trả về một View. Trong trường hợp này, layout được sử dụng là fragment_frg__thong_ke.xml, chứa một ViewPager2 và một TabLayout.
//
//        viewPager2 và tabLayout: Khai báo các biến để tham chiếu tới ViewPager2 và TabLayout trong layout.
//
//        onCreateView(): Tạo và thiết lập ViewPager2 và TabLayout:
//
//        Khởi tạo adapter viewPagerAdapter (đã được custom) để cung cấp nội dung cho ViewPager2.
//        Gọi setAdapter() trên ViewPager2 và truyền vào adapter đã khởi tạo.
//        Sử dụng TabLayoutMediator để kết nối TabLayout và ViewPager2:
//        Truyền vào TabLayout và ViewPager2.
//        Sử dụng TabLayoutMediator.TabConfigurationStrategy để cấu hình các tab:
//        Đặt tiêu đề cho tab dựa trên vị trí của nó trong ViewPager2.