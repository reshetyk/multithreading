package synchronizers.cyclicbarrier.currencycollector;

import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @author Alexey
 */
public class CurrencyCollector implements Runnable{
    private CyclicBarrier cyclicBarrier;
    private List<Currency> resultStore;

    public CurrencyCollector(CyclicBarrier cyclicBarrier, List<Currency> resultStore) {
        this.cyclicBarrier = cyclicBarrier;
        this.resultStore = resultStore;
    }

    private Currency richCurrency() {
        return new Currency("USD", new Random().nextDouble() * 100);
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " trying to get currency");
        try {
            Thread.currentThread().sleep(new Random().nextInt(5000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Currency result = richCurrency();
        resultStore.add(result);
        System.out.println(Thread.currentThread().getName() + " got currency: " + result);
        try {
            cyclicBarrier.await();
            System.out.println(Thread.currentThread().getName()  + "done ");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}