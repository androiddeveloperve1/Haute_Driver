package com.app.mylibertadriver.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.app.mylibertadriver.R;
import com.app.mylibertadriver.databinding.ItemTodayEarningBinding;
import com.app.mylibertadriver.interfaces.RecycleItemClickListener;

/**
 * Create By Rahul Mangal
 * Project Haute Delivery
 */

public class TodayEarningAdapter extends RecyclerView.Adapter<TodayEarningAdapter.MyViewHolder> {
    RecycleItemClickListener listenr;

    public TodayEarningAdapter(RecycleItemClickListener listenr) {
        this.listenr = listenr;
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
        //holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return 10;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ViewDataBinding binding;

        public MyViewHolder(ViewDataBinding databinding) {
            super(databinding.getRoot());
            this.binding = databinding;
        }


    }
}