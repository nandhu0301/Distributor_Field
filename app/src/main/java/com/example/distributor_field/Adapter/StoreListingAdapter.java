package com.example.distributor_field.Adapter;

import static android.content.Context.MODE_PRIVATE;

import static com.example.distributor_field.Constant.TimeUtil.fetchInternetTime;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.distributor_field.CheckInActivity;
import com.example.distributor_field.Constant.Constants;
import com.example.distributor_field.Constant.TimeUtil;
import com.example.distributor_field.OrderHistoryActivity;
import com.example.distributor_field.R;
import com.example.distributor_field.User_Dashboard_Activity;
import com.example.distributor_field.bean.StoreDetails;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StoreListingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    ArrayList<StoreDetails> storeDetails = new ArrayList<>();
    String token;
    String threeDigitday;
    String selectedDay;
    Activity activity;
    ProgressDialog progressdialog;
    String address_latitude = "", address_longitude = "";


    public StoreListingAdapter(Context context, Activity activity, ArrayList<StoreDetails> storeDetails, String threeDigitday, String selectedDay) {
        this.context = context;
        this.storeDetails = storeDetails;
        this.threeDigitday = threeDigitday;
        this.selectedDay = selectedDay;
        this.activity = activity;

        checkPermission();
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.TOKEN_SHARED_PREFERENCE, MODE_PRIVATE);
        token = sharedPreferences.getString(Constants.TOKEN, "");


    }

    @NonNull
    @Override
    public StoreListingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.store_listing_layout, parent, false);
        return new StoreListingAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        StoreDetails storeDetailsobj = storeDetails.get(position);

        ((ViewHolder) viewHolder).storeName.setText(storeDetailsobj.getStore_name());
        ((ViewHolder) viewHolder).storeAddress.setText(storeDetailsobj.getStore_address());

        if (threeDigitday.equalsIgnoreCase(selectedDay)) {
            ((ViewHolder) viewHolder).checkIn.setVisibility(View.VISIBLE);
        } else {
            ((ViewHolder) viewHolder).checkIn.setVisibility(View.INVISIBLE);
        }

        ((ViewHolder) viewHolder).orderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OrderHistoryActivity.class);
                SharedPreferences sharedPreferences = context.getSharedPreferences("StoreDetails", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("storeId", storeDetailsobj.getId());
                editor.apply();
                context.startActivity(intent);
            }
        });
        Log.e("storeDetailsobj", String.valueOf(storeDetailsobj.getCheckin()));

        if (storeDetailsobj.getCheckin() == 1){
            ((ViewHolder) viewHolder).checkIn.setVisibility(View.GONE);
            ((ViewHolder) viewHolder).detail_shownBtn.setVisibility(View.VISIBLE);
        }else {
            ((ViewHolder) viewHolder).checkIn.setVisibility(View.VISIBLE);
            ((ViewHolder) viewHolder).detail_shownBtn.setVisibility(View.GONE);
        }

        ((ViewHolder) viewHolder).detail_shownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, CheckInActivity.class);
                SharedPreferences sharedPreferences = context.getSharedPreferences("StoreDetails", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("storeId", storeDetailsobj.getId());
                editor.apply();
                context.startActivity(intent);
            }
        });


        ((ViewHolder) viewHolder).checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (isLocationServiceEnabled(context)) {
                    progressdialog = new ProgressDialog(context, R.style.AppCompatAlertDialogStyle);
                    progressdialog.setMessage("Loading");
                    progressdialog.setCanceledOnTouchOutside(false);
                    progressdialog.show();


                     int storeId = storeDetailsobj.getId();
                    String storename = storeDetailsobj.getStore_name();
                    String storeownername = storeDetailsobj.getStore_owner_name();
                    String storephoneNumber = storeDetailsobj.getPhone_no();

                     /*if(storeDetailsobj.getLatitude() !=null) {

                          storelatitude = storeDetailsobj.getLatitude();
                     }else {
                          storelatitude = "0";
                     }

                    if(storeDetailsobj.getLongitude() !=null) {

                        storelongtitude = storeDetailsobj.getLongitude();
                    }else {
                        storelongtitude = "0";
                    }*/

                    if (storeDetailsobj.getLocation_details() != null){

                        String storelatitude = storeDetailsobj.getLatitude();
                        String storelongtitude = storeDetailsobj.getLongitude();

                        getcurrentlocation(String.valueOf(storeId),storelatitude,storelongtitude);


                    }else {
                        progressdialog.dismiss();
                      //  Toast.makeText(context, "ensure location capture", Toast.LENGTH_SHORT).show();
                        if (context instanceof User_Dashboard_Activity) {
                            ((User_Dashboard_Activity)context).updatelocation(storeId,storename,storeownername,storephoneNumber);
                        }


                    }



                }else {
                    Toast.makeText(context, "Please enable your Location", Toast.LENGTH_SHORT).show();
                }


                /*if (storeDetailsobj.getLatitude() != null) {
                    Intent intent = new Intent(context, CheckInActivity.class);
                    SharedPreferences sharedPreferences = context.getSharedPreferences("StoreDetails", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("storeId", storeDetailsobj.getId());
                    editor.apply();
                    context.startActivity(intent);
                } else {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                        checkPermission();
                    } else {
                        getcurrentlocation(String.valueOf(storeDetailsobj.getId()));

                    }


                }*/


//                if (storeDetailsobj.getLatitude().equals(null))
//                {
//
//                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return storeDetails.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        TextView storeName, storeAddress;
        Button checkIn,detail_shownBtn;
        Button orderHistory;


        public ViewHolder(View convertView) {
            super(convertView);
            storeName = convertView.findViewById(R.id.store_name_txtview);
            storeAddress = convertView.findViewById(R.id.store_address_textview);
            checkIn = convertView.findViewById(R.id.check_in_button);
            detail_shownBtn = convertView.findViewById(R.id.shown_button);
            orderHistory = convertView.findViewById(R.id.orderHistory);

        }
    }

    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {//Can add more as per requirement

            ActivityCompat.requestPermissions(activity,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);

        }
        return;
    }

    @SuppressLint("MissingPermission")
    public void getcurrentlocation(String storeId,String storeLatitude,String storeLongtitude) {


        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        LocationServices.getFusedLocationProviderClient(context)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(context)
                                .removeLocationUpdates(this);

                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int lastlocation = locationResult.getLocations().size() - 1;
                            double latitude = locationResult.getLocations().get(lastlocation).getLatitude();
                            double longtitude = locationResult.getLocations().get(lastlocation).getLongitude();

                            address_latitude = String.valueOf(latitude);
                            address_longitude = String.valueOf(longtitude);

                            Geocoder geocoder = new Geocoder(context); // Initialize the Geocoder
                            List<Address> addresses = null; // Get the address
                            try {
                                addresses = geocoder.getFromLocation(latitude, longtitude, 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            double startLatitude = Double.parseDouble(storeLatitude);
                            double startLongitude = Double.parseDouble(storeLongtitude); // Longitude of the starting location

                            Log.e("startLatitude_locationlatitude", String.valueOf(startLatitude));
                            Log.e("startLongitude_locationlongitude", String.valueOf(startLongitude));

                            double endLatitude = latitude; // Latitude of the ending location
                            double endLongitude = longtitude; // Longitude of the ending location

                            Log.e("assigned_locationlatitude", String.valueOf(latitude));
                            Log.e("assigned_locationlongitude", String.valueOf(longtitude));


                            if (addresses != null && !addresses.isEmpty()) {
                                Address address = addresses.get(0);
                                String addressLine = address.getAddressLine(0);


                                double distance = calculateDistance(startLatitude, startLongitude, endLatitude, endLongitude);

                                TimeUtil.fetchInternetDateTime(new TimeUtil.DateTimeListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.N)
                                    @Override
                                    public void onDateTimeFetched(String dateTime) {

                                        String Datetime = dateTime;
                                        String[] parts = Datetime.split("T");
                                        String currentdate = parts[0]; // "2023-07-10"
                                        String time = parts[1];
                                        String currenttime = fetchInternetTime(time);
                                        Log.e("currenttime", currenttime);
                                        Log.d("DateTimedateTime", currentdate);


                                        new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {

                                                    OkHttpClient client = new OkHttpClient();
                                                    FormBody.Builder formBuilder = new FormBody.Builder()
                                                            .add("Storeid", storeId)
                                                            .add("CurrentDate", currentdate)
                                                            .add("status", "true")
                                                            .add("CurrentTime", currenttime)
                                                            .add("Latitude", String.valueOf(endLatitude))
                                                            .add("Longitude", String.valueOf(endLongitude))
                                                             .add("LocationCode", addressLine)
                                                            .add("Distance", String.valueOf(distance))
                                                            .add("Difference", "0");


                                                    RequestBody requestBody = formBuilder.build();

                                                    Request request = new Request.Builder()
                                                            .url(Constants.domain_name+"checkin")
                                                            .post(requestBody)
                                                            .addHeader("Authorization", "Bearer " + token)
                                                            .build();

                                                    try (Response response1 = client.newCall(request).execute()) {
                                                        String responseData1 = response1.body().string();

                                                        Intent intent = new Intent(context, CheckInActivity.class);
                                                        SharedPreferences sharedPreferences = context.getSharedPreferences("StoreDetails", Context.MODE_PRIVATE);
                                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                                        editor.putInt("storeId", Integer.parseInt(storeId));
                                                        editor.apply();
                                                        context.startActivity(intent);

                                                        progressdialog.dismiss();
                                                        Log.e("responseData1Response", responseData1 + "" + storeId);

                                                    } catch (IOException e) {

                                                        progressdialog.dismiss();
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }, 2000);



                                    }

                                    @Override
                                    public void onDateTimeFetchFailed() {
                                        Log.d("DateTime", "Fetch failed");
                                    }
                                });












                            } else {
                                // No address found
                            }


                        }
                    }
                }, Looper.getMainLooper());


    }


    //calculate distance


    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {

        int earthRadius = 6371;
        // Convert degrees to radians
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // Calculate the differences between coordinates
        double latDiff = lat2Rad - lat1Rad;
        double lonDiff = lon2Rad - lon1Rad;

        // Apply the Haversine formula
        double a = Math.pow(Math.sin(latDiff / 2), 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.pow(Math.sin(lonDiff / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Calculate the distance in kilometers
        double distance = earthRadius * c;

        // Convert distance to meters
        distance *= 1000.0;

        return distance;
    }




    public boolean isLocationServiceEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean locationEnabled = gpsEnabled || networkEnabled;

        int locationMode;
        try {
            locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        return locationEnabled && locationMode != Settings.Secure.LOCATION_MODE_OFF;
    }
}
