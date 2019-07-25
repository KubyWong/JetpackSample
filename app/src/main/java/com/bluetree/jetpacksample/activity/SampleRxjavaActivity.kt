package com.bluetree.jetpacksample.activity

import android.os.*
import android.view.View
import android.widget.TextView
import com.bluetree.jetpacksample.utils.LogUtils
import com.bluetree.jetpacksample.utils.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.google.gson.Gson
import io.reactivex.*
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * rxjava使用demo
 */
class SampleRxjavaActivity : BaseRecycleActivity() {
    val handler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            when (msg?.what) {
                0->ToastUtils.show(this@SampleRxjavaActivity,"最简单的rxjava，complete")
                1->ToastUtils.show(this@SampleRxjavaActivity,"快速创建observer")
                else ->ToastUtils.show(this@SampleRxjavaActivity, msg?.obj as String?)
            }

        }
    }
    override fun initList(): MutableList<Any?> {
        val listData = mutableListOf<MyDataModel>()
        listData.add(MyDataModel("", "rxjava框架作用是异步，原理是观察者模式"))
        listData.add(MyDataModel("", "快速创建observer，仅仅实现onNext函数即可，rxjava自动帮你构建出observer"))
        listData.add(MyDataModel("", "快速创建observable，使用操作符"))
        listData.add(MyDataModel("", "快速创建observer+快速创建observable"))
        listData.add(MyDataModel("", "使用Schedule控制rxjava异步操作"))
        listData.add(MyDataModel("", "map。【变换】，就是将事件序列中的对象或整个序列进行加工处理，转换成不同的事件或事件序列"))
        listData.add(MyDataModel("", "flatMap操作符，铺平多级list中的item，如List<Student>中的List<course>,可以铺平成全部List<course>"))
        listData.add(MyDataModel("", "lift(变换)操作符，两个observer先后执行.这些变换虽然功能各有不同，但实质上都是针对事件序列的处理和再发送。而在 RxJava 的内部，它们（map,flatMap）是基于同一个基础的变换方法： lift(Operator)。"))
        listData.add(MyDataModel("8", "compose操作符，Observable 可以利用传入的 Transformer 对象的 call 方法直接对自身进行处理。除了 lift() 之外， Observable 还有一个变换方法叫做 compose(Transformer)。它和 lift() 的区别在于， lift() 是针对事件项和事件序列的，而 compose() 是针对 Observable 自身进行变换。"))
        return listData.toMutableList()
    }

    override fun getItemClickListener(): BaseQuickAdapter.OnItemClickListener {
        return object : BaseQuickAdapter.OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                when (position) {
                    0 -> simpleRxjava()
                    1 -> rxjavaCreateObserverAuto()
                    2 -> rxjavaCreateObservableAuto()
                    3 -> rxjavaCreateObserverAndCreateObservableAuto()
                    4 -> rxjavaSchedule()
                    5 -> rxjavaChange()
                    6 -> rxjava_flatMap()
                    7 -> rxjava_lift()//变换的原理：lift()
                    8 -> rxjava_compose()//compose: 对 Observable 整体的变换
                }
            }

        }
    }

    /**
     *  lift() 是针对事件项和事件序列的，而 compose() 是针对 Observable 自身进行变换。举个例子，假设在程序中有多个 Observable ，并且他们都需要应用一组相同的 lift() 变换。你可以这么写：
    observable1
    .lift1()
    .lift2()
    .lift3()
    .lift4()
    .subscribe(subscriber1);
    observable2
    .lift1()
    .lift2()
    .lift3()
    .lift4()
    .subscribe(subscriber2);
    observable3
    .lift1()
    .lift2()
    .lift3()
    .lift4()
    .subscribe(subscriber3);
    observable4
    .lift1()
    .lift2()
    .lift3()
    .lift4()
    .subscribe(subscriber1);
     */
    private fun rxjava_compose() {
        //compose() 方法，Observable 可以利用传入的 Transformer 对象的 call 方法直接对自身进行处理，也就不必被包在方法的里面了。
        val subscriber = object : Consumer<Student> {
            override fun accept(t: Student?) {
                LogUtils.i(Gson().toJson(t))
            }
        }

        val createObservableFun: (Int) -> Observable<Int> = {x -> Observable.create(object : ObservableOnSubscribe<Int> {
            override fun subscribe(emitter: ObservableEmitter<Int>) {
                emitter.onNext(x)
            }

        })}
        val myObservableTransFormer = MyObservableTransFormer()
        createObservableFun(1).compose(myObservableTransFormer).subscribe(subscriber)
        createObservableFun(2).compose(myObservableTransFormer).subscribe(subscriber)
        createObservableFun(3).compose(myObservableTransFormer).subscribe(subscriber)
        createObservableFun(4).compose(myObservableTransFormer).subscribe(subscriber)
    }
    class MyObservableTransFormer : ObservableTransformer<Int,Student> {
        override fun apply(upstream: Observable<Int>): ObservableSource<Student> {
            return upstream
                    .lift(observable1)
                    .lift(observable2)
        }

        val observable1 = object : ObservableOperator<Course, Int> {
            override fun apply(observer: Observer<in Course>): Observer<in Int> {
                return object : Observer<Int> {

                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onError(e: Throwable) {
                    }

                    override fun onNext(t: Int) {
                        observer.onNext(Course("courser《$t》"))
                    }

                }
            }
        }
        val observable2 = object : ObservableOperator<Student,Course> {
            override fun apply(observer: Observer<in Student>): Observer<in Course> {
                return object : Observer<Course> {
                    val listCourse = mutableListOf<Course>()
                    var studentFlag = 0

                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(t: Course) {
                        listCourse.add(t)
                        studentFlag++
                        val stu = Student("name$studentFlag", listCourse)
                        observer.onNext(stu)
                        listCourse.clear()
                    }

                    override fun onError(e: Throwable) {

                    }

                }
            }

        }
    }

    /**
     * 变换的原理，所有变换都是基于lift的
     * lift() 过程，有点像一种代理机制，通过事件拦截和处理实现事件序列的变换。
     */
    private fun rxjava_lift() {
        Observable.just("name1","name2","name3")
                .lift(object : ObservableOperator<Student,String> {
                    override fun apply(observer: Observer<in Student>): Observer<in String> {

                        return object : Observer<String> {
                            override fun onComplete() {

                            }

                            override fun onSubscribe(d: Disposable) {

                            }

                            override fun onNext(t: String) {
                                observer.onNext(Student(t, null))

                            }

                            override fun onError(e: Throwable) {

                            }

                        }
                    }

                })
                .subscribe(object : Consumer<Student> {
                    override fun accept(t: Student?) {
                        LogUtils.i("通过lift转换类型，再走next方法，student is ${t?.name}")
                        handler.sendMessage(handler.obtainMessage(7,"通过lift转换类型，再走next方法，student is ${t?.name}"))
                    }

                })
    }

    /**
     * 希望 Subscriber 中直接传入单个的 Course 对象呢（这对于代码复用很重要）？用 map() 显然是不行的，因为 map() 是一对一的转化，而我现在的要求是一对多的转化。那怎么才能把一个 Student 转化成多个 Course 呢？
    这个时候，就需要用 flatMap() 了
     */
    private fun rxjava_flatMap() {

        val listStudent = mutableListOf<Student>()
        for (i in 1..4) {
            val listCourse = mutableListOf<Course>()
            for (j in 0..i) {
                listCourse.add(Course("student$i.course$j"))
            }

            val student = Student("student$i",listCourse)
            listStudent.add(student)
        }
        LogUtils.i(Arrays.toString(listStudent.toTypedArray()))
        Observable.fromIterable(listStudent)
                .flatMap(object : Function<Student, Observable<Course>> {
                    override fun apply(t: Student): Observable<Course> {
                        return Observable.fromIterable(t.list)
                    }

                })
                .subscribe(object : Consumer<Course> {
                    override fun accept(t: Course?) {
                        LogUtils.i(t?.subName)
                    }

                })
    }


    /**
     * 可以看到，map() 方法将参数中的 String 对象转换成一个 Bitmap 对象后返回，而在经过 map() 方法后，事件的参数类型也由 String 转为了
     * Bitmap。这种直接变换对象并返回的，是最常见的也最容易理解的变换。
     */
    private fun rxjavaChange() {
        Observable.just("java","php")
                .map(object : Function<String,TextView> {
                    override fun apply(t: String): TextView {
                        val tvResult = TextView(this@SampleRxjavaActivity)
                        return tvResult
                    }
                })
                .subscribe(object : Consumer<TextView> {
                    override fun accept(t: TextView?) {
                        handler.sendMessage(handler.obtainMessage(5,"map(): 事件对象的直接变换。它是 RxJava 最常用的变换,${t?.text}"))
                    }

                })


    }

    private fun rxjavaSchedule() {
        /*在RxJava 中，Scheduler ——调度器，相当于线程控制器，RxJava 通过它来指定每一段代码应该运行在什么样的线程。RxJava 已经内置了几个 Scheduler ，它们已经适合大多数的使用场景：
Schedulers.immediate(): 直接在当前线程运行，相当于不指定线程。这是默认的 Scheduler。
Schedulers.newThread(): 总是启用新线程，并在新线程执行操作。
Schedulers.io(): I/O 操作（读写文件、读写数据库、网络信息交互等）所使用的 Scheduler。行为模式和 newThread() 差不多，区别在于 io() 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率。不要把计算工作放在 io() 中，可以避免创建不必要的线程。
Schedulers.computation(): 计算所使用的 Scheduler。这个计算指的是 CPU 密集型计算，即不会被 I/O 等操作限制性能的操作，例如图形的计算。这个 Scheduler 使用的固定的线程池，大小为 CPU 核数。不要把 I/O 操作放在 computation() 中，否则 I/O 操作的等待时间会浪费 CPU。
另外， Android 还有一个专用的 AndroidSchedulers.mainThread()，它指定的操作将在 Android 主线程运行。
有了这几个 Scheduler ，就可以使用 subscribeOn() 和 observeOn() 两个方法来对线程进行控制了。 * subscribeOn(): 指定 subscribe() 所发生的线程，即 Observable.OnSubscribe 被激活时所处的线程。或者叫做事件产生的线程。 * observeOn(): 指定 Subscriber 所运行在的线程。或者叫做事件消费的线程。*/
        Observable.create(object : ObservableOnSubscribe<Int> {
            override fun subscribe(emitter: ObservableEmitter<Int>) {
                LogUtils.i("使用Schedule控制rxjava异步操作，subscribe订阅事件产生的线程为：${Thread.currentThread().name}")
                handler.sendMessage(handler.obtainMessage(4,"使用Schedule控制rxjava异步操作，subscribe订阅事件产生的线程为：${Thread.currentThread().name}"))
                emitter.onNext(1)
                emitter.onComplete()
            }

        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Consumer<Int> {
                    override fun accept(t: Int?) {
                        LogUtils.i("next$t 消费线程为：${Thread.currentThread()}")
                    }

                },object:Consumer<Throwable>{
                    override fun accept(t: Throwable?) {
                        LogUtils.i("error消费线程为：${Thread.currentThread().name}")
                    }

                }, object : Action {
                    override fun run() {
                        LogUtils.i("complete消费线程为：${Thread.currentThread().name}")
                        handler.sendMessage(handler.obtainMessage(4,"使用Schedule控制rxjava异步操作，complete消费线程为：${Thread.currentThread().name}"))
                    }

                })

    }

    /**
     * 自动构建observable订阅自动构建observer
     */
    private fun rxjavaCreateObserverAndCreateObservableAuto() {
        Observable.fromArray("Apple","Oragin").subscribe(object : Consumer<String> {
            override fun accept(t: String?) {
                LogUtils.i("自动构建observable订阅自动构建observer，拿到了数据 $t")
                handler.sendMessageDelayed(handler.obtainMessage(3, "自动构建observable订阅自动构建observer，拿到了数据 $t"), 500)
            }
        })
    }

    /**
     * rxjava通过操作符，自动构建observable（被观察者）
     */
    private fun rxjavaCreateObservableAuto() {
        //传统创建观察者，
        val observer1 = object : Observer<String> {
            /**
             * next都执行完的时候被调用
             */
            override fun onComplete() {
                LogUtils.i("onComplete() 自动构建observable")
                handler.sendMessage(handler.obtainMessage(2,"onComplete() 自动构建observable"))
            }


            override fun onSubscribe(d: Disposable) {
                LogUtils.i("onSubscribe()")
            }

            override fun onNext(t: String) {
                LogUtils.i("onNext - $t")
            }

            /**
             * 执行过程中出现错误的时候被调用，与onComplete函数，只能调用其中一个
             */
            override fun onError(e: Throwable) {
                LogUtils.i("onError")
            }
        }

        Observable.fromArray("Apple","Oragin","Bannana","Peer").subscribe(observer1)
    }

    /**
     * 快速构建一个observer
     */
    private fun rxjavaCreateObserverAuto() {

        //被观察者
        val observable = Observable.create(object : ObservableOnSubscribe<String> {
            override fun subscribe(emitter: ObservableEmitter<String>) {
                emitter.onNext("1")
            }

        })

        //被观察者订阅观察者
        observable.subscribe(object : Consumer<String> {
            override fun accept(t: String?) {
                LogUtils.i("快速创建一个观察者 ，仅仅实现accept()函数即可，相当于Observer的next()函数，拿到了数据 $t")
                handler.sendEmptyMessage(1)
            }

        })

    }

    /**
     * rxjava框架作用是异步，原理是观察者模式
     */
    private fun simpleRxjava() {

        //创建观察者，
        val observer = object : Observer<String> {
            /**
             * next都执行完的时候被调用
             */
            override fun onComplete() {
                LogUtils.i("onComplete()")
                handler.sendEmptyMessage(0)
            }


            override fun onSubscribe(d: Disposable) {
                LogUtils.i("onSubscribe()")
            }

            override fun onNext(t: String) {
                LogUtils.i("onNext - $t")
            }

            /**
             * 执行过程中出现错误的时候被调用，与onComplete函数，只能调用其中一个
             */
            override fun onError(e: Throwable) {
                LogUtils.i("onError")
            }

        }

        //被观察者
        val observable = Observable.create(object : ObservableOnSubscribe<String> {
            override fun subscribe(emitter: ObservableEmitter<String>) {
                emitter.onNext("1")
                emitter.onNext("2")
                emitter.onNext("3")
                emitter.onComplete()
            }

        })

        //被观察者订阅观察者
        observable.subscribe(observer)
    }


    class Course{
        var subId:Int = 0
        lateinit var subName:String
        constructor(subName:String){
            this.subName = subName
        }

    }
    class Student(var name:String, var list: MutableList<Course>?) {
    }
}
