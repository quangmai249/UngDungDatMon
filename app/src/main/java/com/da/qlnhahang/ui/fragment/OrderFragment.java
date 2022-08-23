package com.da.qlnhahang.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.da.qlnhahang.App;
import com.da.qlnhahang.model.Order;
import com.da.qlnhahang.model.Table;
import com.da.qlnhahang.model.User;
import com.da.qlnhahang.ui.MainActivity;
import com.da.qlnhahang.ui.adapter.ItemOrderAdapter;
import com.da.qlnhahang.databinding.FragmentOrderBinding;
import com.da.qlnhahang.model.Item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderFragment extends Fragment implements ValueEventListener, ItemOrderAdapter.ItemItemClick {
    private FragmentOrderBinding binding;
    private Order order;
    private ItemOrderAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOrderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
//item
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new ItemOrderAdapter(this);
        binding.rcOrder.setAdapter(adapter);
        Table table = (Table) getArguments().getSerializable(Table.class.getName());
        binding.tvTableName.setText(table.getRoom() +": "+ table.getName());
        binding.btnAdd.setOnClickListener(v -> {
            order = order == null ? new Order() : order;
            Bundle bundle = new Bundle();
            bundle.putSerializable(Order.class.getName(), order);
            bundle.putSerializable(Table.class.getName(), table);
            ItemFragment itemFragment = new ItemFragment();
            itemFragment.setArguments(bundle);
            ((MainActivity) getActivity()).showFm(itemFragment);
        });
        binding.imBack.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().popBackStack();
        });
        if (order == null) {
            FirebaseDatabase.getInstance().getReference("order")
                    .child(table.getId())
                    .addValueEventListener(this);
        } else {
            setShowOwnerWaiter();
        }
        binding.imSubmit.setOnClickListener(v -> {
            if (order.getItems() != null && !order.getItems().isEmpty()) {
                FirebaseDatabase.getInstance().getReference("order")
                        .child(table.getId())
                        .child(order.getId() + "")
                        .setValue(order);
            }
            getActivity().getSupportFragmentManager().popBackStack();
        });
    }

    private void setShowOwnerWaiter() {
        User user = App.instance.user;
        if (user.getId().equals(order.getWaiter())) {
            binding.imSubmit.setVisibility(View.VISIBLE);
            binding.btnAdd.setVisibility(View.VISIBLE);
        } else {
            binding.imSubmit.setVisibility(View.GONE);
            binding.btnAdd.setVisibility(View.GONE);
        }
        adapter.setData(order.getItems(), user.getId().equals(order.getWaiter()));
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        for (DataSnapshot sn: snapshot.getChildren()) {
            Order order = sn.getValue(Order.class);
            if (order.getStatus() == 0) {
                this.order = order;
                setShowOwnerWaiter();
                return;
            }
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    @Override
    public void onItemItemClicked(Item item) {

    }


}
