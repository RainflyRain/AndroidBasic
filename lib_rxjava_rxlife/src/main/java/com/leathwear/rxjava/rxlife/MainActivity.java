package com.leathwear.rxjava.rxlife;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Callable;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableEmitter;
import io.reactivex.rxjava3.core.FlowableOnSubscribe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";

    class HandlerException extends Exception{
        public HandlerException(String message) {
            super(message);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        basicMethod();

        backPress();
    }

    private void basicMethod() {
        Observable.create((ObservableOnSubscribe<String>) emitter -> {
            emitter.onNext("hello emitter");
            emitter.onError(new HandlerException("handle error"));
        })
        .subscribe(s -> Log.i(TAG, "onCreate: "+s),e-> Log.i(TAG, "onCreate: "+e.getMessage()));



        Observable.just("hello").subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull String s) {

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        Callable callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                Log.i(TAG, "call: hello callable");
                return "hello callable";
            }
        };
        Observable.fromCallable(callable)
            .subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Throwable {
                Log.i(TAG, "accept: "+s);
            }
        });

        //简单发送
        Flowable.just("hello world").subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Throwable {
                Log.i(TAG, "accept: "+s);
            }
        });

        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<String> emitter) throws Throwable {
                emitter.onNext("hello backpress");
            }
        }, BackpressureStrategy.LATEST)
        .subscribe(s -> Log.i(TAG, "accept: "+s));
    }

    private void backPress(){
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> emitter) throws Throwable {
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(10);
                    emitter.onNext(i);
                }
            }
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.i(TAG, "onSubscribe: ");
            }

            @Override
            public void onNext(@NonNull Object o) {
                try {
                    Thread.sleep(5000);
                    Log.i(TAG, "onNext: "+o);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.i(TAG, "onError: ");
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete: ");
            }
        });
    }
}