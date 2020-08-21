package com.lib.net

/**
 * 网络请求配置项
 */
object HttpConfig {

    /**
     * 默认服务器地址
     */
    val BASE_URL :String = "https://raw.githubusercontent.com/"

    /**
     * 请求超时时间/分钟
     */
    val CONNECT_TIME_OUT = 5L
    /**
     * 读取超时时间/分钟
     */
    val READ_TIME_OUT = 5L
    /**
     * 写入超时时间
     */
    val WRITE_TIME_OUT = 5L



}