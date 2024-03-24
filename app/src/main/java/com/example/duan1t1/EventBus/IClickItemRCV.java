package com.example.duan1t1.EventBus;

import androidx.recyclerview.widget.RecyclerView;

public interface IClickItemRCV {
    void iclickItem(RecyclerView.ViewHolder viewHolder,int position,int type);
}
