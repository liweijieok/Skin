package com.example.daynightmode.skin

import androidx.lifecycle.MutableLiveData

/**
 * Project Name: SimplePermission
 *
 * @author liweijie
 * @date : 2022/11/30 17:19
 * @email: liweijieok@qq.com
 * @desc:
 * @lastModify:
 */
object AppThemeController {

    // 当前模式
    private var currentMode = AppThemeType.RED

    var isShowCompanion = false

    val globalTheme by lazy { MutableLiveData<GlobalTheme>() }

    private val mListener = mutableListOf<ISkinChange>()

    fun getCurrent() = currentMode

    @Synchronized
    fun registerSkinChange(listener: ISkinChange) {
        mListener.add(listener)
    }

    @Synchronized
    fun unregisterSkinChange(listener: ISkinChange) {
        mListener.remove(listener)
    }

    /**
     * 切换伴生皮肤
     */
    fun changeCompanion() {
        isShowCompanion = !isShowCompanion
        changeSkin(currentMode)
    }

    @Synchronized
    fun changeSkin(newTheme: AppThemeType) {
        // TODO 如果需要globalTheme，可以在这里设置
        currentMode = newTheme
        if (mListener.isEmpty()) {
            return
        }
        mListener.forEach {
            it.onSkinChange(newTheme)
        }
    }

}

fun interface ISkinChange {
    fun onSkinChange(theme: AppThemeType)
}

open class GlobalTheme : AppBaseTheme() {
    // 可以设置一些不跟随页面变化的主题数
    // 不同的主题可以有不同的GlobalTheme
    // 子主题复写当前类即可
}