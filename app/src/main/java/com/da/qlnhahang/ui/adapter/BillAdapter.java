package com.da.qlnhahang.ui.adapter;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.da.qlnhahang.R;
import com.da.qlnhahang.databinding.ItemBillBinding;
import com.da.qlnhahang.model.Item;
import com.da.qlnhahang.model.Table;
import com.da.qlnhahang.ui.MainActivity;
import com.da.qlnhahang.ui.fragment.OrderFragment;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillViewHolder> {

    private ArrayList<Table> data;

    public void setData(ArrayList<Table> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public ArrayList<Table> getData() {
        return data;
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBillBinding binding = ItemBillBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new BillViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int index) {
        int position = index;
        holder.bindData(data.get(position), position);
    }
//getcount
    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class BillViewHolder extends RecyclerView.ViewHolder {
        private ItemBillBinding binding;
        private ItemBillAdapter adapter = new ItemBillAdapter();
        public BillViewHolder(ItemBillBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.rcBillDetail.setAdapter(adapter);
        }

        void bindData(Table item, int position) {
            binding.tvTime.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(item.getOrders().get(0).getId())));
            binding.tvTableName.setText(item.getRoom() + ": " + item.getName());
            if (item.getOrders().get(0).getStatus() == 0) {
                itemView.setOnClickListener(view -> {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Table.class.getName(), item);
                    OrderFragment orderFragment = new OrderFragment();
                    orderFragment.setArguments(bundle);
                    ((MainActivity) itemView.getContext()).showFm(orderFragment);
                });
            }
            ArrayList<Item> items = new ArrayList<>();
            if (item.getOrders().get(0).getStatus() != 0) {
                for (Item i : item.getOrders().get(0).getItems()) {
                    if (i.getStatus() == 2) {
                        items.add(i);
                    }
                }
            } else {
                items = item.getOrders().get(0).getItems();
            }
            adapter.setData(items);
            int total = 0;
            for (Item i: items) {
                total += Integer.parseInt(i.getPrice()) * i.getCount();
            }
            binding.tvTotal.setText("Tổng " + new DecimalFormat("#,###").format(total));
            binding.imDone.setVisibility(item.getOrders().get(0).getStatus() == 0 ? View.INVISIBLE : View.VISIBLE);
            if (item.isExpanded()) {
                binding.imExpand.setBackgroundResource(R.drawable.ic_collapand);
                binding.panel.setVisibility(View.VISIBLE);
            } else {
                binding.imExpand.setBackgroundResource(R.drawable.ic_expand);
                binding.panel.setVisibility(View.GONE);
            }
            binding.imExpand.setOnClickListener(view -> {
                item.setExpanded(!item.isExpanded());
                notifyItemChanged(position);
            });
        }
    }
}
