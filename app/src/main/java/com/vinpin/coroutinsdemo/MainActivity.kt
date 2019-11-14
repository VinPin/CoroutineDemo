package com.vinpin.coroutinsdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.vinpin.coroutinsdemo.network.ApiService
import com.vinpin.coroutinsdemo.network.KcRetrofitUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    /**
     * activity 中协程作用域
     * 1. 实现CoroutineScope接口并委托by MainScope()
     * 2. 在onDestroy方法中调用 cancel()取消作用域内所有的协程
     */

    private val mViewModel: MainViewModel by lazy {
        val factory = ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
        ViewModelProviders.of(this, factory).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mViewModel.toastMsg().observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })

        txt_btn_1.setOnClickListener {
            mViewModel.getBanner()
        }
        txt_btn_2.setOnClickListener {
            mViewModel.test()
        }
        txt_btn_3.setOnClickListener {
            mViewModel.asyncTest()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}
