package componentes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class PieChartPanel extends JPanel {

    private String title = "";
    private long ok = 0;
    private long reset = 0;

    private Color okColor = new Color(76, 175, 80);
    private Color resetColor = new Color(244, 67, 54);

    public PieChartPanel() {
        setOpaque(false);
    }

    public void setTitle(String title) {
        this.title = title != null ? title : "";
        repaint();
    }

    public void setData(long ok, long reset) {
        this.ok = Math.max(0, ok);
        this.reset = Math.max(0, reset);
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

            // title
            if (!title.isEmpty()) {
                g2d.setColor(new Color(30, 56, 136));
                g2d.setFont(new Font("Epunda Slab Medium", Font.PLAIN, 14));
                g2d.drawString(title, 10, 18);
            }

            long total = ok + reset;
            if (total <= 0) {
                g2d.setColor(new Color(140, 140, 140));
                g2d.setFont(new Font("Lato", Font.PLAIN, 12));
                g2d.drawString("Sin datos", 10, 40);
                return;
            }

            int padTop = 28;
            int pad = 16;
            int diameter = Math.min(w - pad * 2, h - padTop - pad * 2);
            diameter = Math.max(10, diameter);

            int cx = (w - diameter) / 2;
            int cy = padTop + (h - padTop - diameter) / 2;

            double okAngle = (double) ok / (double) total * 360.0;
            double resetAngle = 360.0 - okAngle;

            // Pie
            g2d.setColor(okColor);
            g2d.fillArc(cx, cy, diameter, diameter, 90, (int) Math.round(-okAngle));

            g2d.setColor(resetColor);
            g2d.fillArc(cx, cy, diameter, diameter, 90 - (int) Math.round(okAngle), (int) Math.round(-resetAngle));

            // border
            g2d.setColor(new Color(220, 220, 220));
            g2d.drawOval(cx, cy, diameter, diameter);

            // legend
            g2d.setFont(new Font("Lato", Font.PLAIN, 12));
            int lx = 10;
            int ly = h - 18;

            g2d.setColor(okColor);
            g2d.fillRect(lx, ly - 10, 10, 10);
            g2d.setColor(new Color(30, 56, 136));
            g2d.drawString("OK: " + ok, lx + 14, ly);

            int lx2 = lx + 90;
            g2d.setColor(resetColor);
            g2d.fillRect(lx2, ly - 10, 10, 10);
            g2d.setColor(new Color(30, 56, 136));
            g2d.drawString("Reset: " + reset, lx2 + 14, ly);

        } finally {
            g2d.dispose();
        }
    }
}
