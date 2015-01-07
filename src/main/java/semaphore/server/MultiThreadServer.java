package semaphore.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Semaphore;

/**
 * @author alre
 */
public class MultiThreadServer {
    private final Integer portNumber;
    private final Semaphore semaphore;
    private boolean isStopped;

    MultiThreadServer(Integer portNumber, Integer maxConnections) {
        this.portNumber = portNumber;
        this.semaphore = new Semaphore(maxConnections);
    }

    public void startServer() {
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(portNumber);
            System.out.println("--Server started and listening port: " + portNumber);
            while (!isStopped) {
                final Thread thread = new Thread(new ServerRequestHandler(socket.accept(), semaphore));
                System.out.println("created " + thread.getName());
                thread.start();
            }
            System.out.println("--Server stopped");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopServer () {
        isStopped = true;
    }
}

