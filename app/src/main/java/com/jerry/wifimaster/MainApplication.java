package com.jerry.wifimaster;


import android.app.Application;
import android.os.Build;
import android.webkit.WebView;

import com.anythink.core.api.ATSDK;
import com.yanzhenjie.nohttp.InitializationConfig;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.OkHttpNetworkExecutor;


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
        initThrid();
    }


    private void initThrid()
    {
        InitializationConfig config = InitializationConfig.newBuilder(this)
                .connectionTimeout(30 * 1000)
                .readTimeout(30 * 1000)
                .networkExecutor(new OkHttpNetworkExecutor())
                .build();
        NoHttp.initialize(config);
    }

    public static MainApplication getInstance()
    {
        return mInstance;
    }
}
