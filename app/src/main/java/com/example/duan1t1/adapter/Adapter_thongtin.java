package com.example.duan1t1.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


import com.example.duan1t1.fragment.Fragment_khoanchi;
import com.example.duan1t1.fragment.Fragment_thongtin;
import com.example.duan1t1.model.User;


public class Adapter_thongtin extends FragmentStateAdapter {
    int i = 2;
    Fragment_thongtin fragmentThongtin;
    Fragment_khoanchi fragmentKhoanchi;

    public Adapter_thongtin(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        fragmentThongtin = new Fragment_thongtin();
        fragmentKhoanchi = new Fragment_khoanchi();
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {

            return fragmentThongtin;
        } else {
            return fragmentKhoanchi;
        }
    }

    @Override
    public int getItemCount() {
        return i;
    }

    public void data(User user){
        fragmentThongtin.setUs(user);
    }
}
//        Constructor: Adapter này nhận vào một FragmentActivity. Constructor này được sử dụng để khởi tạo Adapter và các biến cần thiết, bao gồm cả hai Fragment được sử dụng.
//
//        createFragment: Phương thức này tạo và trả về một Fragment tương ứng với vị trí được chỉ định trong ViewPager2. Nếu vị trí là 0, nó trả về Fragment_thongtin, nếu không, nó trả về Fragment_khoanchi.
//
//        getItemCount: Phương thức này trả về số lượng Fragment được hiển thị trong ViewPager2. Ở đây, chỉ có hai Fragment được hiển thị, do đó phương thức này trả về giá trị cố định là 2.
//
//        data: Phương thức này được sử dụng để cung cấp dữ liệu người dùng cho Fragment_thongtin. Nó gọi phương thức setUs của Fragment_thongtin và truyền vào đối tượng User.
