package com.leathwear.rxjava.rxlife

import android.util.Log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.processors.PublishProcessor
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.IOException
import java.util.concurrent.Callable


/**
 * Created by zpf on 2022/5/13.
 */
class FlowableExample {

    fun undeliverable(){
        RxJavaPlugins.setErrorHandler { error: Throwable? ->
            Log.i(TAG, "undeliverable: $error")
        }

        val main = PublishProcessor.create<Int>()
        val inner = PublishProcessor.create<Int>()

        // switchMapDelayError will delay all errors
        val ts = main.switchMapDelayError { v: Int? -> inner }.test()

        main.onNext(1)

        // the inner fails
        inner.onError(IOException())

        // the consumer is still clueless
        ts.assertEmpty()

        // the consumer cancels
        ts.cancel()

    }

    /**
     * 基础用法
     */
    fun basicMethod() {
        Observable.create(ObservableOnSubscribe { emitter: ObservableEmitter<String> ->
            emitter.onNext("hello emitter")
            emitter.onError(RuntimeException("handle error"))
        } as ObservableOnSubscribe<String>)
            .subscribe({ s: String -> Log.i(TAG, "onCreate: $s") }) { e: Throwable ->
                Log.i(
                    TAG,
                    "onCreate: " + e.message
                )
            }
        Observable.just("hello").subscribe(object : Observer<String> {
            override fun onSubscribe(d: Disposable) {}
            override fun onNext(s: String) {}
            override fun onError(e: Throwable) {}
            override fun onComplete() {}
        })
        val callable: Callable<*> = Callable {
            Log.i(TAG, "call: hello callable")
            "hello callable"
        }
        Observable.fromCallable(callable)
            .subscribe { s -> Log.i(TAG, "accept: $s") }

        //简单发送
        Flowable.just("hello world").subscribe { s -> Log.i(TAG, "accept: $s") }
        Flowable.create<String>(
            { emitter -> emitter.onNext("hello backpress") },
            BackpressureStrategy.LATEST
        )
            .subscribe { s: String -> Log.i(TAG, "accept: $s") }
    }

    /**
     * 背压
     */
    fun backPress() {
        Observable.create<Any> { emitter ->
            for (i in 0..9) {
                Thread.sleep(10)
                emitter.onNext(i)
            }
        }.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Any> {
                override fun onSubscribe(d: Disposable) {
                    Log.i(TAG, "onSubscribe: ")
                }

                override fun onNext(o: Any) {
                    try {
                        Log.i(TAG, "onNext: $o")
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }

                override fun onError(e: Throwable) {
                    Log.i(TAG, "onError: ")
                }

                override fun onComplete() {
                    Log.i(TAG, "onComplete: ")
                }
            })
    }

    /**
     * Flowable 嵌套
     */
    fun refresh(): Flowable<String> {
        return Flowable.create<String>({
            for (i in 1..20) {
                Log.i(TAG, "refresh create: $i")
                it.onNext("refresh $i")
                Thread.sleep(50)
            }
            it.onComplete()
        }, BackpressureStrategy.LATEST)
    }

    fun loadMore(): Flowable<String> {
        return Flowable.create<String>({
            for (i in 1..20) {
                Log.i(TAG, "loadMore create: $i")
                it.onNext("loadMore $i")
                Thread.sleep(50)
                if (i == 10){
                    5/0
                }
            }
            it.onComplete()
        }, BackpressureStrategy.LATEST)
            .onErrorReturn { "loadMore Error" }
    }

    fun loadNet(): Flowable<String>{
        return Flowable.create<String>({
            for (i in 1..20) {
                Log.i(TAG, "loadNet create: $i")
                it.onNext("loadNet $i")
                Thread.sleep(50)
                if (i == 10){
                    5/0
                }
            }
            it.onComplete()
        }, BackpressureStrategy.LATEST)
            .onErrorReturn { "loadNet Error" }
    }

    fun loadDouble() {
        refresh().flatMap { refreshStr ->
            Log.i(TAG, "loadDouble flatMap: $refreshStr")
            loadMore()
        }.observeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.i(TAG, "loadDouble onNext: $it")
            }, {
                Log.i(TAG, "loadDouble onThrowable: ${it.message}")
            }, {
                Log.i(TAG, "loadDouble: onComplete")
            })
    }

    fun loadDependent(){
        refresh().flatMap {
            loadMore()
        }.flatMap {
            loadNet()
        }.subscribeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.i(TAG, "loadDependent onNext: $it")
            }, {
                Log.i(TAG, "loadDependent onThrowable: ${it.message}")
            }, {
                Log.i(TAG, "loadDependent: onComplete")
            })
    }

    fun syncData(){

        val step1 = Observable.create<String> { e ->
            for (i in 1..20){
                e.onNext(i.toString() +" step1")
                if (i == 20){
                    Thread{
                        Log.i(TAG, "syncData: 同步step1 结束")
                        Thread.sleep(5000)
                        Log.i(TAG, "syncData: syncData: 同步step1 结束2")
                        e.onComplete()
                    }.start()
                }
            }
        }

        val step2 = Observable.create<String> { e ->
            for (i in 1..20){
                e.onNext(i.toString() +" step2")
                if (i == 20){
                    Thread{
                        Log.i(TAG, "syncData: 同步step2 结束")
                        Thread.sleep(5000)
                        Log.i(TAG, "syncData: syncData: 同步step2 结束2")
                        e.onComplete()
                    }.start()
                }
            }
        }

        val step3 = Observable.create<String> { e ->
            for (i in 1..20){
                e.onNext(i.toString()+" step3")
                if (i == 20){
                    Thread{
                        Log.i(TAG, "syncData: 同步step3 结束")
                        Thread.sleep(5000)
                        Log.i(TAG, "syncData: syncData: 同步step3 结束2")
                        e.onComplete()
                    }.start()
                }
            }
        }

        val step4 = Observable.create<String> { e ->
            for (i in 1..20){
                e.onNext(i.toString() +" step4")
                if (i == 20){
                    Thread{
                        Log.i(TAG, "syncData: 同步step4 结束")
                        Thread.sleep(5000)
                        Log.i(TAG, "syncData: syncData: 同步step4 结束2")
                        e.onComplete()
                    }.start()
                }
            }
        }

        val listObservables = listOf(step1,step2,step3,step4)

        Observable.concat<String>(listObservables)
            .takeUntil{
                val finished = it.equals("19 step1")
                if (finished){
                    Log.i(TAG, "takeUntil subscribe: $finished , $it")
                }
                finished
            }
            .subscribeOn(Schedulers.newThread())
            .subscribe({
                Log.i(TAG, "subscribe: 同步成功 $it")
            },{
                Log.i(TAG, "subscribe: 异常")
            })

    }

    companion object{
        private const val TAG = "FlowableExample"
    }
}