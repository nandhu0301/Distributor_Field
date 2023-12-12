package com.example.distributor_field.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.distributor_field.CategoryActivity;
import com.example.distributor_field.Constant.Constants;
import com.example.distributor_field.R;
import com.example.distributor_field.UpdatePaymentActivity;
import com.example.distributor_field.User_Dashboard_Activity;
import com.example.distributor_field.bean.Category;
import com.example.distributor_field.bean.ClaimData;
import com.example.distributor_field.bean.OutstandingProducts;
import com.example.distributor_field.bean.dataCollection;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OuterAdapter1 extends RecyclerView.Adapter<OuterAdapter1.OuterViewHolder> {
    private List<dataCollection> dataCollectionList;
    Context context;
    String token;
    ArrayList<OutstandingProducts> outstandingProductsArrayList = new ArrayList<>();


    public OuterAdapter1(Context context, List<dataCollection> dataCollectionList) {
        this.dataCollectionList = dataCollectionList;
        this.context = context;
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.TOKEN_SHARED_PREFERENCE, MODE_PRIVATE);
        token = sharedPreferences.getString(Constants.TOKEN, "");
        Log.d("Recccccc", String.valueOf(dataCollectionList));
    }

    @NonNull
    @Override
    public OuterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for the outer RecyclerView item
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.oustanding_bill_layout, parent, false);

        return new OuterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OuterViewHolder holder, int position) {
        dataCollection dataCollection = dataCollectionList.get(position);
        Log.d("dattata", String.valueOf(dataCollection.getBillNo()));
        //  ((OutstandingAdapter.ViewHolder) viewHolder).bilNumberTxt.setText("" + orderHistory.getBillNo());
        ((OuterAdapter1.OuterViewHolder) holder).outBillNumber.setText("Bill No: " + dataCollection.getBillNo());
        ((OuterAdapter1.OuterViewHolder) holder).outPurchasedAmount.setText("Purchased Amount: \n ₹" + dataCollection.getPurchasedAmount());
        ((OuterAdapter1.OuterViewHolder) holder).outBalancedAmounnt.setText("Balance Amount: \n ₹" + dataCollection.getBalanceAmount());
        List<List<ClaimData>> innerList = dataCollection.getList();

        // Create an inner adapter
        InnerAdapter innerAdapter = new InnerAdapter(innerList);
        holder.innerRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        // Set the inner adapter to the inner RecyclerView inside the outer ViewHolder
        holder.innerRecyclerView.setAdapter(innerAdapter);
        holder.clickLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                CharSequence[] items = {"View Bill", "Update Bill"};
                androidx.appcompat.app.AlertDialog.Builder dialog = new androidx.appcompat.app.AlertDialog.Builder(context);
                dialog.setTitle("Choose an action");

                dialog.setItems(items, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, final int item) {
                        if (item == 0) {
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                            final View dialogView = inflater.inflate(R.layout.outstanding_products_layout, null);

                            RecyclerView outStandingProductRecyclerview = dialogView.findViewById(R.id.outStandingProductRecyclerview);
                            TextView OutsatndingProductBillumber = dialogView.findViewById(R.id.OutsatndingProductBillumber);
                            ImageView cancel = dialogView.findViewById(R.id.cancelDialog);

                            OkHttpClient client = new OkHttpClient();

                            OutsatndingProductBillumber.setText("Bill No: " + String.valueOf(dataCollection.getBillNo()));
                            HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.domain_name+"outstanding/claim").newBuilder();
                            urlBuilder.addQueryParameter("BillNo", String.valueOf(dataCollection.getBillNo()));
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


                                    Gson gson = new Gson();
                                    Type userListType = new TypeToken<List<OutstandingProducts>>() {
                                    }.getType();
                                    outstandingProductsArrayList = gson.fromJson(responseBody, userListType);

                                    com.example.distributor_field.Adapter.OutstandingProducts categoryListAdapter = new com.example.distributor_field.Adapter.OutstandingProducts(context, outstandingProductsArrayList);
                                    outStandingProductRecyclerview.setLayoutManager(new LinearLayoutManager(context));
                                    outStandingProductRecyclerview.setAdapter(categoryListAdapter);

                                    categoryListAdapter.notifyDataSetChanged();
// Create a DividerItemDecoration instance with the desired orientation


                                }
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }

                            dialogBuilder.setView(dialogView);
                            final AlertDialog b = dialogBuilder.create();
                            b.show();
                            b.setCancelable(true);
                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    b.dismiss();
                                }
                            });
                        } else if (item == 1) {
                            Intent intent = new Intent(context, UpdatePaymentActivity.class);
                            intent.putExtra("BillNumberUpdate", dataCollection.getBillNo());
                            Log.d("Transfer", String.valueOf(dataCollection.getBillNo()));
                            context.startActivity(intent);


                        }

                    }
                });

                dialog.show();


            }

        });


        //innerAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataCollectionList.size();
    }

    public class OuterViewHolder extends RecyclerView.ViewHolder {
        TextView outBillNumber, outPurchasedAmount, outBalancedAmounnt;

        RecyclerView innerRecyclerView;
        RelativeLayout clickLayout;


        public OuterViewHolder(@NonNull View itemView) {
            super(itemView);
            outBillNumber = itemView.findViewById(R.id.outBillumber);
            outPurchasedAmount = itemView.findViewById(R.id.outPurchasedAmount);
            outBalancedAmounnt = itemView.findViewById(R.id.outBalancedAmount);
            innerRecyclerView = itemView.findViewById(R.id.innerRecyclerView);
            clickLayout = itemView.findViewById(R.id.clickLayout);

        }
    }


}
