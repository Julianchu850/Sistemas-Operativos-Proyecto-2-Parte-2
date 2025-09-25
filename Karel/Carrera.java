import kareltherobot.*;
import java.awt.Color;
import java.util.concurrent.CountDownLatch;

public class Carrera {
    public static void main(String[] args) {
        World.readWorld("Carretera.kwld");
        World.setVisible(true);
        World.setDelay(10);
        CountDownLatch latch = new CountDownLatch(1);

        // Azules
        Racer.crear_robots(1, 7, Directions.East, 0, Color.BLUE, "corto", 28,latch);
        //Racer.crear_robots(1, 7, Directions.East, 0, Color.BLUE, "largo", 0);

        Racer.crear_robots(12, 23, Directions.South, 0, Color.GREEN, "corto", 28, latch);
       // Racer.crear_robots(12, 23, Directions.South, 0, Color.GREEN, "largo", 3);

        latch.countDown(); // el contador pasa de 1 a 0


    }

}
