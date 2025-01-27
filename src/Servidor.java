import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;
import java.util.Random;

/**
 * Clase principal del servidor seguro para el juego "Adivinar el Número".
 * Configura un servidor SSL/TLS que acepta una única conexión de cliente
 * y permite que este intente adivinar un número secreto.
 */
public class Servidor {

    public static void main(String[] args) {
        try {
            SSLServerSocket serverSocket = configurarServidorSSL();
            System.out.println("Servidor iniciado en puerto 8443, esperando a un cliente...");

            // Generar número aleatorio
            int numeroSecreto = generarNumeroSecreto();
            System.out.println("Número secreto generado: " + numeroSecreto);

            // Esperar y manejar conexión de cliente
            manejarCliente(serverSocket, numeroSecreto);

            System.out.println("Servidor cerrado.");
        } catch (Exception e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }

    /**
     * Configura un servidor SSL/TLS utilizando un KeyStore para la autenticación.
     *
     * @return Un objeto SSLServerSocket configurado para aceptar conexiones seguras.
     * @throws Exception Si ocurre algún error durante la configuración del servidor.
     */
    private static SSLServerSocket configurarServidorSSL() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        try (FileInputStream fis = new FileInputStream("claves/keystore.jks")) {
            keyStore.load(fis, "alejandro".toCharArray()); // Contraseña del KeyStore
        }

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, "alejandro".toCharArray()); // Contraseña de la clave privada

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), null, null);

        SSLServerSocketFactory ssf = sslContext.getServerSocketFactory();
        return (SSLServerSocket) ssf.createServerSocket(8443);
    }

    /**
     * Genera un número aleatorio entre 1 y 100 que será el número secreto del juego.
     *
     * @return Un número entero aleatorio entre 1 y 100.
     */
    private static int generarNumeroSecreto() {
        return new Random().nextInt(100) + 1;
    }

    /**
     * Maneja la conexión con un único cliente, permitiendo que intente adivinar el número secreto.
     *
     * @param serverSocket  El servidor SSL configurado para aceptar conexiones.
     * @param numeroSecreto El número secreto que el cliente debe adivinar.
     */
    private static void manejarCliente(SSLServerSocket serverSocket, int numeroSecreto) {
        try (SSLSocket socket = (SSLSocket) serverSocket.accept();
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println("Cliente conectado.");

            // Iniciar interacción con el cliente
            out.println("Bienvenido al juego 'Adivina el Número'. Intenta adivinar entre 1 y 100.");
            procesarIntentosCliente(in, out, numeroSecreto);

        } catch (IOException e) {
            System.err.println("Error en la conexión con el cliente: " + e.getMessage());
        }
    }

    /**
     * Procesa los intentos del cliente para adivinar el número secreto.
     *
     * @param in            Objeto BufferedReader para leer mensajes del cliente.
     * @param out           Objeto PrintWriter para enviar mensajes al cliente.
     * @param numeroSecreto El número secreto que el cliente debe adivinar.
     * @throws IOException Si ocurre algún error en la comunicación con el cliente.
     */
    private static void procesarIntentosCliente(BufferedReader in, PrintWriter out, int numeroSecreto) throws IOException {
        boolean acertado = false;

        while (!acertado) {
            String intento = in.readLine(); // Leer intento del cliente
            if (intento == null) break;

            try {
                int numeroIntento = Integer.parseInt(intento);

                // Comparar intento con el número secreto y enviar pista
                if (numeroIntento < numeroSecreto) {
                    out.println("Mayor");
                } else if (numeroIntento > numeroSecreto) {
                    out.println("Menor");
                } else {
                    out.println("Correcto");
                    acertado = true;
                }
            } catch (NumberFormatException e) {
                out.println("Por favor, introduce un número válido.");
            }
        }

        System.out.println("El cliente adivinó el número.");
    }
}
