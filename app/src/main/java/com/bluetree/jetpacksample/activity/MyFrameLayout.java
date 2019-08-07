package com.bluetree.jetpacksample.activity;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.bluetree.jetpacksample.utils.LogUtils;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

public class MyFrameLayout extends FrameLayout {
    private float[] downPositionX = {0,0};

    /**
     * 当前事件序列是否为横向滑动
     */
    private boolean isHorizontal;

    public MyFrameLayout(@Nullable Context context) {
        super(context);
    }

    public MyFrameLayout(@Nullable Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFrameLayout(@Nullable Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        LogUtils.i("~\nMyLayout.dispatchTouchEvent(x,y) = (" + ev.getX() + "," + ev.getY() + ")");
        switch (ev.getAction()) {
            case ACTION_DOWN:
                LogUtils.i("down--->");
                downPositionX[0] = ev.getX();
                downPositionX[1] = ev.getY();
                getChildAt(0).dispatchTouchEvent(ev);
                isHorizontal = false;
                break;
            case ACTION_MOVE:
                LogUtils.i("-------move----");
                if (isHorizontalScroller(ev)) {
                    return getChildAt(0).dispatchTouchEvent(ev);
                }

                break;
            case ACTION_UP:
                LogUtils.i("<---up");
            default:
                if (isHorizontalScroller(ev)) {
                    return getChildAt(0).dispatchTouchEvent(ev);
                }
                break;

        }



        return super.dispatchTouchEvent(ev);
    }

    /**
     * 是否横向滑动
     * @param ev
     * @return
     */
    private boolean isHorizontalScroller(MotionEvent ev) {
        if(!isHorizontal) {
            isHorizontal = Math.abs(downPositionX[0] - ev.getX()) > 8 ;
        }
        return isHorizontal;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        LogUtils.i("MyLayout.onInterceptTouchEvent(x,y) = (" + ev.getX() + "," + ev.getY() + ")");
        switch (ev.getAction()) {
            case ACTION_DOWN:
                LogUtils.i("down--------------");
                break;
            case ACTION_MOVE:
                LogUtils.i("move------------------");
                if (Math.abs(ev.getY() - downPositionX[1]) > 10) {
                    isHorizontal = false;
                    //竖直方向移动返回
                    return true;
                }
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        LogUtils.i("MyLayout.onTouchEvent(x,y) = (" + e.getX() + "," + e.getY() + ")");
        return super.onTouchEvent(e);
    }

}
