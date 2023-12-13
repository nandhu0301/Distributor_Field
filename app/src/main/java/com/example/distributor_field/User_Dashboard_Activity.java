package com.example.distributor_field;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.distributor_field.Adapter.CategoryAdapter;
import com.example.distributor_field.Adapter.StoreListingAdapter;
import com.example.distributor_field.Constant.Constants;
import com.example.distributor_field.bean.Category;
import com.example.distributor_field.bean.StoreDetails;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class User_Dashboard_Activity extends AppCompatActivity {

    ImageView profile_image;
    TextView salesOfficerName;
    String token;
    Button create_new_store;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private final int REQUEST_CODE_GALLERY = 1;
    String imagePath;
    ImageView store_image;
    RecyclerView store_recyclerview;
    ArrayList<StoreDetails> storeDetails = new ArrayList<>();
    ArrayList<com.example.distributor_field.bean.taskcollection> taskcollectionArrayList = new ArrayList<>();
    private GoogleMap mMap;
    Button click;
    final private int REQUEST_CODE = 111;
    private FusedLocationProviderClient fusedLocationClient;
    private Geocoder geocoder;
    TextView tv_display_marker_location;
    SearchView searchView;
    Marker marker;
    double latitude, longitude;
    Button confirm_address;

    ImageView cancelMap;

    FirebaseAuth mAuth;
    TextView monTextview, tueTextview, wedTextview, thuTextview, friTextview, satTextview;
    String threeDigitDay;

    TextView currentDateTextview;
    MapView mapView;
    LinearLayout bt_logout;
    AlertDialog dialog;

    //String selectedDay;
    EditText address_txt;
    ProgressDialog progressdialog;
    String address_latitude = "", address_longitude = "", addressLine = "";

    //voice Note
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private static String mFileName = null;
    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;
    private static final int REQUEST_PERMISSION_CODE = 123;
    File file;
    boolean recordCheck = true;
    boolean play = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        profile_image = findViewById(R.id.profile_image);
        salesOfficerName = findViewById(R.id.salesOfficerName);
        create_new_store = findViewById(R.id.create_new_store);
        store_recyclerview = findViewById(R.id.store_recyclerview);
        monTextview = findViewById(R.id.monTextview);
        tueTextview = findViewById(R.id.tueTextview);
        wedTextview = findViewById(R.id.wenTextview);
        thuTextview = findViewById(R.id.thuTextview);
        friTextview = findViewById(R.id.friTextview);
        satTextview = findViewById(R.id.satTextview);
        currentDateTextview = findViewById(R.id.currentDate);
        bt_logout = findViewById(R.id.bt_logout);


        FirebaseApp.initializeApp(User_Dashboard_Activity.this);
        mAuth = FirebaseAuth.getInstance();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.TOKEN_SHARED_PREFERENCE, MODE_PRIVATE);
        token = sharedPreferences.getString(Constants.TOKEN, "");
        Log.d("Token::", token);
        // OnLoad Process

        loadSalesOfficerDetails();
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        // Format the date
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = dateFormat1.format(currentDate);

        currentDateTextview.setText(formattedDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE", Locale.getDefault());
        threeDigitDay = dateFormat.format(calendar.getTime());

        // Get the current date
//openPdfWithExternalApp("https://distributor.smiligence.in/storage/invoice_pdf/1689569618_64b4c9521d00b.pdf");

// Print the current date


        bt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(User_Dashboard_Activity.this);
                bottomSheetDialog.setContentView(R.layout.logout_confirmation);
                Button logout = bottomSheetDialog.findViewById(R.id.logout);
                Button stayinapp = bottomSheetDialog.findViewById(R.id.stayinapp);

                bottomSheetDialog.show();
                bottomSheetDialog.setCancelable(false);

                logout.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (!((Activity) User_Dashboard_Activity.this).isFinishing()) {

                                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.clear();
                                    editor.commit();

                                    SharedPreferences loginsharedPreferences = getSharedPreferences(Constants.TOKEN_SHARED_PREFERENCE, MODE_PRIVATE);
                                    SharedPreferences.Editor logineditor = loginsharedPreferences.edit();
                                    logineditor.clear();
                                    logineditor.commit();

                                    SharedPreferences sharedPreferenceBrand = getSharedPreferences("SharedPreferenceBrand", MODE_PRIVATE);
                                    SharedPreferences.Editor brandEditor = sharedPreferenceBrand.edit();
                                    brandEditor.clear();
                                    brandEditor.commit();

                                    SharedPreferences storesharedPreferences = getSharedPreferences("StoreDetails", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor storeeditor = storesharedPreferences.edit();
                                    storeeditor.clear();
                                    storeeditor.apply();

                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                                bottomSheetDialog.dismiss();
                            }
                        });
                stayinapp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });

            }
        });

        loadStoreDetails(threeDigitDay, threeDigitDay);

        if (threeDigitDay.equalsIgnoreCase("Mon")) {
            monTextview.setBackgroundResource(R.color.purple_200);
            monTextview.setTextColor(getResources().getColor(R.color.white));
            tue();
            wed();
            thu();
            fri();
            sat();


        } else if (threeDigitDay.equalsIgnoreCase("Tue")) {
            tueTextview.setBackgroundResource(R.color.purple_200);
            tueTextview.setTextColor(getResources().getColor(R.color.white));
            mon();
            wed();
            thu();
            fri();
            sat();


        } else if (threeDigitDay.equalsIgnoreCase("Wed")) {
            wedTextview.setBackgroundResource(R.color.purple_200);
            wedTextview.setTextColor(getResources().getColor(R.color.white));
            mon();
            tue();
            thu();
            fri();
            sat();


        } else if (threeDigitDay.equalsIgnoreCase("Thu")) {
            thuTextview.setBackgroundResource(R.color.purple_200);
            thuTextview.setTextColor(getResources().getColor(R.color.white));
            mon();
            tue();
            wed();
            fri();
            sat();


        } else if (threeDigitDay.equalsIgnoreCase("Fri")) {
            friTextview.setBackgroundResource(R.color.purple_200);
            friTextview.setTextColor(getResources().getColor(R.color.white));
            mon();
            tue();
            wed();
            thu();
            sat();


        } else if (threeDigitDay.equalsIgnoreCase("Sat")) {
            satTextview.setBackgroundResource(R.color.purple_200);
            satTextview.setTextColor(getResources().getColor(R.color.white));
            mon();
            tue();
            wed();
            thu();
            fri();


        }

        //add new store
        create_new_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(User_Dashboard_Activity.this);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.add_new_store_layout, null);
                dialogBuilder.setView(dialogView);

                EditText store_name = dialogView.findViewById(R.id.shopNameEdt);
                EditText store_owner_name = dialogView.findViewById(R.id.shopOwnerNameEdt);
                EditText phone_number = dialogView.findViewById(R.id.phoneNumberEdt);
                EditText alternate_phone_number = dialogView.findViewById(R.id.alternate_phone_number_edt);
                EditText store_address = dialogView.findViewById(R.id.store_address);
                EditText store_landmark = dialogView.findViewById(R.id.store_landmark);
                EditText store_gstin = dialogView.findViewById(R.id.store_gst);
                Spinner stateSpinner = dialogView.findViewById(R.id.spinner);
                Spinner Districtspinner = dialogView.findViewById(R.id.Districtspinner);
                ImageView Cancel = dialogView.findViewById(R.id.Cancel);
                store_image = dialogView.findViewById(R.id.store_image);
                Button submit_store_details = dialogView.findViewById(R.id.submitButton);
                EditText pick_address = dialogView.findViewById(R.id.pick_address);
                AutoCompleteTextView autoCompleteTextView = dialogView.findViewById(R.id.autoCompleteTextView);

                Button record = dialogView.findViewById(R.id.btnRecord);
                Button delete = dialogView.findViewById(R.id.btnDelete);
                Button playSound = dialogView.findViewById(R.id.btnPlay);
                EditText remarks = dialogView.findViewById(R.id.remarks);
                record.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (recordCheck) {
                            startRecording();
                            Toast.makeText(User_Dashboard_Activity.this, "Recording", Toast.LENGTH_SHORT).show();
                            record.setText("Stop");
//record.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.baseline_play_circle_outline_24),null,null,null);
                            recordCheck = false;
                        } else {
                            pauseRecording();
                            Toast.makeText(User_Dashboard_Activity.this, "Completed", Toast.LENGTH_SHORT).show();
                            record.setText("Record");
                            record.setVisibility(View.GONE);
                            playSound.setVisibility(View.VISIBLE);
// record.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.baseline_play_circle_outline_24),null,null,null);
                            delete.setVisibility(View.VISIBLE);
                            recordCheck = true;
                        }
                    }
                });
                playSound.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (play) {
                            playAudio();
                            playSound.setText(
                                    "Pause"
                            );
                            playSound.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.baseline_pause_circle_outline_24), null, null, null);
                            play = false;
                            if (!mPlayer.isPlaying()) {
                                playSound.setText("Play");
                                play = true;
                                playSound.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.baseline_play_circle_outline_24), null, null, null);
                            }
                        } else {
                            playSound.setText(
                                    "Play"
                            );
                            playSound.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.baseline_play_circle_outline_24), null, null, null);
                            pausePlaying();
                            play = true;
                        }
                    }
                });
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mFileName = null;
                        delete.setVisibility(View.GONE);
                        playSound.setVisibility(View.GONE);
                        record.setVisibility(View.VISIBLE);
                        play = true;
                    }
                });
                pick_address.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checkPermission();

                        AlertDialog.Builder dialogBuilder1 = new AlertDialog.Builder(User_Dashboard_Activity.this);
                        LayoutInflater inflater1 = getLayoutInflater();
                        if (!(User_Dashboard_Activity.this).isFinishing()) {
                            final View dialogView1 = inflater1.inflate(R.layout.dialog_xml, null);
                            dialogBuilder1.setView(dialogView1);

                            // Get a reference to the SupportMapFragment
                            tv_display_marker_location = dialogView1.findViewById(R.id.tv_display_marker_location);
                            searchView = dialogView1.findViewById(R.id.searchView);
                            confirm_address = dialogView1.findViewById(R.id.submit_location_button);
                            cancelMap = dialogView1.findViewById(R.id.cancelMap);
                            mapView = dialogView1.findViewById(R.id.mapContainer);
                            mapView.onCreate(savedInstanceState);
                        }


                        confirm_address.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(User_Dashboard_Activity.this, "" + latitude + longitude, Toast.LENGTH_SHORT).show();
                                pick_address.setText(tv_display_marker_location.getText().toString().trim());
                            }
                        });


                        // Set up the map and other configurations
                        mapView.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                // Customize the map as needed
                                mMap = googleMap;
                                fusedLocationClient = LocationServices.getFusedLocationProviderClient(User_Dashboard_Activity.this);
                                geocoder = new Geocoder(User_Dashboard_Activity.this, Locale.getDefault());
                                if (ActivityCompat.checkSelfPermission(User_Dashboard_Activity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(User_Dashboard_Activity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                                    checkPermission();

                                    fusedLocationClient.getLastLocation()
                                            .addOnSuccessListener(User_Dashboard_Activity.this, new OnSuccessListener<Location>() {
                                                @Override
                                                public void onSuccess(Location location) {
                                                    if (location != null) {
                                                        latitude = location.getLatitude();
                                                        longitude = location.getLongitude();
                                                        // Create LatLng object
                                                        LatLng location1 = new LatLng(latitude, longitude);

                                                        // Add marker to the location
                                                        googleMap.addMarker(new MarkerOptions().position(location1));

                                                        // Move camera to the location
                                                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location1, 18));
                                                        // Toast.makeText(MainActivity.this, "ej"+latitude, Toast.LENGTH_SHORT).show();
                                                        getAddressFromLocation(latitude, longitude);
                                                    }
                                                }
                                            });
                                    googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                                        @Override
                                        public void onCameraMove() {
                                            LatLng centerLatLng = googleMap.getCameraPosition().target;

                                            LatLng latLng = googleMap.getCameraPosition().target;

                                            if (marker == null) {
                                                MarkerOptions markerOptions = new MarkerOptions()
                                                        .position(latLng)
                                                        .title("Pick Location");
                                                marker = googleMap.addMarker(markerOptions);
                                            } else {
                                                marker.setPosition(latLng);
                                            }
                                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                                            getAddressFromLocation(centerLatLng.latitude, centerLatLng.longitude);
                                        }
                                    });

                                    return;
                                }
                                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                    @Override
                                    public boolean onQueryTextSubmit(String query) {
                                        searchLocation(query);
                                        return true;
                                    }

                                    @Override
                                    public boolean onQueryTextChange(String newText) {
                                        return false;
                                    }
                                });
                                fusedLocationClient.getLastLocation()
                                        .addOnSuccessListener(User_Dashboard_Activity.this, new OnSuccessListener<Location>() {
                                            @Override
                                            public void onSuccess(Location location) {
                                                if (location != null) {
                                                    latitude = location.getLatitude();
                                                    longitude = location.getLongitude();
                                                    // Create LatLng object
                                                    LatLng location1 = new LatLng(latitude, longitude);

                                                    // Add marker to the location
                                                    googleMap.addMarker(new MarkerOptions().position(location1));

                                                    // Move camera to the location
                                                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location1, 18));
                                                    // Toast.makeText(MainActivity.this, "ej"+latitude, Toast.LENGTH_SHORT).show();
                                                    getAddressFromLocation(latitude, longitude);
                                                }
                                            }
                                        });
                                googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                                    @Override
                                    public void onCameraMove() {
                                        LatLng centerLatLng = googleMap.getCameraPosition().target;

                                        LatLng latLng = googleMap.getCameraPosition().target;

                                        if (marker == null) {
                                            MarkerOptions markerOptions = new MarkerOptions()
                                                    .position(latLng)
                                                    .title("Pick Location");
                                            marker = googleMap.addMarker(markerOptions);
                                        } else {
                                            marker.setPosition(latLng);
                                        }
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                                        getAddressFromLocation(centerLatLng.latitude, centerLatLng.longitude);
                                    }
                                });


                            }
                        });

                        final AlertDialog b1 = dialogBuilder1.create();
                        if (!(User_Dashboard_Activity.this).isFinishing()) {
                            b1.show();
                        }
                        b1.setCancelable(false);

                        confirm_address.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(User_Dashboard_Activity.this, "" + latitude + longitude, Toast.LENGTH_SHORT).show();
                                pick_address.setText(tv_display_marker_location.getText().toString().trim());
                                b1.dismiss();
                            }
                        });


                        cancelMap.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                b1.dismiss();
                            }
                        });
                    }
                });

                //load spinner data
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(Constants.domain_name + "retailer/state-collection").header("Authorization", "Bearer " + token).build();
                try {
                    Response response = client.newCall(request).execute();
                    // Handle the response
                    if (response.isSuccessful()) {
                        // storeDetails.clear();
                        String responseString = response.body().string();

// Step 2: Parse the string into a JSON array using Gson
                        Gson gson = new Gson();
                        ArrayList<String> arrayList = gson.fromJson(responseString, new TypeToken<ArrayList<String>>() {
                        }.getType());
                        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(User_Dashboard_Activity.this, android.R.layout.simple_spinner_item, arrayList);
                        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        // Set the adapter to the spinner
                        stateSpinner.setAdapter(adapter1);
                        Log.d("RESPONSEEE::", String.valueOf(arrayList));

                    } else {
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        // Get the selected item from the first spinner
                        String selectedItem = (String) parent.getItemAtPosition(position);
                        OkHttpClient client = new OkHttpClient();

// Build the URL with parameters
                        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.domain_name + "retailer/district-collection").newBuilder();
                        urlBuilder.addQueryParameter("state", selectedItem);
                        String url = urlBuilder.build().toString();

// Create a GET request with the URL
                        Request request = new Request.Builder()
                                .url(url)
                                .get()
                                .addHeader("Authorization", "Bearer " + "5|ki0rmmvON8A7JGvWHi2Zu4K8RjCBuy70lrz5VQw1")
                                .build();

// Send the request and handle the response
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if (response.isSuccessful()) {
                                    String responseData = response.body().string();
                                    runOnUiThread(new Runnable() {
                                        public void run() {

                                            Gson gson = new Gson();
                                            ArrayList<String> arrayList = gson.fromJson(responseData, new TypeToken<ArrayList<String>>() {
                                            }.getType());
                                            ArrayAdapter<String> adapter = new ArrayAdapter<>(User_Dashboard_Activity.this, android.R.layout.simple_spinner_item, arrayList);
                                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                            // Set the adapter to the spinner
                                            Districtspinner.setAdapter(adapter);

                                        }
                                    });


                                    // Toast.makeText(User_Dashboard_Activity.this, ""+responseData, Toast.LENGTH_SHORT).show();


                                    // Process the response data as needed
                                    // Parse the JSON response or handle other types of response data
                                    // Update the UI or perform any other required operations with the retrieved data
                                } else {
                                    // Handle the error response
                                }
                            }

                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                            }
                        });

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Handle the case where no item is selected
                    }
                });


//                Districtspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                        // Get the selected item from the first spinner
//                        String selectedItem = (String) parent.getItemAtPosition(position);
//
//
//                        OkHttpClient client = new OkHttpClient();
//
//// Build the URL with parameters
//                        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://distributor.smiligence.in/api/salesforce/retailer/zone-collection").newBuilder();
//                        urlBuilder.addQueryParameter("zone", selectedItem);
//                        String url = urlBuilder.build().toString();
//
//// Create a GET request with the URL
//                        Request request = new Request.Builder()
//                                .url(url)
//                                .get()
//                                .addHeader("Authorization", "Bearer " + token)
//                                .build();
//
//// Send the request and handle the response
//                        client.newCall(request).enqueue(new Callback() {
//                            @Override
//                            public void onResponse(Call call, Response response) throws IOException {
//                                if (response.isSuccessful()) {
//                                    String responseData = response.body().string();
//                                    runOnUiThread(new Runnable() {
//                                        public void run() {
//
//                                            Gson gson = new Gson();
//                                            ArrayList<String> arrayList = gson.fromJson(responseData, new TypeToken<ArrayList<String>>() {
//                                            }.getType());
//
//
//                                            ArrayAdapter<String> adapter = new ArrayAdapter<>(User_Dashboard_Activity.this, android.R.layout.simple_dropdown_item_1line, arrayList);
//
//// Set the adapter to the AutoCompleteTextView
//                                            autoCompleteTextView.setAdapter(adapter);
//                                        }
//                                    });
//
//                                } else {
//                                    // Handle the error response
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call call, IOException e) {
//                                e.printStackTrace();
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parent) {
//                        // Handle the case where no item is selected
//                    }
//                });


                store_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestStoragePermission();
                        openGallery();
                    }
                });


                final AlertDialog b = dialogBuilder.create();
                if (!(User_Dashboard_Activity.this).isFinishing()) {
                    b.show();
                }
                b.setCancelable(false);

                Cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        b.dismiss();
                    }
                });
                submit_store_details.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String store_name_str = store_name.getText().toString().trim();
                        String store_owner_name_str = store_owner_name.getText().toString().trim();
                        String phone_number_str = phone_number.getText().toString().trim();
                        String alternate_phone_number_str = alternate_phone_number.getText().toString().trim();
                        String store_address_str = store_address.getText().toString().trim();
                        String store_landmark_str = store_landmark.getText().toString().trim();
                        String store_gstin_str = store_gstin.getText().toString().trim();
                        String pick_Address_str = pick_address.getText().toString().trim();

                        //upload functionality
                        if (store_name_str.equals("") || store_name_str.equals(null)) {
                            store_name.setError(Constants.REQUIRED);
                            return;
                        } else if (store_owner_name_str.equals("") || store_owner_name_str.equals(null)) {
                            store_owner_name.setError(Constants.REQUIRED);
                            return;
                        } else if (phone_number_str.equals("") || phone_number_str.equals(null)) {
                            phone_number.setError(Constants.REQUIRED);
                            return;
                        } else if (alternate_phone_number_str.equals("") || alternate_phone_number_str.equals(null)) {
                            alternate_phone_number.setError(Constants.REQUIRED);
                            return;
                        } else if (store_address_str.equals("") || store_address_str.equals(null)) {
                            store_address.setError(Constants.REQUIRED);
                            return;
                        } else if (pick_Address_str.equals("") || pick_Address_str.equals(null)) {
                            pick_address.setError(Constants.REQUIRED);
                            return;
                        } else if (store_landmark_str.equals("") || store_landmark_str.equals(null)) {
                            store_landmark.setError(Constants.REQUIRED);
                            return;
                        } else if (store_gstin_str.equals("") || store_gstin_str.equals(null)) {
                            store_gstin.setError(Constants.REQUIRED);
                            return;
                        } else if (imagePath == null) {
                            Toast.makeText(User_Dashboard_Activity.this, "Store Image Mandatory ", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (mFileName==null) {
                            Toast.makeText(User_Dashboard_Activity.this, "Voice Record is Mandatory", Toast.LENGTH_SHORT).show();
                        } else if (remarks.equals("")) {
                            remarks.setError(Constants.REQUIRED);
                        }


                        else {
                            uploadImage(store_name_str, store_owner_name_str, phone_number_str, alternate_phone_number_str, store_address_str, store_landmark_str, store_gstin_str, imagePath, b,
                                    stateSpinner.getSelectedItem().toString(), Districtspinner.getSelectedItem().toString(), autoCompleteTextView.getText().toString().trim(), String.valueOf(latitude), String.valueOf(longitude), pick_address.getText().toString().trim(),remarks.getText().toString().trim(),mFileName);
                        }
                    }
                });
            }
        });


        //load Store Details

        monTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monTextview.setBackgroundResource(R.color.purple_200);
                monTextview.setTextColor(getResources().getColor(R.color.white));
                tue();
                wed();
                thu();
                fri();
                sat();
                loadStoreDetails("Mon", threeDigitDay);
            }
        });

        tueTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tueTextview.setBackgroundResource(R.color.purple_200);
                tueTextview.setTextColor(getResources().getColor(R.color.white));
                mon();
                wed();
                thu();
                fri();
                sat();
                loadStoreDetails("Tue", threeDigitDay);
            }
        });

        wedTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wedTextview.setBackgroundResource(R.color.purple_200);
                wedTextview.setTextColor(getResources().getColor(R.color.white));
                mon();
                tue();
                thu();
                fri();
                sat();
                loadStoreDetails("Wed", threeDigitDay);
            }
        });

        thuTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thuTextview.setBackgroundResource(R.color.purple_200);
                thuTextview.setTextColor(getResources().getColor(R.color.white));
                mon();
                tue();
                wed();
                fri();
                sat();
                loadStoreDetails("Thu", threeDigitDay);
            }
        });

        friTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friTextview.setBackgroundResource(R.color.purple_200);
                friTextview.setTextColor(getResources().getColor(R.color.white));
                mon();
                tue();
                wed();
                thu();
                sat();
                loadStoreDetails("Fri", threeDigitDay);
            }
        });

        satTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                satTextview.setBackgroundResource(R.color.purple_200);
                satTextview.setTextColor(getResources().getColor(R.color.white));
                mon();
                tue();
                wed();
                thu();
                fri();
                loadStoreDetails("Sat", threeDigitDay);
            }
        });

        //voicemail

        RRequestPermissions();
        checkPermissions();


    }


    public void mon() {
        monTextview.setBackgroundResource(R.drawable.ackround_purple);
        monTextview.setTextColor(getResources().getColor(R.color.purple_200));
    }

    public void tue() {
        tueTextview.setBackgroundResource(R.drawable.ackround_purple);
        tueTextview.setTextColor(getResources().getColor(R.color.purple_200));
    }

    public void wed() {
        wedTextview.setBackgroundResource(R.drawable.ackround_purple);
        wedTextview.setTextColor(getResources().getColor(R.color.purple_200));
    }

    public void thu() {
        thuTextview.setBackgroundResource(R.drawable.ackround_purple);
        thuTextview.setTextColor(getResources().getColor(R.color.purple_200));
    }

    public void fri() {
        friTextview.setBackgroundResource(R.drawable.ackround_purple);
        friTextview.setTextColor(getResources().getColor(R.color.purple_200));
    }

    public void sat() {
        satTextview.setBackgroundResource(R.drawable.ackround_purple);
        satTextview.setTextColor(getResources().getColor(R.color.purple_200));
    }

    public void loadStoreDetails(String day, String threeDigitDay) {
        storeDetails.clear();

        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.domain_name + "salesbeat").newBuilder();
        urlBuilder.addQueryParameter("date", day);
        String url = urlBuilder.build().toString();
        // Create a GET request with the URL
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization", "Bearer " + token)
                .build();
        try {
            Response response = client.newCall(request).execute();

            // Toast.makeText(this, "Respose", Toast.LENGTH_SHORT).show();
            // Handle the response
            if (response.isSuccessful()) {
                // storeDetails.clear();
                String responseBody = response.body().string();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Gson gson = new Gson();
                        Type userListType = new TypeToken<List<StoreDetails>>() {
                        }.getType();

                        storeDetails = gson.fromJson(responseBody, userListType);
                        StoreListingAdapter storeListingAdapter = new StoreListingAdapter(User_Dashboard_Activity.this, User_Dashboard_Activity.this, storeDetails, threeDigitDay, day);
                        store_recyclerview.setLayoutManager(new LinearLayoutManager(User_Dashboard_Activity.this));
                        store_recyclerview.setAdapter(storeListingAdapter);
                    }
                });
            } else {
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void loadSalesOfficerDetails() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(Constants.domain_name + "user/").header("Authorization", "Bearer " + token).build();
        try {
            Response response = client.newCall(request).execute();
            // Handle the response
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                runOnUiThread(new Runnable() {
                    public void run() {

                        try {
                            JSONObject jsonObject = new JSONObject(responseBody).getJSONObject("data");
                            if (!(User_Dashboard_Activity.this).isFinishing()) {
                                salesOfficerName.setText(jsonObject.getString("name"));
                                Glide.with(User_Dashboard_Activity.this).load(jsonObject.getString("image")).into(profile_image);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            } else {
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            imagePath = getImagePath(selectedImageUri);
            if (!(User_Dashboard_Activity.this).isFinishing()) {
                Glide.with(User_Dashboard_Activity.this).load(selectedImageUri).into(store_image);
            }
            // Upload the image using OkHttp client
            // uploadImage(imagePath);
        }
    }

    private void uploadImage(String shopName, String shop_owner_name, String phoneNumber, String alternate_phone_number, String store_address, String store_landmark, String store_gstin, String imagePath, AlertDialog dialog,

                             String state, String district, String zone, String latitude, String longtitude, String address,String remarks,String audioPath) {
        //Toast.makeText(this, ""+imagePath, Toast.LENGTH_SHORT).show();
        Log.d("TEST:", imagePath);

        Log.d("Details::", "shopname" + shopName + "shopowner" + shop_owner_name + "phone" + phoneNumber + "alterphone" + alternate_phone_number + "address" + store_address + "landmark" + store_landmark + "gstin" + store_gstin + "image" + imagePath

                + "state" + state + "district" + district + "zone" + zone + "latiude" + latitude + "longtitude" + longtitude + "address" + address);


        Toast.makeText(this, "" + latitude + longtitude, Toast.LENGTH_SHORT).show();
        OkHttpClient client = new OkHttpClient();

        // Replace "YOUR_TOKEN" with the actual Bearer token
        // String token = "144|gNB8AmiHGQVCbqRzG1wuxXMm0bKEtUvrtsWXb0TG";
        File audio=new File(audioPath);

        // Create the file object from the image path
        File file = new File(imagePath);
        Log.d("File Path:", imagePath);
        // Set the media type for the image file
        MediaType mediaType = MediaType.parse("image/*");

        // Create the request body with the file as Form Data part
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("store_image", file.getName(), RequestBody.create(file, mediaType)).
                addFormDataPart("store_name", shopName).
                addFormDataPart("store_owner_name", shop_owner_name).
                addFormDataPart("store_gst_no", store_gstin).
                addFormDataPart("store_landmark", store_landmark).
                addFormDataPart("store_address", store_address).
                addFormDataPart("phone_no", phoneNumber).
                addFormDataPart("alt_phone_no", alternate_phone_number).
                addFormDataPart("state", state).addFormDataPart("district", district).addFormDataPart("zone", zone).
                addFormDataPart("latitude", latitude).
                addFormDataPart("longitude", longtitude).
                addFormDataPart("location_details", address)
                .addFormDataPart("audio", audio.getName(), RequestBody.create(audio, mediaType))
                .addFormDataPart("Remarks", remarks).
                 build();


        Request request = new Request.Builder().url(Constants.domain_name + "retailer/create").
                addHeader("Authorization", "Bearer " + token).
                post(requestBody).build();

        try {

            Response response = client.newCall(request).execute();

            Log.d("Executes:", imagePath);
            // Handle the response
            if (response.isSuccessful()) {
                // The image has been uploaded successfully
                String imageUrl = response.body().string();
                Toast.makeText(this, "Store Data Uploaded Sucessfully" + response, Toast.LENGTH_SHORT).show();
                loadStoreDetails(threeDigitDay, threeDigitDay);
                dialog.dismiss();

            } else {
                // Handle the error
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getImagePath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            imagePath = cursor.getString(columnIndex);
            cursor.close();
            return imagePath;
        }
        return null;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }


    private void getAddressFromLocation(double latitude, double longitude) {
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                String addressLine = address.getAddressLine(0);
                String city = address.getLocality();
                String state = address.getAdminArea();
                String country = address.getCountryName();
                String postalCode = address.getPostalCode();
                tv_display_marker_location.setText(addressLine);
                // Use the retrieved address information as needed
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {//Can add more as per requirement

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);

        }
        return;
    }

    private void searchLocation(String query) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(query, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                tv_display_marker_location.setText(address.getAddressLine(0));
                latitude = address.getLatitude();
                longitude = address.getLongitude();
                LatLng latLng = new LatLng(latitude, longitude);
                if (marker == null) {
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(latLng)
                            .title("Pick Location");
                    marker = mMap.addMarker(markerOptions);
                } else {
                    marker.setPosition(latLng);
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openPdfWithExternalApp(String pdfUrl) {
        Uri uri = Uri.parse(pdfUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Handle the case when no PDF viewer app is installed
            e.printStackTrace();
        }
    }

    public void updatelocation(int storeId, String storeName, String storeOwnerName, String phoneNumber) {

       // Toast.makeText(this, "" + storeId, Toast.LENGTH_SHORT).show();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(User_Dashboard_Activity.this);
        final View dialogView = LayoutInflater.from(User_Dashboard_Activity.this).inflate(R.layout.fetchlocation_layout, null);
        dialogBuilder.setView(dialogView);

        TextView updateprojectName = dialogView.findViewById(R.id.project_nametxt);
        TextView updateownerName = dialogView.findViewById(R.id.owner_Nametxt);
        TextView updateownerPhoneNumber = dialogView.findViewById(R.id.owner_phoneNumber);
        address_txt = dialogView.findViewById(R.id.address_layout);

        CardView fetchaddress = dialogView.findViewById(R.id.fetchlocation_cardview);
        CardView saveaddress = dialogView.findViewById(R.id.save_cardview);

        updateprojectName.setText(storeName);
        updateownerName.setText(storeOwnerName);
        updateownerPhoneNumber.setText(phoneNumber);

        fetchaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(User_Dashboard_Activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);

                } else {

                    if (isLocationServiceEnabled(User_Dashboard_Activity.this)) {
                        progressdialog = new ProgressDialog(User_Dashboard_Activity.this, R.style.AppCompatAlertDialogStyle);
                        progressdialog.setMessage("Loading");
                        progressdialog.setCanceledOnTouchOutside(false);
                        progressdialog.show();

                        getcurrentlocation();
                    } else
                        Toast.makeText(User_Dashboard_Activity.this, "Please enable your Location", Toast.LENGTH_SHORT).show();

                }
            }
        });

        saveaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (address_latitude.equals("") && address_longitude.equals("")) {
                    Toast.makeText(User_Dashboard_Activity.this, "Fetch Location First", Toast.LENGTH_SHORT).show();


                } else if (addressLine.equals("")) {
                    Toast.makeText(User_Dashboard_Activity.this, "Address is not fount", Toast.LENGTH_SHORT).show();

                } else {
                    updatelocationdetails(storeId, address_latitude, address_longitude, addressLine);
                }
            }
        });


        dialog = dialogBuilder.create();
        dialog.show();
    }


    public void updatelocationdetails(int id, String latitude, String longitude, String address) {
        Log.e("startlocationlatitude", String.valueOf(latitude));
        Log.e("startlocationlongitude", String.valueOf(longitude));
        Log.e("startlocationaddress", String.valueOf(address));
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("storeId", String.valueOf(id))
                .add("longitude", longitude)
                .add("latitude", latitude)
                .add("location_details", addressLine);


        RequestBody requestBody = formBuilder.build();

        Request request = new Request.Builder()
                .url(Constants.domain_name + "retailer/update")
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + token)
                .build();

        try (Response response1 = client.newCall(request).execute()) {
            String responseData1 = response1.body().string();
            Toast.makeText(User_Dashboard_Activity.this, "Success", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            progressdialog.dismiss();
            Log.e("responseData1Response", responseData1);

        } catch (IOException e) {

            progressdialog.dismiss();
            e.printStackTrace();
        }


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


    @SuppressLint("MissingPermission")
    public void getcurrentlocation() {


        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        LocationServices.getFusedLocationProviderClient(User_Dashboard_Activity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(User_Dashboard_Activity.this)
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int lastlocation = locationResult.getLocations().size() - 1;
                            double latitude = locationResult.getLocations().get(lastlocation).getLatitude();
                            double longitude = locationResult.getLocations().get(lastlocation).getLongitude();

                            Log.e("latitudelatitude", String.valueOf(latitude));
                            Log.e("longtitudelongtitude", String.valueOf(longitude));
                            address_latitude = String.valueOf(latitude);
                            address_longitude = String.valueOf(longitude);

                            Geocoder geocoder = new Geocoder(User_Dashboard_Activity.this); // Initialize the Geocoder
                            List<Address> addresses = null; // Get the address
                            try {
                                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (addresses != null && !addresses.isEmpty()) {
                                Address address = addresses.get(0);
                                addressLine = address.getAddressLine(0);

                                address_txt.setText(addressLine);
                                address_txt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_location_01, 0, 0, 0);

                                address_txt.setVisibility(View.VISIBLE);
                                progressdialog.dismiss();


                            } else {
                                // No address found
                                Toast.makeText(User_Dashboard_Activity.this, "No address found", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                }, Looper.getMainLooper());


    }


    //For voice Note
    private boolean checkPermissions() {
        int result = ContextCompat.
                checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void startRecording() {
        if (checkPermissions()) {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/AudioRecording.3gp";
//file=convertStringToFile(mFileName);
            } else {
                Log.e("TAG", "External storage is not available");
            }
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile(mFileName);
            try {
                mRecorder.prepare();
                mRecorder.start();
//Toast.makeText(this, "Suceess", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Log.e("TAG", "" + e);
                e.printStackTrace();
                Toast.makeText(this, "" + e, Toast.LENGTH_LONG).show();
            }
        } else {
            RequestPermissions();
        }
    }

    private void RequestPermissions() {
        ActivityCompat.requestPermissions(User_Dashboard_Activity.this, new String[]{RECORD_AUDIO
                                        , WRITE_EXTERNAL_STORAGE},
                                REQUEST_AUDIO_PERMISSION_CODE);
    }

    public void pauseRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        recordCheck = true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode ==
                REQUEST_PERMISSION_CODE
        ) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.
                    PERMISSION_GRANTED
            ) {
// Permission granted, you can start recording here
            } else {
                if (grantResults.length > 0) {
                    boolean permissionToRecord = grantResults[0] == PackageManager.
                            PERMISSION_GRANTED;
                    boolean permissionToStore = grantResults[1] == PackageManager.
                            PERMISSION_GRANTED;
                    if (permissionToRecord && permissionToStore) {
// Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
//Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
// Permission denied, handle accordingly (e.g., show a message)

                if (grantResults.length > 0) {
                    boolean READ_EXTERNAL_STORAGE = grantResults[0] == PackageManager.
                            PERMISSION_GRANTED;
                    boolean WRITE_EXTERNAL_STORAGE = grantResults[1] == PackageManager.
                            PERMISSION_GRANTED;
                    if (READ_EXTERNAL_STORAGE && WRITE_EXTERNAL_STORAGE) {
// perform action when allow permission success
                    } else {
// Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    public void playAudio() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(
                    mFileName
            );
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e("TAG", "prepare() failed");
        }
        play = false;
    }

    public void pausePlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void RRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.
                    PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE
                                        }, REQUEST_PERMISSION_CODE);
            }
        }
        if (Build.VERSION.
                SDK_INT
                >= Build.VERSION_CODES.
                R
        ) {
            if (Environment.isExternalStorageManager()) {
// perform action when allow permission success
            } else {
                Intent intent = new Intent();
                intent.setAction(Settings.
                        ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                );
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
                Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
            }
        }
    }


}