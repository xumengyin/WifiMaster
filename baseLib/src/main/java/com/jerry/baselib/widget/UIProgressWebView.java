package com.jerry.baselib.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.core.content.ContextCompat;

import com.jerry.baselib.R;


/**
 * Created by guqian on 2017/8/2.
 */

public class UIProgressWebView extends WebView {

    public interface OnScrollChangedListener {
        void onScrollChanged(int l, int t, int oldl, int oldt);
    }

    public OnScrollChangedListener listener;

    public void setOnScrollChangedListener(OnScrollChangedListener listener) {
        this.listener = listener;
    }

    /**
     * 进度
     */
    public interface OnProgressChangedListener {
        void onProgressChanged(WebView view, int newProgress);
    }

    public OnProgressChangedListener onProgressChangedListener;

    public void setOnProgressChangedListener(OnProgressChangedListener listener) {
        this.onProgressChangedListener = listener;
    }

    private ProgressBar progressbar;

    public UIProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);

        progressbar.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 3, 0, 0));
        addView(progressbar);
        //        setWebViewClient(new WebViewClient(){});
        setWebChromeClient(new WebChromeClient());
    }

    public static Context getFixedContext(Context context) {
        if (Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT < 23) // Android Lollipop 5.0 & 5.1
            return context.createConfigurationContext(new Configuration());
        return context;
    }

    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (onProgressChangedListener != null) {
                onProgressChangedListener.onProgressChanged(view, newProgress);
            }
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE)
                    progressbar.setVisibility(VISIBLE);
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
        if (listener != null) {
            listener.onScrollChanged(l, t, oldl, oldt);
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }
}
