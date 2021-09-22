package com.jerry.wifimaster;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.blankj.utilcode.util.NetworkUtils;
import com.jerry.baselib.utils.LogUtils;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ActivityContext;

public class ConnectReceiver extends BroadcastReceiver {
    Context context;
    IConnectRec callBack;

    NetworkUtils.NetworkType currentType;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            LogUtils.logd("ConnectReceiver----" + intent.getExtras().toString());

            // NetworkUtils.NetworkType curNet=NetworkUtils.getNetworkType();
            if (callBack != null) {

                if (NetworkUtils.getWifiEnabled()) {
                    callBack.onConnect();
                } else {
                    callBack.onDisConnect();
                }
            }


        }
    }
    @Inject
    public ConnectReceiver(@ActivityContext Context context) {
        this.context = context;
    }

    public void setCallBack(IConnectRec callBack) {
        this.callBack = callBack;
    }

    public void register() {
        currentType = NetworkUtils.getNetworkType();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(this, intentFilter);
    }

    public void unRegister() {
        context.unregisterReceiver(this);
    }

    public interface IConnectRec {
        void onConnect();

        void onDisConnect();
    }
}
