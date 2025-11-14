/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Front_End;

import Back_End.FuenteUtil;
import Database.UsuariosDAO;
import java.awt.Image;

import com.formdev.flatlaf.FlatLightLaf;

import Back_End.CorreoService;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Karol
 */
public class SignIn extends javax.swing.JFrame {

    int xmouse, ymouse;
    private java.util.List<Component> focusOrder;
    private int currentFocusIndex = 0;

    /**
     * Creates new form SignIn
     */
    public SignIn() {
        FlatLightLaf.setup();
        UIManager.put("TextComponent.arc", 20);
        UIManager.put("Component.borderColor", new Color(180, 180, 180));
        UIManager.put("Component.borderWidth", 2);

        initComponents();
        initStyles();
        initButtons(); // <-- 👈 añadimos esta llamada
        initFocusOrder(); // <-- Agregar esta línea
        setupKeyboardNavigation(); // <-- Agregar esta línea

        setSize(1440, 1024);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    // Método para aplicar fuentes personalizadas (necesita la clase FuenteUtil)
    private void initStyles() {
        try {
            // Reemplaza FuenteUtil por el nombre de tu clase de utilidades de fuentes
            Title1.setFont(FuenteUtil.cargarFuente("EpundaSlab-ExtraBold.ttf", 36f));
            jLabel2.setFont(FuenteUtil.cargarFuente("EpundaSlab-Regular.ttf", 20f));
            O.setFont(FuenteUtil.cargarFuente("EpundaSlab-Regular.ttf", 20f));
            jLabel6.setFont(FuenteUtil.cargarFuente("EpundaSlab-Regular.ttf", 20f));
            jLabel5.setFont(FuenteUtil.cargarFuente("EpundaSlab-Regular.ttf", 20f));
            ButtonLogIn1.setFont(FuenteUtil.cargarFuente("EpundaSlab-Regular.ttf", 20f));

            Title.setFont(FuenteUtil.cargarFuente("EpundaSlab-SemiBold.ttf", 28f));
            // Aplicar la misma fuente al resto de etiquetas del formulario (panel derecho)
            Font labelFont = FuenteUtil.cargarFuente("EpundaSlab-Regular.ttf", 20f);

            FotoPefil.setFont(labelFont);
            FotoPefil8.setFont(labelFont);
            numIDLabel.setFont(labelFont);
            nombres.setFont(labelFont);
            Apellidos.setFont(labelFont);
            Fnacimiento.setFont(labelFont);
            Telefono1.setFont(labelFont);
            Telefono.setFont(labelFont);
            Correo.setFont(labelFont);
            password.setFont(labelFont);
            ButtonSignIn.setFont(labelFont);
            Closetxt.setFont(FuenteUtil.cargarFuente("EpundaSlab-ExtraBold.ttf", 30f));
            Closetxt.setForeground(new Color(250, 250, 250));
            minimizetxt.setFont(FuenteUtil.cargarFuente("EpundaSlab-EXtrabold.ttf", 30f));
            minimizetxt.setForeground(new Color(250, 250, 250));
            titlelbl.setFont(FuenteUtil.cargarFuente("EpundaSlab-ExtraBold.ttf", 28f));
            titlelbl.setForeground(new Color(250, 250, 250));

        } catch (Exception e) {
            System.err.println("Error al cargar las fuentes: " + e.getMessage());
            // Si falla, se usa la fuente por defecto de Swing
        }

        Genre.putClientProperty("JComponent.roundRect", true);
        Genre.putClientProperty("JComponent.arc", 30);
        PasswordText.putClientProperty("JComponent.roundRect", true);
        PasswordText.putClientProperty("JComponent.arc", 30);
        IDType.putClientProperty("JComponent.roundRect", true);
        IDType.putClientProperty("JComponent.arc", 30);

        // (Opcional) también puedes cambiar color de borde
        Genre.putClientProperty("JComponent.outline", "borderColor:#B4B4B4");
        PasswordText.putClientProperty("JComponent.outline", "borderColor:#B4B4B4");
        IDType.putClientProperty("JComponent.outline", "borderColor:#B4B4B4");
    }

    private void initButtons() {
        PasswordText.setBounds(240, 580, 180, 35);
        PasswordText.setFont(new Font("Lato", Font.PLAIN, 16));
        PasswordText.setForeground(new Color(142, 142, 147));
        PasswordText.setBackground(Color.WHITE);
        PasswordText.putClientProperty("JComponent.roundRect", true);
        PasswordText.putClientProperty("JComponent.arc", 30);

        // --- Icono a la izquierda ---
        JLabel lblIconoLeft = new JLabel(new ImageIcon(getClass().getResource("/Images/password.png")));
        lblIconoLeft.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 10));
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);
        leftPanel.add(lblIconoLeft, BorderLayout.CENTER);
        PasswordText.putClientProperty("JTextField.leadingComponent", leftPanel);

        // --- Icono a la derecha (mostrar/ocultar contraseña) ---
        JLabel lblIconoRight = new JLabel(new ImageIcon(getClass().getResource("/Images/eye_off.png")));
        lblIconoRight.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 15));
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.add(lblIconoRight, BorderLayout.CENTER);
        PasswordText.putClientProperty("JTextField.trailingComponent", rightPanel);

        // --- Interacción para mostrar/ocultar contraseña ---
        lblIconoRight.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblIconoRight.addMouseListener(new java.awt.event.MouseAdapter() {
            private boolean showing = false;

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                showing = !showing;
                if (showing) {
                    PasswordText.setEchoChar((char) 0);
                    lblIconoRight.setIcon(new ImageIcon(getClass().getResource("/Images/eye_on.png")));
                } else {
                    PasswordText.setEchoChar('●');
                    lblIconoRight.setIcon(new ImageIcon(getClass().getResource("/Images/eye_off.png")));
                }
            }
        });

        // --- Estilos FlatLaf ---
        PasswordText.putClientProperty("FlatLaf.style", "borderColor:#B4B4B4; borderWidth:3;");
        PasswordText.putClientProperty("JTextField.placeholderText", "●●●●●●●●●");
        PasswordText.putClientProperty("TextField.placeholderForeground", new Color(142, 142, 147));
    }

    private void initFocusOrder() {
        focusOrder = new ArrayList<>();

        // Definir el orden lógico de navegación (izquierda a derecha, arriba a abajo)
        focusOrder.add(IDType);
        focusOrder.add(numIDText);
        focusOrder.add(nameText);
        focusOrder.add(ApellidosText);
        focusOrder.add(DateText);
        focusOrder.add(Genre);
        focusOrder.add(celText);
        focusOrder.add(CorreoText);
        focusOrder.add(PasswordText);
        focusOrder.add(ButtonSignIn);
        focusOrder.add(ButtonLogIn1);

        // Hacer focusable los componentes que no lo son por defecto
        for (Component comp : focusOrder) {
            comp.setFocusable(true);
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
            } else if (comp instanceof JComboBox) {
                JComboBox<?> comboBox = (JComboBox<?>) comp;
                if (comboBox.getClientProperty("originalBorder") == null) {
                    comboBox.putClientProperty("originalBorder", comboBox.getBorder());
                }
                comboBox.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(focusColor, 2),
                        (javax.swing.border.Border) comboBox.getClientProperty("originalBorder")));
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
            } else if (comp instanceof JComboBox) {
                JComboBox<?> comboBox = (JComboBox<?>) comp;
                Object originalBorder = comboBox.getClientProperty("originalBorder");
                if (originalBorder != null) {
                    comboBox.setBorder((javax.swing.border.Border) originalBorder);
                }
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

        if (focused instanceof JTextField || focused instanceof JPasswordField || focused instanceof JComboBox) {
            // Ya está enfocado, no hacer nada adicional
        } else if (focused instanceof JButton) {
            ((JButton) focused).doClick();
        }
    }

    private void abrirVentanaLogin() {
        // Cerrar la ventana actual
        this.dispose();
        // Abrir la ventana de Login
        login login = new login();
        login.setVisible(true);
        login.setLocationRelativeTo(null); // Centrar en pantalla
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

        BackGround = new javax.swing.JPanel();
        header = new javax.swing.JPanel();
        titlelbl = new javax.swing.JLabel();
        Closebtn = new javax.swing.JPanel();
        Closetxt = new javax.swing.JLabel();
        minimizebtn = new javax.swing.JPanel();
        minimizetxt = new javax.swing.JLabel();
        blue = new componentes.RoundedPanel();
        Title1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        O = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        Dialog = new javax.swing.JLabel();
        Carpianin = new javax.swing.JLabel();
        ButtonLogIn1 = new javax.swing.JButton();
        white = new componentes.RoundedPanel();
        Title = new javax.swing.JLabel();
        FotoPefil = new javax.swing.JLabel();
        Telefono = new javax.swing.JLabel();
        ButtonSignIn = new javax.swing.JButton();
        Apellidos = new javax.swing.JLabel();
        numIDLabel = new javax.swing.JLabel();
        nombres = new javax.swing.JLabel();
        password = new javax.swing.JLabel();
        Fnacimiento = new javax.swing.JLabel();
        Correo = new javax.swing.JLabel();
        IDType = new javax.swing.JComboBox<>();
        FotoPefil8 = new javax.swing.JLabel();
        PasswordText = new javax.swing.JPasswordField();
        jLabel1 = new javax.swing.JLabel();
        Telefono1 = new javax.swing.JLabel();
        Genre = new javax.swing.JComboBox<>();
        CorreoText = new componentes.IconTextField();
        ApellidosText = new componentes.IconTextField();
        nameText = new componentes.IconTextField();
        numIDText = new componentes.IconTextField();
        celText = new componentes.IconTextField();
        DateText = new componentes.IconTextField();
        LabelImagen1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        BackGround.setBackground(new java.awt.Color(255, 255, 255));
        BackGround.setPreferredSize(new java.awt.Dimension(1440, 1024));
        BackGround.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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

        BackGround.add(header, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1440, 40));

        blue.setBackground(new java.awt.Color(152, 206, 255));
        blue.setRightRounded(false);
        blue.setAlignmentX(271.0F);
        blue.setAlignmentY(176.0F);
        blue.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Title1.setFont(new java.awt.Font("Epunda Slab SemiBold", 1, 36)); // NOI18N
        Title1.setText("BIENVENIDO");
        blue.add(Title1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 110, -1, -1));

        jLabel2.setFont(new java.awt.Font("Epunda Slab", 0, 20)); // NOI18N
        jLabel2.setText("Crea tu cuenta");
        blue.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 160, -1, -1));

        O.setFont(new java.awt.Font("Epunda Slab", 0, 20)); // NOI18N
        O.setText("O");
        blue.add(O, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 190, -1, -1));

        jLabel6.setFont(new java.awt.Font("Epunda Slab", 0, 20)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Si ya tienes cuenta,");
        jLabel6.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        blue.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 270, 170, 30));

        jLabel5.setFont(new java.awt.Font("Epunda Slab", 0, 20)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Inicia Sesión");
        jLabel5.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        blue.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 300, 170, 30));

        Dialog.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/dialog.png"))); // NOI18N
        Dialog.setLabelFor(jLabel5);
        blue.add(Dialog, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 230, -1, 175));

        Carpianin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Mascota.png"))); // NOI18N
        blue.add(Carpianin, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 390, -1, 184));

        ButtonLogIn1.setBackground(new java.awt.Color(250, 250, 250));
        ButtonLogIn1.setFont(new java.awt.Font("Epunda Slab", 0, 20)); // NOI18N
        ButtonLogIn1.setText("Iniciar Sesión");
        ButtonLogIn1.setToolTipText("");
        ButtonLogIn1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ButtonLogIn1MouseClicked(evt);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ButtonLogIn1MouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                ButtonLogIn1MouseExited(evt);
            }
        });
        ButtonLogIn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonLogIn1ActionPerformed(evt);
            }
        });
        blue.add(ButtonLogIn1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 580, -1, 40));

        BackGround.add(blue, new org.netbeans.lib.awtextra.AbsoluteConstraints(271, 180, 449, 729));

        white.setBackground(new java.awt.Color(250, 250, 250));
        white.setLeftRounded(false);
        white.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Title.setFont(new java.awt.Font("Epunda Slab SemiBold", 1, 28)); // NOI18N
        Title.setText("Registrarse");
        white.add(Title, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 110, -1, -1));

        FotoPefil.setFont(new java.awt.Font("Epunda Slab", 0, 20)); // NOI18N
        FotoPefil.setText("Foto de Perfil");
        white.add(FotoPefil, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 200, -1, -1));

        Telefono.setFont(new java.awt.Font("Epunda Slab", 0, 20)); // NOI18N
        Telefono.setText("Teléfono");
        white.add(Telefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 480, -1, -1));

        ButtonSignIn.setBackground(new java.awt.Color(152, 206, 255));
        ButtonSignIn.setFont(new java.awt.Font("Epunda Slab", 0, 20)); // NOI18N
        ButtonSignIn.setText("Registrarse");
        ButtonSignIn.setToolTipText("");
        ButtonSignIn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ButtonSignIn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ButtonSignInMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                ButtonSignInMouseExited(evt);
            }
        });
        ButtonSignIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonSignInActionPerformed(evt);
            }
        });
        ButtonSignIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonSignInActionPerformed(evt);
            }
        });

        ButtonLogIn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abrirVentanaLogin();
            }
        });
        white.add(ButtonSignIn, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 640, -1, 40));

        Apellidos.setFont(new java.awt.Font("Epunda Slab", 0, 20)); // NOI18N
        Apellidos.setText("Apellidos");
        white.add(Apellidos, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 340, -1, -1));

        numIDLabel.setFont(new java.awt.Font("Epunda Slab", 0, 20)); // NOI18N
        numIDLabel.setText("N° de Documento");
        white.add(numIDLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 270, -1, -1));

        nombres.setFont(new java.awt.Font("Epunda Slab", 0, 20)); // NOI18N
        nombres.setText("Nombres");
        white.add(nombres, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 340, -1, -1));

        password.setFont(new java.awt.Font("Epunda Slab", 0, 20)); // NOI18N
        password.setText("Contraseña");
        white.add(password, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 550, -1, -1));

        Fnacimiento.setFont(new java.awt.Font("Epunda Slab", 0, 20)); // NOI18N
        Fnacimiento.setText("Fecha de Nacimiento");
        white.add(Fnacimiento, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 410, -1, -1));

        Correo.setFont(new java.awt.Font("Epunda Slab", 0, 20)); // NOI18N
        Correo.setText("Correo Electrónico");
        white.add(Correo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 550, -1, -1));

        IDType.setFont(new java.awt.Font("Lato", 0, 16)); // NOI18N
        IDType.setForeground(new java.awt.Color(142, 142, 147));
        IDType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "C.C", "C.E", "T.I", "Pasaporte" }));
        IDType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IDTypeActionPerformed(evt);
            }
        });
        white.add(IDType, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 230, 180, 35));

        FotoPefil8.setFont(new java.awt.Font("Epunda Slab", 0, 20)); // NOI18N
        FotoPefil8.setText("Tipo de Documento");
        white.add(FotoPefil8, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 200, -1, -1));

        PasswordText.setFont(new java.awt.Font("Lato", 0, 12)); // NOI18N
        PasswordText.setForeground(new java.awt.Color(142, 142, 147));
        PasswordText.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                PasswordTextMousePressed(evt);
            }
        });
        PasswordText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PasswordTextActionPerformed(evt);
            }
        });
        white.add(PasswordText, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 580, 180, 35));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Group 5.png"))); // NOI18N
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileNameExtensionFilter("Imágenes", "jpg", "png", "jpeg"));

            int res = chooser.showOpenDialog(null);
            if (res == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();

                try {
                    // Convertir imagen a bytes
                    FileInputStream fis = new FileInputStream(file);
                    imagenSeleccionada = fis.readAllBytes();

                    // Mostrar imagen en jLabel circular
                    ImageIcon icon = new ImageIcon(imagenSeleccionada);
                    jLabel1.setIcon(new ImageIcon(redondearImagen(icon.getImage(), 150)));

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error al cargar imagen");
                }
            }
        }

        private String redondearImagen(java.awt.Image image, int size) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'redondearImagen'");
        }
    });

        white.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 230, -1, -1));

        CorreoText.setForeground(new java.awt.Color(142, 142, 147));
        CorreoText.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/correo.png"))); // NOI18N
        CorreoText.setText("Escribe tu Correo");
        CorreoText.setToolTipText("");
        CorreoText.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CorreoTextMouseClicked(evt);
            }
        });
        white.add(CorreoText, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 580, 180, 35));

        ApellidosText.setForeground(new java.awt.Color(142, 142, 147));
        ApellidosText.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/user.png"))); // NOI18N
        ApellidosText.setIconGap(2);
        ApellidosText.setText("Escribe tus apellidos");
        ApellidosText.setToolTipText("");
        ApellidosText.setFont(new java.awt.Font("Lato", 0, 15)); // NOI18N
        ApellidosText.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ApellidosTextMousePressed(evt);
            }
        });
        ApellidosText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ApellidosTextActionPerformed(evt);
            }
        });
        white.add(ApellidosText, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 370, 180, 35));

        nameText.setForeground(new java.awt.Color(142, 142, 147));
        nameText.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/user.png"))); // NOI18N
        nameText.setIconGap(2);
        nameText.setText("Escribe tus nombres");
        nameText.setToolTipText("");
        nameText.setFont(new java.awt.Font("Lato", 0, 15)); // NOI18N
        nameText.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                nameTextMousePressed(evt);
            }
        });
        nameText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameTextActionPerformed(evt);
            }
        });
        white.add(nameText, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 370, 180, 35));

        numIDText.setForeground(new java.awt.Color(142, 142, 147));
        numIDText.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/id-card.png"))); // NOI18N
        numIDText.setIconGap(2);
        numIDText.setText("1234567891");
        numIDText.setToolTipText("");
        numIDText.setFont(new java.awt.Font("Lato", 0, 15)); // NOI18N
        numIDText.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                numIDTextMousePressed(evt);
            }
        });
        white.add(numIDText, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 300, 180, 35));

        celText.setForeground(new java.awt.Color(142, 142, 147));
        celText.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/mynaui_telephone.png"))); // NOI18N
        celText.setIconGap(2);
        celText.setText("Número de Teléfono");
        celText.setToolTipText("");
        celText.setFont(new java.awt.Font("Lato", 0, 15)); // NOI18N
        celText.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                celTextMousePressed(evt);
            }
        });
        white.add(celText, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 510, 180, 35));

        DateText.setForeground(new java.awt.Color(142, 142, 147));
        DateText.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/date.png"))); // NOI18N
        DateText.setIconGap(2);
        DateText.setPlaceholder("YYYY-MM-DD");
        DateText.setToolTipText("");
        DateText.setFont(new java.awt.Font("Lato", 0, 15)); // NOI18N
        DateText.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                DateTextMousePressed(evt);
            }
        });
        DateText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DateTextActionPerformed(evt);
            }
        });
        white.add(DateText, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 440, 180, 35));

        BackGround.add(white, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 180, 449, 729));

        LabelImagen1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Background_1.jpg"))); // NOI18N
        BackGround.add(LabelImagen1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1570, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(BackGround, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(BackGround, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void ButtonSignInActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_ButtonSignInActionPerformed
        // 1️⃣ Capturar los datos del formulario
        String tipoId = IDType.getSelectedItem().toString();
        String numeroId = numIDText.getText().trim();
        String nombres = nameText.getText().trim();
        String apellidos = ApellidosText.getText().trim();
        String correo = CorreoText.getText().trim();
        String contrasena = new String(PasswordText.getPassword()).trim();
        String fechaNacimiento = DateText.getText().trim(); // formato: YYYY-MM-DD
        String telefono = celText.getText().trim();
        String genero = Genre.getSelectedItem().toString();

        // 2️⃣ Validaciones de campos vacíos
        if (tipoId.isEmpty() || numeroId.isEmpty() || nombres.isEmpty() || apellidos.isEmpty() ||
                correo.isEmpty() || contrasena.isEmpty() || fechaNacimiento.isEmpty() || telefono.isEmpty()
                || genero.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Todos los campos son obligatorios.", "Campos vacíos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 3️⃣ Validar campos numéricos
        if (!numeroId.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "⚠️ El número de identificación debe contener solo números.",
                    "Error de formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!telefono.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "⚠️ El número de teléfono debe contener solo números.",
                    "Error de formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 4️⃣ Validar formato de correo
        if (!correo.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            JOptionPane.showMessageDialog(this, "⚠️ Ingresa un correo electrónico válido.", "Error de formato",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 5️⃣ Validar longitud de contraseña
        if (contrasena.length() < 6) {
            JOptionPane.showMessageDialog(this, "⚠️ La contraseña debe tener al menos 6 caracteres.",
                    "Contraseña débil", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 6️⃣ Validar formato de fecha
        java.sql.Date fechaSQL;
        try {
            fechaSQL = java.sql.Date.valueOf(fechaNacimiento);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "⚠️ La fecha debe tener el formato YYYY-MM-DD.", "Error de formato",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 7️⃣ Mostrar ventana emergente de Términos y Condiciones
        boolean aceptado = mostrarTerminosYCondiciones();
        if (!aceptado) {
            JOptionPane.showMessageDialog(this, "Debe aceptar los Términos y Condiciones para continuar.",
                    "Registro cancelado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 8️⃣ Registrar usuario solo si aceptó los términos
        UsuariosDAO dao = new UsuariosDAO();
        boolean exito = dao.registrarUsuario(
                tipoId,
                numeroId,
                nombres,
                apellidos,
                correo,
                contrasena, // el DAO la hashea internamente
                fechaSQL,
                telefono,
                genero,
                false, // esPremium por defecto
                imagenSeleccionada
        );

        if (exito) {
            JOptionPane.showMessageDialog(this, "✅ Registro exitoso. ¡Bienvenido/a a FLEX-IA!");

            // Enviar correo de confirmación
            try {
                String asunto = "🎉 ¡Bienvenido a FLEX-IA!";
                String rutaPlantilla = "src/main/resources/templates/plantilla_bienvenida.html";
                String cuerpoHTML = CorreoService.cargarPlantilla(rutaPlantilla, nombres, apellidos);

                // Llamada al servicio de correo
                new Thread(() -> {
                    CorreoService.enviarCorreo(correo, asunto, cuerpoHTML);
                }).start();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "⚠️ El registro fue exitoso, pero no se pudo enviar el correo de confirmación.\n"
                                + "Error: " + e.getMessage(),
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            }

            new login().setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Error al registrar el usuario. Intente nuevamente.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

    }// GEN-LAST:event_ButtonSignInActionPerformed

    private void ButtonLogIn1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_ButtonLogIn1ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_ButtonLogIn1ActionPerformed

    private void IDTypeActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_IDTypeActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_IDTypeActionPerformed

    private void ButtonLogIn1MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_ButtonLogIn1MouseClicked
        abrirVentanaLogin();
    }// GEN-LAST:event_ButtonLogIn1MouseClicked

    private void ButtonLogIn1MouseExited(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_ButtonLogIn1MouseExited
        ButtonLogIn1.setBackground(new Color(0xFAFAFA));
        ButtonLogIn1.setForeground(Color.BLACK);
    }// GEN-LAST:event_ButtonLogIn1MouseExited

    private void ButtonLogIn1MouseEntered(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_ButtonLogIn1MouseEntered
        ButtonLogIn1.setBackground(new Color(0x1E3888));
        ButtonLogIn1.setForeground(Color.WHITE);
    }// GEN-LAST:event_ButtonLogIn1MouseEntered

    private void ButtonSignInMouseEntered(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_ButtonSignInMouseEntered
        ButtonSignIn.setBackground(new Color(0x1E3888));
        ButtonSignIn.setForeground(Color.WHITE);
    }// GEN-LAST:event_ButtonSignInMouseEntered

    private void ButtonSignInMouseExited(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_ButtonSignInMouseExited
        ButtonSignIn.setBackground(new Color(0x98CEFF));
        ButtonSignIn.setForeground(Color.BLACK);
    }// GEN-LAST:event_ButtonSignInMouseExited

    private void CorreoTextMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_CorreoTextMouseClicked
        CorreoText.setText("");
    }// GEN-LAST:event_CorreoTextMouseClicked

    private void ApellidosTextMousePressed(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_ApellidosTextMousePressed
        ApellidosText.setText("");
    }// GEN-LAST:event_ApellidosTextMousePressed

    private void nameTextMousePressed(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_nameTextMousePressed
        nameText.setText("");
    }// GEN-LAST:event_nameTextMousePressed

    private void nameTextActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_nameTextActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_nameTextActionPerformed

    private void numIDTextMousePressed(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_numIDTextMousePressed
        numIDText.setText("");
    }// GEN-LAST:event_numIDTextMousePressed

    private void ApellidosTextActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_ApellidosTextActionPerformed
        ApellidosText.setText("");
    }// GEN-LAST:event_ApellidosTextActionPerformed

    private void celTextMousePressed(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_celTextMousePressed
        celText.setText("");
    }// GEN-LAST:event_celTextMousePressed

    private void DateTextMousePressed(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_DateTextMousePressed
        DateText.setText("");
    }// GEN-LAST:event_DateTextMousePressed

    private void DateTextActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_DateTextActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_DateTextActionPerformed

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

    private void PasswordTextActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_PasswordTextActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_PasswordTextActionPerformed

    private void PasswordTextMousePressed(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_PasswordTextMousePressed
        PasswordText.setText("");
    }// GEN-LAST:event_PasswordTextMousePressed

    private boolean mostrarTerminosYCondiciones() {
        // 🧾 Texto HTML con estilo visual y márgenes
        String terminosHTML = """
                <html>
                <head>
                    <style>
                        body {
                            font-family: 'Segoe UI', sans-serif;
                            font-size: 13px;
                            color: #333333;
                            text-align: justify;
                            margin: 12px;
                            line-height: 1.5;
                        }
                        h2 {
                            text-align: center;
                            color: #1e88e5;
                            margin-top: 0;
                        }
                        p {
                            margin: 10px 0;
                        }
                        b {
                            color: #1e88e5;
                        }
                    </style>
                </head>
                <body>
                    <h2>Términos y Condiciones de Uso</h2>
                    <p>Bienvenido/a a <b>FLEX-IA</b>, una aplicación diseñada para la prevención y tratamiento del síndrome del túnel carpiano.</p>

                    <p>Al registrarse, usted acepta que sus datos personales (como nombre, documento, edad, género, correo, número de teléfono, entre otros)
                    sean recolectados, almacenados y tratados por FLEX-IA con el propósito de ofrecerle un servicio personalizado en su proceso
                    de diagnóstico y recuperación.</p>

                    <p>Los datos ingresados durante el registro y en la <b>encuesta del dolor</b> serán utilizados únicamente con fines clínicos, estadísticos y de mejora del servicio.
                    FLEX-IA garantiza la confidencialidad y protección de su información conforme a las leyes de protección de datos vigentes.</p>

                    <p>En ningún caso compartiremos su información con terceros sin su consentimiento previo. Usted podrá solicitar la eliminación de sus datos en cualquier momento.</p>

                    <p>Al marcar la casilla “He leído y acepto los Términos y Condiciones” y presionar Aceptar, usted confirma haber leído y comprendido los presentes términos.</p>

                    <p style='margin-top:15px; font-size:12px; color:#666;'>Última actualización: Octubre 2025</p>
                </body>
                </html>
                """;

        // 🧠 Usamos JEditorPane en lugar de JLabel (mejor renderizado HTML)
        JEditorPane editor = new JEditorPane("text/html", terminosHTML);
        editor.setEditable(false);
        editor.setOpaque(true);
        editor.setBackground(Color.WHITE);

        // 🔽 Scroll solo vertical
        JScrollPane scroll = new JScrollPane(editor);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setPreferredSize(new java.awt.Dimension(580, 340));
        scroll.setBorder(BorderFactory.createEmptyBorder());

        // ✅ Checkbox de aceptación
        JCheckBox checkBox = new JCheckBox("He leído y acepto los Términos y Condiciones");
        checkBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // 🎛 Botones
        JButton btnAceptar = new JButton("Aceptar");
        JButton btnCancelar = new JButton("Cancelar");

        btnAceptar.setEnabled(false);
        btnAceptar.setFocusPainted(false);
        btnCancelar.setFocusPainted(false);

        // 🎨 Estilo moderno (opcional)
        btnAceptar.setBackground(new Color(30, 136, 229));
        btnAceptar.setForeground(Color.WHITE);
        btnCancelar.setBackground(new Color(240, 240, 240));
        btnCancelar.setForeground(Color.BLACK);
        btnAceptar.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btnCancelar.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        // 🔄 Habilitar botón solo si se marca la casilla
        checkBox.addActionListener(e -> btnAceptar.setEnabled(checkBox.isSelected()));

        // 🧱 Panel inferior (checkbox + botones)
        JPanel panelInferior = new JPanel(new BorderLayout(10, 10));
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelInferior.add(checkBox, BorderLayout.WEST);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBotones.add(btnCancelar);
        panelBotones.add(btnAceptar);
        panelInferior.add(panelBotones, BorderLayout.EAST);

        // 🪟 Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.add(scroll, BorderLayout.CENTER);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        // 🧩 Crear y configurar el diálogo
        JDialog dialogo = new JDialog(this, "Términos y Condiciones", true);
        dialogo.getContentPane().add(panelPrincipal);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialogo.setResizable(false);
        dialogo.pack();
        dialogo.setLocationRelativeTo(this);

        // 🧭 Controlar respuesta
        final boolean[] aceptado = { false };
        btnAceptar.addActionListener(e -> {
            aceptado[0] = true;
            dialogo.dispose();
        });
        btnCancelar.addActionListener(e -> {
            aceptado[0] = false;
            dialogo.dispose();
        });

        dialogo.setVisible(true);
        return aceptado[0];
    }

    public BufferedImage redondearImagen(Image img, int size) {
        BufferedImage circleBuffer = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = circleBuffer.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Crear máscara circular
        Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, size, size);
        g2.setClip(circle);

        g2.drawImage(img.getScaledInstance(size, size, Image.SCALE_SMOOTH), 0, 0, null);
        g2.dispose();

        return circleBuffer;
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
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SignIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SignIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SignIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SignIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        // </editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SignIn().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private byte[] imagenSeleccionada; // almacenará la imagen temporalmente
    private javax.swing.JLabel Apellidos;
    private componentes.IconTextField ApellidosText;
    private javax.swing.JPanel BackGround;
    private javax.swing.JButton ButtonLogIn1;
    private javax.swing.JButton ButtonSignIn;
    private javax.swing.JLabel Carpianin;
    private javax.swing.JPanel Closebtn;
    private javax.swing.JLabel Closetxt;
    private javax.swing.JLabel Correo;
    private componentes.IconTextField CorreoText;
    private componentes.IconTextField DateText;
    private javax.swing.JLabel Dialog;
    private javax.swing.JLabel Fnacimiento;
    private javax.swing.JLabel FotoPefil;
    private javax.swing.JLabel FotoPefil8;
    private javax.swing.JComboBox<String> Genre;
    private javax.swing.JComboBox<String> IDType;
    private javax.swing.JLabel LabelImagen1;
    private javax.swing.JLabel O;
    private javax.swing.JPasswordField PasswordText;
    private javax.swing.JLabel Telefono;
    private javax.swing.JLabel Telefono1;
    private javax.swing.JLabel Title;
    private javax.swing.JLabel Title1;
    private componentes.RoundedPanel blue;
    private componentes.IconTextField celText;
    private javax.swing.JPanel header;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel minimizebtn;
    private javax.swing.JLabel minimizetxt;
    private componentes.IconTextField nameText;
    private javax.swing.JLabel nombres;
    private javax.swing.JLabel numIDLabel;
    private componentes.IconTextField numIDText;
    private javax.swing.JLabel password;
    private javax.swing.JLabel titlelbl;
    private componentes.RoundedPanel white;
    // End of variables declaration//GEN-END:variables
}
