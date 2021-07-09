package com.jerry.wifimaster.ui

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.NetworkUtils
import com.jerry.wifimaster.Constants
import com.jerry.wifimaster.R
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import kotlinx.android.synthetic.main.activity_signal_plus.*

class SignalPlusActivity : BaseNativeAdActivity() {


    override fun getLayoutId(): Int {
        return R.layout.activity_signal_plus
    }

    override fun getAdsContentView(): View {
        if (vAdContentView == null) {
            vAdContentView = vAdContent
        }
        return vAdContentView
    }

    override fun onCloseAds() {
        //vMainAd.visibility = GONE
    }

    override fun loadData(savedInstanceState: Bundle?) {

        val strehgth=intent.getIntExtra(Constants.INTENT_KEY,5)

        vSignal.text="${strehgth}%"
        vTiptitle.text="信号增强了${strehgth}%"
        //加载广告
        requestAds(Constants.ADS_XINXILIU)
        vWifiName.text=NetworkUtils.getSSID()

    }


    override fun initViews() {
        super.initViews()
        QMUIStatusBarHelper.translucent(this)
        val statusHeight = QMUIDisplayHelper.getStatusBarHeight(this)
        val params = uinv.layoutParams as ViewGroup.MarginLayoutParams
        params.topMargin = statusHeight
        uinv.layoutParams = params
        uinv.setNavigationTitle("增强结果")


    }
}