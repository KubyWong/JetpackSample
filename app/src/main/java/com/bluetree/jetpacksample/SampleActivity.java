package com.bluetree.jetpacksample;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bluetree.jetpacksample.activity.ExpandnableTableLayoutActivity2;
import com.bluetree.jetpacksample.activity.SampleAnnotationActivity;
import com.bluetree.jetpacksample.activity.SampleBindDataActivity;
import com.bluetree.jetpacksample.activity.SampleCustomViewActivity;
import com.bluetree.jetpacksample.activity.SampleFingerPrinterActivity;
import com.bluetree.jetpacksample.activity.SampleMyOkHttpActivity;
import com.bluetree.jetpacksample.activity.SampleNavigationActivity;
import com.bluetree.jetpacksample.activity.SamplePagingActivity;
import com.bluetree.jetpacksample.activity.SampleRecycleViewActivity;
import com.bluetree.jetpacksample.activity.SampleReqNetWorkActivity;
import com.bluetree.jetpacksample.activity.SampleRxjavaActivity;
import com.bluetree.jetpacksample.activity.SampleViewModelActivity;
import com.bluetree.jetpacksample.activity.TreeViewActivity;
import com.bluetree.jetpacksample.utils.LogUtils;
import com.bluetree.jetpacksample.utils.ToastUtils;
import com.bluetree.myskinlib.SampleChangeSkin1Activity;

import java.util.ArrayList;
import java.util.List;

public class SampleActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final List<String> list = new ArrayList<>();
        setContentView(R.layout.activity_sample);
        list.add("retrofit(基于okhttp封装) + rxjava");//0
        list.add("使用LifeCycle去监听有生命的组件，如Activity和Fragment");//1
        list.add("使用LiveData+ViewModel模拟网络请求加载数据");//2
        list.add("使用paging来进行分页加载");//3
        list.add("databinding数据绑定");//4
        list.add("navigation导航");//5
        list.add("一个开源易用的RecycleviewAdapter");//6
        list.add("rxjava");//7
        list.add("封装双Recycleview实现的表格布局");//8
        list.add("指纹验证");//9
        list.add("高级自定义控件TreeView，曲线");//10
        list.add("自定义ButterKnife");//11
        list.add("自己封装网络请求框架");//12
        list.add("动态下载皮肤插件，实现换肤功能");//13

        RecyclerView rv = findViewById(R.id.rv);
        rv.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        RecyclerView.Adapter adapter = new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(
                        LayoutInflater.from(SampleActivity.this).inflate(android.R.layout.simple_list_item_1, null)
                ) {

                };
                return viewHolder;
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
                TextView tv = viewHolder.itemView.findViewById(android.R.id.text1);
                tv.setText(list.get(i));
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (i) {//retrofit+rxjava
                            case 0:
                                startActivity(new Intent(SampleActivity.this, SampleReqNetWorkActivity.class));
                                break;

                            case 1://Lifecycle
                                initLifecycleObeserver();
                                break;

                            case 2://LiveData+ViewModel
//                              参考LiveData  https://juejin.im/entry/5987022df265da3e161a86f3
                                startActivity(new Intent(SampleActivity.this, SampleViewModelActivity.class));



                                //1、activity显示UI接受互动 2、ViewModel储存UI数据 3、Repository储存与加载数据的API，可以通过Presenter类
                                //注意：1、千万不要把Context传入ViewModel中，因为ViewModel的生命周期比他们更长，如果需要引用Application的ViewModel，可以了解一下AndroidViewModel
                                //https://www.jianshu.com/p/b8a29407b6e1
                                break;
                            case 3:
                                startActivity(new Intent(SampleActivity.this, SamplePagingActivity.class));

                                break;
                            case 4:
                                startActivity(new Intent(SampleActivity.this, SampleBindDataActivity.class));
                                break;
                            case 5:
                                startActivity(new Intent(SampleActivity.this, SampleNavigationActivity.class));
//                                startActivity(new Intent(SampleActivity.this,SampleNavigationActivity2.class));
                                break;
                            case 6://BaseRecyclerViewAdapterHelper
                                //参考：https://www.jianshu.com/p/b343fcff51b0
                                //由于adapter本身能力有限，我们又不想耦合view层所以有些需求是现实不了，于是合作了一些优秀开源库，为开发者提供更多可能性。以下扩展框架都是有结合BRVAH的demo。
                                //PinnedSectionItemDecoration：一个强大的粘性标签库
                                //EasyRefreshLayout：这个库让你轻松实现下拉刷新和上拉更多
                                //EasySwipeMenuLayout：独立的侧滑删除
                                startActivity(new Intent(SampleActivity.this, SampleRecycleViewActivity.class));
                                break;
                            case 7:
                                jumpActivity(SampleRxjavaActivity.class);
                                break;
                            case 8:
                                jumpActivity(ExpandnableTableLayoutActivity2.class);
                                break;
                            case 9:
                                jumpActivity(SampleFingerPrinterActivity.class);
                                break;
                            case 10:
                                jumpActivity(SampleCustomViewActivity.class);
                                break;
                            case 11:
                                jumpActivity(SampleAnnotationActivity.class);
                                break;
                            case 12:
                                jumpActivity(SampleMyOkHttpActivity.class);
                                break;
                            case 13:
                                jumpActivity(SampleChangeSkin1Activity.class);
                                break;
                        }
                    }

                });
            }

            @Override
            public int getItemCount() {
                return list.size();
            }
        };
        rv.setAdapter(adapter);
    }




    /**
     * Android Achitechture Componant
     * 组件化开发
     */
    private void initLifecycleObeserver() {
        LifecycleObserver observe = new MyLifeObserver();
        getLifecycle().addObserver(observe);
    }

    class MyLifeObserver implements LifecycleObserver {

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        public void onViewShowed() {
            String msg= "onresume 感知到页面在显示";
            LogUtils.i(this, msg);
            ToastUtils.show(SampleActivity.this,msg);
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        public void onViewHinded() {
            String msg = "onpause 感知到页面为pause";
            LogUtils.i(this, msg);
            ToastUtils.show(SampleActivity.this,msg);
        }
    }
}
