package com.example.distributor_field;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.distributor_field.Constant.Constants;
import com.example.distributor_field.Constant.TextUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.messaging.FirebaseMessaging;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    Button bt_login;
    TextInputEditText et_username, et_password;
    SharedPreferences.Editor editor;
    String firebasetoken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_username = findViewById(R.id.userNameEdt);
        et_password = findViewById(R.id.passwordEdt);
        bt_login = findViewById(R.id.loginButton);


        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform the API call here


                if (TextUtils.validateLoginForm(et_username.getText().toString().trim(), et_password.getText().toString().trim(), et_username, et_password)) {
                    performApiCall(et_username.getText().toString().trim(), et_password.getText().toString().trim());

                }
            }

            private void performApiCall(String phone, String password) {
                OkHttpClient client = new OkHttpClient();

                String url = Constants.domain_name+"login";
                // Create the request body with the username and password
                RequestBody requestBody = new FormBody.Builder()
                        .add("phone", phone)
                        .add("password", password)
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();
                // Make the asynchronous API call
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.isSuccessful()) {
                            // Get the authorization token from the response
                            String responseData = response.body().string();
                            try {
                                JSONObject jsonObject = new JSONObject(responseData);
                                if (jsonObject.getString("status").equals("fail")) {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            try {
                                                Toast.makeText(LoginActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                            } catch (JSONException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                    });
                                } else {

                                    FirebaseMessaging.getInstance().getToken()
                                            .addOnCompleteListener(new OnCompleteListener<String>() {
                                                @Override
                                                public void onComplete(@NonNull Task<String> task) {
                                                    if (!task.isSuccessful()) {
                                                        // Handle the error

                                                        return;
                                                    }

                                                    // Get the FCM token
                                                    firebasetoken = task.getResult();
                                                    if (firebasetoken != null) {
                                                        String authToken = null;
                                                        try {
                                                            authToken = jsonObject.getString("token");
                                                            SharedPreferences sharedPreferences = getSharedPreferences(Constants.TOKEN_SHARED_PREFERENCE, MODE_PRIVATE);
                                                            editor = sharedPreferences.edit();
                                                            editor.putString(Constants.TOKEN, authToken);
                                                            editor.commit();
                                                            Intent intent = new Intent(LoginActivity.this, User_Dashboard_Activity.class);
                                                            startActivity(intent);
                                                            tokenUpdate(firebasetoken, authToken);
                                                        } catch (JSONException e) {
                                                            throw new RuntimeException(e);
                                                        }
                                                        // Use the token as needed
                                                    }
                                                }
                                            });


                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            // Process the authorization token as needed
                        } else {
                            // Handle request failure
                            // You can retrieve the error message using response.message()
                        }
                    }
                });
            }
        });


    }

    public void tokenUpdate(String firebaseToken, String authToken) {
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("deviceToken", firebaseToken);
        RequestBody requestBody = formBuilder.build();
        Request request = new Request.Builder()
                .url(Constants.domain_name+"user/deviceToken")
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + authToken)
                .build();
        try (Response response1 = client.newCall(request).execute()) {
            String responseData1 = response1.body().string();
            Log.e("Response", responseData1);
            Log.e("token", firebaseToken);
            // Process the response data as needed
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
