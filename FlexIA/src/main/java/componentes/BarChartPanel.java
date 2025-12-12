package componentes;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BarChartPanel extends JPanel {

    public static class Point {
        public final LocalDate date;
        public final double value;

        public Point(LocalDate date, double value) {
            this.date = date;
            this.value = value;
        }
    }

    private final List<Point> points = new ArrayList<>();
    private String title = "";
    private Color barColor = new Color(152, 206, 255);

    public BarChartPanel() {
        setOpaque(false);
    }

    public void setTitle(String title) {
        this.title = title != null ? title : "";
        repaint();
    }

    public void setBarColor(Color barColor) {
        if (barColor != null) {
            this.barColor = barColor;
        }
        repaint();
    }

    public void setData(List<Point> newPoints) {
        points.clear();
        if (newPoints != null) {
            points.addAll(newPoints);
        }
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

            // padding
            int padTop = 28;
            int padBottom = 28;
            int padLeft = 36;
            int padRight = 16;

            // Title
            if (!title.isEmpty()) {
                g2d.setColor(new Color(30, 56, 136));
                g2d.setFont(new Font("Epunda Slab Medium", Font.PLAIN, 14));
                g2d.drawString(title, 10, 18);
            }

            int chartX = padLeft;
            int chartY = padTop;
            int chartW = Math.max(1, w - padLeft - padRight);
            int chartH = Math.max(1, h - padTop - padBottom);

            // Axis
            g2d.setColor(new Color(220, 220, 220));
            g2d.drawLine(chartX, chartY + chartH, chartX + chartW, chartY + chartH);

            if (points.isEmpty()) {
                g2d.setColor(new Color(140, 140, 140));
                g2d.setFont(new Font("Lato", Font.PLAIN, 12));
                g2d.drawString("Sin datos", chartX + 10, chartY + chartH / 2);
                return;
            }

            double max = 0;
            for (Point p : points) {
                max = Math.max(max, p.value);
            }
            if (max <= 0) {
                max = 1;
            }

            int n = points.size();
            int gap = 6;
            int barW = Math.max(6, (chartW - (gap * (n - 1))) / n);

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM");
            g2d.setFont(new Font("Lato", Font.PLAIN, 10));

            for (int i = 0; i < n; i++) {
                Point p = points.get(i);

                int x = chartX + i * (barW + gap);
                int barH = (int) Math.round((p.value / max) * chartH);
                int y = chartY + chartH - barH;

                // Bar
                g2d.setColor(barColor);
                g2d.fillRoundRect(x, y, barW, barH, 10, 10);

                // Value label (small)
                g2d.setColor(new Color(30, 56, 136));
                String v = String.valueOf((int) Math.round(p.value));
                int vw = g2d.getFontMetrics().stringWidth(v);
                g2d.drawString(v, x + (barW - vw) / 2, Math.max(12, y - 2));

                // Date label
                String d = (p.date != null) ? p.date.format(fmt) : "";
                int dw = g2d.getFontMetrics().stringWidth(d);
                g2d.setColor(new Color(120, 120, 120));
                g2d.drawString(d, x + (barW - dw) / 2, chartY + chartH + 16);
            }

        } finally {
            g2d.dispose();
        }
    }
}
