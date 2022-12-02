package com.example.daynightmode.binding_adapter

import android.widget.TextView
import androidx.databinding.BindingAdapter

/**
 * Project Name: SimplePermission
 *
 * @author liweijie
 * @date : 2022/11/30 17:06
 * @email: liweijieok@qq.com
 * @desc:
 * @lastModify:
 */
object SkinBindingAdapter {

    @JvmStatic
    @BindingAdapter("tvColor")
    fun setTextColor(view: TextView, color: Int?) {
        color?.let {
            view.setTextColor(view.context.resources.getColor(color))
        }
    }
}