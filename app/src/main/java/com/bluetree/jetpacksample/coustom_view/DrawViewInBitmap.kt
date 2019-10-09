package com.bluetree.jetpacksample.coustom_view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.annotation.Nullable
import android.util.AttributeSet
import android.view.View

/**
 * Created by Gavin
 * Date: 2019/8/29
 * Time: 16:28
 * 绘制一个点
 */
class DrawViewInBitmap : View {
    private var paint: Paint = Paint()
    private lateinit var blankBoard: BlankBoard

    constructor(context: Context) : super(context, null, 0)
    constructor(context: Context, @Nullable attrs: AttributeSet) :this(context,attrs,0)
    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        blankBoard = BlankBoard(Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888))
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //画笔设置成绿色
        paint.color = Color.GREEN
        //1、画一个圆到bitmap
        drawCircle2BlankBoardBitmap(200f, 200f, 50f,blankBoard.blankBoardCanvas, paint)
        //2、在自定义view的canvas中绘制blankBoard.bitmap
        canvas?.drawBitmap(blankBoard.blankBoardBitmap,0f,0f,paint)
        /*这里要注意点是：同一个Bitmap可以绘制在不同的Canvas上，
        一开始Bitmap在blankBoard.blankBoardCanvas上绘制，虽然是绘制成功了但是我们要展示给用户看，
        必须要画在View.onDraw方法中得到的canvas上。
        blankBoard.blankBoardBitmap在Canvas*/
    }

    /**
     * 绘制一个圆到白板上
     * （圆点x坐标，圆点y坐标，圆半径，画笔）
     */
    private fun drawCircle2BlankBoardBitmap(dx: Float, dy: Float
                                            , radius: Float, canvas: Canvas, mPaint: Paint){
        canvas.save()//保存之前的canvas坐标状态
        canvas.translate(dx,dy)
        canvas.drawCircle(0f,0f,radius,mPaint)
        canvas.restore()//恢复之前的canvas坐标状态
    }

    /**
     * 保存所绘制的bitmap
     */
    class BlankBoard {
        var blankBoardCanvas: Canvas
        var blankBoardBitmap: Bitmap

        constructor(bitmap: Bitmap){
            this.blankBoardBitmap = bitmap
            this.blankBoardCanvas = Canvas(this.blankBoardBitmap)
        }
    }
}