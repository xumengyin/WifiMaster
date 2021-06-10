package com.jerry.wifimaster.devicescan;


import android.content.Context;
import android.os.Handler;
import android.os.Message;


import com.jerry.wifimaster.utils.Constant;

import java.lang.ref.WeakReference;


/**
 *.
 */
public class DeviceScanManager
{
    private static final String tag = DeviceScanManager.class.getSimpleName();

    private DeviceScanManagerHandler mUiHandler;
    private DeviceScanHandler mDeviceScanHandler;
    private CustomHandlerThread mHandlerThread;

    private DeviceScanResult mScanResult;

    public DeviceScanManager()
    {
        mUiHandler = new DeviceScanManagerHandler(this);
    }

    public void startScan(Context context, DeviceScanResult scanResult)
    {
        mScanResult = scanResult;

        mHandlerThread = new CustomHandlerThread("DeviceScanThread",
            DeviceScanHandler.class);
        mHandlerThread.start();
        mHandlerThread.isReady();

        mDeviceScanHandler = (DeviceScanHandler) mHandlerThread.getLooperHandler();
        mDeviceScanHandler.init(context, mUiHandler);

        mDeviceScanHandler.sendMessage(mDeviceScanHandler
                .obtainMessage(Constant.MSG.START));
    }

    public void stopScan()
    {
        if (mHandlerThread != null)
        {
            mDeviceScanHandler.sendMessage(mDeviceScanHandler
                    .obtainMessage(Constant.MSG.STOP));

            mHandlerThread.quit();
            mHandlerThread = null;
        }
    }

    public DeviceScanManagerHandler getUIHandler()
    {
        return mUiHandler;
    }

    public static class DeviceScanManagerHandler extends Handler
    {
        private WeakReference<DeviceScanManager> weakReference;

        public DeviceScanManagerHandler(DeviceScanManager manager)
        {
            weakReference = new WeakReference<>(manager);
        }

        @Override
        public void handleMessage(Message msg)
        {
            DeviceScanManager manager = weakReference.get();
            if (manager == null)
                return;
            switch (msg.what)
            {
                case Constant.MSG.SCAN_ONE :
                    if (manager.mScanResult != null)
                    {
                        IP_MAC ip_mac = (IP_MAC) msg.obj;
                        if (ip_mac != null)
                            manager.mScanResult.deviceScanResult(ip_mac);

                    }
                    break;
                case Constant.MSG.SCAN_OVER :
                    break;
            }
        }
    }
}
