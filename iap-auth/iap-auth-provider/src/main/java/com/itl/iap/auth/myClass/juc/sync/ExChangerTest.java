package com.itl.iap.auth.myClass.juc.sync;

import java.util.concurrent.*;

/**
 * @author cch
 * @date 2021/8/9$
 * @since JDK1.8
 */
public class ExChangerTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        final Phaser phaser = new Phaser(10);//初始10
        phaser.awaitAdvance(phaser.getPhase());//主线程调用阻塞在这awaitAdvance表示等待当前phase(当前同步点)完成
        phaser.arrive();//10个worker线程,完成工作调用arrive,表示到达同步点
        phaser.register();//注册一个
        phaser.bulkRegister(10);//注册多个


        final ArrayBlockingQueue arrayBlockingQueue = new ArrayBlockingQueue<String>(1);

        Phaser root = new Phaser(2);
        Phaser c1 = new Phaser(root,3);
        Phaser c2 = new Phaser(root,2);
        Phaser c3 = new Phaser(c1,0);

        root.getRegisteredParties();
        root.getUnarrivedParties();

        final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1,1,1,TimeUnit.SECONDS,new ArrayBlockingQueue<>(1));


//        threadPoolExecutor.execute();

        final Future<String> callable = threadPoolExecutor.submit(() -> {
            System.out.println("Callable");
            return "123";
        });

        final String s = callable.get();

        threadPoolExecutor.shutdownNow();
        try {
            boolean loop = true;
            do {
                loop = !threadPoolExecutor.awaitTermination(10,TimeUnit.SECONDS);
            }while (loop);
        }catch (Exception e){

        }


        ExecutorService executor = Executors.newCachedThreadPool();
        final Exchanger exchanger = new Exchanger();

        executor.execute(new Runnable() {
            String data1 = "A";

            @Override
            public void run() {
                nbaTrade(data1, exchanger);
            }
        });
        executor.execute(new Runnable() {
            String data1 = "B";

            @Override
            public void run() {
                nbaTrade(data1, exchanger);
            }
        });
        executor.execute(new Runnable() {
            String data1 = "C";

            @Override
            public void run() {
                nbaTrade(data1, exchanger);
            }
        });
        executor.execute(new Runnable() {
            String data1 = "D";

            @Override
            public void run() {
                nbaTrade(data1, exchanger);
            }
        });
        executor.shutdown();
    }

    private static void nbaTrade(String data1, Exchanger exchanger) {
        try {
            System.out.println(Thread.currentThread().getName() + "在交易截止之前把 " + data1 + " 交易出去");
            Thread.sleep((long) (Math.random() * 1000));
            String data2 = (String) exchanger.exchange(data1);
            System.out.println(Thread.currentThread().getName() + "交易得到" + data2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
