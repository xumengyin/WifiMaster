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
import com.jerry.baselib.base.BaseFragment;
import com.jerry.baselib.utils.LogUtils;
import com.jerry.wifimaster.R;
import com.jerry.wifimaster.ads.NativeDemoRender;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseNativeAdFragment extends BaseFragment {

    private static final String TAG = "BaseNativeAdActivity";

    NativeDemoRender anyThinkRender;
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
                    Log.i(TAG, "onDeeplinkCallback:" + adInfo.toString() + "--status:" + isSuccess);
                }

                @Override
                public void onAdImpressed(ATNativeAdView view, ATAdInfo entity) {
                    Log.i(TAG, "native ad onAdImpressed:\n" + entity.toString());
                }

                @Override
                public void onAdClicked(ATNativeAdView view, ATAdInfo entity) {
                    Log.i(TAG, "native ad onAdClicked:\n" + entity.toString());
                }

                @Override
                public void onAdVideoStart(ATNativeAdView view) {
                    Log.i(TAG, "native ad onAdVideoStart");
                }

                @Override
                public void onAdVideoEnd(ATNativeAdView view) {
                    Log.i(TAG, "native ad onAdVideoEnd");
                }

                @Override
                public void onAdVideoProgress(ATNativeAdView view, int progress) {
                    Log.i(TAG, "native ad onAdVideoProgress:" + progress);
                }
            });

            nativeAd.setDislikeCallbackListener(new ATNativeDislikeListener() {
                @Override
                public void onAdCloseButtonClick(ATNativeAdView view, ATAdInfo entity) {
                    Log.i(TAG, "native ad onAdCloseButtonClick");
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

            }

            atNativeAdView.addView(mCloseView);

            atNativeAdView.setVisibility(View.VISIBLE);
            nativeAd.prepare(atNativeAdView, anyThinkRender.getClickView(), null);
        }
    }

    protected abstract void onCloseAds();
    protected void requestAds(String adIds) {
        anyThinkRender = new NativeDemoRender(getContext());
        atNativeAdView = new ATNativeAdView(getContext());
       // atNativeAdView.setPadding(get);
        atNative = new ATNative(getContext(), adIds, new ATNativeNetworkListener() {
            @Override
            public void onNativeAdLoaded() {
                LogUtils.logi("onNativeAdLoaded");
                showAds();
            }

            @Override
            public void onNativeAdLoadFail(AdError adError) {
                LogUtils.logi("onNativeAdLoadFail:"+adError.toString());
            }
        });

        Map<String, Object> localMap = new HashMap<>();

        // since v5.6.4
        localMap.put(ATAdConst.KEY.AD_WIDTH, getAdsContentView().getWidth());
        localMap.put(ATAdConst.KEY.AD_HEIGHT, getAdsContentView().getHeight());

        atNative.setLocalExtra(localMap);
        atNative.makeAdRequest();
    }

    protected abstract View getAdsContentView();

    private void initCloseView() {
        if (mCloseView == null) {
            mCloseView = new ImageView(getActivity());
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
