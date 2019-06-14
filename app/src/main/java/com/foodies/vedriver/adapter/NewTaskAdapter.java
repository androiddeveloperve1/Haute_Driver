package com.foodies.vedriver.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.foodies.vedriver.R;
import com.foodies.vedriver.databinding.ItemTodayEarningBinding;
import com.foodies.vedriver.databinding.ItemsNewTaskBinding;
import com.foodies.vedriver.interfaces.RecycleItemClickListener;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class NewTaskAdapter extends RecyclerView.Adapter<NewTaskAdapter.MyViewHolder> {
    RecycleItemClickListener listenr;

    public NewTaskAdapter(RecycleItemClickListener listenr) {
        this.listenr = listenr;
    }

    @NonNull
    @Override
    public NewTaskAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        ItemsNewTaskBinding binding = DataBindingUtil.inflate(inflater, R.layout.items_new_task, viewGroup, false);
        binding.setClick(listenr);
        return new NewTaskAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NewTaskAdapter.MyViewHolder holder, int i) {
        //holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return 1;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ViewDataBinding binding;

        public MyViewHolder(ViewDataBinding databinding) {
            super(databinding.getRoot());
            this.binding = databinding;
        }


    }
}