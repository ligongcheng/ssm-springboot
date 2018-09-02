package cn.it.ssm.thread;

import com.google.common.util.concurrent.RateLimiter;

public class Limit {

    public static void main(String[] args) {
        RateLimiter limiter = RateLimiter.create(1);
        while (true) {
            try {
                if (limiter.tryAcquire()){
                    System.out.println("1");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
