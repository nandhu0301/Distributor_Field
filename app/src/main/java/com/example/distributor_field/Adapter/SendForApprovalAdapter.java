package com.example.distributor_field.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.distributor_field.R;
import com.example.distributor_field.bean.CustomData;

import java.util.ArrayList;
import java.util.List;

public class SendForApprovalAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<CustomData> data = new ArrayList<>();

    public SendForApprovalAdapter(Context context, ArrayList<CustomData> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.send_for_approval, parent, false);
        }


        CustomData customData = data.get(position);
        //   String[] rowData = data.get(position);

        TextView textView1 = convertView.findViewById(R.id.textView1);
        TextView textView2 = convertView.findViewById(R.id.textView2);
        TextView textView3 = convertView.findViewById(R.id.textView3);

        textView1.setText(customData.getProduct_name());
        textView2.setText(""+customData.getProduct_quantity());
        textView3.setText("₹ "+customData.getTotal_price());

        return convertView;
    }
}
