package com.jerry.wifimaster.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.ViewGroup
import com.blankj.utilcode.util.ActivityUtils
import com.jerry.baselib.base.BaseActivity
import com.jerry.wifimaster.Constants
import com.jerry.wifimaster.R
import com.jerry.wifimaster.devicescan.DeviceScanManager
import com.jerry.wifimaster.devicescan.IP_MAC
import com.jerry.wifimaster.utils.DeviceScanUtil
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import kotlinx.android.synthetic.main.activity_detective_ani.*
import kotlinx.android.synthetic.main.activity_detective_ani.uinv
import kotlinx.android.synthetic.main.activity_netspeed_ani.*
import java.io.Serializable

class DetectiveAniActivity : BaseActivity() {


    companion object{
        const val duration=12*1000L
    }
    val mainHandler =object :Handler(Looper.getMainLooper())
    {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val intent= Intent(this@DetectiveAniActivity,DetectActivity::class.java)
            intent.putExtra(Constants.INTENT_KEY,deviceList as Serializable)
            //intent.putExtra(bundle)
            startActivity(intent)
            finish()
        }
    }
    val deviceManager by lazy {
        DeviceScanUtil(this@DetectiveAniActivity)
    }
    val deviceList= mutableListOf<IP_MAC>()
    override fun loadData(savedInstanceState: Bundle?) {
        vDetectView.post {
            startScan()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_detective_ani
    }

    override fun initViews() {

        QMUIStatusBarHelper.translucent(this)
        val statusHeight = QMUIDisplayHelper.getStatusBarHeight(this)
        val params = uinv.layoutParams as ViewGroup.MarginLayoutParams
        params.topMargin = statusHeight
        uinv.layoutParams = params
        uinv.setNavigationTitle("安全检测")

        vCancle.setOnClickListener {
            val title: String
            val tag = vCancle.tag
            if (tag == "0") {
                vCancle.tag = "1"
                title = "取消检测"
                startScan()
            } else {
                vCancle.tag = "0"
                title = "开始检测"
                stopScan()
            }
            vCancle.text = title

        }
    }

    private fun startScan()
    {
        mainHandler.sendEmptyMessageDelayed(0,duration)
        vDetectView.startAnimate()
        deviceManager.start {
            if (!deviceList.contains(it)) {
                deviceList.add(it)
            }
            runOnUiThread {
                vNumTip.text="扫描中,同WIFI下有${deviceList.size}台设备"
            }
        }
    }
    private fun stopScan()
    {
        mainHandler.removeCallbacksAndMessages(null)
        vDetectView.stop()
        deviceManager.destroy()
    }
    override fun onDestroy() {
        super.onDestroy()
        stopScan()
    }
}