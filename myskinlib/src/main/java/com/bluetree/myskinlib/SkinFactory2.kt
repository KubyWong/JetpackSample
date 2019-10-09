package com.bluetree.myskinlib

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.bluetree.utilspro.LogUtils
import java.lang.Exception
import java.lang.reflect.Constructor

/**
 * xml布局实例化监听
 */
class SkinFactory2 : LayoutInflater.Factory2 {

    private var skinViewList: MutableList<SkinView> = mutableListOf()
    val widgetString = arrayOf("android.view.","android.widget.","android.webkit.")
    override fun onCreateView(parent: View?, name: String?, context: Context?, attrs: AttributeSet?): View? {
        LogUtils.i(this, name)

        var view : View? = null
        if (name!!.contains(".")) {
            view = onCreateView(name, context, attrs)
        }else{
            for (s in widgetString) {
                view = onCreateView(s + name, context, attrs)
                if(view!=null) break
            }
        }
        parsetView(view,context,attrs)
        //applyNewSkin()

        return view
    }

    /**
     * 设置新皮肤
     */
    fun applyNewSkin() {
        for (item in skinViewList) {
            if(item.listPropertyItem == null) continue
            for (itemProperty in item.listPropertyItem!!) {
                if (itemProperty.viewAttriName.contains("background")) {
                    if (itemProperty.attriType.contains("color")) {
                        item.view.setBackgroundColor(SkinManager.getInstance().getColor(itemProperty))
                    }
                } else if (itemProperty.viewAttriName.equals("textColor")) {
                    (item.view as TextView).setTextColor(SkinManager.getInstance().getColor(itemProperty))
                }
                //todo...完善规则
            }
        }
    }

    /**
     * 收集需要换肤的View和需要替换的属性
     */
    private fun parsetView(view: View?, context: Context?, attrs: AttributeSet?) {

        if (attrs == null || view == null) {
            return
        }

        var listPropertyItem: MutableList<PropertyItem> ?= null
        for (i in 0 until attrs?.attributeCount) {
            val name = attrs.getAttributeName(i)//得到属性名字 name
            val value = attrs.getAttributeValue(i)//得到@123412358978 也就是@id

            LogUtils.i(this,"$name : $value")
            if (isSkinView(name)) {
                if (listPropertyItem == null) {
                    listPropertyItem = mutableListOf()
                }
                if(!value.contains("@")) continue
                val id = Integer.parseInt(value.substring(1))
                val attriType = view.resources.getResourceTypeName(id)//根据id获取属性值的类型
                val attriName = view.resources.getResourceEntryName(id)//根据id获取属性值的名称
                R.color.colorPrimaryDark
                val propertyItem = PropertyItem(name, id, attriType, attriName)
                listPropertyItem.add(propertyItem)

                LogUtils.i(this,attriType)
                LogUtils.i(this,attriName)
            }
        }

        if (listPropertyItem != null) {
            skinViewList.add(SkinView(view, listPropertyItem))
        }

    }

    private fun isSkinView(attrName: String):Boolean {
        return attrName.contains("background")
                || attrName.contains("textColor")
                || attrName.contains("src")
                || attrName.contains("color")
    }

    override fun onCreateView(name: String?, context: Context?, attrs: AttributeSet?): View?{
        var view:View? = null
        try {
            val item  = context?.getClassLoader()?.loadClass(name);
            var construtor: Constructor<View> = item?.getConstructor(Context::class.java, AttributeSet::class.java) as Constructor<View>
            view = construtor?.newInstance(context, attrs)
        } catch (e: Exception) {
//            e.printStackTrace()
        }finally {
            return view
        }
    }

    class SkinView(var view: View, var listPropertyItem: List<PropertyItem>?){
    }

    class PropertyItem(var viewAttriName:String,var viewAtrriValueId:Int,var attriType:String,var attriName:String){

    }
}
