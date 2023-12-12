package com.example.distributor_field.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.distributor_field.Constant.Constants;
import com.example.distributor_field.R;
import com.example.distributor_field.bean.OrderHistory;

import java.util.ArrayList;

public class OutstandingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    ArrayList<com.example.distributor_field.bean.dataCollection> dataCollectionArrayList = new ArrayList<>();
    String token;


    public OutstandingAdapter(Context context, ArrayList<com.example.distributor_field.bean.dataCollection> dataCollectioArrayList) {
        this.context = context;
        this.dataCollectionArrayList = dataCollectioArrayList;


        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.TOKEN_SHARED_PREFERENCE, MODE_PRIVATE);
        token = sharedPreferences.getString(Constants.TOKEN, "");


    }

    @NonNull
    @Override
    public OutstandingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.oustanding_bill_layout, parent, false);
        return new OutstandingAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {

        com.example.distributor_field.bean.dataCollection dataCollection = dataCollectionArrayList.get(position);

        //  ((OutstandingAdapter.ViewHolder) viewHolder).bilNumberTxt.setText("" + orderHistory.getBillNo());
        ((ViewHolder) viewHolder).outBillNumber.setText("Bill No \n" + dataCollection.getBillNo());
        ((ViewHolder) viewHolder).outPurchasedAmount.setText("Purchased Amount \n ₹" + dataCollection.getPurchasedAmount());
        ((ViewHolder) viewHolder).outBalancedAmounnt.setText("Balance Amount \n ₹"+ dataCollection.getBalanceAmount());
        // ((ViewHolder) viewHolder).invoiceTxt.setText("Link");
    }

    @Override
    public int getItemCount() {
        return dataCollectionArrayList.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        TextView outBillNumber, outPurchasedAmount, outBalancedAmounnt;


        public ViewHolder(View convertView) {
            super(convertView);
            outBillNumber = convertView.findViewById(R.id.outBillumber);
            outPurchasedAmount = convertView.findViewById(R.id.outPurchasedAmount);
            outBalancedAmounnt = convertView.findViewById(R.id.outBalancedAmount);


        }
    }
}
