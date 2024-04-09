package com.example.duan1t1.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


import com.example.duan1t1.fragment.ThongKe_DoanhThu;
import com.example.duan1t1.fragment.ThongKe_Top10SP;


public class viewPagerAdapter extends FragmentStateAdapter {
     public viewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }




    @NonNull
    @Override
    public Fragment createFragment(int position) {
         switch (position){
             case 0: return new ThongKe_DoanhThu();
             case 1: return new ThongKe_Top10SP();
             default: return new ThongKe_DoanhThu();
         }

    }

    @Override
    public int getItemCount() {
        return 2;
    }

}
//        Constructor: Lớp có một constructor nhận một đối tượng FragmentActivity làm tham số, được sử dụng để khởi tạo lớp cha của nó, FragmentStateAdapter.
//
//        createFragment(int position): Đây là phương thức trừu tượng được ghi đè từ lớp cha. Nó được sử dụng để tạo và trả về một fragment dựa trên vị trí (position) của ViewPager2. Trong phương thức này, sử dụng câu lệnh switch-case để xác định fragment nào sẽ được tạo ra dựa trên vị trí được chỉ định. Nếu vị trí là 0, phương thức trả về một fragment ThongKe_DoanhThu. Nếu vị trí là 1, phương thức trả về một fragment ThongKe_Top10SP. Trong trường hợp mặc định, nếu vị trí không phù hợp với bất kỳ trường hợp nào khác, phương thức trả về một fragment ThongKe_DoanhThu.
//
//        getItemCount(): Phương thức này trả về số lượng fragment mà adapter sẽ quản lý. Trong trường hợp này, adapter chỉ quản lý hai fragment, do đó trả về giá trị là 2.