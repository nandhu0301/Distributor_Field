package com.example.distributor_field.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.distributor_field.R;
import com.example.distributor_field.bean.ClaimData;

import java.util.List;

public class InnerAdapter extends RecyclerView.Adapter<InnerAdapter.InnerViewHolder> {
    private List<List<ClaimData>> innerList;

    public InnerAdapter(List<List<ClaimData>> innerList) {
        this.innerList = innerList;
    }

    @NonNull
    @Override
    public InnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for the inner RecyclerView item
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inner_item_layout, parent, false);

        return new InnerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerViewHolder holder, int position) {
        //List<ClaimData> claimDataList = innerList.get(position);
        List<ClaimData> list = innerList.get(position);
        // Clear the TextView before setting new data
        ((InnerAdapter.InnerViewHolder) holder).createDate.setText("");
        ((InnerAdapter.InnerViewHolder) holder).paidAmount.setText("");
        ((InnerAdapter.InnerViewHolder) holder).outstandingAmount.setText("");

        // Iterate over the list and set each ClaimData value in the TextView
        for (ClaimData claimData : list) {
            ((InnerAdapter.InnerViewHolder) holder).createDate.append( claimData.getCreateDate() + "\n\n");
            ((InnerViewHolder) holder).paidAmount.append(claimData.getAmountStatus() +  "\n\n");

            ((InnerViewHolder) holder).outstandingAmount.append(claimData.getOutstandingAmount() + "\n\n");

        }



//            Log.d("GjhgSJHGSD", "" + "dd" + innerList.size() + list.size());
//

//
//        }

    }

    @Override
    public int getItemCount() {

        return innerList.size();
    }

    public class InnerViewHolder extends RecyclerView.ViewHolder {
        TextView createDate, paidAmount, outstandingAmount;

        public InnerViewHolder(@NonNull View itemView) {
            super(itemView);
            createDate = itemView.findViewById(R.id.createdDate);
            paidAmount = itemView.findViewById(R.id.paidAmount);
            outstandingAmount = itemView.findViewById(R.id.outsatndAmount);

        }
    }

}
