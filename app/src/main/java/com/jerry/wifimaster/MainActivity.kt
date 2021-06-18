package com.jerry.wifimaster

import android.os.Bundle
import com.jerry.baselib.base.BaseActivity
import com.jerry.wifimaster.ui.WifiFragment
import com.qmuiteam.qmui.util.QMUIStatusBarHelper

open class MainActivity : BaseActivity() {
    val wifiFragment by lazy {
        WifiFragment()
    }

    override fun loadData(savedInstanceState: Bundle?) {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }


    override fun initViews() {
        QMUIStatusBarHelper.translucent(this)
        supportFragmentManager.beginTransaction().add(R.id.frame, wifiFragment)
            .commitAllowingStateLoss()
    }
}