package com.jerry.wifimaster.ui;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import com.jerry.baselib.base.BaseWebViewActivity;
import com.jerry.baselib.base.UINavigationView;
import com.jerry.wifimaster.R;

/**
 * Created by guqian on 2017/8/2.
 */

public class ServiceWebActivity extends BaseWebViewActivity {

    UINavigationView mUinv;

    public String mUrl;
    public String mTitle;

    public static void start(Context context, String url, String title) {

        Intent intent = new Intent();
        intent.setClass(context, ServiceWebActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    @Override
    public void loadData(Bundle savedInstanceState) {
        super.loadData(savedInstanceState);
        mUrl = getIntent().getStringExtra("url");
        mTitle = getIntent().getStringExtra("title");
        mUinv.setNavigationTitle(mTitle);

        mWebView.loadUrl(mUrl);
    }

    @Override
    public void initViews() {
        super.initViews();
        mUinv=findViewById(R.id.uinv);
    }

    @Override
    public boolean handlerOverrideUrlLoading(WebView view, String url) {

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.destroy();
        }
    }
}
