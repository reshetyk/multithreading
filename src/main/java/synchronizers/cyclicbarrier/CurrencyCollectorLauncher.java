package synchronizers.cyclicbarrier;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

public class CurrencyCollectorLauncher {
    public static void main(String args[]) {
//        Parser parser = new Parser(3, "");

        for (int i = 0; i < 15; i++) {
//            new Thread(new CurrencyCollector(parser, "Printer" + (i + 1))).start();
        }
    }
}

class Parser implements Runnable{

    private CyclicBarrier queue;
    private String source;
    private Random rand;

    public Parser(CyclicBarrier queue, String source) {
        this.queue = queue;
        this.source = source;
    }

    private void parse() {
        try {
            System.out.println();
            queue.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        parse();
    }

}

class CurrencyCollector implements Runnable {

    private String name;
    private Random rand;
    private Parser parser;

    public CurrencyCollector(Parser parser, String name) {
        this.name = name;
        this.parser = parser;
        this.rand = new Random();
    }

    public void run() {
        try {
            while (true) {
                TimeUnit.SECONDS.sleep(rand.nextInt(10));
                System.out.println(name + " is empty");
//                parser.parse(name);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}