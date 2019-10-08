package com.bluetree.myokhttp

interface IJsonDataTransform<T> {
    fun onSuccess(respone: T)
    fun onFail()

}