package com.bluetree.myskinlib

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.bluetree.utilspro.LogUtils
import java.lang.reflect.Constructor

/**
 * xml布局实例化监听
 */
class SkinFactory2 : LayoutInflater.Factory2 {

    val widgetString = arrayOf("android.view.","android.widget.","android.webkit.")
    override fun onCreateView(parent: View?, name: String?, context: Context?, attrs: AttributeSet?): View {
        LogUtils.i(this, name)

        var view = null
        for (s in widgetString) {
            if (name!!.contains(s)) {
                onCreateView(name, context, attrs)
            }else{
                break
            }
        }
        if (view == null) {
            onCreateView(name, context, attrs)
        }

        return view
    }

    override fun onCreateView(name: String?, context: Context?, attrs: AttributeSet?): View {
        val item  = context?.getClassLoader()?.loadClass(name);
        var construtor:Constructor<View> = item?.getConstructor(Context::class.java, AttributeSet::class.java) as Constructor<View>
        var view = construtor?.newInstance(context, attrs)
        return view

    }

}
