package com.app.mylibertadriver.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.mylibertadriver.R;
import com.app.mylibertadriver.model.AttributeModel;

import java.util.ArrayList;

public class CartAttributeAdapter extends RecyclerView.Adapter<CartAttributeAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<AttributeModel> list;

    public CartAttributeAdapter(Context context, ArrayList<AttributeModel> list) {
        mContext = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CartAttributeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_attribute_in_cart, viewGroup, false);
        return new CartAttributeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAttributeAdapter.ViewHolder viewHolder, int i) {
        viewHolder.bind(viewHolder);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnItemClickListener {
        public TextView et_attribute_name;
        public TextView et_attribute_price;

        public ViewHolder(View v) {
            super(v);
            et_attribute_name = v.findViewById(R.id.et_attribute_name);
            et_attribute_price = v.findViewById(R.id.et_attribute_price);
        }

        public void bind(final CartAttributeAdapter.ViewHolder holder) {
            Log.e("@@@@@",""+list.get(getAdapterPosition()).getAttribute_name()+"::"+String.format("%.02f", Float.parseFloat(list.get(getAdapterPosition()).getAttribute_price())));
            et_attribute_name.setText("" + list.get(getAdapterPosition()).getAttribute_name());
            et_attribute_price.setText("$" + String.format("%.02f", Float.parseFloat(list.get(getAdapterPosition()).getAttribute_price())));
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    }
}