package com.lib.net

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

open abstract class AbsRetrofitManager{
    var retrofit: Retrofit? = null


   public fun getMyRetrofit(): Retrofit? {
        if (retrofit == null) {
            getHttpClient()
            retrofit = Retrofit.Builder()
                .baseUrl(getBaseUrl() )
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getHttpClient())
                .build()
        }

        return retrofit
    }


    open fun getHttpClient() : OkHttpClient{
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        var okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(HttpConfig.CONNECT_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(HttpConfig.READ_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(HttpConfig.WRITE_TIME_OUT, TimeUnit.SECONDS)
            .build()
        return okHttpClient
    }


    public abstract open fun getBaseUrl() : String?



}