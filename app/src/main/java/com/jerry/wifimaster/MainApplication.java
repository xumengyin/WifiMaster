package com.jerry.wifimaster;


import android.app.Application;
import android.os.Build;
import android.webkit.WebView;

import com.anythink.core.api.ATSDK;


/**
 * Created by 郭攀峰 on 2015/11/4.
 */
public class MainApplication extends Application
{
    private static MainApplication mInstance;


    private void adapterWebView()
    {
        //Android 9及以上必须设置
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            String processName = getProcessName();
            if (!getPackageName().equals(processName)) {
                WebView.setDataDirectorySuffix(processName);
            }
        }
    }


    private void initAds()
    {
        ATSDK.setNetworkLogDebug(BuildConfig.DEBUG);
        ATSDK.init(this, Constants.ADS_APPID, Constants.ADS_APP_KEY);
    }
    @Override
    public void onCreate()
    {
        super.onCreate();

        mInstance = this;
        adapterWebView();
        initAds();
    }

    public static MainApplication getInstance()
    {
        return mInstance;
    }
}
