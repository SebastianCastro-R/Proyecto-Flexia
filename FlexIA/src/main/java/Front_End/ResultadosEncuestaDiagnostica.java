package Front_End;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import Back_End.FuenteUtil;
import componentes.RoundedPanel;

public class ResultadosEncuestaDiagnostica extends javax.swing.JFrame {

    // Para mover la ventana (barra superior)
    private int xmouse, ymouse;

    private final EncuestaDiagnosticaResultado resultado;

    public ResultadosEncuestaDiagnostica(EncuestaDiagnosticaResultado resultado) {
        this.resultado = resultado;

        initUI();
        setLocationRelativeTo(null);
    }

    private void initUI() {
        // No queremos que se cierre la app; si el usuario intenta cerrar, volvemos a Home.
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                irAHome();
            }
        });

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

        String nombre = safe(resultado.getNombre()).trim();
        JLabel h1 = new JLabel(nombre.isEmpty() ? "Resultados de tu encuesta" : ("Resultados de " + nombre));
        h1.setFont(FuenteUtil.cargarFuente("EpundaSlab-EXtrabold.ttf", 36f));
        h1.setForeground(new Color(30, 56, 136));
        h1.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel("<html><body style='width:900px;'>Este resumen se basa en tus respuestas. Puedes cerrar esta pantalla cuando quieras y volverás a Home.</body></html>");
        sub.setFont(FuenteUtil.cargarFuente("EpundaSlab-Regular.ttf", 18f));
        sub.setForeground(new Color(120, 120, 120));
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(h1);
        content.add(Box.createVerticalStrut(8));
        content.add(sub);
        content.add(Box.createVerticalStrut(25));

        // Superficie principal (mejor jerarquía visual)
        RoundedPanel surface = new RoundedPanel();
        surface.setArc(25);
        surface.setBackground(Color.WHITE);
        surface.setLayout(new BoxLayout(surface, BoxLayout.Y_AXIS));
        surface.setAlignmentX(Component.LEFT_ALIGNMENT);
        surface.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 2),
                BorderFactory.createEmptyBorder(22, 22, 22, 22)
        ));
        surface.setPreferredSize(new Dimension(1300, 560));

        // Fila superior: métricas
        JPanel rowTop = new JPanel(new GridLayout(1, 3, 18, 0));
        rowTop.setOpaque(false);
        rowTop.setAlignmentX(Component.LEFT_ALIGNMENT);

        rowTop.add(crearRiesgoCard(resultado.getRiesgo()));
        rowTop.add(crearDolorCard(resultado.getNivelDolor()));
        rowTop.add(crearCard("Horas frente al computador", safe(resultado.getHorasComputadorTexto()), new Color(30, 56, 136)));

        // Fila inferior: listas
        JPanel rowBottom = new JPanel(new GridLayout(1, 2, 18, 0));
        rowBottom.setOpaque(false);
        rowBottom.setAlignmentX(Component.LEFT_ALIGNMENT);

        rowBottom.add(crearListaCard("Factores clave", resultado.getFactoresClave()));
        rowBottom.add(crearListaCard("Recomendaciones", resultado.getRecomendaciones()));

        surface.add(rowTop);
        surface.add(Box.createVerticalStrut(18));
        surface.add(rowBottom);

        content.add(surface);
        content.add(Box.createVerticalStrut(18));

        // Mascota (decorativo, opcional)
        try {
            JLabel carpianin = new JLabel(new ImageIcon(getClass().getResource("/Images/Carpianin.png")));
            carpianin.setAlignmentX(Component.LEFT_ALIGNMENT);
            content.add(carpianin);
        } catch (Exception ignored) {
            // si no existe, no pasa nada
        }

        root.add(content, BorderLayout.CENTER);

        // ===== Footer: cierre manual =====
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(250, 250, 250));
        footer.setBorder(BorderFactory.createEmptyBorder(0, 70, 25, 70));

        JLabel hint = new JLabel("Cuando termines, presiona \"Volver a Home\".");
        hint.setFont(FuenteUtil.cargarFuente("EpundaSlab-Medium.ttf", 18f));
        hint.setForeground(new Color(30, 56, 136));
        footer.add(hint, BorderLayout.WEST);

        JButton btnHome = new JButton("Volver a Home");
        btnHome.setFont(FuenteUtil.cargarFuente("EpundaSlab-EXtrabold.ttf", 18f));
        btnHome.setBackground(new Color(30, 56, 136));
        btnHome.setForeground(Color.WHITE);
        btnHome.setBorder(BorderFactory.createEmptyBorder(12, 22, 12, 22));
        btnHome.setFocusPainted(false);
        btnHome.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHome.addActionListener(e -> irAHome());
        footer.add(btnHome, BorderLayout.EAST);

        root.add(footer, BorderLayout.SOUTH);

        // Atajo: ESC vuelve a Home
        getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "volverHome");
        getRootPane().getActionMap().put("volverHome", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                irAHome();
            }
        });

        setContentPane(root);
    }

    private RoundedPanel crearCard(String titulo, String valor, Color colorValor) {
        RoundedPanel card = new RoundedPanel();
        card.setArc(20);
        card.setBackground(new Color(203, 230, 255));
        card.setPreferredSize(new Dimension(400, 150));
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

    private RoundedPanel crearRiesgoCard(String riesgo) {
        Color color = colorPorRiesgo(riesgo);
        String txt = safe(riesgo);

        RoundedPanel card = new RoundedPanel();
        card.setArc(20);
        card.setBackground(new Color(203, 230, 255));
        card.setPreferredSize(new Dimension(400, 150));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JLabel t = new JLabel("Nivel de riesgo");
        t.setFont(FuenteUtil.cargarFuente("EpundaSlab-EXtrabold.ttf", 18f));
        t.setForeground(new Color(30, 56, 136));

        // Badge
        RoundedPanel badge = new RoundedPanel();
        badge.setArc(18);
        badge.setBackground(color);
        badge.setLayout(new BorderLayout());
        badge.setMaximumSize(new Dimension(220, 44));
        badge.setPreferredSize(new Dimension(220, 44));

        JLabel v = new JLabel(txt.isEmpty() ? "N/D" : txt.toUpperCase(), SwingConstants.CENTER);
        v.setFont(FuenteUtil.cargarFuente("EpundaSlab-EXtrabold.ttf", 20f));
        v.setForeground(Color.WHITE);
        badge.add(v, BorderLayout.CENTER);

        card.add(t);
        card.add(Box.createVerticalStrut(14));
        card.add(badge);
        card.add(Box.createVerticalStrut(10));

        JLabel hint = new JLabel("Recuerda tomar pausas activas.");
        hint.setFont(FuenteUtil.cargarFuente("EpundaSlab-Regular.ttf", 14f));
        hint.setForeground(new Color(30, 56, 136));
        card.add(hint);

        return card;
    }

    private RoundedPanel crearDolorCard(int dolor) {
        RoundedPanel card = new RoundedPanel();
        card.setArc(20);
        card.setBackground(new Color(203, 230, 255));
        card.setPreferredSize(new Dimension(400, 150));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JLabel t = new JLabel("Dolor reportado");
        t.setFont(FuenteUtil.cargarFuente("EpundaSlab-EXtrabold.ttf", 18f));
        t.setForeground(new Color(30, 56, 136));

        JLabel v = new JLabel(Math.max(0, dolor) + " / 10");
        v.setFont(FuenteUtil.cargarFuente("EpundaSlab-EXtrabold.ttf", 30f));
        v.setForeground(new Color(30, 56, 136));

        JProgressBar bar = new JProgressBar(0, 10);
        bar.setValue(Math.max(0, Math.min(10, dolor)));
        bar.setStringPainted(false);
        bar.setForeground(new Color(30, 56, 136));
        bar.setBackground(Color.WHITE);
        bar.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 14));

        card.add(t);
        card.add(Box.createVerticalStrut(12));
        card.add(v);
        card.add(Box.createVerticalStrut(10));
        card.add(bar);

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

    private void irAHome() {
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
