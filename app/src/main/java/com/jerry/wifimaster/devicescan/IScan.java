package com.jerry.wifimaster.devicescan;

import android.content.Context;

public interface IScan {
    void startScan(Context context,DeviceScanResult result);
    void stopScan();
}
