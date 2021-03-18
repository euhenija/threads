package optionalTask;

import java.util.concurrent.Semaphore;

public class Airport {
    public static void main(String[] args) {
        Semaphore path = new Semaphore(5);
        new Plane(path, 1).start();
        new Plane(path, 2).start();
        new Plane(path, 3).start();
        new Plane(path, 4).start();
        new Plane(path, 5).start();
        new Plane(path, 6).start();
        new Plane(path, 7).start();
        new Plane(path, 8).start();
        new Plane(path, 9).start();
        new Plane(path, 10).start();

    }
}
