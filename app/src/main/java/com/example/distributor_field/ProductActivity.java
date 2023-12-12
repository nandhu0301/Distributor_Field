package com.example.distributor_field;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.distributor_field.Adapter.ProductListingAdapter;
import com.example.distributor_field.Adapter.StoreListingAdapter;
import com.example.distributor_field.Constant.Constants;
import com.example.distributor_field.bean.CategoryDetails;
import com.example.distributor_field.bean.ProductDetails;
import com.example.distributor_field.bean.StoreDetails;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProductActivity extends AppCompatActivity {

    String categoryId;
    String token;
    ArrayList<ProductDetails> productDetails = new ArrayList<>();
    RecyclerView productRecyclerview;

    RelativeLayout purchasedetailslayout;

    TextView viewOrder, orderCount, orderCountlablel;
    ImageView backtocategory;
    String brandName, categoryName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        categoryId = getIntent().getStringExtra("categoryId");
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.TOKEN_SHARED_PREFERENCE, MODE_PRIVATE);
        token = sharedPreferences.getString(Constants.TOKEN, "");

        purchasedetailslayout = findViewById(R.id.purchasesheet);
        viewOrder = findViewById(R.id.viewOrder);
        orderCount = findViewById(R.id.orderCount);
        orderCountlablel = findViewById(R.id.cart_badge);
        backtocategory = findViewById(R.id.backtocategory);

        // vendorName = getIntent().getStringExtra("Vendor");
        brandName = getIntent().getStringExtra("Brand");
        categoryName = getIntent().getStringExtra("Category");




        productRecyclerview = findViewById(R.id.product_recyclerview);
        productRecyclerview.setLayoutManager(new GridLayoutManager(ProductActivity.this, 2));
        productRecyclerview.setHasFixedSize(true);
        try {
            loadProductDetails();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public void loadProductDetails() throws IOException {


        productDetails.clear();
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.domain_name+"productcollection").newBuilder();
        urlBuilder.addQueryParameter("brand", brandName);
        urlBuilder.addQueryParameter("category", categoryName);
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

                Log.d("Repom", responseBody);
                runOnUiThread(new Runnable() {
                    public void run() {
                        Gson gson = new Gson();
                        Type userListType = new TypeToken<List<ProductDetails>>() {
                        }.getType();

                        productDetails = gson.fromJson(responseBody, userListType);
                        Log.d("Product", String.valueOf(productDetails));
                        ProductListingAdapter storeListingAdapter = new ProductListingAdapter(ProductActivity.this, productDetails, purchasedetailslayout, categoryId,
                                viewOrder, orderCount, orderCountlablel, backtocategory, brandName, categoryName);
                        productRecyclerview.setLayoutManager(new LinearLayoutManager(ProductActivity.this));
                        productRecyclerview.setAdapter(storeListingAdapter);
                    }
                });
            } else {
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
