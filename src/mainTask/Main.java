package mainTask;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int MAX_QUEUE_CAPACITY = 5;
        Semaphore quantityOfFreeBerths = new Semaphore(2);
        BlockingQueue<Ship> queueOfShips = new LinkedBlockingQueue<>(MAX_QUEUE_CAPACITY);
        BlockingQueue<Ship> queueOfShips1 = new LinkedBlockingQueue<>(MAX_QUEUE_CAPACITY);
        BlockingQueue<Ship> queueOfShips2 = new LinkedBlockingQueue<>(MAX_QUEUE_CAPACITY);


        ShipGenerator gen = new ShipGenerator(queueOfShips);
        ShipGenerator gen1 = new ShipGenerator(queueOfShips1);
        ShipGenerator gen2 = new ShipGenerator(queueOfShips2);
        new Thread(gen).start();
        new Thread(gen1).start();
        new Thread(gen2).start();

        Thread.sleep(2000);
        Port port = new Port(queueOfShips);
        new Thread(port).start();

    }
}
