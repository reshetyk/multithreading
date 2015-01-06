package cyclicbarrier.currencycollector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author Alexey
 */
public class Launcher {
    public static void main(String[] args) throws InterruptedException, BrokenBarrierException, ExecutionException {
        final List<CurrencyCollector> currencyCollectors = new ArrayList<CurrencyCollector>();
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(3, new Runnable() {
            @Override
            public void run() {
                System.out.println("complete");

            }
        });
        currencyCollectors.add(new CurrencyCollector(cyclicBarrier));
        currencyCollectors.add(new CurrencyCollector(cyclicBarrier));
        currencyCollectors.add(new CurrencyCollector(cyclicBarrier));

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(3);
//        executorService.invokeAll()
        final List<Future<Currency>> futureList = executorService.invokeAll(currencyCollectors);

        final ArrayList<Currency> currencies = new ArrayList<Currency>();
        for (Future<Currency> currencyFuture : futureList) {
            currencies.add(currencyFuture.get());
        }

//        cyclicBarrier.await();
        System.out.println("average is " + CurrencyCalculator.calcAverage(currencies));
    }
}