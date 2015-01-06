package exchanger;

import java.util.concurrent.Exchanger;

/**
 * Created by Alexey on 10.10.2014.
 */
public class Test {

    class DataBuffer<T> {
        private T data[];
        private int bufferSize;
        private int cursor;

        DataBuffer(int bufferSize) {
            this.bufferSize = bufferSize;
            data = (T[]) new Object[bufferSize];
            cursor = 0;
        }

        public boolean isFull() {
            return cursor == bufferSize;
        }

        public boolean isEmpty() {
            return 0 == cursor;
        }

        public synchronized void add(T item) {
            data[cursor] = item;
            cursor++;
        }

        public synchronized T take() {
            if (isEmpty()) return null;
            if (isFull()) return null;
            final T t = data[cursor];
            cursor--;
            return t;
        }
    }

    class FillAndEmpty {
        Exchanger<DataBuffer> exchanger = new Exchanger<DataBuffer>();
        DataBuffer initialEmptyBuffer = new DataBuffer<String>(100);
        DataBuffer initialFullBuffer = new DataBuffer<String>(100);

        class FillingLoop implements Runnable {
            public void run() {
                DataBuffer currentBuffer = initialEmptyBuffer;
                while (currentBuffer != null) {
                    currentBuffer.add("item ");
                    if (currentBuffer.isFull()) {
                        try {
                            currentBuffer = exchanger.exchange(currentBuffer);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("FillingLoop performs exchange");
                    }
                }
            }
        }

        class EmptyingLoop implements Runnable {
            public void run() {
                DataBuffer currentBuffer = initialFullBuffer;
                while (currentBuffer != null) {
                    currentBuffer.take();
                    if (currentBuffer.isEmpty()) {
                        try {
                            currentBuffer = exchanger.exchange(currentBuffer);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("EmptyingLoop performs exchange");
                    }
                }
            }
        }

        void start() {
            new Thread(new FillingLoop()).start();
            new Thread(new EmptyingLoop()).start();
        }


    }
}
