package com.jerry.wifimaster.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jerry.baselib.base.BaseActivity
import com.jerry.wifimaster.Constants
import com.jerry.wifimaster.R
import com.jerry.wifimaster.adapter.ScanDevicesAdapter
import com.jerry.wifimaster.devicescan.IP_MAC
import com.jerry.wifimaster.perference.SettingPreference
import com.jerry.wifimaster.utils.CommonUtils
import com.jerry.wifimaster.utils.DeviceScanNetworkUtil
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import kotlinx.android.synthetic.main.activity_detect.*
import kotlinx.android.synthetic.main.activity_detect.uinv
import kotlinx.android.synthetic.main.activity_detective_ani.*
import kotlinx.android.synthetic.main.activity_signalani_plus.*
import kotlin.random.Random

class SignalPlusAniActivity : BaseActivity() {


    companion object{
        const val duration=10*1000L
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_signalani_plus
    }



    override fun loadData(savedInstanceState: Bundle?) {

    }

    var animate: ValueAnimator? = null
    fun start()
    {
        if (animate!=null) {
            animate?.cancel()
            animate=null
        }
        //5-15
        val upgrade= Random(2021).nextInt(10)+5
        animate=ValueAnimator.ofInt(0,upgrade)
        animate?.addListener(object :AnimatorListenerAdapter()
        {
            override fun onAnimationStart(animation: Animator?) {
                vProgress.startAnimate(duration)
                vSignalView.startAnimate()
            }

            override fun onAnimationEnd(animation: Animator?) {
                vSignalView.stopAni()
                SettingPreference.save(SettingPreference.KET_SIGNAL_TIME,System.currentTimeMillis(),false)
                SettingPreference.save(SettingPreference.KET_SIGNAL_STREHGTH,upgrade)

                val intent=Intent(this@SignalPlusAniActivity,SignalPlusActivity::class.java)
                intent.putExtra(Constants.INTENT_KEY,upgrade)
                startActivity(intent)
            }
        })
        animate?.addUpdateListener {
            vSignalValue.text= "${it.animatedValue}%"
        }
        animate?.duration= duration
        animate?.start()
      //  vProgress.startAnimate(duration)

    }

    override fun onDestroy() {
        super.onDestroy()

    }
    override fun initViews() {
        QMUIStatusBarHelper.translucent(this)
        val statusHeight = QMUIDisplayHelper.getStatusBarHeight(this)
        val params = uinv.layoutParams as ViewGroup.MarginLayoutParams
        params.topMargin = statusHeight
        uinv.layoutParams = params
        uinv.setNavigationTitle("信号强度")


    }
}