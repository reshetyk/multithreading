package condition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class HashLocker {
    public final static int ACCOUNT_COUNT = 10;
    public final static int HASH_COUNT = 4;
    public final static int THREAD_COUNT = 2;

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ReentrantLock lock = new ReentrantLock(true);
        Condition lockCondition = lock.newCondition();
        Set<Integer> inProcess = new CopyOnWriteArraySet<>();
        ExecutorService poolExecutor = Executors.newFixedThreadPool(THREAD_COUNT);
        List<Mapper> mappers = new ArrayList<>();

        for (int i = 0; i < ACCOUNT_COUNT; i++) {
            mappers.add(new Mapper(i));
        }

        poolExecutor.invokeAll(mappers).forEach((Future<Account> future) -> {
            try {
                Account account = future.get();
                poolExecutor.submit(new Saver(account, inProcess, lock, lockCondition));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        poolExecutor.shutdown();
//        poolExecutor.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
    }

}

class Account {
    int id;
    int hash;

    Account(int id, int hash) {
        this.id = id;
        this.hash = hash;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", hash='" + hash + '\'' +
                '}';
    }
}

class Saver implements Runnable {

    private final ReentrantLock lock;
    Condition lockCondition;
    Account account;
    Set<Integer> inProcess;


    public Saver(Account account, Set<Integer> inProcess, ReentrantLock lock, Condition lockCondition) {
        this.account = account;
        this.inProcess = inProcess;
        this.lock = lock;
        this.lockCondition = lockCondition;
    }

    @Override
    public void run() {
        String currentThreadName = Thread.currentThread().getName();
        try {
            while (inProcess.contains(account.hash)) {
                System.out.println(currentThreadName + ": waiting until account with hash [" + account.hash + "] will be stored");
                lock.lock();
                lockCondition.await();
            }

            inProcess.add(account.hash);
            System.out.println(currentThreadName + ": hash -> " + account.hash + " currently in process " + Arrays.toString(inProcess.toArray()));
            Thread.currentThread().sleep(100 + (int) (Math.random() * 200));
            System.out.println(currentThreadName + ": saving account data " + account.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inProcess.contains(account.hash)) {
                inProcess.remove(account.hash);
                System.out.println(currentThreadName + ": hash -> " + account.hash + " currently in process " + Arrays.toString(inProcess.toArray()) + " after removal");
                lockCondition.signal();
            }
            lock.unlock();
        }
    }
}

class Mapper implements Callable<Account> {
    Integer id;

    Mapper(Integer id) {
        this.id = id;
    }

    @Override
    public Account call() throws Exception {
        int duration = 40 + (int) (Math.random() * 60);
        int hash = (int) (Math.random() * HashLocker.HASH_COUNT);

        System.out.println("on mapping id=" + id + " -> " + hash + ", duration=" + duration);
        Thread.currentThread().sleep(duration);
        System.out.println(Thread.currentThread().getName() + ": mapper done; id=" + id);

        return new Account(id, hash);
    }
}