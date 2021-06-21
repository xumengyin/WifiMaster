package com.jerry.wifimaster.bean;

import com.jerry.wifimaster.R;
import com.jerry.wifimaster.adapter.MenuAdapter;

import java.util.ArrayList;
import java.util.List;

public class Menus {

    public static List<Menus> menus;
    public int drawableId;
    public String desc;
    public String descValue;
    public int type;

    public Menus(int drawableId, String desc, String descValue, int type) {
        this.drawableId = drawableId;
        this.desc = desc;
        this.descValue = descValue;
        this.type = type;
    }

    static {
        menus=new ArrayList<>();
        menus.add(new Menus(R.drawable.top_menu1,"实时网速","0KB/S", MenuAdapter.TYPE_CURRENT_SPEED));
        menus.add(new Menus(R.drawable.top_menu2,"网络检测","0KB/S", MenuAdapter.TYPE_NET_SPEED_N));
        menus.add(new Menus(R.drawable.top_menu3,"安全检测","防蹭网", MenuAdapter.TYPE_TEST));
        menus.add(new Menus(R.drawable.top_menu4,"信号增强","连接更稳定", MenuAdapter.TYPE_SIGNAL_PLUS));
        menus.add(new Menus(R.drawable.top_menu5,"信号强度","较强", MenuAdapter.TYPE_SIGNAL_STREHGTH));
        menus.add(new Menus(R.drawable.top_menu6,"连接速度","未知", MenuAdapter.TYPE_CONNECT_SPEED));
        menus.add(new Menus(R.drawable.top_menu7,"ip地址","0.0.0.0", MenuAdapter.TYPE_IP));
    }
}
