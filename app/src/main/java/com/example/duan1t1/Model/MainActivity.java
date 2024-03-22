package com.example.duan1t1.Model;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.example.duan1t1.DAO.DAOQuanTriVien;
import com.example.duan1t1.EventBus.DangNhapActivity;
import com.example.duan1t1.FragmentAdmin.HoaDonFragment;
import com.example.duan1t1.FragmentAdmin.SanPhamFragment;
import com.example.duan1t1.FragmentAdmin.TrangChuFragment;
import com.example.duan1t1.R;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private static final int FRAGMENT_TRANGCHU = 0;

    private static final int FRAGMENT_HOADON = 1;
    private static final int FRAGMENT_SANPHAM = 2;

//    private static final int FRAGMENT_DOANHTHU = 3;
//    private static final int FRAGMENT_TOP10 = 4;
//    private static final int FRAGMENT_QUANLYTAIKHOAN = 5;
//
//    private static final int FRAGMENT_THONGTINTAIKHOAN = 6;


    private int mCurrentFragment = FRAGMENT_TRANGCHU;


    DrawerLayout drawerLayout_home;
    NavigationView navigationView_home;
    Toolbar toolbar;

    String[] title = {"Trang chủ ",
            "Quản lý đơn hàng  ",
            "Quản lý sản phẩm ",
//            "Doanh thu",
//            "Top 10 sản phẩm bán chạy",
//            "Quản lý tài khoản ",
//            "Thông tin tài khoản"
            };
    TextView tv_username;

    Button btndonhang,btnsanpham,btnquanly,btnthongtin;
    //test may NA
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();

        navigationView_home();
        navigationView_home.setItemTextColor(ColorStateList.valueOf(getColor(R.color.black)));
        navigationView_home.setItemIconTintList(ColorStateList.valueOf(getColor(R.color.black)));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title[0]);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                MainActivity.this,drawerLayout_home,toolbar,R.string.open,R.string.close);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout_home.addDrawerListener(drawerToggle);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(getDrawable(R.drawable.ic_menu));


        showInf();


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout_home,new TrangChuFragment());
        fragmentTransaction.commit();
        drawerLayout_home.close();



    }

    private void showInf() {
        DAOQuanTriVien quanTriVien = new DAOQuanTriVien(MainActivity.this);
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");

        if (username != null && !username.equals("admin")) {
            // Thực hiện công việc cần thiết
            QuanTriVien quanTriVien1 = quanTriVien.getID(username);
            tv_username.setText(quanTriVien1.getMaQTV());

            navigationView_home.getMenu().findItem(R.id.createAccount).setVisible(false);
        }

    }
    private void navigationView_home() {
        navigationView_home.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //TRANG CHỦ
                if (item.getItemId() == R.id.trangchu) {
                    toolbar.setTitle(title[0]);
                    if (mCurrentFragment != FRAGMENT_TRANGCHU) {
                        replaceFragment(new TrangChuFragment());
                        mCurrentFragment = FRAGMENT_TRANGCHU;
                    }
                }

                //QUẢN LÝ ĐƠN HÀNG
                else if (item.getItemId() == R.id.donnhap) {
                    toolbar.setTitle(title[1]);
                    if (mCurrentFragment != FRAGMENT_HOADON) {
                        replaceFragment(new HoaDonFragment());
                        mCurrentFragment = FRAGMENT_HOADON;
                    }
                }

                //QUẢN LÝ SẢN PHẨM
                else if (item.getItemId() == R.id.sanPham) {
                    toolbar.setTitle(title[2]);
                    if (mCurrentFragment != FRAGMENT_SANPHAM) {
                        replaceFragment(new SanPhamFragment());
                        mCurrentFragment = FRAGMENT_SANPHAM;
                    }
                }

//                //DOANH THU
//                else if (item.getItemId() == R.id.revenue) {
//                    toolbar.setTitle(title[3]);
//                    if (mCurrentFragment != FRAGMENT_DOANHTHU) {
//                        replaceFragment(new DoanhThuFragment());
//                        mCurrentFragment = FRAGMENT_DOANHTHU;
//                    }
//                }
//
//                //TOP 10 SẢN PHẨM BÁN CHẠY
//                else if (item.getItemId() == R.id.top10) {
//                    toolbar.setTitle(title[4]);
//                    if (mCurrentFragment != FRAGMENT_TOP10) {
//                        replaceFragment(new Top10Fragment());
//                        mCurrentFragment = FRAGMENT_TOP10;
//                    }
//                }
//
//                //QUẢN LÝ TÀI KHOẢN
//                else if (item.getItemId() == R.id.createAccount) {
//                    toolbar.setTitle(title[5]);
//                    if (mCurrentFragment != FRAGMENT_QUANLYTAIKHOAN) {
//                        replaceFragment(new QuanLyTKFragment());
//                        mCurrentFragment = FRAGMENT_QUANLYTAIKHOAN;
//                    }
//                }
//
//                //THÔNG TIN TÀI KHOẢN
//                else if (item.getItemId() == R.id.changePassword) {
//                    toolbar.setTitle(title[6]);
//                    if (mCurrentFragment != FRAGMENT_THONGTINTAIKHOAN) {
//                        replaceFragment(new DoiMatKhauFragment());
//                        mCurrentFragment = FRAGMENT_THONGTINTAIKHOAN;
//                    }
//                }

                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Bạn có chắc chắn muốn đăng xuất tài khoản này không ?");
                    builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(MainActivity.this, DangNhapActivity.class));
                            finishAffinity();
                        }
                    });
                    builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

                return true;
            }


        });

    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout_home,fragment);
        fragmentTransaction.commit();
        drawerLayout_home.close();
    }



    private void initUI() {
        toolbar = findViewById(R.id.toolbar_home);

        drawerLayout_home = findViewById(R.id.drawerLayout_home);

        navigationView_home = findViewById(R.id.navigationView_home);

        tv_username = navigationView_home.getHeaderView(0).findViewById(R.id.tv_username);
    }



}