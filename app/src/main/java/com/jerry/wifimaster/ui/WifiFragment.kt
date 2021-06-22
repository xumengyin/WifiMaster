package com.jerry.wifimaster.ui

import android.graphics.drawable.Drawable
import android.net.wifi.WifiInfo
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.jerry.baselib.utils.LogUtils
import com.jerry.baselib.utils.ToastUtil
import com.jerry.wifimaster.ConnectReceiver
import com.jerry.wifimaster.NetSpeedHelper
import com.jerry.wifimaster.R
import com.jerry.wifimaster.adapter.MenuAdapter
import com.jerry.wifimaster.adapter.WifisAdapter
import com.jerry.wifimaster.bean.Menus
import com.jerry.wifimaster.bean.WifiBean
import com.jerry.wifimaster.utils.CommonUtils
import com.jerry.wifimaster.utils.NetworkUtil
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.thanosfisherman.wifiutils.WifiUtils
import kotlinx.android.synthetic.main.wifi_fragment.*

class WifiFragment : BaseAdFragment() {

    lateinit var menuAdapter: MenuAdapter
    lateinit var wifisAdapter: WifisAdapter

    val connectReceiver by lazy {
        ConnectReceiver(this@WifiFragment.context)
    }
    val netSpeedHelper = NetSpeedHelper({
        //当前网速
        menuAdapter.data[0].descValue = it
        menuAdapter.notifyItemChanged(0)

    })

    override fun onStop() {
        super.onStop()
        netSpeedHelper.stop()
    }

    override fun onResume() {
        super.onResume()
        netSpeedHelper.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        connectReceiver.unRegister()
    }
    override fun initViews() {
        val statusHeight = QMUIDisplayHelper.getStatusBarHeight(context)
        vTopLayout.setPadding(0, statusHeight, 0, 0)
        connectReceiver.register()
        menuAdapter = MenuAdapter(Menus.menus)
        vMenuRv.apply {
            layoutManager = GridLayoutManager(this@WifiFragment.context, 4)
            adapter = menuAdapter
        }
        vWifiRv.apply {
            wifisAdapter = WifisAdapter(mutableListOf())
            layoutManager = LinearLayoutManager(this@WifiFragment.context)
            adapter = wifisAdapter
        }
    }

    override fun loadData(savedInstanceState: Bundle?) {
        super.loadData(savedInstanceState)
        scanWifi()
        initMenuData()
    }

    private fun initMenuData() {
        activity?.apply {
            var d: Drawable? = null
            var text = "未连接WIFI"
            if (NetworkUtil.getWifiEnabled(this)) {
                val netWork = NetworkUtil.getWifiInfo(this)
                if (netWork != null) {
                    LogUtils.logd(netWork.toString())
                    text = netWork.ssid
                    val strength = CommonUtils.getWifiStrength(netWork.rssi)
                    if (netWork.rssi < -100)
                        d = ContextCompat.getDrawable(this, R.drawable.wifi_b3)
                    else if (netWork.rssi > -50)
                        d = ContextCompat.getDrawable(this, R.drawable.wifi_b1)
                    else
                        d = ContextCompat.getDrawable(this, R.drawable.wifi_b2)
                    val connectSpeed =
                        if (netWork.linkSpeed == WifiInfo.LINK_SPEED_UNKNOWN) "未知" else "" + netWork.linkSpeed + WifiInfo.LINK_SPEED_UNITS
                    val ip = NetworkUtil.getLocalIp()
                    menuAdapter.data.forEachIndexed { index, menus ->
                        if (menus.type == MenuAdapter.TYPE_SIGNAL_STREHGTH) {
                            menus.descValue = strength
                            menuAdapter.notifyItemChanged(index)
                        } else if (menus.type == MenuAdapter.TYPE_CONNECT_SPEED) {
                            menus.descValue = connectSpeed
                            menuAdapter.notifyItemChanged(index)
                        } else if (menus.type == MenuAdapter.TYPE_IP) {
                            menus.descValue = ip
                            menuAdapter.notifyItemChanged(index)
                        }
                    }

                } else {
                    d = ContextCompat.getDrawable(this, R.drawable.wifi_b4)
                    text = "未连接WIFI"
                }
            } else {
                d = ContextCompat.getDrawable(this, R.drawable.wifi_b4)
                text = "未连接WIFI"
            }
            vWifiName.setText(text)
            vWifiName.setCompoundDrawables(d, null, null, null)


        }

    }

    private fun scanWifi() {
        activity?.apply {
            if (NetworkUtil.getWifiEnabled(this)) {
                var curSSid = ""
                val wifiInfo = NetworkUtil.getWifiInfo(this)
                curSSid = if (wifiInfo == null) "" else wifiInfo.ssid
                val dataList = mutableListOf<WifiBean>()
                WifiUtils.withContext(this).scanWifi { it ->
                    it.forEach {
                        LogUtils.logd("wifi ssid:${it.SSID} ----level:${it.level}")
                        if (!it.SSID.isNullOrEmpty()) {
                            val wifi = WifiBean(it)
                            if (!dataList.contains(wifi)) {
                                if (curSSid.isNotEmpty() &&curSSid == it.SSID) {
                                    dataList.add(0, wifi)
                                } else {
                                    dataList.add(wifi)
                                }
                            }
                        }
                    }
                    wifisAdapter.setList(dataList)
                }.start()

            } else {

                //todo 提示打开wifi
                ToastUtil.toast(this@WifiFragment.context, "请打开wifi")
//                WifiUtils.withContext(this).enableWifi {
//                    if (it) {
//                        Logs.d("enableWifi ok")
//                    } else {
//                        Logs.d("enableWifi fail")
//                    }
//
//                }
            }
        }

    }

    override fun getLayoutId(): Int {
        return R.layout.wifi_fragment
    }
}