package com.jerry.wifimaster;

import com.jerry.wifimaster.utils.FileUtils;

import java.io.File;

public class Constants {




    //请求地址
    public static final String baseUrl="https://www.baidu.com/";
    public static final String testPingUrl="baidu.coms";
    //下载地址
    public static final String rootFileLoc= FileUtils.getAppRootPath(MainApplication.getInstance()).getAbsolutePath()+ File.separator+"wifis";
    public static final String downloadUrl= "http://wap.dl.pinyin.sogou.com/wapdl/android/apk/SogouInput_android_v8.18_sweb.apk";
    public static final String downloadSaveLoc= rootFileLoc+File.separator+"download";


    public static final String WEB_AGREEMENT="https://www.baidu.com/";
    public static final String WEB_PRIVACY="https://www.baidu.com/";
    public static final String INTENT_KEY="INTENT_KEY";


    /**
     * 广告相关
     */

    public static final String ADS_APPID="a60c187f5dc2f2";
    public static final String ADS_APP_KEY="d690d889f2dc3a1e66d3809a15303be3";
    public static final String ADS_SPLASH="b60cc34b54ddad";
    public static final String ADS_XINXILIU="b60dc180aa8376";



}
