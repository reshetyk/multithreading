package synchronizers.countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Alexey
 */
class Service implements Runnable {
    private final String name;
    private final int timeToStart;
    private final CountDownLatch latch;

    public Service(String name, int timeToStart, CountDownLatch latch) {
        this.name = name;
        this.timeToStart = timeToStart;
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(timeToStart);
        } catch (InterruptedException ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(name + " is Up");
        latch.countDown(); //reduce count of CountDownLatch by 1
    }

    public static void main(String args[]) {
        final CountDownLatch latch = new CountDownLatch(3);
        Thread cacheService = new Thread(new Service("CacheService", 3000, latch));
        Thread alertService = new Thread(new Service("AlertService", 1000, latch));
        Thread validationService = new Thread(new Service("ValidationService", 4000, latch));
        cacheService.start(); //separate thread will initialize CacheService
        try {
        latch.await();
         latch.getCount(); //main thread is waiting on CountDownLatch to finish
        alertService.start(); //another thread for AlertService initialization
        validationService.start();
            System.out.println("All services are up, Application is starting now");
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

}
