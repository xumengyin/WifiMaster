package com.jerry.wifimaster.module;

import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;

/**
 * 组价装在acitivty上 随着activity周期销毁  组件生命周期  //组件作用域
 *
 * *ApplicationComponent	       Application#onCreate()	Application#onDestroy()
 *  * ActivityRetainedComponent	    Activity#onCreate()	Activity#onDestroy()
 *  * ActivityComponent	        Activity#onCreate()	Activity#onDestroy()
 *  * FragmentComponent	        Fragment#onAttach()	Fragment#onDestroy()
 *  * ViewComponent	        View#super()	视图销毁时
 *  * ViewWithFragmentComponent	V    iew#super()	视图销毁时
 *  * ServiceComponent	         Service#onCreate()	Service#onDestroy()
 *
 * 作用域:默认 是提供新对象  作用域内会提供单例对象
 *
 Android 类	        生成的组件	         作用域
 Application	ApplicationComponent	@Singleton
 View Model	   ActivityRetainedComponent	@ActivityRetainedScope
 Activity	  ActivityComponent	@ActivityScoped
 Fragment	   FragmentComponent	@FragmentScoped
 View	        ViewComponent	@ViewScoped
 带有@WithFragmentBindings 注释的 View	ViewWithFragmentComponent	@ViewScoped
 Service	      ServiceComponent	@ServiceScoped
 *
 *
 *
 *
 */
@Module
@InstallIn(ActivityComponent.class)
public class ActivityModule {
}
