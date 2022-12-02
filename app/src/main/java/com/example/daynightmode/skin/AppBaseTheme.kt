package com.example.daynightmode.skin

import com.example.daynightmode.R

/**
 * Project Name: SimplePermission
 *
 * @author liweijie
 * @date : 2022/11/30 18:50
 * @email: liweijieok@qq.com
 * @desc: 可以存放一些通用的资源
 * @lastModify:
 */
abstract class AppBaseTheme {
    open val textColor = R.color.origin
}

abstract class AppBaseThemeOwner<T : AppBaseTheme> constructor() : IAppBaseTheme<T> {

    /**
     * 获取主题
     * @return
     */
    fun getTheme(): T {
        return if (AppThemeController.getSkinInterceptor().intercept()) companionTheme() else theme()
    }

}

internal interface IAppBaseTheme<T : AppBaseTheme> {
    /**
     * 当前主题
     * @return
     */
    fun theme(): T

    /**
     * 伴生主题，类似，优先级比theme高，当开启了之后有限使用companionTheme
     * @return
     */
    fun companionTheme(): T
}
