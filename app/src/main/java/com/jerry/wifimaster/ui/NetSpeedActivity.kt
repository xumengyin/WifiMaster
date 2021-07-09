package com.jerry.wifimaster.ui

import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.NetworkUtils
import com.jerry.wifimaster.Constants
import com.jerry.wifimaster.R
import com.jerry.wifimaster.bean.NetSpeedResult
import com.jerry.wifimaster.bean.SpeedTestEvent
import com.jerry.wifimaster.utils.CommonUtils
import kotlinx.android.synthetic.main.activity_net_speed.*
import org.greenrobot.eventbus.EventBus


class NetSpeedActivity : NetPseedBase() {
    override fun onCloseAds() {
        getAdsContentView().visibility = View.GONE
    }

    override fun loadData(savedInstanceState: Bundle?) {
        val result = intent.getSerializableExtra(Constants.INTENT_KEY) as NetSpeedResult

        apendSpanText(vLags, "${result.pings}", defaultUnit1)
        setMaxSpeedData(result.downloadSpeed, vDownload)
        setMaxSpeedData(result.uploadSpeed, vUpload)
        requestAds(Constants.ADS_XINXILIU)
        vWifiName.setText(NetworkUtils.getSSID())
        val speedTestEvent = SpeedTestEvent(result.downloadSpeed)
        CommonUtils.eventBusPostMsg(speedTestEvent)
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
        setDefaultStatusBar()
        setAppBarTitle("网络测速")

    }


}