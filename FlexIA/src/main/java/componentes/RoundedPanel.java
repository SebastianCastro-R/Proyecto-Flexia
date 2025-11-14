/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package componentes;

/**
 *
 * @author Karol
 */
import java.awt.*;
import java.awt.geom.Path2D;
import javax.swing.*;
import java.beans.*;

/**
 * Panel con bordes redondeados configurables individualmente.
 * Compatible con el diseñador de NetBeans (Matisse).
 */
public class RoundedPanel extends JPanel {

    private int arc = 20;
    private boolean leftRounded = true;
    private boolean rightRounded = true;

    public RoundedPanel() {
        setOpaque(false);
    }

    // === PROPIEDADES CON SOPORTE PARA NETBEANS ===

    @BeanProperty(preferred = true, description = "Radio de redondeo de las esquinas")
    public int getArc() {
        return arc;
    }

    public void setArc(int arc) {
        int old = this.arc;
        this.arc = arc;
        firePropertyChange("arc", old, arc);
        repaint();
    }

    @BeanProperty(preferred = true, description = "Redondear esquinas izquierdas (superior e inferior)")
    public boolean isLeftRounded() {
        return leftRounded;
    }

    public void setLeftRounded(boolean leftRounded) {
        boolean old = this.leftRounded;
        this.leftRounded = leftRounded;
        firePropertyChange("leftRounded", old, leftRounded);
        repaint();
    }

    @BeanProperty(preferred = true, description = "Redondear esquinas derechas (superior e inferior)")
    public boolean isRightRounded() {
        return rightRounded;
    }

    public void setRightRounded(boolean rightRounded) {
        boolean old = this.rightRounded;
        this.rightRounded = rightRounded;
        firePropertyChange("rightRounded", old, rightRounded);
        repaint();
    }

    // === DIBUJO ===

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Shape shape = createRoundedShape(width, height);
        g2.setColor(getBackground());
        g2.fill(shape);

        g2.dispose();
    }

    private Shape createRoundedShape(int width, int height) {
        int r = arc;
        Path2D path = new Path2D.Double();

        path.moveTo(leftRounded ? r : 0, 0);
        path.lineTo(width - (rightRounded ? r : 0), 0);
        if (rightRounded) path.quadTo(width, 0, width, r);

        path.lineTo(width, height - (rightRounded ? r : 0));
        if (rightRounded) path.quadTo(width, height, width - r, height);

        path.lineTo(leftRounded ? r : 0, height);
        if (leftRounded) path.quadTo(0, height, 0, height - r);

        path.lineTo(0, leftRounded ? r : 0);
        if (leftRounded) path.quadTo(0, 0, r, 0);

        path.closePath();
        return path;
    }
}

