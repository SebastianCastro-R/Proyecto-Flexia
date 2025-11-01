package Interfaz;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import Login.FuenteUtil;

import java.util.ArrayList;
import java.util.List;

import com.mycompany.flexia.database.VideosDAO;

public class Ejercicios extends javax.swing.JFrame {

    private Menu menuPanel;
    private boolean menuVisible = false;
    private int menuWidth = 370;
    private int menuX = -menuWidth;
    int xmouse, ymouse;
    private javax.swing.JPanel Closebtn;
    private javax.swing.JLabel Closetxt;
    private javax.swing.JPanel minimizebtn;
    private javax.swing.JLabel minimizetxt;

    // Nuevas variables para controlar la capa de bloqueo
    private JLayeredPane layeredPane;
    private JPanel glassPane;

    public Ejercicios() {
        initComponents();

        // Configurar layered pane
        layeredPane = getLayeredPane();

        // Crear glass pane para bloquear interacciones
        glassPane = new JPanel();
        glassPane.setOpaque(false);
        glassPane.setVisible(false);
        glassPane.addMouseListener(new MouseAdapter() {
        });
        glassPane.addMouseMotionListener(new MouseMotionAdapter() {
        });
        glassPane.setCursor(Cursor.getDefaultCursor());

        // Añadir glass pane a una capa superior
        layeredPane.add(glassPane, JLayeredPane.MODAL_LAYER);
        glassPane.setBounds(0, 0, getWidth(), getHeight());

        setUndecorated(true);
        setSize(1440, 1024);
        setLocationRelativeTo(null);
        setResizable(false);

        menuPanel = new Menu("Videos");
        menuPanel.setBounds(menuX, 0, menuWidth, getHeight());
        menuPanel.setVisible(true);
        menuPanel.setOpaque(true);
        menuPanel.setBackground(new Color(250, 250, 250));
        menuPanel.setOnCloseCallback(() -> {
            toggleMenu(null);
        });

        // Añadir el menú a una capa superior
        layeredPane.add(menuPanel, JLayeredPane.POPUP_LAYER);

        revalidate();
        repaint();
    }

    private void initComponents() {

        Closebtn = new javax.swing.JPanel();
        Closetxt = new javax.swing.JLabel();
        minimizebtn = new javax.swing.JPanel();
        minimizetxt = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        JPanel fondo = new JPanel();
        fondo.setBackground(new Color(250, 250, 250));
        fondo.setLayout(new BorderLayout());

        // ----- BARRA SUPERIOR -----
        JPanel barra = new JPanel(new org.netbeans.lib.awtextra.AbsoluteLayout());
        barra.setBackground(new Color(30, 56, 136));
        barra.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                barraMousePressed(evt);
            }
        });
        barra.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                barraMouseDragged(evt);
            }
        });

        JLabel menu = new JLabel(new ImageIcon(getClass().getResource("/icons/menu.png")));
        menu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        menu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                toggleMenu(evt);
            }
        });
        barra.add(menu, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 5, -1, 30));

        JLabel titulo = new JLabel("FLEX-IA");
        titulo.setFont(new Font("Epunda Slab ExtraBold", Font.PLAIN, 24));
        titulo.setForeground(Color.WHITE);
        barra.add(titulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 0, -1, 40));

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
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ClosebtnLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(Closetxt, javax.swing.GroupLayout.PREFERRED_SIZE, 60,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)));
        ClosebtnLayout.setVerticalGroup(
                ClosebtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(Closetxt, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE));

        barra.add(Closebtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(1380, 0, -1, 40));

        minimizebtn.setBackground(new java.awt.Color(30, 56, 136));

        minimizetxt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        minimizetxt.setText("-");
        minimizetxt.setFont(FuenteUtil.cargarFuente("EpundaSlab-EXtrabold.ttf", 30f));
        minimizetxt.setForeground(new Color(250, 250, 250));
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
                        .addGap(0, 60, Short.MAX_VALUE)
                        .addGroup(minimizebtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(minimizebtnLayout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(minimizetxt, javax.swing.GroupLayout.PREFERRED_SIZE, 60,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))));
        minimizebtnLayout.setVerticalGroup(
                minimizebtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 40, Short.MAX_VALUE)
                        .addGroup(minimizebtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(minimizebtnLayout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(minimizetxt, javax.swing.GroupLayout.PREFERRED_SIZE, 40,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))));

        barra.add(minimizebtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(1312, 0, 60, 40));

        fondo.add(barra, BorderLayout.NORTH);

        // ----- PANEL PRINCIPAL -----
        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBackground(new Color(250, 250, 250));

        JLabel tituloPag = new JLabel("EJERCICIOS", SwingConstants.CENTER);
        tituloPag.setFont(new Font("Epunda Slab ExtraBold", Font.PLAIN, 32));
        tituloPag.setAlignmentX(Component.CENTER_ALIGNMENT);
        tituloPag.setBorder(new EmptyBorder(30, 0, 30, 0));
        contenido.add(tituloPag);

        // Crear unidades
        contenido.add(crearUnidad("Unidad 1", 6)); // 6 ejercicios como ejemplo
        contenido.add(Box.createVerticalStrut(30));
        contenido.add(crearUnidad("Unidad 2", 4));

        JScrollPane scroll = new JScrollPane(contenido);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        fondo.add(scroll, BorderLayout.CENTER);

        fondo.setBounds(0, 0, 1440, 1024);
        getContentPane().add(fondo);
    }

    // ---------------- CREACIÓN DE UNIDAD ----------------
    private JPanel crearUnidad(String nombre, int numEjercicios) {
        JPanel unidadPanel = new JPanel(new BorderLayout());
        unidadPanel.setBackground(new Color(250, 250, 250));

        JLabel tituloUnidad = new JLabel(nombre);
        tituloUnidad.setFont(new Font("Epunda Slab ExtraBold", Font.PLAIN, 22));
        tituloUnidad.setForeground(new Color(30, 56, 136));
        tituloUnidad.setBorder(new EmptyBorder(10, 20, 10, 0));
        unidadPanel.add(tituloUnidad, BorderLayout.NORTH);

        // Panel contenedor con botones de navegación
        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBackground(new Color(250, 250, 250));
        contenedor.setBorder(new CompoundBorder(
                new LineBorder(new Color(200, 220, 250), 2, true),
                new EmptyBorder(15, 20, 15, 20)));

        // Lista con todos los ejercicios
        List<JPanel> ejercicios = new ArrayList<>();
        for (int i = 1; i <= numEjercicios; i++) {
            ejercicios.add(crearEjercicio("Ejercicio #" + i,
                    "Este ejercicio ayuda a mejorar la movilidad y fuerza de la muñeca.",
                    "/Images/Background.jpg"));
        }

        // Panel que mostrará los ejercicios visibles
        JPanel panelVisible = new JPanel(new GridLayout(1, 3, 25, 0));
        panelVisible.setBackground(new Color(250, 250, 250));

        // Índice para desplazarse
        final int[] indice = { 0 };
        mostrarEjercicios(panelVisible, ejercicios, indice[0]);

        // Botones izquierda y derecha
        JButton btnIzquierda = new JButton();
        JButton btnDerecha = new JButton();

        // Cargar imágenes desde la carpeta /icons/
        ImageIcon iconIzq = new ImageIcon(getClass().getResource("/icons/izquierda.png"));
        ImageIcon iconDer = new ImageIcon(getClass().getResource("/icons/derecha.png"));

        // Escalar las imágenes para que se vean proporcionadas
        Image imgIzq = iconIzq.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        Image imgDer = iconDer.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);

        // Asignar íconos a los botones
        btnIzquierda.setIcon(new ImageIcon(imgIzq));
        btnDerecha.setIcon(new ImageIcon(imgDer));
        estiloBotonNavegacion(btnIzquierda);
        estiloBotonNavegacion(btnDerecha);

        btnIzquierda.addActionListener(e -> {
            if (indice[0] > 0) {
                indice[0] -= 3;
                mostrarEjercicios(panelVisible, ejercicios, indice[0]);
            }
        });

        btnDerecha.addActionListener(e -> {
            if (indice[0] + 3 < ejercicios.size()) {
                indice[0] += 3;
                mostrarEjercicios(panelVisible, ejercicios, indice[0]);
            }
        });

        contenedor.add(btnIzquierda, BorderLayout.WEST);
        contenedor.add(panelVisible, BorderLayout.CENTER);
        contenedor.add(btnDerecha, BorderLayout.EAST);

        unidadPanel.add(contenedor, BorderLayout.CENTER);
        return unidadPanel;
    }

    private void mostrarEjercicios(JPanel panelVisible, List<JPanel> ejercicios, int inicio) {
        panelVisible.removeAll();
        for (int i = inicio; i < inicio + 3 && i < ejercicios.size(); i++) {
            panelVisible.add(ejercicios.get(i));
        }
        panelVisible.revalidate();
        panelVisible.repaint();
    }

    private void estiloBotonNavegacion(JButton boton) {
        boton.setFont(new Font("Arial", Font.BOLD, 20));
        boton.setFocusPainted(false);
        boton.setBackground(new Color(30, 56, 136));
        boton.setForeground(Color.WHITE);
        boton.setPreferredSize(new Dimension(50, 100));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // ---------------- CREACIÓN DE EJERCICIO ----------------
    private JPanel crearEjercicio(String titulo, String descripcion, String rutaImagen) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 250));
        panel.setBorder(new CompoundBorder(
                new LineBorder(new Color(230, 230, 250), 1, true),
                new EmptyBorder(15, 15, 15, 15)));

        JLabel tituloLbl = new JLabel(titulo);
        tituloLbl.setFont(new Font("Epunda Slab ExtraBold", Font.PLAIN, 18));
        tituloLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Contenedor para la imagen + botón de play
        JPanel contenedorImagen = new JPanel(null);
        contenedorImagen.setPreferredSize(new Dimension(250, 140));
        contenedorImagen.setMaximumSize(new Dimension(250, 140));
        contenedorImagen.setBackground(new Color(245, 245, 250));

        ImageIcon original = new ImageIcon(getClass().getResource(rutaImagen));
        Image imgEscalada = original.getImage().getScaledInstance(250, 140, Image.SCALE_SMOOTH);
        JLabel preview = new JLabel(new ImageIcon(imgEscalada));
        preview.setBounds(0, 0, 250, 140);
        contenedorImagen.add(preview);

        // Botón de play centrado sobre la imagen
        JLabel playBtn = new JLabel(new ImageIcon(getClass().getResource("/icons/play.png")));
        playBtn.setBounds(100, 45, 50, 50);
        playBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Acción al hacer clic (abrir interfaz de instrucciones con datos del ejercicio)
        playBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Obtener datos del ejercicio desde BD
                VideosDAO videosDAO = new VideosDAO();
                VideosDAO.Video video = videosDAO.obtenerVideoPorTitulo(titulo);
                
                String tituloEjercicio = titulo;
                String descripcionEjercicio = descripcion;
                String archivo = obtenerUrlVideoDeBD(titulo);
                String instruccionesAdicionales = (video != null && video.getInstrucciones() != null) ?
                                                    video.getInstrucciones() : obtenerInstruccionesPorDefecto();

                // Abrir ventana de instrucciones con los datos
                Instrucciones instrucciones = new Instrucciones(tituloEjercicio, descripcionEjercicio, archivo, instruccionesAdicionales);
                instrucciones.setVisible(true);
                instrucciones.setLocationRelativeTo(null);
                
                // Cerrar ventana actual
                Ejercicios.this.dispose();
            }
        });

        contenedorImagen.add(playBtn);

        JTextArea desc = new JTextArea(descripcion);
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        desc.setEditable(false);
        desc.setBackground(new Color(245, 245, 250));
        desc.setFont(new Font("SansSerif", Font.PLAIN, 13));
        desc.setBorder(null);

        panel.add(tituloLbl);
        panel.add(Box.createVerticalStrut(10));
        panel.add(contenedorImagen);
        panel.add(Box.createVerticalStrut(10));
        panel.add(desc);

        return panel;
    }

    private String obtenerInstruccionesPorDefecto() {
        return "• Realice el ejercicio en un espacio amplio y seguro.\n" +
                "• Mantenga una postura correcta durante todo el ejercicio.\n" +
                "• Si siente dolor, deténgase inmediatamente.\n" +
                "• Repita el ejercicio según las indicaciones de su terapeuta.\n" +
                "• Respire profundamente durante la ejecución del movimiento.";
        }

    // Método para obtener URL del video desde BD (debes implementarlo según tu estructura)
private String obtenerUrlVideoDeBD(String tituloEjercicio) {
    VideosDAO videosDAO = new VideosDAO();
    VideosDAO.Video video = videosDAO.obtenerVideoPorTitulo(tituloEjercicio);
    
    if (video != null && video.getArchivo() != null) {
        String url = video.getArchivo();
        System.out.println("URL obtenida de BD: " + url); // Para debug
        
        // Formatear URL de Cloudinary si es necesario
        return formatearUrlCloudinary(url);
    } else {
        // URL por defecto si no se encuentra en BD
        return "https://res.cloudinary.com/tu-cloud/video/upload/v1234567/default-video.mp4";
    }
}

    private String formatearUrlCloudinary(String url) {
        if (url == null) return "";
        
        // Si ya es una URL directa de Cloudinary con formato de video
        if (url.contains("res.cloudinary.com") && url.contains("/video/upload/")) {
            return url;
        }
        
        // Si es un public_id o necesita transformación
        if (url.contains("cloudinary.com") && !url.contains("/video/upload/")) {
            // Convertir a URL directa de video
            url = url.replace("http://", "https://")
                    .replace("/upload/", "/video/upload/f_mp4/");
        }
        
        return url;
    }

    private void barraMousePressed(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_barraMousePressed
        xmouse = evt.getX();
        ymouse = evt.getY();
    }// GEN-LAST:event_barraMousePressed

    private void barraMouseDragged(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_barraMouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xmouse, y - ymouse);
    }// GEN-LAST:event_barraMouseDragged

    private void ExitMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_ExitMouseClicked
        System.exit(0);
    }// GEN-LAST:event_ExitMouseClicked

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

    private void toggleMenu(java.awt.event.MouseEvent evt) {
        if (menuVisible) {
            // Ocultar menú
            Timer slideOut = new Timer(2, e -> {
                if (menuX > -menuWidth) {
                    menuX -= 10;
                    menuPanel.setLocation(menuX, 0);
                } else {
                    ((Timer) e.getSource()).stop();
                    menuVisible = false;
                    glassPane.setVisible(false); // Deshabilitar bloqueo
                }
            });
            slideOut.start();
        } else {
            // Mostrar menú
            glassPane.setVisible(true); // Habilitar bloqueo
            Timer slideIn = new Timer(2, e -> {
                if (menuX < 0) {
                    menuX += 10;
                    menuPanel.setLocation(menuX, 0);
                } else {
                    ((Timer) e.getSource()).stop();
                    menuVisible = true;
                }
            });
            slideIn.start();
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new Ejercicios().setVisible(true));
    }
}