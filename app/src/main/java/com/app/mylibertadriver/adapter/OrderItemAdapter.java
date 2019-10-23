package com.app.mylibertadriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.mylibertadriver.BR;
import com.app.mylibertadriver.R;
import com.app.mylibertadriver.databinding.ItemOrderItemsBinding;
import com.app.mylibertadriver.interfaces.RecycleItemClickListener;
import com.app.mylibertadriver.model.orders.OrderDetailsModel;
import com.app.mylibertadriver.model.orders.OrderItemModel;

import java.util.ArrayList;

/**
 * Create By Rahul Mangal
 * Project Haute Delivery
 */

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.MyViewHolder> {
    OrderDetailsModel lists;
    RecycleItemClickListener listenr;
    Context mContext;

    public OrderItemAdapter(Context mContext, OrderDetailsModel lists, RecycleItemClickListener listenr) {
        this.lists = lists;
        this.listenr = listenr;
        this.mContext = mContext;

    }

    @NonNull
    @Override
    public OrderItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        ItemOrderItemsBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_order_items, viewGroup, false);
        binding.setItemClickListener(listenr);
        return new OrderItemAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemAdapter.MyViewHolder holder, int i) {
        OrderItemModel item = lists.getOrder().get(i);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return lists.getOrder().size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemOrderItemsBinding binding;

        public MyViewHolder(ItemOrderItemsBinding databinding) {
            super(databinding.getRoot());
            this.binding = databinding;
        }

        public void bind(OrderItemModel data) {
            this.binding.setVariable(BR.item, data);
            this.binding.setVariable(BR.position, getAdapterPosition());
            this.binding.rvAttribute.setLayoutManager(new LinearLayoutManager(mContext));
            this.binding.setVariable(BR.attribute_adapter, new CartAttributeAdapter(mContext, lists.getOrder().get(getAdapterPosition()).getAttribute()));
            this.binding.executePendingBindings();
        }
    }


}