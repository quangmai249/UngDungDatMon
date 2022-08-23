package com.da.qlnhahang.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.da.qlnhahang.App;
import com.da.qlnhahang.databinding.FragmentHomeBinding;
import com.da.qlnhahang.model.Table;
import com.da.qlnhahang.ui.MainActivity;
import com.da.qlnhahang.ui.adapter.TableAdapter;

public class HomeFragment extends Fragment implements TableAdapter.ItemTableClick {
    private FragmentHomeBinding binding;
    private TableAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
        binding.refresh.setOnRefreshListener(() -> {
            loadData();
            binding.refresh.setRefreshing(false);
        });
    }
    private void loadData() {
        adapter = new TableAdapter(this);
        binding.rcTable.setAdapter(adapter);
        ((App) getContext().getApplicationContext()).tables.observe(getViewLifecycleOwner(), tables -> {
            adapter.setData(tables);
        });
    }
    @Override
    public void onItemTableClicked(Table item) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Table.class.getName(), item);
        OrderFragment orderFragment = new OrderFragment();
        orderFragment.setArguments(bundle);
        ((MainActivity) getActivity()).showFm(orderFragment);

    }


}
