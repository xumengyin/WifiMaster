package com.jerry.wifimaster.bean;

import java.io.Serializable;

public class NetSpeedResult implements Serializable {

    public int pings;
    public long downloadSpeed;
    public long uploadSpeed;

    public NetSpeedResult(int pings, long downloadSpeed, long uploadSpeed) {
        this.pings = pings;
        this.downloadSpeed = downloadSpeed;
        this.uploadSpeed = uploadSpeed;
    }
}
