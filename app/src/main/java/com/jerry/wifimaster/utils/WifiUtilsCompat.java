package com.jerry.wifimaster.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.NetworkSpecifier;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSpecifier;
import android.net.wifi.WifiNetworkSuggestion;

import androidx.annotation.NonNull;

import com.jerry.baselib.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class WifiUtilsCompat {


    private Context mContext;

    public WifiUtilsCompat(Context context) {
        mContext = context;

    }

    public void connectWifiP2p(Context context, String ssid, String password) {

        if (CommonUtils.isAndroidQOrLater()) {
            final NetworkSpecifier specifier = new WifiNetworkSpecifier.Builder()
                    .setSsid(ssid)
                    .setWpa3Passphrase(password)
                    .build();

            final NetworkRequest request =
                    new NetworkRequest.Builder()
                            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                            .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                            .setNetworkSpecifier(specifier)
                            .build();

            final ConnectivityManager connectivityManager = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);

            final ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback(){
                @Override
                public void onAvailable(@NonNull Network network) {
                    super.onAvailable(network);
                    LogUtils.logd("connectWificonnectWifi onAvailable: "+network.toString());
                }

                @Override
                public void onUnavailable() {
                    super.onUnavailable();
                    LogUtils.logd("connectWificonnectWifi onUnavailable: ");
                }
            };
            connectivityManager.requestNetwork(request, networkCallback);
// Release the request when done.
            connectivityManager.unregisterNetworkCallback(networkCallback);
        }
    }


    public void connectWifi(Context context, String ssid, String password) {
        if (CommonUtils.isAndroidQOrLater()) {
            final WifiNetworkSuggestion suggestion1 =
                    new WifiNetworkSuggestion.Builder()
                            .setSsid(ssid)
                            .setWpa2Passphrase(password)
                            .setIsAppInteractionRequired(true) // Optional (Needs location permission)
                            .build();
            final List<WifiNetworkSuggestion> suggestionsList =
                    new ArrayList<WifiNetworkSuggestion>();
            suggestionsList.add(suggestion1);
            final WifiManager wifiManager =
                    (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            final int status = wifiManager.addNetworkSuggestions(suggestionsList);
            if (status != WifiManager.STATUS_NETWORK_SUGGESTIONS_SUCCESS) {
                LogUtils.logd("connectWificonnectWifi fail: " + status);
            }

            final IntentFilter intentFilter =
                    new IntentFilter(WifiManager.ACTION_WIFI_NETWORK_SUGGESTION_POST_CONNECTION);

            final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    context.unregisterReceiver(this);
                    LogUtils.logd("connectWificonnectWifi BroadcastReceiver: " + intent.getAction());
                    if (!intent.getAction().equals(
                            WifiManager.ACTION_WIFI_NETWORK_SUGGESTION_POST_CONNECTION)) {
                        return;
                    }
                    // do post connect processing here...
                }
            };
            context.registerReceiver(broadcastReceiver, intentFilter);

        }

    }
}
