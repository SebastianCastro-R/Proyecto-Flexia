package Back_End;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SesionUsuario {
    private static SesionUsuario instancia;
    private String correoUsuario;
    private String nombreUsuario;
    private Usuario usuarioActual;

    private List<Consumer<Boolean>> premiumListeners = new ArrayList<>();
    
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
    
    public void iniciarSesion(Usuario usuario) {
        this.usuarioActual = usuario;
        this.correoUsuario = usuario.getCorreo();
        this.nombreUsuario = usuario.getNombres();
    }

    public void cerrarSesion() {
        this.correoUsuario = null;
        this.nombreUsuario = null;
        this.usuarioActual = null;
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

    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
    }
    
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioPremium(boolean esPremium) {
        if (usuarioActual != null) {
            usuarioActual.setEsPremium(esPremium);
            // notificar a todos los listeners
            for (Consumer<Boolean> listener : premiumListeners) {
                listener.accept(esPremium);
            }
        }
    }

    public void agregarPremiumListener(Consumer<Boolean> listener) {
        premiumListeners.add(listener);
    }

    public void removerPremiumListener(Consumer<Boolean> listener) {
        premiumListeners.remove(listener);
    }
}