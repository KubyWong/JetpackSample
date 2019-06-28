package com.bluetree.jetpacksample;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bluetree.jetpacksample.R;
import com.bluetree.jetpacksample.activity.SampleBindDataActivity;
import com.bluetree.jetpacksample.activity.SampleNavigationActivity;
import com.bluetree.jetpacksample.activity.SamplePagingActivity;
import com.bluetree.jetpacksample.activity.SampleRecycleViewActivity;
import com.bluetree.jetpacksample.activity.SampleReqNetWorkActivity;
import com.bluetree.jetpacksample.activity.SampleViewModelActivity;
import com.bluetree.jetpacksample.utils.LogUtils;
import com.bluetree.jetpacksample.utils.http.BaseHttpRetrofitCallback;
import com.bluetree.jetpacksample.utils.http.BaseHttpRxjavaCallback;
import com.bluetree.jetpacksample.utils.http.HttpUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.adapter.rxjava2.Result;

public class SampleActivity extends BaseActivity {

    /**
     * 使用静态内部类的Handler可以防止内存泄漏
     */
    private MyHandle handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler = new MyHandle(this);
        initLifecycleObeserver();

        final List<String> list = new ArrayList<>();
        setContentView(R.layout.activity_sample);
        list.add("retrofit+rxjava");//0
        list.add("retrofit高内聚低耦合，可以用Gson来转换数据");//1
        list.add("rxjava是转换了retrofit的回调");//2
        list.add("使用LifeCycle去监听有生命的组件，如Activity和Fragment");//3
        list.add("LiveData 是生命周期感知的「数据持有类」，意味着它代表其它应用组件譬如 Activity、Fragment、Service 的生命周期。这确保了 LiveData 只会更新处于活跃状态的组件。");//4
        list.add("用来保存UI数据的类，Google建议把所有UI数据保存在ViewModel中，而不是Activity中");//5
        list.add("一个开源易用的RecycleviewAdapter");//6
        list.add("retrofit队列同步请求，需要在子线程");//7
        list.add("retrofit + Rxjava串行请求");//8
        list.add("retrofit + Rxjava并行请求");//9
        list.add("使用paging来进行分页加载");//10
        list.add("databinding数据绑定");//11
        list.add("navigation导航");//12




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
                        switch (i) {
                            case 0://最普通的原始的加载
                            case 1:
                            case 2:
                            case 7:
                            case 8:
                                //simpleRequestServer();
                                startActivity(new Intent(SampleActivity.this, SampleReqNetWorkActivity.class));
                                break;

                            case 3://Lifecycle
                                initLifecycleObeserver();
                                break;

                            case 4://LiveData
                            case 5://ViewModel
//                              参考LiveData  https://juejin.im/entry/5987022df265da3e161a86f3
                                startActivity(new Intent(SampleActivity.this, SampleViewModelActivity.class));



                                //1、activity显示UI接受互动 2、ViewModel储存UI数据 3、Repository储存与加载数据的API，可以通过Presenter类
                                //注意：1、千万不要把Context传入ViewModel中，因为ViewModel的生命周期比他们更长，如果需要引用Application的ViewModel，可以了解一下AndroidViewModel
                                //https://www.jianshu.com/p/b8a29407b6e1
                                break;
                            case 6://BaseRecyclerViewAdapterHelper
                                //参考：https://www.jianshu.com/p/b343fcff51b0
                                //由于adapter本身能力有限，我们又不想耦合view层所以有些需求是现实不了，于是合作了一些优秀开源库，为开发者提供更多可能性。以下扩展框架都是有结合BRVAH的demo。
                                //PinnedSectionItemDecoration：一个强大的粘性标签库
                                //EasyRefreshLayout：这个库让你轻松实现下拉刷新和上拉更多
                                //EasySwipeMenuLayout：独立的侧滑删除
                                startActivity(new Intent(SampleActivity.this, SampleRecycleViewActivity.class));
                                break;
                            case 9:
                                requestManyServerByRxjavaTogether();
                                break;
                            case 10:
                                startActivity(new Intent(SampleActivity.this, SamplePagingActivity.class));

                                break;
                            case 11:
                                startActivity(new Intent(SampleActivity.this, SampleBindDataActivity.class));
                                break;
                            case 12:
                                startActivity(new Intent(SampleActivity.this, SampleNavigationActivity.class));
//                                startActivity(new Intent(SampleActivity.this,SampleNavigationActivity2.class));
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
     * Retrofit+Rxjava 并行实现多个网络请求
     */
    private void requestManyServerByRxjavaTogether() {
        Observable<Result<ResponseBody>> observable1 = HttpUtils.getAllApiInCompany().getSongPoerty(1, 5);
        Observable<Result<ResponseBody>> observable2 = HttpUtils.getAllApiInCompany().searchMusicRx("hello");
        Observable.merge(observable1,observable2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result<ResponseBody>>() {
                    int count = 0;
                    @Override
                    public void onSubscribe(Disposable d) {
                        showLoadingBar("Retrofit+Rxjava 并行实现多个网络请求");
                    }

                    @Override
                    public void onNext(Result<ResponseBody> responseBodyResult) {
                        count++;
                        LogUtils.i(this,"成功获取在rxjava中获取数据,count : "+count);
                        try {
                            String resultStr = responseBodyResult.response().body().string();
                            LogUtils.i(this,resultStr);
                            Toast.makeText(SampleActivity.this, "count : "+count+"\n"+resultStr, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        LogUtils.i(this,"finish");
                        Toast.makeText(SampleActivity.this, "Retrofit+Rxjava 并行实现多个网络请求 finish", Toast.LENGTH_SHORT).show();
                        hideLoadingBar();
                    }
                });
    }


    static class MyHandle extends Handler {
        WeakReference<Context> weakReference;

        public MyHandle(Context context) {
            weakReference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(weakReference.get(), "多个接口已调用完成", Toast.LENGTH_SHORT).show();
            if (weakReference.get() instanceof BaseActivity) {
                ((BaseActivity) weakReference.get()).hideLoadingBar();
            }
        }
    }

    /**
     * 最简单的请求网络方法
     */
    private void simpleRequestServer() {
        Map<String, String> mapParams = new HashMap<>();
//        ?page=1&count=2&type=video
        mapParams.put("page", "1");
        mapParams.put("count", "4");
        mapParams.put("type", "video");
        Call<ResponseBody> bean = HttpUtils.getAllApiInCompany().getSimpleString(mapParams);
        bean.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                String s = null;
                try {
                    s = response.body().string();
                    System.out.println(s);
                    LogUtils.i(this,"异步处理结果："+s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
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
            LogUtils.i(this, "onresume 感知到页面在显示");
            //Toast.makeText(this,)
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        public void onViewHinded() {
            LogUtils.i(this, "onpause 感知到页面为pause");
        }
    }
}
