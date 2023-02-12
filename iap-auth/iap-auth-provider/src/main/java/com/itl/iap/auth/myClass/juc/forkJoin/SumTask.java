package com.itl.iap.auth.myClass.juc.forkJoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * @author cch
 * @date 2021/8/4$
 * @since JDK1.8
 */
public class SumTask extends RecursiveTask<Long> {
    public static final int THRESHOLD = 100;
    private long start;
    private long end;

    public SumTask(long n) {
        this(1, n);
    }

    public SumTask() {
    }

    public SumTask(long start, long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        long sum = 0;
        if ((end - start) <= THRESHOLD) {
            for (long l = start; l <= end; l++) {
                sum += l;
            }
        } else {
            long mid = (start + end) >>> 1;
            SumTask left = new SumTask(start, mid);
            SumTask right = new SumTask(mid + 1, end);

            left.fork();
            right.fork();
            sum = left.join() + right.join();
        }
        return sum;
    }

    public void testSum(long l) throws ExecutionException, InterruptedException {
        final SumTask sumTask = new SumTask(l);
        final ForkJoinPool forkJoinPool = new ForkJoinPool();
        final ForkJoinTask<Long> submit = forkJoinPool.submit(sumTask);
        System.out.println(submit.get());//获取返回值
        forkJoinPool.shutdown();
    }
    //        new SumTask().testSum(1000);
//        for (int i = 0; i < 10; i++) {
//            System.out.println("---------");
//            extracted();
//        }
    public static void main(String[] args) {
        final Thread thread = new Thread(() -> {
            while (true) {
                System.out.println("thread");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "thread1");
        thread.setDaemon(true);//主线程推出 thread1 也退出
        thread.start();
    }

    private static void extracted() {
        long a = 123789680213487123L;
        long b = 832742811892374377L;

        final long l = System.nanoTime();
        final long l1 = (a + b) >>> 10;
        System.out.println(System.nanoTime() - l);
//        System.out.println(l1);

        final long ln = System.nanoTime();
        final long l3 = (a + b) / 1024;
        System.out.println(System.nanoTime() - ln);
//        System.out.println(l3);
    }
}
