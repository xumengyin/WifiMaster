package com.jerry.wifimaster.utils;

import android.os.Parcelable;

import com.jerry.baselib.http.AbstractRequest;
import com.jerry.baselib.http.EntityRequest;
import com.jerry.wifimaster.Constants;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.download.DownloadRequest;

public class NetRequestUtil {


    public static <T extends Parcelable> AbstractRequest<T> requestGet(T t) {
        EntityRequest<T> request =
                new EntityRequest(Constants.baseUrl, RequestMethod.GET, t.getClass());
        return request;
    }


    public static DownloadRequest downloadRequest()
    {
        DownloadRequest mRequest = new DownloadRequest(Constants.downloadUrl, RequestMethod.GET, Constants.downloadSaveLoc, "sou.apk", true, true);
        return mRequest;
    }



}
