package com.bluetree.myokhttp

import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class JsonCallBackLis<T> :ICallBack {
    private lateinit var transform: IJsonDataTransform<T>
    private var response: T? = null
    private lateinit var className: Class<T>

    constructor(call:Class<T>,transform: IJsonDataTransform<T>){
        this.className = call
        this.transform = transform

    }


    override fun onSuccess(inputStream: InputStream) {
        //流转换成
        val responseStr = getContent(inputStream)

        response = Gson().fromJson<T>(responseStr, className)
        response?.let {
            transform.onSuccess(it)
        }
    }

    private fun getContent(inputStream: InputStream): String {
        val bufferedReader = BufferedReader(
                               InputStreamReader(inputStream))
                       // 接受流里面的数据
                       var readData: String ?= ""
                       // 拼接流里的数据,保证数据是完整的
                       val builder = StringBuilder()
                       // 使用while循环,读取流里的数据,当流里的数据被读取完毕的时候跳出循环

        do {
            builder.append(readData)
            readData = bufferedReader.readLine()
        }while (readData!=null)

        val stringData = builder.toString()
        return stringData
    }

    override fun onFail() {
        transform.onFail()
    }
}