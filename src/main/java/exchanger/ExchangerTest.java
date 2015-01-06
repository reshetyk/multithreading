package exchanger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

public class ExchangerTest {

    Exchanger<List> exchanger = new Exchanger();
    List emptyList = new ArrayList();
    List filledList = new ArrayList();

    public ExchangerTest() {
        emptyList.add("1");
    }

    class AddList implements Runnable {
        public void run() {
            try {
                while (true) {
                    System.out.println("AddList size " + filledList.size());
                    filledList.add("1");
                    System.out.println("AddList size " + filledList.size());
                    if (filledList.size() == 1) {
                        System.out.println("AddList exchanger");
                        filledList = exchanger.exchange(filledList);


                    }
                }
            } catch (InterruptedException ex) {
                System.out.println(ex);
            }
        }
    }

    class SubtractList implements Runnable {
        public void run() {
            try {

                while (true) {

                    System.out.println("SubtractList size " + emptyList.size());
                    emptyList.remove("1");
                    System.out.println("SubtractList size " + emptyList.size());
                    if (emptyList.size() == 0) {
                        System.out.println("SubtractList exchanged");
                        emptyList = exchanger.exchange(emptyList);

                    }
                }
            } catch (InterruptedException ex) {
                System.out.println(ex);
            }
        }
    }
}

