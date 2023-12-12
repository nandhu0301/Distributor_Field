package com.example.distributor_field.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.distributor_field.CategoryActivity;
import com.example.distributor_field.Constant.ColorUtils;
import com.example.distributor_field.Constant.Constants;
import com.example.distributor_field.ProductActivity;
import com.example.distributor_field.R;
import com.example.distributor_field.bean.Brand;

import java.util.ArrayList;

public class BrandAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    ArrayList<Brand> categoryDetails = new ArrayList<>();
    String token;


    String sharedPreferenceBrandName;

    public BrandAdapter(Context context, ArrayList<Brand> categoryDetails) {
        this.context = context;
        this.categoryDetails = categoryDetails;
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.TOKEN_SHARED_PREFERENCE, MODE_PRIVATE);
        token = sharedPreferences.getString(Constants.TOKEN, "");
        SharedPreferences sharedPreferenceBrand = context.getSharedPreferences("SharedPreferenceBrand", MODE_PRIVATE);
        sharedPreferenceBrandName = sharedPreferenceBrand.getString("SPBrand", "");


    }

    @NonNull
    @Override
    public BrandAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.product_category, parent, false);
        return new BrandAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {

        // CategoryDetails categoryDetailsobj = categoryDetails.get(position);
        // Log.d("TESTTNEW::", categoryDetailsobj.getId() + "ui" + position);
        ((ViewHolder) viewHolder).categoryName.setText(categoryDetails.get(position).getBrandName());
        //  Glide.with(context).load(categoryDetailsobj.getCategory_image()).into(((ViewHolder) viewHolder).categoryImage);
        int color = getLightColor(); // Replace with your logic to get the light color
        ((ViewHolder) viewHolder).linearLayout.setBackgroundColor(color);

        ((ViewHolder) viewHolder).linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sharedPreferenceBrandName == null) {

                    SharedPreferences sharedPreferenceBrand = context.getSharedPreferences("SharedPreferenceBrand", MODE_PRIVATE);
                    SharedPreferences.Editor brandEditor = sharedPreferenceBrand.edit();
                    brandEditor.putString("SPBrand", String.valueOf(categoryDetails.get(position).getBrandId()));
                    brandEditor.commit();

                    Intent intent = new Intent(context, CategoryActivity.class);
                    intent.putExtra("Brand", categoryDetails.get(position).getBrandId());
                    context.startActivity(intent);
                } else if (sharedPreferenceBrandName != null && sharedPreferenceBrandName.equals(categoryDetails.get(position))) {
                    SharedPreferences sharedPreferenceBrand = context.getSharedPreferences("SharedPreferenceBrand", MODE_PRIVATE);
                    SharedPreferences.Editor brandEditor = sharedPreferenceBrand.edit();
                    brandEditor.putString("SPBrand", String.valueOf(categoryDetails.get(position).getBrandId()));
                    brandEditor.commit();

                    Intent intent = new Intent(context, CategoryActivity.class);
                    intent.putExtra("Brand", categoryDetails.get(position).getBrandId());
                    context.startActivity(intent);

                } else if (sharedPreferenceBrandName != null && !(sharedPreferenceBrandName.equals(categoryDetails.get(position)))) {

                    SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();

                    SharedPreferences sharedPreferenceBrand = context.getSharedPreferences("SharedPreferenceBrand", MODE_PRIVATE);
                    SharedPreferences.Editor brandEditor = sharedPreferenceBrand.edit();
                    brandEditor.putString("SPBrand", String.valueOf(categoryDetails.get(position).getBrandId()));
                    brandEditor.commit();

                    Intent intent = new Intent(context, CategoryActivity.class);
                    intent.putExtra("Brand", categoryDetails.get(position).getBrandId());
                    context.startActivity(intent);

                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryDetails.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        //ImageView categoryImage;
        TextView categoryName;
        LinearLayout linearLayout;


        public ViewHolder(View convertView) {
            super(convertView);

            // categoryImage = convertView.findViewById(R.id.image_category);
            categoryName = convertView.findViewById(R.id.text_category);
            linearLayout = convertView.findViewById(R.id.linearlayout);


        }
    }

    private int getLightColor() {
        // Generate or fetch light color dynamically
        // For example, you can use ColorUtils to generate light colors
        int lightColor = ColorUtils.generateLightColor();
        return lightColor;
    }
}
