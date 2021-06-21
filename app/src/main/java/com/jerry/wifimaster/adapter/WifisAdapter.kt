package com.jerry.wifimaster.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jerry.wifimaster.R
import com.jerry.wifimaster.bean.Menus
import com.jerry.wifimaster.bean.WifiBean

class WifisAdapter(data: MutableList<WifiBean>?) :
    BaseQuickAdapter<WifiBean, BaseViewHolder>(R.layout.wifi_item_layout, data) {


    override fun convert(holder: BaseViewHolder, item: WifiBean) {
        item.apply {
            holder.setText(R.id.vWifiName,result.SSID)
        }

    }
}