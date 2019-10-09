package com.bluetree.jetpacksample.coustom_view

import android.content.Context
import android.graphics.*
import android.support.annotation.Nullable
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 * Created by Gavin
 * Date: 2019/8/28
 * Time: 10:15
 */
class AnimateByDrawView :View{

    private var isEnd: Boolean = false
    private lateinit var paint: Paint
    private lateinit var views: ArrayList<AnimationView>
    private lateinit var blankBoard: BlankBoard

    constructor(context: Context) : super(context, null, 0)
    constructor(context: Context, @Nullable attrs: AttributeSet) :this(context,attrs,0)
    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr:Int) :super(context,attrs,defStyleAttr){

        views = ArrayList()
        views.add(AnimationView(500f,200f))
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        blankBoard = BlankBoard(Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888))
        paint = Paint()
        paint.color = Color.RED
        paint.strokeWidth = 4f

    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        Log.i(this.javaClass.simpleName,"Draw ")
        canvas?.drawBitmap(blankBoard.blankBoardBitmap,0f,0f,paint)


//            for (view in views) {
//                if (views.get(views.lastIndex).startDraw(snapShot.blankBoardCanvas, paint)) {
//                    views.removeAt(views.lastIndex)
//                }
//            }
        var iter = views.iterator()
        while (iter.hasNext()) {
            if(iter.next().startDraw(blankBoard.blankBoardCanvas,paint)){
                val path = Path()
                blankBoard.blankBoardCanvas.save()
                path.moveTo(100f,300f)
                path.quadTo(300f, 100f,500f,500f)
                paint.style = Paint.Style.STROKE
                canvas?.drawPath(path, paint)
                blankBoard.blankBoardCanvas.restore()
                paint.style = Paint.Style.FILL_AND_STROKE
                iter.remove()
            }
        }

        if (views.size < 1) {
            isEnd = true
        }

        if (!isEnd) {
            invalidate()
        }
    }

    class BlankBoard {
         var blankBoardCanvas: Canvas
         var blankBoardBitmap: Bitmap

        constructor(bitmap: Bitmap){
            this.blankBoardBitmap = bitmap
            this.blankBoardCanvas = Canvas(this.blankBoardBitmap)
        }
    }

    class AnimationView{
        private val maxLength: Float
        private var currentX: Float = 0.0f
        private var currentY: Float = 0.0f

        private var radius: Float = 20.0f

        private val time = 100


        constructor(fl: Float, fl1: Float){
            currentX = fl
            currentY = fl1
            maxLength = fl1 + 500

        }

        /**
         * return 是否绘制返程
         */
        fun startDraw(canvas: Canvas, paint: Paint):Boolean {
            Log.i(this.javaClass.simpleName,"startDraw $currentY")
            canvas.save()
            canvas.translate(currentX, currentY)
            canvas.drawCircle(0f,0f,radius,paint)
            canvas.restore()

            currentY += maxLength * 1f / time

            return currentY>maxLength
        }

    }

}