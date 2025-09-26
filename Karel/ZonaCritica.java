import java.util.concurrent.locks.*;

public class ZonaCritica {
    private final Lock lock = new ReentrantLock();
    private final Condition cambio = lock.newCondition();

    private String sentidoActual = null;
    private int enZona = 0;
    private int pasaronEnTurno = 0;
    private final int capacidad;

    public ZonaCritica(int capacidad) {
        this.capacidad = capacidad;
    }

    public String sentidoContrario(String sentido) {
        switch (sentido) {
            case "Norte": return "Sur";
            case "Sur":   return "Norte";
            case "Este":  return "Oeste";
            case "Oeste": return "Este";
            default: throw new IllegalArgumentException("Sentido inválido: " + sentido);
        }
    }

    public void entrar(String sentido) throws InterruptedException {
    lock.lock();
    try {
        // esperar si hay un sentido contrario en curso
        while (sentidoActual != null && !sentidoActual.equals(sentido)) {
            cambio.await();
        }

        // si está vacía, me adueño del sentido
        if (sentidoActual == null) {
            sentidoActual = sentido;
            pasaronEnTurno = 0;
        }

        // si ya se completó el lote de este sentido → esperar al próximo turno
            if (pasaronEnTurno >= capacidad) {
                        // cuando se vacíe, el turno pasa al contrario
            sentidoActual = sentidoContrario(sentido);
            pasaronEnTurno = 0;
            cambio.signalAll();
            // esperar hasta que la zona quede vacía
            while (enZona > 0 && !sentido.equals(sentidoActual)) {
                cambio.await();
            }
            if (sentidoActual == null) {
            sentidoActual = sentido;
            pasaronEnTurno = 0;
            }
            pasaronEnTurno = 0;


            
        }

        // ahora sí entro
        enZona++;
        pasaronEnTurno++;
    } finally {
        lock.unlock();
    }
}


    public void salir() {
        lock.lock();
        try {
            enZona--;
            if (enZona == 0) {
                // si quedó vacía, cambio turno de sentido
                sentidoActual = null;
                cambio.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }
}
