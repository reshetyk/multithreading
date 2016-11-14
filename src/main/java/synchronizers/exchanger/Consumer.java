package synchronizers.exchanger;

import java.util.Random;
import java.util.concurrent.Exchanger;

/**
 * @author Alexey
 */
public class Consumer implements Runnable {
    private final DataBuffer dataBuffer;
    private final Exchanger<DataBuffer> exchanger;
    private final static Random random = new Random(1000);

    public Consumer(DataBuffer dataBuffer, Exchanger<DataBuffer> exchanger) {
        this.dataBuffer = dataBuffer;
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        DataBuffer currentBuffer = dataBuffer;
        try {
            while (true) {
                Thread.sleep(random.nextInt(4000));
                takeFromBuffer(currentBuffer);
                if (currentBuffer.isEmpty()) {
                    System.out.println("Consumer: current " + currentBuffer + " is empty and wants to be exchanged");
                    currentBuffer = exchanger.exchange(currentBuffer); //<-- will wait Producer here
                    System.out.println("Consumer: dataBuffer is exchanged, now the buffer is " + currentBuffer );
                }
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    void takeFromBuffer(DataBuffer buffer) {
         buffer.remove();
    }
}

