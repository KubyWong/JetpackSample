package com.bluetree.myokhttp

import java.util.concurrent.*

class ThreadPoolExecutorManager{

    private lateinit var mThreadPoolExecu: ThreadPoolExecutor
    var linkedBlockingDeque: LinkedBlockingDeque<Runnable>? = null

    var threadPoolExecutorManager: ThreadPoolExecutorManager? = null

    constructor(){
        threadPoolExecutorManager = this
         mThreadPoolExecu = ThreadPoolExecutor(3,5,15,TimeUnit.SECONDS
                ,ArrayBlockingQueue<Runnable>(4)
        , RejectedExecutionHandler { r, executor ->
            addTask(r)
        })
        mThreadPoolExecu.execute(corethread)
    }

    private var corethread: Runnable = Runnable {
        while (true) {
            var currentRunnable = linkedBlockingDeque?.take()
            if (currentRunnable != null) {
                mThreadPoolExecu.execute(currentRunnable)
            }

        }
    }

    fun addTask(run: Runnable) {
        if (linkedBlockingDeque == null) {
            linkedBlockingDeque = LinkedBlockingDeque(4)

        }
        linkedBlockingDeque?.add(run)
    }
    companion object{
        fun getInstance(): ThreadPoolExecutorManager {
            return ThreadPoolExecutorManager()
        }
    }

}
