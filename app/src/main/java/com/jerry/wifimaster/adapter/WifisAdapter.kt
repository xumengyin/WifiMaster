package com.jerry.wifimaster.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jerry.wifimaster.R
import com.jerry.wifimaster.bean.WifiBean

class WifisAdapter(data: MutableList<WifiBean>?) :
    BaseQuickAdapter<WifiBean, BaseViewHolder>(R.layout.wifi_item_layout, data) {

    var wifiName: String = ""

    fun setCurWifi(wifiName: String) {
        this.wifiName = wifiName
    }

    override fun convert(holder: BaseViewHolder, item: WifiBean) {
        item.apply {
            holder.setText(R.id.vWifiName, result.SSID)
            if (result.SSID == wifiName) {
                holder.setImageResource(R.id.wifiStatus, R.drawable.top_wifi_connect)
            } else {
                holder.setImageResource(R.id.wifiStatus, R.drawable.top_wifi_password)
            }

            when (levelStrength) {
                WifiBean.LEVEL_LOW -> {
                    holder.setImageResource(R.id.wifiIcon, R.drawable.wifi_b3)
                }
                WifiBean.LEVEL_MIDDLE -> {
                    holder.setImageResource(R.id.wifiIcon, R.drawable.wifi_b2)
                }
                WifiBean.LEVEL_HIGTH -> {
                    holder.setImageResource(R.id.wifiIcon, R.drawable.wifi_b1)
                }
            }
        }

    }
}