package com.example.distributor_field;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.distributor_field.Constant.Constants;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UpdatePaymentActivity extends AppCompatActivity {

    String token;
    ImageView cancel;
    RadioButton cash;
    RadioButton gpay;
    RadioButton cheque;
    Button updatePayment;
    TextInputEditText updateAmount;
    View transactioView, chequeView;

    LinearLayout transactionlayout;
    LinearLayout chequeLinearLayout;

    TextView attachFile;
    ImageView attachImageview;
    int getBillNumberForUpdateBill;
    private final int REQUEST_CODE_GALLERY = 1;

    private static final int PERMISSION_REQUEST_CODE = 1;
    String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_payment);
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.TOKEN_SHARED_PREFERENCE, MODE_PRIVATE);
        token = sharedPreferences.getString(Constants.TOKEN, "");

        cancel = findViewById(R.id.backToCheckin);
        cash = findViewById(R.id.updateCash);
        gpay = findViewById(R.id.updateGpay);
        cheque = findViewById(R.id.updateCheque);

        updatePayment = findViewById(R.id.updatePaymentButton);
        updateAmount = findViewById(R.id.updateBillAmount);
        transactioView = LayoutInflater.from(UpdatePaymentActivity.this).inflate(R.layout.transaction_layout, transactionlayout, false);
        TextInputEditText transactionIdEdittext = transactioView.findViewById(R.id.updateTransactionId);
        chequeView = LayoutInflater.from(UpdatePaymentActivity.this).inflate(R.layout.cheque_layout, chequeLinearLayout, false);

        transactionlayout = findViewById(R.id.transactionLinearLayout);
        chequeLinearLayout = findViewById(R.id.chequeLinearLayout);
        attachFile = chequeView.findViewById(R.id.attachFile);
        attachImageview = chequeView.findViewById(R.id.attachfile_image);

        getBillNumberForUpdateBill = getIntent().getIntExtra("BillNumberUpdate", 0);


        attachFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestStoragePermission();
                openGallery();
            }
        });
        updatePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (updateAmount.getText().toString().equals("") || updateAmount.getText().toString() == null) {
                    updateAmount.setError("Required");
                    return;
                } else if (!(cash.isChecked() == true || gpay.isChecked() == true || cheque.isChecked() == true)) {
                    Toast.makeText(UpdatePaymentActivity.this, "Please Select payment type", Toast.LENGTH_SHORT).show();
                    return;
                } else if (gpay.isChecked() == true && transactionIdEdittext.getText().toString().equals("") || transactionIdEdittext.getText().toString() == null) {
                    transactionIdEdittext.setError("Required");
                    return;
                } else if (cheque.isChecked() == true && imagePath == null) {
                    Toast.makeText(UpdatePaymentActivity.this, "Please Upload Cheque Image" + cheque.isChecked(), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Calendar calendar = Calendar.getInstance();
                    Date currentDate = calendar.getTime();
                    // Format the date
                    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String formattedDate = dateFormat1.format(currentDate);

                    OkHttpClient client = new OkHttpClient();

                    FormBody.Builder formBuilder = new FormBody.Builder();
                    formBuilder.add("outStandingId", "" + getBillNumberForUpdateBill);
                    formBuilder.add("createDate", formattedDate);
                    formBuilder.add("claimAmount", updateAmount.getText().toString());
                    if (cash.isChecked() == true) {
                        formBuilder.add("Type", "Cash");
                    } else if (gpay.isChecked() == true) {
                        formBuilder.add("Type", "TransactionId");
                        formBuilder.add("TransactionId", "1234567");
                    } else if (cheque.isChecked() == true) {
                        formBuilder.add("Type", "Cheque");
                        formBuilder.add("chequeImage", imagePath);
                        Log.d("Cheque", imagePath);
                    }
                    RequestBody requestBody = formBuilder.build();
                    Request request = new Request.Builder()
                            .url(Constants.domain_name+"outstanding")
                            .post(requestBody)
                            .addHeader("Authorization", "Bearer " + token)
                            .build();
                    try (Response response1 = client.newCall(request).execute()) {
                        String responseData1 = response1.body().string();
                        if (responseData1.equals("\"OutStanding Amount Over Cost\"")) {

                            SweetAlertDialog dialog = new SweetAlertDialog(UpdatePaymentActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText(responseData1).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            Intent intent = new Intent(getApplicationContext(), CheckInActivity.class);
                                            startActivity(intent);
                                        }
                                    });

                            dialog.setCancelable(false);
                            dialog.show();
                        } else {
                            SweetAlertDialog dialog = new SweetAlertDialog(UpdatePaymentActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText(responseData1).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            Intent intent = new Intent(getApplicationContext(), CheckInActivity.class);
                                            startActivity(intent);
                                        }
                                    });

                            dialog.setCancelable(false);
                            dialog.show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        gpay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    transactionlayout.addView(transactioView);
                } else {
                    transactionlayout.removeView(transactioView);
                }
            }
        });

        cheque.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    chequeLinearLayout.addView(chequeView);
                } else {
                    chequeLinearLayout.removeView(chequeView);
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            imagePath = getImagePath(selectedImageUri);
            if (!(UpdatePaymentActivity.this).isFinishing()) {
                Glide.with(UpdatePaymentActivity.this).load(selectedImageUri).into(attachImageview);
            }
            // Upload the image using OkHttp client
            // uploadImage(imagePath);
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

}