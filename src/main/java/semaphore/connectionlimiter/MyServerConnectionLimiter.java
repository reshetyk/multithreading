package semaphore.connectionlimiter;

import java.io.IOException;
import java.net.URL;

public class MyServerConnectionLimiter extends ConnectionLimiter {


    public MyServerConnectionLimiter(int maxConcurrentRequests, URL url) {
        super(maxConcurrentRequests, url);
    }

    @Override
    protected void doWork() {
        System.out.println(Thread.currentThread().getName() + " do something with url '" + url + "'");
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        final MyServerConnectionLimiter myServerConnectionLimiter = new MyServerConnectionLimiter(3, new URL("http://google.com"));

        for (int i = 0; i <= 10; i++) {
            final Thread thread = new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        myServerConnectionLimiter.openConnection();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.setName("thread - " + i);
            thread.start();
        }

    }

}