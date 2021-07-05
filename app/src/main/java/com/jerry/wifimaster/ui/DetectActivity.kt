package com.jerry.wifimaster.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jerry.wifimaster.Constants
import com.jerry.wifimaster.R
import com.jerry.wifimaster.adapter.ScanDevicesAdapter
import com.jerry.wifimaster.devicescan.IP_MAC
import com.jerry.wifimaster.utils.CommonUtils
import com.jerry.wifimaster.utils.DeviceScanNetworkUtil
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import kotlinx.android.synthetic.main.activity_detect.*
import kotlinx.android.synthetic.main.activity_detect.uinv
import kotlinx.android.synthetic.main.activity_detective_ani.*

class DetectActivity : BaseNativeAdActivity() {

    val scanAdapter by lazy {
        ScanDevicesAdapter(mutableListOf())
    }
    var listData = mutableListOf<IP_MAC>()
    override fun getLayoutId(): Int {
        return R.layout.activity_detect
    }

    override fun getAdsContentView(): View {
        if (vAdContentView == null) {
            vAdContentView = vAdContent
        }
        return vAdContentView
    }

    override fun onCloseAds() {
        vMainAd.visibility = GONE
    }

    override fun loadData(savedInstanceState: Bundle?) {
        listData = intent.getSerializableExtra(Constants.INTENT_KEY) as MutableList<IP_MAC>

        vWifiNum.text="共${listData.size}台设备连接当前WIFI"
        scanAdapter.setList(listData)
        val m = CommonUtils.getManufacturer()
        if (m.isNullOrBlank()) {
            vXinghao.text = m
        } else {
            vXinghao.text = CommonUtils.getModel()
        }

        val ip = DeviceScanNetworkUtil.getLocalIp()
        if (ip.isNullOrBlank()) {
            vIp.text = "未知"
        } else {
            vIp.text = ip
        }
        //加载广告
        requestAds(Constants.ADS_XINXILIU)
    }


    override fun initViews() {
        super.initViews()
        QMUIStatusBarHelper.translucent(this)
        val statusHeight = QMUIDisplayHelper.getStatusBarHeight(this)
        val params = uinv.layoutParams as ViewGroup.MarginLayoutParams
        params.topMargin = statusHeight
        uinv.layoutParams = params
        uinv.setNavigationTitle("安全检测")
        vCheck.setOnClickListener {
            startActivity(Intent(this@DetectActivity,DetectiveAniActivity::class.java))
        }
        vDeviceRv.apply {
            layoutManager = LinearLayoutManager(this@DetectActivity)
            adapter = scanAdapter
            //scanAdapter.setEmptyView()

            val divider = DividerItemDecoration(this@DetectActivity, LinearLayoutManager.VERTICAL)
            ContextCompat.getDrawable(context, R.drawable.base_divider)
                ?.let { divider.setDrawable(it) }
            addItemDecoration(divider)

        }

    }
}