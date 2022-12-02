package com.skin.green

import com.example.daynightmode.R
import com.example.daynightmode.skin.page.FirstTheme
import com.example.daynightmode.util.ResUtil

/**
 * Project Name: SimplePermission
 *
 * @author liweijie
 * @date : 2022/11/30 18:01
 * @email: liweijieok@qq.com
 * @desc:
 * @lastModify:
 */
class GreenFirstTheme : FirstTheme() {
    override val btnTextColor: Int
        get() = ResUtil.getColor(R.color.skin_green_btn_textColor)

    override val textColor: Int
        get() = R.color.skin_green_color1
}