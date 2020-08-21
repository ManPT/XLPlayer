package com.lib.tools

import android.content.Context

object SharedPreferencesTool {
    private const val defaultName = "shared_preferences"

    fun saveData(context: Context, key: String?, string: String?) {
        val sharedPreferences = context.getSharedPreferences(defaultName, 0)
        val editor = sharedPreferences.edit()
        editor.putString(key, string)
        editor.commit()
    }

    fun saveData(context: Context, name: String?, key: String?, string: String?) {
        val sharedPreferences = context.getSharedPreferences(name, 0)
        val editor = sharedPreferences.edit()
        editor.putString(key, string)
        editor.commit()
    }

    fun getData(context: Context, key: String?, defValue: String): String {
        val sharedPreferences = context.getSharedPreferences(defaultName, 0)
        return if (defValue is String) {
            try {
                sharedPreferences.getString(key, defValue)
            } catch (var6: Exception) {
                defValue
            }
        } else {
            defValue
        }
    }

    fun getData(context: Context, name: String?, key: String?, defValue: String): String {
        val sharedPreferences = context.getSharedPreferences(name, 0)
        return if (defValue is String) {
            try {
                sharedPreferences.getString(key, defValue)
            } catch (var6: Exception) {
                defValue
            }
        } else {
            defValue
        }
    }
}