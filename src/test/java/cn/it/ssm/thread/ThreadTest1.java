package cn.it.ssm.thread;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


public class ThreadTest1 {

    private static ArrayList<Integer> arrayList = new ArrayList<>();

    public static void main(String[] args) {
        ExecutorService ThreadPool = Executors.newFixedThreadPool(6);
        pro p = new ThreadTest1().new pro();
        ThreadPool.execute(p);
        ThreadPool.execute(p);
        ThreadPool.execute(p);
        //ThreadPool.execute(p);
        con c = new ThreadTest1().new con();
        ThreadPool.execute(c);
        ThreadPool.execute(c);
        ThreadPool.execute(c);

    }

    public class pro implements Runnable {

        private AtomicInteger atomicInteger = new AtomicInteger(0);

        private int Length = 3;

        @Override
        public void run() {
            try {
                while (true) {
                    synchronized (arrayList) {
                        while (arrayList.size() >= Length) {
                            //System.out.println("pro wait");
                            arrayList.wait();
                        }
                        arrayList.add(atomicInteger.incrementAndGet());
                        System.out.println(Thread.currentThread().getName() + " 生产 " + atomicInteger.get() +
                                " count:"+ arrayList.size());
                        arrayList.notifyAll();

                    }
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class con implements Runnable {

        private AtomicInteger atomicInteger = new AtomicInteger(0);

        //private int Length = 1000;

        @Override
        public void run() {
            try {
                while (true) {
                    synchronized (arrayList) {
                        while (arrayList.size() == 0) {
                            System.out.println(Thread.currentThread().getName() + " con wait");
                            arrayList.wait();
                        }
                        Integer remove = arrayList.remove(0);
                        atomicInteger.incrementAndGet();
                        System.out.println(Thread.currentThread().getName() + "消费 " + atomicInteger.get() +
                                " count:"+ arrayList.size());
                        arrayList.notifyAll();
                    }
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
