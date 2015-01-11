package exchanger;

import java.util.concurrent.Exchanger;

public class ExchangerDemo {
    private static Exchanger<DataBuffer> exchanger = new Exchanger<>();
    private static DataBuffer initialEmptyBuffer = new DataBuffer("initialEmptyBuffer");
    private static DataBuffer initialFullBuffer = createAndFillBuffer("initialFullBuffer", 5, "item");

    public static void main(String[] args) {
        new Thread(new Consumer(initialEmptyBuffer, exchanger)).start();
        new Thread(new Producer(initialFullBuffer, exchanger)).start();
    }

    public static DataBuffer createAndFillBuffer(String name, int count, String prefix) {
        DataBuffer dataBuffer = new DataBuffer(name);
        for (int i = 0; i < count; i++) {
            dataBuffer.add(prefix + i);
        }
        return dataBuffer;
    }
}

