package com.vinpin.coroutinsdemo.retrofit

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * author : vinpin
 * e-mail : hearzwp@163.com
 * time   : 2019/11/13 13:14
 * desc   : 添加公共请求头拦截器
 */
class HeaderInterceptor(private val headers: Map<String, String>) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHeaders = original.headers()
        val requestBuilder = original.newBuilder()
        // 添加统一通用header，不存在则添加，存在则不添加。
        for (key in headers.keys) {
            if (originalHeaders.get(key) == null) {
                headers[key]?.let {
                    requestBuilder.addHeader(key, it)
                }
            }
        }
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}