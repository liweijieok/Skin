package com.skin.default

import com.example.daynightmode.R
import com.example.daynightmode.skin.AppBaseTheme
import com.example.daynightmode.skin.AppBaseThemeOwner
import com.example.daynightmode.skin.ISkinInterceptor
import com.example.daynightmode.util.ResUtil

/**
 * Project Name: SimplePermission
 *
 * @author liweijie
 * @date : 2022/12/2 17:39
 * @email: liweijieok@qq.com
 * @desc:
 * @lastModify:
 */
open class FirstThemeOwner() : AppBaseThemeOwner<FirstTheme>() {
    override fun theme(): FirstTheme {
        return FirstTheme()
    }

    override fun companionTheme(): FirstThemeCompanionTheme {
        return FirstThemeCompanionTheme()
    }
}

open class FirstTheme : AppBaseTheme() {
    open val btnTextColor = ResUtil.getColor(R.color.white)
}

open class FirstThemeCompanionTheme : FirstTheme() {
    override val btnTextColor = ResUtil.getColor(R.color.white)
}