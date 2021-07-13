package com.jerry.wifimaster.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.fragment.app.Fragment;

import org.greenrobot.eventbus.EventBus;

public class CommonUtils {

    /**
     * 加载动画
     * @param context
     * @param ani
     * @return
     */
    public static Animation loadAni(Context context, int ani) {
        return AnimationUtils.loadAnimation(context, ani);
    }

    public static String getWifiStrength(int dbm) {
        String strength = "强";
        int level = 4 * (dbm + 100) / 45;
        if (level <= 0) {
            strength = "弱";
        } else if (dbm >= 4) {
            strength = "强";
        } else if (level == 1) {
            strength = "较弱";
        } else if (level == 2) {
            strength = "中";
        } else if (level == 3) {
            strength = "较强";
        }
        return strength;
    }


    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    public static String getModel() {
        String model = Build.MODEL;
        if (model != null) {
            model = model.trim().replaceAll("\\s*", "");
        } else {
            model = "";
        }
        return model;
    }

    public static final int GPS_KEY = 998;

    public static void gotoGpsSetting(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        activity.startActivityForResult(intent, GPS_KEY);
    }

    public static void gotoGpsSetting(Fragment fragment) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        fragment.startActivityForResult(intent, GPS_KEY);
    }


    public static void eventBusPostMsg(Object object) {
        EventBus.getDefault().post(object);
    }


    public static boolean isAndroidQOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    }
}
