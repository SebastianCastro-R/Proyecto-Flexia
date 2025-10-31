package Interfaz;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.ArrayList;
import java.util.List;

public class Ejercicios extends javax.swing.JFrame {

    private Menu menuPanel;
    private boolean menuVisible = false;
    private int menuWidth = 370; // ancho del panel del menú
    private int menuX = -menuWidth; // posición inicial fuera de pantalla

    public Ejercicios() {
        initComponents();

        // Cambia el layout del contenedor principal
        getContentPane().setLayout(null);

        setUndecorated(true);
        setSize(1440, 1024);
        setLocationRelativeTo(null);
        setResizable(false);

        // Espera a que la ventana esté visible para crear el menú con la altura correcta
        SwingUtilities.invokeLater(() -> {
            menuPanel = new Menu("Videos");
            menuWidth = 370;
            menuX = -menuWidth;

            menuPanel.setBounds(menuX, 0, menuWidth, getHeight());
            menuPanel.setBackground(new Color(250, 250, 250));
            menuPanel.setVisible(true);
            menuPanel.setOpaque(true);

            getContentPane().add(menuPanel);
            getContentPane().setComponentZOrder(menuPanel, 0);
            revalidate();
            repaint();
        });
    }


    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        JPanel fondo = new JPanel();
        fondo.setBackground(new Color(250, 250, 250));
        fondo.setLayout(new BorderLayout());

        // ----- BARRA SUPERIOR -----
        JPanel barra = new JPanel(new org.netbeans.lib.awtextra.AbsoluteLayout());
        barra.setBackground(new Color(30, 56, 136));

        JLabel menu = new JLabel(new ImageIcon(getClass().getResource("/icons/menu.png")));
        menu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        menu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                toggleMenu(evt);
            }
        });
        barra.add(menu, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 5, -1, 30));

        JLabel titulo = new JLabel("FLEX-IA");
        titulo.setFont(new Font("Epunda Slab ExtraBold", Font.PLAIN, 24));
        titulo.setForeground(Color.WHITE);
        barra.add(titulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 0, -1, 40));

        JLabel exit = new JLabel(new ImageIcon(getClass().getResource("/Images/exit.png")));
        exit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dispose();
            }
        });
        barra.add(exit, new org.netbeans.lib.awtextra.AbsoluteConstraints(1410, 0, 30, 40));
        fondo.add(barra, BorderLayout.NORTH);

        // ----- PANEL PRINCIPAL -----
        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBackground(new Color(250, 250, 250));

        JLabel tituloPag = new JLabel("EJERCICIOS", SwingConstants.CENTER);
        tituloPag.setFont(new Font("Epunda Slab ExtraBold", Font.PLAIN, 32));
        tituloPag.setAlignmentX(Component.CENTER_ALIGNMENT);
        tituloPag.setBorder(new EmptyBorder(30, 0, 30, 0));
        contenido.add(tituloPag);

        // Crear unidades
        contenido.add(crearUnidad("Unidad 1", 6)); // 6 ejercicios como ejemplo
        contenido.add(Box.createVerticalStrut(30));
        contenido.add(crearUnidad("Unidad 2", 4));

        JScrollPane scroll = new JScrollPane(contenido);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        fondo.add(scroll, BorderLayout.CENTER);

        fondo.setBounds(0, 0, 1440, 1024);
        getContentPane().add(fondo);
    }

    // ---------------- CREACIÓN DE UNIDAD ----------------
    private JPanel crearUnidad(String nombre, int numEjercicios) {
        JPanel unidadPanel = new JPanel(new BorderLayout());
        unidadPanel.setBackground(new Color(250, 250, 250));

        JLabel tituloUnidad = new JLabel(nombre);
        tituloUnidad.setFont(new Font("Epunda Slab ExtraBold", Font.PLAIN, 22));
        tituloUnidad.setForeground(new Color(30, 56, 136));
        tituloUnidad.setBorder(new EmptyBorder(10, 20, 10, 0));
        unidadPanel.add(tituloUnidad, BorderLayout.NORTH);

        // Panel contenedor con botones de navegación
        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBackground(new Color(250, 250, 250));
        contenedor.setBorder(new CompoundBorder(
                new LineBorder(new Color(200, 220, 250), 2, true),
                new EmptyBorder(15, 20, 15, 20)
        ));

        // Lista con todos los ejercicios
        List<JPanel> ejercicios = new ArrayList<>();
        for (int i = 1; i <= numEjercicios; i++) {
            ejercicios.add(crearEjercicio("Ejercicio #" + i,
                    "Este ejercicio ayuda a mejorar la movilidad y fuerza de la muñeca.",
                    "/Images/Background.jpg"));
        }

        // Panel que mostrará los ejercicios visibles
        JPanel panelVisible = new JPanel(new GridLayout(1, 3, 25, 0));
        panelVisible.setBackground(new Color(250, 250, 250));

        // Índice para desplazarse
        final int[] indice = {0};
        mostrarEjercicios(panelVisible, ejercicios, indice[0]);

        // Botones izquierda y derecha
        JButton btnIzquierda = new JButton("◀");
        JButton btnDerecha = new JButton("▶");
        estiloBotonNavegacion(btnIzquierda);
        estiloBotonNavegacion(btnDerecha);

        btnIzquierda.addActionListener(e -> {
            if (indice[0] > 0) {
                indice[0] -= 3;
                mostrarEjercicios(panelVisible, ejercicios, indice[0]);
            }
        });

        btnDerecha.addActionListener(e -> {
            if (indice[0] + 3 < ejercicios.size()) {
                indice[0] += 3;
                mostrarEjercicios(panelVisible, ejercicios, indice[0]);
            }
        });

        contenedor.add(btnIzquierda, BorderLayout.WEST);
        contenedor.add(panelVisible, BorderLayout.CENTER);
        contenedor.add(btnDerecha, BorderLayout.EAST);

        unidadPanel.add(contenedor, BorderLayout.CENTER);
        return unidadPanel;
    }

    private void mostrarEjercicios(JPanel panelVisible, List<JPanel> ejercicios, int inicio) {
        panelVisible.removeAll();
        for (int i = inicio; i < inicio + 3 && i < ejercicios.size(); i++) {
            panelVisible.add(ejercicios.get(i));
        }
        panelVisible.revalidate();
        panelVisible.repaint();
    }

    private void estiloBotonNavegacion(JButton boton) {
        boton.setFont(new Font("Arial", Font.BOLD, 20));
        boton.setFocusPainted(false);
        boton.setBackground(new Color(30, 56, 136));
        boton.setForeground(Color.WHITE);
        boton.setPreferredSize(new Dimension(50, 100));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // ---------------- CREACIÓN DE EJERCICIO ----------------
    private JPanel crearEjercicio(String titulo, String descripcion, String rutaImagen) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 250));
        panel.setBorder(new CompoundBorder(
                new LineBorder(new Color(230, 230, 250), 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel tituloLbl = new JLabel(titulo);
        tituloLbl.setFont(new Font("Epunda Slab ExtraBold", Font.PLAIN, 18));
        tituloLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Contenedor para la imagen + botón de play
        JPanel contenedorImagen = new JPanel(null);
        contenedorImagen.setPreferredSize(new Dimension(250, 140));
        contenedorImagen.setMaximumSize(new Dimension(250, 140));
        contenedorImagen.setBackground(new Color(245, 245, 250));

        ImageIcon original = new ImageIcon(getClass().getResource(rutaImagen));
        Image imgEscalada = original.getImage().getScaledInstance(250, 140, Image.SCALE_SMOOTH);
        JLabel preview = new JLabel(new ImageIcon(imgEscalada));
        preview.setBounds(0, 0, 250, 140);
        contenedorImagen.add(preview);

        // Botón de play centrado sobre la imagen
        JLabel playBtn = new JLabel(new ImageIcon(getClass().getResource("/icons/play.png")));
        playBtn.setBounds(100, 45, 50, 50);
        playBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Acción al hacer clic (abrir interfaz de reproducción)
        playBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Aquí abrirías la interfaz del video desde BD
                Ejercicios.this.dispose();
            }
        });


        contenedorImagen.add(playBtn);

        JTextArea desc = new JTextArea(descripcion);
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        desc.setEditable(false);
        desc.setBackground(new Color(245, 245, 250));
        desc.setFont(new Font("SansSerif", Font.PLAIN, 13));
        desc.setBorder(null);

        panel.add(tituloLbl);
        panel.add(Box.createVerticalStrut(10));
        panel.add(contenedorImagen);
        panel.add(Box.createVerticalStrut(10));
        panel.add(desc);

        return panel;
    }

    private void toggleMenu(java.awt.event.MouseEvent evt) {
        if (menuVisible) {
            // Ocultar menú
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
            // Mostrar menú
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

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new Ejercicios().setVisible(true));
    }
}