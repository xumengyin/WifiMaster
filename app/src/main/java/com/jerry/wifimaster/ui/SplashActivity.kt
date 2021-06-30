package com.jerry.wifimaster.ui

import android.Manifest
import android.content.Intent
import android.os.Bundle
import com.anythink.core.api.ATAdConst
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.AdError
import com.anythink.splashad.api.ATSplashAd
import com.anythink.splashad.api.IATSplashEyeAd
import com.jerry.baselib.base.BaseSplashAdActivity
import com.jerry.baselib.utils.LogUtils
import com.jerry.wifimaster.Constants
import com.jerry.wifimaster.MainActivity
import com.jerry.wifimaster.R
import com.jerry.wifimaster.perference.SettingPreference
import com.jerry.wifimaster.ui.dialog.CustomAgreementDialog
import com.jerry.wifimaster.utils.OaidHelper
import com.tbruyelle.rxpermissions3.RxPermissions
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseSplashAdActivity() {


    var argeementDialog: CustomAgreementDialog? = null
    val rxPermissions = RxPermissions(this@SplashActivity)
    val ads by lazy {
        ATSplashAd(this@SplashActivity, Constants.ADS_SPLASH, null, this@SplashActivity, 5000)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun loadData(savedInstanceState: Bundle?) {
        super.loadData(savedInstanceState)

        val oaidHelper = OaidHelper(object : OaidHelper.AppIdsUpdater {
            override fun OnIdsAvalidError() {
                LogUtils.logd("oaidHelper----OnIdsAvalidError")
            }

            override fun OnIdsAvalid(oaid: String) {
                LogUtils.logd("OnIdsAvalid----${oaid}")
            }

        })
        oaidHelper.getDeviceIds(this)
        //ATSDK.integrationChecking(applicationContext);
    }

    fun dealPrivacy() {
        argeementDialog?.apply {
            if (isShowing) {
                dismiss()
            }
            argeementDialog = null
        }
        val settingPreference = SettingPreference.getInstance()
        if (settingPreference.isAgreement) {
            argeementDialog=CustomAgreementDialog(this)
            argeementDialog?.apply {
                buttonOK.setOnClickListener {
                    dismiss()
                    init()
                }
                buttonCancel.setOnClickListener {
                    dismiss()
                    finish()
                }
                show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ads.onDestory()
    }

    override fun onAdDismiss(atAdInfo: ATAdInfo?, iatSplashEyeAd: IATSplashEyeAd?) {
        super.onAdDismiss(atAdInfo, iatSplashEyeAd)
        LogUtils.d("xuxu", "onAdDismiss")
        gotoMain()
    }

    override fun onNoAdError(adError: AdError?) {
        LogUtils.d("xuxu", "onNoAdError:${adError.toString()}")
        gotoMain()
    }

    override fun onAdLoaded() {
        //        LogUtils.d("")

        ads.show(this, content)
    }

    var hasJump = false
    fun gotoMain() {
        if (!hasJump) {
            hasJump = true
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    fun loadAds() {
        content.post {

            val h = content.layoutParams.height
            val w = content.layoutParams.width
            val maps = mapOf(
                ATAdConst.KEY.AD_WIDTH to w,
                ATAdConst.KEY.AD_HEIGHT to h,
                ATAdConst.KEY.AD_CLICK_CONFIRM_STATUS to true
            )
            ads.apply {

                setLocalExtra(maps)
                if (isAdReady) {
                    show(this@SplashActivity, content)
                } else {
                    loadAd()
                }
            }


        }
    }

    override fun initViews() {
        intent.getBooleanExtra(Constants.INTENT_KEY, false).apply {

            if (!this) {
                //首页进入
               dealPrivacy()
            } else {
                //直接展示广告
                loadAds()
            }


        }
    }

    fun init() {
        rxPermissions.request(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).subscribe { grant ->
            if (grant) {
                //gotoMain()
                loadAds()
            } else {
                // todo
                //gotoMain()
                loadAds()
            }

        }
    }

    companion object {

    }

}