package com.example.distributor_field.Constant;

import android.widget.EditText;

import com.example.distributor_field.Constant.Constants;

public class TextUtils {

    public static boolean validateLoginForm(final String userNameStr, final String passwordStr,
                                            EditText userName, EditText password) {

        if ("".equals(userNameStr)) {
            userName.setError(Constants.REQUIRED);
            return false;
        }
        if ("".equals(passwordStr)) {
            password.setError(Constants.REQUIRED);
            return false;
        }
        return true;
    }

}
