package Interfaz;

import Rounded.RoundedPanel;
import com.formdev.flatlaf.FlatLightLaf;

import Login.FuenteUtil;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

/**
 * @author Karol
 */
public class Ejercicio extends javax.swing.JFrame {

    private Process pythonProcess;
    private JLabel cameraLabel;
    private JLabel checkLabel;
    private int xmouse, ymouse;
    private String tituloEjercicio; // Nuevo campo
    private Socket socket; // Agregar esta variable

    // Constructor por defecto (mantener para compatibilidad)
    public Ejercicio() {
        this("Ejercicio Predeterminado"); // Llamar al nuevo constructor
    }

    // Nuevo constructor con parámetros
    public Ejercicio(String tituloEjercicio) {
        this.tituloEjercicio = tituloEjercicio;
        initComponents();

        // Agregar WindowListener para cerrar recursos al cerrar la ventana
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                cerrarRecursos();
            }
        });

        startCameraStream(tituloEjercicio);

        // Actualizar título de la ventana con el nombre del ejercicio
        if (tituloEjercicio != null && !tituloEjercicio.equals("Ejercicio Predeterminado")) {
            titlelbl.setText("EJERCICIO - " + tituloEjercicio);
        }

        setSize(905, 680);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private void startCameraStream(String tipoEjercicio) {
        new Thread(() -> {
            try {
                System.out.println("Iniciando script de Python...");
                String scriptPython = determinarScriptPython(tipoEjercicio);

                ProcessBuilder pb = new ProcessBuilder("python", scriptPython);
                pb.redirectErrorStream(true);
                pythonProcess = pb.start();

                new Thread(() -> {
                    try (BufferedReader reader = new BufferedReader(
                            new InputStreamReader(pythonProcess.getInputStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            System.out.println("[PYTHON] " + line);
                        }
                    } catch (IOException e) {
                        System.out.println("Error leyendo salida de Python: " + e.getMessage());
                    }
                }).start();

                socket = null; // Usar la variable de instancia
                int intentos = 0;
                while (socket == null && intentos < 10) {
                    try {
                        Thread.sleep(1000);
                        socket = new Socket("localhost", 9999); // Asignar a la variable de instancia
                        System.out.println("Conectado al servidor Python.");
                    } catch (IOException e) {
                        intentos++;
                        System.out.println("Intentando conectar con Python... (" + intentos + ")");
                    }
                }

                if (socket == null) {
                    System.out.println("No se pudo conectar con Python después de varios intentos.");
                    pythonProcess.destroyForcibly();
                    SwingUtilities.invokeLater(() -> cameraLabel.setText("No se pudo conectar con el servidor Python"));
                    return;
                }

                DataInputStream input = new DataInputStream(socket.getInputStream());

                while (true) {
                    int size;
                    try {
                        size = input.readInt();
                    } catch (EOFException e) {
                        System.out.println("Conexión cerrada por Python.");
                        break;
                    } catch (SocketException e) {
                        System.out.println("Socket cerrado: " + e.getMessage());
                        break;
                    }

                    if (size <= 0)
                        continue;

                    byte[] data = new byte[size];
                    input.readFully(data);

                    String mensaje = new String(data);
                    if (mensaje.startsWith("STATUS:")) {
                        if (mensaje.equals("STATUS:OK")) {
                            SwingUtilities.invokeLater(() -> {
                                ejercicioCompletado = true;
                                mostrarCheck();
                            });
                            System.out.println("Ejercicio completado (STATUS:OK)");
                        } else if (mensaje.equals("STATUS:RESET")) {
                            SwingUtilities.invokeLater(() -> {
                                ejercicioCompletado = false;
                                ocultarCheck();
                            });
                            System.out.println("Ejercicio no completado (STATUS:RESET)");
                        }
                        continue;
                    }

                    try {
                        Image image = javax.imageio.ImageIO.read(new ByteArrayInputStream(data));
                        if (image != null) {
                            SwingUtilities.invokeLater(() -> {
                                cameraLabel.setIcon(new ImageIcon(image));
                                cameraLabel.setText("");
                            });
                        }
                    } catch (Exception ex) {
                        System.out.println("No se pudo decodificar frame: " + ex.getMessage());
                    }
                }

                input.close();
                socket.close();

                cerrarProcesoPython();

                System.out.println("Conexión y proceso Python cerrados correctamente.");

            } catch (Exception e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> cameraLabel.setText("❌ Error al conectar con la cámara"));
            }
        }).start();
    }

    // Método para cerrar todos los recursos
    private void cerrarRecursos() {
        System.out.println("🔴 Cerrando recursos...");

        // Cerrar socket
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
                System.out.println("✅ Socket cerrado");
            } catch (IOException e) {
                System.out.println("❌ Error cerrando socket: " + e.getMessage());
            }
        }

        // Cerrar proceso Python
        cerrarProcesoPython();
    }

    private void cerrarProcesoPython() {
        if (pythonProcess != null && pythonProcess.isAlive()) {
            System.out.println("🔴 Cerrando proceso Python...");
            pythonProcess.destroy();
            try {
                // Esperar a que el proceso termine
                if (pythonProcess.waitFor(3, TimeUnit.SECONDS)) {
                    System.out.println("✅ Proceso Python cerrado correctamente");
                } else {
                    pythonProcess.destroyForcibly();
                    System.out.println("⚠️ Proceso Python forzado a cerrar");
                }
            } catch (InterruptedException e) {
                pythonProcess.destroyForcibly();
                System.out.println("⚠️ Proceso Python forzado a cerrar (interrumpido)");
            }
        }
    }

    // Agrega esta variable como campo de la clase
    private boolean ejercicioCompletado = false;

    private void startCameraStream() {
        startCameraStream("Ejercicio Predeterminado"); // Llamar al método con parámetro
    }

    // Modifica el método startCameraStream()
    private String determinarScriptPython(String tipoEjercicio) {
        // Mapear títulos de ejercicio a scripts Python específicos
        switch (tipoEjercicio.toLowerCase()) {
            case "ejercicio #1":
                return "Back-End/ejercicio1.py"; // ❌ CAMBIAR: ejercicio1.py -> ejercicio2.py
            case "ejercicio #2":
                return "Back-End/ejercicio2.py";
            case "garra":
                return "Back-End/ejercicio2.py"; // ❌ CAMBIAR: ejercicio2.py -> ejercicio1.py
            case "dedos separados":
                return "Back-End/ejercicio3.py";
            case "pulgar a índice":
                return "Back-End/ejercicio4.py";
            case "pulgar a meñique":
                return "Back-End/ejercicio5.py";
            case "ok sign":
                return "Back-End/ejercicio6.py";
            case "extensión lateral":
                return "Back-End/ejercicio7.py";
            case "flexión de muñeca":
                return "Back-End/ejercicio8.py";
            case "extensión de muñeca":
                return "Back-End/ejercicio9.py";
            case "toques":
                return "Back-End/ejercicio10.py";
            case "dedos en pinzas":
                return "Back-End/ejercicio11.py";
            case "paz y amor":
                return "Back-End/ejercicio12.py";
            default:
                return "Back-End/leccionCamara.py"; // Script por defecto
        }
    }

    // Agrega estos métodos para mostrar/ocultar el check
    // Agrega estos métodos para mostrar/ocultar el check
    private void mostrarCheck() {
        try {
            ImageIcon checkIcon = new ImageIcon(getClass().getResource("/Images/check.png"));
            Image img = checkIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH); // Tamaño aumentado
            checkLabel.setIcon(new ImageIcon(img));
            checkLabel.setVisible(true);

            // Forzar repintado del panel
            checkLabel.revalidate();
            checkLabel.repaint();
        } catch (Exception ex) {
            System.out.println("No se pudo cargar el ícono del check: " + ex.getMessage());
        }
    }

    private void ocultarCheck() {
        checkLabel.setIcon(null);
        checkLabel.setVisible(false);

        // Forzar repintado del panel
        checkLabel.revalidate();
        checkLabel.repaint();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        checkLabel = new JLabel();
        checkLabel.setVisible(false); // Inicialmente oculto
        checkLabel.setHorizontalAlignment(SwingConstants.CENTER);
        checkLabel.setPreferredSize(new Dimension(100, 100));

        jPanel1 = new javax.swing.JPanel();
        header = new javax.swing.JPanel();
        titlelbl = new javax.swing.JLabel();
        minimizebtn = new javax.swing.JPanel();
        minimizetxt = new javax.swing.JLabel();
        Closebtn = new javax.swing.JPanel();
        Closetxt = new javax.swing.JLabel();
        ButtonSiguiente = new javax.swing.JButton();
        ButtonAnterior = new javax.swing.JButton();
        roundedPanel1 = new RoundedPanel();

        // NUEVO: Panel para el check en la derecha
        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(new BorderLayout());
        panelDerecho.setBackground(new Color(245, 245, 250));
        panelDerecho.setPreferredSize(new Dimension(300, 540));

        cameraLabel = new JLabel("Cámara cargando...");
        cameraLabel.setOpaque(true);
        cameraLabel.setBackground(Color.BLACK);
        cameraLabel.setHorizontalAlignment(SwingConstants.CENTER);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(250, 250, 250));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        // HEADER (código existente se mantiene igual)
        header.setBackground(new java.awt.Color(30, 56, 136));
        header.setPreferredSize(new java.awt.Dimension(680, 57));
        header.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                headerMouseDragged(evt);
            }
        });
        header.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                headerMouseClicked(evt);
            }

            public void mousePressed(java.awt.event.MouseEvent evt) {
                headerMousePressed(evt);
            }
        });

        titlelbl.setBackground(new java.awt.Color(250, 250, 250));
        titlelbl.setFont(new java.awt.Font("Epunda Slab Light", 1, 36)); // NOI18N
        titlelbl.setText("FLEX-IA");

        Closebtn.setBackground(new java.awt.Color(30, 56, 136));

        Closetxt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Closetxt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/cerrar.png"))); // NOI18N
        Closetxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ClosetxtMouseClicked(evt);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ClosetxtMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                ClosetxtMouseExited(evt);
            }
        });

        javax.swing.GroupLayout ClosebtnLayout = new javax.swing.GroupLayout(Closebtn);
        Closebtn.setLayout(ClosebtnLayout);
        ClosebtnLayout.setHorizontalGroup(
                ClosebtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(Closetxt, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE));
        ClosebtnLayout.setVerticalGroup(
                ClosebtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(ClosebtnLayout.createSequentialGroup()
                                .addComponent(Closetxt, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap()));

        minimizebtn.setBackground(new java.awt.Color(30, 56, 136));

        minimizetxt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        minimizetxt.setText("-");
        minimizetxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                minimizetxtMouseClicked(evt);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                minimizetxtMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                minimizetxtMouseExited(evt);
            }
        });

        javax.swing.GroupLayout minimizebtnLayout = new javax.swing.GroupLayout(minimizebtn);
        minimizebtn.setLayout(minimizebtnLayout);
        minimizebtnLayout.setHorizontalGroup(
                minimizebtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, minimizebtnLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(minimizetxt, javax.swing.GroupLayout.PREFERRED_SIZE, 60,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)));
        minimizebtnLayout.setVerticalGroup(
                minimizebtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(minimizetxt, javax.swing.GroupLayout.PREFERRED_SIZE, 46,
                                javax.swing.GroupLayout.PREFERRED_SIZE));

        javax.swing.GroupLayout headerLayout = new javax.swing.GroupLayout(header);
        header.setLayout(headerLayout);
        headerLayout.setHorizontalGroup(
                headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(headerLayout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addComponent(titlelbl, javax.swing.GroupLayout.PREFERRED_SIZE, 166,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 590,
                                        Short.MAX_VALUE)
                                .addComponent(minimizebtn, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Closebtn, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));
        headerLayout.setVerticalGroup(
                headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(titlelbl, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(minimizebtn, javax.swing.GroupLayout.Alignment.LEADING,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        Short.MAX_VALUE)
                                .addComponent(Closebtn, javax.swing.GroupLayout.Alignment.LEADING,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        Short.MAX_VALUE)));
        header.add(Closebtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 0, 40, 40));

        jPanel1.add(header, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 905, 40));

        // BOTONES
        ButtonSiguiente.setBackground(new java.awt.Color(152, 206, 255));
        ButtonSiguiente.setFont(new java.awt.Font("Epunda Slab", 0, 20));
        ButtonSiguiente.setText("Siguiente");
        jPanel1.add(ButtonSiguiente, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 610, 120, 40));

        ButtonAnterior.setBackground(new java.awt.Color(152, 206, 255));
        ButtonAnterior.setFont(new java.awt.Font("Epunda Slab", 0, 20));
        ButtonAnterior.setText("Anterior");
        jPanel1.add(ButtonAnterior, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 610, 120, 40));

        // PANEL CÁMARA (solo la cámara, sin el check)
        roundedPanel1.setLayout(new BorderLayout());
        roundedPanel1.add(cameraLabel, BorderLayout.CENTER);
        jPanel1.add(roundedPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 530, 540));

        // NUEVO: Panel derecho con el check
        // Configurar el checkLabel para que esté centrado en el panel derecho
        checkLabel.setHorizontalAlignment(SwingConstants.CENTER);
        checkLabel.setVerticalAlignment(SwingConstants.CENTER);

        // Crear un panel contenedor para el check con márgenes
        JPanel checkContainer = new JPanel(new BorderLayout());
        checkContainer.setBackground(new Color(245, 245, 250));
        checkContainer.add(checkLabel, BorderLayout.CENTER);
        checkContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panelDerecho.add(checkContainer, BorderLayout.CENTER);

        // Agregar el panel derecho a la interfaz
        jPanel1.add(panelDerecho, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 60, 325, 540));

        getContentPane().add(jPanel1);
        pack();
    }

    private void ClosetxtMouseClicked(java.awt.event.MouseEvent evt) {
        cerrarRecursos();
        this.dispose();
    }

    private void ClosetxtMouseEntered(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_ClosetxtMouseEntered
        Closebtn.setBackground(Color.red);
        Closetxt.setForeground(new Color(250, 250, 250));
    }// GEN-LAST:event_ClosetxtMouseEntered

    private void ClosetxtMouseExited(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_ClosetxtMouseExited
        Closebtn.setBackground(new Color(30, 56, 136));
        Closetxt.setForeground(new Color(250, 250, 250));
    }// GEN-LAST:event_ClosetxtMouseExited

    private void minimizetxtMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_minimizetxtMouseClicked
        this.setState(JFrame.ICONIFIED);
    }// GEN-LAST:event_minimizetxtMouseClicked

    private void minimizetxtMouseEntered(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_minimizetxtMouseEntered
        minimizebtn.setBackground(Color.decode("#2e4ca9"));
    }// GEN-LAST:event_minimizetxtMouseEntered

    private void minimizetxtMouseExited(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_minimizetxtMouseExited
        minimizebtn.setBackground(new Color(30, 56, 136));
        minimizetxt.setForeground(new Color(250, 250, 250));
    }// GEN-LAST:event_minimizetxtMouseExited

    private void headerMouseDragged(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_headerMouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xmouse, y - ymouse);
    }// GEN-LAST:event_headerMouseDragged

    private void headerMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_headerMouseClicked
        // TODO add your handling code here:
    }// GEN-LAST:event_headerMouseClicked

    private void headerMousePressed(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_headerMousePressed
        xmouse = evt.getX();
        ymouse = evt.getY();
    }// GEN-LAST:event_headerMousePressed

    public static void main(String args[]) {
        FlatLightLaf.setup();
        Ejercicio ventana = new Ejercicio();
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                ventana.cerrarProcesoPython();
            }
        });
        java.awt.EventQueue.invokeLater(() -> ventana.setVisible(true));
    }

    // Variables
    private javax.swing.JButton ButtonAnterior;
    private javax.swing.JButton ButtonSiguiente;
    private javax.swing.JPanel Closebtn;
    private javax.swing.JLabel Closetxt;
    private javax.swing.JPanel header;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel minimizebtn;
    private javax.swing.JLabel minimizetxt;
    private javax.swing.JLabel titlelbl;
    private Rounded.RoundedPanel roundedPanel1;
}
