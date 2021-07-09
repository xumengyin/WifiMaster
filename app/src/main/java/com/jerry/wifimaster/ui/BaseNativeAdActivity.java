package com.jerry.wifimaster.ui;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.anythink.core.api.ATAdConst;
import com.anythink.core.api.ATAdInfo;
import com.anythink.core.api.AdError;
import com.anythink.nativead.api.ATNative;
import com.anythink.nativead.api.ATNativeAdView;
import com.anythink.nativead.api.ATNativeDislikeListener;
import com.anythink.nativead.api.ATNativeEventExListener;
import com.anythink.nativead.api.ATNativeNetworkListener;
import com.anythink.nativead.api.NativeAd;
import com.jerry.baselib.base.BaseActivity;
import com.jerry.baselib.utils.LogUtils;
import com.jerry.wifimaster.R;
import com.jerry.wifimaster.ads.NativeDemoRender;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseNativeAdActivity extends BaseActivity {

    private static final String TAG = "BaseNativeAdActivity";

    NativeDemoRender anyThinkRender = new NativeDemoRender(this);
    ImageView mCloseView;
    ATNative atNative;
    ATNativeAdView atNativeAdView;
    NativeAd nativeAd;
    protected FrameLayout vAdContentView;

    @Override
    public void initViews() {
        initCloseView();
    }

    private void showAds() {
        if (nativeAd != null) {
            nativeAd.destory();
        }

        nativeAd = atNative.getNativeAd();

        if (nativeAd != null) {
            if (atNativeAdView != null) {
                atNativeAdView.removeAllViews();

                if (atNativeAdView.getParent() == null) {
                    vAdContentView.addView(atNativeAdView, new FrameLayout.LayoutParams(getAdsContentView().getWidth(), getAdsContentView().getHeight()));
                }
            }
            nativeAd.setNativeEventListener(new ATNativeEventExListener() {
                @Override
                public void onDeeplinkCallback(ATNativeAdView view, ATAdInfo adInfo, boolean isSuccess) {
                    LogUtils.d(TAG, "onDeeplinkCallback:" + adInfo.toString() + "--status:" + isSuccess);
                }

                @Override
                public void onAdImpressed(ATNativeAdView view, ATAdInfo entity) {
                    LogUtils.d(TAG, "native ad onAdImpressed:\n" + entity.toString());
                }

                @Override
                public void onAdClicked(ATNativeAdView view, ATAdInfo entity) {
                    LogUtils.d(TAG, "native ad onAdClicked:\n" + entity.toString());
                }

                @Override
                public void onAdVideoStart(ATNativeAdView view) {
                    LogUtils.d(TAG, "native ad onAdVideoStart");
                }

                @Override
                public void onAdVideoEnd(ATNativeAdView view) {
                    LogUtils.d(TAG, "native ad onAdVideoEnd");
                }

                @Override
                public void onAdVideoProgress(ATNativeAdView view, int progress) {
                    LogUtils.d(TAG, "native ad onAdVideoProgress:" + progress);
                }
            });

            nativeAd.setDislikeCallbackListener(new ATNativeDislikeListener() {
                @Override
                public void onAdCloseButtonClick(ATNativeAdView view, ATAdInfo entity) {
                    LogUtils.d(TAG, "native ad onAdCloseButtonClick");
                    if (view.getParent() != null) {
                        ((ViewGroup) view.getParent()).removeView(view);
                        view.removeAllViews();
//                        getAdsContentView().setVisibility(View.GONE);
                    }
                    onCloseAds();
                }
            });
            anyThinkRender.setWhetherSettingDownloadConfirmListener(false);
            try {
                nativeAd.renderAdView(atNativeAdView, anyThinkRender);
            } catch (Exception e) {
                e.printStackTrace();
            }

           // atNativeAdView.addView(mCloseView);

            atNativeAdView.setVisibility(View.VISIBLE);
            nativeAd.prepare(atNativeAdView, anyThinkRender.getClickView(), null);
        }
    }

    protected abstract void onCloseAds();
    protected void requestAds(String adIds) {
        atNativeAdView = new ATNativeAdView(this);
       // atNativeAdView.setPadding(get);
        atNative = new ATNative(this, adIds, new ATNativeNetworkListener() {
            @Override
            public void onNativeAdLoaded() {
                LogUtils.d(TAG,"onNativeAdLoaded");
                showAds();
            }

            @Override
            public void onNativeAdLoadFail(AdError adError) {
                LogUtils.d(TAG,"onNativeAdLoadFail:"+adError.toString());
            }
        });

        Map<String, Object> localMap = new HashMap<>();

        // since v5.6.4
        localMap.put(ATAdConst.KEY.AD_WIDTH, getAdsContentView().getMeasuredWidth());
        localMap.put(ATAdConst.KEY.AD_HEIGHT, getAdsContentView().getMeasuredHeight());

        atNative.setLocalExtra(localMap);
        atNative.makeAdRequest();
    }

    protected abstract View getAdsContentView();

    private void initCloseView() {
        if (mCloseView == null) {
            mCloseView = new ImageView(this);
            mCloseView.setImageResource(R.drawable.ad_close);

            int padding = getResources().getDimensionPixelOffset(R.dimen.dp_5);
            mCloseView.setPadding(padding, padding, padding, padding);

            int size = getResources().getDimensionPixelOffset(R.dimen.dp_30);
            int margin = getResources().getDimensionPixelOffset(R.dimen.dp_2);

            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(size, size);
            layoutParams.topMargin = margin;
            layoutParams.rightMargin = margin;
            layoutParams.gravity = Gravity.TOP | Gravity.RIGHT;

            mCloseView.setLayoutParams(layoutParams);
        }
    }
}
