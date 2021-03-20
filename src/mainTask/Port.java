package mainTask;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Port {

    public Semaphore quantityOfFreeDocks;
    private final int maxPortCapacity;
    private int currentQuantityOfContainers;
    private final Lock locker = new ReentrantLock();
    private final Condition conditionToLoad = locker.newCondition();
    private final Condition conditionToUnload = locker.newCondition();

    public Port(int countDocks, int maxContainers, int currentQuantityOfContainers) {
        quantityOfFreeDocks = new Semaphore(countDocks, true);
        this.maxPortCapacity = maxContainers;
        this.currentQuantityOfContainers = currentQuantityOfContainers;
    }

    public int getCurrentQuantityOfContainers() {
        return currentQuantityOfContainers;
    }

    public int getMaxPortCapacity() {
        return maxPortCapacity;
    }

    public void putContainersFromShipToPort(int containers) {
        locker.lock();
        try {
            while (currentQuantityOfContainers + containers >= maxPortCapacity) {
                conditionToLoad.await();
                conditionToUnload.signalAll();
            }
            currentQuantityOfContainers += containers;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(Thread.currentThread().getName() + " get unloaded  " + containers + " container.");
            conditionToUnload.signalAll();
            locker.unlock();
        }
    }

    public void putContainersFromPortToShip(int containers) {
        locker.lock();
        try {
            while (currentQuantityOfContainers - containers < 0) {
                conditionToUnload.await();
                conditionToLoad.signalAll();
            }
            currentQuantityOfContainers -= containers;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(Thread.currentThread().getName() + " get loaded " + containers + " container.");
            conditionToLoad.signalAll();
            locker.unlock();
        }
    }
}