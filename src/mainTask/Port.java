package mainTask;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Port {

    public Semaphore quantityOfFreeDocks;
    private final int maxPortCapacity;
    private AtomicInteger currentQuantityOfContainers;
    private final Lock locker = new ReentrantLock();
    private final Condition conditionToLoad = locker.newCondition();
    private final Condition conditionToUnload = locker.newCondition();

    public Port(int countDocks, int maxContainers, int currentQuantityOfContainers) {
        quantityOfFreeDocks = new Semaphore(countDocks, true);
        this.maxPortCapacity = maxContainers;
        this.currentQuantityOfContainers = new AtomicInteger(currentQuantityOfContainers);
    }

    public int getCurrentQuantityOfContainers() {
        return currentQuantityOfContainers.get();
    }

    public int getMaxPortCapacity() {
        return maxPortCapacity;
    }

    public void putContainersFromShipToPort(int containers) throws InterruptedException {
        locker.lock();
        int quantityOfAttemptsToUnload = 0;
        try {
            while (currentQuantityOfContainers.get() + containers > maxPortCapacity) {
                quantityOfAttemptsToUnload++;
                if(quantityOfAttemptsToUnload > 0){
                    Thread.currentThread().interrupt();
                }
                conditionToLoad.await();
                conditionToUnload.signalAll();
            }
            currentQuantityOfContainers.set(currentQuantityOfContainers.get() + containers);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(Thread.currentThread().getName() + " get unloaded  " + containers + " container.Port current capacity: " + this.getCurrentQuantityOfContainers());
            conditionToUnload.signalAll();
            locker.unlock();
        }
    }

    public void putContainersFromPortToShip(int containers) {

        locker.lock();
        try {
            int quantityOfAttemptsToLoad = 0;
            while (currentQuantityOfContainers.get() - containers < 0) {
                quantityOfAttemptsToLoad++;
                if (quantityOfAttemptsToLoad > 0) {
                    Thread.currentThread().interrupt();
                }
                conditionToUnload.await();
                conditionToLoad.signalAll();
            }
            currentQuantityOfContainers.set(currentQuantityOfContainers.get() - containers);
            System.out.println(Thread.currentThread().getName() + " get loaded  " + containers + " container.Port current capacity: " + this.getCurrentQuantityOfContainers());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            conditionToLoad.signalAll();
            locker.unlock();
        }
    }
}