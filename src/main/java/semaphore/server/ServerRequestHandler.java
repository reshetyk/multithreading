package semaphore.server;

import java.io.PrintStream;
import java.net.Socket;
import java.util.concurrent.Semaphore;

/**
 * @author alre
 */
class ServerRequestHandler implements Runnable {
    private final Socket socket;
    private final Semaphore semaphore;

    ServerRequestHandler(Socket socket, Semaphore semaphore) {
        this.socket = socket;
        this.semaphore = semaphore;
    }

    public void run() {
        try {
            semaphore.acquire();
            PrintStream printStream = new PrintStream(socket.getOutputStream());
            printStream.println("request accepted by " + Thread.currentThread().getName());
            Thread.sleep(6000);
            printStream.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

}
