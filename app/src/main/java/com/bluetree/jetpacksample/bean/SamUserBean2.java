package com.bluetree.jetpacksample.bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;

import com.bluetree.jetpacksample.BR;

public class SamUserBean2 extends BaseObservable {
    public  String firstName;
    private  String lastName;

    // 数据 bean 中声明一个属性
    public ObservableField<String> age = new ObservableField<>();

    public SamUserBean2(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    @Bindable
    public String getFirstName() {
        return this.firstName;
    }
    public String getLastName() {
        return this.lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        notifyPropertyChanged(BR.firstName);
    }
}
