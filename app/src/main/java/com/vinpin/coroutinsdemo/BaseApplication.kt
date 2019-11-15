package com.vinpin.coroutinsdemo

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 * author : vinpin
 * e-mail : hearzwp@163.com
 * time   : 2019/11/15 14:29
 * desc   :
 */
class BaseApplication : Application() {

    companion object {

        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context

        fun getContext() = mContext
    }

    override fun onCreate() {
        mContext = this
        super.onCreate()
    }
}
