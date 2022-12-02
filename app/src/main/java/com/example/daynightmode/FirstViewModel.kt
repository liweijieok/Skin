package com.example.daynightmode

import com.example.daynightmode.skin.AppBaseThemeOwner
import com.example.daynightmode.skin.AppThemeType
import com.example.daynightmode.skin.BaseSkinModel
import com.skin.default.FirstTheme
import com.skin.default.FirstThemeOwner

/**
 * Project Name: SimplePermission
 *
 * @author liweijie
 * @date : 2022/11/30 17:16
 * @email: liweijieok@qq.com
 * @desc:
 * @lastModify:
 */
class FirstViewModel : BaseSkinModel<FirstTheme>() {
    override fun getSkins(): Map<AppThemeType, Class<out AppBaseThemeOwner<FirstTheme>>> {
        return mapOf(
            AppThemeType.DEFAULT to FirstThemeOwner::class.java
        )
    }
}