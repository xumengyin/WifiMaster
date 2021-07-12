package com.jerry.wifimaster;

import android.net.TrafficStats;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.jerry.baselib.utils.LogUtils;

import java.text.DecimalFormat;

public class NetSpeedHelper {

    Handler handler;
    private String Speed = "0KB/s";
    private long totalByte = 0;
    INetSpeed callBack;
    public static final int GAP = 4;//1S 查一次
    DecimalFormat df = new DecimalFormat("#.#");
    private boolean isStart=false;
    public NetSpeedHelper(INetSpeed callBack) {
        this.callBack = callBack;
        this.handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                int bytes = getNetSpeed();
                if (bytes > (1024 * 1024)) {
                    Speed = df.format(bytes / 1024f / 1024f) + "MB/s";
                } else if (bytes > 1024) {
                    Speed = bytes / 1024 + "KB/s";
                } else {
                    Speed = bytes + "B/s";
                }

                LogUtils.logd("speed:" + bytes);
                callBack.onSpeed(Speed);
                if(isStart)
                    sendMessageDelayed(obtainMessage(),GAP*1000);

            }
        };
    }

    public void start()
    {
        stop();
        totalByte = TrafficStats.getTotalRxBytes();
        handler.sendMessageDelayed(handler.obtainMessage(),GAP*1000);
        isStart=true;
    }

    public void stop()
    {
        isStart=false;
        handler.removeCallbacksAndMessages(null);
    }
    private int getNetSpeed() {
        long traffic_data;
        long tempTotal = TrafficStats.getTotalRxBytes();
        traffic_data = tempTotal - totalByte;//总的接受字节数
        totalByte = tempTotal;
        return (int) (traffic_data / GAP);
    }

    public interface INetSpeed {
        void onSpeed(String speed);
    }
}
