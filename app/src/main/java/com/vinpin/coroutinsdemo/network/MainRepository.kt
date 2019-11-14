package com.vinpin.coroutinsdemo.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

/**
 * author : vinpin
 * e-mail : hearzwp@163.com
 * time   : 2019/11/14 9:08
 * desc   : 数据仓库
 */
class MainRepository {

    suspend fun getBanner(): String? = withContext(Dispatchers.IO) {
        try {
            val apiService = KcRetrofitUtils.create(ApiService::class.java)
            val result: Response<String> = apiService.get("https://www.wanandroid.com/banner/json")
            if (result.isSuccessful) {
                val body = result.body()
                Log.d("test", "success >>> " + body.toString())
                "success"
            } else {
                val errorBody = result.errorBody()
                Log.d("test", "error >>> " + errorBody.toString())
                "error"
            }
        } catch (e: Exception) {
            Log.d("test", "exception >>> " + e.printStackTrace())
            null
        }
    }
}
