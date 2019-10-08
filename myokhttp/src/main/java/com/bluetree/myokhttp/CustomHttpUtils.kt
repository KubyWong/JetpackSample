package com.bluetree.myokhttp

class CustomHttpUtils {
    companion object{
        fun <T,R> sendJsonRequest(url: String,data:T, transform: IJsonDataTransform<R>, className: Class<R>){
            var req= JsonHttpRequest()
            val callback = JsonCallBackLis(className, transform)
            var task = HttpTask<T>(url,data,req,callback)

            ThreadPoolExecutorManager.getInstance().addTask(task)
        }
    }

}