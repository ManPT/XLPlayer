package com.mr.base

import android.content.Context
import com.lib.net.AbsRetrofitManager
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

/**
 * 此类提供一个参考，没有实际意义
 *
 * 用于自定义HttpClient
 * 此处的HttpClient提供了以下几种功能：
 * 1. 添加Log拦截器
 * 2. 添加拦截器，为Url 拼接固定字段
 * 3. cookies 的保存与使用
 * 4. 设置超时时间
 */
class RetrofitManager : AbsRetrofitManager() {

    var context : Context? = null

    override fun getBaseUrl(): String? {
        return "https://xl-mall.xilingbm.com/xl-api/"
    }


    override fun getHttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val originalHttpUrl = originalRequest.url()
               // val appVersion: String = AppUtils.getAppVersionName(context)
                var builder = originalRequest.newBuilder()
                val httpUrl = originalHttpUrl.newBuilder() // 增加 API 版本号
                    .addQueryParameter("version", "1") //增加平台号
                    .addQueryParameter("platform", "1")
                   // .addQueryParameter("appVersion", appVersion )//增加App版本号
                    .build()
                builder = builder.url(httpUrl)

                // 重新构建请求
                chain.proceed(builder.build())
            }
            .cookieJar(object : CookieJar {
                override fun saveFromResponse(
                    url: HttpUrl,
                    cookies: List<Cookie>
                ) {
                    if (null != cookies && cookies.size > 0) {
                        for (cookie in cookies) {
                            /*if (cookie.name().equals(UserManager.OAUTH, ignoreCase = true)) {
                                UserManager.setOAuthToken(cookie.value())
                                //共享认证的Cookie
                                //shareCookies(cookie)
                                break
                            }*/
                        }
                    }
                }

                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    val cookies: MutableList<Cookie> =
                        ArrayList()
                   /* val oAuthToken: String? = UserManager.getOAuthToken()
                    if (!TextUtils.isEmpty(oAuthToken)) {
                        val cookie = Cookie.Builder()
                            .name(UserManager.OAUTH).value(oAuthToken)
                            .path("/")
                            .hostOnlyDomain("*.beautysecret.cn")
                            .build()
                        cookies.add(cookie)
                    }*/
                    return cookies
                }
            })
            .connectTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }



}