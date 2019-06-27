package com.bluetree.jetpacksample.activity;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bluetree.jetpacksample.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class SampleRecycleViewActivity extends AppCompatActivity {

    RecyclerView rv ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_recycle_view);
        rv = findViewById(R.id.rv);

        final List<MyDataModel> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            data.add(new MyDataModel("", "text"+i));
        }
        data.add(new MyDataModel("", "清空数据"));
        MyAdapter adapter = new MyAdapter(data);

        TextView tv_empty = new TextView(this);
        tv_empty.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv_empty.setText("暂无消息");
        adapter.setEmptyView(tv_empty);

        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(SampleRecycleViewActivity.this, "click "+data.get(position).text, Toast.LENGTH_SHORT).show();
                if (position == 10) {
                    data.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    class MyDataModel{
        String imgUrl;
        String text;

        public MyDataModel(String imgUrl, String text) {
            this.imgUrl = imgUrl;
            this.text = text;
        }
    }

    class MyAdapter extends BaseQuickAdapter<MyDataModel,BaseViewHolder> {
        public MyAdapter(@Nullable List<MyDataModel> data) {
            super(android.R.layout.activity_list_item,data);
        }

        @Override
        protected void convert(BaseViewHolder helper, MyDataModel item) {
            ((TextView)helper.getView(android.R.id.text1)).setText(item.text);
        }
    }
}
