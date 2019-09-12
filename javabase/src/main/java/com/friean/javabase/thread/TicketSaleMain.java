package com.friean.javabase.thread;

public class TicketSaleMain {

    public static void main(String[] args) {
        Tickets tickets = new Tickets();

        Thread thread1 = new Thread(tickets);
        Thread thread2 = new Thread(tickets);
        Thread thread3 = new Thread(tickets);
        thread1.start();
        thread2.start();
        thread3.start();
    }

}
