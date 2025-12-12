package componentes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Barra superior estilo "login".
 * - Minimizar
 * - Cerrar (acción configurable)
 * - Permite mover la ventana arrastrando la barra
 */
public class LoginTitleBar extends JPanel {

    private final JFrame frame;
    private int xmouse;
    private int ymouse;

    private final JPanel closeBtn;
    private final JLabel closeTxt;

    private final JPanel minimizeBtn;
    private final JLabel minimizeTxt;

    public LoginTitleBar(JFrame frame, String title, Runnable onClose) {
        this.frame = frame;

        setLayout(new BorderLayout());
        setBackground(new Color(30, 56, 136));
        setPreferredSize(new Dimension(10, 40));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setForeground(new Color(250, 250, 250));
        titleLbl.setFont(new Font("Epunda Slab Light", Font.BOLD, 24));
        titleLbl.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 0, 0));

        JPanel right = new JPanel();
        right.setOpaque(false);
        right.setLayout(new BorderLayout());

        // Botón cerrar
        closeBtn = new JPanel(new BorderLayout());
        closeBtn.setBackground(new Color(30, 56, 136));
        closeBtn.setPreferredSize(new Dimension(60, 40));
        closeTxt = new JLabel();
        closeTxt.setHorizontalAlignment(SwingConstants.CENTER);
        closeTxt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/cerrar.png")));
        closeTxt.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeBtn.add(closeTxt, BorderLayout.CENTER);

        // Botón minimizar
        minimizeBtn = new JPanel(new BorderLayout());
        minimizeBtn.setBackground(new Color(30, 56, 136));
        minimizeBtn.setPreferredSize(new Dimension(60, 40));
        minimizeTxt = new JLabel("-");
        minimizeTxt.setHorizontalAlignment(SwingConstants.CENTER);
        minimizeTxt.setForeground(new Color(250, 250, 250));
        minimizeTxt.setFont(new Font("Dialog", Font.BOLD, 22));
        minimizeTxt.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        minimizeBtn.add(minimizeTxt, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        buttons.setOpaque(false);
        buttons.setLayout(new BorderLayout());
        buttons.add(minimizeBtn, BorderLayout.WEST);
        buttons.add(closeBtn, BorderLayout.EAST);

        right.add(buttons, BorderLayout.EAST);

        add(titleLbl, BorderLayout.WEST);
        add(right, BorderLayout.EAST);

        // Arrastrar ventana
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                xmouse = evt.getX();
                ymouse = evt.getY();
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent evt) {
                int x = evt.getXOnScreen();
                int y = evt.getYOnScreen();
                frame.setLocation(x - xmouse, y - ymouse);
            }
        });

        // Hover/click minimizar
        minimizeTxt.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                frame.setState(JFrame.ICONIFIED);
            }

            @Override
            public void mouseEntered(MouseEvent evt) {
                minimizeBtn.setBackground(Color.decode("#2e4ca9"));
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                minimizeBtn.setBackground(new Color(30, 56, 136));
            }
        });

        // Hover/click cerrar
        closeTxt.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (onClose != null) {
                    onClose.run();
                } else {
                    frame.dispose();
                }
            }

            @Override
            public void mouseEntered(MouseEvent evt) {
                closeBtn.setBackground(Color.red);
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                closeBtn.setBackground(new Color(30, 56, 136));
            }
        });
    }
}
