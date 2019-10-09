package com.bluetree.jetpacksample.coustom_view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * Created by Gavin
 * Date: 2019/8/28
 * Time: 16:41
 */
class PathView:View{
    private var isAvaliableDraw: Boolean = false
    private val radius: Float = 50f
    private lateinit var paint: Paint

    constructor(context: Context?) : super(context, null,0)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        paint = Paint()
        paint.color = Color.GREEN
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 10f
        isAvaliableDraw = true

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (!isAvaliableDraw) {
            return
        }
        canvas?.translate(width/2f,height/2f)
        canvas?.drawCircle(0f, 0f, radius, paint)
        val path = Path()
        path.moveTo(0f, 0f)
        path.lineTo(100f,100f)

        canvas?.let {
            it.drawPath(path,paint)
        }
    }
}