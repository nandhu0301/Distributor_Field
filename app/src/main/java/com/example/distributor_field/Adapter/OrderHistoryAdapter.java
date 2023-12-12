package com.example.distributor_field.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.distributor_field.Constant.ColorUtils;
import com.example.distributor_field.Constant.Constants;
import com.example.distributor_field.ProductActivity;
import com.example.distributor_field.R;
import com.example.distributor_field.bean.Category;
import com.example.distributor_field.bean.OrderHistory;

import java.util.ArrayList;

public class OrderHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    ArrayList<OrderHistory> orderHistoryArrayList = new ArrayList<>();
    String token;


    public OrderHistoryAdapter(Context context, ArrayList<OrderHistory> orderHistoryArrayList) {
        this.context = context;
        this.orderHistoryArrayList = orderHistoryArrayList;


        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.TOKEN_SHARED_PREFERENCE, MODE_PRIVATE);
        token = sharedPreferences.getString(Constants.TOKEN, "");


    }

    @NonNull
    @Override
    public OrderHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.order_his_inside_layout, parent, false);
        return new OrderHistoryAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {

        OrderHistory orderHistory = orderHistoryArrayList.get(position);
        Log.d("TESTTNEW::", "" + orderHistoryArrayList.get(position).getBillNo() + orderHistory.getBillNo());
        ((ViewHolder) viewHolder).bilNumberTxt.setText("" + orderHistory.getBillNo());
        ((ViewHolder) viewHolder).amountTxt.setText("₹ " + orderHistory.getTotalPrice());
        ((ViewHolder) viewHolder).customerTxt.setText("₹ " + orderHistory.getStoreName());
        Log.d("PDF", orderHistory.getInvoiceUrl());
        ((ViewHolder) viewHolder).invoiceTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPdfWithExternalApp(orderHistory.getInvoiceUrl());
            }
        });


        // ((ViewHolder) viewHolder).invoiceTxt.setText("Link");
    }

    @Override
    public int getItemCount() {
        return orderHistoryArrayList.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        TextView bilNumberTxt, customerTxt, amountTxt, invoiceTxt;


        public ViewHolder(View convertView) {
            super(convertView);
            bilNumberTxt = convertView.findViewById(R.id.billNumberTxt);
            customerTxt = convertView.findViewById(R.id.customerTxt);
            amountTxt = convertView.findViewById(R.id.amountTxt);
            invoiceTxt = convertView.findViewById(R.id.invoiceTxt);


        }
    }

    private void openPdfWithExternalApp(String pdfUrl) {
        Uri uri = Uri.parse(pdfUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Handle the case when no PDF viewer app is installed
            e.printStackTrace();
        }
    }

}
