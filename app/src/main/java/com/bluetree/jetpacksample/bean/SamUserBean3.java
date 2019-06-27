package com.bluetree.jetpacksample.bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.bluetree.jetpacksample.BR;

public class SamUserBean3 {

    // 数据 bean 中声明一个属性进行
    public ObservableField<String> firstName = new ObservableField<>();

    public ObservableBoolean sex = new ObservableBoolean();


}
