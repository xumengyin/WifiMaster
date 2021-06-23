package com.jerry.wifimaster.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jerry.wifimaster.R
import com.jerry.wifimaster.bean.Menus

class MenuAdapter(data: MutableList<Menus>?) :
    BaseQuickAdapter<Menus, BaseViewHolder>(R.layout.menu_item, data) {


    var showPanel = true
    override fun convert(holder: BaseViewHolder, item: Menus) {
        item.apply {
            holder.setImageResource(R.id.imgId, drawableId)
            holder.setText(R.id.desc, desc)
            holder.setText(R.id.menuValue, descValue)
            if (type == TYPE_NET_SPEED_N) {
                holder.setVisible(R.id.check, true)
                holder.setGone(R.id.menuValue, true)
            } else {
                holder.setGone(R.id.check, true)
                holder.setVisible(R.id.menuValue, true)
            }

        }

    }


    companion object {
        const val TYPE_CURRENT_SPEED = 1
        const val TYPE_NET_SPEED_N = 2
        const val TYPE_NET_SPEED_Y = 8
        const val TYPE_TEST = 3
        const val TYPE_SIGNAL_PLUS = 4
        const val TYPE_SIGNAL_STREHGTH = 5
        const val TYPE_CONNECT_SPEED = 6
        const val TYPE_IP = 7
    }
}