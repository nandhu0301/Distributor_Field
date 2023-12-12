package com.example.distributor_field.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.distributor_field.CategoryActivity;
import com.example.distributor_field.Constant.Constants;
import com.example.distributor_field.R;
import com.example.distributor_field.SendApprovalActivity;
import com.example.distributor_field.bean.CustomData;
import com.example.distributor_field.bean.ProductDetails;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;

public class ProductListingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<CustomData> dataList = new ArrayList<CustomData>();
    ;
    private Context context;
    ArrayList<ProductDetails> storeDetails = new ArrayList<>();
    String token;
    RelativeLayout purchasedetailslayout;
    String categoryId;
    TextView viewOrder, orderCount, orderCountLabel;
    ImageView backToCategory;
    HashSet<String> uniqueFirstParameters = new HashSet<>();
    ArrayList<CustomData> uniqueItemList = new ArrayList<>();
    ArrayList<CustomData> customDataList = new ArrayList<>();
    String size;
    String brandName, categoryName;

    public ProductListingAdapter(Context context, ArrayList<ProductDetails> storeDetails, RelativeLayout purchasedetailslayout, String categoryId,
                                 TextView viewOrder, TextView orderCount, TextView orderCountLabel, ImageView backToCategory, String brandName, String categoryName) {
        this.context = context;
        this.storeDetails = storeDetails;
        this.purchasedetailslayout = purchasedetailslayout;
        this.categoryId = categoryId;
        this.viewOrder = viewOrder;
        this.orderCount = orderCount;
        this.orderCountLabel = orderCountLabel;
        this.backToCategory = backToCategory;
        this.brandName = brandName;
        this.categoryName = categoryName;


        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.TOKEN_SHARED_PREFERENCE, MODE_PRIVATE);
        token = sharedPreferences.getString(Constants.TOKEN, "");
        SharedPreferences sharedPreferences1 = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String json = sharedPreferences1.getString("dataList", "");
        size = sharedPreferences1.getString("count", "");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<CustomData>>() {
        }.getType();

        if (gson.fromJson(json, type) != null) {

            dataList = gson.fromJson(json, type);
        }

        backToCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                String json = gson.toJson(dataList);
                SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("dataList", json);
                editor.putString("count", String.valueOf(dataList.size()));
                editor.apply();
                Intent intent = new Intent(context, CategoryActivity.class);
                intent.putExtra("Brand", brandName);
                intent.putExtra("Category", categoryName);
                context.startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public ProductListingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.product_list, parent, false);
        return new ProductListingAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {


        ProductDetails storeDetailsobj = storeDetails.get(position);

        if (dataList != null && dataList.size() > 0) {

            for (int i = 0; i < dataList.size(); i++) {
                CustomData customData = dataList.get(i);
                if (customData.getProduct_name().equals(storeDetailsobj.getProductName())) {
                    ((ViewHolder) viewHolder).quantity.setText("" + customData.getProduct_quantity());
                }
            }
            purchasedetailslayout.setVisibility(View.VISIBLE);
            orderCount.setText("" + size);
            orderCountLabel.setText("" + size);

//

        } else {
            purchasedetailslayout.setVisibility(View.INVISIBLE);
            orderCount.setText("0");
            orderCountLabel.setText("0");
        }


        ((ProductListingAdapter.ViewHolder) viewHolder).productName.setText(storeDetailsobj.getProductName());
        ((ViewHolder) viewHolder).sellingPrice.setText("₹ " + storeDetailsobj.getProductSellingPrice());
        ((ViewHolder) viewHolder).availableStock.setText("" + storeDetailsobj.getProductQuantity());


        viewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Gson gson = new Gson();
                String json = gson.toJson(dataList);

                SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("dataList", json);
                editor.putString("count", String.valueOf(dataList.size()));
                editor.apply();

                Intent intent = new Intent(context, SendApprovalActivity.class);
                //intent.putExtra("categoryId", categoryId);
                intent.putExtra("Brand", brandName);
                intent.putExtra("Category", categoryName);
                context.startActivity(intent);
            }
        });
        ((ViewHolder) viewHolder).removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dataList != null && dataList.size() > 0) {
                    for (int i = 0; i < dataList.size(); i++) {
                        CustomData item1 = dataList.get(i);
                        if (item1.getProduct_name().equals(storeDetailsobj.getProductName())) {
                            dataList.remove(i);
                            break;
                        }
                    }
                } else {
                    Toast.makeText(context, "No Products Added to remove", Toast.LENGTH_SHORT).show();
                }

                ((ViewHolder) viewHolder).quantity.setText("");

                if (dataList.size() > 0) {
                    purchasedetailslayout.setVisibility(View.VISIBLE);
                    orderCount.setText("" + dataList.size());
                    orderCountLabel.setText("" + dataList.size());
                } else {
                    purchasedetailslayout.setVisibility(View.INVISIBLE);
                }
            }
        });

        ((ViewHolder) viewHolder).addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String qtyEdt = ((ViewHolder) viewHolder).quantity.getText().toString();

                if (qtyEdt.equals("") || qtyEdt == null) {
                    ((ViewHolder) viewHolder).quantity.setError("Required");
                    return;
                } else if (qtyEdt.startsWith("0")) {
                    ((ViewHolder) viewHolder).quantity.setError("Quantity ");
                    if (((ViewHolder) viewHolder).quantity.length() > 0) {
                        ((ViewHolder) viewHolder).quantity.setText(qtyEdt.substring(1));
                        return;
                    } else {
                        ((ViewHolder) viewHolder).quantity.setText("");
                        return;
                    }
                }
                int count = 0;
                int res = Integer.parseInt(((ViewHolder) viewHolder).quantity.getText().toString());
                int basicPrice = (res * storeDetailsobj.getProductSellingPrice());
                if (dataList != null && dataList.size() > 0) {
                    for (int i = 0; i < dataList.size(); i++) {
                        CustomData item1 = dataList.get(i);
                        if (item1.getProduct_name().equals(storeDetailsobj.getProductName())) {
                            dataList.remove(i);
                            dataList.add(new CustomData(storeDetailsobj.getProductId(), storeDetailsobj.getProductName(), res, storeDetailsobj.getProductSellingPrice(), basicPrice));
                            break;
                        } else {
                            count = count + 1;
                        }
                    }
                    if (count == dataList.size()) {
                        dataList.add(new CustomData(storeDetailsobj.getProductId(), storeDetailsobj.getProductName(), storeDetailsobj.getProductSellingPrice(), res, basicPrice));
                    }
                } else {
                    dataList.add(new CustomData(storeDetailsobj.getProductId(), storeDetailsobj.getProductName(), res, storeDetailsobj.getProductSellingPrice(), basicPrice));
                }

                if (dataList.size() > 0) {
                    purchasedetailslayout.setVisibility(View.VISIBLE);
                    orderCount.setText("" + dataList.size());
                    orderCountLabel.setText("" + dataList.size());
                } else {
                    purchasedetailslayout.setVisibility(View.INVISIBLE);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return storeDetails.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {


        TextView productName, sellingPrice, availableStock;
        EditText quantity;
        Button addButton;
        Button removeButton;


        //  RelativeLayout purchasedetailslayout;


        public ViewHolder(View convertView) {
            super(convertView);
            productName = convertView.findViewById(R.id.productName);
            sellingPrice = convertView.findViewById(R.id.sellingPrice);
            availableStock = convertView.findViewById(R.id.availableStock);
            quantity = convertView.findViewById(R.id.quantity);
            addButton = convertView.findViewById(R.id.addButton);
            removeButton = convertView.findViewById(R.id.removeButton);

//            purchasedetailslayout=convertView.findViewById(R.id.purchasesheet);


        }
    }

}