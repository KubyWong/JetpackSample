package com.bluetree.jetpacksample.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.TextView;

import com.bluetree.jetpacksample.BaseActivity;
import com.bluetree.jetpacksample.R;
import com.bluetree.jetpacksample.viewmodel.SearchMusicViewModel;
import com.bluetree.jetpacksample.utils.LogUtils;
import com.bluetree.jetpacksample.utils.UIEvent;

/**
 * LiveData+ViewModel
 */
public class SampleViewModelActivity extends BaseActivity {

    //viewmodel中可以持有多个LiveData
    SearchMusicViewModel musicViewModel;
    private TextView textView;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_view_model);

        textView = findViewById(R.id.tv);
        searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                LogUtils.i(this,"onQueryTextSubmit : "+s);
                Log.i("listener", "onQueryTextSubmit: "+s);
                musicViewModel.getMusicList(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                LogUtils.i(this,"onQueryTextChange : "+s);
                Log.i("listener", "onQueryTextChange: "+s);
                return false;
            }
        });


        initViewModel();
    }

    /**
     * 初始化viewmodel和绑定数据
     */
    private void initViewModel() {
        musicViewModel = ViewModelProviders.of(this).get(SearchMusicViewModel.class);

        /*使用LiveData和ViewModel渲染，Google建议UI数据放到ViewModel中，
        所以把LiveData放到ViewModel，LiveData是一个有感知生命的数据，并且作为一个被观察者，
        他可以注册一个观察者，当这个LiveData中数据改变了，那么可以再通过注册进来的observer进行刷新。*/

        Observer<String> stringObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //接收到变化，获得最新的数据，在这里更新UI
                textView.setText(s);
            }
        };
        //被观察者 observe（注册/添加/订阅） 观察者
        musicViewModel.getLiveData().observe(this,stringObserver);

        musicViewModel.getUiEventMutableLiveData().observe(this, new Observer<UIEvent>() {
            @Override
            public void onChanged(@Nullable UIEvent uiEvent) {
                switch (uiEvent) {
                    case HIND_PROGRESS_DIALOG:
                        hideLoadingBar();
                        break;
                    case SHOW_PROGRESS_DIALOG:
                        showLoadingBar();
                        break;
                }
            }
        });
    }
}
