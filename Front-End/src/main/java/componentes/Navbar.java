package componentes;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Navbar extends JPanel {

    private int xx, xy; // Variables para mover la ventana
    private JFrame parent; // Referencia al JFrame principal
    private JLabel lblTitulo;
    private JButton btnMinimizar;
    private JButton btnCerrar;

    public Navbar(JFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(new Color(247, 247, 247));
        setPreferredSize(new Dimension(parent.getWidth(), 40));
        setBorder(new EmptyBorder(0, 15, 0, 15));

        // Panel izquierdo con título
        JPanel panelIzquierdo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelIzquierdo.setOpaque(false);
        lblTitulo = new JLabel("Flexia");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(65, 65, 65));
        panelIzquierdo.add(lblTitulo);

        // Panel derecho con botones
        JPanel panelDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        panelDerecho.setOpaque(false);

        btnMinimizar = crearBoton("—");
        btnCerrar = crearBoton("×");

        // Acción de minimizar
        btnMinimizar.addActionListener(e -> parent.setState(JFrame.ICONIFIED));

        // Acción de cerrar
        btnCerrar.addActionListener(e -> System.exit(0));

        panelDerecho.add(btnMinimizar);
        panelDerecho.add(btnCerrar);

        add(panelIzquierdo, BorderLayout.WEST);
        add(panelDerecho, BorderLayout.EAST);

        // Habilitar movimiento de la ventana
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                xx = e.getX();
                xy = e.getY();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getXOnScreen();
                int y = e.getYOnScreen();
                parent.setLocation(x - xx, y - xy);
            }
        });
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        boton.setForeground(new Color(90, 90, 90));
        boton.setPreferredSize(new Dimension(40, 30));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setForeground(new Color(50, 50, 50));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setForeground(new Color(90, 90, 90));
            }
        });

        return boton;
    }
}
