import kareltherobot.*;
import java.awt.Color;

public class Carrera {
    public static void main(String[] args) {
        World.readWorld("Carretera.kwld");
        World.setVisible(true);
        World.setDelay(30);

        // Azules
        Racer azulCorto1 = new Racer(1, 7, Directions.East, 0, Color.GREEN, "corto", "azul", 0);
        Racer azulCorto2 = new Racer(1, 7, Directions.East, 0, Color.BLUE, "corto", "azul", 1);
        Racer azulCorto3 = new Racer(1, 7, Directions.East, 0, Color.BLUE, "corto", "azul", 2);
       
      

       Racer verdeCorto1 = new Racer(12, 23, Directions.South, 0, Color.GREEN, "cortoVerde", "verde", 0);
         Racer verdeCorto2 = new Racer(12, 23, Directions.South, 0, Color.GREEN, "cortoVerde", "verde", 1);
        Racer verdeCorto3 = new Racer(12, 23, Directions.South, 0, Color.GREEN, "cortoVerde", "verde", 2);
        
        Thread t1 = new Thread(azulCorto1);
        Thread t2 = new Thread(azulCorto2);
        Thread t3 = new Thread(azulCorto3);
       
        Thread t4 = new Thread(verdeCorto1);
        Thread t5 = new Thread(verdeCorto2);
        Thread t6 = new Thread(verdeCorto3);
        
  

       

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();
        
    

   
       
    }   
}
