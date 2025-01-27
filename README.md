# Adivinar el Número Seguro

Este proyecto implementa un juego de adivinanza del número utilizando **sockets seguros (SSL/TLS)** en Java. Se compone de un servidor y un cliente que se comunican de manera segura para jugar.

## Descripción del Proyecto

El programa tiene tres componentes principales:
1. **Servidor**: Genera un número aleatorio entre 1 y 100, espera a que un cliente se conecte y maneja sus intentos para adivinar el número secreto. Proporciona pistas al cliente (`Mayor`, `Menor`, `Correcto`).
2. **Cliente**: Se conecta al servidor de manera segura y permite que el usuario intente adivinar el número secreto introduciéndolo en la consola.
3. **Clase Main**: Inicia tanto el servidor como el cliente. El servidor se ejecuta en un hilo separado para garantizar la concurrencia.

El juego implementa SSL/TLS para garantizar la seguridad en la comunicación entre el cliente y el servidor, utilizando certificados generados con la herramienta `keytool`.

## Cómo Ejecutar el Proyecto

### Requisitos Previos
- Java JDK 11 o superior instalado.
- Herramienta `keytool` incluida en la instalación de Java.
- Conexión local para ejecutar el cliente y el servidor.

### Generar Certificados
Primero, necesitas generar los certificados necesarios para el servidor y el cliente:
```bash
keytool -genkeypair -alias servidor -keyalg RSA -keysize 2048 -validity 365 -keystore claves/keystore.jks -storepass alejandro
```

Exporta el certificado del servidor:
```
keytool -exportcert -alias servidor -keystore claves/keystore.jks -file claves/servidor.cer -storepass alejandro
```

Importa el certificado del servidor al `truststore` del cliente:
```
keytool -importcert -alias servidor -file claves/servidor.cer -keystore claves/truststore.jks -storepass alejandro
```

### Compilación y Generación del JAR
Compila el proyecto:
 ```
javac Main.java Servidor.java Cliente.java
 ```

Genera el archivo JAR ejecutable:
 ```
jar cfe Ejecutable.jar Main *.class claves/*
```

### Ejecución
Ejecuta el programa desde la consola:
```
java -jar Ejecutable.jar
 ```

### Flujo de Ejecución
1. El servidor se inicia y genera un número secreto.
2. El cliente se conecta al servidor y recibe el mensaje inicial.
3. El cliente ingresa intentos para adivinar el número, y el servidor responde con pistas (`Mayor`, `Menor` o `Correcto`).
4. Una vez que el cliente adivina el número, ambos terminan su ejecución.

## Organización del Código

### Archivos Principales
- `Main.java`: Clase principal que inicia el servidor y el cliente.
- `Servidor.java`: Implementa el servidor seguro.
- `Cliente.java`: Implementa el cliente seguro.


### Requisitos de Seguridad
- Los archivos `keystore.jks` y `truststore.jks` deben estar en la carpeta `claves/`.
- Las contraseñas de los archivos KeyStore y TrustStore deben ser coherentes con el código fuente.

## Documentación
La documentación completa del código puede generarse con Javadoc. Usa el siguiente comando para generar los archivos HTML:
```
javadoc -d doc Main.java Servidor.java Cliente.java
```

Abre el archivo `doc/index.html` en tu navegador para ver la documentación.

## Bibliotecas Usadas
El proyecto utiliza exclusivamente las bibliotecas estándar de Java:
- `javax.net.ssl.*`
- `java.security.*`
- `java.util.*`
- `java.io.*`
