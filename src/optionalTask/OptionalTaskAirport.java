package optionalTask;

import java.util.concurrent.Semaphore;

public class OptionalTaskAirport implements Runnable {

    Semaphore semaphore;
    int planeNumber;

    OptionalTaskAirport(Semaphore semaphore, int planeNumber) {
        this.semaphore = semaphore;
        this.planeNumber = planeNumber;

    }

    public void run() {
        try {
                semaphore.acquire();
                System.out.println("Path is ready for plane " + planeNumber);
                Thread.sleep(500);
                System.out.println("Plane " + planeNumber + " is taking off ");
                Thread.sleep(2000);
                System.out.println("Plane " + planeNumber + " has taken off successfully");
                Thread.sleep(500);
                System.out.println("Path is free after plane " + planeNumber + " has taken off");
                semaphore.release();
                Thread.sleep(500);
        } catch (InterruptedException e) {
            System.out.println("Plane " + planeNumber + " has problems");
        }

    }
}
