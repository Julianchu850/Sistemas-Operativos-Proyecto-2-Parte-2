import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.*;


public class Tablero {
    private static final int MAX_STREETS = 30; // ajustar tamaño de matriz = tamaño mundo
    private static final int MAX_AVENUES = 30;
    private static final int[][] celdas = new int[MAX_STREETS + 1][MAX_AVENUES + 1];
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition disponible = lock.newCondition();
     //  zona critica entre la avenida 23 y 29 de la calle 10
    public static final Semaphore zonaVerde = new Semaphore(6); 
    // zona critica de la primera bahia entre las avenidas 12 y 15 de la calle 1
    public static final Semaphore bahiaEspera = new Semaphore(4); 
    // Bahia de doble sentido entre la avenida 16 y 21 de la calle 1

    public static ZonaCritica bahiaVertical = new ZonaCritica(4);
    public static ZonaCritica bahiaLarga = new ZonaCritica(4);
    public static ZonaCritica bahiaCorta = new ZonaCritica(4);







    public static void ocupar(int street, int avenue) throws InterruptedException {
        lock.lock();
        try {
            while (celdas[street][avenue] == 1) {
                disponible.await(); // espero hasta que la celda se libere
            }
            celdas[street][avenue] = 1; // marco la celda como ocupada
        } finally {
            lock.unlock();
        }
    }

    public static void liberar(int street, int avenue) {
        lock.lock();
        try {
            celdas[street][avenue] = 0; // libero la celda
            disponible.signalAll(); // aviso a los demás robots
        } finally {
            lock.unlock();
        }
    }
}
