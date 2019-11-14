package com.vinpin.coroutinsdemo.network

import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

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

    @POST
    suspend fun post(@Url url: String): Response<String>

    @POST
    suspend fun post(@Url url: String, @Body body: RequestBody): Response<String>

    @POST
    suspend fun post(
        @Url url: String, @Body body: RequestBody, @HeaderMap headerMap: Map<String, String>
    ): Response<String>

    @POST
    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded")
    suspend fun post(@Url url: String, @FieldMap fieldMap: Map<String, String>): Response<String>

    @GET
    suspend fun get(@Url url: String): Response<String>

    @GET
    suspend fun get(@Url url: String, @QueryMap queryMap: Map<String, String>): Response<String>
}