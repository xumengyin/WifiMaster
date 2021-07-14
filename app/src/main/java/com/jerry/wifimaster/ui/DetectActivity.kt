package com.jerry.wifimaster.ui

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.core.content.ContextCompat
import androidx.core.view.LayoutInflaterCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jerry.baselib.utils.LogUtils
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

    var scrollstate = 0
    val scanAdapter by lazy {
        ScanDevicesAdapter(mutableListOf())
    }
    val adsHeadView by lazy {
        LayoutInflater.from(this).inflate(R.layout.ads_head_layout,null,false)
    }
    var listData = mutableListOf<IP_MAC>()
    override fun getLayoutId(): Int {
        return R.layout.activity_detect
    }

    override fun getAdsContentView(): View {
        if (vAdContentView == null) {
            vAdContentView = adsHeadView.findViewById(R.id.vAdContent)
        }
        return vAdContentView
    }

    override fun onCloseAds() {
        //vMainAd.visibility = GONE
        scanAdapter.removeAllHeaderView()
    }

    override fun loadData(savedInstanceState: Bundle?) {
        listData = intent.getSerializableExtra(Constants.INTENT_KEY) as MutableList<IP_MAC>

        vWifiNum.text = "共${listData.size}台设备连接当前WIFI"
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

    fun initHeadView()
    {
        scanAdapter.addHeaderView(adsHeadView)
    }
    var isAni=false
    override fun initViews() {
        super.initViews()
        QMUIStatusBarHelper.translucent(this)
        val statusHeight = QMUIDisplayHelper.getStatusBarHeight(this)
        val params = uinv.layoutParams as ViewGroup.MarginLayoutParams
        params.topMargin = statusHeight
        uinv.layoutParams = params
        uinv.setNavigationTitle("安全检测")

        initHeadView()
        vCheck.setOnClickListener {
            startActivity(Intent(this@DetectActivity, DetectiveAniActivity::class.java))
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

        vDeviceRv.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                LogUtils.d("mmm", "vScrollView scrollY:${dy}")
                scrollstate=dy
                if (scrollstate > 0) {
//                //上滑动，要隐藏
                if (!isAni) {
                    isAni=true
                    vBtnLayout.animate().withEndAction { isAni=false }.translationY(vBtnLayout.measuredHeight+10f).setDuration(400).start()
                }
            } else if(scrollstate<0){
                //下滑 要展示
                if(!isAni)
                {
                    isAni=true
                    vBtnLayout.animate().withEndAction { isAni=false }.translationY(0f).setDuration(400).start()

                }
            }
            }
        })
//        vDeviceRv.setOnScrollChangeListener { v: View?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
//            LogUtils.d("mmm", "vScrollView scrollY:${scrollY}---oldScrollY:${oldScrollY}")
//            scrollstate = scrollY - oldScrollY
//            if (scrollstate > 0) {
//                //上滑动，要隐藏
//                if (!isAni) {
//                  //  vBtnLayout.translationY
//                    isAni=true
//                    vBtnLayout.animate().withEndAction { isAni=false }.translationY(vBtnLayout.measuredHeight+10f).setDuration(400).start()
//                }
//            } else if(scrollstate<0){
//                //下滑 要展示
//                if(!isAni)
//                {
//                    isAni=true
////                    val ani=CommonUtils.loadAni(this@DetectActivity,R.anim.bottom_toshow)
//                    vBtnLayout.animate().withEndAction { isAni=false }.translationY(0f).setDuration(400).start()
//
//                }
//            }
//        }

    }
}