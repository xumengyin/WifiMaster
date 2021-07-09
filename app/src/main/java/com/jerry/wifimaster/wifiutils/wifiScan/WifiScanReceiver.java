package com.jerry.wifimaster.wifiutils.wifiScan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;


public class WifiScanReceiver extends BroadcastReceiver {
    @NonNull
    private final WifiScanCallback callback;

    public WifiScanReceiver(@NonNull WifiScanCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        callback.onScanResultsReady();
    }
}
