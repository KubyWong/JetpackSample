package com.bluetree.jetpacksample.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.arch.paging.PositionalDataSource;
import android.support.annotation.NonNull;

import com.bluetree.jetpacksample.bean.SamMusicBean;
import com.bluetree.jetpacksample.bean.SamMusicResponeBean;
import com.bluetree.jetpacksample.utils.LogUtils;
import com.bluetree.jetpacksample.utils.http.HttpUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 分页加载activity的ViewModel
 */
public class PagingViewModel extends AndroidViewModel {
    final int PAGE_SIZE = 20;
    int pageCount = 1;

    //step5、使用paging的时候，必须使用PagedList<T>来代替List<T>
    LiveData<PagedList<SamMusicBean>> pagedListLiveData;

    public PagingViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<PagedList<SamMusicBean>> getPagedListLiveData() {
        initPagedList();
        return pagedListLiveData;
    }

    private void initPagedList() {
        //step13、生成自定义的DataSource对象
        PositionalDataSource<SamMusicBean> positionalDataSource = new MyDataSource();

        //step14、通过PagedList.Config.Builder()生成配置对象，传入的到LivePagedListBuilder中
        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(PAGE_SIZE)//每次加载的数据数量15
                //距离本页数据几个时候开始加载下一页数据(例如现在加载10个数据,设置prefetchDistance为2,则滑到第八个数据时候开始加载下一页数据).
                .setPrefetchDistance(5)//15
                //这里设置是否设置PagedList中的占位符,如果设置为true,我们的数据数量必须固定,由于网络数据数量不固定,所以设置false.
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(PAGE_SIZE)//15
                .build();


        /*
        //step6、新建LiveData<PagedList<T>>，需要通过LivePagedListBuilder来完成，里面需要两个参数。
        一个是：DataSource.Factor，作用就是返回一个DataSource，有几种类型，（PositionalDataSource适合接口是带pageCount，pageSize）
        一个是：PagedList.Config，配置主要分页加载参数，如每页加载多少条数据等
        */
        // 构建LiveData
        pagedListLiveData = new LivePagedListBuilder(new PageDataSourceFacotry(positionalDataSource)//自己定义
                , config).build();

    }

    /*
    //step8、定制自己的DataSource，继承哪个DataSource是根据后台接口来定的，这里的接口是pageCount和pageSize
    所以，我们需要继承PositionalDataSource<T>，并且实现他们的抽象函数
    */
    class MyDataSource extends PositionalDataSource<SamMusicBean>{
        List<SamMusicBean> listData = new ArrayList<>();
        /**
         * recyclerView第一次加载时自动调用
         *
         * @param params   包含当前加载的位置position、下一页加载的长度count
         * @param callback 将数据回调给UI界面使用callback.onResult
         */
        @Override
        public void loadInitial(@NonNull LoadInitialParams params, @NonNull final LoadInitialCallback<SamMusicBean> callback) {
            //step9、在这里请求网络加载数据

            LogUtils.i(this,"loadInitial "+pageCount);
            //计算显示到第几条
            final int position = computeInitialLoadPosition(params, PAGE_SIZE);
            //recyclerView第一次加载时我们调用OkHttp进行数据的加载
            Map<String, String> paramsGet = new HashMap<>();
            paramsGet.put("page", pageCount+"");
            paramsGet.put("count", PAGE_SIZE + "");
            paramsGet.put("type", "video");
            HttpUtils.getAllApiInCompany().getVideoBean(paramsGet).enqueue(new Callback<SamMusicResponeBean>() {
                @Override
                public void onResponse(Call<SamMusicResponeBean> call, Response<SamMusicResponeBean> response) {
                    List<SamMusicBean> tmpList = response.body().getResult();
                    listData.addAll(tmpList);

                    //step10、
                    //最重要的一步，paging是基于观察者模式，我们在这里调用callback.onResult();
                    //会直接将数据list返回到UI层，等下面接受到这个list数据的数据的时候我会提醒大家
                    //如果设置占位符需要调用三个参数的onResult()方法，最后一个参数为每页的总数据量
                    //我们没有设置占位符，因此调用两个参数的方法，注意position通过computeInitialLoadPosition(params, PAGE_SIZE)获取
                    callback.onResult(listData,position);
                }

                @Override
                public void onFailure(Call<SamMusicResponeBean> call, Throwable t) {

                }
            });

        }

        /**
         * 当用户滑动recyclerView到下一屏的时候自动调用，这里我们自动加载下一页的数据
         *
         * @param params   包含当前加载的位置position、下一页加载的长度count
         * @param callback 将数据回调给UI界面使用callback.onResult
         */
        @Override
        public void loadRange(@NonNull LoadRangeParams params, @NonNull final LoadRangeCallback<SamMusicBean> callback) {

            //step11、请求网络加载更多数据，执行在子线程
            pageCount++;
            LogUtils.i(this,"loadRange "+pageCount);

            Map<String, String> paramsGet = new HashMap<>();
            paramsGet.put("page", pageCount + "");
            paramsGet.put("count", PAGE_SIZE + "");
            paramsGet.put("type", "video");
            HttpUtils.getAllApiInCompany().getVideoBean(paramsGet)
                    .enqueue(new Callback<SamMusicResponeBean>() {
                @Override
                public void onResponse(Call<SamMusicResponeBean> call, Response<SamMusicResponeBean> response) {
                    List<SamMusicBean> tmpList = response.body().getResult();
                    listData.addAll(tmpList);

                    //step12、请求网络加载更多数据成功，调用callback的onResult()函数，传入总的list触发ui更新
                    callback.onResult(listData);
                }

                @Override
                public void onFailure(Call<SamMusicResponeBean> call, Throwable t) {

                }
            });
        }
    }

    /**
     * 自定义DataSource.Factory
     * //step7、定义一个DataSource.Facotry的子类，因为后台给的接口是那种有pageSize和pageCount的那种，
     * 所以这里选择返回的DataSource应该是PositionalDataSource<T>这种，别问我为什么，google就是这么说的。其他的情况可以参考
     * https://www.jianshu.com/p/ad040aab0e66
     */
    class PageDataSourceFacotry extends DataSource.Factory {

        PositionalDataSource<SamMusicBean> positionalDataSource;

        public PageDataSourceFacotry(PositionalDataSource<SamMusicBean> positionalDataSource) {
            this.positionalDataSource = positionalDataSource;
        }

        @Override
        public DataSource create() {
            return positionalDataSource;
        }
    }

}
