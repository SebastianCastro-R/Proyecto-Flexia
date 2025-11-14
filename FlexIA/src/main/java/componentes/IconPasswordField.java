package componentes;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.*;
import javax.swing.*;
import java.beans.BeanProperty;

public class IconPasswordField extends JPasswordField {

    private Icon icon;
    private String placeholder = "";
    private int arc = 40;
    private Color borderColor = new Color(180, 180, 180);
    private int iconGap = 10;

    public IconPasswordField() {
        setFont(new Font("Lato", Font.PLAIN, 16));
        setOpaque(false);
        updateBorder();
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);

        putClientProperty("JComponent.roundRect", true);
        putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeholder);
    }

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
        updateBorder();
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

    public int getArc() {
        return arc;
    }

    public void setArc(int arc) {
        this.arc = arc;
        repaint();
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        repaint();
    }

    public int getIconGap() {
        return iconGap;
    }

    public void setIconGap(int iconGap) {
        this.iconGap = iconGap;
        updateBorder();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, arc, arc);

        g2.dispose();
        super.paintComponent(g);

        if (icon != null) {
            int iconX = 10;
            int iconY = (getHeight() - icon.getIconHeight()) / 2;
            icon.paintIcon(this, g, iconX, iconY);
        }
    }

    @Override
    protected void paintBorder(Graphics g) {
        // Evitar borde por defecto
    }
}
