import kareltherobot.*;
import java.awt.Color;
import java.util.concurrent.CountDownLatch;





public class Racer extends Robot implements Runnable {

    private String tipoCamino;
    private int ordenSalida;
    private String zona;
    private int miTicket;
    private int miStreet;
    private int miAvenue;
    private Color color;
    private CountDownLatch latch;

    public Racer(int Street, int Avenue, Direction direction, int beepers, Color color, String tipoCamino,
            int ordenSalida, CountDownLatch latch) {
        super(Street, Avenue, direction, beepers, color);
        World.setupThread(this);
        this.latch = latch;
        this.color = color;
        this.tipoCamino = tipoCamino;
        this.ordenSalida = ordenSalida;
        this.miStreet = Street;
        this.miAvenue = Avenue;
    }

    public static void crear_robots(int Street, int Avenue, Direction direction, int beepers, Color color,
            String tipoCamino, int numRobots, CountDownLatch latch) {
        int ordenSalida = 0;
        while (numRobots > 0) {
            Racer robot = new Racer(Street, Avenue, direction, beepers, color, tipoCamino, ordenSalida, latch);
            Thread thrd = new Thread(robot);
            thrd.start();
            numRobots--;
            ordenSalida++;
        }

    }

    // Nuevo move controlado por el Tablero
    @Override
    public void move() {
        int nextStreet = miStreet;
        int nextAvenue = miAvenue;

        if (facingNorth())
            nextStreet++;
        else if (facingSouth())
            nextStreet--;
        else if (facingEast())
            nextAvenue++;
        else if (facingWest())
            nextAvenue--;

        try {
            // Esperar a que la siguiente celda esté libre
            Tablero.ocupar(nextStreet, nextAvenue);

            // Liberar la celda actual
            Tablero.liberar(miStreet, miAvenue);

            // Ahora sí mover el robot en Karel
            super.move();
            miStreet = nextStreet;
            miAvenue = nextAvenue;

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void camino_largo_azul() {
        for (int i = 0; i < 4; i++)
            move();
        turnLeft();
        for (int i = 0; i < 10; i++)
            move();
        turnLeft();
        for (int i = 0; i < 3; i++)
            move();
        turnRight();
        for (int i = 0; i < 3; i++)
            move();
        turnRight();
        for (int i = 0; i < 8; i++)
            move();
        turnRight();
        for (int i = 0; i < 4; i++)
            move();
        turnRight();
        for (int i = 0; i < 3; i++)
            move();
        turnLeft();
        for (int i = 0; i < 5; i++)
            move();
        turnLeft();
        for (int i = 0; i < 7; i++)
            move();
        turnLeft();
        for (int i = 0; i < 5; i++)
            move();
        turnRight();
        for (int i = 0; i < 3; i++)
            move();

        try{
            Tablero.zonaVerde.acquire();
        for (int i = 0; i < 6; i++)
            move();
        }catch(InterruptedException e) {
        e.printStackTrace();
        }finally{
            Tablero.zonaVerde.release();
        }
        move();
        turnLeft();
        for (int i = 0; i < 6; i++)
            move();
    }

    public void camino_corto_azul() {
        for (int i = 0; i < 23; i++) {
            move();
        }
        turnLeft();

        // Continuar el camino normalmente
        for (int i = 0; i < 10; i++) {
            move();
        }
    }

    public void camino_corto_verde() {
        move();
    if (Tablero.zonaVerde.tryAcquire()) {  
        try {
            // --- Recorrido dentro de la zona verde ---
            for (int i = 0; i < 1; i++) move();
            turnLeft();
            for (int i = 0; i < 7; i++) move();
            // aquí el robot ya salió de la zona verde
        } finally {
            Tablero.zonaVerde.release(); // libera el permiso
        }
    } else {
        // Si ya hay 6 adentro, se va por el camino largo
        camino_largo_verde();
        return;
    }
        

        turnRight();
        for (int i = 0; i < 5; i++)
            move();
        turnRight();
        for (int i = 0; i < 1; i++)
            move();
        turnLeft();
        for (int i = 0; i < 4; i++)
            move();
        turnRight();
        for (int i = 0; i < 3; i++)
            move();
        turnRight();
        for (int i = 0; i < 1; i++)
            move();
        turnLeft();
        for (int i = 0; i < 5; i++)
            move();
        turnLeft();
        for (int i = 0; i < 1; i++)
            move();
        turnRight();
        for (int i = 0; i < 5; i++)
            move();
        turnRight();
        for (int i = 0; i < 1; i++)
            move();
        turnLeft();
        for (int i = 0; i < 8; i++)
            move();
    }

    public void camino_largo_verde() {
        for (int i = 0; i > 1; i++)
            move();
        turnRight();
        for (int i = 0; i < 3; i++)
            move();
        turnRight();
        for (int i = 0; i < 8; i++)
            move();
        turnLeft();
        for (int i = 0; i < 2; i++)
            move();
        turnLeft();
        for (int i = 0; i < 4; i++)
            move();
        turnRight();
        for (int i = 0; i < 17; i++)
            move();
        turnLeft();
        for (int i = 0; i < 5; i++)
            move();
        turnLeft();
        for (int i = 0; i < 9; i++)
            move();
        turnRight();
        for (int i = 0; i < 8; i++)
            move();
        turnRight();
        for (int i = 0; i < 2; i++)
            move();
    }

    public void run() {
        pickbeepers();
        try {
            latch.await(); // espera a que todos los robots estén listos
            Thread.sleep(ordenSalida * 300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (tipoCamino.equals("largo") && this.color == Color.BLUE) {
            camino_largo_azul();
        } else if (tipoCamino.equals("corto") && this.color == Color.BLUE) {
            camino_corto_azul();
        } else if (tipoCamino.equals("corto") && this.color == Color.GREEN) {
            camino_corto_verde();
        } else if (tipoCamino.equals("largo") && this.color == Color.GREEN) {
            camino_largo_verde();
        }
    }

    private void pickbeepers() {
        for (int i = 0; i < 4; i++) {
            if (nextToABeeper()) {
                pickBeeper();
            }
        }
    }

    public void turnRight() {
        turnLeft();
        turnLeft();
        turnLeft();
    }
}
