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

import java.util.ArrayList;
import java.util.List;

public class ExpandnableTableLayoutActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandnable_table_layout2);

        List<List<String>> list = new ArrayList<>();
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
            void onFixBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
                List<String> itemBean = listData.get(i);
                final ViewGroup parentView = ((ViewGroup) viewHolder.itemView);
                final TextView tvTitle = parentView.findViewById(R.id.tv_title);
                final TextView tvCode = parentView.findViewById(R.id.tv_code);
                final TextView tvDes = parentView.findViewById(R.id.tv_des);
                tvTitle.setText(itemBean.get(0));
                tvTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tvDes.getVisibility() == View.GONE || tvDes.getHeight() < 2) {
                            int targetHeight = 300;
                            smoothShowView(targetHeight, tvDes);
                            smoothShowView(targetHeight,fix_tablelayout.getColumnRvActive().getChildAt(i).findViewById(R.id.v_space));

                        } else {
                            tvDes.setVisibility(View.GONE);
                            fix_tablelayout.getColumnRvActive().getChildAt(i).findViewById(R.id.v_space).setVisibility(View.GONE);
//                            moveAdapter.hideChildLine(i);
                        }
                    }
                });
                tvCode.setText(itemBean.get(0));
                tvDes.setText(itemBean.get(1));
            }

            @Override
            void onActiveBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                List<String> itemBean = listData.get(i);
                View parentView = viewHolder.itemView;
                TextView tv_current_price = parentView.findViewById(R.id.tv_current_price);
                TextView tv_chg = parentView.findViewById(R.id.tv_chg);
                TextView tv_short_des = parentView.findViewById(R.id.tv_short_des);

                tv_current_price.setText(itemBean.get(0));
                tv_chg.setText(itemBean.get(1));
                tv_short_des.setText(itemBean.get(2));
            }

            @Override
            View getFixView(ViewGroup viewGroup) {
                return LayoutInflater.from(ExpandnableTableLayoutActivity2.this).inflate(R.layout.item_fix_row, viewGroup,false);
            }

            @Override
            View getMoveView(ViewGroup viewGroup) {
                return LayoutInflater.from(ExpandnableTableLayoutActivity2.this).inflate(R.layout.item_scroll_row, viewGroup,false);
            }

            @Override
            int getFixColumnWidth() {
                return 0;
            }
        });
    }
}
