package com.foodies.vedriver.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.foodies.vedriver.R;
import com.foodies.vedriver.databinding.ItemCurrentTaskBinding;
import com.foodies.vedriver.databinding.ItemTodayEarningBinding;
import com.foodies.vedriver.interfaces.RecycleItemClickListener;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class CurrentTaskAdapter extends RecyclerView.Adapter<CurrentTaskAdapter.MyViewHolder> {
    RecycleItemClickListener listenr;

    public CurrentTaskAdapter(RecycleItemClickListener listenr) {
        this.listenr = listenr;
    }

    @NonNull
    @Override
    public CurrentTaskAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        ItemCurrentTaskBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_current_task, viewGroup, false);
        binding.setClick(listenr);
        return new CurrentTaskAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrentTaskAdapter.MyViewHolder holder, int i) {
        //holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return 4;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ViewDataBinding binding;

        public MyViewHolder(ViewDataBinding databinding) {
            super(databinding.getRoot());
            this.binding = databinding;
        }



    }
}