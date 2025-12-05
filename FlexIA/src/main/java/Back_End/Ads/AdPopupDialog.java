package Back_End.Ads;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;

public class AdPopupDialog extends JDialog {
    private AdContent adContent;
    private AdListener adListener;
    
    public AdPopupDialog(JFrame parent, AdContent adContent) {
        super(parent, "Patrocinado", true);
        this.adContent = adContent;
        initUI();
        setupListeners();
    }
    
    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setSize(400, 300);
        setLocationRelativeTo(getOwner());
        setResizable(false);
        setUndecorated(true);
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(30, 56, 136));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel titleLabel = new JLabel("Publicidad");
        titleLabel.setFont(new Font("Epunda Slab", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JButton closeBtn = new JButton("×");
        closeBtn.setFont(new Font("Arial", Font.BOLD, 18));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setBackground(new Color(30, 56, 136));
        closeBtn.setBorderPainted(false);
        closeBtn.setFocusPainted(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        if (adContent.getActionUrl().startsWith("premium://")) {
            JButton premiumBtn = createStyledButton("Obtener Premium", new Color(30, 56, 136));
            premiumBtn.addActionListener(e -> onPremiumClicked());
            buttonPanel.add(premiumBtn);
        } else {
            JButton learnMoreBtn = createStyledButton("Más Información", new Color(30, 56, 136));
            learnMoreBtn.addActionListener(e -> onAdClicked());
            buttonPanel.add(learnMoreBtn);
        }
        

        contentPanel.add(adTitle, BorderLayout.NORTH);
        contentPanel.add(adDesc, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(contentPanel, BorderLayout.CENTER);
        
        // Footer
        JLabel footerLabel = new JLabel("Anuncio • Usuario Gratuito", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        footerLabel.setForeground(Color.GRAY);
        footerLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        add(footerLabel, BorderLayout.SOUTH);
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Epunda Slab Medium", Font.PLAIN, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(140, 35));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void setupListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                if (adListener != null) {
                    adListener.onAdShown("popup");
                }
            }
            
            @Override
            public void windowClosing(WindowEvent e) {
                if (adListener != null) {
                    adListener.onAdClosed("popup");
                }
            }
        });
    }
    
    private void onAdClicked() {
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
        if (adListener != null) {
            adListener.onPremiumUpgradeClicked();
        }
        dispose();
    }
    
    private void closeAd() {
        if (adListener != null) {
            adListener.onAdClosed("popup");
        }
        dispose();
    }
    
    public void setAdListener(AdListener listener) {
        this.adListener = listener;
    }
}