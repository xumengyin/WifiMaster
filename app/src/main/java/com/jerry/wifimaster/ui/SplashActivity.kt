package com.jerry.wifimaster.ui

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jerry.wifimaster.MainActivity
import com.jerry.wifimaster.R
import com.tbruyelle.rxpermissions3.RxPermissions

class SplashActivity : AppCompatActivity() {


    val rxPermissions = RxPermissions(this@SplashActivity)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        init()
    }


    fun init() {
        rxPermissions.request(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).subscribe {grant->
            if(grant)
            {
                startActivity(Intent(this@SplashActivity,MainActivity::class.java))
                finish()
            }else
            {
                // todo
            }

        }
    }

    companion object {

    }

}