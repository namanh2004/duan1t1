package com.example.duan1t1.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.duan1t1.R;
import com.example.duan1t1.adapter.Adapter_hoTro;
import com.example.duan1t1.model.Hang;
import com.example.duan1t1.model.User;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class frg_hotro extends Fragment {

    Adapter_hoTro adapterHoTro;
    List<User> list_us;

    public frg_hotro() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frg_hotro, container, false);
        ListView listView = view.findViewById(R.id.lits_hotro);
        list_us = new ArrayList<>();
        adapterHoTro = new Adapter_hoTro(list_us, getContext());
        listView.setAdapter(adapterHoTro);
        nghe();
        return view;
    }

    private void nghe() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("hoTro").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(getContext(), "Lỗi không có dữ liệu", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {
                    for (DocumentChange dc : value.getDocumentChanges()) {
                        switch (dc.getType()) {
                            case ADDED:
                                dc.getDocument().toObject(Hang.class);
                                list_us.add(dc.getDocument().toObject(User.class));
                                adapterHoTro.notifyDataSetChanged();
                                break;
                            case MODIFIED:
                                User dtoq = dc.getDocument().toObject(User.class);
                                if (dc.getOldIndex() == dc.getNewIndex()) {
                                    list_us.set(dc.getOldIndex(), dtoq);
                                    adapterHoTro.notifyDataSetChanged();
                                } else {
                                    list_us.remove(dc.getOldIndex());
                                    list_us.add(dtoq);
                                    adapterHoTro.notifyDataSetChanged();
                                }
                                break;
                            case REMOVED:
                                list_us.remove(dc.getOldIndex());
                                adapterHoTro.notifyDataSetChanged();
                                break;
                        }
                    }
                }
            }
        });
    }
}