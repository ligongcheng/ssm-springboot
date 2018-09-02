package cn.it.ssm.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;


public class ThreadTest implements Runnable {

    private static final BlockingQueue<Integer> blockingDeque = new LinkedBlockingQueue<>(1000);

    private static final AtomicInteger atomicInteger = new AtomicInteger(0);

    private int type;

    public ThreadTest(int type) {
        this.type = type;
    }

    @Override
    public void run() {

        if (type == 0){
            while (true) {
                int i = atomicInteger.incrementAndGet();
                try {
                    blockingDeque.put(i);
                    System.out.println("put " + i);
                    //Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else if (type == 1){
            while (true) {
                try {
                    Integer take = blockingDeque.take();
                    System.out.println("take " + take);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService producerThreadPool = Executors.newFixedThreadPool(5);
        ExecutorService consumerThreadPool = Executors.newFixedThreadPool(1);
        ThreadTest pro = new ThreadTest(0);
        ThreadTest con = new ThreadTest(1);
        for (int i = 0; i < 5; i++) {
            producerThreadPool.execute(pro);
        }
        for (int j = 0; j < 1; j++) {
            consumerThreadPool.execute(con);
        }
    }
}
