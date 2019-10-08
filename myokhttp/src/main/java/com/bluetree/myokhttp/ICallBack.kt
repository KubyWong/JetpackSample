package com.bluetree.myokhttp

import java.io.InputStream

interface ICallBack {
    fun onSuccess(response: InputStream)
    fun onFail()

}