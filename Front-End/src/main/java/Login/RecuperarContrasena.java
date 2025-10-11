package Login;

import javax.swing.*;
import java.awt.*;

public class RecuperarContrasena extends JFrame {

    public RecuperarContrasena(String correoUsuario) {
        // Configuración básica de la ventana
        setTitle("Recuperar contraseña");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel titulo = new JLabel("Recuperar contraseña", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titulo, gbc);

        // Etiqueta del correo
        JLabel correoLbl = new JLabel("Correo electrónico:");
        correoLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(correoLbl, gbc);

        // Campo del correo (no editable)
        JTextField correoTxt = new JTextField(correoUsuario);
        correoTxt.setEditable(false);
        gbc.gridx = 1;
        panel.add(correoTxt, gbc);

        // Botón enviar código
        JButton enviarCodigoBtn = new JButton("Enviar código");
        enviarCodigoBtn.setBackground(new Color(60, 130, 200));
        enviarCodigoBtn.setForeground(Color.WHITE);
        enviarCodigoBtn.setFocusPainted(false);
        enviarCodigoBtn.setBorderPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(enviarCodigoBtn, gbc);

        // Campo para escribir el código recibido
        JLabel codigoLbl = new JLabel("Código de verificación:");
        codigoLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        panel.add(codigoLbl, gbc);

        JTextField codigoTxt = new JTextField();
        gbc.gridx = 1;
        panel.add(codigoTxt, gbc);

        // Botón verificar
        JButton verificarBtn = new JButton("Verificar código");
        verificarBtn.setBackground(new Color(100, 180, 100));
        verificarBtn.setForeground(Color.WHITE);
        verificarBtn.setFocusPainted(false);
        verificarBtn.setBorderPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(verificarBtn, gbc);

        add(panel);
    }

    // Método de prueba independiente
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RecuperarContrasena("usuario@ejemplo.com").setVisible(true);
        });
    }
}