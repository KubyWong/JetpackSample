package com.bluetree.jetpacksample.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.bluetree.jetpacksample.reponsitory.DataRepository;
import com.bluetree.jetpacksample.utils.UIEvent;

/**
 * google建议把UI需要用到的数据都放到ViewModel中
 * 生命周期比Activity和Fragment要长，如旋转屏幕，activity会被销毁重建，但是ViewModel不会
 * 禁止要把context传到ViewModel，造成内存泄漏
 */
public class SearchMusicViewModel extends ViewModel {

    //创建LiveData
    MutableLiveData<String> liveData = new MutableLiveData<>();
    MutableLiveData<UIEvent> uiEventMutableLiveData = new MutableLiveData<>();


    //复杂的网络请求可以通过在ViewModel中使用一个Repository来维护
    // https://www.jianshu.com/p/b8a29407b6e1
//    在这一部分，我们要完成Repository的编写。正如架构图示多描述的那样，Repository层的数量往往和DAO相对应。
//    我们需要在Repository层完成接口数据的获取以及转换为持久化数据，同时需要完成持久化数据的完成。
    DataRepository dataRepository;

    public SearchMusicViewModel() {
        dataRepository = new DataRepository(liveData,uiEventMutableLiveData);
    }

    public MutableLiveData<String> getLiveData() {
        if(liveData == null) liveData = new MutableLiveData<>();
        return liveData;
    }

    public DataRepository getDataRepository() {
        return dataRepository;
    }

    public MutableLiveData<UIEvent> getUiEventMutableLiveData() {
        return uiEventMutableLiveData;
    }

    public void getMusicList(String s) {
        dataRepository.getMusicList(s);
    }
}
