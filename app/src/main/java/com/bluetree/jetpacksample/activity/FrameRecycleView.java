package com.bluetree.jetpacksample.activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.bluetree.jetpacksample.utils.LogUtils;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

/**
 * 取消拦截触摸事件的Recycleview
 */
public class FrameRecycleView extends RecyclerView {

    public FrameRecycleView(@NonNull Context context) {
        super(context);
    }

    public FrameRecycleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FrameRecycleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        LogUtils.i("FrameRecycleView.dispatchTouchEvent(x,y) = (" + e.getX() + "," + e.getY() + ")");
        switch (e.getAction()) {
            case ACTION_DOWN:
                LogUtils.i("down--->");
                break;
            case ACTION_MOVE:
                LogUtils.i("move------------------");
                break;
            case ACTION_UP:
                LogUtils.i("<---up");
                break;
        }
        /*if (e.getX() < 300) {
            if (e.getAction() == ACTION_DOWN) {
                return super.dispatchTouchEvent(e);
            } else if (e.getAction() == MotionEvent.ACTION_UP) {
                return super.dispatchTouchEvent(e);
            } else {
            }
        }*/
        return super.dispatchTouchEvent(e);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        LogUtils.i("FrameRecycleView.onInterceptTouchEvent(x,y) = (" + e.getX() + "," + e.getY() + ")");
        if (e.getAction() == ACTION_MOVE) {
            return false;
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        LogUtils.i("FrameRecycleView.onTouchEvent(x,y) = (" + e.getX() + "," + e.getY() + ")");
       /* if (e.getAction() == ACTION_MOVE) {
            return false;
        }*/
        return super.onTouchEvent(e);
    }
}
