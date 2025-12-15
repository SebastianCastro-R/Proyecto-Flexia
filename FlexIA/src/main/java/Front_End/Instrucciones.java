package Front_End;

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

import Back_End.SesionUsuario;
import Back_End.Usuario;
import Database.VideosDAO;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class Instrucciones extends javax.swing.JFrame {

    static {
        // Evita que JavaFX termine cuando se cierran todas las ventanas con JFXPanel.
        // Esto es clave para poder reabrir Instrucciones y que el video vuelva a cargar.
        try {
            Platform.setImplicitExit(false);
        } catch (Exception e) {
            // ignorar
        }
    }

    private String tituloEjercicio;
    private String descripcionEjercicio;
    private String archivo;
    private String instruccionesAdicionales;

    // Para mover la ventana (barra superior)
    int xmouse, ymouse;

    private javax.swing.border.Border bordeSinFoco = javax.swing.BorderFactory.createEmptyBorder(2, 2, 2, 2);
    private javax.swing.border.Border bordeConFoco = javax.swing.BorderFactory.createLineBorder(new Color(0, 102, 204),
            2);
    private int idEjercicio;
    private int idUsuario;
    private int idVideo;

    // Constructor simplificado - recibe el nombre del ejercicio + usuario
    public Instrucciones(String nombreEjercicio, int idUsuario) {
        this.tituloEjercicio = nombreEjercicio;
        this.idUsuario = (idUsuario > 0) ? idUsuario : getIdUsuarioSesionOrDefault();

        // Determinar los datos del ejercicio basado en el nombre
        determinarDatosEjercicio(nombreEjercicio);

        // Intentar resolver el idVideo por título (para poder guardar progreso)
        this.idVideo = obtenerIdVideoPorTitulo(nombreEjercicio);

        initComponents();
        configurarNavegacionTecladoInstrucciones();
        personalizarInterfaz();
    }

    // Método para determinar los datos del ejercicio basado en el nombre
    private void determinarDatosEjercicio(String nombreEjercicio) {
        if (nombreEjercicio == null) {
            // Valores por defecto
            this.descripcionEjercicio = "Ejercicio de rehabilitación de mano";
            this.archivo = "/videos/ejercicio1.mp4"; // Ruta por defecto
            this.instruccionesAdicionales = obtenerInstruccionesPorDefecto();
            this.idEjercicio = 1; // ✅ ID por defecto
            return;
        }

        String ejercicioLower = nombreEjercicio.toLowerCase().trim();

        // Mapeo de ejercicios con sus descripciones, archivos y IDs
        if (ejercicioLower.contains("mano abierta") || ejercicioLower.contains("abrir")) {
            this.descripcionEjercicio = "Extiende completamente todos los dedos de la mano, manteniendo la palma plana.";
            this.archivo = "/videos/ejercicio1.mp4";
            this.instruccionesAdicionales = "• Comience con la mano relajada\n• Extienda lentamente todos los dedos\n• Mantenga la posición por 3 segundos\n• Relaje y repita";
            this.idEjercicio = 1; // ✅ ID del ejercicio
        } else if (ejercicioLower.contains("puño") || ejercicioLower.contains("cerrado")) {
            this.descripcionEjercicio = "Cierra la mano formando un puño firme, con el pulgar por fuera de los dedos.";
            this.archivo = "/videos/ejercicio2.mp4";
            this.instruccionesAdicionales = "• Comience con la mano abierta\n• Cierre los dedos lentamente formando un puño\n• Apriete suavemente\n• Mantenga 3 segundos y relaje";
            this.idEjercicio = 2; // ✅ ID del ejercicio
        } else if (ejercicioLower.contains("garra")) {
            this.descripcionEjercicio = "Forma una garra flexionando las articulaciones medias de los dedos.";
            this.archivo = "/videos/ejercicio3.mp4";
            this.instruccionesAdicionales = "• Mantenga la palma extendida\n• Flexione solo las articulaciones medias\n• Forme una 'garra' con los dedos\n• Mantenga y relaje";
            this.idEjercicio = 3; // ✅ ID del ejercicio
        } else if (ejercicioLower.contains("separados")) {
            // Agrega más casos según necesites...
            this.idEjercicio = 4;
        } else {
            // Valores por defecto para otros ejercicios
            this.descripcionEjercicio = "Ejercicio de rehabilitación para mejorar la movilidad de la mano.";
            this.archivo = "/videos/ejercicio1.mp4";
            this.instruccionesAdicionales = obtenerInstruccionesPorDefecto();
            this.idEjercicio = obtenerIdEjercicio(nombreEjercicio); // ✅ Usa el método para determinar ID
        }
    }

    // Constructor completo (recomendado) desde el menú de ejercicios
    public Instrucciones(String tituloEjercicio, String descripcionEjercicio, String archivo,
            String instruccionesAdicionales, int idVideo, int idUsuario) {
        this.tituloEjercicio = tituloEjercicio;
        this.descripcionEjercicio = descripcionEjercicio;
        this.archivo = archivo;
        this.instruccionesAdicionales = instruccionesAdicionales;
        this.idUsuario = (idUsuario > 0) ? idUsuario : getIdUsuarioSesionOrDefault();
        this.idEjercicio = obtenerIdEjercicio(tituloEjercicio);
        this.idVideo = idVideo;

        initComponents();
        configurarNavegacionTecladoInstrucciones();
        personalizarInterfaz();
    }

    // Constructor anterior (mantener para compatibilidad)
    public Instrucciones(String tituloEjercicio, String descripcionEjercicio, String archivo,
            String instruccionesAdicionales) {
        this(tituloEjercicio, descripcionEjercicio, archivo, instruccionesAdicionales,
                obtenerIdVideoPorTituloStatic(tituloEjercicio), getIdUsuarioSesionOrDefault());
    }

    // Constructor para compatibilidad (sin ID de usuario)
    public Instrucciones(String nombreEjercicio) {
        this(nombreEjercicio, getIdUsuarioSesionOrDefault());
    }

    private static int getIdUsuarioSesionOrDefault() {
        try {
            Usuario u = SesionUsuario.getInstancia().getUsuarioActual();
            if (u != null && u.getIdUsuario() > 0) {
                return u.getIdUsuario();
            }
        } catch (Exception e) {
            // ignorar
        }
        return 1;
    }

    private int obtenerIdVideoPorTitulo(String titulo) {
        try {
            VideosDAO dao = new VideosDAO();
            VideosDAO.Video v = dao.obtenerVideoPorTitulo(titulo);
            return (v != null) ? v.getIdVideo() : -1;
        } catch (Exception e) {
            return -1;
        }
    }

    private static int obtenerIdVideoPorTituloStatic(String titulo) {
        try {
            VideosDAO dao = new VideosDAO();
            VideosDAO.Video v = dao.obtenerVideoPorTitulo(titulo);
            return (v != null) ? v.getIdVideo() : -1;
        } catch (Exception e) {
            return -1;
        }
    }

    private int obtenerIdEjercicio(String nombreEjercicio) {
        if (nombreEjercicio == null)
            return 1;

        String ejercicioLower = nombreEjercicio.toLowerCase().trim();

        if (ejercicioLower.contains("ejercicio #1") || ejercicioLower.contains("abrir")
                || ejercicioLower.contains("mano abierta")) {
            return 1;
        } else if (ejercicioLower.contains("ejercicio #2") || ejercicioLower.contains("puño")
                || ejercicioLower.contains("cerrado")) {
            return 2;
        } else if (ejercicioLower.contains("ejercicio #3") || ejercicioLower.contains("garra")) {
            return 3;
        } else if (ejercicioLower.contains("ejercicio #4") || ejercicioLower.contains("separados")) {
            return 4;
        } else if (ejercicioLower.contains("ejercicio #5") || ejercicioLower.contains("pulgar a índice")
                || ejercicioLower.contains("pulgar indice")) {
            return 5;
        } else if (ejercicioLower.contains("ejercicio #6") || ejercicioLower.contains("pulgar a meñique")
                || ejercicioLower.contains("pulgar menique")) {
            return 6;
        } else if (ejercicioLower.contains("ejercicio #7") || ejercicioLower.contains("ok sign")
                || ejercicioLower.contains("ok")) {
            return 7;
        } else if (ejercicioLower.contains("ejercicio #8") || ejercicioLower.contains("extensión lateral")
                || ejercicioLower.contains("lateral")) {
            return 8;
        } else if (ejercicioLower.contains("ejercicio #9") || ejercicioLower.contains("flexión de muñeca")
                || ejercicioLower.contains("flexion muneca")) {
            return 9;
        } else if (ejercicioLower.contains("ejercicio #10") || ejercicioLower.contains("extensión de muñeca")
                || ejercicioLower.contains("extension muneca")) {
            return 10;
        } else if (ejercicioLower.contains("ejercicio #11") || ejercicioLower.contains("dedos en pinza")
                || ejercicioLower.contains("pinza")) {
            return 11;
        } else if (ejercicioLower.contains("ejercicio #12") || ejercicioLower.contains("paz y amor")
                || ejercicioLower.contains("paz")) {
            return 12;
        }

        return 1; // Por defecto
    }

    private void personalizarInterfaz() {
        // Actualizar título de la ventana
        Titulo.setText("INSTRUCCIONES - " + tituloEjercicio);

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

    // ... (los métodos configurarNavegacionTecladoInstrucciones,
    // configurarAccionTecladoInstrucciones,
    // configurarAtajosTeclado y configurarInformacionEjercicio se mantienen igual)

    private void configurarNavegacionTecladoInstrucciones() {
        // Hacer elementos interactivos enfocables
        Exit.setFocusable(true);
        ButtonVolver.setFocusable(true);
        ButtonEjercicio.setFocusable(true);

        // Configurar bordes iniciales
        Exit.setBorder(bordeSinFoco);
        ButtonVolver.setBorder(bordeSinFoco);
        ButtonEjercicio.setBorder(bordeSinFoco);

        // Configurar acciones para Enter y Space
        configurarAccionTecladoInstrucciones(Exit, new Runnable() {
            public void run() {
                ExitMouseClicked(null);
            }
        });

        configurarAccionTecladoInstrucciones(ButtonVolver, new Runnable() {
            public void run() {
                ButtonVolverMouseClicked(null);
            }
        });

        configurarAccionTecladoInstrucciones(ButtonEjercicio, new Runnable() {
            public void run() {
                ButtonEjercicioMouseClicked(null);
            }
        });

        // Configurar ESC para cerrar la ventana
        this.getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "cerrarVentana");

        this.getRootPane().getActionMap().put("cerrarVentana", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                // Preguntar si quiere salir o volver
                int opcion = JOptionPane.showConfirmDialog(
                        Instrucciones.this,
                        "¿Qué deseas hacer?",
                        "Salir de Instrucciones",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (opcion == JOptionPane.YES_OPTION) {
                    // Salir de la aplicación
                    System.exit(0);
                } else {
                    // Volver a ejercicios
                    ButtonVolverMouseClicked(null);
                }
            }
        });

        // Configurar atajos de teclado adicionales
        configurarAtajosTeclado();
    }

    private void configurarAccionTecladoInstrucciones(javax.swing.JComponent componente, Runnable accion) {
        // Enter key
        componente.getInputMap(javax.swing.JComponent.WHEN_FOCUSED).put(
                javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ENTER, 0), "pressed");
        componente.getActionMap().put("pressed", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                accion.run();
            }
        });

        // Space key
        componente.getInputMap(javax.swing.JComponent.WHEN_FOCUSED).put(
                javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_SPACE, 0), "spacePressed");
        componente.getActionMap().put("spacePressed", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                accion.run();
            }
        });

        // Agregar listeners para cambiar el borde cuando gana/pierde el foco
        componente.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                componente.setBorder(bordeConFoco);

                // Efecto visual adicional para botones
                if (componente == ButtonVolver) {
                    ButtonVolver.setBackground(new Color(0x1E3888));
                    ButtonVolver.setForeground(Color.WHITE);
                } else if (componente == ButtonEjercicio) {
                    ButtonEjercicio.setBackground(new Color(0xD9D9D9));
                    ButtonEjercicio.setForeground(new Color(0x1E3888));
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                componente.setBorder(bordeSinFoco);

                // Restaurar colores originales de botones
                if (componente == ButtonVolver) {
                    ButtonVolver.setBackground(new Color(0xD9D9D9));
                    ButtonVolver.setForeground(new Color(0x1E3888));
                } else if (componente == ButtonEjercicio) {
                    ButtonEjercicio.setBackground(new Color(0x1E3888));
                    ButtonEjercicio.setForeground(Color.WHITE);
                }
            }
        });
    }

    private void configurarAtajosTeclado() {
        // Atajo V para Volver
        this.getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V,
                        java.awt.event.InputEvent.CTRL_DOWN_MASK),
                "volverAtajo");
        this.getRootPane().getActionMap().put("volverAtajo", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                ButtonVolverMouseClicked(null);
            }
        });

        // Atajo E para Ejercicio
        this.getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E,
                        java.awt.event.InputEvent.CTRL_DOWN_MASK),
                "ejercicioAtajo");
        this.getRootPane().getActionMap().put("ejercicioAtajo", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                ButtonEjercicioMouseClicked(null);
            }
        });

        // Atajo Q para Salir
        this.getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q,
                        java.awt.event.InputEvent.CTRL_DOWN_MASK),
                "salirAtajo");
        this.getRootPane().getActionMap().put("salirAtajo", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                ExitMouseClicked(null);
            }
        });
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
        JLabel lblInstruccionesTitle = new JLabel("Instrucciones");
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
        txtInstrucciones.setBackground(new Color(245, 247, 255)); // Fondo ligeramente rojizo
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

    // ... (el resto del código initComponents se mantiene igual)

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
        AccesibilityButton = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(250, 250, 250));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(30, 56, 136));
        jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel2MousePressed(evt);
            }
        });
        jPanel2.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPanel2MouseDragged(evt);
            }
        });
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        
        AccesibilityButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/CircleButton.png"))); // NOI18N
        AccesibilityButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        AccesibilityButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                abrirVentanaAccesibilidad();
            }
        });
        jPanel1.add(AccesibilityButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(1375, 523, -1, -1));

        Titulo.setFont(new java.awt.Font("Epunda Slab ExtraBold", 0, 24)); // NOI18N
        Titulo.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Titulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 0, -1, 40));

        // Botón minimizar (estilo login)
        minimizebtn = new javax.swing.JPanel();
        minimizetxt = new javax.swing.JLabel();
        minimizebtn.setBackground(new java.awt.Color(30, 56, 136));
        minimizebtn.setLayout(new java.awt.BorderLayout());
        minimizetxt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        minimizetxt.setText("-");
        minimizetxt.setFont(new java.awt.Font("Epunda Slab ExtraBold", 0, 30));
        minimizetxt.setForeground(new java.awt.Color(250, 250, 250));
        minimizetxt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
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
        minimizebtn.add(minimizetxt, java.awt.BorderLayout.CENTER);
        jPanel2.add(minimizebtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(1312, 0, 60, 40));

        // Botón cerrar (reutiliza Exit como label)
        Closebtn = new javax.swing.JPanel();
        Closebtn.setBackground(new java.awt.Color(30, 56, 136));
        Closebtn.setLayout(new java.awt.BorderLayout());

        Exit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Exit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/cerrar.png"))); // NOI18N
        Exit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Exit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ExitMouseClicked(evt);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ExitMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                ExitMouseExited(evt);
            }
        });
        Closebtn.add(Exit, java.awt.BorderLayout.CENTER);
        jPanel2.add(Closebtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(1380, 0, 60, 40));

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

        // Abrir la ventana de Ejercicios (lista de ejercicios)
        Ejercicios ejercicios = new Ejercicios();
        ejercicios.setVisible(true);
        ejercicios.setLocationRelativeTo(null); // Centrar en pantalla

        // Cerrar completamente esta ventana
        this.dispose();
    }// GEN-LAST:event_ButtonVolverMouseClicked

    private void ButtonEjercicioMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_ButtonEjercicioMouseClicked
        // Cerrar la ventana actual
        this.setVisible(false);

        // Pasar usuario + video para poder guardar progreso al completar repeticiones
        Ejercicio ejercicio = new Ejercicio(this.tituloEjercicio, this.idVideo, this.idUsuario);
        ejercicio.setVisible(true);
        ejercicio.setLocationRelativeTo(null); // Centrar en pantalla

        // Cerrar completamente esta ventana
        this.dispose();
    }// GEN-LAST:event_ButtonEjercicioMouseClicked

    private void jPanel2MousePressed(java.awt.event.MouseEvent evt) {
        xmouse = evt.getX();
        ymouse = evt.getY();
    }

    private void jPanel2MouseDragged(java.awt.event.MouseEvent evt) {
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xmouse, y - ymouse);
    }

    private void ExitMouseEntered(java.awt.event.MouseEvent evt) {
        Closebtn.setBackground(java.awt.Color.red);
    }

    private void ExitMouseExited(java.awt.event.MouseEvent evt) {
        Closebtn.setBackground(new java.awt.Color(30, 56, 136));
    }

    private void minimizetxtMouseEntered(java.awt.event.MouseEvent evt) {
        minimizebtn.setBackground(java.awt.Color.decode("#2e4ca9"));
    }

    private void minimizetxtMouseExited(java.awt.event.MouseEvent evt) {
        minimizebtn.setBackground(new java.awt.Color(30, 56, 136));
    }

    private void minimizetxtMouseClicked(java.awt.event.MouseEvent evt) {
        this.setState(javax.swing.JFrame.ICONIFIED);
    }

    private void ExitMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_ExitMouseClicked
        // Confirmar antes de salir
        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Estás seguro de que quieres salir de la aplicación?",
                "Confirmar salida",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }// GEN-LAST:event_ExitMouseClicked

    private void abrirVentanaAccesibilidad() {
        // 1. Crear el JDialog
        JDialog dialog = new JDialog(this, "Opciones de Accesibilidad", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(350, 250); // Aumentamos el tamaño
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this); // Centrar en la ventana Home

        // 2. Panel principal de opciones
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 3. Opciones de Aumento/Disminución de Letra... (Mantener estas)
         JLabel label = new JLabel("Tamaño de Fuente:");

        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel fontPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JButton increaseBtn = new JButton("A +");
        JButton decreaseBtn = new JButton("A -");
        JButton resetBtn = new JButton("Original");

        // Lógica para Aumentar Letra
        increaseBtn.addActionListener(e -> {
            float currentFactor = AccesibilidadUtil.getScalingFactor();
            if (currentFactor < 1.1f) { // Límite superior: 150%
                AccesibilidadUtil.scaleFont(this, currentFactor + 0.1f);
            } else {
                JOptionPane.showMessageDialog(dialog, "Límite de aumento de fuente alcanzado (Máx 150%).", "Alerta", JOptionPane.WARNING_MESSAGE);
            }

        });
        // Lógica para Disminuir Letra
        decreaseBtn.addActionListener(e -> {
            float currentFactor = AccesibilidadUtil.getScalingFactor();
            if (currentFactor > 0.8f) { // Límite inferior: 80%
                AccesibilidadUtil.scaleFont(this, currentFactor - 0.1f);
            } else {
                JOptionPane.showMessageDialog(dialog, "Límite de disminución de fuente alcanzado (Min 80%).", "Alerta", JOptionPane.WARNING_MESSAGE);
            }

        });

        // Lógica para Restablecer
        resetBtn.addActionListener(e -> {

            AccesibilidadUtil.scaleFont(this, 1.0f);

        });

        fontPanel.add(decreaseBtn);
        fontPanel.add(increaseBtn);
        fontPanel.add(resetBtn);
        
        // Agregamos un separador
        optionsPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        optionsPanel.add(Box.createVerticalStrut(10));

        // 4. Opción de Alto Contraste (¡NUEVO!)
        JLabel contrastLabel = new JLabel("Modo de Alto Contraste:");
        contrastLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JCheckBox contrastCheckBox = new JCheckBox("Activar Alto Contraste");
        // Sincronizar el estado inicial del CheckBox con el estado de la utilidad
        contrastCheckBox.setSelected(AccesibilidadUtil.isHighContrastActive()); 
        contrastCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        contrastCheckBox.addActionListener(e -> {
            boolean activate = contrastCheckBox.isSelected();
            AccesibilidadUtil.applyHighContrast(this, activate);
        });

        // 5. Agregar componentes al panel de opciones
        optionsPanel.add(label);
        optionsPanel.add(Box.createVerticalStrut(5));
        optionsPanel.add(fontPanel);
        optionsPanel.add(Box.createVerticalStrut(10));
        optionsPanel.add(contrastLabel);
        optionsPanel.add(Box.createVerticalStrut(5));
        optionsPanel.add(contrastCheckBox); // ¡Agregamos el CheckBox!
        optionsPanel.add(Box.createVerticalGlue()); // Para que quede en la parte superior

        // 6. Agregar el panel al diálogo y mostrar
        dialog.add(optionsPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ButtonEjercicio;
    private javax.swing.JButton ButtonVolver;
    private javax.swing.JLabel Exit;
    private javax.swing.JPanel Closebtn;
    private javax.swing.JPanel minimizebtn;
    private javax.swing.JLabel minimizetxt;
    private javax.swing.JLabel Titulo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel AccesibilityButton;
    // End of variables declaration//GEN-END:variables
}