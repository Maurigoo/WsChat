package Cliente;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {
    public static final int PUERTO = 2018;
    public static final String IP_SERVER = "172.126.101.150"; // Cambiar si es necesario

    public static void main(String[] args) {
        System.out.println("        CLIENTE DE CHAT GRUPAL       ");
        System.out.println("-------------------------------------");

        try (
            Socket socketAlServidor = new Socket(); 
            Scanner sc = new Scanner(System.in)
        ) {
            socketAlServidor.connect(new InetSocketAddress(IP_SERVER, PUERTO));
            System.out.println("CLIENTE: Conectado al servidor.");

            try (
                PrintStream salida = new PrintStream(socketAlServidor.getOutputStream());
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socketAlServidor.getInputStream()))
            ) {
                // Hilo para escuchar mensajes del servidor
                Thread receptor = new Thread(() -> {
                    try {
                        String mensaje;
                        while ((mensaje = entrada.readLine()) != null) {
                            System.out.println("\n" + mensaje); // Mostrar mensajes del servidor
                        }
                    } catch (IOException e) {
                        System.err.println("CLIENTE: Conexión cerrada por el servidor.");
                    }
                });
                receptor.start();

                // Entrada del usuario
                String texto;
                while (true) {
                    System.out.print("Tú: ");
                    texto = sc.nextLine();
                    salida.println(texto); // Enviar mensaje al servidor

                    if ("FIN".equalsIgnoreCase(texto)) {
                        break; // Terminar si el cliente escribe "FIN"
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("CLIENTE: Error de conexión con el servidor.");
            e.printStackTrace();
        }
    }
}
