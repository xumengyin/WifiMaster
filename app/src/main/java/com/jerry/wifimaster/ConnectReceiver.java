package com.jerry.wifimaster;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.jerry.baselib.utils.LogUtils;

public class ConnectReceiver extends BroadcastReceiver {
    Context context;
    IConnectRec callBack;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            LogUtils.logd("ConnectReceiver----"+intent.getExtras().toString());
            if(callBack!=null)
            {
                callBack.onConnect();
            }
        }
    }

    public ConnectReceiver(Context context) {
        this.context = context;
    }

    public void setCallBack(IConnectRec callBack) {
        this.callBack = callBack;
    }

    public void register() {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(this, intentFilter);
    }

    public void unRegister() {
        context.unregisterReceiver(this);
    }

    public interface IConnectRec {
        void onConnect();
    }
}
