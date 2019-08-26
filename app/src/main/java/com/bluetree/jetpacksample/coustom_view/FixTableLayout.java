package com.bluetree.jetpacksample.coustom_view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.bluetree.jetpacksample.R;
import com.bluetree.jetpacksample.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

/**
 * 固定标题表格布局
 */
public class FixTableLayout extends FrameLayout {
    private float[] downPositionX = {0,0};

    /**
     * 当前事件序列是否为横向滑动
     */
    private boolean isHorizontal;

    private RecyclerView rvActive,rvFixed;
    private RecyclerView.Adapter activeAdapter,fixAdapter;
    private CombineAdapter commonAdapter;

    public FixTableLayout(@Nullable Context context) {
        super(context);
    }

    public FixTableLayout(@Nullable Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    public FixTableLayout(@Nullable Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
       // init();
    }

    private void init() {
        //活动布局
        HorizontalScrollView horizontalScrollerView = new MyHorizontalScrollView(getContext());
        horizontalScrollerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        horizontalScrollerView.setOverScrollMode(OVER_SCROLL_NEVER);
        horizontalScrollerView.setHorizontalScrollBarEnabled(false);

        rvActive = new FrameRecycleView2(getContext());
        rvActive.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        horizontalScrollerView.addView(rvActive);
        addView(horizontalScrollerView);

        //固定布局
        rvFixed = new RecyclerView(getContext());
        rvFixed.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(rvFixed);

        rvActive.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvFixed.setLayoutManager(new LinearLayoutManager(getContext()));

        setAdapter();
    }

    private void setAdapter() {
        fixAdapter = new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View viewRow = null;
                if (commonAdapter != null) {
                    viewRow = commonAdapter.getFixView(viewGroup);
                }else{
                    viewRow = LayoutInflater.from(getContext()).inflate(R.layout.item_fix_row, null);
                }
                return new RecyclerView.ViewHolder(viewRow) {
                    @Override
                    public String toString() {
                        return super.toString();
                    }
                };
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                if (commonAdapter != null) {
                    commonAdapter.onFixBindViewHolder(viewHolder, i);
                }
            }

            @Override
            public int getItemCount() {
                if (commonAdapter == null) {
                    return 0;
                }
                return commonAdapter.listData.size();
            }
        };
        rvFixed.setAdapter(fixAdapter);

        activeAdapter = new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View itemLinearLayout = LayoutInflater.from(getContext()).inflate(R.layout.item_scroll_row, null);
                if (commonAdapter != null) itemLinearLayout = commonAdapter.getMoveView(viewGroup);
                return new RecyclerView.ViewHolder(itemLinearLayout) {
                    @Override
                    public String toString() {
                        return super.toString();
                    }
                };
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                if (commonAdapter != null) {
                    commonAdapter.onActiveBindViewHolder(viewHolder, i);
                }
            }

            @Override
            public int getItemCount() {
                if (commonAdapter == null) {
                    return 0;
                }
                return commonAdapter.listData.size();
            }
        };
        rvActive.setAdapter(activeAdapter);

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
                getChildAt(0).dispatchTouchEvent(ev);
                break;
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

    public void noitfyDataSetChange() {
        fixAdapter.notifyDataSetChanged();
        activeAdapter.notifyDataSetChanged();
    }

    public abstract static class CombineAdapter<T,F extends RecyclerView.ViewHolder,A extends RecyclerView.ViewHolder>{

        public List<T> listData;

        public CombineAdapter(List<T> listData) {
            this.listData = listData;
        }

        public CombineAdapter() {
            listData = new ArrayList<>();
        }

        public abstract void onFixBindViewHolder(@NonNull F vh, int i);
        public abstract void onActiveBindViewHolder(@NonNull A vh, int i);
        public abstract View getFixView(ViewGroup viewGroup);
        public abstract View getMoveView(ViewGroup viewGroup);
        public abstract int getFixColumnWidth();

        void smoothShowView(int targetHeight, final View... tvDes) {
            if(tvDes == null||tvDes.length<1) return;
            int startHeight = 0;
            for (int i = 0; i < tvDes.length; i++) {
                tvDes[i].getLayoutParams().height = startHeight;
                tvDes[i].setVisibility(VISIBLE);
            }

            ValueAnimator animatorHeight = ValueAnimator.ofInt(startHeight,targetHeight);
            animatorHeight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    for (int i = 0; i < tvDes.length; i++) {
                        tvDes[i].getLayoutParams().height = (int) animation.getAnimatedValue();
                        tvDes[i].setLayoutParams(tvDes[i].getLayoutParams());
                    }
                }
            });
            animatorHeight.setDuration(400);
            animatorHeight.start();
        }
        public void showView(int targetHeight, final View... tvDes) {
            if(tvDes == null||tvDes.length<1) return;
            int startHeight = 0;
            for (int i = 0; i < tvDes.length; i++) {
                tvDes[i].getLayoutParams().height = startHeight;
                tvDes[i].setVisibility(VISIBLE);
            }
            for (int i = 0; i < tvDes.length; i++) {
                tvDes[i].getLayoutParams().height = targetHeight;
                tvDes[i].setLayoutParams(tvDes[i].getLayoutParams());
            }
        }
        /**
         * 动画隐藏view
         * @param tvDes
         */
        void smoothHideView(final View tvDes) {
            if(tvDes == null) return;
            int startHeight = 0;
            tvDes.getLayoutParams().height = startHeight;
            tvDes.setVisibility(View.VISIBLE);
            ValueAnimator animatorHeight = ValueAnimator.ofInt(startHeight,0);
            animatorHeight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    tvDes.getLayoutParams().height = (int) animation.getAnimatedValue();
                    tvDes.setLayoutParams(tvDes.getLayoutParams());
                }
            });
            animatorHeight.setDuration(200);
            animatorHeight.start();
        }

        /**
         * 测量textview的高度
         * @param tv
         * @param s
         * @return
         */
        public int measureTextviewHeight(TextView tv, String s) {
            tv.setText(s);
            int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            tv.measure(w, h);
            int height = tv.getMeasuredHeight();
            //int width = tv.getMeasuredWidth();
            return height;
        }


    }

    public void setAdapter(CombineAdapter commonAdapter) {
        init();
        this.commonAdapter = commonAdapter;
    }

    public RecyclerView getColunmRvFixed() {
        return rvFixed;
    }

    public RecyclerView getColumnRvActive() {
        return rvActive;
    }

}
