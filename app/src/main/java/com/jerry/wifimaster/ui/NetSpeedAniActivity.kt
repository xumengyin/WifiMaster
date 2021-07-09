package com.jerry.wifimaster.ui

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.blankj.utilcode.util.NetworkUtils
import com.jerry.baselib.http.Download
import com.jerry.baselib.http.DownloadCallback
import com.jerry.baselib.http.EntityRequest
import com.jerry.baselib.utils.LogUtils
import com.jerry.baselib.utils.ToastUtil
import com.jerry.wifimaster.Constants
import com.jerry.wifimaster.R
import com.jerry.wifimaster.bean.FormUpload
import com.jerry.wifimaster.bean.NetSpeedResult
import com.jerry.wifimaster.net.TestFileBinary
import com.jerry.wifimaster.net.UploadSpeedBinary
import com.jerry.wifimaster.utils.DeviceScanNetworkUtil
import com.jerry.wifimaster.utils.NetRequestUtil
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.yanzhenjie.nohttp.Headers
import com.yanzhenjie.nohttp.OnUploadListener
import com.yanzhenjie.nohttp.RequestMethod
import com.yanzhenjie.nohttp.download.DownloadRequest
import kotlinx.android.synthetic.main.activity_netspeed_ani.*
import java.io.File

class NetSpeedAniActivity : NetPseedBase() {

    companion object {
        const val STEP_0 = 0 //初始状态
        const val STEP_1 = 1 //测延时
        const val STEP_2 = 2 //测下载
        const val STEP_3 = 3//测上传
        const val STEP_4 = 4 //全部结束

        const val STATUS_TEST="1"
        const val STATUS_IDLE="0"
    }

    var curStep = STEP_0
    var requeNet: DownloadRequest? = null
    var vUploadBinary: UploadSpeedBinary?=null
    var curSpeed = 0L
    //ping
    var avgPing = 0
    var maxDownloadSpeed = 0L
    var maxUploadSpeed=0L
    val gapTime = 2 * 1000L
    var endCound = false
    val mainHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {

            when (msg.what) {
                0 -> {
                    if (endCound)
                        return
                    val speedKB = curSpeed / 1024
                    vSpeedView.setCreditValue(speedKB.toInt())
                    sendEmptyMessageDelayed(0, gapTime)
                }
                1->{
                    //去下个页面
                    val intent= Intent(this@NetSpeedAniActivity,NetSpeedActivity::class.java)
                    val result=NetSpeedResult(avgPing,maxDownloadSpeed,maxUploadSpeed)
                    intent.putExtra(Constants.INTENT_KEY,result)
                    startActivity(intent)
                    finish()
                }
            }

        }
    }

    fun startCountSpeed() {
        endCound = false
        maxDownloadSpeed = 0L
        mainHandler.sendEmptyMessageDelayed(0, gapTime)
    }

    private fun endCountSpeed() {
        endCound = true
        mainHandler.removeCallbacksAndMessages(null)

    }

    override fun loadData(savedInstanceState: Bundle?) {

    }


    private fun testUpload()
    {
        setStep(STEP_3)
        vUploadBinary?.cancel()

        //vUploadBinary= UploadSpeedBinary(resources.openRawResource(R.raw.manufacture))
        vUploadBinary= UploadSpeedBinary()
        vUploadBinary?.apply {
            this.setUploadSpeedCallBack {
                //更新回到主线程
                runOnUiThread {
                    if(it> maxUploadSpeed)
                        maxUploadSpeed=it
                    //curUploadSpeed=it
                    vSpeedView.setCreditValue((it/1024).toInt())
                }
            }
            this.setUploadListener(0,object: OnUploadListener{
                override fun onFinish(what: Int) {
                    vSpeedView.setCreditValue(0)
                    setMaxSpeedData(maxUploadSpeed,value3)
                    setStep(STEP_4)
                    mainHandler.sendEmptyMessageDelayed(1,1000)
                }

                override fun onCancel(what: Int) {
                    cancelAll()
                    setStep(STEP_0)
                }

                override fun onProgress(what: Int, progress: Int) {
                    //不用他的

                }

                override fun onError(what: Int, exception: Exception?) {
                    cancelAll()
                    vSpeedView.setCreditValue(0)
                    ToastUtil.toast(
                        this@NetSpeedAniActivity,
                        getString(R.string.http_exception_network)
                    )
                    vSpeedView.setCreditValue(0)
                    setMaxSpeedData(maxUploadSpeed,value3)
                    setStep(STEP_4)
                }

                override fun onStart(what: Int) {

                }

            })
        }
        //vUploadBinary
        val uploadRequest= EntityRequest(Constants.testUploadUrl, RequestMethod.POST, FormUpload::class.java)
        val params = mutableMapOf<String, Any>()

//        params.put("name","file")
//        params.put("cmd","uploadfile")
//        params.put("dir","vmaster")
        uploadRequest
            .add(params)
            //.add("age", 18)
            .add("file", vUploadBinary)
       // uploadRequest.setcon("content-type","multipart/form-data")
        request(uploadRequest,false) {
            if (it.isSucceed) {

            }else
            {

            }
        }

    }
    //测试延时
    private fun testPing() {
        setStep(STEP_1)
        Thread(Runnable {
            val res = DeviceScanNetworkUtil.getPingRTT(Constants.testPingUrl, "3")
            runOnUiThread {
                if (curStep == STEP_0)
                    return@runOnUiThread
                val resInt = res.toFloat().toInt()
                avgPing=resInt
                apendSpanText(value1, resInt.toString(), defaultUnit1)
                testDownload()
            }
        }).start()

    }

    override fun onDestroy() {
        super.onDestroy()
        endCountSpeed()
        cancelAll()
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

            setStartBtn(vStartSpeed.tag as String)

        }
    }
    private fun setStartBtn(tag:String)
    {
        val text: String
        if ( tag == STATUS_IDLE) {
            vStartSpeed.tag = STATUS_TEST
            testPing()
           // testUpload()
            text = "取消测速"
        } else {
            vStartSpeed.tag = STATUS_IDLE
            //取消测速
            setStep(STEP_0)
            cancelAll()
            text = "开始测速"
        }
        vStartSpeed.text = text
    }


    private fun cancelAll() {
        //取消下载
        requeNet?.apply {
            cancel()
        }
        //取消上传
        vUploadBinary?.cancel()

    }

    private fun aniLoading(img: ImageView, show: Boolean = true) {
        val ani = img.background as AnimationDrawable
        if (show)
            ani.start()
        else
            ani.stop()
    }

    private fun setStep(step: Int) {
        curStep = step
        when (step) {
            STEP_0 -> {
                vWifiTip.visibility = View.INVISIBLE
                value1.visibility = View.VISIBLE
                value2.visibility = View.VISIBLE
                value3.visibility = View.VISIBLE
                apendSpanText(value1, defaultValue1, defaultUnit1)
                apendSpanText(value2, defaultValue1, defaultUnit2)
                apendSpanText(value3, defaultValue1, defaultUnit2)

                vLoadAni0.visibility = View.GONE
                vLoadAni1.visibility = View.GONE
                vLoadAni2.visibility = View.GONE
                aniLoading(vLoadAni1, false)
                aniLoading(vLoadAni2, false)
                aniLoading(vLoadAni0, false)
            }
            STEP_1 -> {
                vWifiTip.visibility = View.VISIBLE
                value1.visibility = View.GONE
                value2.visibility = View.VISIBLE
                value3.visibility = View.VISIBLE
                apendSpanText(value2, defaultValue1, defaultUnit2)
                apendSpanText(value3, defaultValue1, defaultUnit2)
                vLoadAni0.visibility = View.VISIBLE
                vLoadAni1.visibility = View.GONE
                vLoadAni2.visibility = View.GONE
                aniLoading(vLoadAni0, true)
                aniLoading(vLoadAni1, false)
                aniLoading(vLoadAni2, false)

            }
            STEP_2 -> {
                value1.visibility = View.VISIBLE
                value2.visibility = View.GONE
                value3.visibility = View.VISIBLE

                apendSpanText(value2, defaultValue1, defaultUnit2)
                apendSpanText(value3, defaultValue1, defaultUnit2)
                vLoadAni1.visibility = View.VISIBLE
                vLoadAni2.visibility = View.GONE
                vLoadAni0.visibility = View.GONE
                aniLoading(vLoadAni1)
                aniLoading(vLoadAni2, false)
                aniLoading(vLoadAni0, false)
            }
            STEP_3 -> {
                value1.visibility = View.VISIBLE
                value2.visibility = View.VISIBLE
                value3.visibility = View.GONE
                //  value1.text = defaultValue
                //  value2.text = defaultValue
                apendSpanText(value3, defaultValue1, defaultUnit2)
                vLoadAni1.visibility = View.GONE
                vLoadAni2.visibility = View.VISIBLE
                vLoadAni0.visibility = View.GONE
                aniLoading(vLoadAni2)
                aniLoading(vLoadAni1, false)
                aniLoading(vLoadAni0, false)
            }
            STEP_4 -> {
                value1.visibility = View.VISIBLE
                value2.visibility = View.VISIBLE
                value3.visibility = View.VISIBLE
                //  value1.text = defaultValue
                //  value2.text = defaultValue
                vLoadAni1.visibility = View.GONE
                vLoadAni2.visibility = View.GONE
                vLoadAni0.visibility = View.GONE
                aniLoading(vLoadAni2, false)
                aniLoading(vLoadAni1, false)
                aniLoading(vLoadAni0, false)
            }

        }


    }



    private fun testDownload() {
        requeNet?.apply {
            cancel()
        }
        setStep(STEP_2)
        requeNet = NetRequestUtil.downloadRequest()
        requeNet?.cancelSign = this
        Download.getInstance().download(0, requeNet, object : DownloadCallback(this) {
            override fun onFinish(what: Int, filePath: String?) {
                endCountSpeed()
                //结束
                vSpeedView.setCreditValue(0)
                setStep(STEP_3)
                setMaxSpeedData(maxDownloadSpeed,value2)
                //下完删除刚下载的文件
                filePath?.apply {
                    val downloadFile = File(filePath)
                    downloadFile.delete()
                }
                //测试上传
                testUpload()
            }

            override fun onCancel(what: Int) {
                //
                endCountSpeed()
                vSpeedView.setCreditValue(0)

            }

            override fun onException(message: String?) {
                endCountSpeed()
                vSpeedView.setCreditValue(0)
                ToastUtil.toast(
                    this@NetSpeedAniActivity,
                    getString(R.string.http_exception_network)
                )
                setMaxSpeedData(maxDownloadSpeed,value2)
                //测试上传
                testUpload()
                //setStartBtn(STATUS_TEST)
            }

            override fun onProgress(what: Int, progress: Int, fileCount: Long, speed: Long) {

                LogUtils.logd("download fileCount:" + fileCount + "--progress:" + progress)
                curSpeed = speed
                if (speed > maxDownloadSpeed)
                    maxDownloadSpeed = speed
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