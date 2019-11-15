package com.vinpin.coroutinsdemo

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

/**
 * author : vinpin
 * e-mail : hearzwp@163.com
 * time   : 2019/11/13 14:42
 * desc   :
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var mMainRepository: MainRepository =
        MainRepository()

    private val mToastMsg: MutableLiveData<String> = MutableLiveData()

    fun toastMsg(): LiveData<String> = mToastMsg

    /**
     * ViewModel 中协程作用域
     * 1. 添加扩展依赖 implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.1.0'
     * 2. 直接使用viewModelScope即可
     */

    fun getBanner() {
        viewModelScope.launch {
            val startTime = System.currentTimeMillis()
            val banner = mMainRepository.getBanner()
            val endTime = System.currentTimeMillis()
            Log.d("test", "$banner  耗时: ${endTime - startTime}")
            mToastMsg.postValue("$banner 耗时: ${endTime - startTime}")
        }
    }

    /**
     * 串行执行
     */
    fun test() {
        viewModelScope.launch {
            val startTime = System.currentTimeMillis()
            val banner = mMainRepository.getBanner()
            val banner1 = mMainRepository.getBanner()
            val endTime = System.currentTimeMillis()
            Log.d("test", "$banner 和 $banner1  耗时: ${endTime - startTime}")
            mToastMsg.postValue("$banner 和 $banner1  耗时: ${endTime - startTime}")
        }
    }

    /**
     * 并发执行
     */
    fun asyncTest() {
        viewModelScope.launch {
            val startTime = System.currentTimeMillis()
            val deferred = async {
                mMainRepository.getBanner()
            }
            val deferred1 = async {
                mMainRepository.getBanner()
            }
            val banner = deferred.await()
            val banner1 = deferred1.await()
            val endTime = System.currentTimeMillis()
            Log.d("test", "$banner 和 $banner1  耗时: ${endTime - startTime}")
            mToastMsg.postValue("$banner 和 $banner1  耗时: ${endTime - startTime}")
        }
    }
}
