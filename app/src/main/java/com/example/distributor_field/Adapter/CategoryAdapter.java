package com.example.distributor_field.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
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
import com.example.distributor_field.bean.Category;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    ArrayList<Category> categoryDetails = new ArrayList<>();
    String token;


    String sharedPreferenceBrandName;
    String brandId;

    public CategoryAdapter(Context context, ArrayList<Category> categoryDetails, String brandId) {
        this.context = context;
        this.categoryDetails = categoryDetails;
        this.brandId = brandId;

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.TOKEN_SHARED_PREFERENCE, MODE_PRIVATE);
        token = sharedPreferences.getString(Constants.TOKEN, "");
        SharedPreferences sharedPreferenceBrand = context.getSharedPreferences("SharedPreferenceBrand", MODE_PRIVATE);
        sharedPreferenceBrandName = sharedPreferenceBrand.getString("SPBrand", "");


    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.product_category, parent, false);
        return new CategoryAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {

        // CategoryDetails categoryDetailsobj = categoryDetails.get(position);
        // Log.d("TESTTNEW::", categoryDetailsobj.getId() + "ui" + position);
        ((ViewHolder) viewHolder).categoryName.setText(categoryDetails.get(position).getCategoryName());
        //  Glide.with(context).load(categoryDetailsobj.getCategory_image()).into(((ViewHolder) viewHolder).categoryImage);
        int color = getLightColor(); // Replace with your logic to get the light color
        ((ViewHolder) viewHolder).linearLayout.setBackgroundColor(color);

        ((ViewHolder) viewHolder).linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra("Brand", brandId);
                intent.putExtra("Category", categoryDetails.get(position).getCategoryId());
                Log.d("Res", brandId +"cate"+ categoryDetails.get(position).getCategoryId());
                context.startActivity(intent);

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
