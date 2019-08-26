package com.bluetree.jetpacksample.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bluetree.jetpacksample.R;
import com.bluetree.jetpacksample.coustom_view.FixTableLayout;
import com.bluetree.jetpacksample.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class ExpandnableTableLayoutActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandnable_table_layout2);

        final List<List<String>> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ArrayList<String> itemList = new ArrayList<String>();
            for (int j = 0; j < 5; j++) {
                itemList.add("item" + j);
            }
            list.add(itemList);
        }

        final FixTableLayout fix_tablelayout = findViewById(R.id.fix_tablelayout);
        fix_tablelayout.setAdapter(new FixTableLayout.CombineAdapter<List<String>,RecyclerView.ViewHolder, RecyclerView.ViewHolder>(list) {
            @Override
            public void onFixBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
                List<String> itemBean = listData.get(i);
                final ViewGroup parentView = ((ViewGroup) viewHolder.itemView);
                final TextView tvTitle = parentView.findViewById(R.id.tv_title);
                final TextView tvCode = parentView.findViewById(R.id.tv_code);
                final TextView tvDes = parentView.findViewById(R.id.tv_des);
                tvTitle.setText(itemBean.get(0));

                if (itemBean.get(3).equals("1")) {
                    tvDes.setVisibility(View.VISIBLE);
                }else{
                    tvDes.setVisibility(View.GONE);
                }

                tvTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tvDes.getVisibility() == View.GONE || tvDes.getHeight() < 2) {
                            int targetHeight = measureTextviewHeight(tvDes, "cvzxcvzxcv\nzdxfasdfasdf\nasdfasdfasdf");
                            LogUtils.e(targetHeight+"");
                            showView(targetHeight, tvDes,fix_tablelayout.getColumnRvActive().getChildAt(i).findViewById(R.id.v_space));
                            fix_tablelayout.noitfyDataSetChange();
                        } else {
                            tvDes.setVisibility(View.GONE);
                            fix_tablelayout.getColumnRvActive().getChildAt(i).findViewById(R.id.v_space).setVisibility(View.GONE);
//                            moveAdapter.hideChildLine(i);
                            fix_tablelayout.noitfyDataSetChange();
                        }
                    }
                });
                tvCode.setText(itemBean.get(0));
                tvDes.setText(itemBean.get(1));
            }

            public void onActiveBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
                final List<String> itemBean = listData.get(i);
                View parentView = viewHolder.itemView;
                TextView tv_current_price = parentView.findViewById(R.id.tv_current_price);
                TextView tv_chg = parentView.findViewById(R.id.tv_chg);
                final TextView tv_short_des = parentView.findViewById(R.id.tv_short_des);
                final View v_space = parentView.findViewById(R.id.v_space);

                tv_current_price.setText(itemBean.get(0));
                tv_chg.setText(itemBean.get(1));
                tv_short_des.setText(itemBean.get(2));

                if (itemBean.get(3).equals("1")) {
                    v_space.setVisibility(View.VISIBLE);
                }else{
                    v_space.setVisibility(View.GONE);
                }

                /*tv_short_des.setOnTouchListener(new View.OnTouchListener() {

                    private float y;
                    private float x;

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int actionId = event.getAction();
                        if (actionId == MotionEvent.ACTION_DOWN) {
                            x = event.getX();
                            y = event.getY();
                        } else if (actionId == MotionEvent.ACTION_UP) {
                            if (Math.abs(x - event.getX()) < 8 && Math.abs(y - event.getY()) < 8) {
                                LogUtils.e(this,"click is avaliable");
                                if (itemBean.get(3).equals("1")) {//当前为显示
                                    itemBean.set(3, "0");
                                    fix_tablelayout.noitfyDataSetChange();
                                }else{//当前为隐藏
                                    for (int j = 0; j < listData.size(); j++) {
                                        listData.get(j).set(3, "0");
                                    }
                                    listData.get(i).set(3, "1");
                                    fix_tablelayout.noitfyDataSetChange();
                                }
                            }
                        }
                        LogUtils.e(this, "onTouch is avaliable");
                        return true;
                    }
                });*/
                tv_short_des.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        if (Math.abs(x - event.getX()) < 8 && Math.abs(y - event.getY()) < 8) {
                            LogUtils.e(this,"click is avaliable");
                            if (itemBean.get(3).equals("1")) {//当前为显示
                                itemBean.set(3, "0");
                                fix_tablelayout.noitfyDataSetChange();
                            }else{//当前为隐藏
                                for (int j = 0; j < listData.size(); j++) {
                                    listData.get(j).set(3, "0");
                                }
                                listData.get(i).set(3, "1");
                                fix_tablelayout.noitfyDataSetChange();


                                String s = "cvzxcvzxcv\nzdxfasdfasdf\nasdfasdfasdf";
                                TextView tv = (TextView) fix_tablelayout.getColunmRvFixed().getChildAt(i).findViewById(R.id.tv_des);

                                int targetHeight = measureTextviewHeight(tv, s);
                                tv.getLayoutParams().height = targetHeight;

                                LogUtils.i(targetHeight+" ----------------------------------- ====================== ");

                            }
                        }
//                    }
                });
            }

            @Override
            public View getFixView(ViewGroup viewGroup) {
                return LayoutInflater.from(ExpandnableTableLayoutActivity2.this).inflate(R.layout.item_fix_row, viewGroup,false);
            }

            @Override
            public View getMoveView(ViewGroup viewGroup) {
                return LayoutInflater.from(ExpandnableTableLayoutActivity2.this).inflate(R.layout.item_scroll_row, viewGroup,false);
            }

            public int getFixColumnWidth() {
                return 0;
            }
        });
    }
}
