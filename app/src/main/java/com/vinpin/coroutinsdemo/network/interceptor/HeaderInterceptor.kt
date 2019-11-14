package com.vinpin.coroutinsdemo.network.interceptor

import java.io.IOException

import okhttp3.Interceptor
import okhttp3.Response

/**
 * author : vinpin
 * e-mail : hearzwp@163.com
 * time   : 2019/2/22 17:21
 * desc   : 请求头里边添加Header拦截器
 */
class HeaderInterceptor(private val headers: Map<String, String>) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHeaders = original.headers()
        val requestBuilder = original.newBuilder()
        // 添加统一通用header，不存在则添加，存在则不添加，为null则移除。
        if (headers.isNotEmpty()) {
            val keys = headers.keys
            for (key in keys) {
                val originalValue = originalHeaders.get(key)
                if (originalValue == null) {
                    val value = headers[key]
                    if (value != null) {
                        requestBuilder.addHeader(key, value)
                    } else {
                        requestBuilder.removeHeader(key)
                    }
                }
            }
        }
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
