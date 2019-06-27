package com.bluetree.jetpacksample.viewmodel;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.bluetree.jetpacksample.bean.SamUserBean;

public class MyDataBindingViewModel extends ViewModel {
    MutableLiveData<SamUserBean> userBeanLiveData;

    public LiveData<SamUserBean> getUserBeanLiveData() {
        if (userBeanLiveData == null) {
            userBeanLiveData = new MutableLiveData<>();
        }
        return userBeanLiveData;
    }
}
