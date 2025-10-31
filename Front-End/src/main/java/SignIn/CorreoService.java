package SignIn;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import javax.activation.DataHandler;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class CorreoService {

    private static final String REMITENTE = "alexandriabiblioteca611@gmail.com"; 
    private static final String PASSWORD = "pnxqpirozaqbaidm"; 

    public static String cargarPlantilla(String ruta, String nombres, String apellidos) {
    try {
        // Primero intenta leer desde el sistema de archivos (modo desarrollo)
        if (Files.exists(Paths.get(ruta))) {
            String contenido = Files.readString(Paths.get(ruta), StandardCharsets.UTF_8);
            contenido = contenido.replace("{{NOMBRES}}", nombres)
                                 .replace("{{APELLIDOS}}", apellidos);
            return contenido;
        }

        // Si no existe, intenta leer desde el classpath (modo empaquetado .jar)
        try (InputStream is = CorreoService.class.getResourceAsStream("/templates/plantilla_bienvenida.html")) {
            if (is == null) throw new FileNotFoundException("No se encontró plantilla en el classpath");
            String contenido = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            contenido = contenido.replace("{{NOMBRES}}", nombres)
                                 .replace("{{APELLIDOS}}", apellidos);
            return contenido;
        }

    } catch (Exception e) {
        System.err.println("Error al cargar la plantilla: " + e.getMessage());
        return "<p>Hola " + nombres + " " + apellidos + ", bienvenido a FLEX-IA.</p>";
    }
    }


    // Envía correos con HTML y UTF-8 (mantiene estilos e imágenes)
    public static boolean enviarCorreo(String destinatario, String asunto, String mensajeHtml) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "465");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.ssl.enable", "true");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(REMITENTE, PASSWORD);
                }
            });

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(REMITENTE, "FLEX-IA"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(asunto, "UTF-8");

            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(mensajeHtml, "text/html; charset=utf-8"); // HTML ACTIVO

           // Imagen 1: logo FLEX-IA
            MimeBodyPart logoPart = new MimeBodyPart();
            try (InputStream logoStream = CorreoService.class.getResourceAsStream("/images/logo-flexia2.png")) {
                if (logoStream == null) throw new FileNotFoundException("No se encontró logo-flexia.png en resources");
                logoPart.setDataHandler(new DataHandler(new ByteArrayDataSource(logoStream, "image/png")));
                logoPart.setHeader("Content-ID", "<logoFlexia>");
                logoPart.setDisposition(MimeBodyPart.INLINE);
            }

            // Imagen 2: mascota FLEX-IA
            MimeBodyPart mascotaPart = new MimeBodyPart();
            try (InputStream mascotaStream = CorreoService.class.getResourceAsStream("/images/Mascota.png")) {
                if (mascotaStream == null) throw new FileNotFoundException("No se encontró Mascota.png en resources");
                mascotaPart.setDataHandler(new DataHandler(new ByteArrayDataSource(mascotaStream, "image/png")));
                mascotaPart.setHeader("Content-ID", "<mascotaFlexia>");
                mascotaPart.setDisposition(MimeBodyPart.INLINE);
            }

            MimeMultipart multipart = new MimeMultipart("related");
            multipart.addBodyPart(htmlPart);
            multipart.addBodyPart(logoPart);
            multipart.addBodyPart(mascotaPart);

            message.setContent(multipart);

            Transport.send(message);
            System.out.println("✅ Correo enviado correctamente a " + destinatario);
            return true;

        } catch (Exception e) {
            System.out.println("❌ Error al enviar correo: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean enviarCorreoConReplyTo(String destinatario, String asunto, String mensajeHtml, String replyTo) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "465");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.ssl.enable", "true");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(REMITENTE, PASSWORD);
                }
            });

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(REMITENTE, "FLEX-IA"));
            message.setReplyTo(InternetAddress.parse(replyTo)); // << Aquí está la magia 😎
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(asunto, "UTF-8");

            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(mensajeHtml, "text/html; charset=utf-8");

            MimeMultipart multipart = new MimeMultipart("alternative");
            multipart.addBodyPart(htmlPart);

            message.setContent(multipart);

            Transport.send(message);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
