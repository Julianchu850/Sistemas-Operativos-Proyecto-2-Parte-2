import kareltherobot.*;
import java.awt.Color;

public class Racer extends Robot implements Runnable {
    
    private String tipoCamino; 
    private int ordenSalida;   
    private String zona;       
    private int miTicket;
    private int miStreet;
    private int miAvenue;

    public Racer(int Street, int Avenue, Direction direction, int beepers, Color color, String tipoCamino, String zona, int ordenSalida) {
        super(Street, Avenue, direction, beepers , color);
        World.setupThread(this);
        this.tipoCamino = tipoCamino;
        this.ordenSalida = ordenSalida;
        this.zona = zona;
        this.miStreet = Street;
        this.miAvenue = Avenue;

    }

    // Nuevo move controlado por el Tablero
    @Override
    public void move() {
        int nextStreet = miStreet;
        int nextAvenue = miAvenue;

        if (facingNorth()) nextStreet++;
        else if (facingSouth()) nextStreet--;
        else if (facingEast()) nextAvenue++;
        else if (facingWest()) nextAvenue--;

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


        public void camino_largo_azul() {
        for (int i = 0; i < 4; i++) move();
        turnLeft();
        for (int i = 0; i < 10; i++) move();
        turnLeft();
        for (int i = 0; i < 3; i++) move();
        turnRight();
        for (int i = 0; i < 3; i++) move();
        turnRight();
        for (int i = 0; i < 8; i++) move();
        turnRight();
        for (int i = 0; i < 4; i++) move();
        turnRight();
        for (int i = 0; i < 3; i++) move();
        turnLeft();
        for (int i = 0; i < 5; i++) move();
        turnLeft();
        for (int i = 0; i < 7; i++) move();
        turnLeft();
        for (int i = 0; i < 5; i++) move();
        turnRight();
        for (int i = 0; i < 10; i++) move();
        turnLeft();
        for (int i = 0; i < 2; i++) move();
    }

    public void camino_corto_verde() {
        for (int i = 0; i < 2; i++) move();
        turnLeft();
        for (int i = 0; i < 7; i++) move();
        turnRight();
        for (int i = 0; i < 5; i++) move();
        turnRight();
        for (int i = 0; i < 1; i++) move();
        turnLeft();
        for (int i = 0; i < 4; i++) move();
        turnRight();
        for (int i = 0; i < 3; i++) move();
        turnRight();
        for (int i = 0; i < 1; i++) move();
        turnLeft();
        for (int i = 0; i < 5; i++) move();
        turnLeft();
        for (int i = 0; i < 1; i++) move();
        turnRight();
        for (int i = 0; i < 5; i++) move();
        turnRight();
        for (int i = 0; i < 1; i++) move();
        turnLeft();
        for (int i = 0; i < 8; i++) move();
    }

    public void camino_largo_verde(){
        for (int i = 0; i < 1; i++) move();
        turnRight();
        for (int i = 0; i < 3; i++) move();
        turnRight();
        for (int i = 0; i < 8; i++) move();
        turnLeft();
        for (int i = 0; i < 2; i++) move();
        turnLeft();
        for (int i = 0; i < 4; i++) move();
        turnRight();
        for (int i = 0; i < 17; i++) move();
        turnLeft();
        for (int i = 0; i < 5; i++) move();
        turnLeft();
        for (int i = 0; i < 9; i++) move();
        turnRight();
        for (int i = 0; i < 8; i++) move();
        turnRight();
        for (int i = 0; i < 2; i++) move();
    }


    public void run() {
        pickbeepers();
        try {
            Thread.sleep(ordenSalida * 600); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (tipoCamino.equals("largo")) {
            camino_largo_azul();
        } else if (tipoCamino.equals("corto")) {
            camino_corto_azul();
        } else if (tipoCamino.equals("cortoVerde")) {
            camino_corto_verde();
        } else if (tipoCamino.equals("largoVerde")) {
            camino_largo_verde();
        }
    }

    private void pickbeepers(){
        for(int i = 0; i<4; i++){
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
