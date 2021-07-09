package com.jerry.wifimaster.bean;

import com.jerry.baselib.utils.LogUtils;

import java.io.Serializable;
import java.text.DecimalFormat;

public class SpeedTestEvent implements Serializable {
    public String speed;

    public SpeedTestEvent(long speedByte) {
        LogUtils.logd("SpeedTestEvent:" + speedByte);
        DecimalFormat format = new DecimalFormat("#.##");
        float speed = speedByte / 1024f;
        if (speed > 1) {
            speed /= 1024f;
            if (speed > 1) {
                this.speed = format.format(speed) + "MB/s";

            } else {
                this.speed = format.format(speedByte / 1024) + "KB/s";
            }
        } else {
            this.speed = "1KB/s";
        }
        //this.speed=
    }
}
