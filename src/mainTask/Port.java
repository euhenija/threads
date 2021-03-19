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
    private final Condition conLoading = locker.newCondition();
    private final Condition conUnloading = locker.newCondition();

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

    public void loading(int containers) {
        locker.lock();
        try {
            while (currentQuantityOfContainers + containers >= maxPortCapacity) {
                conLoading.await();
                conUnloading.signalAll();
            }
            currentQuantityOfContainers += containers;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(Thread.currentThread().getName() + " get unloaded  " + containers + " container.");
            conUnloading.signalAll();
            locker.unlock();
        }
    }

    public void putContainersFromPortToShip(int containers) {
        locker.lock();
        try {
            while (currentQuantityOfContainers + containers >= maxPortCapacity) {
                conUnloading.await();
                conLoading.signalAll();
            }
            currentQuantityOfContainers -= containers;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(Thread.currentThread().getName() + " get loaded " + containers + " container.");
            conLoading.signalAll();
            locker.unlock();
        }
    }
}