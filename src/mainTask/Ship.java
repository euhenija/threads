package mainTask;

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
                int containersToLoad = maxShipCapacity - currentShipCapacity;
                destinationPort.putContainersFromPortToShip(containersToLoad);
                changeShipCurrentCapacityAfterLoading();
                Thread.sleep(500);
            } else if (this.getTypeOfOperation().equals("unload")) {
                destinationPort.putContainersFromShipToPort(currentShipCapacity);
                changeShipCurrentCapacityAfterUnloading();
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            showResultOfShipVisit();
            destinationPort.quantityOfFreeDocks.release();
        }
    }


    public void changeShipCurrentCapacityAfterLoading() {
        if (destinationPort.getCurrentQuantityOfContainers() + (maxShipCapacity - currentShipCapacity) < destinationPort.getMaxPortCapacity()) {
            this.currentShipCapacity = this.maxShipCapacity;
        }
    }

    public void changeShipCurrentCapacityAfterUnloading() {
        if (destinationPort.getCurrentQuantityOfContainers() - currentShipCapacity > 0) {
            this.currentShipCapacity = 0;
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
