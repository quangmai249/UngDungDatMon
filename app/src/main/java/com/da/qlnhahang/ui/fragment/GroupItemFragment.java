package com.da.qlnhahang.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.da.qlnhahang.model.Item;
import com.da.qlnhahang.ui.adapter.ItemAdapter;
import com.da.qlnhahang.databinding.FragmentGroupItemBinding;

import java.util.ArrayList;

public class GroupItemFragment extends Fragment implements ItemAdapter.ItemItemClick {
    private FragmentGroupItemBinding binding;
    private ItemAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentGroupItemBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new ItemAdapter(this);
        binding.rcItem.setAdapter(adapter);
        ArrayList<Item> items = (ArrayList<Item>) getArguments().getSerializable(Item.class.getName());
        adapter.setData(items);
    }


    @Override
    public void onItemItemClicked(Item item) {
        if (item.getAvailable().equals("Có sẵn")) {
            item.setSelected(!item.isSelected());
            adapter.notifyItemChanged(adapter.getData().indexOf(item));
        } else {
            Toast.makeText(getContext(), "Sản phẩm hết hàng", Toast.LENGTH_SHORT).show();
        }
    }
}
