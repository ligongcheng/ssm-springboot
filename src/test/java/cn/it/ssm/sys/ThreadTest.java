package cn.it.ssm.sys;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadTest implements Callable {
    volatile static AtomicInteger i = new AtomicInteger(1);

    public static void main(String[] args) throws Exception {

        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        Future<AtomicInteger> future = threadPool.submit(() -> {
                    i.incrementAndGet();
                    System.out.println(Thread.currentThread().getName());
                    //Thread.sleep(3000);
                    return i;
                }
        );
        try {
            System.out.println(future.get(2, TimeUnit.SECONDS).get());
        } catch (Exception e) {
            e.printStackTrace();
        }

        threadPool.shutdown();
    }

    @Override
    public Object call() throws Exception {

        return "111111";
    }


}
