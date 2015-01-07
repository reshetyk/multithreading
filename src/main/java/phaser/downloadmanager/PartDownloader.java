package phaser.downloadmanager;

import java.net.URL;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Phaser;

/**
 * @author Alexey
 */
public class PartDownloader implements Runnable {
    private final Part part;
    private final Phaser phaser;

    public PartDownloader(Part part, Phaser phaser) {
        this.part = part;
        this.phaser = phaser;
    }

    protected void downloadPart() throws InterruptedException, BrokenBarrierException {
        System.out.println("downloading " + part);
        Thread.currentThread().sleep(new Random().nextInt(6000));
        System.out.println(part + " is downloaded");

        phaser.arriveAndDeregister();
    }


    @Override
    public void run() {
        try {
            downloadPart();
        } catch (InterruptedException | BrokenBarrierException e) {
            makePartSnapshot();
            System.out.println("downloading was interrupted");
        }

    }

    private void makePartSnapshot() {
        System.out.println("snapshot for " + this + " done");
    }

    public static class Part {
        private URL url;
        private int from;
        private int to;

        public Part(URL url, int from, int to) {
            this.url = url;
            this.from = from;
            this.to = to;
        }

        @Override
        public String toString() {
            return "Part{" +
                    "from=" + from +
                    ", to=" + to +
                    '}';
        }

    }
}
