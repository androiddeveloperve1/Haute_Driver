package com.app.mylibertadriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.app.mylibertadriver.R;
import com.app.mylibertadriver.model.MainOptionModel;
import com.app.mylibertadriver.model.SubOptionModel;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class OptionDetailAdapter extends RecyclerView.Adapter<OptionDetailAdapter.ViewHolder> {

    private Context mContext;
    private MainOptionModel mainOptionModel;

    public OptionDetailAdapter(Context context, MainOptionModel mainOptionModel) {

        mContext = context;
        this.mainOptionModel = mainOptionModel;
    }

    @NonNull
    @Override
    public OptionDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.option_details_item, viewGroup, false);
        return new OptionDetailAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OptionDetailAdapter.ViewHolder viewHolder, int i) {
        SubOptionModel attributeModel = mainOptionModel.getSuboptions().get(i);
        viewHolder.et_attribute_name.setText(attributeModel.getName());
        viewHolder.et_attribute_price.setText("$" + String.format("%.02f", Float.parseFloat(attributeModel.getBestPrice())));


    }

    @Override
    public int getItemCount() {
        return mainOptionModel.getSuboptions().size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView img_check;
        public TextView et_attribute_name;
        public TextView et_attribute_price;
        public ConstraintLayout main;

        public ViewHolder(View v) {
            super(v);
            img_check = v.findViewById(R.id.img_check);
            et_attribute_name = v.findViewById(R.id.et_attribute_name);
            et_attribute_price = v.findViewById(R.id.et_attribute_price);
            main = v.findViewById(R.id.main);
        }


    }
}