package mainTask;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        Port port = new Port(2,5000,2000);

        new Thread(new Ship(port,3,0, "unload")).start();
        new Thread(new Ship(port,330,34, "load")).start();
        new Thread(new Ship(port,10,10, "load")).start();
        new Thread(new Ship(port,300,34, "load")).start();
        new Thread(new Ship(port,300,34, "load")).start();
        new Thread(new Ship(port,100,100, "unload")).start();
        new Thread(new Ship(port,10,10, "unload")).start();
        new Thread(new Ship(port,10,10, "unload")).start();
    }
}
