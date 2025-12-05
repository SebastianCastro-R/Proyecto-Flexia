package Back_End.Ads;

import javax.swing.*;
import java.util.*;

public class AdManager {
    private static AdManager instance;
    private javax.swing.Timer adTimer;
    private int adDelaySeconds = 5;
    private int adIntervalSeconds = 60;
    private boolean isPremiumUser = false;
    private Map<String, JFrame> activeWindows;
    private List<AdContent> adContents;
    private AdListener listener;
    private int adCounter = 0;
    
    private AdManager() {
        activeWindows = new HashMap<>();
        loadAdContents();
        setupPremiumListener();
    }
    
    public static AdManager getInstance() {
        if (instance == null) {
            instance = new AdManager();
        }
        return instance;
    }
    
    private void loadAdContents() {
        adContents = new ArrayList<>();
        
        adContents.add(new AdContent(
            "Mejora tu postura",
            "Descubre ejercicios avanzados para evitar el túnel carpiano",
            "/icons/images/Carpianin2.png",
            "https://flexia.com/ejercicios-avanzados",
            "all"
        ));
        
        adContents.add(new AdContent(
            "Premium FlexIA",
            "Elimina anuncios y desbloquea contenido exclusivo",
            "/icons/images/Carpianin2.png",
            "premium://upgrade",
            "all"
        ));
        
        adContents.add(new AdContent(
            "Kit de ergonomía",
            "Productos recomendados para tu espacio de trabajo",
            "/icons/images/Carpianin2.png",
            "https://flexia.com/recomendaciones",
            "home"
        ));
    }
    
    private void setupPremiumListener() {
        try {
            // Usar reflexión para evitar dependencia directa
            Class<?> sesionClass = Class.forName("Back_End.SesionUsuario");
            Object sesion = sesionClass.getMethod("getInstancia").invoke(null);
            
            // Crear un Consumer dinámico
            java.util.function.Consumer<Boolean> premiumConsumer = esPremium -> {
                this.isPremiumUser = esPremium;
                if (esPremium) {
                    stopAllAds();
                    System.out.println("✅ Usuario premium - Anuncios desactivados");
                }
            };
            
            // Agregar listener usando reflexión
            sesionClass.getMethod("agregarPremiumListener", java.util.function.Consumer.class)
                      .invoke(sesion, premiumConsumer);
            
        } catch (Exception e) {
            System.err.println("⚠️ Error configurando listener premium: " + e.getMessage());
        }
    }
    
    public void registerWindow(String windowName, JFrame window) {
        activeWindows.put(windowName, window);
        
        // Verificar si es usuario premium usando reflexión
        try {
            Class<?> sesionClass = Class.forName("Back_End.SesionUsuario");
            Object sesion = sesionClass.getMethod("getInstancia").invoke(null);
            Object usuario = sesionClass.getMethod("getUsuarioActual").invoke(sesion);
            
            if (usuario != null) {
                Boolean esPremium = (Boolean) usuario.getClass().getMethod("isEsPremium").invoke(usuario);
                this.isPremiumUser = esPremium;
            }
        } catch (Exception e) {
            System.err.println("Error verificando estado premium: " + e.getMessage());
        }
        
        // Solo programar anuncios si NO es premium
        if (!isPremiumUser && shouldShowAdsOnWindow(windowName)) {
            scheduleAdForWindow(windowName, window);
        }
    }
    
    private boolean shouldShowAdsOnWindow(String windowName) {
        List<String> allowedWindows = Arrays.asList("home", "ejercicios", "ejercicio", "contactanos", "instrucciones");
        return allowedWindows.contains(windowName.toLowerCase());
    }
    
    private void scheduleAdForWindow(String windowName, JFrame window) {
        javax.swing.Timer windowTimer = new javax.swing.Timer(adDelaySeconds * 1000, e -> {
            if (!isPremiumUser && window.isVisible()) {
                showPopupAd(window);
                adCounter++;
            }
        });
        windowTimer.setRepeats(false);
        windowTimer.start();
        
        // Timer para mostrar periódicamente
        javax.swing.Timer intervalTimer = new javax.swing.Timer(adIntervalSeconds * 1000, e -> {
            if (!isPremiumUser && window.isVisible() && adCounter < 4) {
                showPopupAd(window);
                adCounter++;
            }
        });
        intervalTimer.start();
    }
    
    public void showPopupAd(JFrame parentFrame) {
        if (isPremiumUser) return;
        
        SwingUtilities.invokeLater(() -> {
            AdContent ad = getRandomAd();
            AdPopupDialog dialog = new AdPopupDialog(parentFrame, ad);
            dialog.setAdListener(new AdListener() {
                @Override
                public void onAdShown(String adType) {
                    System.out.println("Anuncio mostrado: " + ad.getTitle());
                }
                
                @Override
                public void onAdClosed(String adType) {
                    System.out.println("Anuncio cerrado");
                }
                
                @Override
                public void onAdClicked(String adType) {
                    System.out.println("Anuncio clickeado: " + ad.getTitle());
                }
                
                @Override
                public void onPremiumUpgradeClicked() {
                    openPremiumUpgrade(parentFrame);
                }
            });
            dialog.setVisible(true);
        });
    }
    
    private AdContent getRandomAd() {
        Random rand = new Random();
        return adContents.get(rand.nextInt(adContents.size()));
    }
    
    private void openPremiumUpgrade(JFrame parentFrame) {
        // Cerrar el anuncio primero
        parentFrame.requestFocus();
        
        // Mostrar mensaje instructivo
        JOptionPane.showMessageDialog(parentFrame,
            "<html><body style='width:300px;'>" +
            "<h3>Actualizar a Premium</h3>" +
            "<p>Para actualizar tu cuenta a Premium:</p>" +
            "<ol>" +
            "<li>Ve a <b>Mi Perfil</b> en el menú lateral</li>" +
            "<li>Busca el boton <b>Convietete en premium</b></li>" +
            "<li>Le das click y te redirecciona al portal de pagos</li>" +
            "</ol>" +
            "<p>¡Disfruta de FlexIA sin anuncios!</p>" +
            "</body></html>",
            "Cómo actualizar a Premium",
            JOptionPane.INFORMATION_MESSAGE);
        
        // Intentar abrir Perfil si existe
        try {
            // Verificar si el menú lateral puede navegar al perfil
            if (parentFrame instanceof Front_End.Home) {
                Front_End.Home home = (Front_End.Home) parentFrame;
            }
            
        } catch (Exception e) {
            // Ignorar errores
            
        }
    }
    
    public void stopAllAds() {
        if (adTimer != null) {
            adTimer.stop();
        }
        activeWindows.clear();
    }
    
    public void setAdListener(AdListener listener) {
        this.listener = listener;
    }
    
    public boolean isUserPremium() {
        return isPremiumUser;
    }
}