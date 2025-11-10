package com.mycompany.flexia.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

public class UsuariosDAO {

    /**
     * Registra un usuario: hashea la contraseña con BCrypt antes de guardarla.
     * 
     * @return true si insertó correctamente
     */
    public boolean registrarUsuario(String tipoId,
            String numeroId,
            String nombres,
            String apellidos,
            String correo,
            String contrasenaPlano,
            java.sql.Date fechaNacimiento,
            String telefono,
            String genero,
            boolean esPremium) {
        String sql = "INSERT INTO usuarios (tipo_id, numero_id, nombres, apellidos, correo_electronico, contrasena, fecha_nacimiento, telefono, genero, es_premium) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        // Generamos el hash con coste 12 (puedes ajustar)
        String hashed = BCrypt.hashpw(contrasenaPlano, BCrypt.gensalt(12));

        try (Connection conn = Conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tipoId);
            ps.setString(2, numeroId);
            ps.setString(3, nombres);
            ps.setString(4, apellidos);
            ps.setString(5, correo);
            ps.setString(6, hashed);
            ps.setDate(7, fechaNacimiento);
            ps.setString(8, telefono);
            ps.setString(9, genero);
            ps.setBoolean(10, esPremium);

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Comprueba las credenciales: busca por correo y compara el hash con BCrypt.
     * Devuelve true si coincide (login OK).
     */
    public boolean autenticarUsuarioPorCorreo(String correo, String contrasenaPlano) {
        String sql = "SELECT contrasena FROM usuarios WHERE correo_electronico = ?";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hashGuardado = rs.getString("contrasena");
                    // BCrypt se encarga de la comparación segura
                    return BCrypt.checkpw(contrasenaPlano, hashGuardado);
                } else {
                    // usuario no existe
                    return false;
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al autenticar usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean verificarUsuario(String correo, String contrasena) {
        String sql = "SELECT contrasena FROM usuarios WHERE correo_electronico = ?";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String contrasenaHash = rs.getString("contrasena");
                return BCrypt.checkpw(contrasena, contrasenaHash);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al verificar usuario: " + e.getMessage());
        }

        return false;
    }

    /**
     * Actualiza la contraseña de un usuario por su correo electrónico
     */
    public boolean actualizarContrasena(String correo, String nuevaContrasenaPlano) {
        String sql = "UPDATE usuarios SET contrasena = ? WHERE correo_electronico = ?";

        // Generar nuevo hash con un salt distinto cada vez
        String hashed = BCrypt.hashpw(nuevaContrasenaPlano, BCrypt.gensalt(12));

        try (Connection conn = Conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, hashed);
            ps.setString(2, correo);

            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("🔐 Contraseña actualizada correctamente para: " + correo);
                return true;
            } else {
                System.out.println("⚠️ No se encontró usuario con el correo: " + correo);
                return false;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar contraseña: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    

}
