package componentes;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class RoundedComboBox<E> extends JComboBox<E> {
    
    private int arc = 40;
    private Color borderColor = new Color(180, 180, 180);
    private Color backgroundColor = Color.WHITE;
    
    public RoundedComboBox() {
        setOpaque(false);
        setBackground(backgroundColor);
        setBorder(new EmptyBorder(5, 10, 5, 10));
        setFont(new Font("Lato", Font.PLAIN, 16));
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

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, arc, arc);

        g2.dispose();
        super.paintComponent(g);
    }
}
