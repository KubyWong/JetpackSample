package com.bluetree.jetpacksample.activity

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.bluetree.jetpacksample.BaseActivity
import com.bluetree.jetpacksample.R
import com.bluetree.jetpacksample.bean.SamMusicResponeBean
import com.bluetree.jetpacksample.utils.LogUtils
import com.bluetree.jetpacksample.utils.ToastUtils
import com.bluetree.jetpacksample.utils.http.BytePayApiService
import com.bluetree.jetpacksample.utils.http.HttpUtils
import com.bluetree.jetpacksample.utils.http.HttpUtils.BASE_URL
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.*
import retrofit2.adapter.rxjava2.Result
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*

class SampleReqNetWorkActivity : BaseActivity(){
    /**
     * 使用静态内部类的Handler可以防止内存泄漏
     */
    private var handler: MyHandle? = null

    class MyHandle(context: Context) :Handler(){
        var weakReference: WeakReference<Context> = WeakReference(context)

        override fun handleMessage(msg: Message?) {
            Toast.makeText(weakReference.get(), "多个接口已调用完成", Toast.LENGTH_SHORT).show()
            if (weakReference.get() is BaseActivity) {
                (weakReference.get() as BaseActivity).hideLoadingBar()
            }
        }
    }

    private var lv: ListView? = null
    val strArr = arrayOf(
            "retrofit基本用法，异步请求(需要callback)"
            , "retrofit基本用法，同步请求（需在子线程）"
            , "数据转换器：添加Gson数据转换器"
            , "回调转换器：rxjava来转换回调"
            , "多接口，串行请求"
            , "多接口，并行请求"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_req_net_work)

        handler = MyHandle(this)
        lv = findViewById(R.id.lv)

        with(lv!!) {
            adapter = ArrayAdapter<String>(this@SampleReqNetWorkActivity,android.R.layout.simple_list_item_1,strArr)
            setOnItemClickListener(object : AdapterView.OnItemClickListener {
                override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    Toast.makeText(this@SampleReqNetWorkActivity, "position: ${position}", Toast.LENGTH_SHORT).show()


                    when (position) {
                        0 -> {
                            //同步(Synchronous)和异步(Asynchronous)
                            simpleRequestServerSynchronous()
                        }
                        1 -> {
                            //同步(Synchronous)和异步(Asynchronous)
                            //使用rxjava来转换回调，可以使用rxjava的特性
                            simpleRequestServerAsynchronous()
                        }
                        2 -> {
                            addConvertBean()
                        }
                        3 -> {
                            addConvertCallAadapterRxjava()
                        }
                        4 -> {
                            requestManyServerByRxjavaByOrder()

                        }
                        5 -> {
                            requestManyServerByRxjavaTogether()
                        }
                        else ->{

                        }
                    }
                }

            })
        }
    }


    /**
     * Retrofit+Rxjava 并行实现多个网络请求
     */
    private fun requestManyServerByRxjavaTogether() {
        val observable1 = HttpUtils.getAllApiInCompany().getSongPoerty(1, 5)
        val observable2 = HttpUtils.getAllApiInCompany().searchMusicRx("hello")
        Observable.merge(observable1, observable2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Result<ResponseBody>> {
                    internal var count = 0
                    override fun onSubscribe(d: Disposable) {
                        showLoadingBar("Retrofit+Rxjava 并行实现多个网络请求")
                    }

                    override fun onNext(responseBodyResult: Result<ResponseBody>) {
                        count++
                        LogUtils.i(this, "成功获取在rxjava中获取数据,count : $count")
                        try {
                            val resultStr = responseBodyResult.response()!!.body()!!.string()
                            LogUtils.i(this, resultStr)
                            Toast.makeText(this@SampleReqNetWorkActivity, "count : $count\n$resultStr", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onComplete() {
                        LogUtils.i(this, "finish")
                        Toast.makeText(this@SampleReqNetWorkActivity, "Retrofit+Rxjava 并行实现多个网络请求 finish", Toast.LENGTH_SHORT).show()
                        hideLoadingBar()
                    }
                })
    }

    /**
     * Retrofit+Rxjava 多个任务串行请求
     */
    private fun requestManyServerByRxjavaByOrder() {
        val observable1 = HttpUtils.getAllApiInCompany().getTangPoerty(1, 5)
        val observable2 = HttpUtils.getAllApiInCompany().searchMusicRx("hello")

        observable1.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(object : Function<Result<ResponseBody>, Observable<Result<ResponseBody>>> {
                    @Throws(Exception::class)
                    override fun apply(responseBodyResult: Result<ResponseBody>): Observable<Result<ResponseBody>> {
                        val result = responseBodyResult.response()!!.body()!!.string()
                        LogUtils.i(this, "串行接口1数据：$result")
                        handler?.post(Runnable { Toast.makeText(this@SampleReqNetWorkActivity, "串行接口1数据：$result", Toast.LENGTH_SHORT).show() })
                        return observable2
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Result<ResponseBody>> {
                    override fun onSubscribe(d: Disposable) {
                        showLoadingBar("Retrofit+Rxjava 多个任务串行请求")
                        LogUtils.i(this, "Retrofit+Rxjava 多个任务串行请求 begin")
                    }

                    override fun onNext(responseBodyResult: Result<ResponseBody>) {
                        var result: String? = null
                        try {
                            result = responseBodyResult.response()!!.body()!!.string()
                            LogUtils.i(this, "串行接口2数据：" + result!!)
                            Toast.makeText(this@SampleReqNetWorkActivity, "串行接口2数据：$result", Toast.LENGTH_SHORT).show()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onComplete() {
                        LogUtils.i(this, "Retrofit+Rxjava 多个任务串行请求 finish")
                        hideLoadingBar()
                        Toast.makeText(this@SampleReqNetWorkActivity, "Retrofit+Rxjava 多个任务串行请求 finish", Toast.LENGTH_SHORT).show()
                    }
                })

    }


    private fun addConvertCallAadapterRxjava() {
        var rf1 = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        var server:BytePayApiService = rf1.create(BytePayApiService::class.java)

        server.searchMusicRx("Hello")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())//主程序处理
                .subscribe(object : Observer<Result<ResponseBody>>{
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: Result<ResponseBody>) {
                        ToastUtils.show(this@SampleReqNetWorkActivity, "rxjava 转换callback：" + (t.response()?.body()?.string()
                                ?: "null"))

                    }

                    override fun onError(e: Throwable) {
                    }

                })

    }

    private fun addConvertBean() {
        var rf1 = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        var server:BytePayApiService = rf1.create(BytePayApiService::class.java)

        val paramsGet = HashMap<String, String>()
        paramsGet["page"] = "1"
        paramsGet["count"] = "20"
        paramsGet["type"] = "video"
        server.getVideoBean(paramsGet).enqueue(object : Callback<SamMusicResponeBean> {
            override fun onResponse(call: Call<SamMusicResponeBean>, response: Response<SamMusicResponeBean>) {
                val tmpList = response.body()
                ToastUtils.show(this@SampleReqNetWorkActivity, "我是一个bean对象：${Gson().toJson(tmpList, SamMusicResponeBean::class.java)}")
            }

            override fun onFailure(call: Call<SamMusicResponeBean>, t: Throwable) {
                ToastUtils.show(this@SampleReqNetWorkActivity, "error")
            }
        })
    }


    /**
     * 同步处理，需要放在子线程
     */
    private fun simpleRequestServerAsynchronous() {
        Thread(object : Runnable {
            override fun run() {
                var response = HttpUtils.getAllApiInCompany().searchMusic("Gavin").execute()

                var s: String? = null
                try {
                    s = response.body()!!.string()
                    LogUtils.i(this, "同步Asynchronous处理结果：" + s!!)
                    println(s)
                    handler?.post(object :Runnable{
                        override fun run() {
                            ToastUtils.show(this@SampleReqNetWorkActivity,"同步请求（需在子线程）${s}")
                        }
                    })
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        ).start()
    }


    /**
     * 最简单的请求网络方法，异步处理
     */
    private fun simpleRequestServerSynchronous() {
        val mapParams = HashMap<String, String>()
        //        ?page=1&count=2&type=video
        mapParams["page"] = "1"
        mapParams["count"] = "4"
        mapParams["type"] = "video"
        val bean = HttpUtils.getAllApiInCompany().getSimpleString(mapParams)
        bean.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                var s: String? = null
                try {
                    s = response.body()!!.string()
                    LogUtils.i(this, "异步Synchronous处理结果：" + s!!)
                    println(s)
                    ToastUtils.show(this@SampleReqNetWorkActivity,"异步Synchronous处理结果：" + s!!)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}
