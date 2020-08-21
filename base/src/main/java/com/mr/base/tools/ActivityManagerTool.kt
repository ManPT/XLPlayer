package com.lib.tools

import com.lib.base.BaseActivity

object ActivityManagerTool {
    var activityList: ArrayList<BaseActivity> = ArrayList()

    fun createActivity(activity: BaseActivity) {
        if (!activityList.contains(activity)) {
            activityList.add(activity)
        }
    }

    fun destroyActivity(activity: BaseActivity) {
        if (activityList.contains(activity)) {
            activityList.remove(activity)
        }
    }

    fun clearAllActivity() {
        for (activity: BaseActivity in activityList) {
            activity.finish()
        }
        activityList.clear()
    }


}