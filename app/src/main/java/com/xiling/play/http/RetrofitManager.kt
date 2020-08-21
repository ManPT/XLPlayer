package com.xiling.play.http

import android.text.TextUtils
import com.blankj.utilcode.utils.AppUtils
import com.lib.net.AbsRetrofitManager
import com.xiling.play.BuildConfig
import com.xiling.play.XLPlayerApplication
import com.xiling.play.login.UserManager
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.*
import java.util.concurrent.TimeUnit

class RetrofitManager : AbsRetrofitManager() {

    override fun getBaseUrl(): String? {
        return BuildConfig.BASE_URL
    }


    override fun getHttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val originalHttpUrl = originalRequest.url()
                val appVersion: String = AppUtils.getAppVersionName(XLPlayerApplication.context)
                var builder = originalRequest.newBuilder()
                val httpUrl = originalHttpUrl.newBuilder() // 增加 API 版本号
                    .addQueryParameter("version", "1") //增加平台号
                    .addQueryParameter("platform", "1")
                    .addQueryParameter("appVersion", appVersion )//增加App版本号
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
                            if (cookie.name().equals(UserManager.OAUTH, ignoreCase = true)) {
                                UserManager.setOAuthToken(cookie.value())
                                //共享认证的Cookie
                                //shareCookies(cookie)
                                break
                            }
                        }
                    }
                }

                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    val cookies: MutableList<Cookie> =
                        ArrayList()
                    val oAuthToken: String? = UserManager.getOAuthToken()
                    if (!TextUtils.isEmpty(oAuthToken)) {
                        val cookie = Cookie.Builder()
                            .name(UserManager.OAUTH).value(oAuthToken)
                            .path("/")
                            .hostOnlyDomain("*.beautysecret.cn")
                            .build()
                        cookies.add(cookie)
                    }
                    return cookies
                }
            })
            .connectTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }



}