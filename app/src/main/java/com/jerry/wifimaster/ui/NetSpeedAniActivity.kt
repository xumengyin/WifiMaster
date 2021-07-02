package com.jerry.wifimaster.ui

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.blankj.utilcode.util.NetworkUtils
import com.jerry.baselib.base.BaseActivity
import com.jerry.baselib.http.Download
import com.jerry.baselib.http.DownloadCallback
import com.jerry.baselib.utils.LogUtils
import com.jerry.baselib.utils.ToastUtil
import com.jerry.wifimaster.Constants
import com.jerry.wifimaster.R
import com.jerry.wifimaster.utils.DeviceScanNetworkUtil
import com.jerry.wifimaster.utils.NetRequestUtil
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.yanzhenjie.nohttp.Headers
import com.yanzhenjie.nohttp.download.DownloadRequest
import kotlinx.android.synthetic.main.activity_netspeed_ani.*
import java.io.File

class NetSpeedAniActivity : BaseActivity() {

    companion object {
        const val STEP_0 = 0 //初始状态
        const val STEP_1 = 1 //测延时
        const val STEP_2 = 2 //测下载
        const val STEP_3 = 3//测上传
        const val STEP_4 = 4
    }

    var curStep = STEP_0
    var requeNet: DownloadRequest? = null
    val defaultValue by lazy {
        getString(R.string.default_value)
    }
    var curSpeed = 0L
    val gapTime = 2 * 1000L
    var endCound = false
    val mainHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            if (endCound)
                return
            val speedKB = curSpeed / 1024
            vSpeedView.setCreditValue(speedKB.toInt())
            sendEmptyMessageDelayed(0, gapTime)
        }
    }

    fun startCountSpeed() {
        endCound = false
        mainHandler.sendEmptyMessageDelayed(0, gapTime)
    }

    private fun endCountSpeed() {
        endCound = true
        mainHandler.removeCallbacksAndMessages(null)
    }

    override fun loadData(savedInstanceState: Bundle?) {

    }
    //测试延时
    private fun testPing()
    {
        setStep(STEP_1)
        Thread(Runnable {
            DeviceScanNetworkUtil.ping(Constants.testPingUrl,"3")
        }).start()

    }
    override fun onDestroy() {
        super.onDestroy()
        endCountSpeed()
        requeNet?.apply {
            cancel()
        }
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_netspeed_ani
    }

    override fun initViews() {
        QMUIStatusBarHelper.translucent(this)
        val statusHeight = QMUIDisplayHelper.getStatusBarHeight(this)
        val params = uinv.layoutParams as ViewGroup.MarginLayoutParams
        params.topMargin = statusHeight
        uinv.layoutParams = params
        uinv.setNavigationTitle("网络测速")
        if (NetworkUtils.isWifiConnected()) {
            vWifiName2.text = NetworkUtils.getSSID()
            setStep(STEP_0)
        } else {
            //todo
            ToastUtil.toast(this, "无WIFI连接", R.drawable.toast_wrong)
        }


        vStartSpeed.setOnClickListener {

            //开始下载测试
            //startTest()
            testPing()
        }
    }

    fun aniLoading(img: ImageView, show: Boolean = true) {
        val ani = img.background as AnimationDrawable
        if (show)
            ani.start()
        else
            ani.stop()
    }

    fun setStep(step: Int) {
        curStep = step
        when (step) {
            STEP_0 -> {
                value2.visibility = View.VISIBLE
                value3.visibility = View.VISIBLE
                value1.text = defaultValue
                value2.text = defaultValue
                value3.text = defaultValue
                vLoadAni1.visibility = View.GONE
                vLoadAni2.visibility = View.GONE
                aniLoading(vLoadAni1, false)
                aniLoading(vLoadAni2, false)
            }
            STEP_2 -> {
                value2.visibility = View.GONE
                value3.visibility = View.VISIBLE
//                value1.text = defaultValue
                value2.text = defaultValue
                value3.text = defaultValue
                vLoadAni1.visibility = View.VISIBLE
                vLoadAni2.visibility = View.GONE
                aniLoading(vLoadAni1)
                aniLoading(vLoadAni2, false)
            }
            STEP_3 -> {
                value2.visibility = View.VISIBLE
                value3.visibility = View.GONE
                //  value1.text = defaultValue
                //  value2.text = defaultValue
                value3.text = defaultValue
                vLoadAni1.visibility = View.GONE
                vLoadAni2.visibility = View.VISIBLE
                aniLoading(vLoadAni2)
                aniLoading(vLoadAni1, false)
            }

        }


    }

    fun startTest() {
        requeNet?.apply {
            cancel()
        }
        setStep(STEP_1)
        requeNet = NetRequestUtil.downloadRequest()
        requeNet?.cancelSign = this
        Download.getInstance().download(0, requeNet, object : DownloadCallback(this) {
            override fun onFinish(what: Int, filePath: String?) {
                endCountSpeed()
                //结束
                vSpeedView.setCreditValue(0)
                //下完删除刚下载的文件
                filePath?.apply {
                    val downloadFile = File(filePath)
                    downloadFile.delete()
                }

            }

            override fun onCancel(what: Int) {
                //
                endCountSpeed()
                vSpeedView.setCreditValue(0)

            }

            override fun onException(message: String?) {
                endCountSpeed()
                vSpeedView.setCreditValue(0)
            }

            override fun onProgress(what: Int, progress: Int, fileCount: Long, speed: Long) {

                LogUtils.logd("download fileCount:" + fileCount + "--progress:" + progress)
                curSpeed = speed
            }

            override fun onStart(
                what: Int,
                isResume: Boolean,
                rangeSize: Long,
                responseHeaders: Headers?,
                allCount: Long
            ) {
                //开始下载
                startCountSpeed()
                LogUtils.logd("download allCount:" + allCount)
            }

        })
    }


}