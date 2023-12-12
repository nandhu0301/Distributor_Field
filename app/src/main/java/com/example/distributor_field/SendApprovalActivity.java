package com.example.distributor_field;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.distributor_field.Adapter.SendForApprovalAdapter;
import com.example.distributor_field.Constant.Constants;
import com.example.distributor_field.bean.CustomData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendApprovalActivity extends AppCompatActivity {

    ListView listView;
    ImageView backtoproduct;
    int categoryId;
    Button sendApproval;
    String json;
    String token;
    int totalAmount;
    String brandName, categoryName;
    int getStoreId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_approval);

        listView = findViewById(R.id.listView);
        backtoproduct = findViewById(R.id.backtoproduct);
        sendApproval = findViewById(R.id.sendApproval);

        brandName = getIntent().getStringExtra("Brand");
        categoryName = getIntent().getStringExtra("Category");


        SharedPreferences sharedPreferences1 = getSharedPreferences(Constants.TOKEN_SHARED_PREFERENCE, MODE_PRIVATE);
        token = sharedPreferences1.getString(Constants.TOKEN, "");
        categoryId = getIntent().getIntExtra("categoryId", 0);


        SharedPreferences storeSharedPreference = getSharedPreferences("StoreDetails", Context.MODE_PRIVATE);
        getStoreId = storeSharedPreference.getInt("storeId", 0);


        // Retrieve the JSON string from SharedPr```eferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        json = sharedPreferences.getString("dataList", "");

        Log.d("Json", json+"op"+getStoreId);


// Convert the JSON string back to ArrayList using Gson
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<CustomData>>() {
        }.getType();
        ArrayList<CustomData> dataList = gson.fromJson(json, type);

        if (dataList != null && dataList.size() > 0) {
            for (int i = 0; i < dataList.size(); i++) {
                totalAmount = totalAmount + dataList.get(i).getTotal_price();
            }
        }


        SendForApprovalAdapter adapter = new SendForApprovalAdapter(this, dataList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
//        if (adapter != null) {
//            int totalHeight = 0;
//
//            for (int i = 0; i < adapter.getCount(); i++) {
//                View listItem = adapter.getView(i, null, listView);
//                listItem.measure(0, 0);
//                totalHeight += listItem.getMeasuredHeight();
//            }
//
//            ViewGroup.LayoutParams params = listView.getLayoutParams();
//            params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
//            listView.setLayoutParams(params);
//            listView.requestLayout();
//            listView.setAdapter(adapter);
//            adapter.notifyDataSetChanged();
//
//        }

        backtoproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProductActivity.class);
                intent.putExtra("Brand", brandName);
                intent.putExtra("Category", categoryName);
                startActivity(intent);
            }
        });

        sendApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkHttpClient client = new OkHttpClient();
                FormBody.Builder formBuilder = new FormBody.Builder()
                        .add("storeId", String.valueOf(getStoreId))
                        .add("collection", json)
                        .add("total_price", String.valueOf(totalAmount));


// Step 3: Build the request body with the FormData and any additional parameters
                RequestBody requestBody = formBuilder.build();
                Request request = new Request.Builder()
                        .url(Constants.domain_name+"sendapproval")
                        .addHeader("Authorization", "Bearer " + token)
                        .addHeader("Accept","application/json")
                        .post(requestBody)
                        .build();

// Step 4: Send the POST request and handle the response
                try {
                    Response response = client.newCall(request).execute();
                    Log.d("storeResponsee", String.valueOf(response));
                    if (response.isSuccessful()) {
                        // Handle successful response
                        String responseBody = response.body().string();
                        Log.d("storeResponsee", responseBody);
                        SweetAlertDialog dialog = new SweetAlertDialog(SendApprovalActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Success")
                                .setContentText("Order Sent for approval").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.clear();
                                        editor.commit();

                                        Intent intent = new Intent(getApplicationContext(), User_Dashboard_Activity.class);
                                        startActivity(intent);
                                    }
                                });

                        dialog.setCancelable(false);
                        dialog.show();


                        // Process the response as needed
                    } else {
                        // Handle error response
                        // You can get the error code and message using response.code() and response.message()
                    }
                } catch (IOException e) {
                    // Handle exception
                }


//                RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("collection", String.valueOf(dataList)).addFormDataPart("total_price", "6000").
//
//                        build();
//
//                // Build the request
//                Request request = new Request.Builder().url("https://distributor.smiligence.in/api/salesforce/sendapproval").
//                        addHeader("Authorization", "Bearer " + token).
//                        post(requestBody).build();
//
//                try {
//
//                    Response response = client.newCall(request).execute();
//
//                    // Handle the response
//                    if (response.isSuccessful()) {
//                        // The image has been uploaded successfully
//                        String imageUrl = response.body().string();
//                        Log.d("EXT::", String.valueOf(response));
//
//                    } else {
//                        // Handle the error
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        });


    }
}