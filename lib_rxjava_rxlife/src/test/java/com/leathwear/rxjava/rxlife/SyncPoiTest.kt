package com.leathwear.rxjava.rxlife

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.observers.TestObserver
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.TimeUnit

/**
 * Created by zpf on 2022/12/20.
 */

class SyncPoiTest {

    @Test
    fun syncPoiDataIntervalTest() {

        val testScheduler = TestScheduler()
        val testObserver = TestObserver<Long>()

        Observable.intervalRange(1, 3, 1, 1, TimeUnit.MICROSECONDS, testScheduler)
            .flatMap {
                Observable.just(11L)
            }
            .subscribe(testObserver)

        testScheduler.advanceTimeBy(3, TimeUnit.MICROSECONDS)

        testObserver.assertValueCount(3)

        testObserver.assertValues(11L, 11L, 11L)

        testObserver.dispose()
    }

    @Test
    fun syncPoiDataFlatMap() {
        val numbers = listOf<Int>(1, 2, 3)
        val task1 = Observable.create<Int> {
            for (i in numbers) {
                Thread.sleep(1)
                it.onNext(i)
                if (i == 2) {
                    throw java.lang.IllegalArgumentException("i == 2 error")
                }
            }
            it.onComplete()
        }.onErrorComplete()

        val task2 = Observable.create<Int> {
            for (i in numbers) {
                Thread.sleep(1)
                it.onNext(i + 3)
            }
            it.onComplete()
        }

        val taskResult = mutableListOf<Int>()

        task1.flatMap {
            println("flatmap : $it")
            task2
        }.subscribe({
            taskResult.add(it)
            println("flatmap result: $it")
        }, {
            println("flatmap result: $it")
        })

        Assert.assertArrayEquals(intArrayOf(1, 2, 3, 4, 5, 6), taskResult.toIntArray())
    }

    @Test
    fun syncDataConcatTest() {
        val numbers = listOf<Int>(1, 2, 3)
        val task1 = Observable.create<Int> {
            for (i in numbers) {
                Thread.sleep(1000)
                it.onNext(i)
                if (i == 2) {
                    throw java.lang.IllegalArgumentException("i == 2 error")
                }
            }
            it.onComplete()
        }

        val task2 = Observable.create<Int> {
            for (i in numbers) {
                Thread.sleep(1000)
                it.onNext(i + 3)
            }
            it.onComplete()
        }

        val task2Result = mutableListOf<Int>()

        Observable.concatDelayError(listOf(task1, task2))
            .subscribe(
                {
                    task2Result.add(it)
                    println("concat : $it")
                },
                {
                    println("concat : $it")
                }
            )
        Assert.assertArrayEquals(task2Result.toIntArray(), intArrayOf(1, 2, 3))
    }

}