package Cliente;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import javax.swing.text.*;

public class Cliente {
    public static final int PUERTO = 2018;
    public static final String IP_SERVER = "localhost";
    
    private JFrame ventana;
    private JTextPane areaChat; // Cambiado a JTextPane para mejor control del formato
    private JTextField campoMensaje;
    private JButton botonEnviar;
    private JButton botonLimpiar;
    private PrintStream salida;
    private String nombre;
    private Socket socketAlServidor;
    private SimpleAttributeSet estiloMensaje;

    public Cliente() {
        inicializarEstilos();
        inicializarInterfaz();
        conectarAlServidor();
    }

    private void inicializarEstilos() {
        estiloMensaje = new SimpleAttributeSet();
        StyleConstants.setForeground(estiloMensaje, Color.BLACK);
        StyleConstants.setFontFamily(estiloMensaje, "Arial");
        StyleConstants.setFontSize(estiloMensaje, 14);
    }

    private void inicializarInterfaz() {
        // Configuración de la ventana principal
        ventana = new JFrame("Chat Grupal 💬");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(600, 400);
        ventana.setLayout(new BorderLayout(5, 5));

        // Panel de chat
        areaChat = new JTextPane();
        areaChat.setEditable(false);
        areaChat.setBackground(new Color(245, 245, 245));
        JScrollPane scrollChat = new JScrollPane(areaChat);
        ventana.add(scrollChat, BorderLayout.CENTER);

        // Panel superior con botón de limpiar
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botonLimpiar = new JButton("Limpiar chat 🗑️");
        botonLimpiar.setBackground(new Color(220, 220, 220));
        botonLimpiar.setForeground(Color.BLACK);
        panelSuperior.add(botonLimpiar);
        ventana.add(panelSuperior, BorderLayout.NORTH);

        // Panel inferior para enviar mensajes
        JPanel panelInferior = new JPanel(new BorderLayout(5, 5));
        campoMensaje = new JTextField();
        campoMensaje.setFont(new Font("Arial", Font.PLAIN, 14));
        botonEnviar = new JButton("Enviar 📤");
        botonEnviar.setBackground(new Color(100, 149, 237));
        botonEnviar.setForeground(Color.WHITE);
        
        panelInferior.add(campoMensaje, BorderLayout.CENTER);
        panelInferior.add(botonEnviar, BorderLayout.EAST);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        ventana.add(panelInferior, BorderLayout.SOUTH);

        // Eventos
        botonEnviar.addActionListener(e -> enviarMensaje());
        botonLimpiar.addActionListener(e -> limpiarChat());
        campoMensaje.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    enviarMensaje();
                }
            }
        });

        ventana.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (salida != null) {
                    salida.println("FIN");
                }
            }
        });
    }

    private void limpiarChat() {
        areaChat.setText("");
    }

    private void agregarMensaje(String mensaje) {
        try {
            StyledDocument doc = areaChat.getStyledDocument();
            doc.insertString(doc.getLength(), mensaje + "\n", estiloMensaje);
            areaChat.setCaretPosition(doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void conectarAlServidor() {
        try {
            socketAlServidor = new Socket(IP_SERVER, PUERTO);
            salida = new PrintStream(socketAlServidor.getOutputStream());
            
            // Solicitar nombre mediante diálogo
            nombre = JOptionPane.showInputDialog(ventana, 
                "Introduce tu nombre:", 
                "Bienvenido al Chat 👋", 
                JOptionPane.QUESTION_MESSAGE);
            
            if (nombre == null || nombre.trim().isEmpty()) {
                System.exit(0);
            }

            salida.println("NOMBRE:" + nombre);
            ventana.setTitle("Chat Grupal 💬 - " + nombre);
            ventana.setVisible(true);

            // Iniciar receptor de mensajes
            new Thread(new ReceptorMensajes(socketAlServidor)).start();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, 
                "Error al conectar con el servidor 😞",
                "Error de conexión",
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void enviarMensaje() {
        String mensaje = campoMensaje.getText().trim();
        if (!mensaje.isEmpty()) {
            salida.println(mensaje);
            // Mostrar tu propio mensaje en el chat
            agregarMensaje("Tú: " + mensaje);
            campoMensaje.setText("");
        }
        campoMensaje.requestFocus();
    }

    private class ReceptorMensajes implements Runnable {
        private BufferedReader entrada;

        public ReceptorMensajes(Socket socket) {
            try {
                entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                String mensaje;
                while ((mensaje = entrada.readLine()) != null) {
                    // Si el mensaje es tuyo, no lo muestres de nuevo
                    if (!mensaje.startsWith("Tú: ")) {
                        final String mensajeFinal = mensaje;
                        SwingUtilities.invokeLater(() -> agregarMensaje(mensajeFinal));
                    }
                }
            } catch (IOException e) {
                SwingUtilities.invokeLater(() -> 
                    agregarMensaje("⚠️ Conexión perdida con el servidor"));
            }
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new Cliente());
    }
}
