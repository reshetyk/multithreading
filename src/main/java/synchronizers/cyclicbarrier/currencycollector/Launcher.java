package synchronizers.cyclicbarrier.currencycollector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static java.util.stream.Collectors.averagingDouble;

/**
 * @author Alexey
 */
public class Launcher {
    public static void main(String[] args) throws InterruptedException, BrokenBarrierException, ExecutionException {
        final List<CurrencyCollector> currencyCollectors = new ArrayList<CurrencyCollector>();
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(4, new Runnable() {
            @Override
            public void run() {
                System.out.println("complete");

            }
        });
        List<Currency> resultStore = new CopyOnWriteArrayList<>();
        currencyCollectors.add(new CurrencyCollector(cyclicBarrier, resultStore));
        currencyCollectors.add(new CurrencyCollector(cyclicBarrier, resultStore));
        currencyCollectors.add(new CurrencyCollector(cyclicBarrier, resultStore));

        ExecutorService executorService = Executors.newFixedThreadPool(3);

        currencyCollectors.stream().forEach(c -> executorService.execute(c));

        cyclicBarrier.await();

        System.out.println("average is " + resultStore.stream().collect(averagingDouble(c -> c.getValue())));

        executorService.shutdown();
    }
}