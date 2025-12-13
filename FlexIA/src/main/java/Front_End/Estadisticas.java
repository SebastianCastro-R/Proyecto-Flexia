package Front_End;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import Back_End.SesionUsuario;
import Back_End.Usuario;
import Database.DolorDAO;
import Database.MetricasEjercicioDAO;
import Database.ProgresoDAO;
import Database.RachaDAO;
import Database.UsuariosDAO;
import componentes.BarChartPanel;
import componentes.PieChartPanel;
import componentes.RoundedPanel;

public class Estadisticas extends JFrame {

    private Menu menuPanel;
    private boolean menuVisible = false;
    private int menuWidth = 370;
    private int menuX = -menuWidth;
    private int xmouse, ymouse;

    private JLabel lblDiasActividadValor;
    private JLabel lblPrecisionValor;
    private JLabel lblDolorValor;
    private JLabel lblEjerciciosValor;

    // Gráficos
    private BarChartPanel chartProgreso;
    private BarChartPanel chartDolor;
    private PieChartPanel chartPrecision;

    public Estadisticas() {
        setUndecorated(true);
        setSize(1440, 1024);
        setLocationRelativeTo(null);
        setResizable(false);

        initUI();
        cargarEstadisticas();
    }

    private void initUI() {
        JPanel root = new JPanel(null);
        root.setBackground(new Color(250, 250, 250));
        setContentPane(root);

        // ===== HEADER =====
        JPanel header = new JPanel(null);
        header.setBackground(new Color(30, 56, 136));
        header.setBounds(0, 0, 1440, 40);

        // Drag window
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

        JLabel menuLabel = new JLabel(new ImageIcon(getClass().getResource("/icons/menu.png")));
        menuLabel.setBounds(10, 5, 30, 30);
        menuLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        menuLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                toggleMenu();
            }
        });
        header.add(menuLabel);

        JLabel title = new JLabel("ESTADÍSTICAS");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Epunda Slab ExtraBold", Font.PLAIN, 24));
        title.setBounds(60, 0, 300, 40);
        header.add(title);

        // Minimize
        JPanel minimizebtn = new JPanel(null);
        minimizebtn.setBackground(new Color(30, 56, 136));
        minimizebtn.setBounds(1320, 0, 60, 40);
        JLabel minimizetxt = new JLabel("-", SwingConstants.CENTER);
        minimizetxt.setForeground(Color.WHITE);
        minimizetxt.setFont(new Font("Arial", Font.BOLD, 24));
        minimizetxt.setBounds(0, 0, 60, 40);
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
        minimizebtn.add(minimizetxt);
        header.add(minimizebtn);

        // Close
        JPanel closebtn = new JPanel(null);
        closebtn.setBackground(new Color(30, 56, 136));
        closebtn.setBounds(1380, 0, 60, 40);
        JLabel closetxt = new JLabel(new ImageIcon(getClass().getResource("/icons/cerrar.png")),
                SwingConstants.CENTER);
        closetxt.setBounds(0, 0, 60, 40);
        closetxt.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closetxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                System.exit(0);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                closebtn.setBackground(Color.RED);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                closebtn.setBackground(new Color(30, 56, 136));
            }
        });
        closebtn.add(closetxt);
        header.add(closebtn);

        root.add(header);

        // ===== CONTENIDO =====
        JLabel subtitulo = new JLabel("Resumen de tu progreso", SwingConstants.CENTER);
        subtitulo.setFont(new Font("Epunda Slab Medium", Font.PLAIN, 28));
        subtitulo.setForeground(new Color(30, 56, 136));
        subtitulo.setBounds(0, 90, 1440, 40);
        root.add(subtitulo);

        JPanel grid = new JPanel(new GridLayout(2, 2, 25, 25));
        grid.setBackground(new Color(250, 250, 250));
        grid.setBounds(220, 200, 1000, 520);

        grid.add(crearTarjeta("Días con actividad", (lbl) -> lblDiasActividadValor = lbl));
        grid.add(crearTarjeta("Tasa de precisión", (lbl) -> lblPrecisionValor = lbl));
        grid.add(crearTarjeta("Nivel de dolor", (lbl) -> lblDolorValor = lbl));
        grid.add(crearTarjeta("Ejercicios completados", (lbl) -> lblEjerciciosValor = lbl));

        root.add(grid);

        // ===== GRÁFICOS (debajo de los datos numéricos) =====
        JPanel chartsRow = new JPanel(new GridLayout(1, 3, 25, 0));
        chartsRow.setBackground(new Color(250, 250, 250));
        chartsRow.setBounds(120, 760, 1200, 220);

        chartsRow.add(crearTarjetaGraficoBarras("Progreso (últimos 7 días)", (c) -> chartProgreso = c, new Color(152, 206, 255)));
        chartsRow.add(crearTarjetaGraficoBarras("Dolor (últimos 7 días)", (c) -> chartDolor = c, new Color(255, 152, 0)));
        chartsRow.add(crearTarjetaGraficoTorta("Precisión", (c) -> chartPrecision = c));

        root.add(chartsRow);

        // ===== MENÚ LATERAL =====
        menuPanel = new Menu("Estadisticas");
        menuPanel.setBounds(menuX, 0, menuWidth, getHeight());
        menuPanel.setVisible(true);
        menuPanel.setOpaque(true);
        menuPanel.setBackground(new Color(250, 250, 250));
        menuPanel.setOnCloseCallback(() -> toggleMenu());
        root.add(menuPanel);
        root.setComponentZOrder(menuPanel, 0);
    }

    private interface LabelReceiver {
        void accept(JLabel lbl);
    }

    private JPanel crearTarjeta(String titulo, LabelReceiver receiver) {
        RoundedPanel card = new RoundedPanel();
        card.setBackground(Color.WHITE);
        card.setLayout(null);
        card.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 250), 2, true));

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Epunda Slab ExtraBold", Font.PLAIN, 22));
        lblTitulo.setForeground(new Color(30, 56, 136));
        lblTitulo.setBounds(0, 30, 470, 30);

        JLabel lblValor = new JLabel("—", SwingConstants.CENTER);
        lblValor.setFont(new Font("Epunda Slab ExtraBold", Font.PLAIN, 48));
        lblValor.setForeground(new Color(152, 206, 255));
        lblValor.setBounds(0, 85, 470, 70);

        JLabel lblDetalle = new JLabel(" ", SwingConstants.CENTER);
        lblDetalle.setFont(new Font("Epunda Slab Medium", Font.PLAIN, 16));
        lblDetalle.setForeground(new Color(30, 56, 136));
        lblDetalle.setBounds(0, 170, 470, 30);
        lblDetalle.setName("detalle");

        card.add(lblTitulo);
        card.add(lblValor);
        card.add(lblDetalle);

        receiver.accept(lblValor);
        return card;
    }

    private interface ChartReceiver<T> {
        void accept(T chart);
    }

    private JPanel crearTarjetaGraficoBarras(String titulo, ChartReceiver<BarChartPanel> receiver, Color barColor) {
        RoundedPanel card = new RoundedPanel();
        card.setBackground(Color.WHITE);
        card.setLayout(new java.awt.BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 250), 2, true));

        BarChartPanel chart = new BarChartPanel();
        chart.setTitle(titulo);
        chart.setBarColor(barColor);
        chart.setPreferredSize(new Dimension(1, 1));

        card.add(chart, java.awt.BorderLayout.CENTER);
        receiver.accept(chart);
        return card;
    }

    private JPanel crearTarjetaGraficoTorta(String titulo, ChartReceiver<PieChartPanel> receiver) {
        RoundedPanel card = new RoundedPanel();
        card.setBackground(Color.WHITE);
        card.setLayout(new java.awt.BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 250), 2, true));

        PieChartPanel chart = new PieChartPanel();
        chart.setTitle(titulo);
        chart.setPreferredSize(new Dimension(1, 1));

        card.add(chart, java.awt.BorderLayout.CENTER);
        receiver.accept(chart);
        return card;
    }

    private void setDetalleEnTarjeta(JLabel lblValor, String detalle) {
        JPanel card = (JPanel) lblValor.getParent();
        for (java.awt.Component c : card.getComponents()) {
            if (c instanceof JLabel && "detalle".equals(c.getName())) {
                ((JLabel) c).setText(detalle);
                break;
            }
        }
    }

    private int getIdUsuarioSesion() {
        try {
            SesionUsuario sesion = SesionUsuario.getInstancia();
            Usuario u = sesion.getUsuarioActual();
            if (u != null && u.getIdUsuario() > 0) {
                return u.getIdUsuario();
            }

            String correo = sesion.getCorreoUsuario();
            if (correo != null && !correo.isEmpty()) {
                UsuariosDAO dao = new UsuariosDAO();
                Usuario u2 = dao.obtenerUsuarioPorCorreo(correo);
                if (u2 != null) {
                    return u2.getIdUsuario();
                }
            }
        } catch (Exception e) {
            // ignorar
        }
        return -1;
    }

    private void cargarEstadisticas() {
        int idUsuario = getIdUsuarioSesion();

        // Días de actividad
        int diasActividad = new RachaDAO().contarDiasConActividad(idUsuario);
        int rachaActual = new RachaDAO().obtenerRachaActual(idUsuario);
        lblDiasActividadValor.setText(String.valueOf(Math.max(0, diasActividad)));
        setDetalleEnTarjeta(lblDiasActividadValor, "Racha actual: " + Math.max(0, rachaActual) + " día(s)");

        // Precisión
        MetricasEjercicioDAO.PrecisionGlobal prec = MetricasEjercicioDAO.obtenerPrecisionGlobal(idUsuario);
        double precision = prec.precision();
        if (precision < 0) {
            lblPrecisionValor.setText("N/A");
            setDetalleEnTarjeta(lblPrecisionValor, "Sin datos aún");
        } else {
            int pct = (int) Math.round(precision * 100.0);
            lblPrecisionValor.setText(pct + "%");
            setDetalleEnTarjeta(lblPrecisionValor, "Eventos: " + prec.total() + " (OK: " + prec.ok + ")");
        }

        // Gráfico torta: OK vs RESET
        if (chartPrecision != null) {
            chartPrecision.setData(prec.ok, prec.reset);
        }

        // Dolor
        int ultimoDolor = DolorDAO.obtenerUltimoNivelDolor(idUsuario);
        double prom7 = DolorDAO.obtenerPromedioDolorUltimosNDias(idUsuario, 7);
        if (ultimoDolor <= 0) {
            lblDolorValor.setText("N/A");
            setDetalleEnTarjeta(lblDolorValor, "Sin registro" );
        } else {
            lblDolorValor.setText(ultimoDolor + "/5");
            if (prom7 > 0) {
                setDetalleEnTarjeta(lblDolorValor, "Prom. 7 días: " + String.format("%.1f", prom7));
            } else {
                setDetalleEnTarjeta(lblDolorValor, "Último registro" );
            }
        }

        // Gráfico dolor (últimos 7 días)
        if (chartDolor != null) {
            java.util.List<BarChartPanel.Point> pts = new java.util.ArrayList<>();
            for (Database.DolorDAO.DolorDiario d : DolorDAO.obtenerDolorUltimosNDias(idUsuario, 7)) {
                pts.add(new BarChartPanel.Point(d.fecha, d.nivel));
            }
            chartDolor.setData(pts);
        }

        // Ejercicios completados
        long totalEjercicios = ProgresoDAO.obtenerTotalEjerciciosRealizados(idUsuario);
        int videosUnicos = ProgresoDAO.contarVideosCompletados(idUsuario);
        lblEjerciciosValor.setText(String.valueOf(Math.max(0, totalEjercicios)));
        setDetalleEnTarjeta(lblEjerciciosValor, "Videos únicos: " + Math.max(0, videosUnicos));

        // Gráfico progreso (últimos 7 días)
        if (chartProgreso != null) {
            java.util.List<BarChartPanel.Point> pts = new java.util.ArrayList<>();
            for (Database.ProgresoDAO.ProgresoDiario p : ProgresoDAO.obtenerProgresoUltimosNDias(idUsuario, 7)) {
                pts.add(new BarChartPanel.Point(p.fecha, p.totalEjercicios));
            }
            chartProgreso.setData(pts);
        }
    }

    private void toggleMenu() {
        if (menuVisible) {
            Timer slideOut = new Timer(2, e -> {
                if (menuX > -menuWidth) {
                    menuX -= 10;
                    menuPanel.setLocation(menuX, 0);
                } else {
                    ((Timer) e.getSource()).stop();
                    menuVisible = false;
                }
            });
            slideOut.start();
        } else {
            Timer slideIn = new Timer(2, e -> {
                if (menuX < 0) {
                    menuX += 10;
                    menuPanel.setLocation(menuX, 0);
                } else {
                    ((Timer) e.getSource()).stop();
                    menuVisible = true;
                }
            });
            slideIn.start();
        }
    }
}
