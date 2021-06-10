package com.jerry.wifimaster.utils;

import android.content.Context;

import com.jerry.wifimaster.devicescan.DeviceScanManager;
import com.jerry.wifimaster.devicescan.DeviceScanResult;
import com.jerry.wifimaster.devicescan.IP_MAC;

//设备发现功能
public class DeviceScanUtil {

    private DeviceScanManager manager;
    private Context context;

    public DeviceScanUtil(Context context) {
        manager = new DeviceScanManager();
        this.context = context.getApplicationContext();
    }
    public void start(final IScanResults results)
    {
        manager.startScan(context, new DeviceScanResult() {
            @Override
            public void deviceScanResult(IP_MAC ip_mac) {
                results.scanResult(ip_mac);
            }
        });
    }

    public void destroy()
    {
        if (manager != null) {
            manager.stopScan();
        }
    }
    public interface IScanResults {
        void scanResult(IP_MAC ip_mac);
    }
}
