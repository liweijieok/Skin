package com.github.liweijie.skin

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources

/**
 * Project Name: SimplePermission
 *
 * @author liweijie
 * @date : 2022/11/24 17:42
 * @email: liweijieok@qq.com
 * @desc:
 * @lastModify:
 */
@SuppressLint("StaticFieldLeak")
object SkinLoaderManager {

    private var skinResource: SkinResManager? = null
    private var originalResource: SkinResManager? = null
    private var ctx: Context? = null

    fun init(ctx: Context) {
        this.ctx = ctx.applicationContext
        val pkgName = ctx.packageName
        originalResource = SkinResManager(pkgName, ctx.resources)
    }

    /**
     * 获取当前的有效的资源
     * 假如有换肤则使用换肤的，没有就返回系统的
     * @param ctx
     * @return
     */
    fun getResource(ctx: Context?): SkinResManager {
        var useResource = skinResource ?: originalResource
        if (useResource == null) {
            if (ctx != null) {
                useResource = SkinResManager(ctx.applicationContext.packageName, ctx.applicationContext.resources)
            } else {
                throw IllegalStateException("ctx param is null and not use Resource")
            }
        }
        return useResource
    }


    /**
     * 加载换肤插件，支持自定义创建
     * @param ctx
     * @param filePath
     * @param packageName
     * @param creator
     */
    fun loadSkin(ctx: Context?, filePath: String, packageName: String, creator: ISkinResourceCreator? = null) {
        if (ctx == null) {
            this.ctx = ctx?.applicationContext
        }
        if (this.ctx == null) {
            throw IllegalStateException(" not call init method &&  loadSkin method context is null")
        }
        val readCreator = creator ?: DefaultSkinResourceCreator()
        readCreator.createSkinResource(this.ctx!!, filePath)?.let {
            skinResource = SkinResManager(packageName, it)
        }
    }

}

interface ISkinResourceCreator {
    fun createSkinResource(context: Context, filePath: String): Resources?
}


class DefaultSkinResourceCreator : ISkinResourceCreator {
    override fun createSkinResource(context: Context, filePath: String): Resources? {
        return kotlin.runCatching {
            val assert = AssetManager::class.java.newInstance()
            val method = assert.javaClass.getDeclaredMethod("addAssetPath", String::class.java)
            method.invoke(assert, filePath)
            val superRes: Resources = context.resources
            Resources(assert, superRes.displayMetrics, superRes.configuration)
        }.getOrNull()
    }

}