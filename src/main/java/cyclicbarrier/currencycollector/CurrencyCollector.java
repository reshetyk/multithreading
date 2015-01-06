package cyclicbarrier.currencycollector;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;

/**
 * @author Alexey
 */
public class CurrencyCollector implements Callable<Currency>{
    private CyclicBarrier cyclicBarrier;

    public CurrencyCollector(CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
    }

    private Currency richCurrency() {
        return new Currency("USD", new Random().nextDouble() * 100);
    }

    @Override
    public Currency call() throws Exception {
        System.out.println(Thread.currentThread().getName() + " trying to get currency");
        try {
            Thread.currentThread().sleep(new Random().nextInt(5000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Currency result = richCurrency();

        System.out.println(Thread.currentThread().getName() + " got currency: " + result);
        try {
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        return result;
    }
}