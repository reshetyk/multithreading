package synchronizers.semaphore;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;

/**
 * @author Alexey
 */
public class BoundedHashSet<T> {
    public static void main(String[] args) throws InterruptedException {
        final BoundedHashSet<Integer> stringBoundedHashSet = new BoundedHashSet<Integer>(4);
        Thread tProducer = new Thread() {
            @Override
            public void run() {
                super.run();
                int i = 0;
                while (i <= 100) {
                    try {
                        stringBoundedHashSet.add(++i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        Thread tConsumer = new Thread() {
            @Override
            public void run() {
                super.run();
                int i = 100;
                while (i >= 0) {
                    stringBoundedHashSet.remove(--i);
                }
            }
        };

        tProducer.start();
        tConsumer.start();


    }

    private final Set<T> set;
    private final Semaphore sem;

    public BoundedHashSet(int bound) {
        this.set = Collections.synchronizedSet(new HashSet<T>());
        sem = new Semaphore(bound);
    }

    public boolean add(T o) throws InterruptedException {
        sem.acquire();
        boolean wasAdded = false;
        try {
            wasAdded = set.add(o);
            System.out.println("added " + o);
            return wasAdded;
        } finally {
            if (!wasAdded)
                sem.release();
        }
    }

    public boolean remove(Object o) {
        boolean wasRemoved = set.remove(o);
        if (wasRemoved) {
            sem.release();
            System.out.println("removed " + o);

        }
        return wasRemoved;
    }

    public int size() {
        return set.size();
    }

    public boolean isEmpty() {
        return set.isEmpty();
    }
}
