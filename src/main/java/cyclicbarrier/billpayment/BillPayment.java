package cyclicbarrier.billpayment;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @author Alexey
 */
public class BillPayment {
    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3, new Runnable() {
            @Override
            public void run() {
                System.out.println("Ok");
            }
        });
        new Thread(new BillValidatorCorrectness(cyclicBarrier)).start();
        new Thread(new BillValidatorCredential(cyclicBarrier)).start();
        new Thread(new BillValidatorHasMoney(cyclicBarrier)).start();

    }
}

class BillValidatorCorrectness implements Runnable {
    private CyclicBarrier cyclicBarrier;

    BillValidatorCorrectness(CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
    }


    @Override
    public void run() {
        System.out.println("verification bill...");
        try {
            Thread.currentThread().sleep(1000);
            System.out.println("verification ok!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}


class BillValidatorCredential implements Runnable {
    private CyclicBarrier cyclicBarrier;

    BillValidatorCredential(CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
    }


    @Override
    public void run() {
        System.out.println("check credential...");
        try {
            Thread.currentThread().sleep(2000);
            System.out.println("credential ok!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}

class BillValidatorHasMoney implements Runnable {
    private CyclicBarrier cyclicBarrier;

    BillValidatorHasMoney(CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
    }


    @Override
    public void run() {
        System.out.println("check has money...");
        try {
            Thread.currentThread().sleep(2000);
            System.out.println("has money ok!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}

