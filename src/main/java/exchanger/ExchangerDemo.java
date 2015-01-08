package exchanger;

import java.util.concurrent.Exchanger;

public class ExchangerDemo {
    static Exchanger<DataBuffer> exchanger = new Exchanger<>();
    static DataBuffer initialEmptyBuffer = new DataBuffer();
    static DataBuffer initialFullBuffer = createAndFillBuffer("item", 5);

    public static DataBuffer createAndFillBuffer (String prefix, int count) {
        DataBuffer dataBuffer = new DataBuffer();
        for (int i = 0; i < count; i++) {
            dataBuffer.add(prefix + i);
        }
        return dataBuffer;
    }

    public static void main(String[] args) {
        new Thread(new Consumer(initialFullBuffer, exchanger)).start();
        new Thread(new Producer(initialEmptyBuffer, exchanger)).start();
    }
}

