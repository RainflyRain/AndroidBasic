package com.example.kotlin

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

/**
 * CoroutineScope/GlobalScope：协程作用域，定义协程的执行范围。
 * launch/withContext/coroutineScope：协程构建器,是协程作用域的扩展函数。
 * suspend 修饰符：修饰挂起函数。
 * 挂起函数：不会阻塞线程。但是会挂起协程，并且只能在协程中使用。
 * 切换线程：launch（返回job）、async(返回Deferred<T>)、withContent()
 */
class KotlinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)
        coroutineTest0()
        coroutineTest1()
    }

    private fun coroutineTest0() {
        //runBlocking会阻塞当前线程
        runBlocking {
            delay(2000)
            Log.i(TAG, " word")
        }
        Log.i(TAG, " hello")
    }

    private fun coroutineTest1() {
        runBlocking {
            launch {
                delay(2000)
                Log.i(TAG, " word")
            }
            Log.i(TAG, " hello ")
        }
    }

    /**
     * runBlocking{} 会阻塞线程直到协程内全部子任务执行完成。
     *
     * Runs a new coroutine and blocks the current thread interruptibly until its completion.
     * This function should not be used from a coroutine. It is designed to bridge regular
     * blocking code to libraries that are written in suspending style, to be used in main
     * functions and in tests.
     *
     * runBlocking 运行一个新的协程并且阻塞当前可中断的线程直至该协程运行结束。该函数不应在协程中调用。它被设计用来
     * 桥接常规阻塞代码和以挂起函数风格编写的代码库，以用于主函数和测试。
     *
     */
    private fun coroutineTest() = runBlocking {
        GlobalScope.launch {
            delay(1000)
            Log.i(TAG, "word")
        }
//        var content = 1
//        async {
//            delay(1000)
//            content = 2
//            Log.i(TAG, "num:$content")
//        }
//        launch {
//            delay(1000)
//            content = 3
//            Log.i(TAG, "num:$content")
//        }
//        withContext(Dispatchers.IO){
//            delay(1000)
//            content = 4
//            Log.i(TAG, "num:$content")
//        }
        Log.i(TAG, "hello ")
    }

    /**
     * coroutineScope 不会阻塞当前线程，是一个挂起函数
     */
    private suspend fun coroutineTest2() = coroutineScope {
        GlobalScope.launch {
            delay(1000)
            Log.i(TAG, "word")
        }
        Log.i(TAG, "hello ")
    }


    companion object {
        private const val TAG = "KotlinActivity"
    }

}