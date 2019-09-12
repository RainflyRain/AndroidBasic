package com.friean.javabase.thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 模拟电影票售卖窗口售卖电影票
 */
public class Tickets implements Runnable {

    //电影票的剩余数量
    private int ticketsNum = 100;

    Object obj = new Object();

    /**
     * true ： 公平锁，每个线程有公平的执行权
     * false : 非公平锁，线程独占执行权
     */
    Lock lock = new ReentrantLock(true);

    @Override
    public void run() {

       while (true){
           // 第一种 同步代码块
           //此处如果不加锁，会出现线程不安全问题
//           synchronized (obj){
//               if (ticketsNum > 0){//是否有票
//                   try {
//                       Thread.sleep(100);//模拟电影票售卖过程
//                   } catch (InterruptedException e) {
//                       e.printStackTrace();
//                   }
//                   //打印当前售出的电影票数和线程名
//                   String name = Thread.currentThread().getName();
//                   System.out.println("线程窗口"+name+"销售电影票："+ticketsNum--);
//               }else {
//                   return;
//               }
//           }

           //第二种方法 同步方法
//           saleTickets();

           //第三种方法 同步锁
           try {
               lock.lock();
               if (ticketsNum > 0){//是否有票
                   try {
                       Thread.sleep(100);//模拟电影票售卖过程
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
                   //打印当前售出的电影票数和线程名
                   String name = Thread.currentThread().getName();
                   System.out.println("线程窗口"+name+"销售电影票："+ticketsNum--);
               }else {
                   return;
               }
           } finally {
               lock.unlock();
           }

       }
    }


    /**
     * 如果是非静态方法，锁对象为调用该方法的对象
     *
     * 如果是静态方法，锁对象为该方法的类class对象
     */
    private synchronized void saleTickets(){
        if (ticketsNum > 0){//是否有票
            try {
                Thread.sleep(100);//模拟电影票售卖过程
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //打印当前售出的电影票数和线程名
            String name = Thread.currentThread().getName();
            System.out.println("线程窗口"+name+"销售电影票："+ticketsNum--);
        }else {
            return;
        }
    }
}
