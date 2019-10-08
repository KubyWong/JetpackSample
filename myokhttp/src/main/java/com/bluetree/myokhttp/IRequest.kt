package com.bluetree.myokhttp

interface IRequest {
    fun setUrl(url:String)
    fun setData(data: ByteArray)
    fun setCallBack(callBack:ICallBack)
    fun execute()
}
