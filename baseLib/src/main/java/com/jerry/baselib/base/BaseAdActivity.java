package com.jerry.baselib.base;

import android.content.Context;
import android.os.Bundle;

import com.anythink.core.api.ATAdInfo;
import com.anythink.core.api.ATNetworkConfirmInfo;
import com.anythink.core.api.AdError;
import com.anythink.splashad.api.ATSplashExListenerWithConfirmInfo;
import com.anythink.splashad.api.IATSplashEyeAd;
import com.jerry.baselib.utils.LogUtils;

public  class BaseAdActivity extends BaseActivity implements ATSplashExListenerWithConfirmInfo {

    private static final String TAG = "BaseAdActivity";

    @Override
    public void onDownloadConfirm(Context context, ATAdInfo atAdInfo, ATNetworkConfirmInfo atNetworkConfirmInfo) {

    }

    @Override
    public void onDeeplinkCallback(ATAdInfo atAdInfo, boolean b) {

    }

    @Override
    public void onAdLoaded() {
        LogUtils.d(TAG,"onAdLoaded");
    }

    @Override
    public void onNoAdError(AdError adError) {

    }

    @Override
    public void onAdShow(ATAdInfo atAdInfo) {

    }

    @Override
    public void onAdClick(ATAdInfo atAdInfo) {

    }

    @Override
    public void onAdDismiss(ATAdInfo atAdInfo, IATSplashEyeAd iatSplashEyeAd) {

    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void initViews() {

    }

    @Override
    public void loadData(Bundle savedInstanceState) {

    }
}
