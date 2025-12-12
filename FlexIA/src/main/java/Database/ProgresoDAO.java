package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProgresoDAO {

    /**
     * Tabla de resumen (último completado por video):
     *
     * CREATE TABLE IF NOT EXISTS usuarios_video_progreso (
     *   id_usuario INT NOT NULL REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
     *   id_video   INT NOT NULL REFERENCES videos(id_video)   ON DELETE CASCADE,
     *   completado BOOLEAN NOT NULL DEFAULT TRUE,
     *   fecha_completado TIMESTAMP NOT NULL DEFAULT NOW(),
     *   PRIMARY KEY (id_usuario, id_video)
     * );
     */
    public static void marcarVideoCompletado(int idUsuario, int idVideo) {
        String sql = "INSERT INTO usuarios_video_progreso (id_usuario, id_video, completado, fecha_completado) " +
                "VALUES (?, ?, TRUE, NOW()) " +
                "ON CONFLICT (id_usuario, id_video) DO UPDATE SET completado = TRUE, fecha_completado = NOW()";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.setInt(2, idVideo);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("❌ ProgresoDAO.marcarVideoCompletado: " + e.getMessage());
        }
    }

    /**
     * Historial diario (1 completado por día, pero permite contar repeticiones extra sin "progreso").
     *
     * CREATE TABLE IF NOT EXISTS usuarios_video_progreso_diario (
     *   id_usuario INT NOT NULL REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
     *   id_video   INT NOT NULL REFERENCES videos(id_video)   ON DELETE CASCADE,
     *   fecha DATE NOT NULL,
     *   completado_en TIMESTAMP NOT NULL DEFAULT NOW(),
     *   repeticiones_extra INT NOT NULL DEFAULT 0,
     *   PRIMARY KEY (id_usuario, id_video, fecha)
     * );
     *
     * -- opcional para consultas:
     * CREATE INDEX IF NOT EXISTS idx_uvpd_usuario_fecha ON usuarios_video_progreso_diario (id_usuario, fecha);
     */
    public static boolean estaVideoCompletadoHoy(int idUsuario, int idVideo) {
        String sql = "SELECT 1 FROM usuarios_video_progreso_diario WHERE id_usuario = ? AND id_video = ? AND fecha = CURRENT_DATE";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.setInt(2, idVideo);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.err.println("❌ ProgresoDAO.estaVideoCompletadoHoy: " + e.getMessage());
            return false;
        }
    }

    /**
     * Registra el completado del día.
     *
     * - Si es la primera vez del día => inserta registro diario.
     * - Si ya estaba completado hoy => incrementa repeticiones_extra (no suma progreso).
     *
     * Retorna true si fue la primera vez del día (o sea, "cuenta" como completado hoy).
     */
    public static boolean registrarVideoCompletadoHoy(int idUsuario, int idVideo) {
        boolean primeraVezHoy = !estaVideoCompletadoHoy(idUsuario, idVideo);

        String sqlDiario = "INSERT INTO usuarios_video_progreso_diario (id_usuario, id_video, fecha, completado_en, repeticiones_extra) " +
                "VALUES (?, ?, CURRENT_DATE, NOW(), 0) " +
                "ON CONFLICT (id_usuario, id_video, fecha) DO UPDATE " +
                "SET repeticiones_extra = usuarios_video_progreso_diario.repeticiones_extra + 1";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(sqlDiario)) {

            ps.setInt(1, idUsuario);
            ps.setInt(2, idVideo);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("❌ ProgresoDAO.registrarVideoCompletadoHoy: " + e.getMessage());
        }

        // Actualizar también tabla resumen (última vez que lo hizo, para estadísticas rápidas)
        marcarVideoCompletado(idUsuario, idVideo);

        return primeraVezHoy;
    }

    /** Carga los id_video completados HOY para el usuario (para la UI). */
    public static Set<Integer> obtenerVideosCompletadosHoy(int idUsuario) {
        Set<Integer> completados = new HashSet<>();

        String sql = "SELECT id_video FROM usuarios_video_progreso_diario WHERE id_usuario = ? AND fecha = CURRENT_DATE";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    completados.add(rs.getInt("id_video"));
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ ProgresoDAO.obtenerVideosCompletadosHoy: " + e.getMessage());
        }

        return completados;
    }

    /** Retorna true si el video está marcado como completado (histórico/resumen). */
    public static boolean estaVideoCompletado(int idUsuario, int idVideo) {
        String sql = "SELECT 1 FROM usuarios_video_progreso WHERE id_usuario = ? AND id_video = ? AND completado = TRUE";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.setInt(2, idVideo);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.err.println("❌ ProgresoDAO.estaVideoCompletado: " + e.getMessage());
            return false;
        }
    }

    /** Carga TODOS los id_video completados alguna vez (histórico/resumen). */
    public static Set<Integer> obtenerVideosCompletados(int idUsuario) {
        Set<Integer> completados = new HashSet<>();

        String sql = "SELECT id_video FROM usuarios_video_progreso WHERE id_usuario = ? AND completado = TRUE";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    completados.add(rs.getInt("id_video"));
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ ProgresoDAO.obtenerVideosCompletados: " + e.getMessage());
        }

        return completados;
    }

    /** Total de videos completados (únicos) por el usuario (tabla resumen). */
    public static int contarVideosCompletados(int idUsuario) {
        String sql = "SELECT COUNT(*) AS total FROM usuarios_video_progreso WHERE id_usuario = ? AND completado = TRUE";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement ps = (conn != null) ? conn.prepareStatement(sql) : null) {

            if (ps == null) {
                return 0;
            }

            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ ProgresoDAO.contarVideosCompletados: " + e.getMessage());
        }

        return 0;
    }

    /** Total de ejercicios realizados (diario): cuenta el completado base + repeticiones extra. */
    public static long obtenerTotalEjerciciosRealizados(int idUsuario) {
        String sql = "SELECT COALESCE(COUNT(*),0) + COALESCE(SUM(repeticiones_extra),0) AS total " +
                "FROM usuarios_video_progreso_diario WHERE id_usuario = ?";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement ps = (conn != null) ? conn.prepareStatement(sql) : null) {

            if (ps == null) {
                return 0;
            }

            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("total");
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ ProgresoDAO.obtenerTotalEjerciciosRealizados: " + e.getMessage());
        }

        return 0;
    }

    public static class ProgresoDiario {
        public final LocalDate fecha;
        public final long totalEjercicios;

        public ProgresoDiario(LocalDate fecha, long totalEjercicios) {
            this.fecha = fecha;
            this.totalEjercicios = totalEjercicios;
        }
    }

    /**
     * Serie de progreso por día (últimos N días, incluyendo hoy).
     * totalEjercicios = (COUNT registros del día) + (SUM repeticiones_extra del día).
     */
    public static List<ProgresoDiario> obtenerProgresoUltimosNDias(int idUsuario, int dias) {
        List<ProgresoDiario> out = new ArrayList<>();
        if (idUsuario <= 0 || dias <= 0) {
            return out;
        }

        // Traer solo días presentes
        String sql = "SELECT fecha, (COUNT(*) + COALESCE(SUM(repeticiones_extra),0)) AS total " +
                "FROM usuarios_video_progreso_diario " +
                "WHERE id_usuario = ? AND fecha >= (CURRENT_DATE - (? - 1)) " +
                "GROUP BY fecha ORDER BY fecha ASC";

        java.util.Map<LocalDate, Long> mapa = new java.util.HashMap<>();

        try (Connection conn = Conexion.getConnection();
                PreparedStatement ps = (conn != null) ? conn.prepareStatement(sql) : null) {

            if (ps != null) {
                ps.setInt(1, idUsuario);
                ps.setInt(2, dias);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        LocalDate fecha = rs.getDate("fecha").toLocalDate();
                        long total = rs.getLong("total");
                        mapa.put(fecha, total);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ ProgresoDAO.obtenerProgresoUltimosNDias: " + e.getMessage());
        }

        // Rellenar días faltantes con 0
        LocalDate inicio = LocalDate.now().minusDays(dias - 1L);
        for (int i = 0; i < dias; i++) {
            LocalDate d = inicio.plusDays(i);
            out.add(new ProgresoDiario(d, mapa.getOrDefault(d, 0L)));
        }

        return out;
    }
}
