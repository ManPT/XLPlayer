package com.lib.tools

import android.util.Log
import com.mr.base.BuildConfig


object LogTool {

    fun d(tag: String, str: String) {
        if (BuildConfig.Debug) {
            Log.d(tag, str)
        }
    }

    public fun i(tag: String, str: String) {
        if (BuildConfig.Debug) {
            Log.i(tag, str)
        }
    }


    public fun e(tag: String, str: String) {
        if (BuildConfig.Debug) {
            Log.e(tag, str)
        }
    }

    public fun w(tag: String, str: String) {
        if (BuildConfig.Debug) {
            Log.w(tag, str)
        }
    }

    public fun v(tag: String, str: String) {
        if (BuildConfig.Debug) {
            Log.v(tag, str)
        }
    }

}