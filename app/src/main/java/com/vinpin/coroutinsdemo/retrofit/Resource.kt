package com.vinpin.coroutinsdemo.retrofit

import org.json.JSONObject
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

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

        fun <T : Any> success(data: T) = Resource(Status.SUCCESS, data, null)

        fun error(error: ApiException) = Resource(Status.ERROR, null, error)

        fun <T : Any> error(error: ApiException, data: T?) = Resource(Status.ERROR, data, error)

        fun loading() = Resource(Status.LOADING, null, null)

        fun <T : Any> loading(data: T?) = Resource(Status.LOADING, data, null)
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
class ApiException(var errorCode: Int, var errorMsg: String) : Exception(errorMsg) {

    var causeMsg: String? = null
    var detail: String? = null
    var error: Throwable? = null

    companion object {

        const val TAG = "ApiException"

        /** 根据指定的错误json字符串来创建对应的异常 */
        fun createApiException(jsonString: String?): ApiException {
            var apiException: ApiException? = null
            jsonString?.let {
                try {
                    val jsonObject = JSONObject(jsonString)
                    val errcode = jsonObject.optInt("errcode")
                    val errmsg = jsonObject.optString("errmsg")
                    apiException = ApiException(errcode, errmsg)
                    apiException?.causeMsg = jsonObject.optString("causemsg")
                    apiException?.detail = jsonObject.optJSONObject("detail")?.toString() ?: ""
                } catch (e: Exception) {
                }
            }
            return apiException ?: ApiException(1, "")
        }
    }
}

/** 处理请求层的错误，对可能的已知的错误进行处理。*/
fun handlingExceptions(e: Throwable): ApiException {
    if (e is ApiException) {
        return e
    }
    return when (e) {
        is HttpException -> {
            val exception = ApiException.createApiException(e.response()?.errorBody()?.string())
            exception.error = e
            exception
        }
        is ConnectException -> ApiException(1, "")
        is UnknownHostException -> ApiException(1, "")
        is SocketTimeoutException -> ApiException(1, "")
        else -> {
            val exception = ApiException(1, "")
            exception.error = e
            exception
        }
    }
}

