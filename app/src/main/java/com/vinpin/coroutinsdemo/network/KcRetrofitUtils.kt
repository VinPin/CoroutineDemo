package com.vinpin.coroutinsdemo.network

import com.vinpin.coroutinsdemo.BuildConfig
import okhttp3.OkHttpClient
import java.util.HashMap

/**
 * author : vinpin
 * e-mail : hearzwp@163.com
 * time   : 2019/11/13 13:55
 * desc   : 获取KcRetrofit实例的工具类
 */
object KcRetrofitUtils {

    private var defaultKcRetrofit: KcRetrofit? = null

    /**
     * 获取默认的KcRetrofit实例，只会创建一次实例。
     */
    fun getDefault(): KcRetrofit {
        if (defaultKcRetrofit == null) {
            defaultKcRetrofit = newInstance()
        }
        return defaultKcRetrofit!!
    }

    /**
     * 创建新的KcRetrofit实例
     */
    fun newInstance(): KcRetrofit {
        return KcRetrofit()
    }

    /**
     * 使用默认的KcRetrofit实例创建HTTP API接口
     */
    fun <T> create(cls: Class<T>): T {
        return getDefault().create(cls)
    }

    /**
     * 创建默认的OkHttpClient实例
     */
    fun defaultOkHttpClient(): OkHttpClient {
        return defaultKcOkHttpClient().debugLog(BuildConfig.DEBUG).build()
    }

    /**
     * 创建默认的KcOkHttpClient实例
     */
    fun defaultKcOkHttpClient(): KcOkHttpClient {
        val map = HashMap<String, String>(2)
        map["Accept"] = "application/json"
        map["User-Agent"] = "Android"
        return KcOkHttpClient().addHeaders(map).sslSocketFactory()
    }
}