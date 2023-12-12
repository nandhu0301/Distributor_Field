package com.example.distributor_field;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.distributor_field.Adapter.OuterAdapter1;
import com.example.distributor_field.Constant.Constants;
import com.example.distributor_field.bean.DataModel;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CheckInActivity extends AppCompatActivity {

    TextView storeName, storeAddress;
    ImageView storeImage;

    int storeId;
    String token;
    Button newOrder;
    ImageView backToDashboard;
    TextView outStandingTextviewAmount, outstandingBillTextview;

    RecyclerView outstandingRecyclerview;
    ArrayList<com.example.distributor_field.bean.dataCollection> orderHistories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        storeName = findViewById(R.id.storeNameTxt);
        storeAddress = findViewById(R.id.storeAddressTxt);
        storeImage = findViewById(R.id.imageView);
        newOrder = findViewById(R.id.newOrder);
        backToDashboard = findViewById(R.id.backToDashboard);
        outStandingTextviewAmount = findViewById(R.id.outstandingamount);
        outstandingBillTextview = findViewById(R.id.outstandingbill);
        outstandingRecyclerview = findViewById(R.id.outstandingRecyclerview);


        //get Store ID
        SharedPreferences storeSharedPreferences = getSharedPreferences("StoreDetails", MODE_PRIVATE);
        storeId = storeSharedPreferences.getInt("storeId", 0);



        SharedPreferences sharedPreferences = getSharedPreferences(Constants.TOKEN_SHARED_PREFERENCE, MODE_PRIVATE);
        token = sharedPreferences.getString(Constants.TOKEN, "");

        loadOustandingBills();
        backToDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), User_Dashboard_Activity.class);
                startActivity(intent);
            }
        });

        newOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), BrandActivity.class);
                startActivity(intent);
            }
        });


        //load store details

        OkHttpClient client = new OkHttpClient();

        String url = Constants.domain_name+"retailer/" + storeId; // Replace with your API endpoint

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization", "Bearer " + token)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                runOnUiThread(new Runnable() {
                    public void run() {

                        try {
                            JSONObject jsonObject = new JSONObject(responseBody).getJSONObject("data");
                            if (!(CheckInActivity.this).isFinishing()) {
                                storeName.setText(jsonObject.getString("store_name"));
                                storeAddress.setText(jsonObject.getString("store_address"));
                                Glide.with(CheckInActivity.this).load(jsonObject.getString("store_image")).into(storeImage);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadOustandingBills() {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.domain_name+"outstanding").newBuilder();
        urlBuilder.addQueryParameter("storeId", "8");// String.valueOf(storeId));
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
            Log.d("ressss", response.toString());
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                Log.d("Response", responseBody);

                try {
                    JSONObject jsonObject = new JSONObject(responseBody);

                    outstandingBillTextview.setText(jsonObject.getString("BillCount") + " Bills");
                    outStandingTextviewAmount.setText("₹ " + jsonObject.getString("StoreOutStanding"));

                    runOnUiThread(new Runnable() {
                        public void run() {

                            Gson gson = new Gson();
                            DataModel dataModel = gson.fromJson(responseBody, DataModel.class);
                            List<com.example.distributor_field.bean.dataCollection> dataCollectionList = dataModel.getDataCollection();
                            OuterAdapter1 outerAdapter = new OuterAdapter1(CheckInActivity.this, dataCollectionList);
                            outstandingRecyclerview.setLayoutManager(new LinearLayoutManager(CheckInActivity.this));
                            outstandingRecyclerview.setAdapter(outerAdapter);
                        }
                    });
                    Log.d("outstanding", jsonObject.getString("dataCollection"));


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), User_Dashboard_Activity.class);
        startActivity(intent);
        super.onBackPressed();
    }

}