package com.bluetree.jetpacksample.utils.http;

import com.bluetree.jetpacksample.utils.LogUtils;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava2.Result;

/**
 * base接口回调，使用ResponseBody会自动输入相关信息
 * @param <T>
 */
public class BaseHttpRxjavaCallback<T> implements Observer<T> {

    private static final String DIVID_STR = "--->";

    @Override
    public void onSubscribe(Disposable d) {
        LogUtils.i(this, "onSubscribe "+d.toString());
    }

    @Override
    public void onNext(T t) {
        LogUtils.i(this, "onNext "+t.toString());

        if (t instanceof Result) {
            Object body = ((Result) t).response().body();
            if (body instanceof ResponseBody) {
                LogUtils.i(this, DIVID_STR + ((Result) t).response().raw().request().url());
                try {
                    String s = ((ResponseBody) body).string();
                    LogUtils.i(this,s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                LogUtils.i(this, DIVID_STR + "end" + DIVID_STR);
            }
        }

    }

    @Override
    public void onError(Throwable e) {
        LogUtils.e(this,"call api fail "+e.toString());
    }

    @Override
    public void onComplete() {
        LogUtils.i(this,"onComplete ");
    }
}
