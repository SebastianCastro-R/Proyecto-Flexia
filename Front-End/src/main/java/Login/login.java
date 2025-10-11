/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Login;

import SignIn.SignIn;
import com.formdev.flatlaf.FlatLightLaf;
import com.mycompany.flexia.database.UsuariosDAO;

import Rounded.RoundedPanelS;
import java.awt.*;
import java.net.URL;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import Login.RecuperarContrasena;

/**
 *
 * @author kfkdm
 */
public class login extends javax.swing.JFrame {

    int xmouse, ymouse;
    private JTextField txtCorreo = new JTextField();
    private JPasswordField txtPass = new JPasswordField();

    /**
     * Creates new form login
     */
    public login() {
        
        initComponents();
        initStyles();
        initPanels();
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
                SwingConstants.CENTER
        );
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
                SwingConstants.CENTER
        );
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

        // configura el botón (dentro de initPanels), reemplaza las líneas previas donde ponías setOpaque(false) etc.
        registrarsebtn.setText("Registrarse");
        registrarsebtn.setFont(new Font("Epunda Slab Regular", Font.PLAIN, 20));
        registrarsebtn.setForeground(Color.BLACK);
        registrarsebtn.setBackground(new Color(0xFAFAFA));
        registrarsebtn.setFocusPainted(false);
        registrarsebtn.setBorderPainted(false); // opcional según si quieres borde
        registrarsebtn.setOpaque(false);         // IMPORTANT: deja true para que L&F pinte el fondo
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

        panelIzquierdo.add(registrarsebtn);

        // ===== Panel derecho (blanco) =====
        RoundedPanelS panelDerecho = new RoundedPanelS(40, new Color(0xFAFAFA)) {
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

        JButton btnGoogle = crearBotonSocial("/Images/google.png", inicioX, baseY, fondoCirculo);
        btnGoogle.setOpaque(false);
        btnGoogle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JButton btnFacebook = crearBotonSocial("/Images/facebook.png", inicioX + espacio + 40, baseY, fondoCirculo);
        btnFacebook.setOpaque(false);
        btnFacebook.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JButton btnOutlook = crearBotonSocial("/Images/outlook.png", inicioX + (espacio + 40) * 2, baseY, fondoCirculo);
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
        txtCorreo.setForeground(new Color(142,142,147));
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
        JLabel lblIconoRight = new JLabel(new javax.swing.ImageIcon(getClass().getResource("/Images/eye_off.png"))); // icono cerrado
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



        // ===== Label "¿Olvidaste tu contraseña?" =====
        JLabel lblOlvidar = new JLabel("<html><u>¿Olvidaste tu contraseña?</u></html>");
        lblOlvidar.setFont(new Font("Lato", Font.ITALIC, 14));
        lblOlvidar.setForeground(new Color(0x1E3888)); // azul oscuro
        lblOlvidar.setBounds(80, 570, 200, 20);
        lblOlvidar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Opcional: efecto hover (cambiar color al pasar)
        lblOlvidar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblOlvidar.setForeground(new Color(0x020300)); // un azul más oscuro
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblOlvidar.setForeground(new Color(0x1E3888));
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                String correoUsuario = txtCorreo.getText(); // el texto del campo de correo
                if (correoUsuario.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingresa tu correo primero.");
                    return;
                }

                RecuperarContrasena ventanaRecuperar = new RecuperarContrasena(correoUsuario);
                ventanaRecuperar.setVisible(true);
                ventanaRecuperar.setLocationRelativeTo(null); // centrar
            }
        });

        panelDerecho.add(lblOlvidar);

        // ===== Botón "Iniciar Sesión" =====
        JButton btnIniciar = new JButton("Iniciar Sesión");
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
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                UsuariosDAO dao = new UsuariosDAO();

                if (dao.autenticarUsuarioPorCorreo(correo, contrasena)) {
                    JOptionPane.showMessageDialog(
                        null,
                        "✅ Inicio de sesión exitoso.",
                        "Bienvenido",
                        JOptionPane.INFORMATION_MESSAGE
                    );

                    // Ejemplo: abrir la siguiente ventana
                    // new MenuPrincipal().setVisible(true);
                    // dispose();
                } else {
                    JOptionPane.showMessageDialog(
                        null,
                        "❌ Correo o contraseña incorrectos.",
                        "Error de inicio de sesión",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        panelDerecho.add(btnIniciar);

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

    



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
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
            .addComponent(Closetxt, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
        );
        ClosebtnLayout.setVerticalGroup(
            ClosebtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ClosebtnLayout.createSequentialGroup()
                .addComponent(Closetxt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

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
                .addComponent(minimizetxt, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        minimizebtnLayout.setVerticalGroup(
            minimizebtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(minimizetxt, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout headerLayout = new javax.swing.GroupLayout(header);
        header.setLayout(headerLayout);
        headerLayout.setHorizontalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(titlelbl, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1125, Short.MAX_VALUE)
                .addComponent(minimizebtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Closebtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        headerLayout.setVerticalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(titlelbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addComponent(minimizebtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Closebtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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
            .addComponent(header, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(layout.createSequentialGroup()
                .addGap(221, 221, 221)
                .addComponent(registrarsebtn))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(header, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(479, 479, 479)
                .addComponent(registrarsebtn)
                .addContainerGap(606, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void headerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_headerMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_headerMouseClicked

    private void headerMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_headerMousePressed
        xmouse = evt.getX();
        ymouse = evt.getY();
    }//GEN-LAST:event_headerMousePressed

    private void headerMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_headerMouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xmouse, y - ymouse);
    }//GEN-LAST:event_headerMouseDragged

    private void ClosetxtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ClosetxtMouseClicked
        System.exit(0);
    }//GEN-LAST:event_ClosetxtMouseClicked

    private void ClosetxtMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ClosetxtMouseEntered
        Closebtn.setBackground(Color.red);
        Closetxt.setForeground(new Color(250, 250, 250));
    }//GEN-LAST:event_ClosetxtMouseEntered

    private void ClosetxtMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ClosetxtMouseExited
        Closebtn.setBackground(new Color(30, 56, 136));
        Closetxt.setForeground(new Color(250, 250, 250));
    }//GEN-LAST:event_ClosetxtMouseExited

    private void minimizetxtMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minimizetxtMouseEntered
        minimizebtn.setBackground(Color.decode("#2e4ca9"));
    }//GEN-LAST:event_minimizetxtMouseEntered

    private void minimizetxtMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minimizetxtMouseExited
        minimizebtn.setBackground(new Color(30, 56, 136));
        minimizetxt.setForeground(new Color(250, 250, 250));
    }//GEN-LAST:event_minimizetxtMouseExited

    private void minimizetxtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minimizetxtMouseClicked
        this.setState(JFrame.ICONIFIED);
    }//GEN-LAST:event_minimizetxtMouseClicked

    private void registrarsebtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_registrarsebtnMouseClicked
        // Cerrar la ventana actual
        this.dispose();
        UIManager.put("ComboBox.arc", 30);
        // Abrir la ventana de LogIn
        SignIn signin = new SignIn();
        signin.setVisible(true);
        signin.setLocationRelativeTo(null); // Centrar en pantalla
    }//GEN-LAST:event_registrarsebtnMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        FlatLightLaf.setup();
        UIManager.put("TextComponent.arc", 20); // Redondea todos los textfields
        UIManager.put("Component.borderColor", new Color(180, 180, 180)); // Color de borde gris
        UIManager.put("Component.borderWidth", 2); // Grosor del borde

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
