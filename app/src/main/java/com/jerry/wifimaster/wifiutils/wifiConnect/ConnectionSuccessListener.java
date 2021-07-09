package com.jerry.wifimaster.wifiutils.wifiConnect;

import androidx.annotation.NonNull;

public interface ConnectionSuccessListener {
    void success();

    void failed(@NonNull ConnectionErrorCode errorCode);
}
