package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

import Back_End.Usuario;

public class UsuariosDAO {

    private Conexion conexion;

    public UsuariosDAO() {
        conexion = new Conexion();
    }


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
            boolean esPremium,
            byte[] fotoPerfil) {
        String sql = "INSERT INTO usuarios (tipo_id, numero_id, nombres, apellidos, correo_electronico, contrasena, fecha_nacimiento, telefono, es_premium, foto_perfil) "
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
            ps.setBoolean(9, esPremium);
            
                // Manejar la foto de perfil (puede ser null)
            if (fotoPerfil != null && fotoPerfil.length > 0) {
                ps.setBytes(10, fotoPerfil);
            } else {
                ps.setNull(10, java.sql.Types.BLOB);
            }

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

    public Usuario obtenerUsuarioPorCorreo(String correo) {
        String sql = "SELECT * FROM usuarios WHERE correo_electronico = ?";
        try (Connection conn = conexion.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, correo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setTipoId(rs.getString("tipo_id"));
                u.setNumeroId(rs.getString("numero_id"));
                u.setNombres(rs.getString("nombres"));
                u.setApellidos(rs.getString("apellidos"));
                u.setCorreo(rs.getString("correo_electronico"));
                u.setFechaNacimiento(rs.getDate("fecha_nacimiento"));
                u.setTelefono(rs.getString("telefono"));
                u.setEsPremium(rs.getBoolean("es_premium"));
                return u;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean actualizarUsuario(Usuario u) {
        String sql = "UPDATE usuarios SET nombres=?, apellidos=?, telefono=?, fecha_nacimiento=?, numero_id=?, tipo_id=? WHERE correo_electronico=?";

        try (Connection conn = conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, u.getNombres());
            ps.setString(2, u.getApellidos());
            ps.setString(3, u.getTelefono());
            ps.setDate(4, u.getFechaNacimiento());
            ps.setString(5, u.getNumeroId());
            ps.setString(6, u.getTipoId());
            ps.setString(7, u.getCorreo()); // WHERE condition

            int filasActualizadas = ps.executeUpdate();
            System.out.println("✅ Filas actualizadas: " + filasActualizadas);
            
            return filasActualizadas > 0;

        } catch (Exception e) {
            System.err.println("❌ Error actualizando usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

        // Agrega este método en UsuariosDAO.java
    public byte[] obtenerFotoPerfil(String correo) {
        String sql = "SELECT foto_perfil FROM usuarios WHERE correo_electronico = ?";
        
        try (Connection conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBytes("foto_perfil");
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener foto de perfil: " + e.getMessage());
        }
        return null;
    }

}
