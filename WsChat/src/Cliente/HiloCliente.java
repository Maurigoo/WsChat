package Cliente;

import java.io.*;
import java.net.*;
import java.util.*;

import Servidor.SocketServidor;

public class HiloCliente implements Runnable {
    private Socket socket;
    private PrintStream salida;

    public HiloCliente(Socket socket) {
        this.socket = socket;
        new Thread(this).start();
    }

    @Override
    public void run() {
        try (
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            salida = new PrintStream(socket.getOutputStream());
            synchronized (SocketServidor.clientes) {
                SocketServidor.clientes.add(salida); // Añadir salida a la lista de clientes
            }

            String mensaje;
            while ((mensaje = entrada.readLine()) != null) {
                if (mensaje.equalsIgnoreCase("FIN")) {
                    break;
                }
                System.out.println("SERVIDOR: Mensaje recibido -> " + mensaje);
                retransmitirMensaje("Otro cliente: " + mensaje, salida);
            }
        } catch (IOException e) {
            System.err.println("HILO CLIENTE: Error en la conexión.");
        } finally {
            desconectarCliente();
        }
    }

    private void retransmitirMensaje(String mensaje, PrintStream remitente) {
        synchronized (SocketServidor.clientes) {
            for (PrintStream cliente : SocketServidor.clientes) {
                if (cliente != remitente) {
                    cliente.println(mensaje);
                }
            }
        }
    }

    private void desconectarCliente() {
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("HILO CLIENTE: Error al cerrar conexión.");
        }
        synchronized (SocketServidor.clientes) {
            SocketServidor.clientes.remove(salida); // Eliminar salida de la lista
        }
        System.out.println("HILO CLIENTE: Cliente desconectado.");
    }
}