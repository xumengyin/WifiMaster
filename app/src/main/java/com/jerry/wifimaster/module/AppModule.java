package com.jerry.wifimaster.module;


import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ActivityContext;
import dagger.hilt.android.scopes.ActivityScoped;
import dagger.hilt.components.SingletonComponent;

//该类该组件 装配在全局   组件生命周期  //组件作用域
/**
 *  * *ApplicationComponent	       Application#onCreate()	Application#onDestroy()
 *  *  * ActivityRetainedComponent	    Activity#onCreate()	Activity#onDestroy()
 *  *  * ActivityComponent	        Activity#onCreate()	Activity#onDestroy()
 *  *  * FragmentComponent	        Fragment#onAttach()	Fragment#onDestroy()
 *  *  * ViewComponent	        View#super()	视图销毁时
 *  *  * ViewWithFragmentComponent	V    iew#super()	视图销毁时
 *  *  * ServiceComponent	         Service#onCreate()	Service#onDestroy()
 *  *
 *  * 作用域:默认 是提供新对象  作用域内会提供单例对象
 *  *
 *  Android 类	        生成的组件	         作用域
 *  Application	ApplicationComponent	@Singleton
 *  View Model	   ActivityRetainedComponent	@ActivityRetainedScope
 *  Activity	  ActivityComponent	@ActivityScoped
 *  Fragment	   FragmentComponent	@FragmentScoped
 *  View	        ViewComponent	@ViewScoped
 *  带有@WithFragmentBindings 注释的 View	ViewWithFragmentComponent	@ViewScoped
 *  Service	      ServiceComponent	@ServiceScoped
 */
@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

//    @ActivityScoped
//    @Provides
//    public FragmentManager getSupportMananger(@ActivityContext AppCompatActivity activity)
//    {
//        return  activity.getSupportFragmentManager();
//    }
//    @Singleton
//    @ActivityScoped
//    @Provides
//    public List<Fragment> getFragmentList()
//    {
//        return new ArrayList<>();
//    }

}
