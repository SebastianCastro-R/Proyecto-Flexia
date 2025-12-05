package Database;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RachaDAO {
    private Connection conexion;

    public RachaDAO() {
        conexion = Conexion.getConnection();
    }

    // Registrar que el usuario realizó actividad hoy
    public boolean registrarActividadHoy(int idUsuario) {
        String sql = "INSERT INTO racha_usuario (id_usuario, fecha, realizo) VALUES (?, ?, ?) " +
                    "ON CONFLICT (id_usuario, fecha) DO UPDATE SET realizo = ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            LocalDate hoy = LocalDate.now();
            
            stmt.setInt(1, idUsuario);
            stmt.setDate(2, Date.valueOf(hoy));
            stmt.setBoolean(3, true);
            stmt.setBoolean(4, true);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar actividad: " + e.getMessage());
            return false;
        }
    }

    // Obtener los últimos 7 días de actividad (alineado con días de la semana)
    public List<Boolean> obtenerUltimaSemanaActividad(int idUsuario) {
        List<Boolean> semana = new ArrayList<>();
        LocalDate hoy = LocalDate.now();
        
        // Obtener el lunes de esta semana (semana empieza en lunes)
        LocalDate lunes = hoy.with(DayOfWeek.MONDAY);
        
        String sql = "SELECT fecha, realizo FROM racha_usuario " +
                    "WHERE id_usuario = ? AND fecha >= ? AND fecha <= ? " +
                    "ORDER BY fecha ASC";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            stmt.setDate(2, Date.valueOf(lunes));
            stmt.setDate(3, Date.valueOf(hoy));
            
            ResultSet rs = stmt.executeQuery();
            
            // Crear mapa para almacenar actividades por fecha
            List<LocalDate> fechasConActividad = new ArrayList<>();
            while (rs.next()) {
                Date fechaBD = rs.getDate("fecha");
                boolean realizo = rs.getBoolean("realizo");
                if (realizo) {
                    fechasConActividad.add(fechaBD.toLocalDate());
                }
            }
            
            // Llenar la semana desde lunes hasta hoy
            LocalDate fecha = lunes;
            for (int i = 0; i < 7; i++) {
                if (fecha.isAfter(hoy)) {
                    // Días futuros - no mostrar
                    semana.add(false);
                } else {
                    // Verificar si hay actividad en esta fecha
                    boolean actividad = fechasConActividad.contains(fecha);
                    semana.add(actividad);
                }
                fecha = fecha.plusDays(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener racha: " + e.getMessage());
            // En caso de error, retornar lista vacía
            for (int i = 0; i < 7; i++) {
                semana.add(false);
            }
        }
        
        return semana;
    }

    // Obtener todas las fechas con actividad para el calendario
    public List<LocalDate> obtenerFechasConActividad(int idUsuario) {
        List<LocalDate> fechas = new ArrayList<>();
        String sql = "SELECT fecha FROM racha_usuario WHERE id_usuario = ? AND realizo = true ORDER BY fecha";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                fechas.add(rs.getDate("fecha").toLocalDate());
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener fechas con actividad: " + e.getMessage());
        }
        
        return fechas;
    }

    // Obtener la racha actual (días consecutivos hasta hoy)
    public int obtenerRachaActual(int idUsuario) {
        String sql = "WITH dias_consecutivos AS ( " +
                    "SELECT fecha, " +
                    "       fecha - ROW_NUMBER() OVER (ORDER BY fecha DESC) * INTERVAL '1 day' as grupo " +
                    "FROM racha_usuario " +
                    "WHERE id_usuario = ? AND realizo = true " +
                    "AND fecha <= CURRENT_DATE " +
                    ") " +
                    "SELECT COUNT(*) as racha " +
                    "FROM dias_consecutivos " +
                    "WHERE grupo = (SELECT MAX(grupo) FROM dias_consecutivos)";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("racha");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener racha actual: " + e.getMessage());
        }
        
        return 0;
    }

    // Verificar si ya registró actividad hoy
    public boolean yaRegistroHoy(int idUsuario) {
        String sql = "SELECT 1 FROM racha_usuario WHERE id_usuario = ? AND fecha = CURRENT_DATE AND realizo = true";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error al verificar registro de hoy: " + e.getMessage());
        }
        
        return false;
    }

    public int obtenerRachaSeguida(int idUsuario) {
        // SQL CORREGIDO para calcular la racha de días consecutivos.
        // Utiliza la técnica de la diferencia de fila (ROW_NUMBER()) para encontrar grupos de días seguidos.
        String sql = "WITH actividad_ordenada AS (" +
                     "    SELECT fecha" +
                     "    FROM racha_usuario" +
                     "    WHERE id_usuario = ? AND realizo = true" +
                     "    ORDER BY fecha ASC" +
                     ")," +
                     "grupos_racha AS (" +
                     "    SELECT " +
                     "        fecha," +
                     "        fecha - ROW_NUMBER() OVER (ORDER BY fecha) * INTERVAL '1 day' AS grupo_id" +
                     "    FROM " +
                     "        actividad_ordenada" +
                     ")," +
                     "racha_actual AS (" +
                     "    SELECT " +
                     "        grupo_id" +
                     "    FROM " +
                     "        grupos_racha" +
                     "    ORDER BY " +
                     "        fecha DESC" +
                     "    LIMIT 1" +
                     ")" +
                     "SELECT " +
                     "    COUNT(*) AS racha" +
                     " FROM " +
                     "    grupos_racha" +
                     " WHERE " +
                     "    grupo_id = (SELECT grupo_id FROM racha_actual)";
        
        try (Connection conn = Conexion.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("racha");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener racha seguida: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    
}