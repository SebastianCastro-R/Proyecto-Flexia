package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MetricasEjercicioDAO {

    /**
     * Tabla propuesta:
     *
     * CREATE TABLE IF NOT EXISTS usuarios_ejercicio_metricas (
     *   id_usuario INT NOT NULL REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
     *   id_video INT NOT NULL REFERENCES videos(id_video) ON DELETE CASCADE,
     *   fecha DATE NOT NULL,
     *   ok_count INT NOT NULL DEFAULT 0,
     *   reset_count INT NOT NULL DEFAULT 0,
     *   updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
     *   PRIMARY KEY (id_usuario, id_video, fecha)
     * );
     */
    private static void ensureTable() {
        String ddl = "CREATE TABLE IF NOT EXISTS usuarios_ejercicio_metricas (" +
                "id_usuario INT NOT NULL REFERENCES usuarios(id_usuario) ON DELETE CASCADE," +
                "id_video INT NOT NULL REFERENCES videos(id_video) ON DELETE CASCADE," +
                "fecha DATE NOT NULL," +
                "ok_count INT NOT NULL DEFAULT 0," +
                "reset_count INT NOT NULL DEFAULT 0," +
                "updated_at TIMESTAMP NOT NULL DEFAULT NOW()," +
                "PRIMARY KEY (id_usuario, id_video, fecha)" +
                ")";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement ps = (conn != null) ? conn.prepareStatement(ddl) : null) {
            if (ps != null) {
                ps.execute();
            }
        } catch (SQLException e) {
            System.err.println("❌ MetricasEjercicioDAO.ensureTable: " + e.getMessage());
        }
    }

    /** Agrega (suma) eventos OK/RESET a la fila de HOY (upsert). */
    public static void agregarEventosHoy(int idUsuario, int idVideo, int okCount, int resetCount) {
        if (idUsuario <= 0 || idVideo <= 0) {
            return;
        }
        if (okCount == 0 && resetCount == 0) {
            return;
        }

        ensureTable();

        String sql = "INSERT INTO usuarios_ejercicio_metricas (id_usuario, id_video, fecha, ok_count, reset_count, updated_at) " +
                "VALUES (?, ?, CURRENT_DATE, ?, ?, NOW()) " +
                "ON CONFLICT (id_usuario, id_video, fecha) DO UPDATE SET " +
                "ok_count = usuarios_ejercicio_metricas.ok_count + EXCLUDED.ok_count, " +
                "reset_count = usuarios_ejercicio_metricas.reset_count + EXCLUDED.reset_count, " +
                "updated_at = NOW()";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement ps = (conn != null) ? conn.prepareStatement(sql) : null) {

            if (ps == null) {
                return;
            }

            ps.setInt(1, idUsuario);
            ps.setInt(2, idVideo);
            ps.setInt(3, okCount);
            ps.setInt(4, resetCount);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("❌ MetricasEjercicioDAO.agregarEventosHoy: " + e.getMessage());
        }
    }

    public static class PrecisionGlobal {
        public final long ok;
        public final long reset;

        public PrecisionGlobal(long ok, long reset) {
            this.ok = ok;
            this.reset = reset;
        }

        public long total() {
            return ok + reset;
        }

        public double precision() {
            long total = total();
            if (total <= 0) {
                return -1;
            }
            return (double) ok / (double) total;
        }
    }

    /** Retorna el agregado global para un usuario. */
    public static PrecisionGlobal obtenerPrecisionGlobal(int idUsuario) {
        if (idUsuario <= 0) {
            return new PrecisionGlobal(0, 0);
        }

        ensureTable();

        String sql = "SELECT COALESCE(SUM(ok_count),0) AS ok, COALESCE(SUM(reset_count),0) AS reset " +
                "FROM usuarios_ejercicio_metricas WHERE id_usuario = ?";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement ps = (conn != null) ? conn.prepareStatement(sql) : null) {

            if (ps == null) {
                return new PrecisionGlobal(0, 0);
            }

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new PrecisionGlobal(rs.getLong("ok"), rs.getLong("reset"));
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ MetricasEjercicioDAO.obtenerPrecisionGlobal: " + e.getMessage());
        }

        return new PrecisionGlobal(0, 0);
    }
}
