## Rxjava RxAndroid

### 简介

Rxjava 是Java VM 响应式扩展的实现。是一个通过可观察的 事件序列 来处理 异步 和 事件驱动(响应式)的库。

Rxjava 扩展了观察者模式以支持数据/事件序列，并添加了运算符，使您可以声明性地将序列组合在一起，同时消除了对低级线程，同步，
线程安全和并发数据结构等问题的担忧。

Rxjava 关键对象：
可观察者
操作符
观察者

Rxjava3关键类：

```
io.reactivex.rxjava3.core.Flowable: 0..N flows, supporting Reactive-Streams and backpressure
io.reactivex.rxjava3.core.Observable: 0..N flows, no backpressure,
io.reactivex.rxjava3.core.Single: a flow of exactly 1 item or an error,
io.reactivex.rxjava3.core.Completable: a flow without items but only a completion or error signal,
io.reactivex.rxjava3.core.Maybe: a flow with no items, exactly one item or an error.
```

### 1.创建操作符
|操作符|参数|作用|可用对象|
|-|-|-| -|
|just|多参数|按照参数顺序一次发射数据|Flowable,Observable,Maybe,Single,Completable|
|fromIterable|Java迭代器|依次发送迭代器数据|Flowable,Observable,Maybe,Single,Completable|
|fromArray|数组|依次发送数组数据|Flowable,Observable|
|fromCallable|Java Callable<T>对象|发送Callable返回的数据|Flowable,Observable,Maybe,Single,Completable|
|fromAction|Rxjava Action对象|Action方法调用时发送数据|Maybe,Completable|
|fromRunnable|Rxjava Action对象|Action方法调用时发送数据|Maybe,Completable|

