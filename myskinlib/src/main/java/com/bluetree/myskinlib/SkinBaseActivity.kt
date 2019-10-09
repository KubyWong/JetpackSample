package com.bluetree.myskinlib

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.view.LayoutInflaterCompat
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.bluetree.utilspro.LogUtils

/**
 * 换肤基类
 */
open class SkinBaseActivity : Activity() {
    private lateinit var factory2: SkinFactory2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //监听布局实例化
        factory2 = SkinFactory2()
        LayoutInflaterCompat.setFactory2(layoutInflater, factory2)

    }

    fun applySkin() {
        factory2?.applyNewSkin()
    }
}