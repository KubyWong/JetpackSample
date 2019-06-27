package com.bluetree.jetpacksample.utils.http;

import com.bluetree.jetpacksample.utils.LogUtils;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * base接口回调，使用ResponseBody会自动输入相关信息
 * @param <T>
 */
public class BaseHttpRetrofitCallback<T> implements Callback<T> {

    private static final String DIVID_STR = "--->";

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        LogUtils.i(this, DIVID_STR + response.raw().request().url());

        T body = response.body();
        if (body instanceof ResponseBody) {
            try {
                String s = ((ResponseBody) body).string();
                LogUtils.i(this,s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        LogUtils.i(this, DIVID_STR + "end" + DIVID_STR);

    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        LogUtils.e(this,"call api fail "+t.toString());
    }
}
