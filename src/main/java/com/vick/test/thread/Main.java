package com.vick.test.thread;

import java.util.concurrent.*;

public class Main {
    public static void main(String... args){
        CyclicBarrier barrier = new CyclicBarrier(5, () -> System.out.println("人到齐了，开饭啦！！！！！"));
        ExecutorService service = Executors.newFixedThreadPool(5);
        service.submit(new Runner("小明",barrier));
        service.submit(new Runner("小华",barrier));
        service.submit(new Runner("小李",barrier));
        service.submit(new Runner("小张",barrier));
        service.submit(new Runner("小毕",barrier));
        service.shutdown();
        System.out.println("主线程结束");
    }
}
class Runner implements Runnable{
    private String name;
    private CyclicBarrier barrier;

    Runner(String name, CyclicBarrier barrier) {
        this.name = name;
        this.barrier = barrier;
    }

    @Override
    public void run() {
        System.out.println(name+" has arrived at the scene.");
        try {
            Thread.sleep(5000);
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}

