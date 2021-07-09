package com.jerry.baselib.base;

import android.os.Bundle;

/**
 *
 */

public interface BaseViewInterface {

    int getLayoutId();

    /**
     * 初始化view
     */
    void initViews();

    /**
     * 加载数据
     */
    void loadData(Bundle savedInstanceState);


    //是否注册eventbus
    boolean isRegisterEventBus();
//    void showLoading();
//
//    void showSuccess();
//
//    void showError();
//
//    void showThrowable();

}