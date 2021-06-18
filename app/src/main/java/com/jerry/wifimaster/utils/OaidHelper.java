package com.jerry.wifimaster.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.bun.miitmdid.core.ErrorCode;
import com.bun.miitmdid.core.MdidSdkHelper;
import com.bun.miitmdid.interfaces.IIdentifierListener;
import com.bun.miitmdid.interfaces.IdSupplier;


/**
 * Created by caict on 2020/6/8.
 */

public class OaidHelper implements IIdentifierListener {

    public static final String SP_NAME = "device_info";
    public static final String SP_KEY_OAID_ID = "device_oaid";

    private Context context;
    private AppIdsUpdater _listener;

    private SharedPreferences sharedPreferences;

    public OaidHelper(AppIdsUpdater callback) {
        _listener = callback;
    }

    public void getDeviceIds(Context cxt) {
        context = cxt;
        sharedPreferences = cxt.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        String oaid = sharedPreferences.getString(SP_KEY_OAID_ID, null);
        if (!TextUtils.isEmpty(oaid)) {
            if (_listener != null) {
                //直接回调
                _listener.OnIdsAvalid(oaid);
            }
        } else {

            long timeb = System.currentTimeMillis();
            // 方法调用
            int nres = CallFromReflect(cxt);

            long timee = System.currentTimeMillis();
            long offset = timee - timeb;
            if (nres == ErrorCode.INIT_ERROR_DEVICE_NOSUPPORT) {//不支持的设备
                if (_listener != null) {
                    _listener.OnIdsAvalidError();
                }
            } else if (nres == ErrorCode.INIT_ERROR_LOAD_CONFIGFILE) {//加载配置文件出错
                if (_listener != null) {
                    _listener.OnIdsAvalidError();
                }
            } else if (nres == ErrorCode.INIT_ERROR_MANUFACTURER_NOSUPPORT) {//不支持的设备厂商
                if (_listener != null) {
                    _listener.OnIdsAvalidError();
                }
            } else if (nres == ErrorCode.INIT_ERROR_RESULT_DELAY) {//获取接口是异步的，结果会在回调中返回，回调执行的回调可能在工作线程

            } else if (nres == ErrorCode.INIT_HELPER_CALL_ERROR) {//反射调用出错
                if (_listener != null) {
                    _listener.OnIdsAvalidError();
                }
            } else {
                if (_listener != null) {
                    _listener.OnIdsAvalidError();
                }
            }
            Log.d(getClass().getSimpleName(), "return value: " + String.valueOf(nres));

        }
    }

    /*
     * 方法调用
     *
     * */
    private int CallFromReflect(Context cxt) {
        return MdidSdkHelper.InitSdk(cxt, true, this);
    }

    /*
     * 获取相应id
     *
     * */
    @Override
    public void OnSupport(boolean isSupport, IdSupplier _supplier) {

        if (_supplier == null) {
            if (_listener != null) {
                _listener.OnIdsAvalidError();
            }
            return;
        }
        String oaid = _supplier.getOAID();
        String vaid = _supplier.getVAID();
        String aaid = _supplier.getAAID();
        //保存
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SP_KEY_OAID_ID, oaid);
        editor.apply();

        StringBuilder builder = new StringBuilder();
        builder.append("support: ").append(isSupport ? "true" : "false").append("\n");
        builder.append("OAID: ").append(oaid).append("\n");
        builder.append("VAID: ").append(vaid).append("\n");
        builder.append("AAID: ").append(aaid).append("\n");
        String idstext = builder.toString();
        if (_listener != null) {
            _listener.OnIdsAvalid(oaid);
        }
    }

    public interface AppIdsUpdater {
        void OnIdsAvalidError();

        void OnIdsAvalid(String oaid);
    }

}
