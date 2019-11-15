package com.vinpin.coroutinsdemo.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * author : vinpin
 * e-mail : hearzwp@163.com
 * time   : 2019/11/13 10:54
 * desc   : HTTP API接口
 */
interface ApiService {

    companion object {
        const val BASE_URL = "http://anyshare.eisoo.com:9124/"
    }

    @GET
    suspend fun get(@Url url: String): Response<String>
}