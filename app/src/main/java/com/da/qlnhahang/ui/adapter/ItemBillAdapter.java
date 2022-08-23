package com.da.qlnhahang.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.da.qlnhahang.databinding.ItemItemBillBinding;
import com.da.qlnhahang.model.Item;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ItemBillAdapter extends RecyclerView.Adapter<ItemBillAdapter.ItemViewHolder> {

    private ArrayList<Item> data;

    public void setData(ArrayList<Item> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public ArrayList<Item> getData() {
        return data;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemItemBillBinding binding = ItemItemBillBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bindData(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private ItemItemBillBinding binding;

        public ItemViewHolder(ItemItemBillBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindData(Item item) {
            DecimalFormat format = new DecimalFormat("#,###");
            binding.tvName.setText(item.getName());
            binding.tvPrice.setText(format.format(Integer.parseInt(item.getPrice())));
            binding.tvCount.setText(item.getCount() + "");
            binding.tvTotal.setText(format.format(Integer.parseInt(item.getPrice()) * item.getCount()));
        }
    }
}