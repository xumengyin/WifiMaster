package com.jerry.wifimaster.ui

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.jerry.wifimaster.Constants
import com.jerry.wifimaster.MainApplication
import com.jerry.wifimaster.R
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.f_setting.*
import javax.inject.Inject

@AndroidEntryPoint
open class SettingFragment @Inject constructor(): BaseNativeAdFragment() {
    override fun onCloseAds() {
        getAdsContentView().visibility = View.GONE
    }

    override fun getAdsContentView(): View {
        if (vAdContentView == null) {
            vAdContentView = vAdContent
        }
        return vAdContentView
    }
    override fun initViews() {
        super.initViews()
        val statusHeight = QMUIDisplayHelper.getStatusBarHeight(activity)
        val params = uinv.layoutParams as ViewGroup.MarginLayoutParams
        params.topMargin = statusHeight
        uinv.layoutParams = params
        uinv.setNavigationTitle("设置")
        uinv.navigationBack.visibility=View.GONE
        uinv.navigationTitle.setTextColor(ContextCompat.getColor(MainApplication.getInstance(),R.color.text_title_color1))
    }
    override fun getLayoutId(): Int {
        return R.layout.f_setting
    }

    override fun loadData(savedInstanceState: Bundle?) {
        super.loadData(savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        val adsView=adsContentView as ViewGroup
        if(adsView.childCount<=0)
            requestAds(Constants.ADS_XINXILIU)
    }
}