package com.example.distributor_field;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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
    TextView outStandingTextviewAmount, outstandingBillTextview,lastPurchaseDate;

    RecyclerView outstandingRecyclerview;
    ArrayList<com.example.distributor_field.bean.dataCollection> orderHistories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);
        Log.d("Rambo","Rajaaaaaaaaaaaaaaaaaaaaaa");

        storeName = findViewById(R.id.storeNameTxt);
        storeAddress = findViewById(R.id.storeAddressTxt);
        storeImage = findViewById(R.id.imageView);
        newOrder = findViewById(R.id.newOrder);
        backToDashboard = findViewById(R.id.backToDashboard);
        outStandingTextviewAmount = findViewById(R.id.outstandingamount);
        outstandingBillTextview = findViewById(R.id.outstandingbill);
        outstandingRecyclerview = findViewById(R.id.outstandingRecyclerview);
        lastPurchaseDate=findViewById(R.id.last_purchased_date);


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

        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.domain_name+"retailer/").newBuilder(); //// Replace with your API endpoint
        urlBuilder.addQueryParameter("storeId", String.valueOf(storeId));
        String url=urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization", "Bearer " + token)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                lastPurchaseDate.setText(new JSONObject(responseBody).getString("LastPurchaseDate"));
                String outStandingamount=new JSONObject(responseBody).getString("OutstandingAmt");
                String lastDueDate=new JSONObject(responseBody).getString("LastDueDate");
                    String today= LocalDate.now().toString();

                if(!outStandingamount.equals("0")){
                    Log.d("RAmbo","First"+storeId);
                    Date date1=stringToDate(today);
                    Date date2=stringToDate(lastDueDate);
                    int result =date1.compareTo(date2);
                    if(result>0){
                        newOrder.setBackgroundColor(Color.BLACK);
                        newOrder.setText("NO NEW ORDER");
                        newOrder.setEnabled(false);
                    }

                }


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
            else {
                Log.d("Rambo",response.body().string());
                Log.d("Rambo", String.valueOf(response.code()));
            }
        } catch (IOException e) {
            Log.d("Rambo","Erorrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
            e.printStackTrace();
        } catch (JSONException e) {
            Log.d("Rambo","Erorrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Log.d("RAmbo","last");
    }

    private Date stringToDate(String date) throws ParseException {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        return  simpleDateFormat.parse(date);
    }

    public void loadOustandingBills() {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.domain_name+"outstanding").newBuilder();
        urlBuilder.addQueryParameter("storeId", String.valueOf(storeId));// String.valueOf(storeId));
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
            Log.d("Rambo", "rajaaaaaaaaaaaaaaaa");
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                Log.d("Rambo", responseBody);

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