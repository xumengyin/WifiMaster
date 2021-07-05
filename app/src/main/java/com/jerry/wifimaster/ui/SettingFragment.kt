package com.jerry.wifimaster.ui

import android.os.Bundle
import android.view.View
import com.jerry.wifimaster.Constants
import com.jerry.wifimaster.R
import kotlinx.android.synthetic.main.f_setting.*

class SettingFragment : BaseNativeAdFragment() {
    override fun onCloseAds() {
        getAdsContentView().visibility = View.GONE
    }

    override fun getAdsContentView(): View {
        if (vAdContentView == null) {
            vAdContentView = vAdContent
        }
        return vAdContentView
    }

    override fun getLayoutId(): Int {
        return R.layout.f_setting
    }

    override fun loadData(savedInstanceState: Bundle?) {
        super.loadData(savedInstanceState)
        requestAds(Constants.ADS_XINXILIU)
    }

}