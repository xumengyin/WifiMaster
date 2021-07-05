package com.jerry.wifimaster.ui

import android.view.View
import android.widget.TextView
import com.jerry.baselib.utils.SpanUtils

abstract class NetPseedBase : BaseNativeAdActivity() {

    val defaultValue1 = "0"
    val defaultUnit1 = "ms"
    val defaultUnit2 = "KB/s"


    public fun apendSpanText(textview: TextView, value: String, unit: String) {
        SpanUtils.with(textview, this).append(value)
            .setFontSize(24, true)
            .append(unit)
            .setFontSize(12, true)
            .create()
    }

    override fun getAdsContentView(): View {

        return View(this)
    }

    override fun onCloseAds() {

    }

}