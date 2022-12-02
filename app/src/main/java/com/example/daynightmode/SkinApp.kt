package com.example.daynightmode

import android.app.Application
import com.example.daynightmode.util.ResUtil

/**
 * Project Name: SimplePermission
 *
 * @author liweijie
 * @date : 2022/11/30 18:22
 * @email: liweijieok@qq.com
 * @desc:
 * @lastModify:
 */
class SkinApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ResUtil.init(this)
    }
}