package com.bluetree.jetpacksample.reponsitory;

import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;

import com.bluetree.jetpacksample.utils.UIEvent;
import com.bluetree.jetpacksample.utils.http.HttpUtils;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava2.Result;

/**
 * 数据仓库类，常用于业务逻辑有跟数据库联系的情况下
 */
public class DataRepository {
    private MutableLiveData<String> liveData;
    private MutableLiveData<UIEvent> uiEventMutableLiveData;

    public DataRepository(MutableLiveData<String> liveData, MutableLiveData<UIEvent> uiEventMutableLiveData) {
        this.liveData = liveData;
        this.uiEventMutableLiveData = uiEventMutableLiveData;
    }

    public void getMusicList(String musicName) {
        HttpUtils.getAllApiInCompany().searchMusicRx(musicName)
        .subscribeOn(Schedulers.io())
        .subscribe(new Observer<Result<ResponseBody>>() {
            @Override
            public void onSubscribe(Disposable d) {
                //通过该表该值触发加载框
                if(uiEventMutableLiveData!=null) uiEventMutableLiveData.setValue(UIEvent.SHOW_PROGRESS_DIALOG);
            }

            @Override
            public void onNext(Result<ResponseBody> responseBodyResult) {
                try {
                    liveData.postValue(responseBodyResult.response().body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                //通过该表该值触发加载框
                if(uiEventMutableLiveData!=null) uiEventMutableLiveData.postValue(UIEvent.HIND_PROGRESS_DIALOG);
            }
        });
        ;
    }
}
