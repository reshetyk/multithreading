package synchronizers.exchanger;

import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Alexey
 */
public class DataBuffer {
    private final static int MAX = 5;
    private Queue<String> queue = new ConcurrentLinkedQueue<>();
    private String name;

    public DataBuffer(String name) {
        this.name = name;
    }

    public void add(String s) {
        if (!isFull()) {
            queue.add(s);
            System.out.println(name + ": added element " + s);
        }
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public boolean isFull() {
        return queue.size() == MAX;
    }

    public String remove() {
        if (!isEmpty()) {
            final String removed = queue.remove();
            System.out.println(name + ":removed element " + removed);
            return removed;
        }
        return null;
    }

    @Override
    public String toString() {
        return "DataBuffer - " + name + " {" +
                "queue=" + Arrays.toString(queue.toArray()) +
                '}';
    }
}
