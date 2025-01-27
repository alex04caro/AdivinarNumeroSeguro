import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;
import java.util.Scanner;

/**
 * Clase principal del cliente seguro para el juego "Adivinar el Número".
 * Se conecta al servidor SSL/TLS y envía intentos para adivinar el número secreto.
 */
public class Cliente {
    public static void main(String[] args) throws Exception {
        // Configurar TrustStore del cliente
        KeyStore trustStore = KeyStore.getInstance("JKS");
        try (FileInputStream fis = new FileInputStream("claves/truststore.jks")) {
            trustStore.load(fis, "alejandro".toCharArray()); // Contraseña del TrustStore
        }

        // Configurar TrustManagerFactory
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);

        // Configurar contexto SSL
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);

        // Conectar al servidor
        SSLSocketFactory ssf = sslContext.getSocketFactory();
        try (SSLSocket socket = (SSLSocket) ssf.createSocket("localhost", 8443);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println("Conexión establecida con el servidor.");

            // Leer mensaje inicial
            System.out.println(in.readLine());

            Scanner scanner = new Scanner(System.in);
            String respuesta;
            do {
                System.out.print("Tu intento: ");
                String intento = scanner.nextLine();
                out.println(intento);

                respuesta = in.readLine();
                System.out.println("Servidor: " + respuesta);
            } while (!"Correcto".equals(respuesta));

            System.out.println("¡Adivinaste el número!");
        }
    }
}
