package mainTask;

public class Ship {

    private int shipMaxCapacity;
    private int shipCurrentCapacity;

    public Ship(int shipMaxCapacity, int shipCurrentCapacity) {
        this.shipMaxCapacity = shipMaxCapacity;
        this.shipCurrentCapacity = shipCurrentCapacity;
    }

    public int getMaxCapacity() {
        return shipMaxCapacity;
    }

    public int getShipCurrentCapacity() {
        return shipCurrentCapacity;
    }

    public void setShipCurrentCapacity(int shipCurrentCapacity) {
        this.shipCurrentCapacity = shipCurrentCapacity;
    }

    public void loadShip (){
        this.shipCurrentCapacity = shipMaxCapacity;
    }

    public void unloadShip (){
        this.shipCurrentCapacity = 0;
    }

    @Override
    public String toString() {
        return "mainTask.Ship{" +
                "shipMaxCapacity=" + shipMaxCapacity +
                ", shipCurrentCapacity=" + shipCurrentCapacity +
                '}';
    }
}
