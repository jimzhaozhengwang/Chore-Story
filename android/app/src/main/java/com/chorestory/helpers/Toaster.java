package com.chorestory.helpers;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class Toaster {
    public static void showToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 400);
        toast.show();
    }
}
