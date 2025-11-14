package Back_End;

public class SesionUsuario {
    private static SesionUsuario instancia;
    private String correoUsuario;
    private String nombreUsuario;
    
    private SesionUsuario() {}
    
    public static SesionUsuario getInstancia() {
        if (instancia == null) {
            instancia = new SesionUsuario();
        }
        return instancia;
    }
    
    public void iniciarSesion(String correo, String nombre) {
        this.correoUsuario = correo;
        this.nombreUsuario = nombre;
    }
    
    public void cerrarSesion() {
        this.correoUsuario = null;
        this.nombreUsuario = null;
    }
    
    public String getCorreoUsuario() {
        return correoUsuario;
    }
    
    public String getNombreUsuario() {
        return nombreUsuario;
    }
    
    public boolean estaLogueado() {
        return correoUsuario != null && !correoUsuario.isEmpty();
    }
}