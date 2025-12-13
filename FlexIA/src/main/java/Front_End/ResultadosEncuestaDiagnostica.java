package Front_End;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

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

    // Colores modernos
    private final Color COLOR_PRIMARIO = new Color(30, 56, 136);
    private final Color COLOR_SECUNDARIO = new Color(72, 129, 255);
    private final Color COLOR_FONDO = new Color(245, 247, 250);
    private final Color COLOR_TARJETA = Color.WHITE;
    private final Color COLOR_TEXTO = new Color(51, 51, 51);
    private final Color COLOR_TEXTO_SECUNDARIO = new Color(102, 102, 102);
    private final Color COLOR_BORDE = new Color(230, 234, 240);

    public ResultadosEncuestaDiagnostica(EncuestaDiagnosticaResultado resultado) {
        this.resultado = resultado;
        initUI();
        setLocationRelativeTo(null);
    }

    private void initUI() {
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
        root.setBackground(COLOR_FONDO);

        // ===== Header (barra superior con sombra) =====
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_PRIMARIO);
        header.setPreferredSize(new Dimension(1440, 60));
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDE),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));

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

        // Título con icono
        JPanel tituloPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        tituloPanel.setBackground(COLOR_PRIMARIO);
        tituloPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
        
        // Icono decorativo
        try {
            JLabel icono = new JLabel(new ImageIcon(getClass().getResource("/icons/chart-icon.png")));
            icono.setPreferredSize(new Dimension(30, 30));
            tituloPanel.add(icono);
        } catch (Exception e) {
            // Icono opcional
        }
        
        JLabel title = new JLabel("RESULTADOS DIAGNÓSTICOS");
        title.setForeground(Color.WHITE);
        title.setFont(FuenteUtil.cargarFuente("EpundaSlab-EXtrabold.ttf", 26f));
        tituloPanel.add(title);
        
        header.add(tituloPanel, BorderLayout.WEST);

        // Controles de ventana
        JPanel controlesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        controlesPanel.setBackground(COLOR_PRIMARIO);
        controlesPanel.setPreferredSize(new Dimension(140, 60));

        // Botón minimizar
        RoundedPanel minimizebtn = new RoundedPanel();
        minimizebtn.setArc(15);
        minimizebtn.setBackground(COLOR_PRIMARIO);
        minimizebtn.setPreferredSize(new Dimension(60, 60));
        minimizebtn.setLayout(new BorderLayout());
        
        JLabel minimizetxt = new JLabel("─", SwingConstants.CENTER);
        minimizetxt.setFont(new Font("Arial", Font.BOLD, 24));
        minimizetxt.setForeground(Color.WHITE);
        minimizetxt.setCursor(new Cursor(Cursor.HAND_CURSOR));
        minimizetxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                setState(JFrame.ICONIFIED);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                minimizebtn.setBackground(new Color(72, 129, 255));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                minimizebtn.setBackground(COLOR_PRIMARIO);
            }
        });
        minimizebtn.add(minimizetxt, BorderLayout.CENTER);
        controlesPanel.add(minimizebtn);

        // Botón cerrar
        RoundedPanel closebtn = new RoundedPanel();
        closebtn.setArc(15);
        closebtn.setBackground(COLOR_PRIMARIO);
        closebtn.setPreferredSize(new Dimension(60, 60));
        closebtn.setLayout(new BorderLayout());
        
        JLabel closetxt = new JLabel("✕", SwingConstants.CENTER);
        closetxt.setFont(new Font("Arial", Font.BOLD, 20));
        closetxt.setForeground(Color.WHITE);
        closetxt.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closetxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                irAHome();
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                closebtn.setBackground(new Color(220, 53, 69));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                closebtn.setBackground(COLOR_PRIMARIO);
            }
        });
        closebtn.add(closetxt, BorderLayout.CENTER);
        controlesPanel.add(closebtn);

        header.add(controlesPanel, BorderLayout.EAST);
        root.add(header, BorderLayout.NORTH);

        // ===== Contenido Principal =====
        JPanel content = new JPanel();
        content.setBackground(COLOR_FONDO);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(40, 80, 30, 80));

        // Título principal
        String nombre = resultado.getNombre() != null ? resultado.getNombre().trim() : "";
        JLabel h1 = new JLabel(nombre.isEmpty() ? "Resultados de tu Evaluación" : ("Hola, " + nombre + "!"));
        h1.setFont(FuenteUtil.cargarFuente("EpundaSlab-EXtrabold.ttf", 42f));
        h1.setForeground(COLOR_PRIMARIO);
        h1.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Subtítulo
        JLabel sub = new JLabel("<html><body style='width:900px; text-align: left;'>" +
                "Este es tu resumen diagnóstico basado en tus respuestas. " +
                "Revisa los resultados y recomendaciones para mejorar tu bienestar.</body></html>");
        sub.setFont(FuenteUtil.cargarFuente("EpundaSlab-Regular.ttf", 20f));
        sub.setForeground(COLOR_TEXTO_SECUNDARIO);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(h1);
        content.add(Box.createVerticalStrut(12));
        content.add(sub);
        content.add(Box.createVerticalStrut(40));

        // Tarjetas de métricas principales
        JPanel metricasPanel = new JPanel(new GridBagLayout());
        metricasPanel.setOpaque(false);
        metricasPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        metricasPanel.setMaximumSize(new Dimension(1280, 220));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 20);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        // Tarjeta 1: Riesgo
        gbc.gridx = 0;
        gbc.gridy = 0;
        String riesgo = resultado.getRiesgo() != null ? resultado.getRiesgo() : "N/D";
        String riesgoMostrar = riesgo.equals("N/D") ? "N/D" : riesgo.toUpperCase();
        metricasPanel.add(crearTarjetaMetrica("Nivel de Riesgo", riesgoMostrar, 
            colorPorRiesgo(riesgo), "shield-icon.png"), gbc);
        
        // Tarjeta 2: Dolor
        gbc.gridx = 1;
        int nivelDolor = resultado.getNivelDolor();
        String textoDolor = "N/D";
        if (nivelDolor >= 0 && nivelDolor <= 10) {
            textoDolor = nivelDolor + " / 10";
        }
        metricasPanel.add(crearTarjetaMetrica("Nivel de Dolor", textoDolor, 
            COLOR_PRIMARIO, "pain-icon.png"), gbc);
        
        // Tarjeta 3: Horas
        gbc.gridx = 2;
        String horasTexto = resultado.getHorasComputadorTexto() != null ? 
            resultado.getHorasComputadorTexto().trim() : "";
        if (horasTexto.isEmpty()) horasTexto = "N/D";
        metricasPanel.add(crearTarjetaMetrica("Horas en Computadora", 
            horasTexto, COLOR_SECUNDARIO, "clock-icon.png"), gbc);
        
        // Tarjeta 4: Fecha
        gbc.gridx = 3;
        gbc.insets = new Insets(0, 0, 0, 0);
        String fechaTexto = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());
        metricasPanel.add(crearTarjetaMetrica("Fecha de Evaluación", 
            fechaTexto, new Color(106, 90, 205), "calendar-icon.png"), gbc);
        
        content.add(metricasPanel);
        content.add(Box.createVerticalStrut(40));

        // Panel de contenido detallado
        RoundedPanel detallePanel = new RoundedPanel();
        detallePanel.setArc(25);
        detallePanel.setBackground(COLOR_TARJETA);
        detallePanel.setLayout(new GridBagLayout());
        detallePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detallePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDE, 1),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.fill = GridBagConstraints.BOTH;
        gbc2.insets = new Insets(0, 0, 20, 20);
        gbc2.weightx = 0.5;
        gbc2.weighty = 1.0;
        
        // Factores clave
        gbc2.gridx = 0;
        gbc2.gridy = 0;
        java.util.List<String> factores = resultado.getFactoresClave();
        if (factores == null || factores.isEmpty()) {
            factores = java.util.Arrays.asList(
                "No se identificaron factores clave específicos.",
                "Se recomienda evaluación más detallada.",
                "Considerar consulta con especialista."
            );
        }
        detallePanel.add(crearTarjetaLista("Factores Clave Identificados", 
            factores, new Color(72, 129, 255, 30)), gbc2);
        
        // Recomendaciones
        gbc2.gridx = 1;
        gbc2.insets = new Insets(0, 0, 20, 0);
        java.util.List<String> recomendaciones = resultado.getRecomendaciones();
        if (recomendaciones == null || recomendaciones.isEmpty()) {
            recomendaciones = java.util.Arrays.asList(
                "Realizar pausas activas cada 45 minutos.",
                "Mantener una postura ergonómica adecuada.",
                "Consultar con un especialista para evaluación completa.",
                "Ajustar la altura de la silla y el monitor.",
                "Realizar ejercicios de estiramiento regularmente."
            );
        }
        detallePanel.add(crearTarjetaLista("Recomendaciones Personalizadas", 
            recomendaciones, new Color(56, 142, 60, 30)), gbc2);
        
        // Barra de progreso del dolor (solo si hay datos válidos)
        if (nivelDolor >= 0 && nivelDolor <= 10) {
            gbc2.gridx = 0;
            gbc2.gridy = 1;
            gbc2.gridwidth = 2;
            gbc2.insets = new Insets(20, 0, 0, 0);
            detallePanel.add(crearBarraDolor(nivelDolor), gbc2);
        } else {
            // Agregar espacio extra si no hay barra de dolor
            gbc2.gridx = 0;
            gbc2.gridy = 1;
            gbc2.gridwidth = 2;
            gbc2.insets = new Insets(20, 0, 0, 0);
            JPanel espacioPanel = new JPanel();
            espacioPanel.setOpaque(false);
            espacioPanel.setPreferredSize(new Dimension(10, 10));
            detallePanel.add(espacioPanel, gbc2);
        }
        
        content.add(detallePanel);
        content.add(Box.createVerticalStrut(30));

        // Mascota decorativa
        try {
            JPanel mascotaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            mascotaPanel.setOpaque(false);
            mascotaPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            JLabel carpianin = new JLabel(new ImageIcon(getClass().getResource("/Images/Carpianin.png")));
            carpianin.setPreferredSize(new Dimension(150, 150));
            
            JLabel mensajeMascota = new JLabel("<html><div style='width:300px; padding:15px; background-color:#E8F4FD; border-radius:15px; border:1px solid #B3D9FF;'>" +
                    "💡 <b>Consejo:</b> ¡Recuerda tomar descansos cada 45 minutos! Tu salud es importante 💙</div></html>");
            mensajeMascota.setFont(FuenteUtil.cargarFuente("EpundaSlab-Regular.ttf", 16f));
            mensajeMascota.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            
            mascotaPanel.add(carpianin);
            mascotaPanel.add(Box.createHorizontalStrut(20));
            mascotaPanel.add(mensajeMascota);
            
            content.add(mascotaPanel);
            content.add(Box.createVerticalStrut(20));
        } catch (Exception ignored) {
            // Si no hay imagen, continuar sin ella
        }

        root.add(content, BorderLayout.CENTER);

        // ===== Footer =====
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(COLOR_FONDO);
        footer.setBorder(BorderFactory.createEmptyBorder(20, 80, 40, 80));

        // Información adicional
        JLabel infoFooter = new JLabel("<html><div style='text-align:center; color:#666;'>" +
                "📋 <b>Nota:</b> Esta evaluación es referencial. Para un diagnóstico completo, consulta con un especialista en salud ocupacional.</div></html>");
        infoFooter.setFont(FuenteUtil.cargarFuente("EpundaSlab-Regular.ttf", 14f));
        infoFooter.setHorizontalAlignment(SwingConstants.CENTER);
        footer.add(infoFooter, BorderLayout.CENTER);

        // Botón de acción principal
        JPanel botonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botonPanel.setOpaque(false);
        botonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        RoundedPanel btnHomeContainer = new RoundedPanel();
        btnHomeContainer.setArc(25);
        btnHomeContainer.setBackground(COLOR_PRIMARIO);
        btnHomeContainer.setLayout(new BorderLayout());
        btnHomeContainer.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        JButton btnHome = new JButton("Ir al Inicio");
        btnHome.setFont(FuenteUtil.cargarFuente("EpundaSlab-EXtrabold.ttf", 18f));
        btnHome.setBackground(COLOR_PRIMARIO);
        btnHome.setForeground(Color.WHITE);
        btnHome.setBorder(BorderFactory.createEmptyBorder(15, 50, 15, 50));
        btnHome.setFocusPainted(false);
        btnHome.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHome.addActionListener(e -> irAHome());
        
        // Efecto hover para el botón
        btnHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnHome.setBackground(COLOR_SECUNDARIO);
                btnHomeContainer.setBackground(COLOR_SECUNDARIO);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnHome.setBackground(COLOR_PRIMARIO);
                btnHomeContainer.setBackground(COLOR_PRIMARIO);
            }
        });
        
        btnHomeContainer.add(btnHome, BorderLayout.CENTER);
        botonPanel.add(btnHomeContainer);
        
        footer.add(botonPanel, BorderLayout.SOUTH);
        
        root.add(footer, BorderLayout.SOUTH);

        // Atajo de teclado
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

    private RoundedPanel crearTarjetaMetrica(String titulo, String valor, Color colorValor, String iconoPath) {
        RoundedPanel tarjeta = new RoundedPanel();
        tarjeta.setArc(20);
        tarjeta.setBackground(COLOR_TARJETA);
        tarjeta.setPreferredSize(new Dimension(280, 180));
        tarjeta.setMaximumSize(new Dimension(280, 180));
        tarjeta.setLayout(new BorderLayout());
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDE, 1),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));

        // Panel principal para centrar contenido
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        // Encabezado con icono
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        headerPanel.setOpaque(false);
        headerPanel.setMaximumSize(new Dimension(230, 40));
        
        try {
            JLabel icono = new JLabel(new ImageIcon(getClass().getResource("/icons/" + iconoPath)));
            icono.setPreferredSize(new Dimension(28, 28));
            headerPanel.add(icono);
        } catch (Exception e) {
            // Icono por defecto
            JLabel iconoPlaceholder = new JLabel("•");
            iconoPlaceholder.setFont(new Font("Arial", Font.BOLD, 24));
            iconoPlaceholder.setForeground(colorValor);
            headerPanel.add(iconoPlaceholder);
        }
        
        JLabel t = new JLabel(titulo);
        t.setFont(FuenteUtil.cargarFuente("EpundaSlab-Medium.ttf", 16f));
        t.setForeground(COLOR_TEXTO_SECUNDARIO);
        t.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(t);
        
        contentPanel.add(headerPanel);
        contentPanel.add(Box.createVerticalStrut(25));

        // Valor principal - CENTRADO
        JLabel v = new JLabel(valor);
        v.setFont(FuenteUtil.cargarFuente("EpundaSlab-EXtrabold.ttf", 36f));
        v.setForeground(colorValor);
        v.setHorizontalAlignment(SwingConstants.CENTER);
        v.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        contentPanel.add(v);

        tarjeta.add(contentPanel, BorderLayout.CENTER);
        return tarjeta;
    }

    private RoundedPanel crearTarjetaLista(String titulo, java.util.List<String> items, Color colorFondo) {
        RoundedPanel tarjeta = new RoundedPanel();
        tarjeta.setArc(20);
        tarjeta.setBackground(new Color(colorFondo.getRed(), colorFondo.getGreen(), colorFondo.getBlue(), 30));
        tarjeta.setPreferredSize(new Dimension(550, 280));
        tarjeta.setMaximumSize(new Dimension(550, 280));
        tarjeta.setLayout(new BorderLayout());
        tarjeta.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Panel de contenido
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        // Título - CENTRADO
        JLabel t = new JLabel(titulo);
        t.setFont(FuenteUtil.cargarFuente("EpundaSlab-EXtrabold.ttf", 20f));
        t.setForeground(COLOR_PRIMARIO);
        t.setHorizontalAlignment(SwingConstants.CENTER);
        t.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        contentPanel.add(t);
        contentPanel.add(Box.createVerticalStrut(20));

        // Panel para la lista
        JPanel listaPanel = new JPanel();
        listaPanel.setLayout(new BoxLayout(listaPanel, BoxLayout.Y_AXIS));
        listaPanel.setOpaque(false);

        // Lista de items
        if (items == null || items.isEmpty()) {
            JLabel empty = new JLabel("No hay información disponible");
            empty.setFont(FuenteUtil.cargarFuente("EpundaSlab-Regular.ttf", 16f));
            empty.setForeground(COLOR_TEXTO_SECUNDARIO);
            empty.setHorizontalAlignment(SwingConstants.CENTER);
            empty.setAlignmentX(Component.CENTER_ALIGNMENT);
            listaPanel.add(empty);
        } else {
            for (String item : items) {
                if (item != null && !item.trim().isEmpty()) {
                    JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
                    itemPanel.setOpaque(false);
                    itemPanel.setMaximumSize(new Dimension(500, 60));
                    
                    JLabel bullet = new JLabel("•");
                    bullet.setFont(new Font("Arial", Font.BOLD, 20));
                    bullet.setForeground(COLOR_SECUNDARIO);
                    bullet.setPreferredSize(new Dimension(20, 20));
                    
                    JLabel itemLabel = new JLabel("<html><div style='width:420px; text-align:left;'>" + escapeHtml(item) + "</div></html>");
                    itemLabel.setFont(FuenteUtil.cargarFuente("EpundaSlab-Regular.ttf", 16f));
                    itemLabel.setForeground(COLOR_TEXTO);
                    
                    itemPanel.add(bullet);
                    itemPanel.add(itemLabel);
                    itemPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    
                    listaPanel.add(itemPanel);
                    listaPanel.add(Box.createVerticalStrut(8));
                }
            }
        }

        // Panel scrollable
        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(listaPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(0, 0, 0, 0));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        contentPanel.add(scrollPane);
        tarjeta.add(contentPanel, BorderLayout.CENTER);

        return tarjeta;
    }

    private RoundedPanel crearBarraDolor(int nivelDolor) {
        RoundedPanel panel = new RoundedPanel();
        panel.setArc(15);
        panel.setBackground(new Color(255, 245, 245));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel titulo = new JLabel("Escala de Dolor Reportado");
        titulo.setFont(FuenteUtil.cargarFuente("EpundaSlab-Medium.ttf", 18f));
        titulo.setForeground(new Color(194, 24, 7));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(titulo);
        panel.add(Box.createVerticalStrut(15));

        // Panel para valor y barra
        JPanel barraPanel = new JPanel();
        barraPanel.setLayout(new BoxLayout(barraPanel, BoxLayout.Y_AXIS));
        barraPanel.setOpaque(false);
        barraPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Valor numérico centrado
        JLabel valorLabel = new JLabel(nivelDolor + " / 10");
        valorLabel.setFont(FuenteUtil.cargarFuente("EpundaSlab-EXtrabold.ttf", 28f));
        valorLabel.setForeground(new Color(194, 24, 7));
        valorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        barraPanel.add(valorLabel);
        barraPanel.add(Box.createVerticalStrut(15));
        
        // Barra de progreso
        JProgressBar bar = new JProgressBar(0, 10);
        bar.setValue(nivelDolor);
        bar.setStringPainted(true);
        bar.setString(nivelDolor + "/10");
        bar.setFont(FuenteUtil.cargarFuente("EpundaSlab-Medium.ttf", 14f));
        bar.setForeground(getColorPorNivelDolor(nivelDolor));
        bar.setBackground(new Color(255, 230, 230));
        bar.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        bar.setMaximumSize(new Dimension(600, 35));
        bar.setPreferredSize(new Dimension(600, 35));
        bar.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        barraPanel.add(bar);
        
        panel.add(barraPanel);
        panel.add(Box.createVerticalStrut(15));

        // Leyenda centrada
        JLabel leyenda = new JLabel(getMensajePorNivelDolor(nivelDolor));
        leyenda.setFont(FuenteUtil.cargarFuente("EpundaSlab-Regular.ttf", 16f));
        leyenda.setForeground(COLOR_TEXTO_SECUNDARIO);
        leyenda.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(leyenda);

        return panel;
    }

    private Color getColorPorNivelDolor(int nivel) {
        if (nivel <= 3) return new Color(56, 142, 60); // Verde
        if (nivel <= 6) return new Color(245, 124, 0); // Naranja
        return new Color(194, 24, 7); // Rojo
    }

    private String getMensajePorNivelDolor(int nivel) {
        if (nivel <= 3) return "Dolor leve - Se recomiendan pausas activas y ajustes ergonómicos.";
        if (nivel <= 6) return "Dolor moderado - Se sugiere consulta preventiva con especialista.";
        return "Dolor severo - Se recomienda consulta inmediata con especialista.";
    }

    private void irAHome() {
        Home home = new Home();
        home.setVisible(true);
        home.setLocationRelativeTo(null);
        dispose();
    }

    private static Color colorPorRiesgo(String riesgo) {
        if (riesgo == null || riesgo.equals("N/D")) return new Color(30, 56, 136);
        String riesgoUpper = riesgo.toUpperCase();
        if (riesgoUpper.contains("ALTO")) {
            return new Color(194, 24, 7); // rojo
        } else if (riesgoUpper.contains("MEDIO") || riesgoUpper.contains("MODERADO")) {
            return new Color(245, 124, 0); // naranja
        } else if (riesgoUpper.contains("BAJO")) {
            return new Color(56, 142, 60); // verde
        } else {
            return new Color(30, 56, 136);
        }
    }

    private static String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}