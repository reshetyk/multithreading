package cyclicbarrier.loadtesting;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @author Alexey
 */
public class StressTester implements Runnable {
    private static final Random RANDOM = new Random(1000);
    private final CyclicBarrier cyclicBarrier;

    public StressTester(CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public void run() {
        openConnection();
        await(); //<-- barrier (all threads will be wait each other here)
        doRequest();
    }

    private void openConnection() {
        sleep(RANDOM.nextInt(2000));
        System.out.println(Thread.currentThread().getName() + " opened connection and ready; " + new Timestamp(new Date().getTime()));
    }

    protected void doRequest() {
        sleep(RANDOM.nextInt(4000));
        System.out.println(Thread.currentThread().getName() + " do request; " + new Timestamp(new Date().getTime()));
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
}
