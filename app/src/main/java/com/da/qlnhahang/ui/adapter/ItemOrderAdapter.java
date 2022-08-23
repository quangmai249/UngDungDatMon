package com.da.qlnhahang.ui.adapter;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.da.qlnhahang.databinding.ItemItemOrderBinding;
import com.da.qlnhahang.model.Item;
import com.da.qlnhahang.utils.OrderStatus;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ItemOrderAdapter extends RecyclerView.Adapter<ItemOrderAdapter.ItemViewHolder> {

    private ArrayList<Item> data;
    private ItemItemClick listener;
    private boolean isOwner;

    public ItemOrderAdapter(ItemItemClick listener) {
        this.listener = listener;
    }

    public void setData(ArrayList<Item> data, boolean owner) {
        this.data = data;
        isOwner = owner;
        notifyDataSetChanged();
    }
//
    public ArrayList<Item> getData() {
        return data;
    }
//
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemItemOrderBinding binding = ItemItemOrderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bindData(data.get(position));
        holder.binding.getRoot().setOnClickListener(v -> {
            listener.onItemItemClicked(data.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private ItemItemOrderBinding binding;

        public ItemViewHolder(ItemItemOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindData(Item item) {
            DecimalFormat format = new DecimalFormat("#,###");
            binding.tvName.setText(item.getName());
            binding.tvPrice.setText(format.format(Integer.parseInt(item.getPrice())));
            binding.tvCount.setText(item.getCount() + "");
            binding.tvStatus.setText(OrderStatus.getStatus(item.getStatus()));
            binding.edtNote.setText(item.getNote());
            binding.imAdd.setVisibility(item.getStatus() == 0 ? View.VISIBLE : View.INVISIBLE);
            binding.imRemove.setVisibility(item.getStatus() == 0 ? View.VISIBLE : View.INVISIBLE);
            binding.edtNote.setEnabled(item.getStatus() == 0);
            if (!TextUtils.isEmpty(item.getReason())) {
                binding.tvReason.setVisibility(View.VISIBLE);
                binding.tvReason.setText(item.getReason());
                binding.imAdd.setVisibility(View.INVISIBLE);
            } else {
                binding.tvReason.setVisibility(View.GONE);
            }
            if (!isOwner) {
                binding.imRemove.setVisibility(View.GONE);
                binding.imAdd.setVisibility(View.GONE);
                binding.edtNote.setEnabled(false);
            }

            binding.imAdd.setOnClickListener(view -> {
                item.setCount(item.getCount() + 1);
                notifyItemChanged(data.indexOf(item));
            });
            binding.imRemove.setOnClickListener(view -> {
                if (item.getCount() > 1) {
                    item.setCount(item.getCount() - 1);
                    notifyItemChanged(data.indexOf(item));
                    return;
                }
                new AlertDialog.Builder(itemView.getContext())
                        .setMessage("Bạn muốn xóa món ăn này khỏi hóa đơn?")
                        .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                        .setPositiveButton("Ok", (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                            notifyItemRemoved(data.indexOf(item));
                            data.remove(item);
                        })
                        .create()
                        .show();
            });
            binding.edtNote.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    item.setNote(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
    }

    public interface ItemItemClick {
        void onItemItemClicked(Item item);
    }
}
