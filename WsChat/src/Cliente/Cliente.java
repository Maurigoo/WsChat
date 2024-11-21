package Cliente;

import javax.swing.*;
import java.io.*;
import java.net.*;

public class Cliente {
    public static final int PUERTO = 2018;
    public static final String IP_SERVER = "localhost"; // Cambiar si es necesario


    /**
     * El método main es el punto de entrada del cliente de chat grupal. Se encarga de:
     * <ul>
     *     <li>Crear la interfaz gráfica (JFrame, JTextArea, panel de entrada de texto)</li>
     *     <li>Conectarse al servidor de chat</li>
     *     <li>Mostrar los mensajes del servidor en el JTextArea</li>
     *     <li>Recibir la entrada del usuario y enviarla al servidor</li>
     *     <li>Desconectarse del servidor cuando el cliente escribe "FIN"</li>
     * </ul>
     * @param args los argumentos de línea de comandos
     */

    public static void main(String[] args) {
        // Crear la interfaz gráfica
        JFrame ventana = new JFrame("Cliente de Chat Grupal");
        ventana.setTitle("Cliente de Mensajeria");
        ventana.setSize(450, 500);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crear un JTextArea dentro de un JScrollPane para mostrar los mensajes
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false); // Evita que el usuario edite el área de texto
        JScrollPane scrollPane = new JScrollPane(textArea);
        ventana.add(scrollPane, "Center");

        // Crear un panel para la entrada de texto y añadirlo a la ventana
        JPanel panel = new JPanel();
        ventana.add(panel, "South");

        // Botón de enviar mensaje
       /* JButton botonEnviar = new JButton("Enviar Mensaje");
        panel.add(botonEnviar);
        botonEnviar.addActionListener(e -> {

        });*/
        ventana.setVisible(true);

        try (
                Socket socketAlServidor = new Socket();
                BufferedReader sc = new BufferedReader(new InputStreamReader(System.in))
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
                            // Agregar el mensaje recibido al JTextArea
                            textArea.append("\n" + mensaje);
                            // Desplazar la barra de desplazamiento hacia abajo para ver el último mensaje
                            textArea.setCaretPosition(textArea.getDocument().getLength());
                        }
                    } catch (IOException e) {
                        System.err.println("CLIENTE: Conexión cerrada por el servidor.");
                    }
                });
                receptor.start();

                // Entrada del nombre (usamos JOptionPane para una ventana de entrada)
                String nombre = JOptionPane.showInputDialog("Introduce tu nombre: ");

                // Verificar si el nombre es válido
                if (nombre != null && !nombre.trim().isEmpty()) {
                    salida.println("NOMBRE:" + nombre); // Enviar el nombre al servidor
                } else {
                    JOptionPane.showMessageDialog(null, "El nombre no puede estar vacío.");
                    return; // Salir si no se ingresa un nombre válido
                }

                // Entrada del usuario
                String texto;
                while (true) {
                    // Mostrar un cuadro de diálogo para que el usuario ingrese el mensaje
                    texto = JOptionPane.showInputDialog("Mensaje: ");
                    if (texto != null && !texto.isEmpty()) {
                        salida.println(texto); // Enviar mensaje al servidor
                        // Mostrar el mensaje enviado en el JTextArea
                        textArea.append("\n" + nombre + ": " + texto);
                        textArea.setCaretPosition(textArea.getDocument().getLength());
                    }

                    if ("FIN".equalsIgnoreCase(texto)) {
                        ventana.dispose();
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
