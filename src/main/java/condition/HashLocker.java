package condition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class HashLocker {
    public final static int ACCOUNT_COUNT = 10;
    public final static int HASH_COUNT = 8;
    public final static int THREAD_COUNT = 5;

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Map<Integer, Thread> inProcess = new ConcurrentHashMap<>();
        ExecutorService poolExecutor = Executors.newFixedThreadPool(THREAD_COUNT);
        List<Mapper> mappers = new ArrayList<>();

        for (int i = 0; i < ACCOUNT_COUNT; i++) {
            mappers.add(new Mapper(i));
        }

        poolExecutor.invokeAll(mappers).forEach((Future<Account> future) -> {
            try {
                Account account = future.get();
                poolExecutor.submit(new Saver(account, inProcess));
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

    Account account;
    Map<Integer, Thread> inProcess;


    public Saver(Account account, Map<Integer, Thread> inProcess) {
        this.account = account;
        this.inProcess = inProcess;
    }

    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        String currentThreadName = currentThread.getName();
        try {
            while (inProcess.containsKey(account.hash)) {
                Thread threadInProgress = inProcess.get(account.hash);
                System.out.println(currentThreadName + ": waiting until account with hash [" + account.hash + "] will be stored");
                threadInProgress.join();
                System.out.println(currentThreadName + ": [" + account.hash + "] stored");
            }
            inProcess.put(account.hash, currentThread);
            System.out.println(currentThreadName + ": [" + account.hash + "] currently in process " + inProcess.toString());
            Thread.currentThread().sleep(100 + (int) (Math.random() * 200));
            System.out.println(currentThreadName + ": saving account data " + account.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inProcess.containsKey(account.hash)) {
                inProcess.remove(account.hash);
            }
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