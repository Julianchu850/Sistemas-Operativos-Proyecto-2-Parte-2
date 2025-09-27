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

        try {
            Tablero.zonaVerde.acquire();
            for (int i = 0; i < 6; i++)
                move();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            Tablero.zonaVerde.release();
        }
        move();
        turnLeft();
        for (int i = 0; i < 6; i++)
            move();
        for (int i = 0; i < 4; i++) {
            putBeeper();
        }
        turnLeft();
        for (int i = 0; i < 1; i++) {
            move();
        }
        turnLeft();
        for (int i = 0; i < 1; i++) {
            move();
        }
        turnRight();
        for (int i = 0; i < 6; i++) {
            move();
        }
        turnLeft();
        for (int i = 0; i < 1; i++) {
            move();
        }
        turnLeft();
        for (int i = 0; i < 6; i++) {
            move();
        }
        turnRight();
        for (int i = 0; i < 2; i++) {
            move();
        }
        turnRight();
        for (int i = 0; i < 1; i++) {
            move();
        }
        turnRight();
        for (int i = 0; i < 1; i++) {
            move();
        }
        turnLeft();
        for (int i = 0; i < 5; i++) {
            move();
        }
        turnLeft();
        for (int i = 0; i < 1; i++) {
            move();
        }
        pickbeepers();
        camino_corto_verde();

    }

    // funcion para camino corto
    public void camino_corto_azul() {
        for (int i = 0; i < 4; i++)
            move();

            // semaforo para verificar si va a camino corto o largo
        if (Tablero.bahiaEspera.tryAcquire()) {
            try {
                for (int i = 0; i < 4; i++)
                    move();
                Thread.sleep(1000); // reduce la probabilidad de congestion

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                Tablero.bahiaEspera.release(); // libera el permiso
            }
        } else {
            camino_largo_azul();
            return;
        }

        // verifica si es posible pasar por una de las zonas criticas
        try {
            Tablero.bahiaLarga.entrar("Este");
            for (int i = 0; i < 7; i++)
                move();
            Tablero.bahiaLarga.salir();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 3; i++)
            move();

        try {
            Tablero.bahiaCorta.entrar("Este");

            for (int i = 0; i < 5; i++)
                move();

            Tablero.bahiaCorta.salir();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        turnLeft();
        // Continuar el camino normalmente
        for (int i = 0; i < 3; i++) {
            move();
        }
        try {
            Tablero.bahiaVertical.entrar("Norte");
            for (int i = 0; i < 7; i++) {
                move();
            }
            Tablero.bahiaVertical.salir();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // continua su recorrido
        for (int i = 0; i < 5; i++)
            move();
        for (int i = 0; i < 4; i++) {
            putBeeper();
        }
        turnLeft();
        for (int i = 0; i < 1; i++) {
            move();
        }
        turnLeft();
        for (int i = 0; i < 1; i++) {
            move();
        }
        turnRight();
        for (int i = 0; i < 6; i++) {
            move();
        }
        turnLeft();
        for (int i = 0; i < 1; i++) {
            move();
        }
        turnLeft();
        for (int i = 0; i < 6; i++) {
            move();
        }
        turnRight();
        for (int i = 0; i < 2; i++) {
            move();
        }
        turnRight();
        for (int i = 0; i < 1; i++) {
            move();
        }
        turnRight();
        for (int i = 0; i < 1; i++) {
            move();
        }
        turnLeft();
        for (int i = 0; i < 5; i++) {
            move();
        }
        turnLeft();
        for (int i = 0; i < 1; i++) {
            move();
        }
        pickbeepers();
        camino_corto_verde();

    }

    public void camino_corto_verde() {
        move();
        if (Tablero.zonaVerde.tryAcquire()) {
            try {
                //  semafoto para verificar si debe ir por el camino corto o largo
                for (int i = 0; i < 1; i++)
                    move();
                turnLeft();
                for (int i = 0; i < 6; i++)
                    move();
                // salimos de la zona del semaforo
            } finally {
                Tablero.zonaVerde.release(); // libera el permiso
            }
        } else {
            // Si ya hay 6 adentro, se va por el camino largo
            camino_largo_verde();
            return;
        }

        // pasamos por zonas criticas
        try {
            Tablero.bahiaVertical.entrar("Sur");
            move();
            turnRight();
            for (int i = 0; i < 5; i++)
                move();
            turnRight();
            for (int i = 0; i < 1; i++)
                move();
            Tablero.bahiaVertical.salir();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        turnLeft();
        for (int i = 0; i < 3; i++)
            move();

        try {
            Tablero.bahiaCorta.entrar("Oeste");
            move();
            turnRight();
            for (int i = 0; i < 3; i++)
                move();
            turnRight();
            for (int i = 0; i < 1; i++)
                move();
            Tablero.bahiaCorta.salir();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        turnLeft();
        for (int i = 0; i < 5; i++)
            move();
        turnLeft();

        try {
            Tablero.bahiaLarga.entrar("Oeste");
            for (int i = 0; i < 1; i++)
                move();
            turnRight();
            for (int i = 0; i < 5; i++)
                move();
            turnRight();
            for (int i = 0; i < 1; i++)
                move();

            Tablero.bahiaLarga.salir();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        turnLeft();
        for (int i = 0; i < 8; i++)
            move();

        turnRight();
        for (int i = 0; i < 2; i++) {
            move();
        }
        turnLeft();
        for (int i = 0; i < 7; i++) {
            move();
        }
        for (int i = 0; i < 4; i++) {
            putBeeper();
        }
        turnLeft();
        for (int i = 0; i < 1; i++) {
            move();
        }
        turnLeft();
        for (int i = 0; i < 6; i++) {
            move();
        }
        turnRight();
        for (int i = 0; i < 1; i++) {
            move();
        }
        turnRight();
        for (int i = 0; i < 6; i++) {
            move();
        }
        turnLeft();
        for (int i = 0; i < 1; i++) {
            move();
        }
        turnLeft();
        for (int i = 0; i < 6; i++) {
            move();
        }
        pickbeepers();
        camino_corto_azul();

    }

    // funcion para camino largo
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

        turnRight();
        for (int i = 0; i < 2; i++) {
            move();
        }
        turnLeft();
        for (int i = 0; i < 7; i++) {
            move();
        }
        for (int i = 0; i < 4; i++) {
            putBeeper();
        }
        turnLeft();
        for (int i = 0; i < 1; i++) {
            move();
        }
        turnLeft();
        for (int i = 0; i < 6; i++) {
            move();
        }
        turnRight();
        for (int i = 0; i < 1; i++) {
            move();
        }
        turnRight();
        for (int i = 0; i < 6; i++) {
            move();
        }
        turnLeft();
        for (int i = 0; i < 1; i++) {
            move();
        }
        turnLeft();
        for (int i = 0; i < 6; i++) {
            move();
        }
        pickbeepers();
        camino_corto_azul();
    }

    public void run() {
        pickbeepers();
        try {
            latch.await(); // espera a que todos los robots estén listos
            Thread.sleep(ordenSalida * 300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // verificamos recorrido inicial
        if (tipoCamino.equals("corto") && this.color == Color.BLUE) {
            camino_corto_azul();
        } else if (tipoCamino.equals("corto") && this.color == Color.GREEN) {
            camino_corto_verde();
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

    private void putbeepers() {
        for (int i = 0; i < 4; i++) {
            putBeeper();
        }
    }

}