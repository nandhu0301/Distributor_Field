package com.example.distributor_field.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.distributor_field.R;
import com.example.distributor_field.bean.ClaimData;
import com.example.distributor_field.bean.dataCollection;

import java.util.List;

public class OutstandingProducts extends RecyclerView.Adapter<OutstandingProducts.OuterViewHolder> {
    private List<com.example.distributor_field.bean.OutstandingProducts> outstandingProductsList;
    Context context;

    public OutstandingProducts(Context context, List<com.example.distributor_field.bean.OutstandingProducts> outstandingProductsList) {
        this.outstandingProductsList = outstandingProductsList;
        this.context = context;
    }

    @NonNull
    @Override
    public OuterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for the outer RecyclerView item
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.outstanding_products_footer, parent, false);

        return new OuterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OuterViewHolder holder, int position) {
        com.example.distributor_field.bean.OutstandingProducts dataCollection = outstandingProductsList.get(position);

        ((OuterViewHolder) holder).outStandingProductName.setText(dataCollection.getProductName());
        ((OuterViewHolder) holder).outstandingProductQuantity.setText(dataCollection.getQty());
        ((OuterViewHolder) holder).outstandingProductAmount.setText(dataCollection.getAmount());


        //innerAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return outstandingProductsList.size();
    }

    public class OuterViewHolder extends RecyclerView.ViewHolder {
        TextView outStandingProductName, outstandingProductQuantity, outstandingProductAmount;


        public OuterViewHolder(@NonNull View itemView) {
            super(itemView);
            outStandingProductName = itemView.findViewById(R.id.outstandingProductName);
            outstandingProductQuantity = itemView.findViewById(R.id.outstandingProductQuantity);
            outstandingProductAmount = itemView.findViewById(R.id.outstandingProductAmount);

        }
    }
}
