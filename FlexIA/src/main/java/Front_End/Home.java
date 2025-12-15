/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Front_End;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.border.Border;

import com.formdev.flatlaf.FlatLightLaf;

import Back_End.FuenteUtil;
import Back_End.SesionUsuario;
import Database.Conexion;
import Database.RachaDAO;
import Database.DolorDAO;
import Database.ProgresoDAO;
import Database.MetricasEjercicioDAO;
import Front_End.AccesibilidadUtil;
import componentes.DonutChartPanel;

import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;

import java.awt.Cursor;

/**
 *
 * @author Karol
 */
public class Home extends javax.swing.JFrame {

    private Menu menuPanel;
    private boolean menuVisible = false;
    private int menuWidth = 450; // ancho del panel del menú
    private int menuX = -menuWidth; // posición inicial fuera de pantalla
    int xmouse, ymouse;

    // Bordes para el efecto de foco
    private Border bordeSinFoco = BorderFactory.createEmptyBorder(2, 2, 2, 2);
    private Border bordeConFoco = BorderFactory.createLineBorder(new Color(0, 102, 204), 2);

    // Labels superpuestos dentro de los círculos de "Estadísticas Generales"
    private JLabel lblCirculoVideos;
    private JLabel lblCirculoDolor;
    private JLabel lblCirculoUnidades;

    // Gráfica de Correctos / Incorrectos
    private DonutChartPanel chartCorrectosIncorrectos;

    // Panel "Continua tu última lección"
    private JLabel lblUltimaLeccionTitulo;
    private JLabel lblUltimaLeccionEjercicio;
    private JTextArea txtUltimaLeccionDescripcion;
    private JLabel lblUltimaLeccionPreview;
    private JLabel lblUltimaLeccionPlay;
    private JPanel panelUltimaLeccionOverlay;
    private Timer hoverTimerUltimaLeccion;
    private int ultimaLeccionOverlayAlpha = 0;
    private int ultimaLeccionOverlayTargetAlpha = 0;
    private int ultimaLeccionPlaySize = 50;
    private final int ultimaLeccionPlayMin = 50;
    private final int ultimaLeccionPlayMax = 55;
    private ProgresoDAO.UltimaLeccionInfo ultimaLeccionActual;

    /**
     * Creates new form Home
     */
    public Home() {

        initComponents();
        colocarFechaActual();
        // La racha se registra cuando el usuario completa un ejercicio (no solo por abrir Home)
        configurarNavegacionTecladoHome();

        // Estadísticas Generales: colocar valores dentro de los círculos (videos, dolor, unidades)
        setupEstadisticasGeneralesCirculos();
        actualizarEstadisticasGeneralesCirculos();

        // Panel: Continúa tu última lección
        setupPanelUltimaLeccion();
        actualizarPanelUltimaLeccion();

        // Gráfica: Correctos vs Incorrectos (al lado de Estadísticas Generales)
        setupGraficaCorrectosIncorrectos();
        actualizarGraficaCorrectosIncorrectos();

        // INICIALIZAR GESTOR DE ANUNCIOS
        Back_End.Ads.AdManager adManager = Back_End.Ads.AdManager.getInstance();
        adManager.registerWindow("home", this);

        menuPanel = new Menu("Home");
        menuPanel.setBounds(menuX, 0, menuWidth, getHeight());
        menuPanel.setVisible(true);
        menuPanel.setOpaque(true);
        menuPanel.setBackground(new Color(250, 250, 250)); // fondo blanco visible
        menuPanel.setOnCloseCallback(() -> {
            toggleMenu(null); // Reutiliza tu animación de cerrar menú
        });

        // IMPORTANTE: agregar el menú al LayeredPane para que siempre quede por encima
        // (evita que la miniatura/preview se dibuje encima del menú)
        getLayeredPane().add(menuPanel, JLayeredPane.POPUP_LAYER);
        getLayeredPane().setLayer(menuPanel, JLayeredPane.POPUP_LAYER);
        menuPanel.setBounds(menuX, 0, menuWidth, getHeight());
        menuPanel.revalidate();
        menuPanel.repaint();

        setLayout(null);
        // Configuración de la ventana
        setSize(1440, 1024);
        setLocationRelativeTo(null);
        setResizable(false);
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

        jPanel1 = new javax.swing.JPanel();
        header = new javax.swing.JPanel();
        Titulo = new javax.swing.JLabel();
        Menu = new javax.swing.JLabel();
        Closebtn = new javax.swing.JPanel();
        Closetxt = new javax.swing.JLabel();
        minimizebtn = new javax.swing.JPanel();
        minimizetxt = new javax.swing.JLabel();
        roundedPanel1 = new componentes.RoundedPanel();
        PanelFecha = new componentes.RoundedPanel();
        fecha = new javax.swing.JLabel();
        icondate = new javax.swing.JLabel();
        notification = new javax.swing.JLabel();
        AccesibilityButton = new javax.swing.JLabel();
        roundedPanel2 = new componentes.RoundedPanel();
        roundedPanel3 = new componentes.RoundedPanel();
        jLabel1 = new javax.swing.JLabel();
        roundedPanel4 = new componentes.RoundedPanel();
        QueDeseas = new javax.swing.JLabel();
        UltimaLeccion = new javax.swing.JLabel();
        EstadiscasGenerales = new javax.swing.JLabel();
        Titulo1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(250, 250, 250));
        jPanel1.setPreferredSize(new java.awt.Dimension(1440, 1024));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        header.setBackground(new java.awt.Color(30, 56, 136));
        header.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                headerMousePressed(evt);
            }
        });
        header.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                headerMouseDragged(evt);
            }
        });

        header.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Titulo.setFont(new java.awt.Font("Epunda Slab ExtraBold", 0, 24)); // NOI18N
        Titulo.setForeground(new java.awt.Color(255, 255, 255));
        Titulo.setText("HOME");
        header.add(Titulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 0, -1, 40));

        Menu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/menu.png"))); // NOI18N
        Menu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Menu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                toggleMenu(evt);
            }
        });
        header.add(Menu, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 5, -1, 30));

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

        header.add(Closebtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(1380, 0, -1, 40));

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

        header.add(minimizebtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(1312, 0, 60, 40));

        jPanel1.add(header, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1440, 40));

        roundedPanel1.setBackground(new java.awt.Color(229, 229, 234));
        roundedPanel1.setPreferredSize(new java.awt.Dimension(444, 885));
        roundedPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PanelFecha.setArc(25);
        PanelFecha.setBackground(new java.awt.Color(152, 206, 255));
        PanelFecha.setPreferredSize(new java.awt.Dimension(235, 40));

        fecha.setFont(new java.awt.Font("Epunda Slab", 0, 20)); // NOI18N
        fecha.setText(" 22 del 2025");

        icondate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/datelack.png"))); // NOI18N

        javax.swing.GroupLayout PanelFechaLayout = new javax.swing.GroupLayout(PanelFecha);
        PanelFecha.setLayout(PanelFechaLayout);
        PanelFechaLayout.setHorizontalGroup(
            PanelFechaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelFechaLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(icondate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(fecha)
                .addContainerGap(49, Short.MAX_VALUE))
        );
        PanelFechaLayout.setVerticalGroup(
            PanelFechaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelFechaLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(PanelFechaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(PanelFechaLayout.createSequentialGroup()
                        .addComponent(icondate)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        roundedPanel1.add(PanelFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(36, 32, 280, -1));

        notification.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/notificacion.png"))); // NOI18N
        notification.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        notification.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                abrirVentanaNotificaciones();
            }
        });
        roundedPanel1.add(notification, new org.netbeans.lib.awtextra.AbsoluteConstraints(361, 23, -1, -1));

        AccesibilityButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/CircleButton.png"))); // NOI18N
        AccesibilityButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        AccesibilityButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                abrirVentanaAccesibilidad();
            }
        });
        jPanel1.add(AccesibilityButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(1375, 523, -1, -1));

        JPanel panelRacha = crearPanelRacha();
        JPanel panelImagenes = crearPanelImagenesRacha();

        // Crear un JLayeredPane para superposición
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(380, 120));
        layeredPane.setBounds(36, 125, 400, 300);

        // Agregar el panel de racha en la capa DEFAULT
        panelRacha.setBounds(0, 0, 380, 100);
        layeredPane.add(panelRacha, JLayeredPane.DEFAULT_LAYER);

        // Agregar el panel de imágenes en una capa superior
        panelImagenes.setBounds(250, -5, 150, 280); // Ajusta estas coordenadas
        layeredPane.add(panelImagenes, JLayeredPane.PALETTE_LAYER);

        roundedPanel1.add(layeredPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(36, 125, 400, 300));

        JPanel panelEncuesta = crearPanelEncuestaDolor();
        roundedPanel1.add(panelEncuesta, new org.netbeans.lib.awtextra.AbsoluteConstraints(36, 255, 370, 220));

        JPanel panelConsejo = crearPanelConsejo();
        roundedPanel1.add(panelConsejo, new org.netbeans.lib.awtextra.AbsoluteConstraints(36, 500, 370, 370));

        jPanel1.add(roundedPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 90, 444, 895));

        roundedPanel2.setBackground(new java.awt.Color(229, 229, 234));

        javax.swing.GroupLayout roundedPanel2Layout = new javax.swing.GroupLayout(roundedPanel2);
        roundedPanel2.setLayout(roundedPanel2Layout);
        roundedPanel2Layout.setHorizontalGroup(
                roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 400, Short.MAX_VALUE));
        roundedPanel2Layout.setVerticalGroup(
                roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 310, Short.MAX_VALUE));

        jPanel1.add(roundedPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 670, 400, 310));

        roundedPanel3.setBackground(new java.awt.Color(229, 229, 234));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Group 21.png"))); // NOI18N

        javax.swing.GroupLayout roundedPanel3Layout = new javax.swing.GroupLayout(roundedPanel3);
        roundedPanel3.setLayout(roundedPanel3Layout);
        roundedPanel3Layout.setHorizontalGroup(
                roundedPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(roundedPanel3Layout.createSequentialGroup()
                                .addGap(49, 49, 49)
                                .addComponent(jLabel1)
                                .addContainerGap(48, Short.MAX_VALUE)));
        roundedPanel3Layout.setVerticalGroup(
                roundedPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                roundedPanel3Layout.createSequentialGroup()
                                        .addContainerGap(32, Short.MAX_VALUE)
                                        .addComponent(jLabel1)
                                        .addGap(28, 28, 28)));

        jPanel1.add(roundedPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 670, 400, 310));

        roundedPanel4.setBackground(new java.awt.Color(203, 230, 255));
        roundedPanel4.setMaximumSize(new java.awt.Dimension(852, 291));
        roundedPanel4.setPreferredSize(new java.awt.Dimension(850, 300));

        javax.swing.GroupLayout roundedPanel4Layout = new javax.swing.GroupLayout(roundedPanel4);
        roundedPanel4.setLayout(roundedPanel4Layout);
        roundedPanel4Layout.setHorizontalGroup(
                roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 850, Short.MAX_VALUE));
        roundedPanel4Layout.setVerticalGroup(
                roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 320, Short.MAX_VALUE));

        jPanel1.add(roundedPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 280, 850, 320));

        QueDeseas.setFont(new java.awt.Font("Epunda Slab ExtraBold", 0, 32)); // NOI18N
        QueDeseas.setForeground(new java.awt.Color(30, 56, 136));
        QueDeseas.setText("¿Qué Deseas Hacer Hoy?");
        jPanel1.add(QueDeseas, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 140, -1, -1));

        UltimaLeccion.setFont(new java.awt.Font("Epunda Slab Medium", 1, 30)); // NOI18N
        UltimaLeccion.setForeground(new java.awt.Color(30, 56, 136));
        UltimaLeccion.setText("Continua Tu Última Lección");
        jPanel1.add(UltimaLeccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 230, -1, -1));

        EstadiscasGenerales.setFont(new java.awt.Font("Epunda Slab Medium", 1, 30)); // NOI18N
        EstadiscasGenerales.setForeground(new java.awt.Color(30, 56, 136));
        EstadiscasGenerales.setText("Estadísticas Generales");
        jPanel1.add(EstadiscasGenerales, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 620, -1, -1));

        Titulo1.setFont(new java.awt.Font("Epunda Slab ExtraBold", 0, 32)); // NOI18N
        Titulo1.setText("Cuidemos Tus Manos, Un Movimiento A La Vez");
        jPanel1.add(Titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 90, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1440,
                                javax.swing.GroupLayout.PREFERRED_SIZE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1024,
                                javax.swing.GroupLayout.PREFERRED_SIZE));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Agregar en el constructor después de initComponents():
    private void configurarNavegacionTecladoHome() {
        // Hacer elementos interactivos enfocables
        Menu.setFocusable(true);
        minimizetxt.setFocusable(true);
        Closetxt.setFocusable(true);

        // Configurar bordes iniciales
        Menu.setBorder(bordeSinFoco);
        minimizetxt.setBorder(bordeSinFoco);
        Closetxt.setBorder(bordeSinFoco);

        // Configurar acciones para Enter y Space
        configurarAccionTecladoHome(Menu, new Runnable() {
            public void run() {
                toggleMenu(null);
            }
        });

        configurarAccionTecladoHome(minimizetxt, new Runnable() {
            public void run() {
                minimizetxtMouseClicked(null);
            }
        });

        configurarAccionTecladoHome(Closetxt, new Runnable() {
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

    private void configurarAccionTecladoHome(javax.swing.JComponent componente, Runnable accion) {
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

    private void headerMousePressed(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_headerMousePressed
        xmouse = evt.getX();
        ymouse = evt.getY();
    }// GEN-LAST:event_headerMousePressed

    private void headerMouseDragged(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_headerMouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xmouse, y - ymouse);
    }// GEN-LAST:event_headerMouseDragged

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
        // Asegurar que el menú se mantenga en el layer superior
        try {
            if (menuPanel != null) {
                getLayeredPane().setLayer(menuPanel, JLayeredPane.POPUP_LAYER);
                // mantener el alto del frame (por si cambió tras pack/setSize)
                menuPanel.setSize(menuWidth, getHeight());
            }
        } catch (Exception ignored) {
        }

        if (menuVisible) {
            // Ocultar menú
            Timer slideOut = new Timer(2, e -> {
                if (menuX > -menuWidth) {
                    menuX -= 10;
                    menuPanel.setLocation(menuX, 0);
                    menuPanel.repaint();
                } else {
                    ((Timer) e.getSource()).stop();
                    menuVisible = false;
                    // Mostrar el ícono del menú cuando el menú esté completamente oculto
                    Menu.setVisible(true);
                }
            });
            slideOut.start();
        } else {
            // Ocultar el ícono del menú antes de mostrar el menú lateral
            Menu.setVisible(false);

            // Mostrar menú
            Timer slideIn = new Timer(2, e -> {
                if (menuX < 0) {
                    menuX += 10;
                    menuPanel.setLocation(menuX, 0);
                    menuPanel.repaint();
                } else {
                    ((Timer) e.getSource()).stop();
                    menuVisible = true;
                }
            });
            slideIn.start();
        }
    }

    private JPanel crearPanelRacha() {
        // Primero creamos el panel de racha normal (sin imágenes)
        PanelRacha = new componentes.RoundedPanel();
        PanelRacha.setBackground(new Color(203, 230, 255));
        PanelRacha.setLayout(new BoxLayout(PanelRacha, BoxLayout.Y_AXIS));
        PanelRacha.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        PanelRacha.setPreferredSize(new Dimension(380, 100));

        // Hacer el panel clickeable
        PanelRacha.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Obtener datos del usuario logueado
        SesionUsuario sesion = SesionUsuario.getInstancia();
        int idUsuario = obtenerIdUsuario(sesion.getCorreoUsuario());

        // Obtener datos de racha
        RachaDAO rachaDAO = new RachaDAO();
        List<Boolean> semanaActividad = rachaDAO.obtenerUltimaSemanaActividad(idUsuario);
        int rachaActual = rachaDAO.obtenerRachaActual(idUsuario);
        boolean yaRegistroHoy = rachaDAO.yaRegistroHoy(idUsuario);

        // Panel de días - MOVEMOS TODO MÁS A LA IZQUIERDA
        JPanel panelDias = new JPanel(new GridLayout(2, 7, 8, 8));
        panelDias.setBackground(new Color(203, 230, 255));
        panelDias.setMaximumSize(new Dimension(235, 80)); // Reducimos el ancho máximo
        panelDias.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        panelDias.setAlignmentX(Component.LEFT_ALIGNMENT); // Alineamos a la izquierda

        String[] dias = {"L", "M", "M", "J", "V", "S", "D"};

        // Primera fila: letras de días
        for (String dia : dias) {
            JLabel lblDia = new JLabel(dia, SwingConstants.CENTER);
            lblDia.setFont(FuenteUtil.cargarFuente("EpundaSlab-EXtrabold.ttf", 16f));
            lblDia.setForeground(new Color(30, 56, 136));
            panelDias.add(lblDia);
        }

        // Segunda fila: checks o círculos - REDUCIMOS TAMAÑO PARA QUE QUEPAN MÁS A LA IZQUIERDA
        for (int i = 0; i < 7; i++) {
            JPanel circuloDia = new JPanel();
            circuloDia.setLayout(new BorderLayout());
            circuloDia.setPreferredSize(new Dimension(25, 25)); // Círculos más pequeños
            
            boolean realizoActividad = i < semanaActividad.size() ? semanaActividad.get(i) : false;
            
            if (realizoActividad) {
                circuloDia.setBackground(new Color(152, 206, 255));
                JLabel check = new JLabel("✓", SwingConstants.CENTER);
                check.setFont(new java.awt.Font("Poppins", java.awt.Font.BOLD, 14));
                check.setForeground(new Color(30, 56, 136));
                circuloDia.add(check, BorderLayout.CENTER);
            } else {
                circuloDia.setBackground(Color.WHITE);
                circuloDia.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
            }
            
            panelDias.add(circuloDia);
        }

        // Panel inferior: mensaje motivacional - TAMBIÉN MÁS A LA IZQUIERDA
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBackground(new Color(203, 230, 255));
        panelInferior.setMaximumSize(new Dimension(250, 40)); // Reducimos ancho
        panelInferior.setAlignmentX(Component.LEFT_ALIGNMENT); // Alineamos a la izquierda

        String mensajeMotivacional = obtenerMensajeMotivacional(rachaActual, yaRegistroHoy);
        JLabel lblMensaje = new JLabel(mensajeMotivacional, SwingConstants.LEFT); // Alineamos texto a la izquierda
        lblMensaje.setFont(FuenteUtil.cargarFuente("EpundaSlab-Medium.ttf", 12f));
        lblMensaje.setForeground(new Color(30, 56, 136));

        panelInferior.add(lblMensaje, BorderLayout.CENTER);

        // Agregar listener para abrir calendario al hacer clic
        PanelRacha.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                abrirCalendarioRacha(idUsuario);
            }
        });

        // Agregamos los componentes alineados a la izquierda
        PanelRacha.add(panelDias);
        PanelRacha.add(Box.createVerticalStrut(10));
        PanelRacha.add(panelInferior);

        return PanelRacha;
    }
    // Método para crear el panel de imágenes superpuesto con contador
    // Versión con coordenadas exactas usando JLayeredPane
    private JPanel crearPanelImagenesRacha() {
        SesionUsuario sesion = SesionUsuario.getInstancia();
        int idUsuario = obtenerIdUsuario(sesion.getCorreoUsuario());
        RachaDAO rachaDAO = new RachaDAO();
        int rachaSeguida = rachaDAO.obtenerRachaSeguida(idUsuario);
        
        JPanel panelImagenes = new JPanel();
        panelImagenes.setOpaque(false);
        panelImagenes.setLayout(new BoxLayout(panelImagenes, BoxLayout.Y_AXIS));
        panelImagenes.setAlignmentX(Component.CENTER_ALIGNMENT);

        try {
            ImageIcon iconoRacha = new ImageIcon(getClass().getResource("/Images/Racha.png"));
            
            // Usar JLayeredPane para coordenadas exactas
            JLayeredPane layeredPane = new JLayeredPane();
            layeredPane.setPreferredSize(new Dimension(
                iconoRacha.getIconWidth(), 
                iconoRacha.getIconHeight()
            ));
            layeredPane.setOpaque(false);
            
            // Imagen de fondo
            JLabel lblRacha = new JLabel(iconoRacha);
            lblRacha.setBounds(0, 0, iconoRacha.getIconWidth(), iconoRacha.getIconHeight());
            layeredPane.add(lblRacha, JLayeredPane.DEFAULT_LAYER);
            

            // Contador con borde usando HTML/CSS
            String htmlContador = "<html><div style='"
                    + "color: #fafafa;"  // Color del texto (blanco)
                    + "text-shadow: "
                    + "-1px -1px 1 #000, "   // Sombra superior izquierda (negro)
                    + "1px -1px 1 #000, "    // Sombra superior derecha
                    + "-1px 1px 0 #000, "    // Sombra inferior izquierda
                    + "1px 1px 0 #000, "     // Sombra inferior derecha
                    + "0 0 3px #000;"        // Sombra difuminada extra
                    + "font-family: EpundaSlab, sans-serif;"
                    + "font-weight: bold;"
                    + "font-size: 20pt;"
                    + "'>" + rachaSeguida + "</div></html>";

            JLabel lblContador = new JLabel(htmlContador, SwingConstants.CENTER);
            lblContador.setFont(FuenteUtil.cargarFuente("EpundaSlab-EXtrabold.ttf", 20f));
            
            // ===== COORDENADAS EXACTAS =====
            Dimension contadorSize = lblContador.getPreferredSize();
            int posX = 14;  // ← Cambia este valor para izquierda/derecha
            int posY = 10;  // ← Cambia este valor para arriba/abajo
            // ================================
            
            lblContador.setBounds(posX, posY, contadorSize.width, contadorSize.height);
            layeredPane.add(lblContador, JLayeredPane.PALETTE_LAYER);
            
            panelImagenes.add(layeredPane);
            panelImagenes.add(Box.createRigidArea(new Dimension(0, 0)));
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            
            JLabel lblContador = new JLabel(String.valueOf(rachaSeguida));
            lblContador.setFont(FuenteUtil.cargarFuente("EpundaSlab-EXtrabold.ttf", 16f));
            lblContador.setForeground(Color.RED);
            
            JLabel lblMascota = new JLabel("🐾");
            lblMascota.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.PLAIN, 20));
            
            panelImagenes.add(lblContador);
            panelImagenes.add(lblMascota);
        }

        return panelImagenes;
    }

    private void abrirCalendarioRacha(int idUsuario) {
        Back_End.CalendarioRacha calendario = new Back_End.CalendarioRacha(this, idUsuario);
        calendario.setVisible(true);
    }

    private void abrirVentanaNotificaciones() {
        // TODO: Implement notifications window
        java.util.List<String> items = new java.util.ArrayList<>();

        NotificationsDialog dlg = new NotificationsDialog(this, items);
        dlg.setVisible(true);
    }

    private int obtenerIdUsuario(String correo) {
        // Método para obtener el ID del usuario basado en el correo
        // Puedes implementar esto según tu estructura de datos
        String sql = "SELECT id_usuario FROM usuarios WHERE correo_electronico = ?";
        
        try (Connection conn = Conexion.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, correo);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("id_usuario");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ID usuario: " + e.getMessage());
        }
        
        return -1; // O maneja el error apropiadamente
    }


    private String obtenerMensajeMotivacional(int racha, boolean yaRegistroHoy) {
        if (racha == 0) {
            return "¡Comienza tu racha hoy!";
        } else if (racha < 3) {
            return "¡Buen comienzo! Sigue así";
        } else if (racha < 7) {
            return "¡Vas por buen camino!";
        } else if (racha < 14) {
            return "¡Excelente compromiso!";
        } else if (racha < 30) {
            return "¡Eres una máquina!";
        } else {
            return "¡Leyenda viviente!";
        }
    }

    private JPanel crearPanelConsejo() {
        panelConsejo = new componentes.RoundedPanel();
        panelConsejo.setLayout(new BoxLayout(panelConsejo, BoxLayout.Y_AXIS));
        panelConsejo.setBackground(new Color(203, 230, 255));
        panelConsejo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelConsejo.setMaximumSize(new Dimension(370, 370));

        // Panel para la imagen (arriba)
        JPanel panelImagen = new componentes.RoundedPanel();
        panelImagen.setBackground(new Color(203, 230, 255));
        panelImagen.setMaximumSize(new Dimension(360, 160));
        panelImagen.setAlignmentX(Component.CENTER_ALIGNMENT);

        try {
            // Cargar imagen del consejo
            ImageIcon iconoConsejo = new ImageIcon(getClass().getResource("/Images/consejo.png"));
            // Redimensionar manteniendo proporciones
            java.awt.Image img = iconoConsejo.getImage();
            int ancho = 350;
            int alto = 160;
            java.awt.Image imgEscalada = img.getScaledInstance(ancho, alto, java.awt.Image.SCALE_SMOOTH);
            JLabel lblImagen = new JLabel(new ImageIcon(imgEscalada));
            lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
            panelImagen.add(lblImagen, BorderLayout.CENTER);
        } catch (Exception e) {
            // Si no encuentra la imagen, poner un placeholder
            JLabel lblPlaceholder = new JLabel("💡");
            lblPlaceholder.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.PLAIN, 40));
            lblPlaceholder.setHorizontalAlignment(SwingConstants.CENTER);
            panelImagen.add(lblPlaceholder, BorderLayout.CENTER);
        }

        // Título "Consejo del Día"
        JLabel lblTituloConsejo = new JLabel("Consejo del Día");
        lblTituloConsejo.setFont(FuenteUtil.cargarFuente("EpundaSlab-EXtrabold.ttf", 20f));
        lblTituloConsejo.setForeground(new Color(30, 56, 136));
        lblTituloConsejo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Texto del consejo
        lblConsejoTexto = new JLabel(
            "<html><body style='width:250px; text-align:center;'>" +
            consejos[indiceConsejoActual] +
            "</body></html>"
        );
        lblConsejoTexto.setFont(FuenteUtil.cargarFuente("EpundaSlab-Regular.ttf", 16f));
        lblConsejoTexto.setForeground(new Color(30, 56, 136));
        lblConsejoTexto.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelBotones.setBackground(new Color(203, 230, 255));
        panelBotones.setMaximumSize(new Dimension(360, 50));
        panelBotones.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnLeerMas = new JButton("Leer Más");
        btnLeerMas.setFont(FuenteUtil.cargarFuente("EpundaSlab-Medium.ttf", 14f));
        btnLeerMas.setBackground(new Color(30, 56, 136));
        btnLeerMas.setForeground(Color.WHITE);
        btnLeerMas.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnLeerMas.setFocusPainted(false);
        btnLeerMas.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnCambiarConsejo = new JButton("Otro consejo");
        btnCambiarConsejo.setFont(FuenteUtil.cargarFuente("EpundaSlab-Medium.ttf", 14f));
        btnCambiarConsejo.setBackground(new Color(152, 206, 255));
        btnCambiarConsejo.setForeground(new Color(30, 56, 136));
        btnCambiarConsejo.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnCambiarConsejo.setFocusPainted(false);
        btnCambiarConsejo.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // ActionListener para cambiar consejo
        btnCambiarConsejo.addActionListener(e -> cambiarConsejo());

        panelBotones.add(btnLeerMas);
        panelBotones.add(btnCambiarConsejo);

        // Ensamblar todo en orden vertical
        panelConsejo.add(panelImagen);
        panelConsejo.add(Box.createVerticalStrut(15));
        panelConsejo.add(lblTituloConsejo);
        panelConsejo.add(Box.createVerticalStrut(10));
        panelConsejo.add(lblConsejoTexto);
        panelConsejo.add(Box.createVerticalStrut(20));
        panelConsejo.add(panelBotones);

        return panelConsejo;
    }

    private void cambiarConsejo() {
        indiceConsejoActual = (indiceConsejoActual + 1) % consejos.length;
        lblConsejoTexto.setText(
            "<html><body style='width:250px; text-align:center;'>" +
            consejos[indiceConsejoActual] +
            "</body></html>"
        );
        // Forzar repintado para actualizar la visualización
        lblConsejoTexto.revalidate();
        lblConsejoTexto.repaint();
    }

    // Lista de consejos predefinidos
    private String[] consejos = {
        "Descansa bien, tus manos también lo necesitan.",
        "Realiza estiramientos cada hora de trabajo.",
        "Mantén una postura correcta al usar el teclado. asdfgfhjkl.kiujyhgtrfdewsxwcvgfhgjmhk,hmgjfhdgs",
        "Hidrata tus manos regularmente.",
        "Usa técnicas de respiración para relajarte."
    };

    private void colocarFechaActual() {
        LocalDate hoy = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("MMMM dd 'del' yyyy", new Locale("es", "COL"));
        fecha.setText(hoy.format(formato));
    }

    private JLabel lblConsejoTexto; // Variable de instancia para poder actualizar el consejo
    private int indiceConsejoActual = 0;

    // Variables para la encuesta de dolor
    private int nivelDolorActual = -1;
    private JLabel[] labelsCaritas;

    private JPanel crearPanelEncuestaDolor() {
        panelEncuestaDolor = new componentes.RoundedPanel();
        panelEncuestaDolor.setLayout(new BoxLayout(panelEncuestaDolor, BoxLayout.Y_AXIS));
        panelEncuestaDolor.setBackground(new Color(203, 230, 255));
        panelEncuestaDolor.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelEncuestaDolor.setMaximumSize(new Dimension(370, 220));

        // Título
        JLabel lblTitulo = new JLabel("¿Cómo te sientes hoy?");
        lblTitulo.setFont(FuenteUtil.cargarFuente("EpundaSlab-EXtrabold.ttf", 20f));
        lblTitulo.setForeground(new Color(30, 56, 136));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Descripción
        JLabel lblDescripcion = new JLabel(
            "<html><body style='width:250px; text-align:center;'>" +
            "Selecciona tu nivel de dolor actual" +
            "</body></html>"
        );
        lblDescripcion.setFont(FuenteUtil.cargarFuente("EpundaSlab-Regular.ttf", 16f));
        lblDescripcion.setForeground(new Color(30, 56, 136));
        lblDescripcion.setAlignmentX(Component.CENTER_ALIGNMENT);

    // Panel de caritas
        JPanel panelCaritas = new JPanel();
        panelCaritas.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 5));
        panelCaritas.setBackground(new Color(203, 230, 255));
        panelCaritas.setMaximumSize(new Dimension(350, 90));
        panelCaritas.setAlignmentX(Component.CENTER_ALIGNMENT);

        labelsCaritas = new JLabel[5];
        String[] caritas = {"😊", "😐", "😖", "😫", "🤕"};

        for (int i = 0; i < 5; i++) {
            final int nivel = i;
            JLabel lblCarita = new JLabel(
                "<html><div style='font-size:32px; padding:2px 0;'>" + caritas[i] + "</div></html>"
            );
            lblCarita.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.PLAIN, 32));
            lblCarita.setForeground(new Color(180, 180, 180)); // Color inicial gris
            lblCarita.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            // Tooltip con descripción del nivel de dolor
            String[] descripciones = {
                "Sin dolor - Me siento bien",
                "Dolor leve - Molestia mínima",
                "Dolor moderado - Molestia noticeable", 
                "Dolor fuerte - Dolor constante",
                "Dolor insoportable - Dolor severo"
            };
            lblCarita.setToolTipText(descripciones[i]);
            
            // Listeners para interactividad
            lblCarita.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    seleccionarNivelDolor(nivel);
                }
                
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    resaltarCaritasHasta(nivel);
                }
                
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    restaurarColoresCaritas();
                }
            });
            
            labelsCaritas[i] = lblCarita;
            panelCaritas.add(lblCarita);
        }
        // Panel para labels de escala
        JPanel panelEscala = new JPanel(new GridLayout(1, 5, 0, 0));
        panelEscala.setBackground(new Color(203, 230, 255));
        panelEscala.setMaximumSize(new Dimension(350, 30));
        panelEscala.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[] escalas = {"1", "2", "3", "4", "5"};
        for (String escala : escalas) {
            JLabel lblEscala = new JLabel(escala, SwingConstants.CENTER);
            lblEscala.setFont(FuenteUtil.cargarFuente("EpundaSlab-Medium.ttf", 16f));
            lblEscala.setForeground(new Color(30, 56, 136));
            panelEscala.add(lblEscala);
        }

        // Label para mostrar la selección actual
        JLabel lblSeleccionActual = new JLabel(" ");
        lblSeleccionActual.setFont(FuenteUtil.cargarFuente("EpundaSlab-Medium.ttf", 16f));
        lblSeleccionActual.setForeground(new Color(30, 56, 136));
        lblSeleccionActual.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSeleccionActual.setName("lblSeleccionActual"); // Para poder referenciarlo después

        // Ensamblar todo
        panelEncuestaDolor.add(lblTitulo);
        panelEncuestaDolor.add(Box.createVerticalStrut(10));
        panelEncuestaDolor.add(lblDescripcion);
        panelEncuestaDolor.add(Box.createVerticalStrut(15));
        panelEncuestaDolor.add(panelCaritas);
        panelEncuestaDolor.add(Box.createVerticalStrut(5));
        panelEncuestaDolor.add(panelEscala);
        panelEncuestaDolor.add(Box.createVerticalStrut(10));
        panelEncuestaDolor.add(lblSeleccionActual);

        return panelEncuestaDolor;
    }

    private void seleccionarNivelDolor(int nivel) {
        nivelDolorActual = nivel;
        actualizarColoresCaritas();
        mostrarConfirmacion(nivel);
    }

    private void resaltarCaritasHasta(int nivel) {
        Color[] colores = {
            new Color(76, 175, 80),    // Verde
            new Color(156, 204, 101),  // Verde claro
            new Color(255, 193, 7),    // Amarillo
            new Color(255, 152, 0),    // Naranja
            new Color(244, 67, 54)     // Rojo
        };
        
        for (int i = 0; i < labelsCaritas.length; i++) {
            if (i <= nivel) {
                labelsCaritas[i].setForeground(colores[i]);
            } else {
                labelsCaritas[i].setForeground(new Color(180, 180, 180)); // Gris
            }
        }
    }

    private void restaurarColoresCaritas() {
        actualizarColoresCaritas();
    }

    private void actualizarColoresCaritas() {
        Color[] colores = {
            new Color(76, 175, 80),    // Verde
            new Color(156, 204, 101),  // Verde claro
            new Color(255, 193, 7),    // Amarillo
            new Color(255, 152, 0),    // Naranja
            new Color(244, 67, 54)     // Rojo
        };
        
        for (int i = 0; i < labelsCaritas.length; i++) {
            if (i <= nivelDolorActual) {
                labelsCaritas[i].setForeground(colores[i]);
            } else {
                labelsCaritas[i].setForeground(new Color(180, 180, 180)); // Gris
            }
        }
    }

    private void mostrarConfirmacion(int nivel) {
        String[] mensajes = {
            "¡Excelente! Sin dolor",
            "Dolor leve seleccionado",
            "Dolor moderado seleccionado", 
            "Dolor fuerte seleccionado",
            "Dolor severo seleccionado - Considera descansar"
        };
        
        // Actualizar el label de selección actual
        for (Component comp : panelEncuestaDolor.getComponents()) {
            if (comp.getName() != null && comp.getName().equals("lblSeleccionActual")) {
                ((JLabel) comp).setText(mensajes[nivel]);
                break;
            }
        }
        
        // Guardar la selección en base de datos (por día)
        int nivelGuardado = nivel + 1; // 1-5
        System.out.println("Nivel de dolor seleccionado: " + nivelGuardado);
        try {
            SesionUsuario sesion = SesionUsuario.getInstancia();
            if (sesion != null && sesion.estaLogueado()) {
                int idUsuario = obtenerIdUsuario(sesion.getCorreoUsuario());
                DolorDAO.registrarDolorHoy(idUsuario, nivelGuardado);
            }
        } catch (Exception e) {
            System.err.println("⚠️ No se pudo guardar el nivel de dolor: " + e.getMessage());
        }

        // Actualizar el círculo de dolor (y demás) en "Estadísticas Generales" sin recargar Home
        actualizarEstadisticasGeneralesCirculos();
    }

    private void setupEstadisticasGeneralesCirculos() {
        // El diseño actual usa una imagen (/Images/Group 21.png) con 3 círculos.
        // Para mostrar valores dinámicos, superponemos labels encima de esa imagen.
        try {
            final int panelW = 400;
            final int panelH = 310;

            // Limpiar contenido actual (solo imagen) y reemplazar por JLayeredPane
            roundedPanel3.removeAll();
            roundedPanel3.setLayout(new BorderLayout());

            JLayeredPane layered = new JLayeredPane();
            layered.setPreferredSize(new Dimension(panelW, panelH));
            layered.setOpaque(false);

            // Fondo: escalar a 400x310 para que los offsets sean estables
            ImageIcon original = new ImageIcon(getClass().getResource("/Images/Group 21.png"));
            java.awt.Image scaledImg = original.getImage().getScaledInstance(panelW, panelH, java.awt.Image.SCALE_SMOOTH);
            JLabel bg = new JLabel(new ImageIcon(scaledImg));
            bg.setBounds(0, 0, panelW, panelH);
            layered.add(bg, JLayeredPane.DEFAULT_LAYER);

            // Coordenadas aproximadas de los círculos dentro de la imagen
            // (Ajustables si se cambia el recurso o el tamaño)
            final int circleSize = 30;
            final int circleX = panelW - 24 - circleSize; // margen derecho ~30px
            final int circleY1 = 21;
            final int circleY2 = 140;
            final int circleY3 = 258;

            lblCirculoVideos = crearLabelCirculo();
            lblCirculoDolor = crearLabelCirculo();
            lblCirculoUnidades = crearLabelCirculo();

            lblCirculoVideos.setToolTipText("Videos vistos (completados)");
            lblCirculoDolor.setToolTipText("Último nivel de dolor registrado (1–5)");
            lblCirculoUnidades.setToolTipText("Unidades completadas (ejercicios realizados)");

            lblCirculoVideos.setBounds(circleX, circleY1, circleSize, circleSize);
            lblCirculoDolor.setBounds(circleX, circleY2, circleSize, circleSize);
            lblCirculoUnidades.setBounds(circleX, circleY3, circleSize, circleSize);

            layered.add(lblCirculoVideos, JLayeredPane.PALETTE_LAYER);
            layered.add(lblCirculoDolor, JLayeredPane.PALETTE_LAYER);
            layered.add(lblCirculoUnidades, JLayeredPane.PALETTE_LAYER);

            roundedPanel3.add(layered, BorderLayout.CENTER);
            roundedPanel3.revalidate();
            roundedPanel3.repaint();

        } catch (Exception e) {
            // No romper Home si el recurso no existe o algo falla.
            System.err.println("⚠️ No se pudieron inicializar los círculos de estadísticas: " + e.getMessage());
        }
    }

    private JLabel crearLabelCirculo() {
        JLabel lbl = new JLabel("—", SwingConstants.CENTER);
        lbl.setForeground(Color.darkGray);
        lbl.setFont(FuenteUtil.cargarFuente("EpundaSlab-EXtrabold.ttf", 18f));
        return lbl;
    }

    private void setValorEnCirculo(JLabel lbl, String valor) {
        if (lbl == null) {
            return;
        }
        String v = (valor == null || valor.trim().isEmpty()) ? "—" : valor.trim();
        lbl.setText(v);

        // Ajuste simple de fuente para valores grandes (ej: 100+)
        float size = 18f;
        if (v.length() >= 3) {
            size = 14f;
        }
        lbl.setFont(FuenteUtil.cargarFuente("EpundaSlab-EXtrabold.ttf", size));
    }

    private void actualizarEstadisticasGeneralesCirculos() {
        // Puede ejecutarse varias veces; si no están inicializados, no hace nada.
        try {
            SesionUsuario sesion = SesionUsuario.getInstancia();
            if (sesion == null || !sesion.estaLogueado()) {
                setValorEnCirculo(lblCirculoVideos, "—");
                setValorEnCirculo(lblCirculoDolor, "—");
                setValorEnCirculo(lblCirculoUnidades, "—");
                return;
            }

            int idUsuario = obtenerIdUsuario(sesion.getCorreoUsuario());
            if (idUsuario <= 0) {
                setValorEnCirculo(lblCirculoVideos, "—");
                setValorEnCirculo(lblCirculoDolor, "—");
                setValorEnCirculo(lblCirculoUnidades, "—");
                return;
            }

            int videosVistos = ProgresoDAO.contarVideosCompletados(idUsuario);
            long unidades = ProgresoDAO.obtenerTotalEjerciciosRealizados(idUsuario);
            int dolor = DolorDAO.obtenerUltimoNivelDolor(idUsuario);

            setValorEnCirculo(lblCirculoVideos, String.valueOf(Math.max(0, videosVistos)));
            setValorEnCirculo(lblCirculoUnidades, String.valueOf(Math.max(0, unidades)));
            setValorEnCirculo(lblCirculoDolor, (dolor > 0) ? String.valueOf(dolor) : "—");

        } catch (Exception e) {
            System.err.println("⚠️ No se pudieron cargar estadísticas generales: " + e.getMessage());
        }
    }

    private void setupGraficaCorrectosIncorrectos() {
        try {
            roundedPanel2.removeAll();
            roundedPanel2.setLayout(new BorderLayout());

            chartCorrectosIncorrectos = new DonutChartPanel();
            chartCorrectosIncorrectos.setLegendLabels("Correctos", "Incorrectos");
            chartCorrectosIncorrectos.setTitle("Ejercicios Realizados");

            roundedPanel2.add(chartCorrectosIncorrectos, BorderLayout.CENTER);
            roundedPanel2.revalidate();
            roundedPanel2.repaint();
        } catch (Exception e) {
            System.err.println("⚠️ No se pudo inicializar la gráfica Correctos/Incorrectos: " + e.getMessage());
        }
    }

    private void actualizarGraficaCorrectosIncorrectos() {
        try {
            if (chartCorrectosIncorrectos == null) {
                return;
            }

            SesionUsuario sesion = SesionUsuario.getInstancia();
            if (sesion == null || !sesion.estaLogueado()) {
                chartCorrectosIncorrectos.setData(0, 0);
                return;
            }

            int idUsuario = obtenerIdUsuario(sesion.getCorreoUsuario());
            if (idUsuario <= 0) {
                chartCorrectosIncorrectos.setData(0, 0);
                return;
            }

            MetricasEjercicioDAO.PrecisionGlobal prec = MetricasEjercicioDAO.obtenerPrecisionGlobal(idUsuario);
            chartCorrectosIncorrectos.setData(prec.ok, prec.reset);

        } catch (Exception e) {
            System.err.println("⚠️ No se pudo cargar la gráfica Correctos/Incorrectos: " + e.getMessage());
        }
    }

    private void setupPanelUltimaLeccion() {
        try {
            // Este panel es el azul grande (roundedPanel4)
            roundedPanel4.removeAll();
            roundedPanel4.setLayout(null);

            // Preview izquierda (tipo video)
            final int previewW = 520;
            final int previewH = 240;

            // Contenedor tipo layered para poder tener overlay + play encima (igual que en Ejercicios)
            JLayeredPane contenedorPreview = new JLayeredPane();
            contenedorPreview.setBounds(35, 40, previewW, previewH);
            contenedorPreview.setOpaque(false);

            lblUltimaLeccionPreview = new JLabel();
            lblUltimaLeccionPreview.setBounds(0, 0, previewW, previewH);
            lblUltimaLeccionPreview.setOpaque(true);
            lblUltimaLeccionPreview.setBackground(new Color(210, 230, 255));
            lblUltimaLeccionPreview.setBorder(BorderFactory.createLineBorder(new Color(180, 200, 240), 2, true));
            lblUltimaLeccionPreview.setHorizontalAlignment(SwingConstants.CENTER);
            contenedorPreview.add(lblUltimaLeccionPreview, JLayeredPane.DEFAULT_LAYER);

            // Overlay azul semi-transparente (para efecto hover)
            panelUltimaLeccionOverlay = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (ultimaLeccionOverlayAlpha > 0) {
                        Graphics2D g2d = (Graphics2D) g.create();
                        g2d.setColor(new Color(30, 56, 136, ultimaLeccionOverlayAlpha));
                        g2d.fillRect(0, 0, getWidth(), getHeight());
                        g2d.dispose();
                    }
                }

                @Override
                public boolean contains(int x, int y) {
                    return false; // deja pasar el mouse (como en Ejercicios)
                }
            };
            panelUltimaLeccionOverlay.setBounds(0, 0, previewW, previewH);
            panelUltimaLeccionOverlay.setOpaque(false);
            contenedorPreview.add(panelUltimaLeccionOverlay, JLayeredPane.MODAL_LAYER);

            // Play (escalado) encima
            lblUltimaLeccionPlay = new JLabel();
            lblUltimaLeccionPlay.setCursor(new Cursor(Cursor.HAND_CURSOR));
            lblUltimaLeccionPlay.setOpaque(false);
            ultimaLeccionPlaySize = ultimaLeccionPlayMin;
            setUltimaLeccionPlayIconSize(ultimaLeccionPlaySize);
            centrarPlayEnPreview(previewW, previewH, ultimaLeccionPlaySize);
            contenedorPreview.add(lblUltimaLeccionPlay, JLayeredPane.PALETTE_LAYER);

            // Hover animation (overlay + zoom play)
            if (hoverTimerUltimaLeccion != null) {
                hoverTimerUltimaLeccion.stop();
            }
            hoverTimerUltimaLeccion = new Timer(10, null);
            hoverTimerUltimaLeccion.addActionListener(e -> {
                // alpha
                if (ultimaLeccionOverlayAlpha < ultimaLeccionOverlayTargetAlpha) {
                    ultimaLeccionOverlayAlpha = Math.min(ultimaLeccionOverlayAlpha + 15, ultimaLeccionOverlayTargetAlpha);
                } else if (ultimaLeccionOverlayAlpha > ultimaLeccionOverlayTargetAlpha) {
                    ultimaLeccionOverlayAlpha = Math.max(ultimaLeccionOverlayAlpha - 15, ultimaLeccionOverlayTargetAlpha);
                }

                // play zoom
                if (ultimaLeccionOverlayTargetAlpha > 0 && ultimaLeccionPlaySize < ultimaLeccionPlayMax) {
                    ultimaLeccionPlaySize = Math.min(ultimaLeccionPlaySize + 2, ultimaLeccionPlayMax);
                    setUltimaLeccionPlayIconSize(ultimaLeccionPlaySize);
                    centrarPlayEnPreview(previewW, previewH, ultimaLeccionPlaySize);
                } else if (ultimaLeccionOverlayTargetAlpha == 0 && ultimaLeccionPlaySize > ultimaLeccionPlayMin) {
                    ultimaLeccionPlaySize = Math.max(ultimaLeccionPlaySize - 2, ultimaLeccionPlayMin);
                    setUltimaLeccionPlayIconSize(ultimaLeccionPlaySize);
                    centrarPlayEnPreview(previewW, previewH, ultimaLeccionPlaySize);
                }

                if (panelUltimaLeccionOverlay != null) {
                    panelUltimaLeccionOverlay.repaint();
                }

                // stop when reached
                if (ultimaLeccionOverlayAlpha == ultimaLeccionOverlayTargetAlpha &&
                        ((ultimaLeccionOverlayTargetAlpha > 0 && ultimaLeccionPlaySize == ultimaLeccionPlayMax) ||
                                (ultimaLeccionOverlayTargetAlpha == 0 && ultimaLeccionPlaySize == ultimaLeccionPlayMin))) {
                    ((Timer) e.getSource()).stop();
                }
            });

            // Mouse listeners (mismo comportamiento: hover + click)
            java.awt.event.MouseAdapter hoverClick = new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    ultimaLeccionOverlayTargetAlpha = 128;
                    if (hoverTimerUltimaLeccion != null && !hoverTimerUltimaLeccion.isRunning()) {
                        hoverTimerUltimaLeccion.start();
                    }
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    ultimaLeccionOverlayTargetAlpha = 0;
                    if (hoverTimerUltimaLeccion != null && !hoverTimerUltimaLeccion.isRunning()) {
                        hoverTimerUltimaLeccion.start();
                    }
                }

                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    abrirUltimaLeccion();
                }
            };
            lblUltimaLeccionPreview.setCursor(new Cursor(Cursor.HAND_CURSOR));
            lblUltimaLeccionPreview.addMouseListener(hoverClick);
            lblUltimaLeccionPlay.addMouseListener(hoverClick);

            roundedPanel4.add(contenedorPreview);

            // Columna derecha
            lblUltimaLeccionTitulo = new JLabel("Unidad #");
            lblUltimaLeccionTitulo.setFont(FuenteUtil.cargarFuente("EpundaSlab-EXtrabold.ttf", 24f));
            lblUltimaLeccionTitulo.setForeground(new Color(0, 0, 0));
            lblUltimaLeccionTitulo.setBounds(600, 55, 240, 30);
            roundedPanel4.add(lblUltimaLeccionTitulo);

            lblUltimaLeccionEjercicio = new JLabel("Ejercicio #");
            lblUltimaLeccionEjercicio.setFont(FuenteUtil.cargarFuente("EpundaSlab-EXtrabold.ttf", 16f));
            lblUltimaLeccionEjercicio.setForeground(new Color(0, 0, 0));
            lblUltimaLeccionEjercicio.setBounds(580, 100, 240, 25);
            roundedPanel4.add(lblUltimaLeccionEjercicio);

            txtUltimaLeccionDescripcion = new JTextArea();
            txtUltimaLeccionDescripcion.setEditable(false);
            txtUltimaLeccionDescripcion.setLineWrap(true);
            txtUltimaLeccionDescripcion.setWrapStyleWord(true);
            txtUltimaLeccionDescripcion.setFont(FuenteUtil.cargarFuente("EpundaSlab-Regular.ttf", 14f));
            txtUltimaLeccionDescripcion.setForeground(new Color(0, 0, 0));
            txtUltimaLeccionDescripcion.setBackground(new Color(203, 230, 255));
            txtUltimaLeccionDescripcion.setBorder(null);

            JScrollPane sp = new JScrollPane(txtUltimaLeccionDescripcion);
            sp.setBorder(null);
            sp.setOpaque(false);
            sp.getViewport().setOpaque(false);
            sp.setBounds(588, 130, 240, 120);
            roundedPanel4.add(sp);

            roundedPanel4.revalidate();
            roundedPanel4.repaint();

        } catch (Exception e) {
            System.err.println("⚠️ No se pudo inicializar el panel de Última Lección: " + e.getMessage());
        }
    }

    private void setUltimaLeccionPlayIconSize(int size) {
        try {
            ImageIcon base = new ImageIcon(getClass().getResource("/icons/play.png"));
            Image scaled = base.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
            lblUltimaLeccionPlay.setIcon(new ImageIcon(scaled));
        } catch (Exception e) {
            // si falla, dejar sin icono
            lblUltimaLeccionPlay.setIcon(null);
        }
    }

    private void centrarPlayEnPreview(int previewW, int previewH, int size) {
        if (lblUltimaLeccionPlay == null) {
            return;
        }
        int x = (previewW - size) / 2;
        int y = (previewH - size) / 2;
        lblUltimaLeccionPlay.setBounds(x, y, size, size);
    }

    private void actualizarPanelUltimaLeccion() {
        try {
            if (lblUltimaLeccionTitulo == null) {
                return;
            }

            SesionUsuario sesion = SesionUsuario.getInstancia();
            if (sesion == null || !sesion.estaLogueado()) {
                setUltimaLeccionPlaceholder();
                return;
            }

            int idUsuario = obtenerIdUsuario(sesion.getCorreoUsuario());
            if (idUsuario <= 0) {
                setUltimaLeccionPlaceholder();
                return;
            }

            ultimaLeccionActual = ProgresoDAO.obtenerUltimaLeccion(idUsuario);
            if (ultimaLeccionActual == null) {
                setUltimaLeccionPlaceholder();
                return;
            }

            String unidad = (ultimaLeccionActual.leccionTitulo != null && !ultimaLeccionActual.leccionTitulo.trim().isEmpty())
                    ? ultimaLeccionActual.leccionTitulo
                    : ((ultimaLeccionActual.idLeccion != null) ? ("Unidad " + ultimaLeccionActual.idLeccion) : "Unidad");

            lblUltimaLeccionTitulo.setText(unidad);
            lblUltimaLeccionEjercicio.setText(ultimaLeccionActual.videoTitulo != null ? ultimaLeccionActual.videoTitulo : "Ejercicio");
            txtUltimaLeccionDescripcion.setText(ultimaLeccionActual.videoDescripcion != null ? ultimaLeccionActual.videoDescripcion : "");

            // Imagen / miniatura
            ImageIcon icon = cargarMiniatura(ultimaLeccionActual.videoMiniatura, 520, 240);
            if (icon != null) {
                lblUltimaLeccionPreview.setIcon(icon);
                lblUltimaLeccionPreview.setText("");
            } else {
                lblUltimaLeccionPreview.setIcon(null);
                lblUltimaLeccionPreview.setText("Sin miniatura");
            }

            // mostrar play + reset hover
            if (lblUltimaLeccionPlay != null) {
                lblUltimaLeccionPlay.setVisible(true);
                ultimaLeccionOverlayAlpha = 0;
                ultimaLeccionOverlayTargetAlpha = 0;
                ultimaLeccionPlaySize = ultimaLeccionPlayMin;
                setUltimaLeccionPlayIconSize(ultimaLeccionPlaySize);
                centrarPlayEnPreview(520, 240, ultimaLeccionPlaySize);
                if (panelUltimaLeccionOverlay != null) {
                    panelUltimaLeccionOverlay.repaint();
                }
                if (hoverTimerUltimaLeccion != null) {
                    hoverTimerUltimaLeccion.stop();
                }
            }

        } catch (Exception e) {
            System.err.println("⚠️ No se pudo cargar Última Lección: " + e.getMessage());
            setUltimaLeccionPlaceholder();
        }
    }

    private void setUltimaLeccionPlaceholder() {
        ultimaLeccionActual = null;
        lblUltimaLeccionTitulo.setText("Aún no hay lección");
        lblUltimaLeccionEjercicio.setText("Realiza tu primer ejercicio");
        txtUltimaLeccionDescripcion.setText("Cuando completes un ejercicio, aquí podrás continuar tu última lección.");
        if (lblUltimaLeccionPreview != null) {
            lblUltimaLeccionPreview.setIcon(null);
            lblUltimaLeccionPreview.setText(" ");
        }
        if (lblUltimaLeccionPlay != null) {
            lblUltimaLeccionPlay.setVisible(false);
        }
        ultimaLeccionOverlayAlpha = 0;
        ultimaLeccionOverlayTargetAlpha = 0;
        if (panelUltimaLeccionOverlay != null) {
            panelUltimaLeccionOverlay.repaint();
        }
        if (hoverTimerUltimaLeccion != null) {
            hoverTimerUltimaLeccion.stop();
        }
    }

    private void abrirUltimaLeccion() {
        try {
            if (ultimaLeccionActual == null) {
                return;
            }

            SesionUsuario sesion = SesionUsuario.getInstancia();
            int idUsuario = (sesion != null) ? obtenerIdUsuario(sesion.getCorreoUsuario()) : -1;

            String titulo = ultimaLeccionActual.videoTitulo;
            String descripcion = ultimaLeccionActual.videoDescripcion;
            String archivo = formatearUrlCloudinary(ultimaLeccionActual.videoArchivo);
            String instrucciones = (ultimaLeccionActual.videoInstrucciones != null && !ultimaLeccionActual.videoInstrucciones.trim().isEmpty())
                    ? ultimaLeccionActual.videoInstrucciones
                    : obtenerInstruccionesPorDefecto();

            Instrucciones instruccionesWin = new Instrucciones(
                    titulo,
                    descripcion,
                    archivo,
                    instrucciones,
                    ultimaLeccionActual.idVideo,
                    idUsuario);

            instruccionesWin.setVisible(true);
            instruccionesWin.setLocationRelativeTo(null);

            // Mantener el patrón del resto de pantallas (cerrar la actual)
            this.dispose();

        } catch (Exception e) {
            System.err.println("⚠️ No se pudo abrir la última lección: " + e.getMessage());
        }
    }

    private ImageIcon cargarMiniatura(String rutaImagen, int width, int height) {
        try {
            String ruta = (rutaImagen == null || rutaImagen.trim().isEmpty()) ? "/Images/Background.jpg" : rutaImagen.trim();

            if (ruta.startsWith("http")) {
                BufferedImage img = ImageIO.read(new URL(ruta));
                if (img == null) {
                    return null;
                }
                Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(scaled);
            }

            // recursos locales
            ImageIcon original = new ImageIcon(getClass().getResource(ruta));
            Image scaled = original.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);

        } catch (Exception e) {
            return null;
        }
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
            return "";
        }

        String out = url.trim().replace("http://", "https://");

        // Si ya es una URL directa de Cloudinary con formato de video
        if (out.contains("res.cloudinary.com") && out.contains("/video/upload/")) {
            return out;
        }

        // Si es Cloudinary pero no está en /video/upload
        if (out.contains("cloudinary.com") && !out.contains("/video/upload/")) {
            out = out.replace("/upload/", "/video/upload/");
        }

        return out;
    }


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
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
        // (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default
         * look and feel.
         * For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        FlatLightLaf.setup();
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        // </editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Home().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Closebtn;
    private javax.swing.JLabel Closetxt;
    private javax.swing.JLabel EstadiscasGenerales;
    private javax.swing.JLabel Menu;
    private javax.swing.JLabel AccesibilityButton;
    private componentes.RoundedPanel PanelFecha;
    private componentes.RoundedPanel PanelRacha;
    private componentes.RoundedPanel panelConsejo;
    private componentes.RoundedPanel panelEncuestaDolor;
    private javax.swing.JLabel QueDeseas;
    private javax.swing.JLabel Titulo;
    private javax.swing.JLabel Titulo1;
    private javax.swing.JLabel UltimaLeccion;
    private javax.swing.JLabel fecha;
    private javax.swing.JLabel icondate;
    private javax.swing.JPanel header;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel notification;
    private javax.swing.JPanel minimizebtn;
    private javax.swing.JLabel minimizetxt;
    private componentes.RoundedPanel roundedPanel1;
    private componentes.RoundedPanel roundedPanel2;
    private componentes.RoundedPanel roundedPanel3;
    private componentes.RoundedPanel roundedPanel4;
    // End of variables declaration//GEN-END:variables
}