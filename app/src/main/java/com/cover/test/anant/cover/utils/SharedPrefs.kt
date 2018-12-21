package com.cover.test.anant.cover.utils

import android.content.Context
import android.content.SharedPreferences
import com.cover.test.anant.cover.application.CoverApplication.Companion.applicationContext

/**
 * Created by anant on 2018-12-21.
 */
fun getSharedPrefs(): SharedPreferences {
    return applicationContext().getSharedPreferences(
            "com.cover.test.anant.cover", Context.MODE_PRIVATE)
}

fun isUpdated(isUpdated: Boolean) {

    val editor = getSharedPrefs().edit()
    editor.putBoolean("isUpdated", isUpdated)
    editor.apply()
}

fun ifUpdated(): Boolean {

    return getSharedPrefs().getBoolean("isUpdated", false)
}