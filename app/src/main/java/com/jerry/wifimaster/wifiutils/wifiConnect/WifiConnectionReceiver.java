package com.jerry.wifimaster.wifiutils.wifiConnect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jerry.wifimaster.wifiutils.WeakHandler;
import com.thanosfisherman.elvis.Objects;

import java.util.logging.Handler;

import static com.jerry.wifimaster.wifiutils.ConnectorUtils.isAlreadyConnected;
import static com.jerry.wifimaster.wifiutils.ConnectorUtils.reEnableNetworkIfPossible;
import static com.jerry.wifimaster.wifiutils.WifiUtils.wifiLog;
import static com.jerry.wifimaster.wifiutils.utils.VersionUtils.isAndroidQOrLater;
import static com.thanosfisherman.elvis.Elvis.of;



public final class WifiConnectionReceiver extends BroadcastReceiver {
    @NonNull
    private final WifiConnectionCallback mWifiConnectionCallback;
    @Nullable
    private ScanResult mScanResult;
    @NonNull
    private final WifiManager mWifiManager;
    private String ssid;

    WeakHandler handler =new WeakHandler();
    public WifiConnectionReceiver(@NonNull final WifiConnectionCallback callback, @NonNull final WifiManager wifiManager) {
        this.mWifiConnectionCallback = callback;
        this.mWifiManager = wifiManager;
    }

    @Override
    public void onReceive(final Context context, @NonNull final Intent intent) {
        final String action = intent.getAction();
        wifiLog("Connection Broadcast action: " + action);
        if (isAndroidQOrLater()) {
            if (Objects.equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION, action)) {
                final SupplicantState state = intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
                final int suppl_error = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, -1);
                wifiLog("Connection Broadcast state: " + state);
                wifiLog("suppl_error: " + suppl_error);
                if (mScanResult == null && isAlreadyConnected2(mWifiManager, ssid)) {
                    mWifiConnectionCallback.successfulConnect();
                }
                if (state == SupplicantState.DISCONNECTED && suppl_error == WifiManager.ERROR_AUTHENTICATING) {
                    mWifiConnectionCallback.errorConnect(ConnectionErrorCode.AUTHENTICATION_ERROR_OCCURRED);
                }
            }
        } else {
            if (Objects.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION, action)) {
                /*
                    Note here we don't check if has internet connectivity, because we only validate
                    if the connection to the hotspot is active, and not if the hotspot has internet.
                 */
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
                        if (isAlreadyConnected(mWifiManager, of(mScanResult).next(scanResult -> scanResult.BSSID).get(),of(mScanResult).next(scanResult -> scanResult.SSID).get())) {

                            mWifiConnectionCallback.successfulConnect();
                        }
//                    }
//                },1000);

            } else if (Objects.equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION, action)) {
                final SupplicantState state = intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
                final int supl_error = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, -1);

                if (state == null) {
                    mWifiConnectionCallback.errorConnect(ConnectionErrorCode.COULD_NOT_CONNECT);
                    return;
                }

                wifiLog("Connection Broadcast state: " + state);

                switch (state) {
                    case COMPLETED:
                    case FOUR_WAY_HANDSHAKE:
                        if (mScanResult == null && isAlreadyConnected2(mWifiManager, ssid)) {
                            mWifiConnectionCallback.successfulConnect();
                        } else if (isAlreadyConnected(mWifiManager, of(mScanResult).next(scanResult -> scanResult.BSSID).get(),of(mScanResult).next(scanResult -> scanResult.SSID).get())) {
                            mWifiConnectionCallback.successfulConnect();
                        }
                        break;
                    case DISCONNECTED:
                        if (supl_error == WifiManager.ERROR_AUTHENTICATING) {
                            wifiLog("Authentication error...");
                            mWifiConnectionCallback.errorConnect(ConnectionErrorCode.AUTHENTICATION_ERROR_OCCURRED);
                        } else {
                            wifiLog("Disconnected. Re-attempting to connect...");
                            reEnableNetworkIfPossible(mWifiManager, mScanResult);
                        }
                }
            }
        }
    }

    public static boolean isAlreadyConnected2(@Nullable WifiManager wifiManager, @Nullable String ssid) {
        if (ssid != null && wifiManager != null) {
            if (wifiManager.getConnectionInfo() != null && wifiManager.getConnectionInfo().getSSID() != null &&
                    wifiManager.getConnectionInfo().getIpAddress() != 0 &&
                    Objects.equals(ssid, wifiManager.getConnectionInfo().getSSID())) {
                wifiLog("Already connected to: " + wifiManager.getConnectionInfo().getSSID() + "  BSSID: " + wifiManager.getConnectionInfo().getBSSID());
                return true;
            }
        }
        return false;
    }

    @NonNull
    public WifiConnectionReceiver connectWith(@NonNull ScanResult result, @NonNull String password, @NonNull ConnectivityManager connectivityManager) {
        mScanResult = result;

        return this;
    }

    @NonNull
    public WifiConnectionReceiver connectWith(@NonNull String ssid, @NonNull String password, @NonNull ConnectivityManager connectivityManager) {
        this.ssid = ssid;

        return this;
    }
}
