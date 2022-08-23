package com.da.qlnhahang.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.da.qlnhahang.model.Notify;
import com.da.qlnhahang.App;
import com.da.qlnhahang.databinding.FragmentNotificationBinding;
import com.da.qlnhahang.model.User;
import com.da.qlnhahang.ui.adapter.NotifyAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class NotifyFragment extends Fragment implements ValueEventListener {
    private FragmentNotificationBinding binding;
    private NotifyAdapter adapter = new NotifyAdapter();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.rcNotify.setAdapter(adapter);
        FirebaseDatabase.getInstance().getReference("notify")
                .addValueEventListener(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        ArrayList<Notify> notifies = new ArrayList<>();
        User user = ((App) getContext().getApplicationContext()).user;
        for (DataSnapshot sn: snapshot.getChildren()) {
            Notify notify = sn.getValue(Notify.class);
            if (notify.getWaiter().equals(user.getId())) {
                notifies.add(notify);
            }
        }
        Collections.reverse(notifies);
        adapter.setData(notifies);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}