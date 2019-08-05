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

public class MyFrameLayout2 extends FrameLayout {
    private float[] downPositionX = {0,0};

    public MyFrameLayout2(@Nullable Context context) {
        super(context);
    }

    public MyFrameLayout2(@Nullable Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFrameLayout2(@Nullable Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        LogUtils.i("~\nMyLayout2.dispatchTouchEvent(x,y) = (" + ev.getX() + "," + ev.getY() + ")");
        switch (ev.getAction()) {
            case ACTION_DOWN:
                LogUtils.e("down--->");
                downPositionX[0] = ev.getX();
                downPositionX[1] = ev.getY();

        }
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        LogUtils.i("MyLayout2.onInterceptTouchEvent(x,y) = (" + ev.getX() + "," + ev.getY() + ")");
        if (ev.getAction() == ACTION_MOVE) {
            LogUtils.e("move------------------");
                return true;
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        LogUtils.i("MyLayout2.onTouchEvent(x,y) = (" + e.getX() + "," + e.getY() + ")");

        return super.onTouchEvent(e);
    }
}
