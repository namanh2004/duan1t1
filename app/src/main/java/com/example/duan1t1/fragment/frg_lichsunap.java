package com.example.duan1t1.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.duan1t1.R;
import com.example.duan1t1.adapter.Adapter_dsYeuCauNap;
import com.example.duan1t1.model.User;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class frg_lichsunap extends Fragment {

    RecyclerView recyclerView;
    Adapter_dsYeuCauNap adapter_dsYeuCauNap;
    List<HashMap<String, Object>> list;
    List<User> list_use;
    FirebaseFirestore db;

    public frg_lichsunap() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frg_lichsunap, container, false);
        recyclerView = view.findViewById(R.id.rcv_nap);
        list =new ArrayList<>();
        list_use = new ArrayList<>();
        getListYC();
        getSoDU();
        Log.e("TAG","soDU"+list_use);
        adapter_dsYeuCauNap = new Adapter_dsYeuCauNap(getContext(), list, list_use);
        recyclerView.setAdapter(adapter_dsYeuCauNap);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    private List<HashMap<String, Object>> getListYC() {
        db = FirebaseFirestore.getInstance();
        db.collection("naptien").orderBy("timeSort", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    return;
                }
                if (value==null){
                    return;

                }
                for (DocumentChange dc : value.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            list.add((HashMap<String, Object>) dc.getDocument().getData());
                            adapter_dsYeuCauNap.notifyDataSetChanged();
                            break;
                        case MODIFIED:
                            HashMap<String,Object> dtoq = (HashMap<String, Object>) dc.getDocument().getData();
                            if (dc.getOldIndex() == dc.getNewIndex()) {
                                list.set(dc.getOldIndex(), dtoq);
                                adapter_dsYeuCauNap.notifyDataSetChanged();
                            } else {
                                list.remove(dc.getOldIndex());
                                list.add(dtoq);
                                adapter_dsYeuCauNap.notifyDataSetChanged();
                            }
                            break;
                        case REMOVED:
                            list.remove(dc.getOldIndex());
                            adapter_dsYeuCauNap.notifyDataSetChanged();
                            break;
                    }

                }
            }
        });
        return list;
    }

    public void getSoDU() {
        db.collection("user").whereEqualTo("chucVu", 3).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    return;
                }
                if (value==null){
                    return;
                }

                for (DocumentChange dc : value.getDocumentChanges()) {
                        switch (dc.getType()) {
                            case ADDED:
                                list_use.add(dc.getDocument().toObject(User.class));

                                break;
                            case MODIFIED:
                                User dtoq = dc.getDocument().toObject(User.class);
                                if (dc.getOldIndex() == dc.getNewIndex()) {
                                    list_use.set(dc.getOldIndex(), dtoq);
                                } else {
                                    list_use.remove(dc.getOldIndex());
                                    list_use.add(dtoq);
                                }
                                break;
                            case REMOVED:
                                list_use.remove(dc.getOldIndex());
                                break;
                    }
                    adapter_dsYeuCauNap.notifyDataSetChanged();
                }
            }
        });
    }
}