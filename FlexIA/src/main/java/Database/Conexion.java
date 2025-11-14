package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:postgresql://localhost:5432/Flexia";
    private static final String USER = "postgres";
    private static final String PASSWORD = "2459";

    // ✅ Retorna una conexión nueva cada vez
    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            // System.out.println("✅ Conexión abierta a PostgreSQL");
            return conn;
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar a PostgreSQL: " + e.getMessage());
            return null;
        }
    }

    // Este método ya no es necesario, pero si lo dejas, no afectará
    public static void cerrarConexion(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                // System.out.println("🔒 Conexión cerrada");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al cerrar la conexión: " + e.getMessage());
        }
    }
}
