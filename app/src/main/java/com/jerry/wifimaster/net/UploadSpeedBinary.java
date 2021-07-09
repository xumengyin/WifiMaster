package com.jerry.wifimaster.net;

import com.jerry.baselib.utils.LogUtils;
import com.yanzhenjie.nohttp.BasicBinary;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.tools.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class UploadSpeedBinary  extends BasicBinary {

    byte[] fakeData;
    IUploadSpeed vUploadSpeedCallBack;

    int cacheSize=4096;
    int loopCount=1000;
    long uploadSize=cacheSize*loopCount;
    public void setUploadSpeedCallBack(IUploadSpeed vCallBack) {
        this.vUploadSpeedCallBack = vCallBack;
    }

    public UploadSpeedBinary() {
        super("upload.a", null);
       // fakeData();
    }

    public void fakeData()
    {
        StringBuffer buffer =new StringBuffer();
        for (int i = 0; i < 100000; i++) {
            buffer.append("aaaaaaaaaa");
        }
        fakeData=buffer.toString().getBytes();
    }

    @Override
    public long getBinaryLength() {
//        LogUtils.logi("getBinaryLength:"+fakeData.length/1024);
        return uploadSize;
    }

    @Override
    protected InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(new byte[10]);
    }


    @Override
    public void onWriteBinary(OutputStream outputStream) {
        if (!isCancelled()) {
            InputStream inputStream = null;
            try {
                //inputStream = getInputStream();
               // if (inputStream == null) return;

                //inputStream = IOUtils.toBufferedInputStream(inputStream);
                start();
                postStart();

                int oldProgress = 0;
                long totalLength = getLength();
                int len;

                byte[] buffer = new byte[cacheSize];
               // Arrays.fill(buffer,(byte)'a');
                long hasUpCount = 0;
                long tempTime;
                long upCountOnce=0;
                long currentTime=System.currentTimeMillis();
                long speed=0;
                while (!isCancelled() && hasUpCount<loopCount) {
                    hasUpCount++;

                    outputStream.write(buffer, 0, buffer.length);
                    outputStream.flush();
                    upCountOnce=upCountOnce+cacheSize;
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
