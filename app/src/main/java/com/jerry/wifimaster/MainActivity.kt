package com.jerry.wifimaster

import android.os.Bundle
import android.view.Gravity
import androidx.core.content.ContextCompat
import com.jerry.baselib.base.BaseActivity
import com.jerry.wifimaster.adapter.FragmentTabAdapter
import com.jerry.wifimaster.ui.SettingFragment
import com.jerry.wifimaster.ui.WifiFragment
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.qmuiteam.qmui.widget.tab.QMUIBasicTabSegment
import com.qmuiteam.qmui.widget.tab.QMUITabSegment
import kotlinx.android.synthetic.main.activity_main.*

open class MainActivity : BaseActivity() {
    val wifiFragment by lazy {
        WifiFragment()
    }
    val settingFragment by lazy {
        SettingFragment()
    }
    lateinit var tabAdapter: FragmentTabAdapter
    override fun loadData(savedInstanceState: Bundle?) {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }


    override fun initViews() {
        QMUIStatusBarHelper.translucent(this)
//        supportFragmentManager.beginTransaction().add(R.id.frame, wifiFragment)
//            .commitAllowingStateLoss()
        initTabs()
    }

    private fun initTabs() {
        vTab.reset()
        val colorMain1 = ContextCompat.getColor(this, R.color.tab_color_main1)
        val colorMain2 = ContextCompat.getColor(this, R.color.tab_color_main2)
        val textSize=QMUIDisplayHelper.dp2px(this,12)
        val iconSize=QMUIDisplayHelper.dp2px(this,24)
        val builder = vTab.tabBuilder()
        builder.apply {
            setGravity(Gravity.CENTER).setColor(colorMain2, colorMain1).skinChangeWithTintColor(false)
        }
        val tab1 = builder.setNormalDrawable(ContextCompat.getDrawable(this, R.drawable.tab_wifi1))
            .setSelectedDrawable(ContextCompat.getDrawable(this, R.drawable.tab_wifi2))
            .setText("WIFI")
            .setTextSize(textSize,textSize)
            .setNormalIconSizeInfo(iconSize,iconSize)
            .build(this)
        val tab2 =
            builder.setNormalDrawable(ContextCompat.getDrawable(this, R.drawable.tab_setting1))
                .setSelectedDrawable(ContextCompat.getDrawable(this, R.drawable.tab_setting2))
                .setText("设置")
                .setTextSize(textSize,textSize)
                .setNormalIconSizeInfo(iconSize,iconSize)
                .build(this)

        vTab.addTab(tab1).addTab(tab2)
        vTab.notifyDataChanged()
        vTab.mode = QMUITabSegment.MODE_FIXED
        vTab.addOnTabSelectedListener(object : QMUIBasicTabSegment.OnTabSelectedListener {
                override fun onDoubleTap(index: Int) {

                }

                override fun onTabReselected(index: Int) {

                }

                override fun onTabUnselected(index: Int) {

                }

                override fun onTabSelected(index: Int) {
                    if(index==0)
                        QMUIStatusBarHelper.setStatusBarDarkMode(this@MainActivity)
                    else
                        QMUIStatusBarHelper.setStatusBarLightMode(this@MainActivity)

                }

            })


        val list = listOf(wifiFragment, settingFragment)
        tabAdapter = FragmentTabAdapter(supportFragmentManager, list)
        vPager.adapter = tabAdapter
        vTab.setupWithViewPager(vPager,false)

    }
}