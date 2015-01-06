package semaphore.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Semaphore;

/**
 * @author alre
 */
public class MultiThreadServer {
    private final Integer portNumber;
    private Semaphore semaphore;

    MultiThreadServer(Integer portNumber, Integer maxConnections) {
        this.portNumber = portNumber;
        this.semaphore = new Semaphore(maxConnections);
    }

    public void startServer() {
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(portNumber);
            System.out.println("Server started and listening port: " + portNumber);
            while (true) {
                new Thread(new ServerRequestHandler(socket.accept(), semaphore)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new MultiThreadServer(1234, 2).startServer();
    }
}

