package com.jerry.wifimaster.wifiutils.wifiRemove;

import androidx.annotation.NonNull;

public interface RemoveSuccessListener {
    void success();

    void failed(@NonNull RemoveErrorCode errorCode);
}
