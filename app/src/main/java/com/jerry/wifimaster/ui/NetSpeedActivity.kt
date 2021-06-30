package com.jerry.wifimaster.ui

import android.os.Bundle
import android.view.View
import com.jerry.wifimaster.R
import kotlinx.android.synthetic.main.activity_net_speed.*

class NetSpeedActivity : BaseNativeAdActivity() {
    override fun onCloseAds() {
        getAdsContentView().visibility = View.GONE
    }

    override fun loadData(savedInstanceState: Bundle?) {

    }

    override fun getAdsContentView(): View {
        if (vAdContentView == null) {
            vAdContentView = vAdContent
        }
        return vAdContentView
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_net_speed
    }

    override fun initViews() {
            super.initViews()
    }


}