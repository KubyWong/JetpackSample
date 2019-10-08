package com.bluetree.myokhttp

import android.os.Handler
import android.os.Message
import android.support.annotation.MainThread
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


class JsonHttpRequest :IRequest{

    private lateinit var callBack: ICallBack
    private lateinit var data: ByteArray
    private lateinit var url: String


    override fun setUrl(url: String) {
        this.url = url

    }

    override fun setData(data: ByteArray) {
        this.data = data
    }

    override fun setCallBack(callBack: ICallBack) {
        this.callBack = callBack
    }

    override fun execute() {
                try {
                    // 得到URL对象,设置要访问的路径
                    val url = URL(url)
                    // 得到联网操作类,HttpURLConnection
                    val openConnection = url
                            .openConnection() as HttpURLConnection
                    // 设置连接超时,以毫秒为单位
                    openConnection.setConnectTimeout(5000)
                    // 设置请求方式,有GET和POST
                    openConnection.setRequestMethod("GET")
                    // 设置读取超时,以毫秒为单位
                    openConnection.setReadTimeout(5000)
                    // 开始连接
                    openConnection.connect()
                    // 通过响应码判断是否连接成功
                    if (openConnection.getResponseCode() === 200) {
                        // 得到服务器返回的数据,是以流的形式返回的
                        val inputStream = openConnection
                                .getInputStream()
                        callBack.onSuccess(inputStream)

                        /*val bufferedReader = BufferedReader(
                                InputStreamReader(inputStream))
                        // 接受流里面的数据
                        var readData: String = ""
                        // 拼接流里的数据,保证数据是完整的
                        val builder = StringBuilder()
                        // 使用while循环,读取流里的数据,当流里的数据被读取完毕的时候跳出循环
                        while ((readData = bufferedReader.readLine()) != null) {
                            builder.append(readData)
                        }
                        val stringData = builder.toString()*/

                    }
                } catch (e:Exception) {
                    e.printStackTrace()
                    callBack.onFail()
                }

    }

}