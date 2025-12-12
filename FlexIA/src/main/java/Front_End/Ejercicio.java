package Front_End;

import componentes.RoundedPanel;
import com.formdev.flatlaf.FlatLightLaf;

import Back_End.SesionUsuario;
import Back_End.Usuario;
import Database.ProgresoDAO;
import Database.RachaDAO;
import Database.VideosDAO;
import Database.MetricasEjercicioDAO;

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
    private int xmouse, ymouse;
    private String tituloEjercicio;
    private Socket socket;
    private boolean ejercicioCompletado = false;
    private int repeticionesFaltantes = 12; // Contador de repeticiones
    private int ejercicioActual = 1; // Número del ejercicio actual

    // Progreso
    private int idUsuario;
    private int idVideo;
    private boolean progresoGuardado = false;

    // Métricas (para tasa de precisión)
    private int okCountSesion = 0;
    private int resetCountSesion = 0;
    private boolean metricasGuardadas = false;

    // Constructor (compatibilidad)
    public Ejercicio(String tituloEjercicio) {
        this(tituloEjercicio, -1, getIdUsuarioSesionOrDefault());
    }

    // Constructor recomendado
    public Ejercicio(String tituloEjercicio, int idVideo, int idUsuario) {
        this.tituloEjercicio = tituloEjercicio;
        this.idVideo = idVideo;
        this.idUsuario = (idUsuario > 0) ? idUsuario : getIdUsuarioSesionOrDefault();

        // Si no vino el idVideo, intentar resolverlo desde la BD por el título
        if (this.idVideo <= 0) {
            this.idVideo = resolverIdVideoPorTitulo(tituloEjercicio);
        }

        initComponents();

        // Determinar el número de ejercicio actual basado en el título
        determinarEjercicioActual(tituloEjercicio);

        // Inicializar textos según el ejercicio
        actualizarTextosEjercicio(tituloEjercicio);

        // Agregar WindowListener para cerrar recursos al cerrar la ventana
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                cerrarRecursos();
            }
        });

        startCameraStream(tituloEjercicio);
        setSize(905, 680);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    // Método para determinar el número de ejercicio actual
    private void determinarEjercicioActual(String tipoEjercicio) {
        if (tipoEjercicio == null)
            return;

        String ejercicioLower = tipoEjercicio.toLowerCase().trim();

        if (ejercicioLower.contains("ejercicio #1") || ejercicioLower.contains("abrir")
                || ejercicioLower.contains("mano abierta")) {
            ejercicioActual = 1;
        } else if (ejercicioLower.contains("ejercicio #2") || ejercicioLower.contains("puño")
                || ejercicioLower.contains("cerrado")) {
            ejercicioActual = 2;
        } else if (ejercicioLower.contains("ejercicio #3") || ejercicioLower.contains("garra")) {
            ejercicioActual = 3;
        } else if (ejercicioLower.contains("ejercicio #4") || ejercicioLower.contains("separados")) {
            ejercicioActual = 4;
        } else if (ejercicioLower.contains("ejercicio #5") || ejercicioLower.contains("pulgar a índice")
                || ejercicioLower.contains("pulgar indice")) {
            ejercicioActual = 5;
        } else if (ejercicioLower.contains("ejercicio #6") || ejercicioLower.contains("pulgar a meñique")
                || ejercicioLower.contains("pulgar menique")) {
            ejercicioActual = 6;
        } else if (ejercicioLower.contains("ejercicio #7") || ejercicioLower.contains("ok sign")
                || ejercicioLower.contains("ok")) {
            ejercicioActual = 7;
        } else if (ejercicioLower.contains("ejercicio #8") || ejercicioLower.contains("extensión lateral")
                || ejercicioLower.contains("lateral")) {
            ejercicioActual = 8;
        } else if (ejercicioLower.contains("ejercicio #9") || ejercicioLower.contains("flexión de muñeca")
                || ejercicioLower.contains("flexion muneca")) {
            ejercicioActual = 9;
        } else if (ejercicioLower.contains("ejercicio #10") || ejercicioLower.contains("extensión de muñeca")
                || ejercicioLower.contains("extension muneca")) {
            ejercicioActual = 10;
        } else if (ejercicioLower.contains("ejercicio #11") || ejercicioLower.contains("dedos en pinza")
                || ejercicioLower.contains("pinza")) {
            ejercicioActual = 11;
        } else if (ejercicioLower.contains("ejercicio #12") || ejercicioLower.contains("paz y amor")
                || ejercicioLower.contains("paz")) {
            ejercicioActual = 12;
        }
    }

    // Método para obtener el nombre del ejercicio basado en el número
    private String obtenerNombreEjercicio(int numeroEjercicio) {
        switch (numeroEjercicio) {
            case 1:
                return "Mano abierta";
            case 2:
                return "Puño cerrado";
            case 3:
                return "Garra";
            case 4:
                return "Dedos separados";
            case 5:
                return "Pulgar a índice";
            case 6:
                return "Pulgar a meñique";
            case 7:
                return "OK Sign";
            case 8:
                return "Extensión lateral";
            case 9:
                return "Flexión de muñeca";
            case 10:
                return "Extensión de muñeca";
            case 11:
                return "Dedos en pinza";
            case 12:
                return "Paz y amor";
            default:
                return "Mano abierta";
        }
    }

    // Método para navegar al ejercicio anterior
    private void navegarEjercicioAnterior() {
        cerrarRecursos();
        this.dispose();

        int ejercicioAnterior = ejercicioActual - 1;
        if (ejercicioAnterior < 1)
            ejercicioAnterior = 12; // Circular: si es menor que 1, va al último

        String nombreEjercicioAnterior = obtenerNombreEjercicio(ejercicioAnterior);

        // Abrir ventana de instrucciones del ejercicio anterior
        Instrucciones instrucciones = new Instrucciones(nombreEjercicioAnterior, getIdUsuarioSesionOrDefault());
        instrucciones.setVisible(true);
        instrucciones.setLocationRelativeTo(null);
    }

    // Método para navegar al ejercicio siguiente
    private void navegarEjercicioSiguiente() {
        cerrarRecursos();
        this.dispose();

        int ejercicioSiguiente = ejercicioActual + 1;
        if (ejercicioSiguiente > 12)
            ejercicioSiguiente = 1; // Circular: si es mayor que 12, va al primero

        String nombreEjercicioSiguiente = obtenerNombreEjercicio(ejercicioSiguiente);

        // Abrir ventana de instrucciones del ejercicio siguiente
        Instrucciones instrucciones = new Instrucciones(nombreEjercicioSiguiente, getIdUsuarioSesionOrDefault());
        instrucciones.setVisible(true);
        instrucciones.setLocationRelativeTo(null);
    }

    // Método para volver a las instrucciones
    private void volverAInstrucciones() {
        cerrarRecursos();
        this.dispose();

        // Volver a las instrucciones del ejercicio actual
        Instrucciones instrucciones = new Instrucciones(tituloEjercicio, getIdUsuarioSesionOrDefault());
        instrucciones.setVisible(true);
        instrucciones.setLocationRelativeTo(null);
    }

    // Método para actualizar textos según el ejercicio
    private void actualizarTextosEjercicio(String tipoEjercicio) {
        if (tipoEjercicio == null)
            return;

        String ejercicioLower = tipoEjercicio.toLowerCase().trim();

        // Actualizar título con formato "EJERCICIO X/12"
        tituloEjercicioLabel.setText("EJERCICIO " + ejercicioActual + "/12");

        // Determinar categoría y descripción según el tipo de ejercicio
        if (ejercicioLower.contains("ejercicio #1") || ejercicioLower.contains("abrir")
                || ejercicioLower.contains("mano abierta")) {
            categoriaLabel.setText("Extensión");
            descripcionLabel.setText("Extiende los dedos completamente");
            repeticionesFaltantes = 12;
        } else if (ejercicioLower.contains("ejercicio #2") || ejercicioLower.contains("puño")
                || ejercicioLower.contains("cerrado")) {
            categoriaLabel.setText("Flexión");
            descripcionLabel.setText("Cierra la mano formando un puño firme");
            repeticionesFaltantes = 12;
        } else if (ejercicioLower.contains("ejercicio #3") || ejercicioLower.contains("garra")) {
            categoriaLabel.setText("Coordinación");
            descripcionLabel.setText("Forma una garra con los dedos flexionados");
            repeticionesFaltantes = 12;
        } else {
            // Valores por defecto
            categoriaLabel.setText("Ejercicio");
            descripcionLabel.setText("Realiza el movimiento indicado");
            repeticionesFaltantes = 12;
        }

        // Actualizar mensaje de repeticiones
        actualizarMensajeRepeticiones();
    }

    // Método para actualizar el mensaje de repeticiones
    private void actualizarMensajeRepeticiones() {
        mensajeRepeticionesLabel.setText("<html><div style='text-align:center;'>Lo estás haciendo bien!<br>Te faltan "
                + repeticionesFaltantes + " repeticiones</div></html>");
    }

    private void startCameraStream(String tipoEjercicio) {
        new Thread(() -> {
            try {
                System.out.println("Iniciando script de Python...");
                String scriptPython = determinarScriptPython(tipoEjercicio);

                ProcessBuilder pb = new ProcessBuilder(
                        "python",
                        "Back-End/ejercicios.py");
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

                socket = null;
                int intentos = 0;
                while (socket == null && intentos < 10) {
                    try {
                        Thread.sleep(1000);
                        socket = new Socket("localhost", 9999);
                        System.out.println("Conectado al servidor Python.");

                        String mensajeEjercicio = "EJERCICIO:" + scriptPython;
                        socket.getOutputStream().write(mensajeEjercicio.getBytes());
                        System.out.println("Ejercicio enviado a Python: " + scriptPython);
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
                            okCountSesion++;
                            SwingUtilities.invokeLater(() -> {
                                ejercicioCompletado = true;
                                repeticionesFaltantes = Math.max(0, repeticionesFaltantes - 1);
                                actualizarMensajeRepeticiones();

                                if (repeticionesFaltantes <= 0) {
                                    onEjercicioFinalizado();
                                }
                            });
                            System.out.println("Ejercicio completado (STATUS:OK)");
                        } else if (mensaje.equals("STATUS:RESET")) {
                            resetCountSesion++;
                            SwingUtilities.invokeLater(() -> {
                                ejercicioCompletado = false;
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

    private void persistirMetricasSesionSiAplica() {
        if (metricasGuardadas) {
            return;
        }
        metricasGuardadas = true;

        // Solo si tenemos IDs válidos
        if (idUsuario > 0 && idVideo > 0) {
            MetricasEjercicioDAO.agregarEventosHoy(idUsuario, idVideo, okCountSesion, resetCountSesion);
        }
    }

    // Método para cerrar todos los recursos
    private void cerrarRecursos() {
        System.out.println("🔴 Cerrando recursos...");

        // Guardar métricas antes de cerrar
        persistirMetricasSesionSiAplica();

        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
                System.out.println("✅ Socket cerrado");
            } catch (IOException e) {
                System.out.println("❌ Error cerrando socket: " + e.getMessage());
            }
        }

        cerrarProcesoPython();
    }

    private void cerrarProcesoPython() {
        if (pythonProcess != null && pythonProcess.isAlive()) {
            System.out.println("🔴 Cerrando proceso Python...");
            pythonProcess.destroy();
            try {
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

    private int resolverIdVideoPorTitulo(String titulo) {
        try {
            VideosDAO dao = new VideosDAO();
            VideosDAO.Video v = dao.obtenerVideoPorTitulo(titulo);
            return (v != null) ? v.getIdVideo() : -1;
        } catch (Exception e) {
            return -1;
        }
    }

    private void onEjercicioFinalizado() {
        if (progresoGuardado) {
            return;
        }
        progresoGuardado = true;

        boolean cuentaComoCompletadoHoy = false;

        // Guardar progreso (1 vez por día por video, pero permitir repeticiones extra sin progreso adicional)
        if (idUsuario > 0 && idVideo > 0) {
            cuentaComoCompletadoHoy = ProgresoDAO.registrarVideoCompletadoHoy(idUsuario, idVideo);

            // Registrar racha SOLO una vez por día (al completar el primer ejercicio del día)
            if (cuentaComoCompletadoHoy) {
                try {
                    RachaDAO rachaDAO = new RachaDAO();
                    if (!rachaDAO.yaRegistroHoy(idUsuario)) {
                        rachaDAO.registrarActividadHoy(idUsuario);
                    }
                } catch (Exception e) {
                    System.err.println("⚠️ No se pudo registrar racha: " + e.getMessage());
                }
            }

        } else {
            System.err.println("⚠️ No se pudo guardar progreso: idUsuario=" + idUsuario + " idVideo=" + idVideo);
        }

        String mensaje = cuentaComoCompletadoHoy
                ? "✅ ¡Ejercicio completado!\nSe registró como completado por hoy."
                : "✅ ¡Ejercicio completado!\nSe registró como repetición extra (sin progreso adicional).";

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Completado",
                JOptionPane.INFORMATION_MESSAGE);

        // Volver al menú de ejercicios
        cerrarRecursos();
        Ejercicios ejercicios = new Ejercicios();
        ejercicios.setVisible(true);
        ejercicios.setLocationRelativeTo(null);
        this.dispose();
    }

    private String determinarScriptPython(String tipoEjercicio) {
        if (tipoEjercicio == null)
            return "ejercicio1";

        String ejercicioLower = tipoEjercicio.toLowerCase().trim();

        if (ejercicioLower.contains("ejercicio #1") || ejercicioLower.contains("abrir")
                || ejercicioLower.contains("mano abierta")) {
            return "ejercicio1";
        } else if (ejercicioLower.contains("ejercicio #2") || ejercicioLower.contains("puño")
                || ejercicioLower.contains("cerrado")) {
            return "ejercicio2";
        } else if (ejercicioLower.contains("ejercicio #3") || ejercicioLower.contains("garra")) {
            return "ejercicio3";
        } else if (ejercicioLower.contains("ejercicio #4") || ejercicioLower.contains("separados")) {
            return "ejercicio4";
        } else if (ejercicioLower.contains("ejercicio #5") || ejercicioLower.contains("pulgar a índice")
                || ejercicioLower.contains("pulgar indice")) {
            return "ejercicio5";
        } else if (ejercicioLower.contains("ejercicio #6") || ejercicioLower.contains("pulgar a meñique")
                || ejercicioLower.contains("pulgar menique")) {
            return "ejercicio6";
        } else if (ejercicioLower.contains("ejercicio #7") || ejercicioLower.contains("ok sign")
                || ejercicioLower.contains("ok")) {
            return "ejercicio7";
        } else if (ejercicioLower.contains("ejercicio #8") || ejercicioLower.contains("extensión lateral")
                || ejercicioLower.contains("lateral")) {
            return "ejercicio8";
        } else if (ejercicioLower.contains("ejercicio #9") || ejercicioLower.contains("flexión de muñeca")
                || ejercicioLower.contains("flexion muneca")) {
            return "ejercicio9";
        } else if (ejercicioLower.contains("ejercicio #10") || ejercicioLower.contains("extensión de muñeca")
                || ejercicioLower.contains("extension muneca")) {
            return "ejercicio10";
        } else if (ejercicioLower.contains("ejercicio #11") || ejercicioLower.contains("dedos en pinza")
                || ejercicioLower.contains("pinza")) {
            return "ejercicio11";
        } else if (ejercicioLower.contains("ejercicio #12") || ejercicioLower.contains("paz y amor")
                || ejercicioLower.contains("paz")) {
            return "ejercicio12";
        }

        return "ejercicio1";
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        header = new javax.swing.JPanel();
        volverBtn = new javax.swing.JPanel(); // NUEVO: Botón volver en header
        volverTxt = new javax.swing.JLabel(); // NUEVO: Label para la flecha
        titlelbl = new javax.swing.JLabel();
        minimizebtn = new javax.swing.JPanel();
        minimizetxt = new javax.swing.JLabel();
        Closebtn = new javax.swing.JPanel();
        Closetxt = new javax.swing.JLabel();
        roundedPanel1 = new RoundedPanel();

        // NUEVOS COMPONENTES PARA LA INFORMACIÓN DEL EJERCICIO
        tituloEjercicioLabel = new javax.swing.JLabel();
        categoriaLabel = new javax.swing.JLabel();
        descripcionLabel = new javax.swing.JLabel();
        intentaEjercicioLabel = new javax.swing.JLabel();
        mensajeRepeticionesLabel = new javax.swing.JLabel();

        cameraLabel = new JLabel("Cámara cargando...");
        cameraLabel.setOpaque(true);
        cameraLabel.setBackground(Color.BLACK);
        cameraLabel.setHorizontalAlignment(SwingConstants.CENTER);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(250, 250, 250));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        // HEADER - CORREGIDO
        header.setBackground(new java.awt.Color(30, 56, 136));
        header.setPreferredSize(new java.awt.Dimension(905, 40)); // Cambiado a 905 para coincidir con el ancho de la
                                                                  // ventana
        header.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout()); // Usar AbsoluteLayout para el header también

        // NUEVO: Botón Volver en header (parte izquierda) - CORREGIDO
        volverBtn.setBackground(new java.awt.Color(30, 56, 136));
        volverBtn.setLayout(new BorderLayout());

        volverTxt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        // SOLUCIÓN: Usar texto en lugar de icono que no existe
        volverTxt.setText("←");
        volverTxt.setFont(new Font("Arial", Font.BOLD, 20));
        volverTxt.setForeground(new Color(250, 250, 250));
        volverTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        volverTxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                volverTxtMouseClicked(evt);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                volverTxt.setForeground(new Color(200, 200, 200));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                volverTxt.setForeground(new Color(250, 250, 250));
            }
        });

        volverBtn.add(volverTxt, BorderLayout.CENTER);
        header.add(volverBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 40, 40));

        // Título FLEX-IA - CORREGIDO
        titlelbl.setBackground(new java.awt.Color(250, 250, 250));
        titlelbl.setFont(new java.awt.Font("Epunda Slab Light", 1, 24)); // Reducido el tamaño para que quepa
        titlelbl.setForeground(new Color(250, 250, 250));
        titlelbl.setText("FLEX-IA");
        header.add(titlelbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 5, 150, 30));

        // Botón Minimizar - CORREGIDO
        minimizebtn.setBackground(new java.awt.Color(30, 56, 136));
        minimizebtn.setLayout(new BorderLayout());

        minimizetxt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        minimizetxt.setText("-");
        minimizetxt.setFont(new Font("Arial", Font.BOLD, 20));
        minimizetxt.setForeground(new Color(250, 250, 250));
        minimizetxt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        minimizetxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                minimizetxtMouseClicked(evt);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                minimizebtn.setBackground(Color.decode("#2e4ca9"));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                minimizebtn.setBackground(new Color(30, 56, 136));
            }
        });

        minimizebtn.add(minimizetxt, BorderLayout.CENTER);
        header.add(minimizebtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(805, 0, 40, 40));

        // Botón Cerrar - CORREGIDO
        Closebtn.setBackground(new java.awt.Color(30, 56, 136));
        Closebtn.setLayout(new BorderLayout());

        Closetxt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Closetxt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/cerrar.png")));
        Closetxt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        Closetxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ClosetxtMouseClicked(evt);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                Closebtn.setBackground(Color.red);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                Closebtn.setBackground(new Color(30, 56, 136));
            }
        });

        Closebtn.add(Closetxt, BorderLayout.CENTER);
        header.add(Closebtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(845, 0, 40, 40));

        // Agregar header al panel principal
        jPanel1.add(header, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 905, 40));

        // PANEL CÁMARA
        roundedPanel1.setLayout(new BorderLayout());
        roundedPanel1.add(cameraLabel, BorderLayout.CENTER);
        jPanel1.add(roundedPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 530, 540));

        // ===== PANEL DERECHO CON INFORMACIÓN DEL EJERCICIO =====
        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(null);
        panelDerecho.setBackground(new Color(245, 245, 250));
        jPanel1.add(panelDerecho, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 60, 325, 540));

        // Título del ejercicio
        tituloEjercicioLabel.setFont(new java.awt.Font("Epunda Slab", Font.BOLD, 24));
        tituloEjercicioLabel.setForeground(new java.awt.Color(30, 56, 136));
        tituloEjercicioLabel.setText("EJERCICIO 1/12");
        tituloEjercicioLabel.setBounds(0, 20, 325, 30);
        tituloEjercicioLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panelDerecho.add(tituloEjercicioLabel);

        // Categoría
        categoriaLabel.setFont(new java.awt.Font("Epunda Slab", Font.BOLD, 18));
        categoriaLabel.setForeground(new java.awt.Color(100, 100, 100));
        categoriaLabel.setText("Categoría: Extensión");
        categoriaLabel.setBounds(0, 60, 325, 25);
        categoriaLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panelDerecho.add(categoriaLabel);

        // Descripción
        descripcionLabel.setFont(new java.awt.Font("Epunda Slab", Font.PLAIN, 16));
        descripcionLabel.setForeground(new java.awt.Color(80, 80, 80));
        descripcionLabel.setText("Extiende los dedos completamente");
        descripcionLabel.setBounds(0, 90, 325, 25);
        descripcionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panelDerecho.add(descripcionLabel);

        // "Intenta el ejercicio..."
        intentaEjercicioLabel.setFont(new java.awt.Font("Epunda Slab", Font.ITALIC, 16));
        intentaEjercicioLabel.setForeground(new java.awt.Color(30, 56, 136));
        intentaEjercicioLabel.setText("Intenta el ejercicio...");
        intentaEjercicioLabel.setBounds(0, 120, 325, 25);
        intentaEjercicioLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panelDerecho.add(intentaEjercicioLabel);

        // ===== BURBUJA DE TEXTO =====
        ImageIcon iconoBurbuja = new javax.swing.ImageIcon(getClass().getResource("/Images/cuadroTexto.png"));
        Image imagenBurbujaEscalada = iconoBurbuja.getImage().getScaledInstance(205, 205, Image.SCALE_SMOOTH);
        ImageIcon iconoBurbujaEscalada = new ImageIcon(imagenBurbujaEscalada);

        JLabel lblBurbuja = new JLabel(iconoBurbujaEscalada);

        // Centramos horizontalmente en panel de 325 px
        int xBurbuja = (325 - 205) / 2;
        int yBurbuja = 150; // posición vertical
        lblBurbuja.setBounds(xBurbuja, yBurbuja, 205, 205);
        lblBurbuja.setLayout(null);

        // ===== Texto dentro de la burbuja =====
        mensajeRepeticionesLabel = new JLabel();
        mensajeRepeticionesLabel.setFont(new Font("Epunda Slab Regular", Font.PLAIN, 16));
        mensajeRepeticionesLabel.setForeground(Color.BLACK);
        mensajeRepeticionesLabel.setText(
                "<html><div style='text-align:center;'>Lo estás haciendo bien!<br>Te faltan 5 repeticiones</div></html>");
        mensajeRepeticionesLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // centramos dentro de la burbuja
        mensajeRepeticionesLabel.setBounds(0, 50, 205, 90);
        lblBurbuja.add(mensajeRepeticionesLabel);

        panelDerecho.add(lblBurbuja);

        // ===== Imagen de carpianin MÁS GRANDE (debajo de la burbuja) =====
        ImageIcon iconoManoOriginal = new ImageIcon(getClass().getResource("/Images/carpianin.png"));
        // Aumentamos el tamaño para que ocupe más espacio
        Image imagenManoEscalada = iconoManoOriginal.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
        ImageIcon iconoManoEscalada = new ImageIcon(imagenManoEscalada);

        JLabel lblMano = new JLabel(iconoManoEscalada);
        int xMano = (325 - 250) / 2;
        int yMano = yBurbuja + 180; // ligeramente debajo de la burbuja
        lblMano.setBounds(xMano, yMano, 250, 250);
        panelDerecho.add(lblMano);

        getContentPane().add(jPanel1);
        pack();
    }

    private void volverTxtMouseClicked(java.awt.event.MouseEvent evt) {
        volverAInstrucciones();
    }


    // ELIMINAMOS ButtonVolverActionPerformed ya que el botón volver fue removido

    private void ClosetxtMouseClicked(java.awt.event.MouseEvent evt) {
        cerrarRecursos();
        this.dispose();
    }

    private void minimizetxtMouseClicked(java.awt.event.MouseEvent evt) {
        this.setState(JFrame.ICONIFIED);
    }

    public static void main(String args[]) {
        FlatLightLaf.setup();
        Ejercicio ventana = new Ejercicio("Mano abierta");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                ventana.cerrarProcesoPython();
            }
        });
        java.awt.EventQueue.invokeLater(() -> ventana.setVisible(true));
    }

    // Variables declaration
    private javax.swing.JPanel Closebtn;
    private javax.swing.JLabel Closetxt;
    private javax.swing.JPanel header;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel minimizebtn;
    private javax.swing.JLabel minimizetxt;
    private javax.swing.JLabel titlelbl;
    private componentes.RoundedPanel roundedPanel1;
    private javax.swing.JPanel volverBtn; // NUEVO
    private javax.swing.JLabel volverTxt; // NUEVO

    // Nuevas variables para los componentes de información del ejercicio
    private javax.swing.JLabel tituloEjercicioLabel;
    private javax.swing.JLabel categoriaLabel;
    private javax.swing.JLabel descripcionLabel;
    private javax.swing.JLabel intentaEjercicioLabel;
    private javax.swing.JLabel mensajeRepeticionesLabel;
}