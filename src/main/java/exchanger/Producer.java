package exchanger;

import java.util.Random;
import java.util.concurrent.Exchanger;

/**
 * @author Alexey
 */
public class Producer implements Runnable {
    private final DataBuffer dataBuffer;
    private final Exchanger<DataBuffer> exchanger;
    private final static Random random = new Random(2000);
    private int count = 0;

    public Producer(DataBuffer dataBuffer, Exchanger<DataBuffer> exchanger) {
        this.dataBuffer = dataBuffer;
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        DataBuffer currentBuffer = dataBuffer;
        try {
            while (true) {
                addToBuffer();
                Thread.sleep(random.nextInt(500));
                if (currentBuffer.isFull()) {
                    System.out.println("Producer: current " + currentBuffer + " is full and wants to be exchanged");
                    currentBuffer = exchanger.exchange(currentBuffer);
                    System.out.println("Producer: dataBuffer is exchanged, now the buffer is " + currentBuffer );
                }
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    private void addToBuffer() {
        dataBuffer.add("NewItem " + ++count);
    }
}
