package com.bluetree.myokhttp

import android.util.Log
import com.google.gson.Gson

open class HttpTask<T> : Runnable {
    private var req: IRequest

    constructor(url: String, data:T,req: IRequest,callback: ICallBack){
        this.req = req
        req.setUrl(url)
        var jsonStr = Gson().toJson(data)
        req.setCallBack(callback)
        req.setData(jsonStr.toByteArray(Charsets.UTF_8))
    }

    override fun run() {
        if (req != null) {
            req.execute()
        }
    }
}
