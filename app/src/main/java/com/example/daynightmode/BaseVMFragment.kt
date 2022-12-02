package com.example.daynightmode

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.daynightmode.skin.AppBaseTheme
import com.example.daynightmode.skin.BaseSkinModel
import java.lang.reflect.ParameterizedType

/**
 * Project Name: SimplePermission
 *
 * @author liweijie
 * @date : 2022/11/30 18:28
 * @email: liweijieok@qq.com
 * @desc:
 * @lastModify:
 */
open class BaseVMFragment<VM : BaseSkinModel<out AppBaseTheme>> : Fragment() {

    lateinit var mVm: VM
    var mProvider: ViewModelProvider? = null

    open fun createVm() {
        //默认取子类泛型的第一个泛型类型
        val clazz =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<out VM>
        mVm = getProvider().get(clazz)
    }

    open fun getProvider(): ViewModelProvider {
        if (mProvider == null) {
            mProvider = ViewModelProvider(this)
        }
        return mProvider!!
    }
}