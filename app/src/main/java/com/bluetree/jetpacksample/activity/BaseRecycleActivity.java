package com.bluetree.jetpacksample.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bluetree.jetpacksample.BaseActivity;
import com.bluetree.jetpacksample.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

abstract public class BaseRecycleActivity extends BaseActivity {
    List list;
    private RecyclerView rv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_recycle_view);
        rv = findViewById(R.id.rv);

        list = initList();
        SampleRecycleViewActivity.MyAdapter adapter = new SampleRecycleViewActivity.MyAdapter(list);

        TextView tv_empty = new TextView(this);
        tv_empty.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv_empty.setText("暂无消息");
        adapter.setEmptyView(tv_empty);

        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter.setOnItemClickListener(getItemClickListener());
    }

    abstract List initList();
    protected abstract BaseQuickAdapter.OnItemClickListener getItemClickListener();
}
