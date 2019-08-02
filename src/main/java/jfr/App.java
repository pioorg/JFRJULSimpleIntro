/**
 * This is based on https://dzone.com/articles/using-java-flight-recorder-with-openjdk-11-2
 */
package jfr;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Stream;

public class App {
    private static BlockingQueue<byte[]> queue = new LinkedBlockingQueue<>();

    public static void main(String[] args) {
        if (args.length > 0) {
            System.out.println("I've got the following arguments:");
            Stream.of(args).forEach(System.out::println);
        }
        new Thread(new Consumer(), "Consumer").start();
        new Thread(new Producer(), "Producer").start();

    }

    static class Producer implements Runnable {
        public void run() {
            while (true) {
                queue.offer(new byte[3 * 1024 * 1024]);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Consumer implements Runnable {

        public void run() {
            while (true) {
                try {
                    queue.take();
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
