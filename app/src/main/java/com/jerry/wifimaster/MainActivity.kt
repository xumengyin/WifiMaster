package com.jerry.wifimaster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jerry.wifimaster.ui.WifiFragment

class MainActivity : AppCompatActivity() {
    val wifiFragment by lazy {
        WifiFragment()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        supportFragmentManager.beginTransaction().add(R.id.frame,wifiFragment).commitAllowingStateLoss()



    }
}