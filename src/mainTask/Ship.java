package mainTask;

public class Ship implements Runnable {
    private final Port destinationPort;
    private final int maxShipCapacity;
    private int currentShipCapacity;
    private String aim;

    public Ship(Port port, int maxShipCapacity, int currentShipCapacity, String aim) {
        this.destinationPort = port;
        this.maxShipCapacity = maxShipCapacity;
        this.currentShipCapacity = currentShipCapacity;
        this.aim = aim;
    }

    public String getAim() {
        return aim;
    }

    public int getCurrentShipCapacity() {
        return currentShipCapacity;
    }

    public int getMaxShipCapacity() {
        return maxShipCapacity;
    }

    @Override
    public void run() {
        try {
            System.out.println(Thread.currentThread().getName() + " with max capacity " + this.getMaxShipCapacity() + " containers and " +
                    getCurrentShipCapacity() + " containers on board, get to port. Queue: "
                    + destinationPort.quantityOfFreeDocks.getQueueLength());
            destinationPort.quantityOfFreeDocks.acquire();

            if (this.getAim().equals("load")) {
                int containersToLoad = maxShipCapacity - currentShipCapacity;
                currentShipCapacity = maxShipCapacity;
                destinationPort.putContainersFromPortToShip(containersToLoad);
                Thread.sleep(500);
            } else if (this.getAim().equals("unload")) {
                Thread.sleep(500);
                destinationPort.putContainersFromShipToPort(currentShipCapacity);
                currentShipCapacity = 0;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(Thread.currentThread().getName() + " get out of port with: " +
                    getCurrentShipCapacity() + " containers on board" + "  Quantity of containers in port: "
                    + destinationPort.getCurrentQuantityOfContainers());
            destinationPort.quantityOfFreeDocks.release();
        }
    }
}
