package Interfaz;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

import Login.FuenteUtil;

import java.util.ArrayList;
import java.util.List;

import com.mycompany.flexia.database.LeccionesDAO;
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
    private Border bordeSinFoco = BorderFactory.createEmptyBorder(2, 2, 2, 2);
    private Border bordeConFoco = BorderFactory.createLineBorder(new Color(0, 102, 204), 2);

    // Nuevas variables para controlar la capa de bloqueo
    private JLayeredPane layeredPane;
    private JPanel glassPane;

    public Ejercicios() {
        initComponents();
        configurarNavegacionTecladoEjercicios();

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

        // Cargar lecciones y videos desde la base de datos
        LeccionesDAO leccionesDAO = new LeccionesDAO();
        List<LeccionesDAO.Leccion> lecciones = leccionesDAO.obtenerTodasLasLeccionesConVideos();

        for (LeccionesDAO.Leccion leccion : lecciones) {
            contenido.add(crearUnidad(leccion.getTitulo(), leccion.getVideos()));
            contenido.add(Box.createVerticalStrut(30));
        }

        JScrollPane scroll = new JScrollPane(contenido);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        fondo.add(scroll, BorderLayout.CENTER);

        fondo.setBounds(0, 0, 1440, 1024);
        getContentPane().add(fondo);
    }

    // Agregar en el constructor después de initComponents():
    private void configurarNavegacionTecladoEjercicios() {
        // Hacer elementos interactivos enfocables
        // Necesitamos encontrar o crear referencias a los componentes de la barra
        // superior

        // Buscar los componentes en la barra
        Component[] components = ((JPanel) getContentPane().getComponent(0)).getComponents();
        JPanel barra = null;

        for (Component comp : components) {
            if (comp instanceof JPanel) {
                barra = (JPanel) comp;
                break;
            }
        }

        if (barra != null) {
            // Encontrar los componentes en la barra
            for (Component comp : barra.getComponents()) {
                if (comp instanceof JLabel) {
                    JLabel label = (JLabel) comp;
                    // Buscar el ícono del menú
                    if (label.getIcon() != null && label.getIcon().toString().contains("menu")) {
                        label.setFocusable(true);
                        label.setBorder(bordeSinFoco);
                        configurarAccionTecladoEjercicios(label, new Runnable() {
                            public void run() {
                                toggleMenu(null);
                            }
                        });
                    }
                }
            }
        }

        // Configurar botones de minimizar y cerrar
        minimizetxt.setFocusable(true);
        Closetxt.setFocusable(true);

        minimizetxt.setBorder(bordeSinFoco);
        Closetxt.setBorder(bordeSinFoco);

        // Configurar acciones para Enter y Space
        configurarAccionTecladoEjercicios(minimizetxt, new Runnable() {
            public void run() {
                minimizetxtMouseClicked(null);
            }
        });

        configurarAccionTecladoEjercicios(Closetxt, new Runnable() {
            public void run() {
                ClosetxtMouseClicked(null);
            }
        });

        // Configurar ESC para cerrar menú si está abierto
        this.getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "cerrarMenu");

        this.getRootPane().getActionMap().put("cerrarMenu", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (menuVisible) {
                    toggleMenu(null);
                }
            }
        });
    }

    private void configurarAccionTecladoEjercicios(javax.swing.JComponent componente, Runnable accion) {
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
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                componente.setBorder(bordeSinFoco);
            }
        });
    }

    // ---------------- CREACIÓN DE UNIDAD ----------------
    private JPanel crearUnidad(String nombre, List<VideosDAO.Video> videos) {
        JPanel unidadPanel = new JPanel(new BorderLayout());
        unidadPanel.setBackground(new Color(250, 250, 250));

        JLabel tituloUnidad = new JLabel(nombre);
        tituloUnidad.setFont(new Font("Epunda Slab ExtraBold", Font.PLAIN, 22));
        tituloUnidad.setForeground(new Color(30, 56, 136));
        tituloUnidad.setBorder(new EmptyBorder(10, 20, 10, 0));
        unidadPanel.add(tituloUnidad, BorderLayout.NORTH);

        // Si no hay videos, mostrar mensaje
        if (videos == null || videos.isEmpty()) {
            JLabel sinVideos = new JLabel("No hay ejercicios disponibles para esta lección");
            sinVideos.setFont(new Font("SansSerif", Font.ITALIC, 16));
            sinVideos.setForeground(Color.GRAY);
            sinVideos.setHorizontalAlignment(SwingConstants.CENTER);
            sinVideos.setBorder(new EmptyBorder(20, 20, 20, 20));
            unidadPanel.add(sinVideos, BorderLayout.CENTER);
            return unidadPanel;
        }

        // Panel contenedor con botones de navegación
        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBackground(new Color(250, 250, 250));
        contenedor.setBorder(new CompoundBorder(
                new LineBorder(new Color(200, 220, 250), 2, true),
                new EmptyBorder(15, 20, 15, 20)));

        // Lista con todos los ejercicios usando los videos reales
        List<JPanel> ejercicios = new ArrayList<>();
        for (VideosDAO.Video video : videos) {
            ejercicios.add(crearEjercicio(
                    video.getTitulo(),
                    video.getDescripcion(),
                    video.getIdVideo(),
                    video // Pasar el objeto video completo para usar después
            ));
        }

        // Panel que mostrará los ejercicios visibles
        JPanel panelVisible = new JPanel(new GridLayout(1, 3, 25, 0));
        panelVisible.setBackground(new Color(250, 250, 250));

        // Índice para desplazarse
        final int[] indice = { 0 };
        mostrarEjercicios(panelVisible, ejercicios, indice[0]);

        // Botones izquierda y derecha (solo si hay más de 3 videos)
        if (ejercicios.size() > 3) {
            JButton btnIzquierda = new JButton();
            JButton btnDerecha = new JButton();

            // Cargar imágenes desde la carpeta /icons/
            ImageIcon iconIzq = new ImageIcon(getClass().getResource("/icons/izquierda.png"));
            ImageIcon iconDer = new ImageIcon(getClass().getResource("/icons/derecha.png"));

            // Escalar las imágenes
            Image imgIzq = iconIzq.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            Image imgDer = iconDer.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);

            btnIzquierda.setIcon(new ImageIcon(imgIzq));
            btnDerecha.setIcon(new ImageIcon(imgDer));
            btnIzquierda.setFocusable(true);
            btnDerecha.setFocusable(true);
            btnIzquierda.setBorder(bordeSinFoco);
            btnDerecha.setBorder(bordeSinFoco);
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

            // Agregar listeners de foco para los botones de navegación
            btnIzquierda.addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusGained(java.awt.event.FocusEvent evt) {
                    btnIzquierda.setBorder(bordeConFoco);
                }

                @Override
                public void focusLost(java.awt.event.FocusEvent evt) {
                    btnIzquierda.setBorder(bordeSinFoco);
                }
            });

            btnDerecha.addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusGained(java.awt.event.FocusEvent evt) {
                    btnDerecha.setBorder(bordeConFoco);
                }

                @Override
                public void focusLost(java.awt.event.FocusEvent evt) {
                    btnDerecha.setBorder(bordeSinFoco);
                }
            });

            contenedor.add(btnIzquierda, BorderLayout.WEST);
            contenedor.add(panelVisible, BorderLayout.CENTER);
            contenedor.add(btnDerecha, BorderLayout.EAST);
        } else {
            // Si hay 3 o menos videos, mostrarlos todos sin botones de navegación
            for (JPanel ejercicio : ejercicios) {
                panelVisible.add(ejercicio);
            }
            contenedor.add(panelVisible, BorderLayout.CENTER);
        }

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
    private JPanel crearEjercicio(String titulo, String descripcion, int idVideo, VideosDAO.Video video) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 250));
        panel.setBorder(new CompoundBorder(
                new LineBorder(new Color(230, 230, 250), 1, true),
                new EmptyBorder(15, 15, 15, 15)));

        JLabel tituloLbl = new JLabel(titulo);
        tituloLbl.setFont(new Font("Epunda Slab ExtraBold", Font.PLAIN, 18));
        tituloLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Obtener la miniatura desde el objeto Video
        String rutaImagen = video.getMiniatura();

        // Si no hay miniatura en BD, usar una por defecto
        if (rutaImagen == null || rutaImagen.isEmpty()) {
            rutaImagen = "/Images/Background.jpg";
        }

        // Resto del código permanece igual...
        JLayeredPane contenedorImagen = new JLayeredPane() {
            @Override
            public boolean contains(int x, int y) {
                return getComponentCount() > 0;
            }
        };
        contenedorImagen.setPreferredSize(new Dimension(250, 140));
        contenedorImagen.setMaximumSize(new Dimension(250, 140));
        contenedorImagen.setBackground(new Color(245, 245, 250));
        contenedorImagen.setOpaque(false);

        // Imagen de fondo - ahora usa la miniatura de la BD
        ImageIcon original = null;
        try {
            // Intentar cargar desde URL (Drive)
            if (rutaImagen.startsWith("http")) {
                original = cargarImagenDesdeURL(rutaImagen, 250, 140);
            } else {
                // Cargar desde recursos locales
                original = new ImageIcon(getClass().getResource(rutaImagen));
            }
        } catch (Exception e) {
            // Si hay error, usar imagen por defecto
            original = new ImageIcon(getClass().getResource("/Images/Background.jpg"));
        }

        Image imgEscalada = original.getImage().getScaledInstance(250, 140, Image.SCALE_SMOOTH);
        JLabel preview = new JLabel(new ImageIcon(imgEscalada));
        preview.setBounds(0, 0, 250, 140);
        preview.setOpaque(false);
        contenedorImagen.add(preview, JLayeredPane.DEFAULT_LAYER);

        // Variables para animación
        Timer hoverTimer = new Timer(10, null);
        final int[] alpha = { 0 }; // Opacidad actual - INICIA EN 0 (transparente)
        final int[] targetAlpha = { 0 }; // Opacidad objetivo
        final int[] playSize = { 50 }; // tamaño actual del icono
        final int playMin = 50;
        final int playMax = 55; // tamaño máximo al hacer hover

        // Overlay azul SEMI-TRANSPARENTE para el efecto hover
        JPanel overlayAzul = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (alpha[0] > 0) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setColor(new Color(30, 56, 136, alpha[0]));
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                    g2d.dispose();
                }
            }

            @Override
            public boolean contains(int x, int y) {
                return false; // ⚡ Esto hace que el mouse "pase a través" del overlay
            }
        };

        overlayAzul.setBounds(0, 0, 250, 140);
        overlayAzul.setOpaque(false); // IMPORTANTE: hacer el panel transparente
        contenedorImagen.add(overlayAzul, JLayeredPane.MODAL_LAYER);

        // Botón de play - CON MEJOR CONFIGURACIÓN
        JLabel playBtn = new JLabel(new ImageIcon(getClass().getResource("/icons/play.png"))) {
            @Override
            public boolean contains(int x, int y) {
                // Mejorar la detección de colisión del mouse
                return x >= 0 && x < getWidth() && y >= 0 && y < getHeight();
            }
        };
        playBtn.setBounds(100, 45, 50, 50);
        playBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        playBtn.setOpaque(false);
        playBtn.setFocusable(true);
        playBtn.setBorder(bordeSinFoco);

        // Agregar listener de foco para el botón de play
        playBtn.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                playBtn.setBorder(bordeConFoco);
                // También activar el efecto hover cuando tiene foco
                targetAlpha[0] = 128;
                if (!hoverTimer.isRunning()) {
                    hoverTimer.start();
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                playBtn.setBorder(bordeSinFoco);
                // Desactivar el efecto hover cuando pierde foco
                targetAlpha[0] = 0;
                if (!hoverTimer.isRunning()) {
                    hoverTimer.start();
                }
            }
        });

        configurarAccionPlayBtn(playBtn, video);

        hoverTimer.addActionListener(e -> {
            // Animación del alpha (overlay azul)
            if (alpha[0] < targetAlpha[0]) {
                alpha[0] = Math.min(alpha[0] + 15, targetAlpha[0]);
            } else if (alpha[0] > targetAlpha[0]) {
                alpha[0] = Math.max(alpha[0] - 15, targetAlpha[0]);
            }

            // Animación del tamaño del ícono de play
            if (targetAlpha[0] > 0 && playSize[0] < playMax) {
                playSize[0] = Math.min(playSize[0] + 2, playMax);
            } else if (targetAlpha[0] == 0 && playSize[0] > playMin) {
                playSize[0] = Math.max(playSize[0] - 2, playMin);
            }

            // Escalar dinámicamente el ícono
            ImageIcon icon = new ImageIcon(
                    new ImageIcon(getClass().getResource("/icons/play.png"))
                            .getImage()
                            .getScaledInstance(playSize[0], playSize[0], Image.SCALE_SMOOTH));
            playBtn.setIcon(icon);

            // Recentrar el botón
            int x = (250 - playSize[0]) / 2;
            int y = (140 - playSize[0]) / 2;
            playBtn.setBounds(x, y, playSize[0], playSize[0]);

            overlayAzul.repaint();
            contenedorImagen.repaint();

            // Detener el timer cuando se alcance el estado final
            if (alpha[0] == targetAlpha[0] &&
                    ((targetAlpha[0] > 0 && playSize[0] == playMax) ||
                            (targetAlpha[0] == 0 && playSize[0] == playMin))) {
                ((Timer) e.getSource()).stop();
            }

            if (playBtn.hasFocus() && targetAlpha[0] == 0) {
                targetAlpha[0] = 128;
            }
        });

        // MouseAdapter para el botón de play
        MouseAdapter playBtnListener = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                targetAlpha[0] = 128; // Opacidad objetivo cuando el mouse entra
                if (!hoverTimer.isRunning()) {
                    hoverTimer.start();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                targetAlpha[0] = 0; // Transparente cuando el mouse sale
                if (!hoverTimer.isRunning()) {
                    hoverTimer.start();
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                abrirInstrucciones(video);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                playBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        };

        // Agregar todos los listeners necesarios al botón
        playBtn.addMouseListener(playBtnListener);
        playBtn.addMouseMotionListener(playBtnListener);

        contenedorImagen.add(playBtn, JLayeredPane.PALETTE_LAYER);

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

    // Y agrega este método para manejar la acción desde el teclado también:
    private void configurarAccionPlayBtn(JLabel playBtn, VideosDAO.Video video) {
        // Enter key
        playBtn.getInputMap(JComponent.WHEN_FOCUSED).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "abrirInstrucciones");
        playBtn.getActionMap().put("abrirInstrucciones", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirInstrucciones(video);
            }
        });

        // Space key
        playBtn.getInputMap(JComponent.WHEN_FOCUSED).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "abrirInstruccionesSpace");
        playBtn.getActionMap().put("abrirInstruccionesSpace", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirInstrucciones(video);
            }
        });
    }

    // Método para abrir instrucciones (extraído del mouseClicked)
    private void abrirInstrucciones(VideosDAO.Video video) {
        String tituloEjercicio = video.getTitulo();
        String descripcionEjercicio = video.getDescripcion();
        String archivo = formatearUrlCloudinary(video.getArchivo());
        String instruccionesAdicionales = (video.getInstrucciones() != null)
                ? video.getInstrucciones()
                : obtenerInstruccionesPorDefecto();

        // Abrir ventana de instrucciones con los datos
        Instrucciones instrucciones = new Instrucciones(tituloEjercicio, descripcionEjercicio, archivo,
                instruccionesAdicionales);
        instrucciones.setVisible(true);
        instrucciones.setLocationRelativeTo(null);

        // Cerrar ventana actual
        Ejercicios.this.dispose();
    }

    public static ImageIcon cargarImagenDesdeURL(String urlString, int width, int height) {
        try {
            URL url = new URL(urlString);
            BufferedImage image = ImageIO.read(url);
            if (image != null) {
                Image scaled = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(scaled);
            } else {
                System.out.println("No se pudo leer imagen (¿URL inválida?)");
            }
        } catch (Exception e) {
            System.err.println("Error cargando imagen: " + e.getMessage());
        }
        return null;
    }

    private String obtenerInstruccionesPorDefecto() {
        return "• Realice el ejercicio en un espacio amplio y seguro.\n" +
                "• Mantenga una postura correcta durante todo el ejercicio.\n" +
                "• Si siente dolor, deténgase inmediatamente.\n" +
                "• Repita el ejercicio según las indicaciones de su terapeuta.\n" +
                "• Respire profundamente durante la ejecución del movimiento.";
    }

    private String formatearUrlCloudinary(String url) {
        if (url == null || url.trim().isEmpty()) {
            System.err.println("URL de video es nula o vacía");
            return "";
        }

        System.out.println("URL original: " + url);

        // Si ya es una URL directa de Cloudinary con formato de video
        if (url.contains("res.cloudinary.com") && url.contains("/video/upload/")) {
            // Asegurar que sea HTTPS
            url = url.replace("http://", "https://");
            System.out.println("URL formateada (ya es video): " + url);
            return url;
        }

        // Si es un public_id o necesita transformación
        if (url.contains("cloudinary.com") && !url.contains("/video/upload/")) {
            // Convertir a URL directa de video
            url = url.replace("http://", "https://")
                    .replace("/upload/", "/video/upload/");
            System.out.println("URL formateada (convertida a video): " + url);
            return url;
        }

        System.out.println("URL final: " + url);
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