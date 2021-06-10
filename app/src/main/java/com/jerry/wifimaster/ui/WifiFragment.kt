package com.jerry.wifimaster.ui

import android.net.wifi.ScanResult
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jerry.wifimaster.MainActivity
import com.jerry.wifimaster.MainApplication
import com.jerry.wifimaster.R
import com.jerry.wifimaster.utils.Logs
import com.jerry.wifimaster.utils.NetworkUtil
import com.thanosfisherman.wifiutils.WifiUtils
import com.thanosfisherman.wifiutils.wifiScan.ScanResultsListener
import kotlinx.android.synthetic.main.wifi_fragment.*

class WifiFragment : BaseFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.wifi_fragment, null, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        testScan.setOnClickListener {
            scanWifi()
        }
    }


    fun scanWifi() {
        activity?.apply {
            if (NetworkUtil.getWifiEnabled(this)) {

                WifiUtils.withContext(this).scanWifi {
                    it.forEach {
                        Logs.d("scanWifi "+it.toString())
                    }

                }.start()

            } else {
                WifiUtils.withContext(this).enableWifi {
                    if (it) {
                        Logs.d("enableWifi ok")
                    } else {
                        Logs.d("enableWifi fail")
                    }

                }
            }
        }

    }
}