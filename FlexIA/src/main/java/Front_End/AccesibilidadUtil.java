package Front_End;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;
import javax.swing.SwingUtilities;

public class AccesibilidadUtil {

    private static final Map<Component, Font> originalFonts = new HashMap<>();
    private static final Map<Component, Color> originalForegrounds = new HashMap<>();
    private static final Map<Component, Color> originalBackgrounds = new HashMap<>();
    
    private static float scalingFactor = 1.0f;
    private static boolean isHighContrast = false; // Nuevo estado de contraste

    // --- Colores para Alto Contraste (Ejemplo: Esquema Oscuro) ---
    private static final Color HC_FOREGROUND = Color.WHITE;
    private static final Color HC_BACKGROUND = Color.BLACK;
    private static final Color HC_ACCENT = Color.YELLOW; // Color para botones, campos de texto, etc.

    /**
     * Devuelve el factor de escalado de fuente actual.
     * @return Factor actual (ej. 1.2f para 120%)
     */
    public static float getScalingFactor() {
        return scalingFactor;
    }
    
    /**
     * Devuelve el estado actual del modo de alto contraste.
     * @return true si el alto contraste está activo.
     */
    public static boolean isHighContrastActive() {
        return isHighContrast;
    }

    /**
     * Aplica el factor de escalado a todos los componentes dentro del contenedor raíz.
     * ... (El resto del método scaleFont sigue igual)
     */
    public static void scaleFont(Container root, float factor) {
        scalingFactor = factor;
        applyFontRecursively(root);
        SwingUtilities.updateComponentTreeUI(root);
        root.revalidate();
        root.repaint();
    }
    
    /**
     * Aplica o desactiva el modo de alto contraste.
     * @param root El contenedor principal (JFrame).
     * @param activate true para activar el alto contraste, false para desactivar.
     */
    public static void applyHighContrast(Container root, boolean activate) {
        isHighContrast = activate;
        applyContrastRecursively(root, activate);
        
        // Es necesario actualizar la UI para que los cambios del L&F surtan efecto
        SwingUtilities.updateComponentTreeUI(root); 
        root.revalidate();
        root.repaint();
    }
    
    /**
     * Método privado recursivo para cambiar la fuente
     * ... (applyFontRecursively sigue igual)
     */
    private static void applyFontRecursively(Component component) {
        if (component == null) return;

        // Guardar fuente original solo una vez
        originalFonts.putIfAbsent(component, component.getFont());

        Font baseFont = originalFonts.get(component);
        if (baseFont != null) {
            float newSize = baseFont.getSize2D() * scalingFactor;
            component.setFont(baseFont.deriveFont(newSize));
        }

        if (component instanceof Container container) {
            for (Component child : container.getComponents()) {
                applyFontRecursively(child);
            }
        }
    }


    /**
     * Método privado recursivo para aplicar o restablecer colores de contraste.
     */
    private static void applyContrastRecursively(Component component, boolean activate) {
        if (component == null) return;

        // 1. Guardar colores originales (solo si no se ha hecho antes)
        originalForegrounds.putIfAbsent(component, component.getForeground());
        originalBackgrounds.putIfAbsent(component, component.getBackground());
        
        // Obtener colores base para la operación
        Color originalFg = originalForegrounds.get(component);
        Color originalBg = originalBackgrounds.get(component);

        if (activate) {
            // --- Aplicar Alto Contraste ---
            
            // Texto general (JLabel, etc.)
            component.setForeground(HC_FOREGROUND);
            
            // Fondo general (JPanel)
            component.setBackground(HC_BACKGROUND);
            
            // Componentes interactivos (JButton, JTextField, JComboBox) 
            // Esto es opcional y depende de tu diseño, a veces es mejor un color de acento
            String componentClassName = component.getClass().getSimpleName();
            if (componentClassName.equals("JButton") || 
                componentClassName.equals("JTextField") || 
                componentClassName.equals("JComboBox")) {
                
                // Cambiamos el fondo para que resalten más.
                component.setBackground(HC_ACCENT); 
                component.setForeground(Color.BLACK); // Para asegurar contraste en los componentes
            }

            // Opcional: Deshabilitar el componente si está deshabilitado para que siga viéndose diferente
            if (!component.isEnabled()) {
                 component.setForeground(Color.GRAY);
                 component.setBackground(HC_BACKGROUND);
            }

        } else {
            // --- Restablecer Contraste ---
            if (originalFg != null) {
                component.setForeground(originalFg);
            }
            if (originalBg != null) {
                // Algunos componentes como JLabel tienen fondo transparente por defecto, 
                // así que solo cambiamos el fondo si el original era diferente a null
                if (originalBg != null) { 
                    component.setBackground(originalBg);
                }
            }
        }

        // Recorrer hijos
        if (component instanceof Container container) {
            for (Component child : container.getComponents()) {
                applyContrastRecursively(child, activate);
            }
        }
    }
}