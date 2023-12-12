package com.example.distributor_field;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.distributor_field.Adapter.OrderHistoryAdapter;
import com.example.distributor_field.Adapter.StoreListingAdapter;
import com.example.distributor_field.Constant.Constants;
import com.example.distributor_field.bean.OrderHistory;
import com.example.distributor_field.bean.StoreDetails;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrderHistoryActivity extends AppCompatActivity {

    TextView thisWeekTextview, thisMonthTextview, thisYearTextview;
    String token;

    int getStoreId;
    ArrayList<OrderHistory> orderHistories = new ArrayList<>();
    RecyclerView orderRecyclerview;

    ImageView backToDashboard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderhistory);

        thisWeekTextview = findViewById(R.id.thisWeekTextview);
        thisMonthTextview = findViewById(R.id.thisMonthTextview);
        thisYearTextview = findViewById(R.id.thisYearTextview);
        orderRecyclerview = findViewById(R.id.orderRecyclerview);
        backToDashboard = findViewById(R.id.backToDashboard);

        backToDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), User_Dashboard_Activity.class);
                startActivity(intent);
            }
        });


        SharedPreferences sharedPreferences = getSharedPreferences(Constants.TOKEN_SHARED_PREFERENCE, MODE_PRIVATE);
        token = sharedPreferences.getString(Constants.TOKEN, "");
        SharedPreferences storeSharedPreference = getSharedPreferences("StoreDetails", Context.MODE_PRIVATE);
        getStoreId = storeSharedPreference.getInt("storeId", 0);

        loadOrderHistory(String.valueOf(getStoreId), "Week");
        thisWeekTextview.setBackgroundResource(R.color.purple_200);
        thisWeekTextview.setTextColor(getResources().getColor(R.color.white));

        thisWeekTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thisWeekTextview.setBackgroundResource(R.color.purple_200);
                thisWeekTextview.setTextColor(getResources().getColor(R.color.white));
                month();
                year();
                loadOrderHistory(String.valueOf(getStoreId), "Week");
            }
        });
        thisMonthTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thisMonthTextview.setBackgroundResource(R.color.purple_200);
                thisMonthTextview.setTextColor(getResources().getColor(R.color.white));
                week();
                year();
                loadOrderHistory(String.valueOf(getStoreId), "Month");
            }
        });
        thisYearTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thisYearTextview.setBackgroundResource(R.color.purple_200);
                thisYearTextview.setTextColor(getResources().getColor(R.color.white));
                week();
                month();
                loadOrderHistory(String.valueOf(getStoreId), "Year");
            }
        });


    }

    public void week() {
        thisWeekTextview.setBackgroundResource(R.drawable.ackround_purple);
        thisWeekTextview.setTextColor(getResources().getColor(R.color.purple_200));
    }

    public void month() {
        thisMonthTextview.setBackgroundResource(R.drawable.ackround_purple);
        thisMonthTextview.setTextColor(getResources().getColor(R.color.purple_200));
    }

    public void year() {
        thisYearTextview.setBackgroundResource(R.drawable.ackround_purple);
        thisYearTextview.setTextColor(getResources().getColor(R.color.purple_200));
    }


    public void loadOrderHistory(String storeId, String filter) {
        OkHttpClient client = new OkHttpClient();


        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.domain_name+"orderhistory").newBuilder();
        urlBuilder.addQueryParameter("storeId", storeId);
        urlBuilder.addQueryParameter("status", filter);

        String url = urlBuilder.build().toString();

// Create a GET request with the URL
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization", "Bearer " + token)
                .build();
        try {
            Response response = client.newCall(request).execute();
            // Handle the response
            if (response.isSuccessful()) {
                // storeDetails.clear();
                String responseBody = response.body().string();

                runOnUiThread(new Runnable() {
                    public void run() {
                        Gson gson = new Gson();
                        Type userListType = new TypeToken<List<OrderHistory>>() {
                        }.getType();

                        orderHistories = gson.fromJson(responseBody, userListType);
                        OrderHistoryAdapter storeListingAdapter = new OrderHistoryAdapter(OrderHistoryActivity.this, orderHistories);
                        orderRecyclerview.setLayoutManager(new LinearLayoutManager(OrderHistoryActivity.this));
                        orderRecyclerview.setAdapter(storeListingAdapter);
                    }
                });
                Log.d("Response", responseBody);
            } else {
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}