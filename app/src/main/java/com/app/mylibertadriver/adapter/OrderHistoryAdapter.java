package com.app.mylibertadriver.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.app.mylibertadriver.BR;
import com.app.mylibertadriver.R;
import com.app.mylibertadriver.databinding.ItemOrderHistoryBinding;
import com.app.mylibertadriver.model.orders.TaskResponse;

import java.util.ArrayList;

/**
 * Create By Rahul Mangal
 * Project Haute Delivery
 */

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.MyViewHolder> {
    ArrayList<TaskResponse> list;

    public OrderHistoryAdapter(ArrayList<TaskResponse> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public OrderHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        ItemOrderHistoryBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_order_history, viewGroup, false);
        return new OrderHistoryAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryAdapter.MyViewHolder holder, int i) {
        holder.bind(list.get(i));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ViewDataBinding binding;

        public MyViewHolder(ViewDataBinding databinding) {
            super(databinding.getRoot());
            this.binding = databinding;
        }

        public void bind(TaskResponse data) {
            this.binding.setVariable(BR.data, data);
            this.binding.setVariable(BR.position, getAdapterPosition());
            this.binding.executePendingBindings();
        }

    }
}