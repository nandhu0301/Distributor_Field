package com.example.distributor_field.Constant;

import android.graphics.Color;

public class ColorUtils {
    public static int generateLightColor() {
        // Generate random RGB values in the light color range
        int red = (int) (Math.random() * 128) + 128;
        int green = (int) (Math.random() * 128) + 128;
        int blue = (int) (Math.random() * 128) + 128;

        // Create the color using RGB values
        return Color.rgb(red, green, blue);
    }
}
