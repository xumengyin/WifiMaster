package com.jerry.wifimaster.ui

import android.view.View
import android.widget.TextView
import com.jerry.baselib.utils.SpanUtils
import java.text.DecimalFormat

abstract class NetPseedBase : BaseNativeAdActivity() {

    val defaultValue1 = "0"
    val defaultUnit1 = "ms"
    val defaultUnit2 = "KB/s"


    fun apendSpanText(textview: TextView, value: String, unit: String) {
        SpanUtils.with(textview, this).append(value)
            .setFontSize(24, true)
            .append(unit)
            .setFontSize(12, true)
            .create()
    }
    fun setMaxSpeedData(maxSpeed:Long,valueText:TextView) {
        val format = DecimalFormat("#.##")
        var speed = maxSpeed / 1024f
        var unit = ""
        if (speed > 1) {
            speed /= 1024
            if(speed>1)
            {
                unit = "MB/s"
                apendSpanText(valueText,format.format(speed), unit)
            }else
            {
                unit = "KB/s"
                apendSpanText(valueText,format.format( (maxSpeed / 1024f).toInt()), unit)
            }

        }else
        {
            unit = "KB/s"
            speed=1f
            apendSpanText(valueText,format.format(speed), unit)
        }

    }
    override fun getAdsContentView(): View {

        return View(this)
    }

    override fun onCloseAds() {

    }

}