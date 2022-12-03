package com.example.daynightmode.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.os.Build
import androidx.annotation.ColorRes

/**
 * Project Name: Skin
 *
 * @author liweijie
 * @date : 2022/11/30 18:17
 * @email: liweijieok@qq.com
 * @desc:
 * @lastModify:
 */
@SuppressLint("StaticFieldLeak")
object ResUtil {
    private lateinit var context: Context

    fun getColor(@ColorRes id: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            resources().getColor(id, context.theme)
        } else {
            resources().getColor(id)
        }
    }

    private fun resources(): Resources {
        return context.resources
    }

    fun init(context: Context) {
        this.context = context.applicationContext
    }
}