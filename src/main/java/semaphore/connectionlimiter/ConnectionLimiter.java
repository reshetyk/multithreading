package semaphore.connectionlimiter;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Semaphore;

/**
 * @author Alexey
 */
public abstract class ConnectionLimiter {
    private final Semaphore semaphore;
    protected URL url;

    public ConnectionLimiter(int maxConcurrentRequests, URL url) {
        this.url = url;
        semaphore = new Semaphore(maxConcurrentRequests);
    }

    public void openConnection() throws InterruptedException, IOException {
        try {
            semaphore.acquire();
            System.out.println("Thread " + Thread.currentThread().getName() + " is acquire URL = " + url);
            doWork();
        } finally {
            semaphore.release();

            System.out.println("Thread " + Thread.currentThread().getName() + " is release URL = " + url);
        }
    }

    protected abstract void doWork();
}
