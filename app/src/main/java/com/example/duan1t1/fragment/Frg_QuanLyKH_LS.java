package com.example.duan1t1.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.duan1t1.R;
import com.example.duan1t1.adapter.viewPagerQuanLy;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;



public class Frg_QuanLyKH_LS extends Fragment {
ViewPager2 viewPager2;
TabLayout tabLayout;

    public Frg_QuanLyKH_LS() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frg__quan_ly_k_h__l_s, container, false);
        viewPager2 = view.findViewById(R.id.view_pager2_kh);
        tabLayout = view.findViewById(R.id.tab_layout_kh);

        viewPagerQuanLy viewPagerQuanLy = new viewPagerQuanLy(getActivity());
        viewPager2.setAdapter(viewPagerQuanLy);
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Quản lý khách hàng");
                        break;
                    case 1:
                        tab.setText("Danh sách yêu cầu nạp");
                        break;
                    default:
                        tab.setText("Quản lý khách hàng");
                        break;
                }
            }
        }).attach();
        return view;
    }
}