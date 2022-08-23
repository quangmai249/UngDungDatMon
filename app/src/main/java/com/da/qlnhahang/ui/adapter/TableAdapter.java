package com.da.qlnhahang.ui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.da.qlnhahang.model.Item;
import com.da.qlnhahang.model.Order;
import com.da.qlnhahang.model.Table;
import com.da.qlnhahang.databinding.ItemTableBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableViewHolder>{

    private ArrayList<Table> data;
    private ItemTableClick listener;

    public TableAdapter(ItemTableClick listener) {
        this.listener = listener;
    }

    public void setData(ArrayList<Table> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public ArrayList<Table> getData() {
        return data;
    }

    @NonNull
    @Override
    public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTableBinding binding = ItemTableBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TableViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TableViewHolder holder, int index) {
        int position = index;
        holder.bindData(data.get(position), position);
        holder.binding.getRoot().setOnClickListener(v -> {
            listener.onItemTableClicked(data.get(position));
        });

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class TableViewHolder extends RecyclerView.ViewHolder {
        private ItemTableBinding binding;
        private DatabaseReference reference;

        public TableViewHolder(ItemTableBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindData(Table item, int position) {
            binding.tvName.setText(item.getRoom() + ": "+ item.getName());
            binding.tvStatus.setVisibility(View.VISIBLE);
            binding.tvStatus.setTextColor(Color.BLACK);
            for (Order o: item.getOrders()) {
                if (o.getStatus() == 0) {
                    int maxStatus = -1;
                    for (Item i : o.getItems()) {
                        if (i.getStatus() > maxStatus) {
                            maxStatus = i.getStatus();
                        }
                    }
                    if (maxStatus == 2) {
                        binding.tvStatus.setText("Đợi giao");
                        binding.tvStatus.setTextColor(Color.RED);
                        return;
                    }
                    if (maxStatus == 1) {
                        binding.tvStatus.setText("Đang chế biến");
                        binding.tvStatus.setTextColor(Color.BLUE);
                        return;
                    }
                    if (maxStatus == 0) {
                        binding.tvStatus.setText("Đợi xác nhận");
                        binding.tvStatus.setTextColor(Color.GREEN);
                        return;
                    }
                }
            }
            if (reference == null) {
                reference = FirebaseDatabase.getInstance().getReference("order")
                        .child(data.get(position).getId());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<Order> orders = new ArrayList<>();
                        for (DataSnapshot sn : snapshot.getChildren()) {
                            Order order = sn.getValue(Order.class);
                            orders.add(order);
                        }
                        data.get(position).setOrders(orders);
                        notifyItemChanged(position);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
    }

    public interface ItemTableClick {
        void onItemTableClicked(Table item);
    }
}
