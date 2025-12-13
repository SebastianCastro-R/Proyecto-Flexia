package Front_End;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import Back_End.FuenteUtil;
import componentes.RoundedPanel;

public class ResultadosEncuestaDiagnostica extends javax.swing.JFrame {

    // Para mover la ventana (barra superior)
    private int xmouse, ymouse;

    private final EncuestaDiagnosticaResultado resultado;

    private JLabel lblCountdown;
    private Timer timer;

    public ResultadosEncuestaDiagnostica(EncuestaDiagnosticaResultado resultado) {
        this.resultado = resultado;

        initUI();
        setLocationRelativeTo(null);
        iniciarRedireccionAutomatica(6);
    }

    private void initUI() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        setSize(1440, 1024);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(250, 250, 250));

        // ===== Header (barra superior) =====
        JPanel header = new JPanel();
        header.setBackground(new Color(30, 56, 136));
        header.setPreferredSize(new Dimension(1440, 40));
        header.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        header.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                xmouse = evt.getX();
                ymouse = evt.getY();
            }
        });
        header.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                int x = evt.getXOnScreen();
                int y = evt.getYOnScreen();
                setLocation(x - xmouse, y - ymouse);
            }
        });

        JLabel title = new JLabel("RESULTADOS");
        title.setForeground(Color.WHITE);
        title.setFont(FuenteUtil.cargarFuente("EpundaSlab-EXtrabold.ttf", 24f));
        header.add(title, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, -1, 40));

        // Botón minimizar
        JPanel minimizebtn = new JPanel(new BorderLayout());
        minimizebtn.setBackground(new Color(30, 56, 136));
        JLabel minimizetxt = new JLabel("-", SwingConstants.CENTER);
        minimizetxt.setFont(FuenteUtil.cargarFuente("EpundaSlab-EXtrabold.ttf", 30f));
        minimizetxt.setForeground(new Color(250, 250, 250));
        minimizetxt.setCursor(new Cursor(Cursor.HAND_CURSOR));
        minimizetxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                setState(JFrame.ICONIFIED);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                minimizebtn.setBackground(Color.decode("#2e4ca9"));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                minimizebtn.setBackground(new Color(30, 56, 136));
            }
        });
        minimizebtn.add(minimizetxt, BorderLayout.CENTER);
        header.add(minimizebtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(1312, 0, 60, 40));

        // Botón cerrar
        JPanel closebtn = new JPanel(new BorderLayout());
        closebtn.setBackground(new Color(30, 56, 136));
        JLabel closetxt = new JLabel();
        closetxt.setHorizontalAlignment(SwingConstants.CENTER);
        closetxt.setIcon(new ImageIcon(getClass().getResource("/icons/cerrar.png")));
        closetxt.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closetxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // Si el usuario cierra, igual lo llevamos a Home (flujo esperado)
                irAHome();
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                closebtn.setBackground(Color.red);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                closebtn.setBackground(new Color(30, 56, 136));
            }
        });
        closebtn.add(closetxt, BorderLayout.CENTER);
        header.add(closebtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(1380, 0, 60, 40));

        root.add(header, BorderLayout.NORTH);

        // ===== Contenido =====
        JPanel content = new JPanel();
        content.setBackground(new Color(250, 250, 250));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(40, 70, 30, 70));

        JLabel h1 = new JLabel("Resultados de tu encuesta");
        h1.setFont(FuenteUtil.cargarFuente("EpundaSlab-EXtrabold.ttf", 36f));
        h1.setForeground(new Color(30, 56, 136));
        h1.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel("<html><body style='width:900px;'>Resumen rápido y claro según tus respuestas.</body></html>");
        sub.setFont(FuenteUtil.cargarFuente("EpundaSlab-Regular.ttf", 18f));
        sub.setForeground(new Color(120, 120, 120));
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(h1);
        content.add(Box.createVerticalStrut(8));
        content.add(sub);
        content.add(Box.createVerticalStrut(25));

        RoundedPanel container = new RoundedPanel();
        container.setArc(25);
        container.setBackground(new Color(229, 229, 234));
        container.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
        container.setAlignmentX(Component.LEFT_ALIGNMENT);
        container.setPreferredSize(new Dimension(1300, 520));

        container.add(crearCard("Nivel de riesgo", resultado.getRiesgo(), colorPorRiesgo(resultado.getRiesgo())));
        container.add(crearCard("Dolor reportado", resultado.getNivelDolor() + " / 10", new Color(30, 56, 136)));
        container.add(crearCard("Horas frente al computador", safe(resultado.getHorasComputadorTexto()), new Color(30, 56, 136)));

        container.add(crearListaCard("Factores clave", resultado.getFactoresClave()));
        container.add(crearListaCard("Recomendaciones", resultado.getRecomendaciones()));

        content.add(container);
        content.add(Box.createVerticalStrut(20));

        // Mascota (decorativo, opcional)
        try {
            JLabel carpianin = new JLabel(new ImageIcon(getClass().getResource("/Images/Carpianin.png")));
            carpianin.setAlignmentX(Component.LEFT_ALIGNMENT);
            content.add(carpianin);
        } catch (Exception ignored) {
            // si no existe, no pasa nada
        }

        root.add(content, BorderLayout.CENTER);

        // ===== Footer / redirección =====
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(250, 250, 250));
        footer.setBorder(BorderFactory.createEmptyBorder(0, 70, 25, 70));

        lblCountdown = new JLabel("Redirigiendo a Home...");
        lblCountdown.setFont(FuenteUtil.cargarFuente("EpundaSlab-Medium.ttf", 18f));
        lblCountdown.setForeground(new Color(30, 56, 136));
        footer.add(lblCountdown, BorderLayout.WEST);

        root.add(footer, BorderLayout.SOUTH);

        setContentPane(root);
    }

    private RoundedPanel crearCard(String titulo, String valor, Color colorValor) {
        RoundedPanel card = new RoundedPanel();
        card.setArc(20);
        card.setBackground(new Color(203, 230, 255));
        card.setPreferredSize(new Dimension(400, 140));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JLabel t = new JLabel(titulo);
        t.setFont(FuenteUtil.cargarFuente("EpundaSlab-EXtrabold.ttf", 18f));
        t.setForeground(new Color(30, 56, 136));

        JLabel v = new JLabel(valor);
        v.setFont(FuenteUtil.cargarFuente("EpundaSlab-EXtrabold.ttf", 30f));
        v.setForeground(colorValor);

        card.add(t);
        card.add(Box.createVerticalStrut(12));
        card.add(v);

        return card;
    }

    private RoundedPanel crearListaCard(String titulo, java.util.List<String> items) {
        RoundedPanel card = new RoundedPanel();
        card.setArc(20);
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(610, 260));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 2),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));

        JLabel t = new JLabel(titulo);
        t.setFont(FuenteUtil.cargarFuente("EpundaSlab-EXtrabold.ttf", 18f));
        t.setForeground(new Color(30, 56, 136));
        t.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(t);
        card.add(Box.createVerticalStrut(10));

        if (items == null || items.isEmpty()) {
            JLabel empty = new JLabel("No hay datos suficientes.");
            empty.setFont(FuenteUtil.cargarFuente("EpundaSlab-Regular.ttf", 16f));
            empty.setForeground(new Color(120, 120, 120));
            empty.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.add(empty);
            return card;
        }

        StringBuilder html = new StringBuilder("<html><body style='width:520px;'>");
        html.append("<ul style='margin-top:6px;'>");
        for (String it : items) {
            html.append("<li>").append(escapeHtml(it)).append("</li>");
        }
        html.append("</ul></body></html>");

        JLabel list = new JLabel(html.toString());
        list.setFont(FuenteUtil.cargarFuente("EpundaSlab-Regular.ttf", 16f));
        list.setForeground(new Color(30, 56, 136));
        list.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(list);
        return card;
    }

    private void iniciarRedireccionAutomatica(int seconds) {
        final int[] remaining = new int[]{seconds};
        actualizarCountdown(remaining[0]);

        timer = new Timer(1000, e -> {
            remaining[0]--;
            if (remaining[0] <= 0) {
                ((Timer) e.getSource()).stop();
                irAHome();
            } else {
                actualizarCountdown(remaining[0]);
            }
        });
        timer.start();
    }

    private void actualizarCountdown(int seconds) {
        lblCountdown.setText("Redirigiendo a Home en " + seconds + "s...");
    }

    private void irAHome() {
        if (timer != null) {
            timer.stop();
        }
        Home home = new Home();
        home.setVisible(true);
        home.setLocationRelativeTo(null);
        dispose();
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }

    private static Color colorPorRiesgo(String riesgo) {
        if (riesgo == null) return new Color(30, 56, 136);
        switch (riesgo.toUpperCase()) {
            case "ALTO":
                return new Color(194, 24, 7); // rojo
            case "MEDIO":
                return new Color(245, 124, 0); // naranja
            case "BAJO":
                return new Color(56, 142, 60); // verde
            default:
                return new Color(30, 56, 136);
        }
    }

    private static String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}
