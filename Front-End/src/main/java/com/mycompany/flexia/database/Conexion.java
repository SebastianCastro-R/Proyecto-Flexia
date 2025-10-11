package com.mycompany.flexia.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:postgresql://localhost:5432/Flexia";
    private static final String USER = "postgres"; // cambia por tu usuario de PostgreSQL
    private static final String PASSWORD = "Univalle"; // cambia por tu contraseña

    private static Connection conn = null;

    public static Connection getConnection() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Conexión exitosa a PostgreSQL");
            } catch (SQLException e) {
                System.err.println("❌ Error al conectar a PostgreSQL: " + e.getMessage());
            }
        }
        return conn;
    }

    public static void cerrarConexion() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("🔒 Conexión cerrada");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al cerrar la conexión: " + e.getMessage());
        }
    }
}
