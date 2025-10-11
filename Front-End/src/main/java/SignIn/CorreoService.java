package SignIn;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class CorreoService {

    private static final String REMITENTE = "alexandriabiblioteca611@gmail.com"; // tu correo
    private static final String PASSWORD = "pnxqpirozaqbaidm"; // contraseña o app password

    public static boolean enviarCorreo(String destinatario, String asunto, String mensaje) {
        try {
            // Propiedades del servidor SMTP de Gmail
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "465");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.ssl.enable", "true");

            // Crear la sesión autenticada
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(REMITENTE, PASSWORD);
                }
            });

            // Crear el mensaje
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(REMITENTE));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            msg.setSubject(asunto);
            msg.setText(mensaje);

            // Enviar
            Transport.send(msg);
            System.out.println("✅ Correo enviado a: " + destinatario);
            return true;

        } catch (Exception e) {
            System.out.println("❌ Error al enviar correo: " + e.getMessage());
            return false;
        }
    }
}
