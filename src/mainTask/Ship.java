package mainTask;

import java.util.concurrent.atomic.AtomicBoolean;

public class Ship implements Runnable {
    private Port destinationPort;
    private final int maxShipCapacity;
    private volatile int currentShipCapacity;
    private String typeOfOperation;

    public Ship(Port port, int maxShipCapacity, int currentShipCapacity, String typeOfOperation) {
        this.destinationPort = port;
        this.maxShipCapacity = maxShipCapacity;
        this.currentShipCapacity = currentShipCapacity;
        this.typeOfOperation = typeOfOperation;
    }

    public String getTypeOfOperation() {
        return typeOfOperation;
    }

    public int getCurrentShipCapacity() {
        return currentShipCapacity;
    }

    public int getMaxShipCapacity() {
        return maxShipCapacity;
    }

    @Override
    public void run() {

        showShipInformationAndWaitingStatus();
        try {
            destinationPort.quantityOfFreeDocks.acquire();
            if (this.getTypeOfOperation().equals("load")) {
                checkShipStateAfterAttemptToLoad();
            } else if (this.getTypeOfOperation().equals("unload")) {
                checkShipStateAfterAttemptToUnload();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            showResultOfShipVisit();
            destinationPort.quantityOfFreeDocks.release();
        }
    }

    public void checkShipStateAfterAttemptToLoad() throws InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            int containersToLoad = currentShipCapacity;
            destinationPort.putContainersFromPortToShip(containersToLoad);
            showShipInformationAndWaitingStatus();
            throw new InterruptedException();
        } else {
            int containersToLoad = maxShipCapacity - currentShipCapacity;
            destinationPort.putContainersFromPortToShip(containersToLoad);
            showShipInformationAndWaitingStatus();
            Thread.sleep(500);
        }
    }

    public void checkShipStateAfterAttemptToUnload() throws InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            destinationPort.putContainersFromShipToPort(0);
            showShipInformationAndWaitingStatus();
            throw new InterruptedException();
        } else {
            destinationPort.putContainersFromShipToPort(currentShipCapacity);
            currentShipCapacity = 0;
            showShipInformationAndWaitingStatus();
            Thread.sleep(500);
        }
    }

    public void showResultOfShipVisit() {
        System.out.println(Thread.currentThread().getName() + " get out of port with "
                + currentShipCapacity + " containers on board.");
    }

    public void showShipInformationAndWaitingStatus() {
        System.out.println(Thread.currentThread().getName() + " with max capacity " + this.getMaxShipCapacity() + " containers and " +
                getCurrentShipCapacity() + " containers on board, get to port. Queue: "
                + destinationPort.quantityOfFreeDocks.getQueueLength());
    }

}
