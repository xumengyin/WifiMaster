package com.jerry.wifimaster.devicescan;


import android.os.Build;
import android.util.Log;

import com.jerry.baselib.utils.LogUtils;
import com.jerry.wifimaster.utils.CommonUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by 郭攀峰 on 2015/10/24.
 *
 */
public class DeviceScanGroup implements Runnable
{
    private static final String tag = DeviceScanGroup.class.getSimpleName();

    private DeviceScanHandler mDeviceScanHandler;
    private int mGroupIndex;
    public Thread mThread;
    private List<IP_MAC> mIpMacList = new ArrayList<>();
    private DeviceScanTask[] mDeviceScanTaskArray = null;

    public DeviceScanGroup(DeviceScanHandler handler, int groupIndex)
    {
        this.mDeviceScanHandler = handler;
        this.mGroupIndex = groupIndex;

    }

    public void stop()
    {
        if (mIpMacList != null)
        {
            synchronized (this)
            {
                if (mDeviceScanTaskArray != null)
                {
                    for (int i = 0; i < mDeviceScanTaskArray.length; i++)
                    {
                        if (mDeviceScanTaskArray[i] != null)
                        {
                            mDeviceScanTaskArray[i].mThread.interrupt();
                            mDeviceScanTaskArray[i].mThread = null;
                        }
                    }
                    mDeviceScanTaskArray = null;
                }
            }
        }
    }

    @Override
    public void run()
    {
        if(CommonUtils.isAndroidQOrLater())
        {
            getIpMacFromArpCmd();
        }else
        {
            getIpMacFromFile();
        }
        Log.d(tag, "device scan group: " + mGroupIndex + " find " + mIpMacList.size()
            + " IP_MAC");

        if (mIpMacList.size() == 0 || Thread.interrupted())
            return;

        furtherScan();
    }

    private void furtherScan()
    {
        DeviceScanTask task;
        IP_MAC ip_mac;
        int taskNum = mIpMacList.size();
        Log.d(tag, "task num = " + taskNum);

        mDeviceScanTaskArray = new DeviceScanTask[taskNum];
        for (int i = 0; i < taskNum; i++)
        {
            task = new DeviceScanTask(this);
            if (mDeviceScanTaskArray != null)
            {
                ip_mac = mIpMacList.get(i);
                task.mThread = new Thread(task.mRunnable);
                task.setParams(ip_mac, mDeviceScanHandler);
                task.mThread.setPriority(Thread.MAX_PRIORITY);
                task.mThread.start();
                mDeviceScanTaskArray[i] = task;
            }
        }
    }

    private void getIpMacFromArpCmd()
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
                        //final String hwAddr = neighborLine[4];

                        InetAddress addr = InetAddress.getByName(ip);
                        if (addr.isLinkLocalAddress() || addr.isLoopbackAddress()) {
                            continue;
                        }
                        LogUtils.logd("--------------:"+line);
                        String macAddress = neighborLine[4];
                        String state = neighborLine[neighborLine.length - 1];
                        if(!"00:00:00:00:00:00".equalsIgnoreCase(macAddress))
                        {
                            IP_MAC ip_mac = new IP_MAC(ip, macAddress.toUpperCase(Locale.US));
                            int index = mDeviceScanHandler.mIpMacInLan.indexOf(ip_mac);
                            if (index == -1)
                            {
                                mIpMacList.add(ip_mac);
                                mDeviceScanHandler.mIpMacInLan.add(ip_mac);
                            }
                        }



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
            } finally {
                try {
                    if (br != null) {
                        br.close();
                    }
                } catch (IOException e) {
                }
            }

    }




    /**
     * 从proc/net/arp中读取ip_mac对
     */
    private void getIpMacFromFile()
    {
        String line;
        String ip;
        String mac;

        String regExp = "((([0-9,A-F,a-f]{1,2}" + ":" + "){1,5})[0-9,A-F,a-f]{1,2})";
        Pattern pattern;
        Matcher matcher;
        try
        {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(
                "/proc/net/arp"));
            if (bufferedReader != null)
            {
                bufferedReader.readLine(); //忽略标题行
                while ((line = bufferedReader.readLine()) != null)
                {
                    ip = line.substring(0, line.indexOf(" "));
                    pattern = Pattern.compile(regExp);
                    matcher = pattern.matcher(line);
                    if (matcher.find())
                    {
                        mac = matcher.group(1);
                        if (!mac.equals("00:00:00:00:00:00"))
                        {
                            IP_MAC ip_mac = new IP_MAC(ip, mac.toUpperCase(Locale.US));
                            Log.d(tag, "ip_mac = " + ip_mac.toString());
                            int index = mDeviceScanHandler.mIpMacInLan.indexOf(ip_mac);
                            if (index == -1)
                            {
                                mIpMacList.add(ip_mac);
                                mDeviceScanHandler.mIpMacInLan.add(ip_mac);
                            }
                        }
                    }
                }
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
