package com.jerry.baselib.base;

import android.os.Bundle;

/**
 * Created by guqian on 2017/7/13.
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


//    void showLoading();
//
//    void showSuccess();
//
//    void showError();
//
//    void showThrowable();

}