package mainTask;

public class Main {


    public static void main(String[] args)  {

        Port port = new Port(2,5000,2000);

        new Thread(new Ship(port,34004,34, "load")).start();
        new Thread(new Ship(port,3300,30, "load")).start();
        new Thread(new Ship(port,10,1, "load")).start();
        new Thread(new Ship(port,4004,4000, "unload")).start();
        new Thread(new Ship(port,30000,34, "load")).start();
        new Thread(new Ship(port,10000,10, "load")).start();
        new Thread(new Ship(port,140,45, "unload")).start();
        new Thread(new Ship(port,10,1, "unload")).start();
        new Thread(new Ship(port,100,10, "load")).start();
        new Thread(new Ship(port,320,160, "unload")).start();
    }
}
