package com.mycompany.flexia.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VideosDAO {
    
public static class Video {
    private int idVideo;
    private String miniatura; // Agregar este campo
    private String titulo;
    private String tipo;
    private String descripcion;
    private String archivo;
    private String duracion;
    private String nivelDificultad;
    private String instrucciones;
    
    public Video(int idVideo, String miniatura, String titulo, String tipo, String descripcion, 
                String archivo, String duracion, String nivelDificultad, 
                String instrucciones) {
        this.idVideo = idVideo;
        this.miniatura = miniatura;
        this.titulo = titulo;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.archivo = archivo;
        this.duracion = duracion;
        this.nivelDificultad = nivelDificultad;
        this.instrucciones = instrucciones;
    }
    
    // Getters
    public int getIdVideo() { return idVideo; }
    public String getMiniatura() { return miniatura; } // Nuevo getter
    public String getTitulo() { return titulo; }
    public String getTipo() { return tipo; }
    public String getDescripcion() { return descripcion; }
    public String getArchivo() { return archivo; }
    public String getDuracion() { return duracion; }
    public String getNivelDificultad() { return nivelDificultad; }
    public String getInstrucciones() { return instrucciones; }
}

// Actualizar las consultas SQL para incluir miniatura
    public List<Video> obtenerTodosLosVideos() {
        List<Video> videos = new ArrayList<>();
        String sql = "SELECT id_video, miniatura, titulo, tipo, descripcion, archivo, duracion, nivel_dificultad, instrucciones FROM videos";
        
        try (Connection conn = Conexion.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Video video = new Video(
                    rs.getInt("id_video"),
                    rs.getString("miniatura"), // Nuevo campo
                    rs.getString("titulo"),
                    rs.getString("tipo"),
                    rs.getString("descripcion"),
                    rs.getString("archivo"),
                    rs.getString("duracion"),
                    rs.getString("nivel_dificultad"),
                    rs.getString("instrucciones")
                );
                videos.add(video);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener videos: " + e.getMessage());
        }
        return videos;
    }
    
    // Obtener video por título
    public Video obtenerVideoPorTitulo(String titulo) {
        String sql = "SELECT id_video, miniatura, titulo, tipo, descripcion, archivo, duracion, nivel_dificultad, instrucciones FROM videos WHERE titulo = ?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, titulo);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Video(
                    rs.getInt("id_video"),
                    rs.getString("titulo"),
                    rs.getString("miniatura"),
                    rs.getString("tipo"),
                    rs.getString("descripcion"),
                    rs.getString("archivo"),
                    rs.getString("duracion"),
                    rs.getString("nivel_dificultad"),
                    rs.getString("instrucciones")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener video: " + e.getMessage());
        }
        return null;
    }
    
    // Obtener video por ID
    public Video obtenerVideoPorId(int idVideo) {
        String sql = "SELECT id_video, miniatura, titulo, tipo, descripcion, archivo, duracion, nivel_dificultad, instrucciones FROM videos WHERE id_video = ?";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idVideo);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Video(
                    rs.getInt("id_video"),
                    rs.getString("titulo"),
                    rs.getString("miniatura"),
                    rs.getString("tipo"),
                    rs.getString("descripcion"),
                    rs.getString("archivo"),
                    rs.getString("duracion"),
                    rs.getString("nivel_dificultad"),
                    rs.getString("instrucciones")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener video: " + e.getMessage());
        }
        return null;
    }
}