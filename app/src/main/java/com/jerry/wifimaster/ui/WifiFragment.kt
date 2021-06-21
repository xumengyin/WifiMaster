package com.jerry.wifimaster.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.jerry.baselib.base.BaseFragment
import com.jerry.baselib.utils.LogUtils
import com.jerry.baselib.utils.ToastUtil
import com.jerry.wifimaster.R
import com.jerry.wifimaster.adapter.MenuAdapter
import com.jerry.wifimaster.adapter.WifisAdapter
import com.jerry.wifimaster.bean.Menus
import com.jerry.wifimaster.bean.WifiBean
import com.jerry.wifimaster.utils.Logs
import com.jerry.wifimaster.utils.NetworkUtil
import com.qmuiteam.qmui.util.QMUIDeviceHelper
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.thanosfisherman.wifiutils.WifiUtils
import kotlinx.android.synthetic.main.wifi_fragment.*

class WifiFragment : BaseAdFragment() {

    lateinit var menuAdapter: MenuAdapter
    lateinit var wifisAdapter: WifisAdapter

    override fun initViews() {
        val statusHeight = QMUIDisplayHelper.getStatusBarHeight(context)
        vTopLayout.setPadding(0, statusHeight, 0, 0)
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
    private fun initMenuData()
    {
        activity?.apply {
            var d: Drawable? =null
            var text="未连接WIFI"
            if (NetworkUtil.getWifiEnabled(this))
            {
                val netWork=NetworkUtil.getWifiInfo(this)
                if(netWork!=null)
                {
                    d=ContextCompat.getDrawable(this,R.drawable.wifi_b1)
                    text=netWork.ssid
                }else
                {
                    d=ContextCompat.getDrawable(this,R.drawable.wifi_b4)
                    text="未连接WIFI"
                }
            }else
            {
                d=ContextCompat.getDrawable(this,R.drawable.wifi_b4)
                text="未连接WIFI"
            }
            vWifiName.setText(text)
            vWifiName.setCompoundDrawables(d,null,null,null)
        }

    }
    fun scanWifi() {
        activity?.apply {
            if (NetworkUtil.getWifiEnabled(this)) {
                val curSSid = NetworkUtil.getWifiInfo(this).ssid
                val dataList = mutableListOf<WifiBean>()
                WifiUtils.withContext(this).scanWifi { it ->
                    it.forEach {
                        LogUtils.logd("wifi ssid:${it.SSID} ----level:${it.level}")
                        if(!it.SSID.isNullOrEmpty())
                        {
                            val wifi = WifiBean(it)
                            if (!dataList.contains(wifi)) {
                                if (curSSid == it.SSID) {
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