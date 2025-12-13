package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EncuestaDAO {

    /**
     * Migraciones suaves para esquemas viejos.
     * - Si existe la columna `genero` y está NOT NULL, la vuelve nullable para evitar fallos al insertar.
     */
    private static void migrateLegacySchema(Connection conn) {
        if (conn == null) {
            return;
        }

        // Si el esquema viejo todavía tiene `genero TEXT NOT NULL`, nuestro INSERT (sin genero)
        // intentará insertar NULL y PostgreSQL falla. Esta migración lo evita.
        try (PreparedStatement ps = conn.prepareStatement("ALTER TABLE encuesta ALTER COLUMN genero DROP NOT NULL")) {
            ps.execute();
        } catch (SQLException e) {
            // Ignorar: puede que la columna no exista, o ya sea nullable.
        }
    }

    private static void ensureTable() {
        String ddl = "CREATE TABLE IF NOT EXISTS encuesta (" +
                "id SERIAL PRIMARY KEY," +
                "correo_usuario TEXT NOT NULL UNIQUE," +
                "nombre TEXT NOT NULL," +
                "edad INT NOT NULL," +
                "mano_dominante TEXT NOT NULL," +
                "ocupacion TEXT NOT NULL," +
                "horas_computador INT NOT NULL," +
                "sintoma1 TEXT NOT NULL," +
                "sintoma2 TEXT NOT NULL," +
                "sintoma3 TEXT NOT NULL," +
                "sintoma4 TEXT NOT NULL," +
                "sintoma5 TEXT NOT NULL," +
                "sintoma6 TEXT NOT NULL," +
                "habito1 TEXT NOT NULL," +
                "habito2 TEXT NOT NULL," +
                "prevencion1 TEXT," +
                "prevencion2 TEXT NOT NULL," +
                "nivel_dolor INT NOT NULL," +
                "created_at TIMESTAMP NOT NULL DEFAULT NOW()," +
                "updated_at TIMESTAMP NOT NULL DEFAULT NOW()" +
                ")";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement ps = (conn != null) ? conn.prepareStatement(ddl) : null) {
            if (ps != null) {
                ps.execute();
            }
            migrateLegacySchema(conn);
        } catch (SQLException e) {
            System.err.println("❌ EncuestaDAO.ensureTable: " + e.getMessage());
        }
    }

    public static boolean existeParaCorreo(String correoUsuario) {
        if (correoUsuario == null || correoUsuario.trim().isEmpty()) {
            return false;
        }

        ensureTable();

        String sql = "SELECT 1 FROM encuesta WHERE correo_usuario = ? LIMIT 1";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement ps = (conn != null) ? conn.prepareStatement(sql) : null) {

            if (ps == null) {
                return false;
            }

            ps.setString(1, correoUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.err.println("❌ EncuestaDAO.existeParaCorreo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Inserta o actualiza (upsert) la encuesta diagnóstica por correo_usuario.
     * Retorna true si la operación se ejecutó sin excepción.
     */
    public static boolean upsertEncuesta(
            String correoUsuario,
            String nombre,
            int edad,
            String manoDominante,
            String ocupacion,
            int horasComputador,
            String sintoma1,
            String sintoma2,
            String sintoma3,
            String sintoma4,
            String sintoma5,
            String sintoma6,
            String habito1,
            String habito2,
            String prevencion1,
            String prevencion2,
            int nivelDolor
    ) {
        if (correoUsuario == null || correoUsuario.trim().isEmpty()) {
            return false;
        }

        ensureTable();

        String sql = "INSERT INTO encuesta (correo_usuario, nombre, edad, mano_dominante, ocupacion, horas_computador, " +
                "sintoma1, sintoma2, sintoma3, sintoma4, sintoma5, sintoma6, habito1, habito2, prevencion1, prevencion2, nivel_dolor, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW()) " +
                "ON CONFLICT (correo_usuario) DO UPDATE SET " +
                "nombre = EXCLUDED.nombre, " +
                "edad = EXCLUDED.edad, " +
                "mano_dominante = EXCLUDED.mano_dominante, " +
                "ocupacion = EXCLUDED.ocupacion, " +
                "horas_computador = EXCLUDED.horas_computador, " +
                "sintoma1 = EXCLUDED.sintoma1, " +
                "sintoma2 = EXCLUDED.sintoma2, " +
                "sintoma3 = EXCLUDED.sintoma3, " +
                "sintoma4 = EXCLUDED.sintoma4, " +
                "sintoma5 = EXCLUDED.sintoma5, " +
                "sintoma6 = EXCLUDED.sintoma6, " +
                "habito1 = EXCLUDED.habito1, " +
                "habito2 = EXCLUDED.habito2, " +
                "prevencion1 = EXCLUDED.prevencion1, " +
                "prevencion2 = EXCLUDED.prevencion2, " +
                "nivel_dolor = EXCLUDED.nivel_dolor, " +
                "updated_at = NOW()";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement ps = (conn != null) ? conn.prepareStatement(sql) : null) {

            if (ps == null) {
                return false;
            }

            ps.setString(1, correoUsuario);
            ps.setString(2, nombre);
            ps.setInt(3, edad);
            ps.setString(4, manoDominante);
            ps.setString(5, ocupacion);
            ps.setInt(6, horasComputador);
            ps.setString(7, sintoma1);
            ps.setString(8, sintoma2);
            ps.setString(9, sintoma3);
            ps.setString(10, sintoma4);
            ps.setString(11, sintoma5);
            ps.setString(12, sintoma6);
            ps.setString(13, habito1);
            ps.setString(14, habito2);
            ps.setString(15, prevencion1);
            ps.setString(16, prevencion2);
            ps.setInt(17, nivelDolor);

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("❌ EncuestaDAO.upsertEncuesta: " + e.getMessage());
            return false;
        }
    }
}
