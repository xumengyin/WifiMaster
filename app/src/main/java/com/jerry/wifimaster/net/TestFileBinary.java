package com.jerry.wifimaster.net;

import com.jerry.baselib.utils.LogUtils;
import com.yanzhenjie.nohttp.BasicBinary;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.tools.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class TestFileBinary extends BasicBinary {

    IUploadSpeed vUploadSpeedCallBack;

    public void setUploadSpeedCallBack(IUploadSpeed vCallBack) {
        this.vUploadSpeedCallBack = vCallBack;
    }
    InputStream inputStream;
    public TestFileBinary(InputStream inputStream) {
        super("upload", null);
       // fakeData();
        this.inputStream=inputStream;
    }


    @Override
    public long getBinaryLength() {
//        LogUtils.logi("getBinaryLength:"+fakeData.length/1024);
        try {
            return inputStream.available();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    protected InputStream getInputStream() throws IOException {
        return inputStream;
    }


    @Override
    public void onWriteBinary(OutputStream outputStream) {
        if (!isCancelled()) {
            InputStream inputStream = null;
            try {
                inputStream = getInputStream();
                if (inputStream == null) return;

                inputStream = IOUtils.toBufferedInputStream(inputStream);
                start();
                postStart();

                int oldProgress = 0;
                long totalLength = getLength();
                int len;

                byte[] buffer = new byte[4096];
                long hasUpCount = 0;
                long tempTime;
                long upCountOnce=0;
                long currentTime=System.currentTimeMillis();
                long speed=0;
                while (!isCancelled() && (len = inputStream.read(buffer)) != -1) {
                    hasUpCount++;

                    outputStream.write(buffer, 0, len);
                    outputStream.flush();
                    upCountOnce=upCountOnce+len;
                    LogUtils.logd("onWriteBinary  count: "+upCountOnce+"------length:"+totalLength);
                    tempTime=System.currentTimeMillis()-currentTime;
                    //1s 钟计算一次
                    if(tempTime> 1000)
                    {
                        currentTime=System.currentTimeMillis();
                        if (vUploadSpeedCallBack!=null) {
                            speed=(long)(upCountOnce/(tempTime/1000f));
                            vUploadSpeedCallBack.onUpload(speed);
                            LogUtils.logd("vUploadSpeedCallBack speed:"+speed);
                        }
                        upCountOnce=0;

                    }
                }
                LogUtils.logd("onWriteBinary  end:  iscancel?"+isCancelled());
            } catch (Exception e) {
                Logger.e(e);
                postError(e);
            } finally {
                IOUtils.closeQuietly(inputStream);
                postFinish();
            }
        }
        finish();
    }


    public interface IUploadSpeed
    {
        void onUpload(long speed);
    }
}
