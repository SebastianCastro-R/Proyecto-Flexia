package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DolorDAO {

    /**
     * Tabla propuesta:
     *
     * CREATE TABLE IF NOT EXISTS usuarios_dolor_diario (
     *   id_usuario INT NOT NULL REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
     *   fecha DATE NOT NULL,
     *   nivel INT NOT NULL CHECK (nivel BETWEEN 1 AND 5),
     *   updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
     *   PRIMARY KEY (id_usuario, fecha)
     * );
     */
    private static void ensureTable() {
        String ddl = "CREATE TABLE IF NOT EXISTS usuarios_dolor_diario (" +
                "id_usuario INT NOT NULL REFERENCES usuarios(id_usuario) ON DELETE CASCADE," +
                "fecha DATE NOT NULL," +
                "nivel INT NOT NULL CHECK (nivel BETWEEN 1 AND 5)," +
                "updated_at TIMESTAMP NOT NULL DEFAULT NOW()," +
                "PRIMARY KEY (id_usuario, fecha)" +
                ")";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement ps = (conn != null) ? conn.prepareStatement(ddl) : null) {
            if (ps != null) {
                ps.execute();
            }
        } catch (SQLException e) {
            System.err.println("❌ DolorDAO.ensureTable: " + e.getMessage());
        }
    }

    /** Registra/actualiza el dolor de HOY para el usuario. Nivel esperado: 1-5. */
    public static void registrarDolorHoy(int idUsuario, int nivel) {
        if (idUsuario <= 0) {
            return;
        }
        if (nivel < 1 || nivel > 5) {
            return;
        }

        ensureTable();

        String sql = "INSERT INTO usuarios_dolor_diario (id_usuario, fecha, nivel, updated_at) " +
                "VALUES (?, CURRENT_DATE, ?, NOW()) " +
                "ON CONFLICT (id_usuario, fecha) DO UPDATE SET nivel = EXCLUDED.nivel, updated_at = NOW()";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement ps = (conn != null) ? conn.prepareStatement(sql) : null) {

            if (ps == null) {
                return;
            }

            ps.setInt(1, idUsuario);
            ps.setInt(2, nivel);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("❌ DolorDAO.registrarDolorHoy: " + e.getMessage());
        }
    }

    /** Obtiene el último nivel de dolor registrado (1-5). Retorna -1 si no hay. */
    public static int obtenerUltimoNivelDolor(int idUsuario) {
        if (idUsuario <= 0) {
            return -1;
        }

        ensureTable();

        String sql = "SELECT nivel FROM usuarios_dolor_diario WHERE id_usuario = ? ORDER BY fecha DESC LIMIT 1";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement ps = (conn != null) ? conn.prepareStatement(sql) : null) {

            if (ps == null) {
                return -1;
            }

            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("nivel");
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ DolorDAO.obtenerUltimoNivelDolor: " + e.getMessage());
        }

        return -1;
    }

    /** Promedio de dolor últimos N días (incluye hoy). Retorna -1 si no hay datos. */
    public static double obtenerPromedioDolorUltimosNDias(int idUsuario, int dias) {
        if (idUsuario <= 0 || dias <= 0) {
            return -1;
        }

        ensureTable();

        // Nota: en PostgreSQL se puede usar CURRENT_DATE - (? - 1)
        String sql = "SELECT AVG(nivel)::float AS promedio " +
                "FROM usuarios_dolor_diario " +
                "WHERE id_usuario = ? AND fecha >= (CURRENT_DATE - (? - 1))";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement ps = (conn != null) ? conn.prepareStatement(sql) : null) {

            if (ps == null) {
                return -1;
            }

            ps.setInt(1, idUsuario);
            ps.setInt(2, dias);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double promedio = rs.getDouble("promedio");
                    if (rs.wasNull()) {
                        return -1;
                    }
                    return promedio;
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ DolorDAO.obtenerPromedioDolorUltimosNDias: " + e.getMessage());
        }

        return -1;
    }

    public static class DolorDiario {
        public final LocalDate fecha;
        public final int nivel; // 1-5

        public DolorDiario(LocalDate fecha, int nivel) {
            this.fecha = fecha;
            this.nivel = nivel;
        }
    }

    /** Serie de dolor por día (últimos N días, incluyendo hoy). Rellena faltantes con 0. */
    public static List<DolorDiario> obtenerDolorUltimosNDias(int idUsuario, int dias) {
        List<DolorDiario> out = new ArrayList<>();
        if (idUsuario <= 0 || dias <= 0) {
            return out;
        }

        ensureTable();

        String sql = "SELECT fecha, nivel FROM usuarios_dolor_diario " +
                "WHERE id_usuario = ? AND fecha >= (CURRENT_DATE - (? - 1)) " +
                "ORDER BY fecha ASC";

        Map<LocalDate, Integer> mapa = new HashMap<>();

        try (Connection conn = Conexion.getConnection();
                PreparedStatement ps = (conn != null) ? conn.prepareStatement(sql) : null) {

            if (ps != null) {
                ps.setInt(1, idUsuario);
                ps.setInt(2, dias);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        LocalDate fecha = rs.getDate("fecha").toLocalDate();
                        int nivel = rs.getInt("nivel");
                        mapa.put(fecha, nivel);
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ DolorDAO.obtenerDolorUltimosNDias: " + e.getMessage());
        }

        LocalDate inicio = LocalDate.now().minusDays(dias - 1L);
        for (int i = 0; i < dias; i++) {
            LocalDate d = inicio.plusDays(i);
            out.add(new DolorDiario(d, mapa.getOrDefault(d, 0)));
        }

        return out;
    }
}
