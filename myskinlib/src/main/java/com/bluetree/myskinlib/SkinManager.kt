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
        context?.let {
            //1、加载皮肤插件，获取皮肤插件的资源文件管理类

            //1-1、根据皮肤插件的路径，获取包名
            var packageInfo = it.packageManager.getPackageArchiveInfo(value, PackageManager.GET_ACTIVITIES)
            packageName = packageInfo.packageName

            //1-2、根据包名，实例化AssetManager来创建插件的Resource对象
            /*  val assets = AssetManager::class.java.newInstance()
              var method = assets.javaClass.getMethod("addAssetPath", String::class.java)//获取addAssetPath方法对象
              method.invoke(assets, packageName)//调用方法，传入对象*/


            var assets: AssetManager? = null
            try {
                assets = AssetManager::class.java.newInstance()
                val method = assets!!.javaClass.getDeclaredMethod("addAssetPath", String::class.java)//方法名，参数类型
                method.invoke(assets, value)//调用该方法的对象，传入的参数
            } catch (e: Exception) {
                e.printStackTrace()
            }


            //1-3、获取皮肤插件的resource对象
            skinRes = Resources(assets,it.resources.displayMetrics, it.resources.configuration)
        }
    }

    fun getColor(propertyItem: SkinFactory2.PropertyItem):Int {
        if(skinRes == null|| context == null) return propertyItem.viewAtrriValueId
        val newAttriId = skinRes!!.getIdentifier(propertyItem.attriName, propertyItem.attriType, packageName)
        if(newAttriId == 0) return propertyItem.viewAtrriValueId

        var result = 0

        try {
            result = skinRes?.getColor(newAttriId)!!//*****这句话非常关键，一定要在这个资源上面取得资源，否则失效****
        } catch (e: Exception) {
            e.printStackTrace()
            result = 0
        }
        return result
    }

    companion object {

        private val instance: SkinManager = SkinManager()

        fun getInstance():SkinManager{
            return instance
        }
    }


}
