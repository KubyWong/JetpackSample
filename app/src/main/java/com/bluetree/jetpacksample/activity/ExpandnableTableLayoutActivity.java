package com.bluetree.jetpacksample.activity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bluetree.jetpacksample.R;

public class ExpandnableTableLayoutActivity extends AppCompatActivity {

    private RecyclerView rv_move;
    private TableMoveAdapter adapter;
    private RecyclerView lv_fixed;
    private TableFixedAdapter fixAdapter;

    enum CULOMN_TYPE {
        FIX, ACTIVE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandnable_table_layout);


        CombineAdapter adapterCombain = new CombineAdapter() {

            @Override
            public void onFixBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int i) {

            }

            @Override
            public void onActiveBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int i) {

            }

            @Override
            public View getFixView() {
                return null;
            }

            @Override
            public View getMoveView() {
                return null;
            }
        };



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

        fixAdapter.linkAdapter(adapter);
    }

    /**
     * 第二个adapter
     */
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
            View itemLinearLayout = LayoutInflater.from(mContext).inflate(R.layout.item_scroll_row, null);
            if(commonAdapter!=null) itemLinearLayout = commonAdapter.getMoveView();
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
            }else{
                bind(viewHolder, arr[i]);
            }
        }

        private void bind(@NonNull RecyclerView.ViewHolder viewHolder, String[] linearBean1) {
            String[] linearBean = linearBean1;
            View parentView = viewHolder.itemView;
            TextView tv_current_price = parentView.findViewById(R.id.tv_current_price);
            TextView tv_chg = parentView.findViewById(R.id.tv_chg);
            TextView tv_short_des = parentView.findViewById(R.id.tv_short_des);

            tv_current_price.setText(linearBean[1]);
            tv_chg.setText(linearBean[2]);
            tv_short_des.setText(linearBean[3]);
        }

        @Override
        public int getItemCount() {
            return arr.length;
        }


        public void showChildLine(int i) {
            ViewGroup viewGroup = (ViewGroup) rv.getChildAt(i);
            View viewSpace = viewGroup.findViewById(R.id.v_space);
            viewSpace.setVisibility(View.VISIBLE);
            smoothShowView(measureTextViewHeight(), viewSpace);
        }

        public void hideChildLine(int i) {
            ViewGroup viewGroup = (ViewGroup) rv.getChildAt(i);
            View viewSpace = viewGroup.findViewById(R.id.v_space);
            viewSpace.setVisibility(View.GONE);
        }
    }

    private static class TableFixedAdapter extends A_RVAdapter{

        private String[][] arr;
        private TableMoveAdapter moveAdapter;

        public TableFixedAdapter(Context mContext, String[][] arr) {
            this.mContext = mContext;
            this.arr = arr;
        }

        public void linkAdapter(TableMoveAdapter moveAdapter) {
            this.moveAdapter = moveAdapter;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View viewRow = LayoutInflater.from(mContext).inflate(R.layout.item_fix_row, null);
            if (commonAdapter != null) {
                viewRow = commonAdapter.getFixView();
            }

            return new RecyclerView.ViewHolder(viewRow) {
                @Override
                public String toString() {
                    return super.toString();
                }
            };
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
            if (commonAdapter != null) {
                commonAdapter.onFixBindViewHolder(viewHolder,i);
            }else{
                onViewHolder(viewHolder, i);
            }
        }

        private void onViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
            String[] linearBean = arr[i];
            final ViewGroup parentView= ((ViewGroup) viewHolder.itemView);
            final TextView tvTitle = parentView.findViewById(R.id.tv_title);
            final TextView tvCode = parentView.findViewById(R.id.tv_code);
            final TextView tvDes = ((TextView) parentView.getChildAt(1));
            tvTitle.setText(linearBean[0]);
            tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tvDes.getVisibility() == View.GONE) {
                        int targetHeight = measureTextViewHeight(i);
                        smoothShowView(targetHeight, tvDes);
                        moveAdapter.showChildLine(i);
                    }else {
                        tvDes.setVisibility(View.GONE);
                        moveAdapter.hideChildLine(i);
                    }
                }
            });
            tvCode.setText(linearBean[0]);
            tvDes.setText(linearBean[0]);
        }


        @Override
        public int getItemCount() {
            return arr.length;
        }

    }

    /**
     * 适配器
     * @param <VH>
     */
    abstract static class A_RVAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>{
        Context mContext;
        CombineAdapter commonAdapter;


        public void setCommonAdapter(CombineAdapter commonAdapter) {
            this.commonAdapter = commonAdapter;
        }

        /**
         * 每行高度
         */
        int rowHeight = 100;

        int measureTextViewHeight(int position){
            return 200;
        }

        int measureTextViewHeight(){
            return 200;
        }

        void smoothShowView(int targetHeight, final View tvDes) {
            int startHeight = 0;
            tvDes.getLayoutParams().height = startHeight;
            tvDes.setVisibility(View.VISIBLE);
            ValueAnimator animatorHeight = ValueAnimator.ofInt(startHeight,targetHeight);
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

        /**
         * 动画隐藏view
         * @param tvDes
         */
        void smoothHideView(final View tvDes) {
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


    }

    abstract class CombineAdapter<F extends RecyclerView.ViewHolder,A extends RecyclerView.ViewHolder>{
        abstract void onFixBindViewHolder(@NonNull F viewHolder, int i);
        abstract void onActiveBindViewHolder(@NonNull A viewHolder, int i);
        abstract View getFixView();
        abstract View getMoveView();
    }

}
