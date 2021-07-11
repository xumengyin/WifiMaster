package com.jerry.wifimaster;


import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anythink.core.api.ATSDK;
import com.jerry.baselib.utils.LogUtils;
import com.jerry.wifimaster.ui.SplashActivity;
import com.jerry.wifimaster.utils.Constant;
import com.jerry.wifimaster.wifiutils.WifiUtils;
import com.yanzhenjie.nohttp.InitializationConfig;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;


/**
 *
 */
public class MainApplication extends Application {
    private static MainApplication mInstance;


    private void adapterWebView() {
        //Android 9及以上必须设置
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            String processName = getProcessName();
            if (!getPackageName().equals(processName)) {
                WebView.setDataDirectorySuffix(processName);
            }
        }
    }

    int activityCount = 0;
    private void addActivityListener() {
        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {

            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                activityCount++;
                boolean isSplash = activity instanceof SplashActivity;
                if (activityCount == 1 && !isSplash) {
                    Intent intent= new Intent(activity,SplashActivity.class);
                    intent.putExtra(Constants.INTENT_KEY,true);
                    activity.startActivity(intent);
                }
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
                activityCount--;
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });
    }

    private void initAds() {
        ATSDK.setNetworkLogDebug(BuildConfig.DEBUG);
        ATSDK.init(this, Constants.ADS_APPID, Constants.ADS_APP_KEY);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        adapterWebView();
        initAds();
        initThrid();
        addActivityListener();
    }


    private void initThrid() {
        InitializationConfig config = InitializationConfig.newBuilder(this)
                .connectionTimeout(30 * 1000)
                .readTimeout(30 * 1000)
                //.networkExecutor(new OkHttpNetworkExecutor())
                .build();
        NoHttp.initialize(config);
        Logger.setDebug(BuildConfig.DEBUG);
        Logger.setTag("xuxu");
        WifiUtils.enableLog(BuildConfig.DEBUG);
        WifiUtils.forwardLog(new com.jerry.wifimaster.wifiutils.Logger() {
            @Override
            public void log(int priority, String tag, String message) {
                LogUtils.logd("tag:" + tag + "----" + message);
            }
        });
    }

    public static MainApplication getInstance() {
        return mInstance;
    }
}
