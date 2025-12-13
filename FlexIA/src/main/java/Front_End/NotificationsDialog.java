package Front_End;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

import java.awt.Cursor;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

/**
 * Ventana simple de notificaciones.
 */
public class NotificationsDialog extends JDialog {

    private final DefaultListModel<String> model = new DefaultListModel<>();

    public NotificationsDialog(java.awt.Frame owner, List<String> notifications) {
        super(owner, "", true);
        setPreferredSize(new Dimension(420, 520));
        setMinimumSize(new Dimension(360, 420));
        setResizable(false);
        setUndecorated(true);


        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(250, 250, 250));
        root.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(30, 56, 136), 2),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));

        // Panel de encabezado con color
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(30, 56, 136));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel title = new JLabel("Notificaciones", SwingConstants.CENTER);
        title.setFont(new Font("Epunda Slab ExtraBold", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        headerPanel.add(title, BorderLayout.CENTER);
        
        // Botón X para cerrar
        JLabel closeButton = new JLabel("X");
        closeButton.setFont(new Font("arial", Font.BOLD, 20));
        closeButton.setForeground(Color.WHITE);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dispose();
            }
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                closeButton.setForeground(new Color(200, 200, 200));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                closeButton.setForeground(Color.WHITE);
            }
        });
        headerPanel.add(closeButton, BorderLayout.EAST);
        
        root.add(headerPanel, BorderLayout.NORTH);

        if (notifications != null) {
            for (String n : notifications) {
                model.addElement(n);
            }
        }

        JList<String> list = new JList<>(model);
        list.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        list.setForeground(new Color(33, 33, 33));
        list.setBackground(new Color(250, 250, 250));
        

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        root.add(scroll, BorderLayout.CENTER);

        setContentPane(root);
        pack();
        setLocationRelativeTo(owner);
    }
}
