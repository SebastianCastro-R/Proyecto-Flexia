package Back_End;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import Database.RachaDAO;

public class CalendarioRacha extends JDialog {
    private int añoActual;
    private int mesActual;
    private List<LocalDate> fechasConActividad;
    private JLabel lblMesAño;
    private JPanel panelCalendario;
    private int idUsuario;

    public CalendarioRacha(JFrame parent, int idUsuario) {
        super(parent, "Calendario de Actividad", true);
        this.idUsuario = idUsuario;
        this.fechasConActividad = new RachaDAO().obtenerFechasConActividad(idUsuario);
        this.añoActual = LocalDate.now().getYear();
        this.mesActual = LocalDate.now().getMonthValue();
        
        inicializarComponentes();
        construirCalendario();
        
        setSize(500, 450);
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        // Panel superior - Navegación
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(Color.WHITE);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Botón mes anterior
        JButton btnAnterior = new JButton("◀");
        btnAnterior.setFont(new Font("Arial", Font.BOLD, 16));
        btnAnterior.setBackground(new Color(30, 56, 136));
        btnAnterior.setForeground(Color.WHITE);
        btnAnterior.setFocusPainted(false);
        btnAnterior.setPreferredSize(new Dimension(40, 30));
        btnAnterior.addActionListener(e -> cambiarMes(-1));

        // Label mes y año
        lblMesAño = new JLabel("", SwingConstants.CENTER);
        lblMesAño.setFont(FuenteUtil.cargarFuente("EpundaSlab-EXtrabold.ttf", 18f));
        lblMesAño.setForeground(new Color(30, 56, 136));

        // Botón mes siguiente
        JButton btnSiguiente = new JButton("▶");
        btnSiguiente.setFont(new Font("Arial", Font.BOLD, 16));
        btnSiguiente.setBackground(new Color(30, 56, 136));
        btnSiguiente.setForeground(Color.WHITE);
        btnSiguiente.setFocusPainted(false);
        btnSiguiente.setPreferredSize(new Dimension(40, 30));
        btnSiguiente.addActionListener(e -> cambiarMes(1));

        panelSuperior.add(btnAnterior, BorderLayout.WEST);
        panelSuperior.add(lblMesAño, BorderLayout.CENTER);
        panelSuperior.add(btnSiguiente, BorderLayout.EAST);

        // Panel de días de la semana
        JPanel panelDiasSemana = new JPanel(new GridLayout(1, 7));
        panelDiasSemana.setBackground(new Color(240, 240, 240));
        panelDiasSemana.setPreferredSize(new Dimension(0, 30));
        
        String[] dias = {"Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"};
        for (String dia : dias) {
            JLabel lblDia = new JLabel(dia, SwingConstants.CENTER);
            lblDia.setFont(FuenteUtil.cargarFuente("EpundaSlab-Medium.ttf", 12f));
            lblDia.setForeground(new Color(30, 56, 136));
            panelDiasSemana.add(lblDia);
        }

        // Panel del calendario
        panelCalendario = new JPanel(new GridLayout(6, 7, 2, 2));
        panelCalendario.setBackground(Color.WHITE);
        panelCalendario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel inferior
        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(Color.WHITE);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        JLabel lblEjemplo = new JLabel("●");
        lblEjemplo.setFont(new Font("Arial", Font.BOLD, 20));
        lblEjemplo.setForeground(new Color(152, 206, 255));
        
        JLabel lblLeyenda = new JLabel(" Días con actividad");
        lblLeyenda.setFont(FuenteUtil.cargarFuente("EpundaSlab-Regular.ttf", 12f));
        lblLeyenda.setForeground(new Color(30, 56, 136));
        
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(FuenteUtil.cargarFuente("EpundaSlab-Medium.ttf", 12f));
        btnCerrar.setBackground(new Color(30, 56, 136));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);
        btnCerrar.addActionListener(e -> dispose());

        JPanel panelLeyenda = new JPanel();
        panelLeyenda.setBackground(Color.WHITE);
        panelLeyenda.add(lblEjemplo);
        panelLeyenda.add(lblLeyenda);

        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(Color.WHITE);
        panelBotones.add(btnCerrar);

        panelInferior.setLayout(new BorderLayout());
        panelInferior.add(panelLeyenda, BorderLayout.WEST);
        panelInferior.add(panelBotones, BorderLayout.EAST);

        add(panelSuperior, BorderLayout.NORTH);
        add(panelDiasSemana, BorderLayout.CENTER);
        add(panelCalendario, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void construirCalendario() {
        panelCalendario.removeAll();
        
        // Actualizar fechas con actividad cada vez que cambia el mes
        this.fechasConActividad = new RachaDAO().obtenerFechasConActividad(idUsuario);
        
        LocalDate primerDiaMes = LocalDate.of(añoActual, mesActual, 1);
        LocalDate primerDiaCalendario = primerDiaMes.with(DayOfWeek.MONDAY);
        
        DateTimeFormatter formatoMes = DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("es", "COL"));
        lblMesAño.setText(primerDiaMes.format(formatoMes).toUpperCase());
        
        LocalDate fecha = primerDiaCalendario;
        
        for (int i = 0; i < 42; i++) { // 6 semanas
            JPanel celdaDia = crearCeldaDia(fecha);
            panelCalendario.add(celdaDia);
            fecha = fecha.plusDays(1);
        }
        
        panelCalendario.revalidate();
        panelCalendario.repaint();
    }

    private JPanel crearCeldaDia(LocalDate fecha) {
        JPanel celda = new JPanel(new BorderLayout());
        celda.setPreferredSize(new Dimension(50, 40));
        
        JLabel lblDia = new JLabel(String.valueOf(fecha.getDayOfMonth()), SwingConstants.CENTER);
        lblDia.setFont(FuenteUtil.cargarFuente("EpundaSlab-Medium.ttf", 14f));
        
        // Verificar si es del mes actual
        boolean esMesActual = fecha.getMonthValue() == mesActual;
        boolean esHoy = fecha.equals(LocalDate.now());
        boolean tieneActividad = fechasConActividad.contains(fecha);
        
        if (esHoy) {
            // Hoy - resaltado especial
            celda.setBackground(new Color(255, 235, 156)); // Amarillo claro
            celda.setBorder(BorderFactory.createLineBorder(new Color(255, 193, 7), 2));
            lblDia.setForeground(Color.BLACK);
        } else if (esMesActual) {
            // Días del mes actual
            celda.setBackground(Color.WHITE);
            celda.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
            lblDia.setForeground(Color.BLACK);
        } else {
            // Días de otros meses
            celda.setBackground(new Color(245, 245, 245));
            celda.setBorder(BorderFactory.createLineBorder(new Color(240, 240, 240)));
            lblDia.setForeground(Color.GRAY);
        }
        
        // Marcar días con actividad
        if (tieneActividad) {
            JLabel lblPunto = new JLabel("●", SwingConstants.CENTER);
            lblPunto.setFont(new Font("Arial", Font.BOLD, 16));
            lblPunto.setForeground(new Color(152, 206, 255)); // Azul claro
            celda.add(lblPunto, BorderLayout.SOUTH);
        }
        
        celda.add(lblDia, BorderLayout.CENTER);
        return celda;
    }

    private void cambiarMes(int direccion) {
        mesActual += direccion;
        
        if (mesActual > 12) {
            mesActual = 1;
            añoActual++;
        } else if (mesActual < 1) {
            mesActual = 12;
            añoActual--;
        }
        
        construirCalendario();
    }
}
