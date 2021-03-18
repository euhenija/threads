package mainTask;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Port implements Runnable {
    private Semaphore quantityOfFreeBerths;
    public final int PORT_MAX_CAPACITY = 200;
    private int portCurrentCapacity = 0;
    private BlockingQueue<Ship> queueOfShips;
    private ReentrantLock locker;
    private Condition condition;
    private Ship ship;

    public Port(BlockingQueue<Ship> queueOfShips) {
        locker = new ReentrantLock();
        condition = locker.newCondition();
        this.queueOfShips = queueOfShips;
    }

    @Override
    public void run() {
        try {

            while (!queueOfShips.isEmpty()) {
                //quantityOfFreeBerths.acquire();
                ship = queueOfShips.take();
                if (ship.getShipCurrentCapacity() != 0) {
                    putContainersFromShipToPort();
                } else putContainersFromPortToShip();
                // quantityOfFreeBerths.release();
            }

        } catch (InterruptedException e) {
            System.out.println("mainTask.Ship has problems");
        }
    }

    public void putContainersFromPortToShip() {
        locker.lock();
        try {
            while (portCurrentCapacity < 1)
                condition.await();
            portCurrentCapacity = portCurrentCapacity - (ship.getMaxCapacity() - ship.getShipCurrentCapacity());
            if (portCurrentCapacity > 0) {
                ship.setShipCurrentCapacity(0);
                System.out.println((ship.getMaxCapacity() - ship.getShipCurrentCapacity()) + " containers were put out of port");
                System.out.println(("Current quantity of containers in port: " + portCurrentCapacity));
                condition.signalAll();
            } else {
                System.out.println("No containers in the port!");

            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        } finally {
            locker.unlock();
        }

    }

    public void putContainersFromShipToPort() {

        locker.lock();
        try {
            while (portCurrentCapacity >= PORT_MAX_CAPACITY)
                condition.await();

            portCurrentCapacity += ship.getShipCurrentCapacity();
            System.out.println(ship.getShipCurrentCapacity() + " containers were pup in port");
            System.out.println("Current quantity of containers in port: " + portCurrentCapacity);
            condition.signalAll();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        } finally {
            locker.unlock();
        }
    }

}