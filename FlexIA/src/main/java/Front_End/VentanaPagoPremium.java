package Front_End;

import Back_End.StripeCheckoutService;
import Back_End.StripeLocalServer;
import Database.UsuariosDAO;
import Back_End.SesionUsuario;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URI;
import javax.swing.*;

public class VentanaPagoPremium extends JFrame {

    private int idUsuario;
    private SesionUsuario sesion;

    public VentanaPagoPremium(int idUsuario, SesionUsuario sesion) {
        this.idUsuario = idUsuario;
        this.sesion = sesion;

        setTitle("Suscripción Premium");
        setSize(450, 330);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Suscripción Premium", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setBounds(50, 20, 340, 40);
        panel.add(lblTitulo);

        JTextArea texto = new JTextArea(
                "Con Premium obtienes:\n\n"
                + "✔ Acceso ilimitado\n"
                + "✔ Funciones avanzadas\n"
                + "✔ Experiencia sin restricciones\n\n"
                + "Precio: $10 USD"
        );
        texto.setEditable(false);
        texto.setFont(new Font("Arial", Font.PLAIN, 16));
        texto.setBackground(Color.WHITE);
        texto.setBounds(70, 70, 300, 150);
        panel.add(texto);

        JButton btnComprar = new JButton("Comprar Premium");
        btnComprar.setFont(new Font("Arial", Font.BOLD, 16));
        btnComprar.setBounds(120, 230, 200, 40);

        btnComprar.addActionListener((ActionEvent e) -> procesarPago());

        panel.add(btnComprar);

        add(panel);
    }

    private void procesarPago() {
        try {
            // Crear servicio Stripe
            StripeCheckoutService checkout = new StripeCheckoutService();

            // Generar sesión de pago con success_url + cancel_url
            String checkoutUrl = checkout.crearSesionPago(idUsuario);

            // Iniciar servidor local para recibir callback de Stripe
            StripeLocalServer server = new StripeLocalServer(new UsuariosDAO(), sesion);
            server.iniciarServidor();

            // Abrir navegador con el checkout real
            Desktop.getDesktop().browse(new URI(checkoutUrl));

            JOptionPane.showMessageDialog(
                this,
                "Redirigiendo a Stripe…",
                "Procesando",
                JOptionPane.INFORMATION_MESSAGE
            );

            // Cerrar ventana
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                "Ocurrió un error al iniciar el pago.\nVer consola para detalles.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }
}
