package mainTask;

public class Main {


    public static void main(String[] args) throws InterruptedException {

        Port port = new Port(2,5000,2000);

        new Thread(new Ship(port,3600,300, "unload")).start();
        new Thread(new Ship(port,3300,300, "load")).start();
        new Thread(new Ship(port,10,10, "load")).start();
        new Thread(new Ship(port,4004,10, "load")).start();
        new Thread(new Ship(port,300,34, "load")).start();
        new Thread(new Ship(port,100,100, "unload")).start();
        new Thread(new Ship(port,140,45, "unload")).start();
        new Thread(new Ship(port,10,10, "unload")).start();
        new Thread(new Ship(port,100,10, "load")).start();
        new Thread(new Ship(port,320,160, "unload")).start();
    }
}
