package com.jerry.wifimaster.utils;

public class CommonUtils {


    public static String getWifiStrength(int dbm)
    {
        String strength="强";
        int level=4*(dbm+100)/45;
        if(level<=0)
        {
            strength="弱";
        }else if(dbm>=4)
        {
            strength="强";
        }else if(level==1)
        {
            strength="较弱";
        }else if(level==2)
        {
            strength="中";
        }else if(level==3)
        {
            strength="较强";
        }
        return  strength;
    }
}