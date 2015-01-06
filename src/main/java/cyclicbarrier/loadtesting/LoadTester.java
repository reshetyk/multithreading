package cyclicbarrier.loadtesting;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @author Alexey
 */
public class LoadTester implements Runnable{
    private static final Random RANDOM = new Random(1000);
    private final CyclicBarrier cyclicBarrier;

    public LoadTester(CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
    }

    protected void doRequest() {
        System.out.println(Thread.currentThread().getName() + " do request; " + new Timestamp(new Date().getTime()));
    }

    @Override
    public void run() {
        sleep(RANDOM.nextInt(4000));
        System.out.println(Thread.currentThread().getName() + " opening connection; " + new Timestamp(new Date().getTime()));

        await();

        sleep(RANDOM.nextInt(4000));
        doRequest();
    }

    private void sleep(int millis) {
        try {
            Thread.currentThread().sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void await() {
        try {
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("Main starts");
        final int parties = 10;
        final CyclicBarrier barrier = new CyclicBarrier(parties, new Runnable() {
            @Override
            public void run() {
                System.out.println("all threads are ready to do request");
            }
        });

        for (int i = 0; i < parties; i++) {
            new Thread(new LoadTester(barrier)).start();
        }
        System.out.println("Main ends");
    }
}
