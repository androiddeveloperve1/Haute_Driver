package com.app.mylibertadriver.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.app.mylibertadriver.BR;
import com.app.mylibertadriver.R;
import com.app.mylibertadriver.databinding.ItemEarnHistoryBinding;
import com.app.mylibertadriver.model.EarningModel;
import com.app.mylibertadriver.model.EarningModelResponse;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class EarningAdapter extends RecyclerView.Adapter<EarningAdapter.MyViewHolder> {
    EarningModelResponse list;

    public EarningAdapter(EarningModelResponse list) {
        this.list = list;
    }

    @NonNull
    @Override
    public EarningAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        ItemEarnHistoryBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_earn_history, viewGroup, false);
        return new EarningAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EarningAdapter.MyViewHolder holder, int i) {
        holder.bind(list.getEarningDetails().get(i));
    }

    @Override
    public int getItemCount() {
        return list.getEarningDetails().size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ViewDataBinding binding;

        public MyViewHolder(ViewDataBinding databinding) {
            super(databinding.getRoot());
            this.binding = databinding;
        }

        public void bind(EarningModel data) {
            this.binding.setVariable(com.app.mylibertadriver.BR.data, data);
            this.binding.setVariable(BR.position, getAdapterPosition());
            this.binding.executePendingBindings();
        }

    }
}