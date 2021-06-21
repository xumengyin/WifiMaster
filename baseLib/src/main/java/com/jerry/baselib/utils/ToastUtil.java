package com.jerry.baselib.utils;

import android.content.Context;

import androidx.core.content.ContextCompat;

import es.dmoral.toasty.Toasty;

public class ToastUtil {


    public static void toast(Context context, String msg) {
        Toasty.normal(context.getApplicationContext(), msg).show();
    }

    public static void toast(Context context, String msg, int drawableId) {

        Toasty.normal(context.getApplicationContext(), msg, ContextCompat.getDrawable(context, drawableId)).show();
    }
    //custom
//    Toasty.custom(yourContext, "I'm a custom Toast", yourIconDrawable, tintColor, duration, withIcon,
//    shouldTint).show();
}
