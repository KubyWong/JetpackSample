package com.bluetree.jetpacksample.coustom_view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import java.util.*

/**
 * Created by Gavin
 * Date: 2019/8/26
 * Time: 14:22
 */
class TreeView: View {


    private val paint: Paint = Paint()
    private var hasEnd: Boolean = false
    private var linkedListBranch: LinkedList<Branch> = LinkedList()
    private lateinit var snapShot: SnapShot

    constructor(context: Context?) :this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) :this(context, attrs,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){

        linkedListBranch = LinkedList<Branch>()


        //10*10二维数组
        linkedListBranch.add(getBranches())



    }

    private fun getBranches(): Branch {
        val bs = Array<IntArray>(3){ IntArray(10) }
        bs[0] = intArrayOf( 0, -1, 126, 243, 102, 152, 127, 41, 40, 100)
        bs[1] = intArrayOf( 1,  0, 104, 182, 160, 160, 187,111, 20, 100)
        bs[2] = intArrayOf( 2,  1, 169, 152, 230, 134, 230, 142, 10, 100)
//        bs[0] = intArrayOf( 1, -1, 126, 243, 102, 152, 127, 41, 40, 100)
//        bs[0] = intArrayOf( 1, -1, 126, 243, 102, 152, 127, 41, 40, 100)
//        bs[0] = intArrayOf( 1, -1, 126, 243, 102, 152, 127, 41, 40, 100)
//        bs[0] = intArrayOf( 1, -1, 126, 243, 102, 152, 127, 41, 40, 100)

        var temp = arrayOfNulls<Branch>(3)
        for (i in 0..bs.size-1) {
            temp[i] = Branch(bs[i])
            if (temp[i]!!.parentId > -1) {
                temp[temp[i]!!.parentId]?.addChild(temp[i])
            }
        }
        return temp[0]!!
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        snapShot = SnapShot(Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888))

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        Log.i("this","onDraw")
        drawBranches()
        canvas?.drawBitmap(snapShot.bitmap,0f,0f,paint)

        if (!hasEnd) {
            invalidate()
        }

    }

    private fun drawBranches() {
        if (linkedListBranch != null) {


            var nextRankBranch: LinkedList<Branch>? = null

            var it = linkedListBranch.iterator()

            snapShot.canvas.save()

            while (it.hasNext()) {
                var itemBranch = it.next()
                if (!itemBranch.drawGrowingBranch(snapShot.canvas,paint,1)) {
                    it.remove()
                    if (itemBranch.childBranch.size > 0) {
                        if (nextRankBranch == null) {
                            nextRankBranch = itemBranch.childBranch
                        } else {
                            nextRankBranch.addAll(itemBranch.childBranch)
                        }
                    }
                }
            }

            snapShot.canvas.restore()

            if (nextRankBranch == null || nextRankBranch.size == 0) {
                hasEnd = true
            } else {
                linkedListBranch.addAll(nextRankBranch)
            }

        }
    }

    /**
     * 整棵树是由很多点绘制而成，每次绘制需要保存上一次的记录，所以不能放到TreeView的onDraw中
     * 图像绘制类，保存在bitmap中
     */
    class SnapShot {

        constructor(createBitmap: Bitmap?){
            this.bitmap = createBitmap
            canvas = Canvas()
            canvas.drawBitmap(createBitmap,0f,0f,paint)
        }

        var bitmap: Bitmap?
        private val paint: Paint = Paint()
        var canvas: Canvas
    }

    /**
     * 树干类
     */
    class Branch() {
        private var currentX: Float = 0.0f
        private var currentY: Float = 0.0f
        private var currentLenght: Int = 0
        private val paint: Paint = Paint()
        public lateinit var childBranch: LinkedList<Branch>
        private var maxLength: Int = 0
        private var radius: Float = 0f
        var parentId: Int = 0
        private var id: Int = 0
        private lateinit var endPoint: Point
        private lateinit var controlPoint: Point
        private lateinit var startPoint: Point

        constructor(data: IntArray) : this(){
            id = data[0]
            parentId = data[1]
            startPoint = Point(data[2], data[3])
            controlPoint = Point(data[4], data[5])
            endPoint = Point(data[6], data[7])
            radius = data[8]*1.0f
            maxLength = data[9]


            paint.color = Color.RED
            childBranch = LinkedList<Branch>()
        }

        fun addChild(branch: Branch?) {
            if (childBranch == null) {
                childBranch = LinkedList<Branch>()
            }
            branch?.let { childBranch.add(it) }
        }

        fun drawGrowingBranch(canvas: Canvas, paint: Paint, i: Int): Boolean {

            if (currentLenght < maxLength) {
                bezier()
                draw(canvas,paint,i*1f)
                radius *= 0.97f
                currentLenght++

                return true
            }else{
                return false
            }

        }

        fun draw(canvas: Canvas, paint: Paint, i: Float) {
            paint.color = Color.RED
            canvas.save()
            canvas.scale(i,i)
            canvas.translate(currentX,currentY)
            canvas.drawCircle(0f,0f,radius,paint)
            canvas.restore()
        }

        /**
         * 贝塞尔算法计算新的坐标
         */
        private fun bezier() {
            var t = currentLenght / maxLength*1f
            var c0 = (1 - t) * (1 - t)
            var c1 = 2 * (1 - t)
            var c2 = t * t
            currentX = c0 * startPoint.x + c1 * controlPoint.x + c2 * endPoint.x
            currentY = c0 * startPoint.y + c1 * controlPoint.y + c2 * endPoint.y
        }
    }

}