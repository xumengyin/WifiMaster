package com.jerry.wifimaster.ui

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jerry.wifimaster.Constants
import com.jerry.wifimaster.R
import com.jerry.wifimaster.adapter.ScanDevicesAdapter
import com.jerry.wifimaster.devicescan.IP_MAC
import com.jerry.wifimaster.utils.CommonUtils
import com.jerry.wifimaster.utils.NetworkUtil
import kotlinx.android.synthetic.main.activity_detect.*

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
            vAdContentView = vAdContent;
        }
        return vAdContentView
    }

    override fun onCloseAds() {
        vMainAd.visibility = GONE
    }

    override fun loadData(savedInstanceState: Bundle?) {
        listData = intent.getSerializableExtra(Constants.INTENT_KEY) as MutableList<IP_MAC>
        scanAdapter.setList(listData)
        val m = CommonUtils.getManufacturer()
        if (m.isNullOrBlank()) {
            vXinghao.text = m
        } else {
            vXinghao.text = CommonUtils.getModel()
        }

        val ip = NetworkUtil.getLocalIp()
        if (ip.isNullOrBlank()) {
            vIp.text = "未知"
        } else {
            vIp.text = ip
        }
    }


    override fun initViews() {
        super.initViews()
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