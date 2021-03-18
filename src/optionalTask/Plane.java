package optionalTask;

import java.util.concurrent.Semaphore;

public class Plane extends Thread {

    public Semaphore path;
    private int planeId;

    public Plane(Semaphore path, int planeId) {
        this.path = path;
        this.planeId = planeId;
    }

    public int getPlaneId() {
        return planeId;
    }

    public void run() {
        try {
            path.acquire();
            System.out.println("Path is ready for plane " + this.getPlaneId());
            Thread.sleep(500);
            System.out.println("Plane " + this.getPlaneId() + " is taking off ");
            Thread.sleep(2000);
            System.out.println("Plane " + this.getPlaneId() + " has taken off successfully");
            Thread.sleep(500);
            System.out.println("Path is free after plane " + this.getPlaneId() + " has taken off");
            path.release();
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("Plane " + this.getPlaneId() + " has problems");
        }

    }
}
