package com.da.qlnhahang.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.da.qlnhahang.model.Group;
import com.da.qlnhahang.model.Order;
import com.da.qlnhahang.App;
import com.da.qlnhahang.databinding.FragmentItemBinding;
import com.da.qlnhahang.model.Item;
import com.da.qlnhahang.model.Table;
import com.da.qlnhahang.ui.adapter.GroupPageAdapter;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ItemFragment extends Fragment {
    private FragmentItemBinding binding;
    private GroupPageAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentItemBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new GroupPageAdapter(this);
        binding.pager.setAdapter(adapter);
        new TabLayoutMediator(binding.tab, binding.pager, (tab, position) -> tab.setText(adapter.getGroups().get(position).getName())).attach();
        Bundle bundle = getArguments();
        getItemInfo();
        binding.btnSave.setOnClickListener(v -> {
            ArrayList<Item> items = new ArrayList<>();
            for (Group g: adapter.getGroups()) {
                for (Item item : g.getItems()) {
                    if (item.isSelected()) {
                        items.add(item);
                    }
                }
            }
            Table table = (Table) bundle.getSerializable(Table.class.getName());
            App app = (App) getContext().getApplicationContext();
            Order order = (Order) bundle.getSerializable(Order.class.getName());
            order.setWaiter(app.user.getId());
            order.setItems(items);

            getActivity().getSupportFragmentManager().popBackStack();
        });

        binding.imBack.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().popBackStack();
        });
    }

    private void getItemInfo() {
        ArrayList<Group> groups = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("group").get().addOnCompleteListener(task -> {
            for (DataSnapshot snapshot: task.getResult().getChildren()) {
                Group group = snapshot.getValue(Group.class);
                group.setId(snapshot.getKey());
                FirebaseDatabase.getInstance().getReference("item").child(snapshot.getKey()).get().addOnCompleteListener(t -> {
                    ArrayList<Item> items = new ArrayList<>();
                    for (DataSnapshot sn: t.getResult().getChildren()) {
                        Item item = sn.getValue(Item.class);
                        item.setId(sn.getKey());
                        items.add(item);
                    }
                    group.setItems(items);
                    groups.add(group);
                    if (groups.size() == task.getResult().getChildrenCount()) {
                        adapter.setGroups(groups);
                    }
                });
            }
        });
    }
}
