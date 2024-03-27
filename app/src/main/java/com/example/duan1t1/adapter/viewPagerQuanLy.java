package com.example.duan1t1.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


import com.example.duan1t1.fragment.QuanLyKhachHang;
import com.example.duan1t1.fragment.frg_lichsunap;


public class viewPagerQuanLy extends FragmentStateAdapter {
    public viewPagerQuanLy(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new QuanLyKhachHang();
            case 1: return new frg_lichsunap();
            default: return new QuanLyKhachHang();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
