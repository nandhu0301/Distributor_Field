package com.example.distributor_field;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.distributor_field.Adapter.BrandAdapter;
import com.example.distributor_field.Adapter.CategoryAdapter;
import com.example.distributor_field.Constant.Constants;

import com.example.distributor_field.bean.Category;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CategoryActivity extends AppCompatActivity {
    String token;
    RecyclerView category_Recyclerview;
    CategoryAdapter categoryListAdapter;
    String brandName;
    ImageView backtobrand;
    TextView title;
    ArrayList<Category> categoryDetails = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor);

        backtobrand = findViewById(R.id.backtovendor);
        title = findViewById(R.id.title);
        title.setText("Category List");


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        category_Recyclerview = (RecyclerView) findViewById(R.id.category_recyclerview);
        category_Recyclerview.setLayoutManager(new GridLayoutManager(CategoryActivity.this, 1));
        category_Recyclerview.setHasFixedSize(true);
        //category_Recyclerview.setLayoutManager(new LinearLayoutManager(CategoryListActivty.this));
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.TOKEN_SHARED_PREFERENCE, MODE_PRIVATE);
        token = sharedPreferences.getString(Constants.TOKEN, "");

        brandName = getIntent().getStringExtra("Brand");


        backtobrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this, BrandActivity.class);

                startActivity(intent);
            }
        });

        loadCategory();
    }

    public void loadCategory() {
        OkHttpClient client = new OkHttpClient();


        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.domain_name+"productcollection").newBuilder();
        urlBuilder.addQueryParameter("brand", brandName);
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
                String responseBody = response.body().string();

                runOnUiThread(new Runnable() {
                    public void run() {

                        Gson gson = new Gson();
                        Type userListType = new TypeToken<List<Category>>() {
                        }.getType();

                        categoryDetails = gson.fromJson(responseBody, userListType);
                        categoryListAdapter = new CategoryAdapter(CategoryActivity.this, categoryDetails, brandName);
                        category_Recyclerview.setAdapter(categoryListAdapter);
                        categoryListAdapter.notifyDataSetChanged();
                    }
                });

            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}