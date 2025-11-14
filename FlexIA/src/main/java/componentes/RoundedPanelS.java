package componentes;
import javax.swing.*;
import java.awt.*;

public class RoundedPanelS extends JPanel {
    private final int cornerRadius;

    public RoundedPanelS(int radius, Color bgColor) {
        this.cornerRadius = radius;
        setBackground(bgColor);
        setOpaque(false); // importante para permitir transparencia en las esquinas
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        g2.dispose();
    }
}
