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
import com.da.qlnhahang.ui.adapter.BillAdapter;
import com.da.qlnhahang.databinding.FragmentBillBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class BillFragment extends Fragment {
    private FragmentBillBinding binding;
    private BillAdapter adapter = new BillAdapter();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBillBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.rcBill.setAdapter(adapter);
        App app = ((App) getContext().getApplicationContext());
        app.tables.observe(getViewLifecycleOwner(), tables -> {
            ArrayList<Table> data = new ArrayList<>();
            for (Table t: tables) {
                for (Order order: t.getOrders()) {
                    if (Objects.equals(order.getWaiter(), app.user.getId())) {
                        Table table = new Table();
                        table.setId(t.getId());
                        table.setName(t.getName());
                        table.setRoom(t.getRoom());
                        ArrayList<Order> o = new ArrayList<>();
                        o.add(order);
                        table.setOrders(o);
                        data.add(table);
                    }
                }
            }
            Collections.sort(data, new Comparator<Table>() {
                @Override
                public int compare(Table table, Table t1) {
                    return (int) (t1.getOrders().get(0).getId() - table.getOrders().get(0).getId());
                }
            });
            adapter.setData(data);
        });
    }
}
