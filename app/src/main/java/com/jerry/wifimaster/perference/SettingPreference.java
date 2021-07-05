package com.jerry.wifimaster.perference;

import android.content.Context;
import android.content.SharedPreferences;

import com.jerry.wifimaster.MainApplication;


public class SettingPreference implements SharedPreferences.OnSharedPreferenceChangeListener {


    public static final String KEY_PREFERENCE_NAME = "KEY_SETTING";
    //首屏隐私协议
    public static final String KEY_USER_AGREEMENT = "KEY_USER_AGREEMENT";

    //信号增强 储存时间
    public static final String KET_SIGNAL_TIME = "KET_SIGNAL_TIME";

    //信号增强 储存结果
    public static final String KET_SIGNAL_STREHGTH = "KET_SIGNAL_STREHGTH";


    public boolean isAgreement;
    public long singnalTime;
    public int singnalValue;

    private static SettingPreference mUserPres;

    private SettingPreference() {
        getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        loadPrefs(getSharedPreferences());
    }

    public static SettingPreference getInstance() {

        if (mUserPres == null) {
            mUserPres = new SettingPreference();
            // mEditor = getSharedPreferences().edit();
        }
        return mUserPres;
    }

    private void loadPrefs(SharedPreferences prefs) {

        this.isAgreement = prefs.getBoolean(KEY_USER_AGREEMENT, true);
        this.singnalTime=prefs.getLong(KET_SIGNAL_TIME,0);
        this.singnalValue=prefs.getInt(KET_SIGNAL_STREHGTH,-1);

    }


    public static SharedPreferences getSharedPreferences() {
        return MainApplication.getInstance().getSharedPreferences(KEY_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public void clear() {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.clear();
        loadPrefs(getSharedPreferences());

    }

    private static SharedPreferences.Editor mEditor;

    public static void save(String key, Object value) {
        //保存数据
        save(key, value, true);
    }

    public static void save(String key, Object value, boolean isSave) {
        if (mEditor == null) {
            mEditor = getSharedPreferences().edit();
        }
        if (value instanceof String) {
            mEditor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            mEditor.putInt(key, (int) value);
        } else if (value instanceof Float) {
            mEditor.putFloat(key, (float) value);
        } else if (value instanceof Long) {
            mEditor.putLong(key, (long) value);
        } else if (value instanceof Boolean) {
            mEditor.putBoolean(key, (boolean) value);
        }
        if (isSave) {
            mEditor.commit();
        }
    }

    public static void apply(String key, Object value) {
        //保存数据
        apply(key, value, true);
    }

    public static void apply(String key, Object value, boolean isSave) {
        if (mEditor == null) {
            mEditor = getSharedPreferences().edit();
        }
        if (value instanceof String) {
            mEditor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            mEditor.putInt(key, (int) value);
        } else if (value instanceof Float) {
            mEditor.putFloat(key, (float) value);
        } else if (value instanceof Long) {
            mEditor.putLong(key, (long) value);
        } else if (value instanceof Boolean) {
            mEditor.putBoolean(key, (boolean) value);
        }
        if (isSave) {
            mEditor.apply();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        loadPrefs(getSharedPreferences());
    }

    @Override
    protected void finalize() {
        getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
