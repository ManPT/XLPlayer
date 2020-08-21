package com.xiling.play.im

import android.util.Log
import com.tencent.imsdk.v2.*
import com.xiling.play.XLPlayerApplication
import com.xiling.play.BuildConfig


/**
 * 腾讯IM实现类
 */
object IMTencentImpl : IMInterface {


    /**
     * 初始化 -- application时调用
     */
    override fun init() {
        val config = V2TIMSDKConfig()
        config.setLogLevel(V2TIMSDKConfig.V2TIM_LOG_INFO)
        V2TIMManager.getInstance()
            .initSDK(XLPlayerApplication.context, BuildConfig.IM_APPID, config, object :
                V2TIMSDKListener() {
                override fun onConnecting() {
                    // 正在连接到腾讯云服务器
                    Log.d("pangtao","正在连接到腾讯云服务器")
                }

                override fun onConnectSuccess() {
                    // 已经成功连接到腾讯云服务器
                    Log.d("pangtao","已经成功连接到腾讯云服务器")
                }

                override fun onConnectFailed(code: Int, error: String?) {
                    // 连接腾讯云服务器失败
                    Log.d("pangtao","连接腾讯云服务器失败")
                }
            }
            )
    }

    override fun login(userId: String, userSig: String, callback: LoginCallBack) {
        V2TIMManager.getInstance().login(userId, userSig, object : V2TIMCallback {
            override fun onSuccess() {
                callback.loginSuccess()
            }

            override fun onError(p0: Int, p1: String?) {
                callback.loginError(p0, p1!!)
            }

        })
    }

    override fun logout(callback: LoginCallBack) {
        V2TIMManager.getInstance().logout(object : V2TIMCallback {
            override fun onSuccess() {
                callback.loginSuccess()
            }

            override fun onError(p0: Int, p1: String?) {
                callback.loginError(p0, p1!!)
            }

        })
    }

    override fun sendMessage(message: String, id: String, callback: IMValueCallBack<String>) {
        V2TIMManager.getInstance().sendGroupTextMessage(
            message,
            id,
            V2TIMMessage.V2TIM_PRIORITY_HIGH,
            object : V2TIMValueCallback<V2TIMMessage> {
                override fun onSuccess(p0: V2TIMMessage?) {
                    callback.success("")
                }

                override fun onError(p0: Int, p1: String?) {
                    callback.error(p0, p1!!)
                }


            });
    }

    override fun createGroup(type: String, callback: IMValueCallBack<String>) {
        V2TIMManager.getInstance()
            .createGroup(type, null, "直播群", object : V2TIMValueCallback<String> {
                override fun onSuccess(p0: String?) {
                    callback.success(p0!!)
                }

                override fun onError(p0: Int, p1: String?) {
                    callback.error(p0, p1!!)
                }

            })
    }

    override fun joinGroup(groupId: String, callback: IMValueCallBack<String>) {
        V2TIMManager.getInstance().joinGroup(groupId, "", object : V2TIMCallback {
            override fun onSuccess() {
                callback.success("");
            }

            override fun onError(p0: Int, p1: String?) {
                callback.error(p0, p1!!);
            }


        })
    }

    override fun quitGroup(groupId: String, callback: IMValueCallBack<String>) {
        V2TIMManager.getInstance().quitGroup(groupId, object : V2TIMCallback {
            override fun onSuccess() {
                callback.success("");
            }

            override fun onError(p0: Int, p1: String?) {
                callback.error(p0, p1!!);
            }


        })
    }

    override fun dismissGroup(groupId: String, callback: IMValueCallBack<String>) {
        V2TIMManager.getInstance().dismissGroup(groupId, object : V2TIMCallback {
            override fun onSuccess() {
                callback.success("");
            }

            override fun onError(p0: Int, p1: String?) {
                callback.error(p0, p1!!);
            }


        })
    }
}