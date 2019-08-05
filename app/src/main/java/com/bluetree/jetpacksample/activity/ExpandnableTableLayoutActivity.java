package com.bluetree.jetpacksample.activity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bluetree.jetpacksample.R;

public class ExpandnableTableLayoutActivity extends AppCompatActivity {

    private RecyclerView rv_move;
    private TableMoveAdapter adapter;
    private RecyclerView lv_fixed;
    private TableFixedAdapter fixAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandnable_table_layout);

        String[][] arr = new String[26][8];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                arr[i][j] = "Bean" + i + "." + "item" + j;
            }
        }

        lv_fixed = findViewById(R.id.lv_fixed);
        lv_fixed.setLayoutManager(new LinearLayoutManager(this));
        fixAdapter = new TableFixedAdapter(this,arr);
        lv_fixed.setAdapter(fixAdapter);

        rv_move = findViewById(R.id.rv_move);
        rv_move.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        adapter = new TableMoveAdapter(this, arr,rv_move);
        rv_move.setAdapter(adapter);

        fixAdapter.setMoveAdapter(adapter);
    }

    private static class TableMoveAdapter extends A_RVAdapter{

        private static final int ITEM_WIDTH = 300;
        private static final int ITEM_HEIGHT = 100;
        private Context mContext;
        private String[][] arr;
        private RecyclerView rv;

        public TableMoveAdapter(Context mContext, String[][] arr) {
            this.mContext = mContext;
            this.arr = arr;
        }

        public TableMoveAdapter(Context mContext, String[][] arr,RecyclerView rv) {
            this.mContext = mContext;
            this.arr = arr;
            this.rv = rv;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            LinearLayout itemLinearLayout = new LinearLayout(mContext);
            itemLinearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            itemLinearLayout.setOrientation(LinearLayout.VERTICAL);

            //第一行
            LinearLayout itemLinearLayoutFirstLine = new LinearLayout(mContext);
            itemLinearLayoutFirstLine.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            itemLinearLayout.addView(itemLinearLayoutFirstLine);

            //第二行
            View viewSpaceSecond = new View(mContext);
            viewSpaceSecond.setId(200 * 1);
            viewSpaceSecond.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, measureTextViewHeight()));
            viewSpaceSecond.setVisibility(View.GONE);
            itemLinearLayout.addView(viewSpaceSecond);

            for (int j = 0; j < arr[0].length; j++) {
                TextView itemRowText = new TextView(mContext);
                itemRowText.setId(j+100);
                itemRowText.setGravity(Gravity.CENTER);
                itemRowText.setLayoutParams(new LinearLayout.LayoutParams(ITEM_WIDTH, ITEM_HEIGHT));
                itemLinearLayoutFirstLine.addView(itemRowText);
            }
            return new RecyclerView.ViewHolder(itemLinearLayout) {
                @Override
                public String toString() {
                    return super.toString();
                }
            };
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            String[] linearBean = arr[i];
            final ViewGroup parentView= (ViewGroup) ((ViewGroup) viewHolder.itemView).getChildAt(0);
            for (int j = 0; j < linearBean.length; j++) {
                TextView itemTextView = ((TextView) parentView.getChildAt(j));
                itemTextView.setText(linearBean[j]);
                /*itemTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogUtils.i(v.toString());
                    }
                });*/
            }
        }

        @Override
        public int getItemCount() {
            return arr.length;
        }


        public void showChildLine(int i) {
            ViewGroup viewGroup = (ViewGroup) rv.getChildAt(i);
            View viewSpace = viewGroup.findViewById(200 * 1);
            viewSpace.setVisibility(View.VISIBLE);
            smoothShowView(measureTextViewHeight(), viewSpace);
        }

        public void hideChildLine(int i) {
            ViewGroup viewGroup = (ViewGroup) rv.getChildAt(i);
            View viewSpace = viewGroup.findViewById(200 * 1);
            viewSpace.setVisibility(View.GONE);
        }
    }

    private static class TableFixedAdapter extends A_RVAdapter{

        private static final int ITEM_WIDTH = 300;
        private static final int ITEM_HEIGHT = 100;
        private Context mContext;
        private String[][] arr;
        private TableMoveAdapter moveAdapter;

        public TableFixedAdapter(Context mContext, String[][] arr) {
            this.mContext = mContext;
            this.arr = arr;
        }

        public void setMoveAdapter(TableMoveAdapter moveAdapter) {
            this.moveAdapter = moveAdapter;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LinearLayout itemLinearLayoutLine = new LinearLayout(mContext);
            itemLinearLayoutLine.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            itemLinearLayoutLine.setOrientation(LinearLayout.VERTICAL);
            TextView itemRowText = new TextView(mContext);
            itemRowText.setGravity(Gravity.CENTER);
            itemRowText.setLayoutParams(new LinearLayout.LayoutParams(ITEM_WIDTH, ITEM_HEIGHT));
            itemLinearLayoutLine.addView(itemRowText);


            TextView itemDescr = new TextView(mContext);
            itemDescr.setGravity(Gravity.CENTER);
            itemDescr.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, measureTextViewHeight()));
            itemDescr.setVisibility(View.GONE);
            itemLinearLayoutLine.addView(itemDescr);
            return new RecyclerView.ViewHolder(itemLinearLayoutLine) {
                @Override
                public String toString() {
                    return super.toString();
                }
            };
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
            String[] linearBean = arr[i];
            final ViewGroup parentView= ((ViewGroup) viewHolder.itemView);
            final TextView tv = ((TextView) parentView.getChildAt(0));
            final TextView tvDes = ((TextView) parentView.getChildAt(1));
            tv.setText(linearBean[0]);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tvDes.getVisibility() == View.GONE) {
                        showChildRow(tvDes, i);
                    }else {
                        hideChildRow(tvDes, i);
                    }
                    //showView(parentView, "fasdfasdfasdfas\nasdfasdfasdfccc");
                }
            });
        }


        @Override
        public int getItemCount() {
            return arr.length;
        }

        @Override
        void hideChildRow(View tvDes, int i) {
            tvDes.setVisibility(View.GONE);
            moveAdapter.hideChildLine(i);
        }

        @Override
        void showChildRow(View tvDes, int i) {
            int targetHeight = measureTextViewHeight();
            smoothShowView(targetHeight, tvDes);
            moveAdapter.showChildLine(i);
        }

    }


    abstract static class A_RVAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>{
        Context mContext;

        int measureTextViewHeight(){
            return 200;
        };

        void smoothShowView(int targetHeight, final View tvDes) {
            tvDes.getLayoutParams().height = 1;
            tvDes.setVisibility(View.VISIBLE);
            ValueAnimator animatorHeight = ValueAnimator.ofInt(1,targetHeight);
            animatorHeight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    tvDes.getLayoutParams().height = (int) animation.getAnimatedValue();
                    tvDes.setLayoutParams(tvDes.getLayoutParams());
                }
            });
            animatorHeight.setDuration(400);
            animatorHeight.start();
        }


        void hideChildRow(View tvDes, int i) {
        }

        void showChildRow(View tvDes, int i) {
        }
    }

}
