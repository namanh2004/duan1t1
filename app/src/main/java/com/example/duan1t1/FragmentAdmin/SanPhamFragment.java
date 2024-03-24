package com.example.duan1t1.FragmentAdmin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan1t1.Adapter.SanPhamAdapter;
import com.example.duan1t1.DAO.DaoSanPham;
import com.example.duan1t1.EventBus.IClickItemRCV;
import com.example.duan1t1.Model.SanPham;
import com.example.duan1t1.R;
import com.example.duan1t1.databinding.FragmentSanPhamBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class SanPhamFragment extends Fragment {
    RecyclerView rcv_sanpham;

    private FragmentSanPhamBinding binding;
    ArrayList<SanPham> lstSanPham;


    FloatingActionButton btn_add;
    static DaoSanPham sanPhamDao;
    SanPhamAdapter adapter;
    SanPham sanPham;

    Dialog dialog;
    EditText edMaSP, edTenSP, edHangSP, edMoTa, edGia, edImages;

    int getPosition;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_san_pham, container, false);
        rcv_sanpham = v.findViewById(R.id.rcv_sp);

        lstSanPham = new ArrayList<>();
        sanPhamDao = new DaoSanPham(getContext());

        btn_add = v.findViewById(R.id.btn_add);
        loadData();
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(getContext(),0);
            }
        });



        return v;
    }

    private void loadData(){
        lstSanPham =(ArrayList<SanPham>) sanPhamDao.getAll();
        adapter = new SanPhamAdapter(getContext(), lstSanPham, new IClickItemRCV() {
            @Override
            public void iclickItem(RecyclerView.ViewHolder viewHolder, int position, int type) {

                getPosition = position;
                if (type == 0) {
                    openDialog(getContext(),1);
                } else {
                    xoa_sp(String.valueOf(position));
                }
            }
        });

        rcv_sanpham.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rcv_sanpham.setAdapter(adapter);

        Bundle lstTong = new Bundle();
        lstTong.putString("mas", lstSanPham.toString());
    }


    public void openDialog(final Context context, final int type){
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_sanpham);

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        TextView title =dialog.findViewById(R.id.tv_title_sp);
        edTenSP = dialog.findViewById(R.id.edt_tenSP_diaSP);
        edHangSP = dialog.findViewById(R.id.edt_hangSP_diaSP);
        edMoTa = dialog.findViewById(R.id.edt_MoTa_diaSP);
        edGia = dialog.findViewById(R.id.edt_giaSP_diaSP);
        edMaSP = dialog.findViewById(R.id.edt_maSP_diaSP);
        edImages = dialog.findViewById(R.id.edt_images_diaSP);

        Button btn_cancel = dialog.findViewById(R.id.btn_cancel_diaSp);
        Button btn_save = dialog.findViewById(R.id.btn_save_diaSp);

        edHangSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder bui1 = new AlertDialog.Builder(context);
                bui1.setTitle("Chọn hãng");
                String[] loai_add = {"Adidas", "Puma", "Nike", "Vans", "New Balance"};
                bui1.setItems(loai_add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        edHangSP.setText(loai_add[i]);
                    }
                });

                AlertDialog dia1 = bui1.create();
                dia1.show();
            }
        });

        if(type != 0){
            sanPham = lstSanPham.get(getPosition);
            title.setText("Cập nhật thông tin");
            edMaSP.setText(String.valueOf(sanPham.getMaSP()));
            edTenSP.setText(sanPham.getTenSP());
            edHangSP.setText(sanPham.getTenHang());
            edMoTa.setText(sanPham.getMoTa());
            edGia.setText(String.valueOf(sanPham.getGiaTien()));
            edImages.setText(sanPham.getImages());
        } else {
            title.setText("Thêm sản phẩm");
            edMaSP.setText("Mã sản phẩm");
        }

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sanPham = new SanPham();
                sanPham.setTenSP(edTenSP.getText().toString().trim());
                sanPham.setTenHang(edHangSP.getText().toString().trim());
                sanPham.setMoTa(edMoTa.getText().toString().trim());
                sanPham.setGiaTien(Integer.parseInt(edGia.getText().toString().trim()));
                sanPham.setImages(edImages.getText().toString());
                if(validate()> 0){
                    if(type == 0){
                        if(sanPhamDao.insert(sanPham) > 0){
                            Toast.makeText(context, "Thêm thành công !", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Thêm thất bại !", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        sanPham.setMaSP(Integer.parseInt(edMaSP.getText().toString()));
                        if(sanPhamDao.update(sanPham) > 0){
                            Toast.makeText(context, "Chỉnh sửa thông tin thành công !", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context, "Chỉnh sửa thông tin thất bại !", Toast.LENGTH_SHORT).show();
                        }
                    }
                    dialog.dismiss();
                    loadData();
                }

            }
        });
        dialog.show();
    }

    public int validate() {
        int check = 1;
        if (edTenSP.getText().length() == 0 || edHangSP.getText().length() == 0 || edMoTa.getText().length() == 0 || edGia.length() == 0 || edImages.length() == 0) {
            Toast.makeText(getContext(), "Bạn phải nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            check = -1;
        }
        return check;
    }

    public void xoa_sp(final String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Xóa Sản Phẩm");
        builder.setMessage("Bạn có chắc muốn xóa ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(getContext(), "Xóa thành công !", Toast.LENGTH_SHORT).show();
                sanPhamDao.delete(id);
                loadData();
                dialog.dismiss();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public static SanPhamFragment newInstance(){
        SanPhamFragment fragment = new SanPhamFragment();
        return fragment;
    }
}