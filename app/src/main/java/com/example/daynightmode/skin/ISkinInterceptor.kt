package com.example.daynightmode.skin

/**
 * Project Name: SimplePermission
 *
 * @author liweijie
 * @date : 2022/12/2 16:17
 * @email: liweijieok@qq.com
 * @desc:
 * @lastModify:
 */
fun interface ISkinInterceptor {
    /**
     * 是否开启拦截
     * @return true为开启 false为关闭
     */
    fun intercept(): Boolean
}