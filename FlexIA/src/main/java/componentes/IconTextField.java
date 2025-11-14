/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package componentes; // cambia al paquete que uses

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import java.awt.*;
import java.beans.BeanProperty;

public class IconTextField extends JTextField {

    private Icon icon;
    private String placeholder = "";
    private int arc = 40;
    private Color borderColor = new Color(180, 180, 180);
    private int iconGap = 10; // Nueva propiedad para el gap del icono

    public IconTextField() {
        setFont(new Font("Lato", Font.PLAIN, 16));
        setOpaque(false);
        updateBorder(); // Usar método para actualizar el border
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);

        // Propiedades FlatLaf
        putClientProperty("JComponent.roundRect", true);
        putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeholder);
    }

    // Método para actualizar el border según el gap del icono
    private void updateBorder() {
        int leftPadding = (icon != null) ? 30 + iconGap : 10;
        setBorder(BorderFactory.createEmptyBorder(5, leftPadding, 5, 10));
    }

    @BeanProperty(preferred = true, description = "Icono mostrado a la izquierda del campo")
    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
        updateBorder(); // Actualizar border cuando cambia el icono
        repaint();
    }

    @BeanProperty(preferred = true, description = "Texto placeholder mostrado cuando está vacío")
    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeholder);
    }

    @BeanProperty(preferred = true, description = "Radio de redondeo del campo de texto")
    public int getArc() {
        return arc;
    }

    public void setArc(int arc) {
        this.arc = arc;
        repaint();
    }

    @BeanProperty(preferred = true, description = "Color del borde")
    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        repaint();
    }

    @BeanProperty(preferred = true, description = "Espacio entre el icono y el texto")
    public int getIconGap() {
        return iconGap;
    }

    public void setIconGap(int iconGap) {
        this.iconGap = iconGap;
        updateBorder(); // Actualizar border cuando cambia el gap
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibujar fondo redondeado
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

        // Dibujar borde redondeado de 2px
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, arc, arc);

        g2.dispose();

        // Llamar al paintComponent original para el texto
        super.paintComponent(g);

        // Dibujar el icono a la izquierda con el gap personalizado
        if (icon != null) {
            int iconX = 10; // Posición fija desde la izquierda
            int iconY = (getHeight() - icon.getIconHeight()) / 2;
            icon.paintIcon(this, g, iconX, iconY);
        }
    }

    @Override
    protected void paintBorder(Graphics g) {
        // No pintar borde por defecto, ya lo hacemos en paintComponent
    }
}
