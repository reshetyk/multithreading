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
            final String message = "request accepted by " + Thread.currentThread().getName();
            printStream.println("Client:" + message);
            System.out.println("Server:" + message);
            Thread.sleep(2000);
            printStream.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

}
