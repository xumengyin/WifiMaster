package com.jerry.wifimaster.bean;

import android.net.wifi.ScanResult;

import java.util.Objects;

public class WifiBean {

    public static final int LEVEL_LOW = 0;
    public static final int LEVEL_MIDDLE = 1;
    public static final int LEVEL_HIGTH = 2;
    public ScanResult result;
    public int levelStrength = LEVEL_LOW;

    public WifiBean(ScanResult result) {
        this.result = result;
        if (result.level >= -60) {
            levelStrength = LEVEL_HIGTH;
        } else if (result.level <= -100) {
            levelStrength = LEVEL_LOW;
        } else {
            levelStrength = LEVEL_MIDDLE;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WifiBean wifiBean = (WifiBean) o;
        return Objects.equals(result, wifiBean.result) || Objects.equals(result.SSID, wifiBean.result.SSID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(result, levelStrength);
    }
}
