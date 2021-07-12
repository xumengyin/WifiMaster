package com.jerry.wifimaster.devicescan;

import android.content.Context;
import android.os.Build;

import com.jerry.baselib.utils.LogUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.regex.Pattern;

/**
 * 发现在android 10上无法获取arp信息了 ，坑 换种实现
 */
public class DeviceScanManager29 implements IScan{


    @Override
    public void startScan(Context context, DeviceScanResult result) {

    }

    @Override
    public void stopScan() {

    }

    public static void findnet()
    {
        // String IP_CMD   ="ip neighbor";
        String IP_CMD   ="ip neigh show";
        BufferedReader br = null;
        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Process ipProc = Runtime.getRuntime().exec(IP_CMD);
                ipProc.waitFor();
                if (ipProc.exitValue() != 0) {
                    throw new Exception("Unable to access ARP entries");
                }

                br = new BufferedReader(new InputStreamReader(ipProc.getInputStream(), "UTF-8"));
                String line;
                while ((line = br.readLine()) != null) {
                    String[] neighborLine = line.split("\\s+");
                    if (neighborLine.length <= 4) {
                        continue;
                    }
                    String ip = neighborLine[0];
                    final String hwAddr = neighborLine[4];

                    InetAddress addr = InetAddress.getByName(ip);
                    if (addr.isLinkLocalAddress() || addr.isLoopbackAddress()) {
                        continue;
                    }
                    String macAddress = neighborLine[4];
                    String state = neighborLine[neighborLine.length - 1];


                    LogUtils.logd("--------------:"+line);

//                    if (!NEIGHBOR_FAILED.equals(state) && !NEIGHBOR_INCOMPLETE.equals(state)) {
//                        boolean isReachable = false;
//                        try {
//                            isReachable = InetAddress.getByName(ip).isReachable(5000);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        if (isReachable) {
//                            result.add(new WifiClient(ip, hwAddr));
//                        }
//                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
            }
        }
    }
}
