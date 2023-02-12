package com.itl.iap.auth.myClass.juc.lock;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.StampedLock;

/**
 * @author cch
 * @date 2021/8/6$
 * @since JDK1.8
 */
public class StampedLockTest {
    private double x, y;
    private final StampedLock stampedLock = new StampedLock();
    void move(double x, double y) {
        long stamp = stampedLock.writeLock();
        try {
            this.x += x;
            this.y += y;
        } finally {
            stampedLock.unlockWrite(stamp);
        }
    }
    double distanceFromOrigin() {//多线程调用 求距离
        long l = stampedLock.tryOptimisticRead();//乐观读
        double currentX = x, currentY = y;//将共享变量拷贝到线程栈
        if (!stampedLock.validate(l)) {//读期间有其他线程修改数据
            l = stampedLock.readLock();//脏读, 升级为悲观锁
            try {
                currentX += x;
                currentY += y;
            } finally {
                stampedLock.unlockRead(l);
            }
        }
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }

    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
        final Semaphore semaphore = new Semaphore(10,true);
        semaphore.acquire();
        semaphore.release();

        final CountDownLatch countDownLatch = new CountDownLatch(10);
        countDownLatch.await();
        countDownLatch.countDown();

        final CyclicBarrier cyclicBarrier = new CyclicBarrier(10);
        cyclicBarrier.await();
    }
}
