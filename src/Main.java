/**
 * La clase {@code Main} es la clase principal que se encarga de iniciar tanto el servidor
 * como el cliente para el juego de adivinanza del número.
 * El servidor se ejecuta en un hilo separado para permitir la ejecución concurrente
 * entre el cliente y el servidor.
 *
 * <p>Este programa utiliza un servidor que espera conexiones de clientes en un puerto
 * específico y un cliente que se conecta a dicho servidor para jugar. El servidor maneja
 * la lógica del juego, y el cliente envía los intentos para adivinar el número secreto.
 * </p>
 */
public class Main {

    /**
     * El método {@code main} inicia la ejecución del servidor y el cliente.
     * El servidor se ejecuta en un hilo separado para permitir que el cliente se conecte
     * de manera simultánea.
     *
     * <p>Después de iniciar el servidor, el hilo principal espera un breve tiempo para
     * asegurarse de que el servidor está listo antes de iniciar el cliente.</p>
     *
     * @param args Los argumentos de la línea de comandos (si se proporcionan).
     */
    public static void main(String[] args) {

        // Iniciar el servidor en un hilo separado
        Thread servidorThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Llamar al método main de la clase Servidor para iniciar el servidor
                Servidor.main(args);
            }
        });

        // Iniciar el hilo del servidor
        servidorThread.start();

        // Esperar un poco para asegurarse de que el servidor está en ejecución
        try {
            // Hacemos que el hilo principal espere 1000 milisegundos (1 segundo)
            // para darle tiempo al servidor de iniciarse antes de que el cliente intente conectarse.
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // Si ocurre una interrupción durante el sleep, se imprime la excepción.
            e.printStackTrace();
        }

        // Iniciar el cliente
        try {
            // Llamar al método main de la clase Cliente para iniciar el cliente
            Cliente.main(args);
        } catch (Exception e) {
            // Si ocurre una excepción durante la ejecución del cliente, se lanza una RuntimeException
            throw new RuntimeException(e);
        }
    }
}
