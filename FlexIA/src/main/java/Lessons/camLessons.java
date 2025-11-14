/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Lessons;

import Back_End.FuenteUtil;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.*;
import java.net.Socket;
import javax.swing.*;
import java.io.*;

/**
 *
 * @author kfkdm
 */
public class camLessons extends javax.swing.JFrame {

    int xmouse, ymouse;

    /**
     * Creates new form camLessons
     */
    public camLessons() {
        checkLabel = new JLabel();  // ✅ crear el JLabel ANTES
        initComponents();           // ahora ya existe y no será null
        initStyles();
        startCameraStream();

        setSize(905, 680);
        setResizable(false);
        setLocationRelativeTo(null);

        checkLabel.setHorizontalAlignment(SwingConstants.CENTER);
        checkLabel.setPreferredSize(new Dimension(100, 100));
    }


    private void initStyles() {
        Closetxt.setFont(FuenteUtil.cargarFuente("EpundaSlab-ExtraBold.ttf", 30f));
        Closetxt.setForeground(new Color(250, 250, 250));
        minimizetxt.setFont(FuenteUtil.cargarFuente("EpundaSlab-EXtrabold.ttf", 30f));
        minimizetxt.setForeground(new Color(250, 250, 250));
        titlelbl.setFont(FuenteUtil.cargarFuente("EpundaSlab-ExtraBold.ttf", 28f));
        titlelbl.setForeground(new Color(250, 250, 250));

    }

    private void startCameraStream() {
        new Thread(() -> {
            try {
                // 1️⃣ Iniciar el servidor Python automáticamente
                System.out.println("Iniciando script de Python...");
                ProcessBuilder pb = new ProcessBuilder("python", "Back-End/leccionCamara.py");
                pb.redirectErrorStream(true);
                pythonProcess = pb.start();

                // Mostrar salida de Python en consola Java
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

                // 2️⃣ Intentar conexión con reintentos
                Socket socket = null;
                int intentos = 0;
                while (socket == null && intentos < 10) {
                    try {
                        Thread.sleep(1000);
                        socket = new Socket("localhost", 9999);
                        System.out.println("Conectado al servidor Python.");
                    } catch (IOException e) {
                        intentos++;
                        System.out.println("Intentando conectar con Python... (" + intentos + ")");
                    }
                }

                if (socket == null) {
                    System.out.println("No se pudo conectar con Python después de varios intentos.");
                    pythonProcess.destroyForcibly();
                    SwingUtilities
                            .invokeLater(() -> cameraLabel.setText("No se pudo conectar con el servidor Python"));
                    return;
                }

                DataInputStream input = new DataInputStream(socket.getInputStream());

                // 3️⃣ Ciclo principal: recibir frames o mensajes
                while (true) {
                    int size;
                    try {
                        size = input.readInt(); // lee el tamaño del paquete
                    } catch (EOFException e) {
                        System.out.println("Conexión cerrada por Python.");
                        break;
                    }

                    if (size <= 0)
                        continue;

                    byte[] data = new byte[size];
                    input.readFully(data);

                    // 🧩 Si el mensaje es STATUS:OK → mostrar check
                    String mensaje = new String(data);
                    if (mensaje.startsWith("STATUS:OK")) {
                        System.out.println("Ejercicio completado (STATUS:OK)");
                        SwingUtilities.invokeLater(() -> {
                            try {
                                ImageIcon checkIcon = new ImageIcon(getClass().getResource("/Images/check.png"));
                                Image img = checkIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                                checkLabel.setIcon(new ImageIcon(img));
                            } catch (Exception ex) {
                                System.out.println("No se pudo cargar el ícono del check: " + ex.getMessage());
                            }
                        });
                        continue;
                    }

                    // 🧩 Si no es texto, asumimos que es un frame JPEG
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

                // 4️⃣ Cerrar todo correctamente
                input.close();
                socket.close();

                if (pythonProcess.isAlive()) {
                    pythonProcess.destroy();
                    Thread.sleep(500);
                    if (pythonProcess.isAlive()) {
                        pythonProcess.destroyForcibly();
                    }
                }

                System.out.println("🔚 Conexión y proceso Python cerrados correctamente.");

            } catch (Exception e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> cameraLabel.setText("❌ Error al conectar con la cámara"));
            }
        }).start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bg = new javax.swing.JPanel();
        header = new javax.swing.JPanel();
        titlelbl = new javax.swing.JLabel();
        Closebtn = new javax.swing.JPanel();
        Closetxt = new javax.swing.JLabel();
        minimizebtn = new javax.swing.JPanel();
        minimizetxt = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

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

        cameraLabel = new javax.swing.JLabel();
        cameraLabel.setBackground(Color.BLACK);
        cameraLabel.setOpaque(true);
        cameraLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cameraLabel.setText("Cámara cargando...");

        javax.swing.GroupLayout bgLayout = new javax.swing.GroupLayout(bg);
        bg.setLayout(bgLayout);
        bgLayout.setHorizontalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(header, javax.swing.GroupLayout.DEFAULT_SIZE,
                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(bgLayout.createSequentialGroup()
                    .addGap(30)
                    .addComponent(cameraLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 533,
                            javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(30)
                    .addComponent(checkLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 100,
                            javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(30, Short.MAX_VALUE))
        );
        bgLayout.setVerticalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(bgLayout.createSequentialGroup()
                    .addComponent(header, javax.swing.GroupLayout.PREFERRED_SIZE, 40,
                            javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(20)
                    .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(cameraLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 569,
                                javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(checkLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 100,
                                javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 51, Short.MAX_VALUE))
        );


        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(bg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.PREFERRED_SIZE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(bg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void ClosetxtMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_ClosetxtMouseClicked
        System.exit(0);
    }// GEN-LAST:event_ClosetxtMouseClicked

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

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        FlatLightLaf.setup();

        // Crear la ventana principal
        camLessons ventana = new camLessons();
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Agregar un listener para cuando se cierre la ventana
        ventana.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.out.println("Cerrando Flex-IA y el proceso Python...");
                ventana.cerrarProcesoPython(); // 🔹 mata el proceso si lo guardas como atributo
            }
        });

        // Mostrar la ventana (en el hilo de eventos de Swing)
        java.awt.EventQueue.invokeLater(() -> ventana.setVisible(true));
    }

    // 🔹 Iniciar el servidor Python automáticamente
    private void iniciarServidorPython() {
        try {
            System.out.println("? Iniciando script de Python...");
            ProcessBuilder pb = new ProcessBuilder("python", "leccionCamara.py");
            pb.redirectErrorStream(true); // combinar salida y errores
            pythonProcess = pb.start();

            // Hilo para leer salida del proceso y ver mensajes
            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(pythonProcess.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println("[PYTHON] " + line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Esperar unos segundos para que el servidor Python arranque
            Thread.sleep(2000);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("No se pudo iniciar el servidor Python.");
        }
    }

    // 🔹 Método para cerrar el proceso de Python al cerrar la ventana
    private void cerrarProcesoPython() {
        if (pythonProcess != null && pythonProcess.isAlive()) {
            try {
                pythonProcess.destroy(); // intento normal
                Thread.sleep(500); // espera medio segundo por si tarda un poco
                if (pythonProcess.isAlive()) {
                    pythonProcess.destroyForcibly(); // fuerza la terminación si no murió aún
                    System.out.println("Proceso Python forzado a terminar.");
                } else {
                    System.out.println("Proceso de Python terminado correctamente.");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Closebtn;
    private javax.swing.JLabel Closetxt;
    private javax.swing.JPanel bg;
    private javax.swing.JPanel header;
    private javax.swing.JPanel minimizebtn;
    private javax.swing.JLabel minimizetxt;
    private javax.swing.JLabel titlelbl;
    private javax.swing.JLabel cameraLabel;
    private JLabel checkLabel;
    private Process pythonProcess;

    // End of variables declaration//GEN-END:variables
}
