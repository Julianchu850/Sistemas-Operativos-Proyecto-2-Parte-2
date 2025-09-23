import kareltherobot.*;
import java.awt.Color;
public class MiPrimerRobot implements Directions
{
public static void main(String [] args)
{
// Usamos el archivo que creamos del mundo
World.readWorld("Mundo.kwld");
World.setVisible(true);
// Coloca el robot en la posición inicial del mundo (1,1),
// mirando al Este, sin ninguna sirena.
Robot Karel = new Robot(1, 1, East, 0);
Robot Karel2 = new Robot(1, 1, East, 0, Color.BLUE);
// Mover el robot 4 pasos


    }
}