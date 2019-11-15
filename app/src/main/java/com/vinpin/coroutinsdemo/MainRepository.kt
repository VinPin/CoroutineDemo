package com.vinpin.coroutinsdemo

import com.vinpin.coroutinsdemo.retrofit.ApiService
import com.vinpin.coroutinsdemo.retrofit.KcRetrofitUtils
import com.vinpin.coroutinsdemo.retrofit.Resource
import com.vinpin.coroutinsdemo.retrofit.handlingExceptions
import kotlinx.coroutines.*

/**
 * author : vinpin
 * e-mail : hearzwp@163.com
 * time   : 2019/11/14 9:08
 * desc   : 数据仓库
 */
class MainRepository {

    suspend fun getBanner(): Resource<out String> = withContext(Dispatchers.IO) {
        try {
            val apiService = KcRetrofitUtils.create(ApiService::class.java)
            val result = apiService.get("https://www.wanandroid.com/banner/json")
            val body: String = result.body().toString()
            Resource.success(body)
        } catch (e: Exception) {
            Resource.error(handlingExceptions(e))
        }
    }
}
