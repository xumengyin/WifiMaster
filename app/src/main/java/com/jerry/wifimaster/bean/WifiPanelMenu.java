package com.jerry.wifimaster.bean;

public class WifiPanelMenu {

    public String title;
    public int icon;

    public int type;

    public WifiPanelMenu(String title, int icon, int type) {
        this.title = title;
        this.icon = icon;
        this.type = type;
    }


    //连接
    public static final int TYPE_PASS_CONNECT=0;
    //举报钓鱼
    public static final int TYPE_REPORT=1;
    //网络测速
    public static final int TYPE_TEST_SPEED=2;
    //安全监测
    public static final int TYPE_CHECK=3;
    //忘记网络
    public static final int TYPE_FORGET_NET=4;
}
