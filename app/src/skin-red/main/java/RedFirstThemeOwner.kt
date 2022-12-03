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
class RedFirstThemeOwner : FirstThemeOwner() {
    override fun theme(): FirstTheme {
        return RedFirstTheme()
    }

    override fun companionTheme(): FirstThemeCompanionTheme {
        return RedFirstCompanionTheme()
    }
}


class RedFirstTheme : FirstTheme() {
    override val btnTextColor: Int
        get() = ResUtil.getColor(R.color.skin_red_btn_textColor)
    override val textColor = R.color.skin_red_text_color
}


class RedFirstCompanionTheme : FirstThemeCompanionTheme() {
    override val btnTextColor: Int
        get() = ResUtil.getColor(R.color.skin_red_companion_btn_textColor)
    override val textColor = R.color.skin_red_companion_text_color
}