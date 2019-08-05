package com.bluetree.jetpacksample.activity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

import com.bluetree.jetpacksample.utils.LogUtils;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

public class MyHorizontalScrollView extends HorizontalScrollView {
    public MyHorizontalScrollView(Context context) {
        super(context);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        LogUtils.i("MyHorizontalScrollView.dispatchTouchEvent(x,y) = (" + e.getX() + "," + e.getY() + ")");
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
        LogUtils.i("MyHorizontalScrollView.onInterceptTouchEvent(x,y) = (" + e.getX() + "," + e.getY() + ")");
//        return true;
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        LogUtils.i("MyHorizontalScrollView.onTouchEvent(x,y) = (" + e.getX() + "," + e.getY() + ")");
        return super.onTouchEvent(e);
    }
}
