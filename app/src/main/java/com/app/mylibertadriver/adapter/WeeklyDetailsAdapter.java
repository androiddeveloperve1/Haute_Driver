package com.app.mylibertadriver.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.app.mylibertadriver.BR;
import com.app.mylibertadriver.R;
import com.app.mylibertadriver.databinding.ItemWeeklyPaymentBinding;
import com.app.mylibertadriver.interfaces.RecycleItemClickListener;
import com.app.mylibertadriver.model.WeeklyEarningModel;
import com.app.mylibertadriver.model.orders.TaskResponse;

import java.util.ArrayList;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class WeeklyDetailsAdapter extends RecyclerView.Adapter<WeeklyDetailsAdapter.MyViewHolder> {
    RecycleItemClickListener listenr;
    ArrayList<WeeklyEarningModel> list;

    public WeeklyDetailsAdapter(ArrayList<WeeklyEarningModel> list, RecycleItemClickListener listenr) {
        this.listenr = listenr;
        this.list = list;
    }

    @NonNull
    @Override
    public WeeklyDetailsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        ItemWeeklyPaymentBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_weekly_payment, viewGroup, false);
        binding.setItemClickListener(listenr);
        return new WeeklyDetailsAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WeeklyDetailsAdapter.MyViewHolder holder, int i) {
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

        public void bind(WeeklyEarningModel data) {
            this.binding.setVariable(com.app.mylibertadriver.BR.data, data);
            this.binding.setVariable(BR.position, getAdapterPosition());
            this.binding.executePendingBindings();
        }

    }
}