package com.example.daynightmode.skin


import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

/**
 * Project Name: SimplePermission
 *
 * @author liweijie
 * @date : 2022/11/30 17:15
 * @email: liweijieok@qq.com
 * @desc:
 * @lastModify:
 */
@Suppress("UNCHECKED_CAST", "LeakingThis")
abstract class BaseSkinModel<T : AppBaseTheme> : ViewModel(), ISkinChange {
    val theme = ObservableField<T>()

    init {
        AppThemeController.registerSkinChange(this)
        val now = getSkins()[AppThemeController.getCurrent()]
        kotlin.runCatching { now?.newInstance() }
            .onFailure { it.printStackTrace() }
            .getOrNull()?.let {
                theme.set(it.getTheme())
            }
    }

    override fun onCleared() {
        super.onCleared()
        AppThemeController.unregisterSkinChange(this)
    }

    override fun onSkinChange(theme: AppThemeType) {
        val skins = getSkins()
        if (skins.isEmpty()) {
            return
        }
        val target = skins[theme] ?: return
        kotlin.runCatching {
            target.newInstance()
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()?.let {
            this.theme.set(it.getTheme())
        }
    }

    abstract fun getSkins(): Map<AppThemeType, Class<out AppBaseThemeOwner<T>>>

}