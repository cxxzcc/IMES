package com.itl.iap.auth.myClass.juc.forkJoin;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author cch
 * @date 2021/8/4$
 * @since JDK1.8
 */
public class SortTask extends RecursiveAction {

    final long[] array;
    final int lo;
    final int hi;

    private int THRESHOLD = 0; //For demo only

    public SortTask(long[] array) {
        this.array = array;
        this.lo = 0;
        this.hi = array.length - 1;
    }

    public SortTask(long[] array, int lo, int hi) {
        this.array = array;
        this.lo = lo;
        this.hi = hi;
    }

    @Override
    protected void compute() {

        if (lo < hi) {
            int pivot = partition(array, lo, hi); //划分
            SortTask left = new SortTask(array, lo, pivot - 1);
            SortTask right = new SortTask(array, pivot + 1, hi);
            left.fork();
            right.fork();
            left.join();
            right.join();
        }
    }

    private int partition(long[] array, int lo, int hi) {
        long x = array[hi];
        int i = lo - 1;
        for (int j = lo; j < hi; j++) {
            if (array[j] <= x) {
                i++;
                swap(array, i, j);
            }
        }
        swap(array, i + 1, hi);
        return i + 1;
    }

    private void swap(long[] array, int i, int j) {
        if (i != j) {
            long temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }

    public void test() {
        final SortTask sortTask = new SortTask(array);//一个任务
        final ForkJoinPool forkJoinPool = new ForkJoinPool();//一个ForkJoinPool
        forkJoinPool.submit(sortTask);//提交任务
        forkJoinPool.shutdown();//结束 开启多线程执行子任务
        forkJoinPool.awaitQuiescence(30, TimeUnit.SECONDS);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long[] a = new long[]{1, 2, 3, 6, 4, 2, 5, 3, 7, 8};
        final SortTask sortTask = new SortTask(a);
        sortTask.test();
        System.out.println(Arrays.toString(a));

        new SumTask().testSum(1000);
        final ReentrantLock reentrantLock = new ReentrantLock();
        reentrantLock.tryLock();
        reentrantLock.unlock();
        reentrantLock.newCondition().signal();
    }
}
