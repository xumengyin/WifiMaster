/*
 * AUTHOR：Yan Zhenjie
 *
 * DESCRIPTION：create the File, and add the content.
 *
 * Copyright © www.mamaqunaer.com. All Rights Reserved
 *
 */
package com.jerry.baselib.http;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;

import com.jerry.baselib.R;
import com.yanzhenjie.nohttp.error.NetworkError;
import com.yanzhenjie.nohttp.error.TimeoutError;
import com.yanzhenjie.nohttp.error.URLError;
import com.yanzhenjie.nohttp.error.UnKnownHostError;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Response;

/**
 * <p>Harmonized Http results callback.</p> Created by Yan Zhenjie on 2016/7/7.
 */
public class DefaultResponseListener<T>
  implements OnResponseListener<Result<T>> {

    private Context mContext;
    private HttpCallback<T> mHttpCallback;
    private Dialog mDialog;

    public DefaultResponseListener(@NonNull Context context, HttpCallback<T> callback, boolean dialog) {
        this.mContext = context;
        this.mHttpCallback = callback;

//        if (dialog) {
//            mDialog = new LoadingDialog(mContext);
//        }
    }

    @Override
    public void onStart(int what) {
        if (mDialog != null && !mDialog.isShowing()) mDialog.show();
    }

    @Override
    public void onFinish(int what) {
        if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
    }

    @Override
    public void onSucceed(int what, Response<Result<T>> response) {
        Result<T> tResult = response.get();
        tResult.setFromCache(response.isFromCache());
        if (mHttpCallback != null) mHttpCallback.onResponse(tResult);
    }

    @Override
    public void onFailed(int what, Response<Result<T>> response) {
        Exception exception = response.getException();

        int stringRes = R.string.http_exception_unknow_error;
        if (exception instanceof NetworkError) {
            stringRes = R.string.http_exception_network;
        } else if (exception instanceof TimeoutError) {
            stringRes = R.string.http_exception_connect_timeout;
        } else if (exception instanceof UnKnownHostError) {
            stringRes = R.string.http_exception_host;
        } else if (exception instanceof URLError) {
            stringRes = R.string.http_exception_url;
        }
        if (mHttpCallback != null) {
            Result<T> errorResult =
              new Result(false, response.getHeaders(), null, -1, mContext.getString(stringRes));
            mHttpCallback.onResponse(errorResult);
        }
    }
}