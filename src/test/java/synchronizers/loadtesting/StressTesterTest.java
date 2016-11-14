package synchronizers.loadtesting;

import org.junit.Test;
import synchronizers.cyclicbarrier.loadtesting.StressTester;

import java.util.concurrent.CyclicBarrier;

public class StressTesterTest {

    @Test
    public void testDoRequest() throws Exception {
        System.out.println("Main starts");
        final int parties = 9;
        final CyclicBarrier barrier = new CyclicBarrier(parties, new Runnable() {
            @Override
            public void run() {
                System.out.println("all threads are ready to do request");
            }
        }
        );

        for (int i = 1; i <= 10; i++) {
            new Thread(new StressTester(barrier)).start();
        }
        Thread.currentThread().join(11000);

        //no need to reset barrier, we can use it again

        for (int i = 0; i < parties; i++) {
            new Thread(new StressTester(barrier)).start();
        }
        Thread.currentThread().join(11000);
        System.out.println("Main ends");
    }
}