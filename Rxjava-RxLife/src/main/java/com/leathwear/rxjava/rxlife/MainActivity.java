package com.leathwear.rxjava.rxlife;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableEmitter;
import io.reactivex.rxjava3.core.FlowableOnSubscribe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;

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

//        Flowable.interval(1, TimeUnit.SECONDS).subscribe((aLong) -> {
//            Log.i(TAG, "accept is : "+aLong);
//            Log.i(TAG, "accept: "+aLong);
//        });


    }
}