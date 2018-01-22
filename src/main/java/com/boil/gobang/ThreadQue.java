package com.boil.gobang;

import java.util.concurrent.LinkedBlockingQueue;

public class ThreadQue {

    private LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();

    public void start() throws InterruptedException {
        new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Runnable runable = queue.take();
                        runable.run();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void put(Runnable r) throws InterruptedException {
        queue.put(r);
    }


}
