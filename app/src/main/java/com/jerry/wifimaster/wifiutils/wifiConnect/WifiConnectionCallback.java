package com.jerry.wifimaster.wifiutils.wifiConnect;

import androidx.annotation.NonNull;

public interface WifiConnectionCallback {
    void successfulConnect();

    void errorConnect(@NonNull ConnectionErrorCode connectionErrorCode);
}
