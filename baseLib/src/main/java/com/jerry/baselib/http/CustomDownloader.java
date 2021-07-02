package com.jerry.baselib.http;

import com.yanzhenjie.nohttp.NetworkExecutor;
import com.yanzhenjie.nohttp.download.DownloadListener;
import com.yanzhenjie.nohttp.download.DownloadRequest;
import com.yanzhenjie.nohttp.download.Downloader;

public class CustomDownloader extends Downloader {
    public CustomDownloader(NetworkExecutor executor) {
        super(executor);
    }

    @Override
    public void download(int what, DownloadRequest request, DownloadListener downloadListener) throws Exception {
        super.download(what, request, downloadListener);
    }
}
