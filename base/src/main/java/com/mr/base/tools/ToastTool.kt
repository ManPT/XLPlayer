package com.mr.base.tools

import android.widget.Toast

object ToastTool {
    var mToast: Toast? = null


    fun showToast(text: CharSequence) {
        cancelToast()
        if (mToast == null) {
            mToast =
                Toast.makeText(Tool.mContext, text, Toast.LENGTH_SHORT)
        } else {
            mToast!!.setText(text)
            mToast!!.duration = Toast.LENGTH_SHORT
        }

        mToast!!.show()
    }



    fun showToast(text: CharSequence, duration: Int) {
        cancelToast()
        if (mToast == null) {
            mToast =
                Toast.makeText(Tool.mContext, text, duration)
        } else {
            mToast!!.setText(text)
            mToast!!.duration = duration
        }

        mToast!!.show()
    }

    /**
     * 取消吐司显示
     */
    fun cancelToast() {
        if (mToast!= null) {
            mToast!!.cancel()
            mToast = null
        }
    }

}