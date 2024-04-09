package com.example.duan1t1.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.example.duan1t1.R;
import com.example.duan1t1.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.viewHolder>  {
    private final Context context;
    private List<User> list;
    List<User> list_nvMoi;
    FirebaseFirestore db;

    public AdapterUser(Context context, List list) {
        this.context = context;
        this.list = list;
        this.list_nvMoi = list;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_quan_ly_khach_hang, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, @SuppressLint("RecyclerView") int position) {
        Log.e("TAG","size"+list.size());
        User user = list.get(position);
        holder.tvTen.setText("Tên: " + list.get(position).getHoTen());
        holder.tvEmail.setText("Email: " + list.get(position).getEmail());

        //Xử lý hiển thị khi trạng thái thay đổi, đỏ khi mà đối tượng đã bị ngưng hoạt động
        if (user.getTrangThai() == 0) {
            holder.tvTrangThai.setText("Không hoạt động");
            holder.tvTrangThai.setTextColor(ContextCompat.getColor(context, R.color.xam));
            holder.ibtn_xoa.setBackgroundResource(R.drawable.remove);
            holder.ibtn_xoa.setEnabled(true);
            holder.ibtn_xoa.setVisibility(View.GONE);
        } else if (user.getTrangThai() == 1) {
            holder.tvTrangThai.setText("Đang hoạt động");
            holder.tvTrangThai.setTextColor(ContextCompat.getColor(context, R.color.xanhla));
            holder.ibtn_xoa.setBackgroundResource(R.drawable.remove_dis);
            holder.ibtn_xoa.setEnabled(false);
        }
        holder.ibtn_xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.warning);
                builder.setTitle("Cảnh báo !");
                builder.setMessage("Bạn có chắc chắn muốn xóa dữ liệu của nhân viên " + user.getHoTen() + " không ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Truy cập tới collection user -> document có mã là user đó và tiến hành xóa vị trí đó
                        db.collection("user").document(list.get(position).getMaUser()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete()) {
                                    //Tiến hành thông báo khi thực thi thành công
                                    Toast.makeText(context, "Thành công", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(context, "Lỗi cụ rồi bảo dev fix đi", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });

        holder.tvTrangThai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.warning);
                builder.setTitle("Cánh bảo !");
                builder.setMessage("Bạn có muốn dừng hoạt động nhân viên " + user.getHoTen() + " không ?");
                builder.setPositiveButton("Tắt trạng thái", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeTT(0, list.get(position));
                    }
                });
                builder.setNegativeButton("Mở trạng thái", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeTT(1, list.get(position));
                    }
                });
                builder.create().show();

            }
        });
    }

    public Filter getTen() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint.toString().isEmpty()) {
                    list = list_nvMoi;
                } else {
                    List<User> list1_nv = new ArrayList<>();
                    for (User user : list_nvMoi) {
                        if (user.getHoTen().toLowerCase().trim().contains(constraint.toString().toLowerCase().trim())) {
                            list1_nv.add(user);
                        }
                    }
                    list =list1_nv;
                }

                FilterResults results = new FilterResults();
                results.values = list;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (List<User>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    private void changeTT(int i, User user) {
        // Truyền trạng thái (0||1) vào trong đối tượng sau đó truy cập tới vị trí có chứa mã user và thực hiện sửa đổi
        user.setTrangThai(i);
        db.collection("user").document(user.getMaUser()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    Toast.makeText(context, "Thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Lỗi cụ rồi bảo dev fix đi", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {


        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView tvTen, tvEmail, tvTrangThai;
        ImageButton ibtn_xoa;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            tvTen = itemView.findViewById(R.id.tv_Ten);
            tvEmail = itemView.findViewById(R.id.tv_Email);
            tvTrangThai = itemView.findViewById(R.id.tv_Trangthai);
            ibtn_xoa = itemView.findViewById(R.id.ibtn_xoa);
        }
    }


}
//        Constructor: Adapter này nhận vào một Context và một danh sách các đối tượng User. Constructor này được sử dụng để khởi tạo Adapter và các biến cần thiết, cũng như khởi tạo đối tượng FirebaseFirestore để tương tác với cơ sở dữ liệu Firebase Firestore.
//
//        onCreateViewHolder: Phương thức này tạo và trả về một ViewHolder mới cho mỗi item trong RecyclerView. Nó inflate layout item_quan_ly_khach_hang để tạo ra một ViewHolder.
//
//        onBindViewHolder: Phương thức này được gọi khi RecyclerView yêu cầu một ViewHolder để hiển thị dữ liệu tại một vị trí cụ thể. Nó gán dữ liệu từ danh sách User vào ViewHolder. Nó hiển thị thông tin về tên, email và trạng thái của người dùng. Nếu trạng thái là "Đang hoạt động", nút xóa sẽ bị vô hiệu hóa và màu chữ sẽ được thiết lập màu xanh lá cây. Nếu trạng thái là "Không hoạt động", nút xóa sẽ hiển thị và màu chữ sẽ được thiết lập màu xám.
//
//        getTen(): Phương thức này trả về một bộ lọc để tìm kiếm người dùng theo tên.
//
//        changeTT(): Phương thức này được sử dụng để thay đổi trạng thái của người dùng (hoạt động hoặc không hoạt động). Nó thực hiện cập nhật trạng thái của người dùng trong cơ sở dữ liệu Firestore.
//
//        getItemCount(): Phương thức này trả về số lượng item trong danh sách người dùng.
//
//        ViewHolder class: Lớp ViewHolder để giữ các thành phần giao diện cho mỗi item trong RecyclerView. Nó chứa các thành phần như TextView cho tên, email và trạng thái của người dùng, cũng như ImageButton cho nút xóa.
