package com.app.mylibertadriver.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.app.mylibertadriver.BR;
import com.app.mylibertadriver.R;
import com.app.mylibertadriver.databinding.ItemTodayEarningBinding;
import com.app.mylibertadriver.interfaces.RecycleItemClickListener;
import com.app.mylibertadriver.model.orders.OrderItemModel;
import com.app.mylibertadriver.model.orders.TaskResponse;

import java.util.ArrayList;

/**
 * Create By Rahul Mangal
 * Project Haute Delivery
 */

public class TodayEarningAdapter extends RecyclerView.Adapter<TodayEarningAdapter.MyViewHolder> {
    RecycleItemClickListener listenr;
    ArrayList<TaskResponse> list;

    public TodayEarningAdapter(ArrayList<TaskResponse> list, RecycleItemClickListener listenr) {
        this.listenr = listenr;
        this.list = list;
    }

    @NonNull
    @Override
    public TodayEarningAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        ItemTodayEarningBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_today_earning, viewGroup, false);
        binding.setItemClickListener(listenr);
        return new TodayEarningAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TodayEarningAdapter.MyViewHolder holder, int i) {
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