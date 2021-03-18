package mainTask;

import java.util.Random;
import java.util.concurrent.BlockingQueue;


public class ShipGenerator implements Runnable {

    private BlockingQueue<Ship> queueOfShips;

    public ShipGenerator(BlockingQueue<Ship> queueOfShips) {
        this.queueOfShips = queueOfShips;
    }

    public BlockingQueue<Ship> getQueueOfShips() {
        return queueOfShips;
    }

    public void run(){
        try {
            Random random = new Random();
            for (int i = 0; i < 2; i++){
                int maxCapacity = random.nextInt(100);
                int currentCapacity = random.nextInt(maxCapacity);
                Ship loadedShip = new Ship(maxCapacity, currentCapacity);
                queueOfShips.put(loadedShip);
                System.out.println("Loaded ship with " + loadedShip.getShipCurrentCapacity() + " containers out of " + loadedShip.getMaxCapacity() + " was added to queue");
            }
            for (int j = 0; j < 3; j++){
                int emptyShipMaxCapacity = random.nextInt(100);
                Ship emptyShip = new Ship(emptyShipMaxCapacity, 0);
                queueOfShips.put(emptyShip);
                System.out.println("Empty ship with max capacity " + emptyShip.getMaxCapacity() + " was added to queue");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
