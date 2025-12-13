package componentes;

import javax.swing.*;
import java.awt.*;

/**
 * Gráfica tipo anillo (donut) para mostrar la relación entre dos valores.
 * Pensada para: Correctos vs Incorrectos.
 */
public class DonutChartPanel extends JPanel {

    private String title = "";
    private String labelA = "Correctos";
    private String labelB = "Incorrectos";

    private long a = 0;
    private long b = 0;

    private Color colorA = new Color(76, 175, 80);
    private Color colorB = new Color(244, 67, 54);

    private Color textColor = new Color(30, 56, 136);

    public DonutChartPanel() {
        setOpaque(false);
    }

    public void setTitle(String title) {
        this.title = title != null ? title : "";
        repaint();
    }

    public void setLegendLabels(String aLabel, String bLabel) {
        if (aLabel != null && !aLabel.trim().isEmpty()) {
            this.labelA = aLabel.trim();
        }
        if (bLabel != null && !bLabel.trim().isEmpty()) {
            this.labelB = bLabel.trim();
        }
        repaint();
    }

    public void setData(long a, long b) {
        this.a = Math.max(0, a);
        this.b = Math.max(0, b);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        try {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            int pad = 16;
            int legendW = 160;
            int captionH = 34;

            int availableW = Math.max(10, w - pad * 2 - legendW);
            int availableH = Math.max(10, h - pad * 2 - captionH);

            int diameter = Math.max(10, Math.min(availableW, availableH));
            int ringThickness = Math.max(10, Math.min(20, diameter / 7));

            int cx = pad + (availableW - diameter) / 2;
            int cy = pad + (availableH - diameter) / 2;

            long total = a + b;

            // Fondo (anillo gris)
            g2d.setStroke(new BasicStroke(ringThickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.setColor(new Color(220, 220, 220));
            g2d.drawArc(cx, cy, diameter, diameter, 0, 360);

            if (total > 0) {
                double aAngle = (double) a / (double) total * 360.0;
                double bAngle = 360.0 - aAngle;

                // Inicio en 90° para que arranque arriba (como el ejemplo)
                int start = 90;

                g2d.setColor(colorA);
                g2d.drawArc(cx, cy, diameter, diameter, start, (int) Math.round(-aAngle));

                g2d.setColor(colorB);
                g2d.drawArc(cx, cy, diameter, diameter, start - (int) Math.round(aAngle), (int) Math.round(-bAngle));
            }

            // Texto central (porcentaje)
            g2d.setColor(textColor);
            String centerText;
            if (total <= 0) {
                centerText = "0%";
            } else {
                int pct = (int) Math.round(((double) a / (double) total) * 100.0);
                centerText = pct + "%";
            }

            Font centerFont = new Font("Epunda Slab ExtraBold", Font.PLAIN, Math.max(16, diameter / 6));
            g2d.setFont(centerFont);
            FontMetrics fm = g2d.getFontMetrics();
            int tx = cx + (diameter - fm.stringWidth(centerText)) / 2;
            int ty = cy + (diameter + fm.getAscent() - fm.getDescent()) / 2;
            g2d.drawString(centerText, tx, ty);

            // Leyenda a la derecha
            int lx = w - legendW + 10;
            int ly = cy + diameter / 3;
            int lineLen = 28;

            g2d.setFont(new Font("Epunda Slab Medium", Font.PLAIN, 14));

            // Correctos
            g2d.setColor(colorA);
            g2d.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.drawLine(lx, ly, lx + lineLen, ly);
            g2d.setColor(textColor);
            g2d.drawString(labelA + " (" + a + ")", lx + lineLen + 10, ly + 5);

            // Incorrectos
            int ly2 = ly + 34;
            g2d.setColor(colorB);
            g2d.drawLine(lx, ly2, lx + lineLen, ly2);
            g2d.setColor(textColor);
            g2d.drawString(labelB + " (" + b + ")", lx + lineLen + 10, ly2 + 5);

            // Título inferior centrado
            if (!title.isEmpty()) {
                g2d.setFont(new Font("Epunda Slab Medium", Font.PLAIN, 24));
                FontMetrics ft = g2d.getFontMetrics();
                int sx = (w - ft.stringWidth(title)) / 2;
                int sy = h - 12;
                g2d.drawString(title, sx, sy);
            }

        } finally {
            g2d.dispose();
        }
    }
}
