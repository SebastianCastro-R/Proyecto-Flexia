// LeccionesDAO.java
package com.mycompany.flexia.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LeccionesDAO {
    
    public static class Leccion {
        private int idLeccion;
        private String titulo;
        private String descripcion;
        private List<VideosDAO.Video> videos;
        
        public Leccion(int idLeccion, String titulo, String descripcion) {
            this.idLeccion = idLeccion;
            this.titulo = titulo;
            this.descripcion = descripcion;
            this.videos = new ArrayList<>();
        }
        
        // Getters y Setters
        public int getIdLeccion() { return idLeccion; }
        public String getTitulo() { return titulo; }
        public String getDescripcion() { return descripcion; }
        public List<VideosDAO.Video> getVideos() { return videos; }
        public void setVideos(List<VideosDAO.Video> videos) { this.videos = videos; }
    }
    
    // Obtener todas las lecciones con sus videos
    public List<Leccion> obtenerTodasLasLeccionesConVideos() {
        List<Leccion> lecciones = new ArrayList<>();
        String sql = "SELECT l.id_leccion, l.titulo, l.descripcion, " +
                    "v.id_video, v.miniatura, v.titulo as video_titulo, v.tipo, " +
                    "v.descripcion as video_descripcion, v.archivo, v.duracion, " +
                    "v.nivel_dificultad, v.instrucciones " +
                    "FROM lecciones l " +
                    "LEFT JOIN leccion_video lv ON l.id_leccion = lv.id_leccion " +
                    "LEFT JOIN videos v ON lv.id_video = v.id_video " +
                    "ORDER BY l.id_leccion, v.id_video";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            Leccion leccionActual = null;
            int leccionAnteriorId = -1;
            
            while (rs.next()) {
                int leccionId = rs.getInt("id_leccion");
                
                // Si es una nueva lección
                if (leccionId != leccionAnteriorId) {
                    if (leccionActual != null) {
                        lecciones.add(leccionActual);
                    }
                    leccionActual = new Leccion(
                        leccionId,
                        rs.getString("titulo"),
                        rs.getString("descripcion")
                    );
                    leccionAnteriorId = leccionId;
                }
                
                // Agregar video si existe
                if (rs.getInt("id_video") != 0 && leccionActual != null) {
                    VideosDAO.Video video = new VideosDAO.Video(
                        rs.getInt("id_video"),
                        rs.getString("miniatura"),
                        rs.getString("video_titulo"),
                        rs.getString("tipo"),
                        rs.getString("video_descripcion"),
                        rs.getString("archivo"),
                        rs.getString("duracion"),
                        rs.getString("nivel_dificultad"),
                        rs.getString("instrucciones")
                    );
                    leccionActual.getVideos().add(video);
                }
            }
            
            // Agregar la última lección
            if (leccionActual != null) {
                lecciones.add(leccionActual);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener lecciones con videos: " + e.getMessage());
        }
        return lecciones;
    }
}
