package exchanger;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Alexey
 */
public class DataBuffer {
    private final static int MAX = 5;
    private Queue<String> queue = new LinkedList<>();

    public void add(String s) {
        if (!isFull()) {
            queue.add(s);
            System.out.println("added element " + s);
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
            System.out.println("removed element " + removed);
            return removed;
        }
        return null;
    }

    @Override
    public String toString() {
        return "DataBuffer{" +
                "queue=" + Arrays.toString(queue.toArray()) +
                '}';
    }
}
