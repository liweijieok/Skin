package com.skin.green

import com.example.daynightmode.R
import com.example.daynightmode.util.ResUtil
import com.skin.origin.FirstTheme
import com.skin.origin.FirstThemeCompanionTheme
import com.skin.origin.FirstThemeOwner

/**
 * Project Name: SimplePermission
 *
 * @author liweijie
 * @date : 2022/11/30 18:01
 * @email: liweijieok@qq.com
 * @desc:
 * @lastModify:
 */
class GreenFirstThemeOwner : FirstThemeOwner() {
    override fun theme(): FirstTheme {
        return GreenFirstTheme()
    }

    override fun companionTheme(): FirstThemeCompanionTheme {
        return GreenFirstCompanionTheme()
    }
}

class GreenFirstTheme : FirstTheme() {
    override val btnTextColor = ResUtil.getColor(R.color.skin_green_text_color)

    override val textColor = R.color.skin_green_btn_textColor
}

class GreenFirstCompanionTheme : FirstThemeCompanionTheme() {
    override val btnTextColor = ResUtil.getColor(R.color.skin_green_companion_text_color)
    override val textColor = R.color.skin_green_companion_btn_textColor
}