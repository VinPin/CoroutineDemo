package com.vinpin.coroutinsdemo.network.http

import java.lang.Exception

/**
 * author : vinpin
 * e-mail : hearzwp@163.com
 * time   : 2019/11/13 15:06
 * desc   : 封装数据及其状态的 Resource 类来公开网络状态
 */
class Resource<T> private constructor() {

    lateinit var status: Status
    var data: T? = null
    var error: ApiException? = null

    private constructor(status: Status, data: T?, error: ApiException?) : this() {
        this.status = status
        this.data = data
        this.error = error
    }

    companion object {

        fun <T> success(data: T) = Resource(Status.SUCCESS, data, null)

        fun <T> error(data: T? = null, error: ApiException) = Resource(Status.ERROR, data, error)

        fun <T> loading(data: T? = null) = Resource(Status.LOADING, data, null)
    }

    fun isSuccess() = status == Status.SUCCESS

    fun isError() = status == Status.ERROR

    fun isLoading() = status == Status.LOADING
}

/**
 * 数据状态类
 */
enum class Status {
    // 成功
    SUCCESS,
    // 失败
    ERROR,
    // 加载
    LOADING
}

/**
 * 封装的异常类
 */
class ApiException(errorCode: Int, errorMsg: String) : Exception(errorMsg) {

    var causeMsg: String? = null
    var detail: String? = null
    var error: Throwable? = null
}