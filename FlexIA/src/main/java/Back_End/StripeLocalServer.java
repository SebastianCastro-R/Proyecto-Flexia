package Back_End;

import com.sun.net.httpserver.HttpServer;

import Database.UsuariosDAO;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class StripeLocalServer {

    private HttpServer server;
    private UsuariosDAO usuariosDAO;
    private SesionUsuario sesion;

    public StripeLocalServer(UsuariosDAO usuariosDAO, SesionUsuario sesion) {
        this.usuariosDAO = usuariosDAO;
        this.sesion = sesion;
    }

    public void iniciarServidor() throws IOException {
        server = HttpServer.create(new InetSocketAddress(4242), 0);

        // ==================================================
        //              URL DE ÉXITO DEL PAGO
        // ==================================================
        server.createContext("/premium/exito", exchange -> {

            String query = exchange.getRequestURI().getQuery();
            int idUsuario = -1;

            // Tomar ?id=xx desde la URL
            if (query != null && query.contains("id=")) {
                try {
                    idUsuario = Integer.parseInt(query.split("=")[1]);
                } catch (Exception e) {
                    idUsuario = -1;
                }
            }

            boolean ok = false;

            if (idUsuario != -1) {
                ok = usuariosDAO.actualizarPremium(idUsuario, true);

                // 🔥 Actualizar sesión si coincide
                if (sesion.getUsuarioActual() != null &&
                        sesion.getUsuarioActual().getIdUsuario() == idUsuario) {

                    sesion.getUsuarioActual().setEsPremium(true);
                }
            }

            String respuesta;
            if (ok) {
                respuesta = "<h1>✔ Pago Exitoso</h1>"
                        + "<p>Tu cuenta ahora es <b>Premium</b>.</p>"
                        + "<script>setTimeout(()=>{ window.close(); }, 2500);</script>";
            } else {
                respuesta = "<h1>❌ Error</h1>"
                        + "<p>No se pudo actualizar tu cuenta.</p>"
                        + "<script>setTimeout(()=>{ window.close(); }, 2500);</script>";
            }

            exchange.sendResponseHeaders(200, respuesta.length());

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(respuesta.getBytes());
            }

            server.stop(1);
        });

        // ==================================================
        //              URL DE CANCELACIÓN
        // ==================================================
        server.createContext("/premium/cancelado", exchange -> {

            String respuesta = "<h1>❌ Pago Cancelado</h1>"
                    + "<p>No se realizaron cambios en tu cuenta.</p>"
                    + "<script>setTimeout(()=>{ window.close(); }, 2500);</script>";

            exchange.sendResponseHeaders(200, respuesta.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(respuesta.getBytes());
            }

            server.stop(1);
        });

        server.start();
        System.out.println("Servidor Stripe en ejecución → http://localhost:4242/");
    }
}
