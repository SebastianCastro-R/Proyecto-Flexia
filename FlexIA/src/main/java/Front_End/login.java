/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Front_End;

import Back_End.FuenteUtil;
import Back_End.RecuperarContrasena;
import Back_End.SesionUsuario;
import Database.Conexion;
import Database.UsuariosDAO;

import com.formdev.flatlaf.FlatLightLaf;

import componentes.RoundedPanelS;

import java.awt.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.*;

/**
 *
 * @author kfkdm
 */
public class login extends javax.swing.JFrame {

    int xmouse, ymouse;
    private JTextField txtCorreo = new JTextField();
    private JPasswordField txtPass = new JPasswordField();
    private RoundedPanelS panelDerecho;
    private JButton lblOlvidar;
    private java.util.List<Component> focusOrder;
    private int currentFocusIndex = 0;
    // Variables para los botones sociales
    private JButton btnGoogle;
    private JButton btnFacebook;
    private JButton btnOutlook;
    private JButton btnIniciar;

    /**
     * Creates new form login
     */
    public login() {

        initComponents();
        initStyles();
        initPanels();
        initFocusOrder();
        setupKeyboardNavigation();

        setSize(1440, 1024);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null); // Centra la ventana

        // Cargar y escalar la imagen al tamaño del JFrame
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/Images/Background.jpg"));

        Image scaledImage = originalIcon.getImage().getScaledInstance(1440, 1024, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        // Crear JLabel con la imagen escalada
        JLabel fondo = new JLabel(scaledIcon);
        fondo.setBounds(0, 0, 1440, 1024);
        getContentPane().add(fondo);

        // Ejemplo: puedes añadir un botón encima del fondo
        JButton btnLogin = new JButton("Iniciar sesión");
        btnLogin.setBounds(620, 800, 200, 50);
        fondo.add(btnLogin);

        setResizable(false); // Evita redimensionar
        setVisible(true);

    }

    private void initStyles() {
        Closetxt.setFont(FuenteUtil.cargarFuente("EpundaSlab-ExtraBold.ttf", 30f));
        Closetxt.setForeground(new Color(250, 250, 250));
        minimizetxt.setFont(FuenteUtil.cargarFuente("EpundaSlab-EXtrabold.ttf", 30f));
        minimizetxt.setForeground(new Color(250, 250, 250));
        titlelbl.setFont(FuenteUtil.cargarFuente("EpundaSlab-ExtraBold.ttf", 28f));
        titlelbl.setForeground(new Color(250, 250, 250));

    }

    private void abrirVentanaRegistro() {
        // Cerrar la ventana actual
        this.dispose();
        UIManager.put("ComboBox.arc", 30);
        // Abrir la ventana de SignIn (registro)
        SignIn signin = new SignIn();
        signin.setVisible(true);
        signin.setLocationRelativeTo(null); // Centrar en pantalla
    }

    private void initPanels() {
        // Coordenadas centradas
        int panelWidth = 449;
        int panelHeight = 729;
        int totalWidth = panelWidth * 2;
        int startX = (1440 - totalWidth) / 2; // 271
        int startY = 57 + ((1024 - 57 - panelHeight) / 2); // 176

        RoundedPanelS panelIzquierdo = new RoundedPanelS(40, new Color(0x98CEFF)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                g2.fillRect(getWidth() / 2, 0, getWidth() / 2, getHeight());
                g2.dispose();
            }
        };
        panelIzquierdo.setBounds(startX, startY, panelWidth, panelHeight);
        panelIzquierdo.setLayout(null);
        add(panelIzquierdo);

        // ===== Texto "Bienvenido" =====
        JLabel lblBienvenido = new JLabel("Bienvenido", SwingConstants.CENTER);
        lblBienvenido.setFont(new Font("Epunda Slab ExtraBold", Font.PLAIN, 36));
        lblBienvenido.setForeground(Color.BLACK);

        // Centramos manualmente dentro del panel
        int anchoPanel = 450; // o el ancho real del panelIzquierdo
        int anchoTexto = 400;
        int x = (anchoPanel - anchoTexto) / 2; // centrado horizontal
        int y = 80; // posición vertical (puedes ajustar)
        lblBienvenido.setBounds(x, y, anchoTexto, 50);

        panelIzquierdo.add(lblBienvenido);

        // ===== Texto secundario =====
        JLabel lblSubtexto = new JLabel(
                "<html><div style='text-align:center;'>Accede a tu cuenta con tus credenciales<br>O</div></html>",
                SwingConstants.CENTER);
        lblSubtexto.setFont(new Font("Epunda Slab Regular", Font.PLAIN, 20));
        lblSubtexto.setForeground(Color.BLACK);

        // Centramos el bloque en el panel
        int anchoPanel2 = 450; // ancho real del panel izquierdo
        int anchoTexto2 = 400;
        int x2 = (anchoPanel2 - anchoTexto2) / 2;
        lblSubtexto.setBounds(x2, 140, anchoTexto2, 60);

        panelIzquierdo.add(lblSubtexto);

        // ===== Imagen de la burbuja de texto =====
        ImageIcon iconoBurbuja = new javax.swing.ImageIcon(getClass().getResource("/Images/cuadroTexto.png"));
        Image imagenBurbujaEscalada = iconoBurbuja.getImage().getScaledInstance(205, 205, Image.SCALE_SMOOTH);
        ImageIcon iconoBurbujaEscalada = new ImageIcon(imagenBurbujaEscalada);

        JLabel lblBurbuja = new JLabel(iconoBurbujaEscalada);

        // Centramos horizontalmente en panel de 449 px
        int xBurbuja = (449 - 205) / 2;
        int yBurbuja = 220; // más arriba para que no toque la mano
        lblBurbuja.setBounds(xBurbuja, yBurbuja, 205, 205);
        lblBurbuja.setLayout(null);

        // ===== Texto dentro de la burbuja =====
        JLabel lblTextoBurbuja = new JLabel(
                "<html><div style='text-align:center;'>Si aún no tienes<br>cuenta, primero<br>debes registrarte</div></html>",
                SwingConstants.CENTER);
        lblTextoBurbuja.setFont(new Font("Epunda Slab Regular", Font.PLAIN, 18));
        lblTextoBurbuja.setForeground(Color.BLACK);

        // centramos dentro de la burbuja
        lblTextoBurbuja.setBounds(0, 40, 205, 90);
        lblBurbuja.add(lblTextoBurbuja);

        panelIzquierdo.add(lblBurbuja);

        // ===== Imagen de la mano (debajo de la burbuja) =====
        ImageIcon iconoManoOriginal = new ImageIcon(getClass().getResource("/Images/carpianin.png"));
        Image imagenManoEscalada = iconoManoOriginal.getImage().getScaledInstance(227, 221, Image.SCALE_SMOOTH);
        ImageIcon iconoManoEscalada = new ImageIcon(imagenManoEscalada);

        JLabel lblMano = new JLabel(iconoManoEscalada);
        int xMano = (449 - 227) / 2;
        int yMano = yBurbuja + 150; // ligeramente debajo de la burbuja
        lblMano.setBounds(xMano, yMano, 227, 221);
        panelIzquierdo.add(lblMano);

        // configura el botón (dentro de initPanels), reemplaza las líneas previas donde
        // ponías setOpaque(false) etc.
        registrarsebtn.setText("Registrarse");
        registrarsebtn.setFont(new Font("Epunda Slab Regular", Font.PLAIN, 20));
        registrarsebtn.setForeground(Color.BLACK);
        registrarsebtn.setBackground(new Color(0xFAFAFA));
        registrarsebtn.setFocusPainted(false);
        registrarsebtn.setBorderPainted(false); // opcional según si quieres borde
        registrarsebtn.setOpaque(false); // IMPORTANT: deja true para que L&F pinte el fondo
        registrarsebtn.setContentAreaFilled(true);
        registrarsebtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registrarsebtn.setBounds(140, 600, 170, 45);
        registrarsebtn.putClientProperty("Button.hoverBackground", new Color(0x1E3888));

        // Efecto hover manual (opcional)
        registrarsebtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                registrarsebtn.setBackground(new Color(0x1E3888));
                registrarsebtn.setForeground(Color.WHITE);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                registrarsebtn.setBackground(new Color(0xFAFAFA));
                registrarsebtn.setForeground(Color.BLACK);
            }
        });

        registrarsebtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abrirVentanaRegistro();
            }
        });

        panelIzquierdo.add(registrarsebtn);

        // ===== Panel derecho (blanco) =====
        panelDerecho = new RoundedPanelS(40, new Color(0xFAFAFA)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                // Redondear solo esquinas derechas
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                g2.fillRect(0, 0, getWidth() / 2, getHeight());
                g2.dispose();
            }
        };
        panelDerecho.setBounds(startX + panelWidth, startY, panelWidth, panelHeight);
        panelDerecho.setLayout(null);
        add(panelDerecho);

        // ===== Logo superior =====
        ImageIcon iconoLogo = new ImageIcon(getClass().getResource("/Images/logo-flexia.png"));
        Image logoEscalado = iconoLogo.getImage().getScaledInstance(209, 231, Image.SCALE_SMOOTH);
        ImageIcon iconoLogoEscalado = new ImageIcon(logoEscalado);

        JLabel lblLogo = new JLabel(iconoLogoEscalado);
        lblLogo.setBounds((panelDerecho.getWidth() - 209) / 2, 40, 209, 231);
        panelDerecho.add(lblLogo);

        // ===== Título "Iniciar Sesión" =====
        JLabel lblIniciar = new JLabel("Iniciar Sesión", SwingConstants.CENTER);
        lblIniciar.setFont(new Font("Epunda Slab SemiBold", Font.PLAIN, 28));
        lblIniciar.setForeground(Color.BLACK);
        lblIniciar.setBounds(0, 280, panelDerecho.getWidth(), 40);
        panelDerecho.add(lblIniciar);

        // ===== Botones sociales (Google, Facebook, Outlook) =====
        Color fondoCirculo = new Color(0xE8E8E8);
        int baseY = 330; // Y base para los botones
        int espacio = 50; // separación horizontal entre ellos
        int inicioX = (panelDerecho.getWidth() - (3 * 51 + 2 * 40)) / 2; // centrado dinámico

        btnGoogle = crearBotonSocial("/Images/google.png", inicioX, baseY, fondoCirculo);
        btnGoogle.setOpaque(false);
        btnGoogle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnFacebook = crearBotonSocial("/Images/facebook.png", inicioX + espacio + 40, baseY, fondoCirculo);
        btnFacebook.setOpaque(false);
        btnFacebook.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnOutlook = crearBotonSocial("/Images/outlook.png", inicioX + (espacio + 40) * 2, baseY, fondoCirculo);
        btnOutlook.setOpaque(false);
        btnOutlook.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelDerecho.add(btnGoogle);
        panelDerecho.add(btnFacebook);
        panelDerecho.add(btnOutlook);

        // ===== Label "Correo Electrónico" =====
        JLabel lblCorreo = new JLabel("Correo Electrónico:");
        lblCorreo.setFont(new Font("Epunda Slab SemiBold", Font.PLAIN, 20));
        lblCorreo.setBounds(80, 410, 300, 30);
        panelDerecho.add(lblCorreo);

        // ===== Campo correo =====

        txtCorreo.setBounds(80, 440, 286, 40);
        txtCorreo.setFont(new Font("Lato", Font.PLAIN, 16));
        txtCorreo.setForeground(new Color(142, 142, 147));
        txtCorreo.putClientProperty("JComponent.roundRect", true);

        // Crear un panel para el icono
        JPanel iconPanel = new JPanel(new BorderLayout());
        iconPanel.setOpaque(false); // transparente
        JLabel lblIcono = new JLabel(new javax.swing.ImageIcon(getClass().getResource("/Images/correo.png")));
        lblIcono.setBorder(BorderFactory.createEmptyBorder(-2, 20, 0, 10)); // margen icono al borde y al texto
        iconPanel.add(lblIcono, BorderLayout.CENTER);

        // Asignar como leadingComponent
        txtCorreo.putClientProperty("JTextField.leadingComponent", iconPanel);

        // Mantener estilo de FlatLaf
        txtCorreo.putClientProperty("FlatLaf.style", "borderColor:#B4B4B4; borderWidth:3;");

        // Placeholder
        txtCorreo.putClientProperty("JTextField.placeholderText", "Ingresa tu correo");
        txtCorreo.putClientProperty("TextField.placeholderForeground", new Color(150, 150, 150));

        panelDerecho.add(txtCorreo);

        // ===== Label "Contraseña" =====
        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setFont(new Font("Epunda Slab SemiBold", Font.PLAIN, 20));
        lblPass.setBounds(80, 490, 300, 30);
        panelDerecho.add(lblPass);

        // ===== Campo contraseña =====

        txtPass.setBounds(80, 520, 286, 40);
        txtPass.setFont(new Font("Lato", Font.PLAIN, 16));
        txtPass.setForeground(new Color(142, 142, 147));
        txtPass.setBackground(Color.WHITE);
        txtPass.putClientProperty("JComponent.roundRect", true);
        txtPass.putClientProperty("JComponent.arc", 30);

        // --- Icono a la izquierda ---
        JLabel lblIconoLeft = new JLabel(new javax.swing.ImageIcon(getClass().getResource("/Images/Password.png")));
        lblIconoLeft.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 10));
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);
        leftPanel.add(lblIconoLeft, BorderLayout.CENTER);
        txtPass.putClientProperty("JTextField.leadingComponent", leftPanel);

        // --- Icono a la derecha (mostrar/ocultar contraseña) ---
        JLabel lblIconoRight = new JLabel(new javax.swing.ImageIcon(getClass().getResource("/Images/eye_off.png"))); // icono
                                                                                                                     // cerrado
        lblIconoRight.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 15));
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.add(lblIconoRight, BorderLayout.CENTER);
        txtPass.putClientProperty("JTextField.trailingComponent", rightPanel);

        // --- Interacción para mostrar/ocultar contraseña ---
        lblIconoRight.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblIconoRight.addMouseListener(new java.awt.event.MouseAdapter() {
            private boolean showing = false;

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                showing = !showing;
                if (showing) {
                    txtPass.setEchoChar((char) 0); // mostrar texto
                    lblIconoRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/eye_on.png")));
                } else {
                    txtPass.setEchoChar('●'); // ocultar texto
                    lblIconoRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/eye_off.png")));
                }
            }
        });

        // --- Estilos FlatLaf ---
        txtPass.putClientProperty("FlatLaf.style", "borderColor:#B4B4B4; borderWidth:3;");
        txtPass.putClientProperty("JTextField.placeholderText", "●●●●●●●●●");
        txtPass.putClientProperty("TextField.placeholderForeground", new Color(150, 150, 150));

        panelDerecho.add(txtPass);

        // En lugar de JLabel lblOlvidar, usar JButton con estilo de label
        lblOlvidar = new JButton("<html><u>¿Olvidaste tu contraseña?</u></html>");
        lblOlvidar.setFont(new Font("Lato", Font.ITALIC, 14));
        lblOlvidar.setForeground(new Color(0x1E3888));
        lblOlvidar.setBackground(Color.WHITE); // Fondo transparente
        lblOlvidar.setBorderPainted(false);
        lblOlvidar.setContentAreaFilled(false);
        lblOlvidar.setFocusPainted(false);
        lblOlvidar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblOlvidar.setBounds(80, 570, 200, 20);

        // Agregar ActionListener en lugar de MouseListener
        lblOlvidar.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String correoUsuario = txtCorreo.getText();
                if (correoUsuario.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingresa tu correo primero.");
                    return;
                }

                RecuperarContrasena ventanaRecuperar = new RecuperarContrasena(correoUsuario);
                ventanaRecuperar.setVisible(true);
                ventanaRecuperar.setLocationRelativeTo(null);
            }
        });

        // Efecto hover
        lblOlvidar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblOlvidar.setForeground(new Color(0x020300));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblOlvidar.setForeground(new Color(0x1E3888));
            }
        });

        panelDerecho.add(lblOlvidar);

        // ===== Botón "Iniciar Sesión" =====
        btnIniciar = new JButton("Iniciar Sesión");
        btnIniciar.setFont(new Font("Epunda Slab SemiBold", Font.PLAIN, 18));
        btnIniciar.setForeground(Color.BLACK);
        btnIniciar.setBackground(new Color(0x98CEFF));
        btnIniciar.setFocusPainted(false);
        btnIniciar.setBounds((panelDerecho.getWidth() - 180) / 2, 600, 180, 45);

        btnIniciar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efecto hover manual (opcional)
        btnIniciar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnIniciar.setBackground(new Color(0x1E3888));
                btnIniciar.setForeground(Color.WHITE);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnIniciar.setBackground(new Color(0x98CEFF));
                btnIniciar.setForeground(Color.BLACK);
            }

        });

        // ====== Acción al hacer clic (ActionListener) ======
        btnIniciar.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String correo = txtCorreo.getText();
                String contrasena = new String(txtPass.getPassword());

                if (correo.isEmpty() || contrasena.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            null,
                            "⚠️ Por favor, complete todos los campos.",
                            "Campos vacíos",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                UsuariosDAO dao = new UsuariosDAO();

                if (dao.autenticarUsuarioPorCorreo(correo, contrasena)) {
                    // ✅ LOGIN EXITOSO - OBTENER DATOS DEL USUARIO Y GUARDAR SESIÓN
                    String nombreUsuario = obtenerNombreUsuarioDesdeBD(correo);

                    if (nombreUsuario != null) {
                        // Guardar en sesión
                        SesionUsuario.getInstancia().iniciarSesion(correo, nombreUsuario);

                        JOptionPane.showMessageDialog(
                                null,
                                "✅ Inicio de sesión exitoso.\nBienvenido " + nombreUsuario,
                                "Bienvenido",
                                JOptionPane.INFORMATION_MESSAGE);
                        // ✅ Verificar si el usuario ya respondió encuesta
                        if (usuarioTieneEncuesta(correo)) {
                            // ✅ Ya respondió → ir al Home
                            Home home = new Home();
                            home.setVisible(true);
                            home.setLocationRelativeTo(null);
                        } else {
                            // ❌ No respondió → ir al Formulario
                            Formulario form = new Formulario();
                            form.setVisible(true);
                            form.setLocationRelativeTo(null);
                        }

                        dispose(); // Cierra login

                        dispose(); // Cierra la ventana de Login
                    } else {
                        JOptionPane.showMessageDialog(
                                null,
                                "❌ Error al obtener datos del usuario.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }

                } else {
                    JOptionPane.showMessageDialog(
                            null,
                            "❌ Correo o contraseña incorrectos.",
                            "Error de inicio de sesión",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panelDerecho.add(btnIniciar);

        // ===== MEJORAS DE ACCESIBILIDAD PARA LECTORES DE PANTALLA =====

        // Panel Izquierdo - Configurar propiedades de accesibilidad
        panelIzquierdo.getAccessibleContext().setAccessibleName("Panel de bienvenida");
        panelIzquierdo.getAccessibleContext().setAccessibleDescription(
                "Panel que contiene información de bienvenida y opción para registrarse");

        // Texto "Bienvenido"
        lblBienvenido.getAccessibleContext().setAccessibleName("Bienvenido");
        lblBienvenido.getAccessibleContext().setAccessibleDescription(
                "Título de bienvenida al sistema");

        // Texto secundario
        lblSubtexto.getAccessibleContext().setAccessibleName("Instrucciones de acceso");
        lblSubtexto.getAccessibleContext().setAccessibleDescription(
                "Accede a tu cuenta con tus credenciales o regístrate si no tienes cuenta");

        // Texto dentro de la burbuja
        lblTextoBurbuja.getAccessibleContext().setAccessibleName("Información para nuevos usuarios");
        lblTextoBurbuja.getAccessibleContext().setAccessibleDescription(
                "Si aún no tienes cuenta, primero debes registrarte");

        // Botón Registrarse
        registrarsebtn.getAccessibleContext().setAccessibleName("Botón registrarse");
        registrarsebtn.getAccessibleContext().setAccessibleDescription(
                "Botón para ir al formulario de registro de nueva cuenta");
        registrarsebtn.setToolTipText("Haga clic para registrarse como nuevo usuario");

        // Panel Derecho - Configurar propiedades de accesibilidad
        panelDerecho.getAccessibleContext().setAccessibleName("Panel de inicio de sesión");
        panelDerecho.getAccessibleContext().setAccessibleDescription(
                "Panel para ingresar credenciales e iniciar sesión");

        // Logo
        lblLogo.getAccessibleContext().setAccessibleName("Logo de Flex IA");
        lblLogo.getAccessibleContext().setAccessibleDescription("Logo de la aplicación Flex IA");
        // Agregar texto alternativo para la imagen
        lblLogo.putClientProperty("AccessibleDescription", "Logo de la aplicación Flex IA");

        // Título "Iniciar Sesión"
        lblIniciar.getAccessibleContext().setAccessibleName("Iniciar Sesión");
        lblIniciar.getAccessibleContext().setAccessibleDescription("Título de inicio de sesión");

        // Botones sociales - Agregar textos accesibles
        btnGoogle.getAccessibleContext().setAccessibleName("Iniciar sesión con Google");
        btnGoogle.getAccessibleContext().setAccessibleDescription("Botón para iniciar sesión usando cuenta de Google");
        btnGoogle.setToolTipText("Iniciar sesión con Google");

        btnFacebook.getAccessibleContext().setAccessibleName("Iniciar sesión con Facebook");
        btnFacebook.getAccessibleContext()
                .setAccessibleDescription("Botón para iniciar sesión usando cuenta de Facebook");
        btnFacebook.setToolTipText("Iniciar sesión con Facebook");

        btnOutlook.getAccessibleContext().setAccessibleName("Iniciar sesión con Outlook");
        btnOutlook.getAccessibleContext()
                .setAccessibleDescription("Botón para iniciar sesión usando cuenta de Outlook");
        btnOutlook.setToolTipText("Iniciar sesión con Outlook");

        // Campo correo electrónico
        lblCorreo.getAccessibleContext().setAccessibleName("Etiqueta correo electrónico");
        lblCorreo.getAccessibleContext().setAccessibleDescription("Etiqueta para el campo de correo electrónico");

        txtCorreo.getAccessibleContext().setAccessibleName("Campo de correo electrónico");
        txtCorreo.getAccessibleContext().setAccessibleDescription(
                "Ingrese su dirección de correo electrónico para iniciar sesión");
        // Mejorar el placeholder para accesibilidad
        txtCorreo.putClientProperty("JTextField.placeholderText", "Ingresa tu correo electrónico");

        // Campo contraseña
        lblPass.getAccessibleContext().setAccessibleName("Etiqueta contraseña");
        lblPass.getAccessibleContext().setAccessibleDescription("Etiqueta para el campo de contraseña");

        txtPass.getAccessibleContext().setAccessibleName("Campo de contraseña");
        txtPass.getAccessibleContext().setAccessibleDescription(
                "Ingrese su contraseña para iniciar sesión. Use el icono del ojo para mostrar u ocultar la contraseña");

        // Icono de mostrar/ocultar contraseña
        lblIconoRight.getAccessibleContext().setAccessibleName("Botón mostrar ocultar contraseña");
        lblIconoRight.getAccessibleContext().setAccessibleDescription(
                "Botón para mostrar u ocultar el texto de la contraseña. Presione para alternar entre modo visible y oculto");

        // Enlace "¿Olvidaste tu contraseña?"
        lblOlvidar.getAccessibleContext().setAccessibleName("Enlace olvidé mi contraseña");
        lblOlvidar.getAccessibleContext().setAccessibleDescription(
                "Enlace para recuperar contraseña si la ha olvidado. Presione Enter o Espacio para activar");
        lblOlvidar.setToolTipText("Haga clic para recuperar su contraseña");

        // Botón Iniciar Sesión
        btnIniciar.getAccessibleContext().setAccessibleName("Botón iniciar sesión");
        btnIniciar.getAccessibleContext().setAccessibleDescription(
                "Botón para iniciar sesión con las credenciales ingresadas");
        btnIniciar.setToolTipText("Haga clic para iniciar sesión con el correo y contraseña ingresados");

        // Configurar mnemónicos para navegación con teclado
        lblCorreo.setLabelFor(txtCorreo);
        lblPass.setLabelFor(txtPass);

    }

    // Método auxiliar para obtener SOLO el nombre del usuario desde la BD
    private String obtenerNombreUsuarioDesdeBD(String correo) {
        String sql = "SELECT nombres FROM usuarios WHERE correo_electronico = ?";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String nombres = rs.getString("nombres");
                // Retornar solo el primer nombre
                if (nombres != null && nombres.contains(" ")) {
                    return nombres.split(" ")[0];
                }
                return nombres;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener datos del usuario: " + e.getMessage());
        }
        return null;
    }

    private JButton crearBotonSocial(String rutaIcono, int x, int y, Color fondo) {
        URL url = getClass().getResource(rutaIcono);
        if (url == null) {
            System.err.println("❌ No se encontró la imagen: " + rutaIcono);
            return new JButton(); // botón vacío si la imagen no existe
        }

        JButton btn = new JButton(new ImageIcon(url));
        btn.setBounds(x, y, 51, 50);
        btn.setBackground(fondo);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.putClientProperty("JButton.buttonType", "roundRect");
        btn.putClientProperty("JButton.arc", 50);
        return btn;
    }

    private boolean usuarioTieneEncuesta(String correo) {
        String sql = "SELECT id FROM encuesta WHERE correo_usuario = ? LIMIT 1";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return true; // ✅ Ya tiene encuesta
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al consultar encuesta: " + e.getMessage());
        }

        return false; // ❌ No encontró encuesta
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

        header = new javax.swing.JPanel();
        titlelbl = new javax.swing.JLabel();
        Closebtn = new javax.swing.JPanel();
        Closetxt = new javax.swing.JLabel();
        minimizebtn = new javax.swing.JPanel();
        minimizetxt = new javax.swing.JLabel();
        registrarsebtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        header.setBackground(new java.awt.Color(30, 56, 136));
        header.setPreferredSize(new java.awt.Dimension(1440, 57));
        header.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                headerMouseDragged(evt);
            }
        });
        header.addMouseListener(new java.awt.event.MouseAdapter() {
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
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1125,
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

        registrarsebtn.setText("jButton1");
        registrarsebtn.setIconTextGap(12);
        registrarsebtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                registrarsebtnMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(header, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(221, 221, 221)
                                .addComponent(registrarsebtn)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(header, javax.swing.GroupLayout.PREFERRED_SIZE, 40,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(479, 479, 479)
                                .addComponent(registrarsebtn)
                                .addContainerGap(606, Short.MAX_VALUE)));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void initFocusOrder() {
        focusOrder = new ArrayList<>();

        // Definir el orden lógico de navegación
        focusOrder.add(txtCorreo);
        focusOrder.add(txtPass);

        // Agregar botones sociales usando las variables de clase
        if (btnGoogle != null)
            focusOrder.add(btnGoogle);
        if (btnFacebook != null)
            focusOrder.add(btnFacebook);
        if (btnOutlook != null)
            focusOrder.add(btnOutlook);

        // Agregar el botón de iniciar sesión
        if (btnIniciar != null)
            focusOrder.add(btnIniciar);

        // Agregar el label "¿Olvidaste tu contraseña?" como focusable
        if (lblOlvidar != null) {
            lblOlvidar.setFocusable(true);
            focusOrder.add(lblOlvidar);
        }

        // AGREGAR EL BOTÓN DE REGISTRARSE (IMPORTANTE)
        if (registrarsebtn != null) {
            registrarsebtn.setFocusable(true);
            focusOrder.add(registrarsebtn);
        }
    }

    private void setupKeyboardNavigation() {
        // Hacer que todos los componentes sean focusables
        for (Component comp : focusOrder) {
            comp.setFocusable(true);

            // Configurar efectos visuales básicos para cada tipo de componente
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                button.setFocusPainted(false); // Desactivar el efecto por defecto
            }
            // Para JTextField y JPasswordField, mantener los estilos originales de FlatLaf
        }

        // Agregar KeyListener global para navegación con Tab
        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                handleKeyPress(evt);
            }
        });

        // Asegurar que el JFrame sea focusable
        setFocusable(true);
        requestFocusInWindow();

        // Agregar FocusListener para cambiar el aspecto visual
        for (Component comp : focusOrder) {
            comp.addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusGained(java.awt.event.FocusEvent evt) {
                    showFocusIndicator(comp, true);
                }

                @Override
                public void focusLost(java.awt.event.FocusEvent evt) {
                    showFocusIndicator(comp, false);
                }
            });
        }
    }

    private void showFocusIndicator(Component comp, boolean hasFocus) {
        // Color azul claro para el indicador de foco
        Color focusColor = new Color(0x98CEFF); // El mismo azul claro que usas en tu diseño

        if (hasFocus) {
            // Componente con foco - agregar borde azul claro sutil
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                // Guardar el borde original si no se ha guardado
                if (button.getClientProperty("originalBorder") == null) {
                    button.putClientProperty("originalBorder", button.getBorder());
                }
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(focusColor, 2), // Borde azul claro fino
                        (javax.swing.border.Border) button.getClientProperty("originalBorder")));
            } else if (comp instanceof JTextField) {
                JTextField textField = (JTextField) comp;
                if (textField.getClientProperty("originalBorder") == null) {
                    textField.putClientProperty("originalBorder", textField.getBorder());
                }
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(focusColor, 2),
                        (javax.swing.border.Border) textField.getClientProperty("originalBorder")));
            } else if (comp instanceof JPasswordField) {
                JPasswordField passwordField = (JPasswordField) comp;
                if (passwordField.getClientProperty("originalBorder") == null) {
                    passwordField.putClientProperty("originalBorder", passwordField.getBorder());
                }
                passwordField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(focusColor, 2),
                        (javax.swing.border.Border) passwordField.getClientProperty("originalBorder")));
            } else if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                if (label.getClientProperty("originalBorder") == null) {
                    label.putClientProperty("originalBorder", label.getBorder());
                    label.putClientProperty("originalOpaque", label.isOpaque());
                    label.putClientProperty("originalBackground", label.getBackground());
                }
                // Para labels, solo cambiar el color del texto o agregar subrayado
                label.setForeground(focusColor);
                Font originalFont = label.getFont();
                label.setFont(originalFont.deriveFont(Font.BOLD));
            }
        } else {
            // Componente sin foco - restaurar apariencia original
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                Object originalBorder = button.getClientProperty("originalBorder");
                if (originalBorder != null) {
                    button.setBorder((javax.swing.border.Border) originalBorder);
                }
            } else if (comp instanceof JTextField) {
                JTextField textField = (JTextField) comp;
                Object originalBorder = textField.getClientProperty("originalBorder");
                if (originalBorder != null) {
                    textField.setBorder((javax.swing.border.Border) originalBorder);
                }
            } else if (comp instanceof JPasswordField) {
                JPasswordField passwordField = (JPasswordField) comp;
                Object originalBorder = passwordField.getClientProperty("originalBorder");
                if (originalBorder != null) {
                    passwordField.setBorder((javax.swing.border.Border) originalBorder);
                }
            } else if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                Object originalBorder = label.getClientProperty("originalBorder");
                Object originalOpaque = label.getClientProperty("originalOpaque");
                Object originalBackground = label.getClientProperty("originalBackground");

                if (originalBorder != null) {
                    label.setBorder((javax.swing.border.Border) originalBorder);
                }
                if (originalOpaque != null) {
                    label.setOpaque((Boolean) originalOpaque);
                }
                if (originalBackground != null) {
                    label.setBackground((Color) originalBackground);
                }
                // Restaurar estilo de texto original
                label.setForeground(new Color(0x1E3888)); // Color azul original de los labels
                Font originalFont = label.getFont();
                label.setFont(originalFont.deriveFont(Font.PLAIN)); // Quitar negrita
            }
        }
    }

    private void handleKeyPress(java.awt.event.KeyEvent evt) {
        switch (evt.getKeyCode()) {
            case java.awt.event.KeyEvent.VK_TAB:
                if (evt.isShiftDown()) {
                    moveFocusBackward();
                } else {
                    moveFocusForward();
                }
                evt.consume();
                break;

            case java.awt.event.KeyEvent.VK_ENTER:
                performActionOnFocusedComponent();
                evt.consume();
                break;

            case java.awt.event.KeyEvent.VK_SPACE:
                performActionOnFocusedComponent();
                evt.consume();
                break;

            case java.awt.event.KeyEvent.VK_ESCAPE:
                // Cerrar la aplicación con ESC
                int option = JOptionPane.showConfirmDialog(
                        this,
                        "¿Está seguro que desea salir?",
                        "Confirmar salida",
                        JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
                evt.consume();
                break;
        }
    }

    private void moveFocusForward() {
        currentFocusIndex = (currentFocusIndex + 1) % focusOrder.size();
        focusOrder.get(currentFocusIndex).requestFocusInWindow();
    }

    private void moveFocusBackward() {
        currentFocusIndex = (currentFocusIndex - 1 + focusOrder.size()) % focusOrder.size();
        focusOrder.get(currentFocusIndex).requestFocusInWindow();
    }

    private void performActionOnFocusedComponent() {
        Component focused = focusOrder.get(currentFocusIndex);

        if (focused instanceof JTextField || focused instanceof JPasswordField) {
            // Ya está enfocado, no hacer nada adicional
        } else if (focused instanceof JButton) {
            ((JButton) focused).doClick();
        } else if (focused instanceof JLabel) {
            // Simular click en el label "¿Olvidaste tu contraseña?"
            if (focused == lblOlvidar) {
                java.awt.event.MouseEvent mockEvent = new java.awt.event.MouseEvent(
                        focused,
                        java.awt.event.MouseEvent.MOUSE_CLICKED,
                        System.currentTimeMillis(),
                        0, 0, 0, 1, false);

                for (java.awt.event.MouseListener listener : lblOlvidar.getMouseListeners()) {
                    listener.mouseClicked(mockEvent);
                }
            }
        }
        // No llamar a updateFocusVisual() aquí - los FocusListener ya manejan esto
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

    private void minimizetxtMouseEntered(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_minimizetxtMouseEntered
        minimizebtn.setBackground(Color.decode("#2e4ca9"));
    }// GEN-LAST:event_minimizetxtMouseEntered

    private void minimizetxtMouseExited(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_minimizetxtMouseExited
        minimizebtn.setBackground(new Color(30, 56, 136));
        minimizetxt.setForeground(new Color(250, 250, 250));
    }// GEN-LAST:event_minimizetxtMouseExited

    private void minimizetxtMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_minimizetxtMouseClicked
        this.setState(JFrame.ICONIFIED);
    }// GEN-LAST:event_minimizetxtMouseClicked

    private void registrarsebtnMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_registrarsebtnMouseClicked
        abrirVentanaRegistro();
    }// GEN-LAST:event_registrarsebtnMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        System.setProperty("awt.embed.singleThread", "true");

        FlatLightLaf.setup();
        UIManager.put("TextComponent.arc", 20);
        UIManager.put("Component.borderColor", new Color(180, 180, 180));
        UIManager.put("Component.borderWidth", 2);

        // Configurar soporte para lectores de pantalla
        ToolTipManager.sharedInstance().setInitialDelay(500);
        ToolTipManager.sharedInstance().setDismissDelay(10000);

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

                new login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Closebtn;
    private javax.swing.JLabel Closetxt;
    private javax.swing.JPanel header;
    private javax.swing.JPanel minimizebtn;
    private javax.swing.JLabel minimizetxt;
    private javax.swing.JButton registrarsebtn;
    private javax.swing.JLabel titlelbl;
    // End of variables declaration//GEN-END:variables
}
