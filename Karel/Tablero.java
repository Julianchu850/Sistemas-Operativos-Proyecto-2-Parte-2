import java.util.concurrent.locks.*;
import java.util.concurrent.Semaphore;


public class Tablero {
    private static final int MAX_STREETS = 30; // ajustar tamaño de matriz = tamaño mundo
    private static final int MAX_AVENUES = 30;
    private static final int[][] celdas = new int[MAX_STREETS + 1][MAX_AVENUES + 1];
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition disponible = lock.newCondition();
    public static final Semaphore zonaVerde = new Semaphore(6); // solo 1 robot en A
    public static final Semaphore zonaAzul = new Semaphore(4); // hasta 2 robots en B
    

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
