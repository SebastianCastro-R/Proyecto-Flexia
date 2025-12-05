package Back_End.Ads;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;

public class AdPopupDialog extends JDialog {
    private AdContent adContent;
    private AdListener adListener;
    private JButton closeBtn;
    private JButton actionButton;
    private javax.swing.Timer enableTimer;
    private int secondsRemaining = 5;
    private JLabel timerLabel;
    
    public AdPopupDialog(JFrame parent, AdContent adContent) {
        super(parent, "Patrocinado", true);
        this.adContent = adContent;
        
        // Eliminar barra de título nativa - control total sobre el cierre
        setUndecorated(true);
        
        initUI();
        setupListeners();
        startCloseTimer();
    }
    
    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setSize(400, 300);
        setLocationRelativeTo(getOwner());
        setResizable(false);
        
        // Header con nuestro propio botón de cerrar
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(30, 56, 136));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel titleLabel = new JLabel("Publicidad");
        titleLabel.setFont(new Font("Epunda Slab", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        closeBtn = new JButton("×");
        closeBtn.setFont(new Font("Arial", Font.BOLD, 18));
        closeBtn.setForeground(new Color(200, 200, 200)); // Gris inicial (deshabilitado)
        closeBtn.setBackground(new Color(30, 56, 136));
        closeBtn.setBorderPainted(false);
        closeBtn.setFocusPainted(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        closeBtn.setEnabled(false);
        closeBtn.addActionListener(e -> closeAd());
        headerPanel.add(closeBtn, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Content
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.WHITE);
        
        // Title
        JLabel adTitle = new JLabel(adContent.getTitle());
        adTitle.setFont(new Font("Epunda Slab ExtraBold", Font.BOLD, 18));
        adTitle.setForeground(new Color(30, 56, 136));
        adTitle.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Description
        JTextArea adDesc = new JTextArea(adContent.getDescription());
        adDesc.setFont(new Font("Epunda Slab", Font.PLAIN, 14));
        adDesc.setForeground(Color.DARK_GRAY);
        adDesc.setWrapStyleWord(true);
        adDesc.setLineWrap(true);
        adDesc.setEditable(false);
        adDesc.setBackground(Color.WHITE);
        adDesc.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        // Crear botón de acción (Premium o Más Información)
        if (adContent.getActionUrl().startsWith("premium://")) {
            actionButton = createStyledButton("Obtener Premium", new Color(30, 56, 136));
            actionButton.addActionListener(e -> onPremiumClicked());
        } else {
            actionButton = createStyledButton("Más Información", new Color(30, 56, 136));
            actionButton.addActionListener(e -> onAdClicked());
        }
        actionButton.setEnabled(false); // Deshabilitado inicialmente
        buttonPanel.add(actionButton);
        
        // Panel inferior con temporizador
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        
        // Label del temporizador
        timerLabel = new JLabel("Cierre disponible en 5 segundos", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        timerLabel.setForeground(Color.RED);
        bottomPanel.add(timerLabel, BorderLayout.SOUTH);
        
        contentPanel.add(adTitle, BorderLayout.NORTH);
        contentPanel.add(adDesc, BorderLayout.CENTER);
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(contentPanel, BorderLayout.CENTER);
        
        // Footer
        JLabel footerLabel = new JLabel("Anuncio • Usuario Gratuito", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        footerLabel.setForeground(Color.GRAY);
        footerLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        add(footerLabel, BorderLayout.SOUTH);
    }
    
    private void startCloseTimer() {
        enableTimer = new javax.swing.Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secondsRemaining--;
                
                // Actualizar label del temporizador
                if (secondsRemaining > 0) {
                    timerLabel.setText("Cierre disponible en " + secondsRemaining + " segundos");
                } else {
                    timerLabel.setText("Listo para cerrar");
                    timerLabel.setForeground(new Color(0, 150, 0)); // Verde cuando está listo
                    enableCloseButton();
                    enableTimer.stop();
                }
            }
        });
        enableTimer.start();
    }
    
    private void enableCloseButton() {
        // Habilitar botón de cerrar
        closeBtn.setEnabled(true);
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Habilitar botón de acción
        actionButton.setEnabled(true);
        actionButton.setForeground(Color.WHITE);
        actionButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Epunda Slab Medium", Font.PLAIN, 14));
        button.setBackground(bgColor);
        button.setForeground(new Color(200, 200, 200)); // Gris inicial (deshabilitado)
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // Cursor normal cuando deshabilitado
        button.setPreferredSize(new Dimension(140, 35));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(bgColor.darker());
                }
            }
            
            public void mouseExited(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(bgColor);
                }
            }
        });
        
        return button;
    }
    
    private void setupListeners() {
        // Configurar comportamiento cuando se abre la ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                if (adListener != null) {
                    adListener.onAdShown("popup");
                }
            }
        });
        
        // Bloquear tecla ESC
        KeyStroke escapeKey = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKey, "blockEscape");
        getRootPane().getActionMap().put("blockEscape", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!closeBtn.isEnabled()) {
                    Toolkit.getDefaultToolkit().beep(); // Sonido si intenta cerrar antes de tiempo
                    
                    // Parpadear el temporizador
                    Timer blinkTimer = new Timer(100, new ActionListener() {
                        private int blinkCount = 0;
                        private Color originalColor = timerLabel.getForeground();
                        
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            if (blinkCount < 6) {
                                timerLabel.setForeground(blinkCount % 2 == 0 ? Color.RED : originalColor);
                                blinkCount++;
                            } else {
                                timerLabel.setForeground(originalColor);
                                ((Timer) evt.getSource()).stop();
                            }
                        }
                    });
                    blinkTimer.start();
                } else {
                    closeAd();
                }
            }
        });
        
        // Bloquear Alt+F4
        KeyStroke altF4 = KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK);
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(altF4, "blockAltF4");
        getRootPane().getActionMap().put("blockAltF4", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Toolkit.getDefaultToolkit().beep(); // Siempre sonar para Alt+F4
            }
        });
        
        // Bloquear Ctrl+W (cierre común en algunas apps)
        KeyStroke ctrlW = KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK);
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ctrlW, "blockCtrlW");
        getRootPane().getActionMap().put("blockCtrlW", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Toolkit.getDefaultToolkit().beep();
            }
        });
    }
    
    private void onAdClicked() {
        if (!actionButton.isEnabled()) return; // No hacer nada si no está habilitado
        
        if (adListener != null) {
            adListener.onAdClicked("popup");
        }
        
        try {
            Desktop.getDesktop().browse(new URI(adContent.getActionUrl()));
        } catch (Exception ex) {
            System.err.println("Error al abrir URL: " + ex.getMessage());
        }
        
        dispose();
    }
    
    private void onPremiumClicked() {
        if (!actionButton.isEnabled()) return; // No hacer nada si no está habilitado
        
        if (adListener != null) {
            adListener.onPremiumUpgradeClicked();
        }
        dispose();
    }
    
    private void closeAd() {
        if (!closeBtn.isEnabled()) {
            Toolkit.getDefaultToolkit().beep(); // Sonido si intenta cerrar antes de tiempo
            return;
        }
        
        if (adListener != null) {
            adListener.onAdClosed("popup");
        }
        
        dispose();
    }
    
    public void setAdListener(AdListener listener) {
        this.adListener = listener;
    }
    
    @Override
    public void dispose() {
        if (enableTimer != null && enableTimer.isRunning()) {
            enableTimer.stop();
        }
        super.dispose();
    }
}