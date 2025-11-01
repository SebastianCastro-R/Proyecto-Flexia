package Interfaz;

/**
 *
 * @author Karol
 */

/**
 * Creates new form Instrucciones
 */

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class Instrucciones extends javax.swing.JFrame {

    private String tituloEjercicio;
    private String descripcionEjercicio;
    private String archivo;
    private String instruccionesAdicionales;

    // Constructor modificado
    public Instrucciones(String tituloEjercicio, String descripcionEjercicio, String archivo,
            String instruccionesAdicionales) {
        this.tituloEjercicio = tituloEjercicio;
        this.descripcionEjercicio = descripcionEjercicio;
        this.archivo = archivo;
        this.instruccionesAdicionales = instruccionesAdicionales;
        initComponents();
        personalizarInterfaz();
    }

    private void personalizarInterfaz() {
        // Actualizar título de la ventana
        Titulo.setText("FLEX-IA - " + tituloEjercicio);

        // Configurar panel de video
        configurarPanelVideo();

        // Configurar información del ejercicio
        configurarInformacionEjercicio();
    }

    private void configurarPanelVideo() {
        JPanel panelContenido = new JPanel(new BorderLayout());
        panelContenido.setBackground(new Color(250, 250, 250));
        panelContenido.setBorder(new EmptyBorder(20, 50, 20, 50));

        JLabel lblTitulo = new JLabel(tituloEjercicio, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Epunda Slab ExtraBold", Font.PLAIN, 28));
        lblTitulo.setForeground(new Color(30, 56, 136));
        lblTitulo.setBorder(new EmptyBorder(0, 0, 20, 0));
        panelContenido.add(lblTitulo, BorderLayout.NORTH);

        // ✅ Panel JavaFX dentro de Swing
        JFXPanel jfxPanel = new JFXPanel();
        jfxPanel.setPreferredSize(new Dimension(800, 450));

        Platform.runLater(() -> {
            try {
                WebView webView = new WebView();
                WebEngine engine = webView.getEngine();

                // Depurar la URL del video
                System.out.println("URL del video: " + archivo);

                // Crear contenido HTML mejorado
                String html = String.format("""
                        <!DOCTYPE html>
                        <html>
                        <head>
                            <style>
                                body {
                                    margin: 0;
                                    padding: 0;
                                    background: black;
                                    display: flex;
                                    align-items: center;
                                    justify-content: center;
                                    height: 100%%;
                                }
                                video {
                                    max-width: 100%%;
                                    max-height: 100%%;
                                }
                            </style>
                        </head>
                        <body>
                            <video controls autoplay muted playsinline>
                                <source src="%s" type="video/mp4">
                                <source src="%s" type="video/webm">
                                Tu navegador no soporta el elemento video.
                            </video>
                        </body>
                        </html>
                        """, archivo, archivo);

                engine.loadContent(html);

                // Agregar listener para errores
                engine.setOnError(e -> {
                    System.err.println("Error cargando video: " + e.getMessage());
                    System.err.println("URL problemática: " + archivo);
                });

                // Agregar listener para estado de carga
                engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
                    switch (newState) {
                        case SUCCEEDED:
                            System.out.println("Video cargado exitosamente");
                            break;
                        case FAILED:
                            System.err.println("Error al cargar el video");
                            break;
                    }
                });

                jfxPanel.setScene(new Scene(webView));

            } catch (Exception e) {
                System.err.println("Error en JavaFX: " + e.getMessage());
                e.printStackTrace();

                // Mostrar mensaje de error en caso de fallo
                JLabel errorLabel = new JLabel("Error al cargar el video: " + e.getMessage());
                errorLabel.setForeground(Color.RED);
                jfxPanel.setLayout(new BorderLayout());
                jfxPanel.add(errorLabel, BorderLayout.CENTER);
            }
        });

        panelContenido.add(jfxPanel, BorderLayout.CENTER);
        jPanel1.add(panelContenido, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 1440, 600));
    }

    private void configurarInformacionEjercicio() {
        // Panel principal de información con diseño mejorado
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(new Color(250, 250, 250));
        infoPanel.setBorder(new CompoundBorder(
                new LineBorder(new Color(180, 200, 240), 2, true), // Borde más suave y redondeado
                new EmptyBorder(25, 40, 25, 40)));

        // Panel contenedor principal usando GridLayout para dos columnas
        JPanel contenedorPrincipal = new JPanel(new GridLayout(1, 2, 40, 0)); // 2 columnas, 40px de separación
        contenedorPrincipal.setBackground(new Color(250, 250, 250));
        contenedorPrincipal.setBorder(new EmptyBorder(10, 20, 10, 20));

        // --- COLUMNA IZQUIERDA: DESCRIPCIÓN ---
        JPanel panelDescripcion = new JPanel(new BorderLayout());
        panelDescripcion.setBackground(new Color(250, 250, 250));
        panelDescripcion.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 0, 2, new Color(230, 230, 250)), // Línea divisoria suave
                new EmptyBorder(0, 0, 0, 20)));

        // Título Descripción
        JLabel lblDescTitle = new JLabel("Descripción del Ejercicio");
        lblDescTitle.setFont(new Font("Epunda Slab ExtraBold", Font.PLAIN, 22));
        lblDescTitle.setForeground(new Color(30, 56, 136));
        lblDescTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        panelDescripcion.add(lblDescTitle, BorderLayout.NORTH);

        // Área de texto de descripción
        JTextArea txtDescripcion = new JTextArea(descripcionEjercicio);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setEditable(false);
        txtDescripcion.setBackground(new Color(245, 247, 255)); // Fondo ligeramente azul
        txtDescripcion.setFont(new Font("Lato", Font.PLAIN, 16));
        txtDescripcion.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 230, 255), 1, true),
                new EmptyBorder(15, 15, 15, 15)));
        txtDescripcion.setPreferredSize(new Dimension(300, 120));

        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);
        scrollDescripcion.setBorder(null);
        scrollDescripcion.setBackground(new Color(245, 247, 255));
        panelDescripcion.add(scrollDescripcion, BorderLayout.CENTER);

        // --- COLUMNA DERECHA: INSTRUCCIONES ---
        JPanel panelInstrucciones = new JPanel(new BorderLayout());
        panelInstrucciones.setBackground(new Color(250, 250, 250));

        // Título Instrucciones
        JLabel lblInstruccionesTitle = new JLabel("Instrucciones Adicionales");
        lblInstruccionesTitle.setFont(new Font("Epunda Slab ExtraBold", Font.PLAIN, 22));
        lblInstruccionesTitle.setForeground(new Color(30, 56, 136));
        lblInstruccionesTitle.setBorder(new EmptyBorder(0, 20, 15, 0));
        panelInstrucciones.add(lblInstruccionesTitle, BorderLayout.NORTH);

        // Área de texto de instrucciones
        String textoInstrucciones = instruccionesAdicionales != null ? instruccionesAdicionales
                : obtenerInstruccionesPorDefecto();
        JTextArea txtInstrucciones = new JTextArea(textoInstrucciones);
        txtInstrucciones.setLineWrap(true);
        txtInstrucciones.setWrapStyleWord(true);
        txtInstrucciones.setEditable(false);
        txtInstrucciones.setBackground(new Color(255, 245, 245)); // Fondo ligeramente rojizo
        txtInstrucciones.setFont(new Font("Lato", Font.PLAIN, 16));
        txtInstrucciones.setBorder(new CompoundBorder(
                new LineBorder(new Color(255, 220, 220), 1, true),
                new EmptyBorder(15, 15, 15, 15)));
        txtInstrucciones.setPreferredSize(new Dimension(300, 120));

        JScrollPane scrollInstrucciones = new JScrollPane(txtInstrucciones);
        scrollInstrucciones.setBorder(null);
        scrollInstrucciones.setBackground(new Color(255, 245, 245));
        panelInstrucciones.add(scrollInstrucciones, BorderLayout.CENTER);

        // Agregar ambos paneles al contenedor principal
        contenedorPrincipal.add(panelDescripcion);
        contenedorPrincipal.add(panelInstrucciones);

        // Agregar al panel principal
        infoPanel.add(contenedorPrincipal, BorderLayout.CENTER);

        // Ajustar la posición y tamaño en el panel principal
        jPanel1.add(infoPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 650, 1360, 220));
    }

    public String obtenerInstruccionesPorDefecto() {
        return "• Realice el ejercicio en un espacio amplio y seguro.\n" +
                "• Mantenga una postura correcta durante todo el ejercicio.\n" +
                "• Si siente dolor, deténgase inmediatamente.\n" +
                "• Repita el ejercicio según las indicaciones de su terapeuta.\n" +
                "• Respire profundamente durante la ejecución del movimiento.";
    }

    // ... (el resto de tu código existente se mantiene igual)

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        Titulo = new javax.swing.JLabel();
        Exit = new javax.swing.JLabel();
        ButtonVolver = new javax.swing.JButton();
        ButtonEjercicio = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(250, 250, 250));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(30, 56, 136));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Titulo.setFont(new java.awt.Font("Epunda Slab ExtraBold", 0, 24)); // NOI18N
        Titulo.setForeground(new java.awt.Color(255, 255, 255));
        Titulo.setText("FLEX-IA");
        jPanel2.add(Titulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 0, -1, 40));

        Exit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/exit.png"))); // NOI18N
        Exit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Exit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ExitMouseClicked(evt);
            }
        });
        jPanel2.add(Exit, new org.netbeans.lib.awtextra.AbsoluteConstraints(1410, 0, 30, 40));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1440, 40));

        ButtonVolver.setBackground(new java.awt.Color(217, 217, 217));
        ButtonVolver.setFont(new java.awt.Font("Epunda Slab", 0, 20)); // NOI18N
        ButtonVolver.setForeground(new java.awt.Color(30, 56, 136));
        ButtonVolver.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/arrow-back.png"))); // NOI18N
        ButtonVolver.setText("Volver");
        ButtonVolver.setToolTipText("");
        ButtonVolver.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ButtonVolver.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ButtonVolverMouseClicked(evt);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ButtonVolverMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                ButtonVolverMouseExited(evt);
            }
        });
        ButtonVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonVolverActionPerformed(evt);
            }
        });
        jPanel1.add(ButtonVolver, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 840, 150, 40));

        ButtonEjercicio.setBackground(new java.awt.Color(30, 56, 136));
        ButtonEjercicio.setFont(new java.awt.Font("Epunda Slab", 0, 20)); // NOI18N
        ButtonEjercicio.setForeground(new java.awt.Color(250, 250, 250));
        ButtonEjercicio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/photo-camera.png"))); // NOI18N
        ButtonEjercicio.setText("Hacer Ejercicio");
        ButtonEjercicio.setToolTipText("");
        ButtonEjercicio.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ButtonEjercicio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ButtonEjercicioMouseClicked(evt);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ButtonEjercicioMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                ButtonEjercicioMouseExited(evt);
            }
        });
        ButtonEjercicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonEjercicioActionPerformed(evt);
            }
        });
        jPanel1.add(ButtonEjercicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(755, 840, 200, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1440,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1024,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ButtonVolverMouseEntered(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_ButtonVolverMouseEntered
        ButtonVolver.setBackground(new Color(0x1E3888));
        ButtonVolver.setForeground(Color.WHITE);
    }// GEN-LAST:event_ButtonVolverMouseEntered

    private void ButtonVolverMouseExited(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_ButtonVolverMouseExited
        ButtonVolver.setBackground(new Color(0xD9D9D9));
        ButtonVolver.setForeground(new Color(0x1E3888));
    }// GEN-LAST:event_ButtonVolverMouseExited

    private void ButtonVolverActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_ButtonVolverActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_ButtonVolverActionPerformed

    private void ButtonEjercicioMouseEntered(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_ButtonEjercicioMouseEntered
        ButtonEjercicio.setBackground(new Color(0xD9D9D9));
        ButtonEjercicio.setForeground(new Color(0x1E3888));
    }// GEN-LAST:event_ButtonEjercicioMouseEntered

    private void ButtonEjercicioMouseExited(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_ButtonEjercicioMouseExited
        ButtonEjercicio.setBackground(new Color(0x1E3888));
        ButtonEjercicio.setForeground(Color.WHITE);
    }// GEN-LAST:event_ButtonEjercicioMouseExited

    private void ButtonEjercicioActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_ButtonEjercicioActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_ButtonEjercicioActionPerformed

    private void ButtonVolverMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_ButtonVolverMouseClicked
        // Cerrar la ventana actual
        this.setVisible(false);

        // Abrir la ventana de LogIn
        Ejercicios Ejercicios = new Ejercicios();
        Ejercicios.setVisible(true);
        Ejercicios.setLocationRelativeTo(null); // Centrar en pantalla
    }// GEN-LAST:event_ButtonVolverMouseClicked

    private void ButtonEjercicioMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_ButtonEjercicioMouseClicked
        // Pasar el título del ejercicio actual a la ventana de Ejercicio
        Ejercicio ejercicio = new Ejercicio(this.tituloEjercicio);
        ejercicio.setVisible(true);
        ejercicio.setLocationRelativeTo(null); // Centrar en pantalla

        // Opcional: cerrar ventana actual
        // this.dispose();
    }// GEN-LAST:event_ButtonEjercicioMouseClicked

    private void ExitMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_ExitMouseClicked
        System.exit(0);
    }// GEN-LAST:event_ExitMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ButtonEjercicio;
    private javax.swing.JButton ButtonVolver;
    private javax.swing.JLabel Exit;
    private javax.swing.JLabel Titulo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}