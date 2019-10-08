package com.bluetree.jetpacksample.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bluetree.jetpacksample.R
import com.bluetree.jetpacksample.utils.LogUtils
import com.bluetree.jetpacksample.utils.http.HttpUtils
import com.bluetree.myokhttp.CustomHttpUtils
import com.bluetree.myokhttp.IJsonDataTransform

class SampleMyOkHttpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_my_ok_http)
    }
    fun onclick(view:View){
        val url = "https://api.apiopen.top/searchMusic"
        CustomHttpUtils.sendJsonRequest(url,null, object : IJsonDataTransform<UserBean> {
            override fun onSuccess(respone: UserBean) {
                LogUtils.i("respone----> $respone")
            }

            override fun onFail() {
                LogUtils.e("dd")
            }

        },UserBean::class.java)
    }

    class UserBean{
        //        {"code":200,"message":"成功!","result":""}
        var code: Int = -1
        lateinit var message: String
        lateinit var result: String
        override fun toString(): String {
            return "UserBean(code=$code, message='$message', result='$result')"
        }


    }
}
