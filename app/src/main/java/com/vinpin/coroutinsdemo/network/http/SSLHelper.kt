package com.vinpin.coroutinsdemo.network.http

import android.annotation.SuppressLint
import java.io.IOException
import java.io.InputStream
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.UnrecoverableKeyException
import java.security.cert.Certificate
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate

import javax.net.ssl.HostnameVerifier
import javax.net.ssl.KeyManager
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

/**
 * author : vinpin
 * e-mail : hearzwp@163.com
 * time   : 2019/2/22 17:38
 * desc   : Android HTTPS 请求证书验证类
 */
object SSLHelper {

    // 获取这个SSLSocketFactory
    val sslSocketFactory: SSLSocketFactory
        get() {
            try {
                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, arrayOf<TrustManager>(trustManager), SecureRandom())
                return sslContext.socketFactory
            } catch (e: Exception) {
                throw RuntimeException(e)
            }

        }

    // 获取TrustManager
    val trustManager: X509TrustManager
        get() = object : X509TrustManager {
            @SuppressLint("TrustAllX509TrustManager")
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {

            }

            @SuppressLint("TrustAllX509TrustManager")
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {

            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        }

    // 获取HostnameVerifier
    val hostnameVerifier: HostnameVerifier
        get() = HostnameVerifier { _, _ -> true }

    /**
     * 单向认证
     *
     * @param caIn 服务端配置的证书输入流
     */
    fun getSSLContext(caIn: InputStream): SSLContext {
        try {
            val trustManagers =
                getTrustManagers(caIn)
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustManagers, null)
            return sslContext
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

    /**
     * 单向认证
     *
     * @param tsIn  服务端配置的证书转bks输入流
     * @param tsPwd 服务端配置的证书转bks的密码
     */
    fun getSSLContext(tsIn: InputStream, tsPwd: String): SSLContext {
        try {
            val trustManagers =
                getTrustManagers(tsIn, tsPwd)
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustManagers, null)
            return sslContext
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

    /**
     * 双向认证
     *
     * @param caIn  服务端配置的证书输入流
     * @param ksIn  客户端配置的证书转bks，Android只支持的bks格式
     * @param ksPwd 客户端配置的证书转bks的密码
     */
    fun getSSLContext(caIn: InputStream, ksIn: InputStream, ksPwd: String): SSLContext {
        try {
            val keyManagers =
                getKeyManagers(ksIn, ksPwd)
            val trustManagers =
                getTrustManagers(caIn)
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(keyManagers, trustManagers, null)
            return sslContext
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

    /**
     * 双向认证
     *
     * @param tsIn  服务端配置的证书转bks输入流
     * @param tsPwd 服务端配置的证书转bks的密码
     * @param ksIn  客户端配置的证书转bks，Android只支持的bks格式
     * @param ksPwd 客户端配置的证书转bks的密码
     */
    fun getSSLContext(
        tsIn: InputStream, tsPwd: String, ksIn: InputStream,
        ksPwd: String
    ): SSLContext {
        try {
            val keyManagers =
                getKeyManagers(ksIn, ksPwd)
            val trustManagers =
                getTrustManagers(tsIn, tsPwd)
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(keyManagers, trustManagers, null)
            return sslContext
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

    /**
     * 机构颁发的证书和自签名证书(单向认证)
     *
     *
     * 从 InputStream 获取一个特定的 CA，用该 CA 创建 KeyStore，然后用后者创建和初始化 TrustManager。
     * TrustManager 是系统用于从服务器验证证书的工具，可以使用一个或多个 CA 从 KeyStore 创建，
     * 而创建的 TrustManager 将仅信任这些 CA。
     *
     * @param caIn 服务端配置的证书输入流
     * @return the trust managers
     */
    private fun getTrustManagers(caIn: InputStream?): Array<TrustManager>? {
        if (caIn == null) {
            return null
        }
        try {
            val cf = CertificateFactory.getInstance("X.509")
            val ca: Certificate
            try {
                ca = cf.generateCertificate(caIn)
            } finally {
                caIn.close()
            }

            val trustStore = KeyStore.getInstance(KeyStore.getDefaultType())
            trustStore.load(null, null)
            trustStore.setCertificateEntry("ca", ca)

            val trustManagerFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(trustStore)
            return trustManagerFactory.trustManagers
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: CertificateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 机构颁发的证书和自签名证书(单向认证)
     *
     * @param tsIn     服务端配置的证书转bks输入流
     * @param password 服务端配置的证书转bks的密码
     * @return the trust managers
     */
    private fun getTrustManagers(tsIn: InputStream?, password: String?): Array<TrustManager>? {
        if (tsIn == null || password == null) {
            return null
        }
        try {
            val trustStore = KeyStore.getInstance("BKS")
            trustStore.load(tsIn, password.toCharArray())
            try {
                tsIn.close()
            } catch (ignored: IOException) {
            }

            val trustManagerFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(trustStore)
            return trustManagerFactory.trustManagers
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: CertificateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 双向认证
     *
     * @param ksIn     客户端配置的证书转bks，Android只支持的bks格式
     * @param password 客户端配置的证书转bks的密码
     * @return the key managers
     */
    private fun getKeyManagers(ksIn: InputStream?, password: String?): Array<KeyManager>? {
        if (ksIn == null || password == null) {
            return null
        }
        try {
            val keyStore = KeyStore.getInstance("BKS")
            keyStore.load(ksIn, password.toCharArray())
            val keyManagerFactory =
                KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
            keyManagerFactory.init(keyStore, password.toCharArray())
            return keyManagerFactory.keyManagers
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: CertificateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        } catch (e: UnrecoverableKeyException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }
}
