package com.bluetree.myskinlib

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources

/**
 * 皮肤管理类
 */
class SkinManager {

    var context: Context? = null
    /**
     * 皮肤资源操作对象
     */
    var skinRes :Resources? = null

    /**
     * 获取到皮肤插件的包名
     */
    private var packageName: String? = null

    /**
     * 皮肤插件的文件路径
     */
    var skinPath: String = ""
    set(value) {
        skinPath = value
        context?.let {
            //1、加载皮肤插件，获取皮肤插件的资源文件管理类

            //1-1、根据皮肤插件的路径，获取包名
            var packageInfo = it.packageManager.getPackageArchiveInfo(value, PackageManager.GET_ACTIVITIES)
            packageName = packageInfo.packageName

            //1-2、根据包名，实例化AssetManager来创建插件的Resource对象
            val assets = AssetManager::class.java.newInstance()
            var method = assets.javaClass.getMethod("addAssetPath", String::class.java)//获取addAssetPath方法对象
            method.invoke(assets, packageName)//调用方法，传入对象

            //1-3、获取皮肤插件的resource对象
            skinRes = Resources(assets,it.resources.displayMetrics, it.resources.configuration)

        }
    }

    companion object {

        private val instance: SkinManager = SkinManager()

        fun getInstance():SkinManager{
            return instance
        }
    }


}
