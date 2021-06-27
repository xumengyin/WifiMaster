package com.jerry.wifimaster.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jerry.wifimaster.R
import com.jerry.wifimaster.bean.WifiBean
import com.jerry.wifimaster.devicescan.IP_MAC

class ScanDevicesAdapter(data: MutableList<IP_MAC>?) :
    BaseQuickAdapter<IP_MAC, BaseViewHolder>(R.layout.device_item_layout, data) {


    override fun convert(holder: BaseViewHolder, item: IP_MAC) {
        item.apply {
            holder.setText(R.id.vDeviceIp, item.mIp)
            holder.setText(
                R.id.vDeviceIp,
                if (item.mDeviceName == "未知设备") item.mManufacture else item.mDeviceName
            )


        }

    }
}