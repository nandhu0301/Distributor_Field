package com.example.distributor_field.Constant;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class TimeUtil {

    public static void fetchInternetDateTime(DateTimeListener listener) {
        new FetchDateTimeTask(listener).execute();
    }

    public interface DateTimeListener {
        void onDateTimeFetched(String dateTime);
        void onDateTimeFetchFailed();
    }


    private static class FetchDateTimeTask extends AsyncTask<Void, Void, String> {
        private DateTimeListener listener;

        public FetchDateTimeTask(DateTimeListener listener) {
            this.listener = listener;
        }

        @Override
        protected String doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://worldtimeapi.org/api/ip")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        String json = responseBody.string();
                        Gson gson = new Gson();
                        TimeResponse timeResponse = gson.fromJson(json, TimeResponse.class);
                        if (timeResponse != null) {
                            return timeResponse.getDatetime();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String dateTime) {
            if (dateTime != null) {
                listener.onDateTimeFetched(dateTime);
            } else {
                listener.onDateTimeFetchFailed();
            }
        }
    }

    private static class TimeResponse {
        private String datetime;

        public String getDatetime() {
            return datetime;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String fetchInternetTime(String Time){

        String convertedTime ="";

        java.text.SimpleDateFormat inputFormat = new java.text.SimpleDateFormat("HH:mm:ss.SSSSSSXXX", Locale.getDefault());
        java.text.SimpleDateFormat outputFormat = new java.text.SimpleDateFormat("hh:mm a", Locale.getDefault());

        try {
            Date date = inputFormat.parse(Time);
            convertedTime = outputFormat.format(date);
            // convertedTime will be something like "11:18:49 AM" or "11:18:49 PM" depending on the time value
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return convertedTime;

    }
}
