package Cliente;

import java.io.*;
import java.net.*;
import java.util.*;

import Servidor.SocketServidor;

/**
 * La clase HiloCliente implementa la interfaz Runnable y se encarga de manejar la comunicación
 * entre el servidor y un cliente específico en un chat.
 */
public class HiloCliente implements Runnable {
    private final Socket socket;
    private PrintStream salida;
    private String nombre;

    /**
     * Constructor de la clase HiloCliente.
     *
     * @param socket El socket asociado al cliente.
     */
    public HiloCliente(Socket socket) {
        this.socket = socket;
        new Thread(this).start();
    }

    /**
     * Este método se encarga de manejar la comunicación con el cliente.
     * Primero, se lee el nombre del cliente y se anuncia su conexión a los demás clientes.
     * Luego, se entra en un bucle para leer los mensajes del cliente y retransmitirlos a los demás.
     * Si se recibe el mensaje "FIN", se sale del bucle y se desconecta el cliente.
     * Si se produce un error de entrada/salida, se imprime un mensaje de error y se desconecta el cliente.
     */
    @Override
    public void run() {
        try (
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            salida = new PrintStream(socket.getOutputStream());
            synchronized (SocketServidor.clientes) {
                SocketServidor.clientes.add(salida); // Añadir salida a la lista de clientes
            }

            // Leer el nombre del cliente
            String primerMensaje = entrada.readLine();
            if (primerMensaje.startsWith("NOMBRE:")) {
                nombre = primerMensaje.substring(7).trim(); // Extraer el nombre
                System.out.println("SERVIDOR: Cliente conectado con nombre -> " + nombre);
                retransmitirMensaje(nombre + " se ha unido al chat.", null);
            }

            String mensaje;
            while ((mensaje = entrada.readLine()) != null) {
                if (mensaje.equalsIgnoreCase("FIN")) {
                    break;
                }
                System.out.println("SERVIDOR: Mensaje recibido de " + nombre + " -> " + mensaje);
                retransmitirMensaje(nombre + ": " + mensaje, salida);
            }
        } catch (IOException e) {
            System.err.println("HILO CLIENTE: Error en la conexión.");
        } finally {
            desconectarCliente();
        }
    }

    /**
     * Retransmite un mensaje a todos los clientes conectados, excepto al remitente.
     *
     * @param mensaje El mensaje a retransmitir.
     * @param remitente El PrintStream del cliente remitente, para evitar enviarle su propio mensaje.
     */
    private void retransmitirMensaje(String mensaje, PrintStream remitente) {
        synchronized (SocketServidor.clientes) {
            for (PrintStream cliente : SocketServidor.clientes) {
                if (cliente != remitente) {
                    cliente.println(mensaje);
                }
            }
        }
    }

    /**
     * Desconecta al cliente, cierra el socket y elimina su PrintStream de la lista de clientes.
     */
    private void desconectarCliente() {
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("HILO CLIENTE: Error al cerrar conexión.");
        }
        synchronized (SocketServidor.clientes) {
            SocketServidor.clientes.remove(salida); // Eliminar salida de la lista
        }
        System.out.println("HILO CLIENTE: Cliente " + nombre + " desconectado.");
        retransmitirMensaje(nombre + " ha salido del chat.", null);
    }
}