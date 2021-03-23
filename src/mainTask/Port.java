package mainTask;

import java.util.concurrent.Semaphore;
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

    public synchronized int getCurrentQuantityOfContainers() {
        return currentQuantityOfContainers.get();
    }

    public int getMaxPortCapacity() {
        return maxPortCapacity;
    }

    public void putContainersFromShipToPort(int containers) throws InterruptedException {
        locker.lock();
        if (currentQuantityOfContainers.get() + containers > maxPortCapacity) {
            tryToLoadOrUnloadShipLater(containers);
        } else {
            try {
                currentQuantityOfContainers.set(currentQuantityOfContainers.get() + containers);
                System.out.println(Thread.currentThread().getName() + " get unloaded  " + containers + " container.Port current capacity: " + this.getCurrentQuantityOfContainers());
            } finally {
                conditionToUnload.signalAll();
                locker.unlock();
            }
        }
    }

    public void putContainersFromPortToShip(int containers) throws InterruptedException {
        locker.lock();
        if (containers > currentQuantityOfContainers.get()) {
            tryToLoadShipLater(containers);
        } else {
            try {
                currentQuantityOfContainers.set(currentQuantityOfContainers.get() - containers);
                System.out.println(Thread.currentThread().getName() + " get loaded  " + containers + " container.Port current capacity: " + this.getCurrentQuantityOfContainers());
            } finally {
                conditionToLoad.signalAll();
                locker.unlock();
            }
        }
    }

    public synchronized void tryToLoadOrUnloadShipLater(int containers) throws InterruptedException {
        int quantityOfAttemptsToUnload = 0;
        while (currentQuantityOfContainers.get() + containers > maxPortCapacity) {
            System.out.println("Port has no ability to unload " + containers + " containers from " + Thread.currentThread().getName());
            quantityOfAttemptsToUnload++;
            if (quantityOfAttemptsToUnload > 0) {
                containers = 0;
                System.out.println(Thread.currentThread().getName() + " went to another port");
                locker.unlock();
            } else {
                conditionToUnload.await();
                conditionToLoad.signalAll();
            }
        }
    }

    public synchronized void tryToLoadShipLater(int containers) throws InterruptedException {
        int quantityOfAttemptsToLoad = 0;
        while (currentQuantityOfContainers.get() - containers < 0) {
            System.out.println("Port has no ability to load " + containers + " to " + Thread.currentThread().getName());
            quantityOfAttemptsToLoad++;
            if (quantityOfAttemptsToLoad > 0) {
                containers = 0;
                System.out.println(Thread.currentThread().getName() + " went to another port");
                locker.unlock();
            } else {
                conditionToLoad.await();
                conditionToUnload.signalAll();
            }
        }
    }

}

