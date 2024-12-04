package Servidor;

import java.io.*;
import java.net.*;
import java.util.*;

import Cliente.HiloCliente;

public class SocketServidor {
    public static final int PUERTO = 2018;
    public static List<PrintStream> clientes = Collections.synchronizedList(new ArrayList<>());


    /**
     * Programa principal del servidor de chat grupal.
     *
     * <p>
     * Este programa crea un servidor que se encarga de recibir conexiones de clientes y gestionarlas en un hilo aparte. Cada hilo se encarga de leer los mensajes del cliente, mostrarlos por consola y retransmitirlos a los dem s clientes conectados.
     *
     * <p>
     * El programa utiliza un puerto de red determinado y se encarga de gestionar las conexiones concurrentes de los clientes.
     *
     * @param args No se utiliza
     */

    public static void main(String[] args) {
        System.out.println("        SERVIDOR DE CHAT GRUPAL       ");
        System.out.println("--------------------------------------");

        try (ServerSocket servidor = new ServerSocket(PUERTO)) {
            System.out.println("SERVIDOR: Esperando conexiones en el puerto " + PUERTO);

            while (true) {
                Socket socketAlCliente = servidor.accept();
                System.out.println("SERVIDOR: Cliente conectado desde " + socketAlCliente.getInetAddress());
                new HiloCliente(socketAlCliente); // Delegar manejo del cliente al hilo
            }
        } catch (IOException e) {
            System.err.println("SERVIDOR: Error de entrada/salida.");
            e.printStackTrace();
        }
    }
}
