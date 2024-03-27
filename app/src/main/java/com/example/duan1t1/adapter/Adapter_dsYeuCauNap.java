package com.example.duan1t1.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duan1t1.R;
import com.example.duan1t1.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Adapter_dsYeuCauNap extends RecyclerView.Adapter<Adapter_dsYeuCauNap.viewHolder> {
    private final Context context;
    private final List<HashMap<String, Object>> list;
    private final List<User> list_use;
    FirebaseFirestore db;


    public Adapter_dsYeuCauNap(Context context, List<HashMap<String, Object>> list, List<User> listUse) {
        this.context = context;
        this.list = list;
        this.list_use = listUse;
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_danhsachyeucaunap, parent, false);
        return new viewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        list.sort(new Comparator<HashMap<String, Object>>() {
            @Override
            public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2) {
                return (int) (Long.parseLong(o2.get("timeSort").toString())-Long.parseLong(o1.get("timeSort").toString()));
            }
        });
        holder.tv_Email.setText("Email: " + list.get(position).get("email").toString());
        holder.tv_soTien.setText("+" + NumberFormat.getNumberInstance(Locale.getDefault()).format(list.get(position).get("sotien"))+" ");
        holder.tv_maNG.setText("Mã người dùng: " + list.get(position).get("maND").toString());
        holder.tv_maGD.setText("Mã giao dịch: " + list.get(position).get("maGG").toString());
        String linkAnh = list.get(position).get("anh").toString();
        Glide.with(context).load(linkAnh)
                .error(R.drawable.baseline_crop_original_24).into(holder.img_anhGD);
        String xam = "#D3D3CF";
        String cam = "#E6903B";

        if (Long.parseLong(list.get(position).get("trangThai").toString())==1){
            holder.btn_Huy.setEnabled(false);
            holder.btn_xacNhan.setEnabled(false);
            holder.btn_xacNhan.setText("Đã xác nhận");
            holder.btn_Huy.setBackgroundColor(Color.parseColor(xam));
            holder.btn_xacNhan.setBackgroundColor(Color.parseColor(xam));
        }else {
            holder.btn_Huy.setEnabled(true);
            holder.btn_xacNhan.setEnabled(true);
            holder.btn_xacNhan.setText("Xác nhận");
            holder.btn_Huy.setBackgroundColor(Color.parseColor(cam));
            holder.btn_xacNhan.setBackgroundColor(Color.parseColor(cam));
        }
        holder.img_anhGD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Showanh(linkAnh);
            }
        });
        User user = getUser(list.get(position),list_use);
        holder.btn_xacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xacNhan(position,user);
            }
        });
    }

    private void Showanh(String anh) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = ((Activity)context).getLayoutInflater().inflate(R.layout.dialog_anh,null,false);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView imageView = view.findViewById(R.id.imv_anh_gg);
        Log.e("TAG", "Showanh: "+anh );
//        imageView.setImageDrawable(anh.getDrawable());
        Glide.with(context).load(anh)
                .error(R.drawable.baseline_crop_original_24).into(imageView);
    }


    private void xacNhan(int p,User user) {
        db = FirebaseFirestore.getInstance();
        Long soTien = Long.valueOf(list.get(p).get("sotien").toString());
        Long soDU = user.getSoDu();
        Log.e("TAG","SODU"+soDU);
        Long tongTien = soDU+soTien;
        String mND = list.get(p).get("maND").toString();
        HashMap<String, Object> tien = new HashMap<>();
        tien.put("soDu", tongTien);
        Log.e("TAG", "xacNhan: "+soDU );
        db.collection("user").document(mND).update(tien).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {

                    list.get(p).put("trangThai",1);
                    db.collection("naptien").document(list.get(p).get("maGG").toString()).set(list.get(p)).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (!task.isComplete()){
                                Toast.makeText(context, "Lỗi", Toast.LENGTH_SHORT).show();
                            }
                            Toast.makeText(context, "Thành công", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private User getUser(HashMap<String,Object> map, List<User> list_use) {
        User user = new User();
        user.setSoDu(0l);
        for (User use : list_use){
            if (map.get("maND").equals(use.getMaUser())){
                return use;
            }
        }
        return  user;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView tv_Email, tv_soTien, tv_maGD, tv_maNG;
        AppCompatButton btn_xacNhan, btn_Huy;
        ImageView img_anhGD;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            tv_Email = itemView.findViewById(R.id.tv_Email_nap);
            tv_soTien = itemView.findViewById(R.id.tv_Sotien_nap);
            tv_maNG = itemView.findViewById(R.id.tv_MaND);
            tv_maGD = itemView.findViewById(R.id.tv_MaGiaoDich);
            img_anhGD = itemView.findViewById(R.id.imgv_anhNap);
            btn_xacNhan = itemView.findViewById(R.id.btn_xacNhan_nap);
            btn_Huy = itemView.findViewById(R.id.btn_Huy_nap);
        }
    }
}
