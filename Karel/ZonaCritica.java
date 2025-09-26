import java.util.concurrent.locks.*;

public class ZonaCritica {
    private final Lock lock = new ReentrantLock();
    private final Condition condicion = lock.newCondition();

    private String sentidoActivo = null;
    private int contadorSentido = 0;
    private int robotsEnZona = 0;
    private final int capacidad;

    public ZonaCritica(int capacidad) {
        this.capacidad = capacidad;
    }

    public void entrar(String sentido) throws InterruptedException {
        lock.lock();
        try {
            // Esperar hasta que pueda entrar
            while (!puedeEntrar(sentido)) {
                condicion.await();
            }
            
            // Actualizar estado
            if (sentidoActivo == null) {
                sentidoActivo = sentido;
            }
            robotsEnZona++;
            contadorSentido++;
            
        } finally {
            lock.unlock();
        }
    }

    private boolean puedeEntrar(String sentido) {
        if (sentidoActivo == null) return true; // Zona libre
        if (!sentidoActivo.equals(sentido)) return false; // Sentido contrario
        return contadorSentido < capacidad; // Verificar capacidad
    }

    public void salir() {
        lock.lock();
        try {
            robotsEnZona--;
            
            // Si la zona se vació, cambiar el sentido
            if (robotsEnZona == 0) {
                sentidoActivo = null;
                contadorSentido = 0;
                condicion.signalAll(); // Despertar a todos los esperando
            }
            // Si se completó el lote pero aún hay robots adentro, 
            // esperar a que salgan todos antes de cambiar sentido
            
        } finally {
            lock.unlock();
        }
    }
    
    public String sentidoContrario(String sentido) {
        switch (sentido) {
            case "Norte": return "Sur";
            case "Sur": return "Norte";
            case "Este": return "Oeste";
            case "Oeste": return "Este";
            default: return sentido;
        }
    }
}