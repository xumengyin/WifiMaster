package com.jerry.wifimaster.ui

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.wifi.WifiInfo
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.NetworkUtils
import com.jerry.baselib.utils.LogUtils
import com.jerry.baselib.utils.ToastUtil
import com.jerry.wifimaster.*
import com.jerry.wifimaster.adapter.MenuAdapter
import com.jerry.wifimaster.adapter.WifisAdapter
import com.jerry.wifimaster.bean.Menus
import com.jerry.wifimaster.bean.SpeedTestEvent
import com.jerry.wifimaster.bean.WifiBean
import com.jerry.wifimaster.bean.WifiPanelMenu
import com.jerry.wifimaster.perference.SettingPreference
import com.jerry.wifimaster.ui.dialog.BaseAlertDialog
import com.jerry.wifimaster.ui.dialog.BottomPanel
import com.jerry.wifimaster.utils.CommonUtils
import com.jerry.wifimaster.utils.DeviceScanNetworkUtil
import com.jerry.wifimaster.utils.LocationUtils
import com.jerry.wifimaster.wifiutils.WifiUtils
import com.jerry.wifimaster.wifiutils.wifiConnect.ConnectionErrorCode
import com.jerry.wifimaster.wifiutils.wifiConnect.ConnectionSuccessListener
import com.jerry.wifimaster.wifiutils.wifiRemove.RemoveErrorCode
import com.jerry.wifimaster.wifiutils.wifiRemove.RemoveSuccessListener
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import kotlinx.android.synthetic.main.wifi_fragment.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

class WifiFragment : BaseNativeAdFragment() {

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
    var isConnectWifiOrDisConnect = false
    var scanWifiTime: Long = 0

    //    initMenuData()
//    scanWifi()
    val connectReceiverCallBack = object : ConnectReceiver.IConnectRec {
        override fun onConnect() {
            LogUtils.logd("connectReceiverCallBack onConnect")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                initMenuData()
                if (LocationUtils.isLocationEnabled()) {
                    // scanWifi()
                } else {
                    //
                    // ToastUtil.toast(activity,"定位 未启动")

                    val dialog =
                        BaseAlertDialog.createCommonDialog(activity, "提示", "为了获取更精确的数据,请开启GPS权限")
                    dialog.setPositiveButton("去授权")
                    dialog.setPositiveButtonListener {
                        CommonUtils.gotoGpsSetting(this@WifiFragment)
                    }
                    dialog.show()
                }
            } else {
                initMenuData()
                // scanWifi()
            }
            uodateScanWifis()
        }

        override fun onDisConnect() {
            LogUtils.logd("connectReceiverCallBack onDisConnect")
            //wifi被关了
            initMenuData()
           // uodateScanWifis()
            wifisAdapter.setList(mutableListOf())
        }

    }

    private fun uodateScanWifis() {
        val newDatas = mutableListOf<WifiBean>()
        //Collections.copy(newDatas,wifisAdapter.data)
        newDatas.addAll(wifisAdapter.data)
        if (newDatas.isNotEmpty()) {
            val wifiName = NetworkUtils.getSSID()
            wifisAdapter.setCurWifi(wifiName)
            newDatas.sort()
            if (wifiName.isNotEmpty()) {
                val fintItem = newDatas.find {
                    it.result.SSID == wifiName
                }
                fintItem?.apply {
                    newDatas.remove(this)
                    newDatas.add(0, this)
                }
            }
            wifisAdapter.setList(newDatas)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onMessageEvent(speedEvent: SpeedTestEvent) {
        //更新网速
        if (menuAdapter.data[1].type == MenuAdapter.TYPE_NET_SPEED_N || menuAdapter.data[1].type == MenuAdapter.TYPE_NET_SPEED_Y) {
            menuAdapter.data[1].type = MenuAdapter.TYPE_NET_SPEED_Y
            menuAdapter.data[1].descValue = speedEvent.speed
            menuAdapter.notifyItemChanged(1)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CommonUtils.GPS_KEY) {
            initMenuData()
            // scanWifi()
        }
    }

    override fun onStop() {
        super.onStop()
        netSpeedHelper.stop()
        cancelTimer()
    }

    var timer: Timer? = null
    var timerTask: TimerTask? = null
    val mainHandler = Handler(Looper.getMainLooper())

    //2分钟只能扫描4次
    private fun timerScan() {
        cancelTimer()
        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() {
                if (System.currentTimeMillis() - scanWifiTime > Constants.SCAN_WIFI_GAP) {
                    mainHandler.post {
                        scanWifi()
                    }
                }
            }
        }
        timer?.schedule(timerTask, 1000, Constants.SCAN_WIFI_GAP)
    }

    private fun cancelTimer() {
        timer?.cancel()
        timerTask?.cancel()
    }

    override fun onResume() {
        super.onResume()
        netSpeedHelper.start()
        connectReceiver.register()
        connectReceiver.setCallBack(connectReceiverCallBack)
        timerScan()
    }

    override fun onPause() {
        super.onPause()
        connectReceiver.unRegister()
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun initViews() {
        super.initViews()
        vWifiName.setOnClickListener {
            //WifiUtilsCompat.findnet()
        }
        val statusHeight = QMUIDisplayHelper.getStatusBarHeight(context)
        vTopLayout.setPadding(0, statusHeight, 0, 0)

        //initMenuData()
        menuAdapter = MenuAdapter(null)
        vMenuRv.apply {
            layoutManager = GridLayoutManager(this@WifiFragment.context, 4)
            adapter = menuAdapter
            itemAnimator = null
        }
        vWifiRv.apply {
            wifisAdapter = WifisAdapter(mutableListOf())
            layoutManager = LinearLayoutManager(this@WifiFragment.context)
            adapter = wifisAdapter
            val divider =
                DividerItemDecoration(this@WifiFragment.context, LinearLayoutManager.VERTICAL)
            ContextCompat.getDrawable(context, R.drawable.base_divider)
                ?.let { divider.setDrawable(it) }
            addItemDecoration(divider)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                }
            })
        }
        //收起面板
        vPanel.setOnClickListener {
            if (menuAdapter.showPanel) {
                menuAdapter.setList(Menus.getHideMenu())
                ratatePanel(true)
            } else {
                //initMenuData()
                menuAdapter.setList(Menus.menus)
                ratatePanel(false)
            }
            menuAdapter.showPanel = !menuAdapter.showPanel
        }
        //菜单点击事件
        menuAdapter.setOnItemClickListener { adapter, view, position ->
            val data = menuAdapter.data[position]
            when (data.type) {
                MenuAdapter.TYPE_SIGNAL_PLUS -> {
                    //信号增强
                    if(checkWifiEnable())
                    {
                        val gapTime =
                            System.currentTimeMillis() - SettingPreference.getInstance().singnalTime
                        val intent: Intent
                        if (gapTime < 1000 * 60 * 3) {
                            intent = Intent(activity, SignalPlusActivity::class.java)
                            intent.putExtra(
                                Constants.INTENT_KEY,
                                SettingPreference.getInstance().singnalValue
                            )
                        } else {
                            intent = Intent(activity, SignalPlusAniActivity::class.java)
                        }
                        startActivity(intent)
                    }


                }
                MenuAdapter.TYPE_NET_SPEED_N,
                MenuAdapter.TYPE_NET_SPEED_Y -> {
                    //测速
                    if(checkWifiEnable())
                    {
                        startActivity(Intent(activity, NetSpeedAniActivity::class.java))
                    }


                }
                MenuAdapter.TYPE_TEST->{
                    if(checkWifiEnable())
                    {
                        //安全检测
                        startActivity(Intent(activity, DetectiveAniActivity::class.java))
                    }

                }

            }

        }
        wifisAdapter.setOnItemClickListener { adapter, view, position ->
            val item = wifisAdapter.data[position]
            item.apply {
                var bottomDialog: BottomPanel
                if (wifisAdapter.wifiName == result.SSID) {
                    bottomDialog =
                        BottomPanel.createCurNetDialog(this@WifiFragment.context, result.SSID)
                } else {
                    bottomDialog =
                        BottomPanel.createPassNetDialog(this@WifiFragment.context, result.SSID)
                }
//                bottomDialog.set
                bottomDialog.setOnSheetItemClickListener { dialog, itemView ->

                    dialog.dismiss()
                    val menuItem = itemView.tag as WifiPanelMenu
                    dealPanelMenu(menuItem, item)
                }
                bottomDialog.build().show()
            }

        }
//        LogUtils.d("mmm", "recyclerView dy:${dy}")
        val scrollview=vMainScrollView as NestedScrollView
       scrollview.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
          // LogUtils.d("mmm", "recyclerView scrollY:${scrollY}---oldScrollY:${oldScrollY}")

       }
    }
    private fun checkWifiEnable():Boolean
    {
        var enable=true
        if(!NetworkUtils.getWifiEnabled())
        {
            ToastUtil.toast(MainApplication.getInstance(),"请先打开WIFI")
            CommonUtils.gotoWifiSetting(activity)
            enable=false
        }
        return  enable
    }
    private fun dealPanelMenu(menu: WifiPanelMenu, wifiInfo: WifiBean) {

        if(!checkWifiEnable())
            return
        when (menu.type) {
            WifiPanelMenu.TYPE_PASS_CONNECT -> {
                //WifiUtils.withContext(MainApplication.getInstance()).

                if (DeviceScanNetworkUtil.isWifiNeedPass(wifiInfo.result)) {
                    val dialog =
                        BaseAlertDialog.createPasswordDialog(activity, wifiInfo.result.SSID)
                    dialog.setNegativeButtonListener {
                        dialog.dismiss()
                    }
                    dialog.setPositiveButton("连接")
                    dialog.setPositiveButtonListener {
                        val editText = dialog.customView.findViewById<EditText>(R.id.passEdit)
                        // ConnectorUtils.con
                        val text = editText.text.toString()
                        if (text.isNullOrEmpty()) {
                            ToastUtil.toast(MainApplication.getInstance(), "请输入密码")
                        } else {

//                            val wifiUtilsCompat=WifiUtilsCompat(requireContext())
//                            wifiUtilsCompat.connectWifiP2p(requireContext(),wifiInfo.result.SSID,text)

                            isConnectWifiOrDisConnect = true
                            WifiUtils.withContext(MainApplication.getInstance())
                                .connectWithSingleScan(text, wifiInfo.result)
                                //.connectWith(wifiInfo.result.SSID, wifiInfo.result.BSSID, text)
                                .onConnectionResult(object : ConnectionSuccessListener {
                                    override fun failed(errorCode: ConnectionErrorCode) {
                                        if (errorCode == ConnectionErrorCode.AUTHENTICATION_ERROR_OCCURRED) {
                                            ToastUtil.toast(
                                                MainApplication.getInstance(),
                                                "密码错误",
                                                R.drawable.toast_wrong
                                            )
                                        } else {
                                            ToastUtil.toast(
                                                MainApplication.getInstance(),
                                                "连接失败",
                                                R.drawable.toast_wrong
                                            )
                                        }

                                        isConnectWifiOrDisConnect = false
                                    }

                                    override fun success() {
                                        isConnectWifiOrDisConnect = false
                                        ToastUtil.toast(
                                            MainApplication.getInstance(),
                                            "wifi连接成功",
                                            R.drawable.toast_ok
                                        )
                                    }

                                }).startConnect()


                            KeyboardUtils.hideSoftInput(editText)
                            dialog.dismiss()

                        }


                    }
                    dialog.show()

                } else {
                    //不需要密码 直接连接
                    WifiUtils.withContext(MainApplication.getInstance())
                        .connectWith(wifiInfo.result.SSID, wifiInfo.result.BSSID, "")
                        .onConnectionResult(object : ConnectionSuccessListener {
                            override fun failed(errorCode: ConnectionErrorCode) {
                                ToastUtil.toast(MainApplication.getInstance(), "连接失败")
                            }

                            override fun success() {
                                ToastUtil.toast(
                                    MainApplication.getInstance(),
                                    "wifi连接成功",
                                    R.drawable.toast_ok
                                )
                            }

                        }).start()
                }

            }
            WifiPanelMenu.TYPE_REPORT -> {
                //报告
                val ssid = wifiInfo.result.SSID
                val dialog = BaseAlertDialog.createCommonDialog(
                    activity,
                    wifiInfo.result.SSID,
                    "你确定要举报${ssid}作为钓鱼WIFI吗?"
                )
                dialog.setPositiveButton("确认举报")
                dialog.setPositiveButtonListener {
                    ToastUtil.toast(MainApplication.getInstance(), "举报成功")
                }
                dialog.show()

            }
            WifiPanelMenu.TYPE_TEST_SPEED -> {
                //测速度
                startActivity(Intent(activity, NetSpeedAniActivity::class.java))
            }
            WifiPanelMenu.TYPE_CHECK -> {
                //安全检测
                startActivity(Intent(activity, DetectiveAniActivity::class.java))

            }
            WifiPanelMenu.TYPE_FORGET_NET -> {
                val ssid = wifiInfo.result.SSID
                val dialog = BaseAlertDialog.createCommonDialog(
                    activity,
                    wifiInfo.result.SSID,
                    "你确定要忘记已保存的${ssid}的密码吗?"
                )
                dialog.setPositiveButton("确认")
                dialog.setPositiveButtonListener {
                    WifiUtils.withContext(MainApplication.getInstance()).remove(ssid, object :
                        RemoveSuccessListener {
                        override fun failed(errorCode: RemoveErrorCode) {
                            ToastUtil.toast(MainApplication.getInstance(), "由于系统限制,忘记网络失败")
                        }

                        override fun success() {
                            ToastUtil.toast(MainApplication.getInstance(), "断开成功")
                        }

                    })
                }
                dialog.show()
            }
        }

    }

    private fun ratatePanel(hide: Boolean) {
//        vPanel.pivotX=0.5f
//        vPanel.pivotY=0.5f
        if (hide)
            vPanel.setImageResource(R.drawable.panel_handle2)
        else
            vPanel.setImageResource(R.drawable.panel_handle)
    }

    override fun onCloseAds() {
        vMainAd.visibility = View.GONE
    }

    override fun loadData(savedInstanceState: Bundle?) {
        super.loadData(savedInstanceState)

        requestAds(Constants.ADS_XINXILIU)
    }

    override fun getAdsContentView(): View {
        if (vAdContentView == null) {
            vAdContentView = vWifiAdsLayout
        }
        return vAdContentView
    }

    private fun initMenuData() {
        activity?.apply {
            var d: Drawable? = null
            var text = "未连接WIFI"
            if (DeviceScanNetworkUtil.getWifiEnabled(this)) {
                val netWork = DeviceScanNetworkUtil.getWifiInfo(this)
                if (netWork != null) {
                    LogUtils.logd(netWork.toString())
                    if (menuAdapter.data.isEmpty()) {
                        if (menuAdapter.showPanel) {
                            menuAdapter.setList(Menus.menus)
                        } else {
                            menuAdapter.setList(Menus.getHideMenu())
                        }
                    }
                    text = netWork.ssid.replace("\"", "")
                    //设置当前ssid
                    wifisAdapter.setCurWifi(text)
                    val strength = CommonUtils.getWifiStrength(netWork.rssi)
                    if (netWork.rssi <= -100)
                        d = ContextCompat.getDrawable(this, R.drawable.wifi_b3)
                    else if (netWork.rssi >= -60)
                        d = ContextCompat.getDrawable(this, R.drawable.wifi_b1)
                    else
                        d = ContextCompat.getDrawable(this, R.drawable.wifi_b2)
                    //WifiInfo.LINK_SPEED_UNKNOWN
                    val connectSpeed =
                        if (netWork.linkSpeed == -1) "未知" else "" + netWork.linkSpeed + WifiInfo.LINK_SPEED_UNITS
                    val ip = DeviceScanNetworkUtil.getLocalIp()
                    Menus.menus.forEachIndexed { index, menus ->
                        if (menus.type == MenuAdapter.TYPE_SIGNAL_STREHGTH) {
                            menus.descValue = strength
                            if (index < menuAdapter.data.size)
                                menuAdapter.notifyItemChanged(index)
                        } else if (menus.type == MenuAdapter.TYPE_CONNECT_SPEED) {
                            menus.descValue = connectSpeed
                            if (index < menuAdapter.data.size)
                                menuAdapter.notifyItemChanged(index)
                        } else if (menus.type == MenuAdapter.TYPE_IP) {
                            menus.descValue = ip
                            if (index < menuAdapter.data.size)
                                menuAdapter.notifyItemChanged(index)
                        }
                    }
                    vPanel.visibility = View.VISIBLE
                } else {
                    d = ContextCompat.getDrawable(this, R.drawable.wifi_b4)
                    text = "未连接WIFI"
                    menuAdapter.setList(Menus.unConnectMenu)
                    vPanel.visibility = View.GONE
                }

            } else {
                d = ContextCompat.getDrawable(this, R.drawable.wifi_b4)
                text = "未连接WIFI"
                menuAdapter.setList(Menus.unConnectMenu)
                vPanel.visibility = View.GONE
            }
            vWifiName.setText(text)
            d?.setBounds(0, 0, d.minimumWidth, d.minimumHeight)
            vWifiName.setCompoundDrawables(d, null, null, null)

        }

    }

    private fun scanWifi() {
        if (isConnectWifiOrDisConnect) {
            LogUtils.logd("  正在连接或断开wifi scanWifi 不能执行")
            return
        }

        LogUtils.logd("scanWifi 执行")
        activity?.apply {
            if (DeviceScanNetworkUtil.getWifiEnabled(this)) {
                val curSSid = NetworkUtils.getSSID()
                val dataList = mutableListOf<WifiBean>()
                var ownWifiBean: WifiBean? = null
                WifiUtils.withContext(MainApplication.getInstance()).scanWifi { it ->
                    LogUtils.logd("scanWifi  finish ${it.size}")
                    scanWifiTime = System.currentTimeMillis()
                    it.forEach {
                        LogUtils.logd("wifi ssid:${it.SSID} ----level:${it.level}")
                        if (!it.SSID.isNullOrEmpty()) {
                            val wifi = WifiBean(it)
                            val oldMan = dataList.find { it2 ->
                                it2.result.SSID == wifi.result.SSID && it2.result.level < wifi.result.level
                            }
                            if (oldMan == null) {
                                if (curSSid.isNotEmpty() && curSSid == it.SSID) {
                                    //dataList.add(0, wifi)
                                    if (ownWifiBean == null || ownWifiBean?.result?.level!! < wifi.result.level) {
                                        ownWifiBean = wifi
                                    }
                                } else {
                                    dataList.add(wifi)
                                }
                            } else {
                                dataList.remove(oldMan)
                                dataList.add(wifi)
                            }

                        }
                    }

                    dataList.sort()
                    ownWifiBean?.apply {
                        dataList.add(0, this)
                    }
                    wifisAdapter.setList(dataList)
                }.start()

            } else {

                //todo 提示打开wifi
                ToastUtil.toast(this@WifiFragment.context, "请打开wifi开关")
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

    override fun isRegisterEventBus(): Boolean {
        return true
    }
}