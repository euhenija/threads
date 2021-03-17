package optionalTask;

import java.util.concurrent.Semaphore;

public class OptionalTaskMain {
    public static void main (String[] args){
        int quantityOfPaths = 5;
        int quantityOfPlanes = 10;
        Semaphore semaphore = new Semaphore(quantityOfPaths);
        for(int i=1; i<quantityOfPlanes+1; i++)
        {
            Thread airport = new Thread(new OptionalTaskAirport(semaphore, i));
            airport.start();
        }
    }
}
