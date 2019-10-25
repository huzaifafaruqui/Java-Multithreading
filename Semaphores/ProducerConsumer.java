
package multithreading;

import java.util.*;

/**
 *
 * @author huzaifafaruqui
 */
class PC {

    LinkedList<Integer> list = new LinkedList<>();
    int bufferSize = 3;

    public synchronized void produce(int item) throws InterruptedException {

        if (list.size() == bufferSize) {
            wait();
        }

        list.add(item);
        System.out.println("added" + item);

        notify();
    }

    public void consume() throws InterruptedException {
        synchronized(this){
        if (list.isEmpty()) {
            wait();
        }

        System.out.println("removed" + list.peekLast());
        list.removeLast();

        notify();
    }
  }     
}

public class ProducerConsumer {

    public static void main(String[] args) {
        // TODO code application logic here

        PC pc = new PC();

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;

                while (i < 10) {
                    try {
                        pc.produce(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ++i;
                }
            }
        });

        // Create consumer thread
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                int j = 0;
                while (j < 10) {
                    try {
                        pc.consume();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ++j;
                }
            }
        });

        t1.start();

        t2.start();
    }

}
