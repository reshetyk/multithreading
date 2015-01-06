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
        new Thread() {
            @Override
            public void run() {
                server.startServer();
            }
        }.start();


        for (int i = 0; i < 10; i++) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        Socket client = new Socket("localhost", portNumber);
                        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                        System.out.println(in.readLine());
                        in.close();
                        client.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }.start();
        }
        Thread.currentThread().join();
        server.stopServer();
    }
}