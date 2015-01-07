package semaphore.server;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MultiThreadServerTest {

    @Test
    public void limitationRequests() throws Exception {
        final int portNumber = 1111;
        final int maxConnections = 3;

        final MultiThreadServer server = new MultiThreadServer(portNumber, maxConnections);

        final Thread serverThread = new Thread(() -> server.startServer());
        serverThread.start();

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                final String answer = createClientAndReceiveAnswer(portNumber, "localhost");
                System.out.println(answer);
            }, "thread-" + i).start();
        }
        serverThread.join(10000);
    }

    private String createClientAndReceiveAnswer(int portNumber, String host) {
        BufferedReader in = null;
        Socket client = null;
        try {
            client = new Socket(host, portNumber);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            return in.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}