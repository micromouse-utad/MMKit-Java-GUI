package pt.globaltronic.microMouseGUI.models.graphics.Graphics3D;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

public class PolygonObject {
    private Screen screen;
    private Polygon P;
    private Color c;
    private boolean draw = true;
    private boolean visible = true;
    private boolean seeThrough;
    private double lighting = 1;

    public PolygonObject(Screen screen, double[] x, double[] y, Color c, int n, boolean seeThrough) {
        this.screen = screen;
        P = new Polygon();
        for (int i = 0; i < x.length; i++)
            P.addPoint((int) x[i], (int) y[i]);
        this.c = c;
        this.seeThrough = seeThrough;
    }

    void updatePolygon(double[] x, double[] y) {
        P.reset();
        for (int i = 0; i < x.length; i++) {
            P.xpoints[i] = (int) x[i];
            P.ypoints[i] = (int) y[i];
            P.npoints = x.length;
        }
    }

    void drawPolygon(Graphics g) {
        if (draw && visible) {
            g.setColor(new Color((int) (c.getRed() * lighting), (int) (c.getGreen() * lighting), (int) (c.getBlue() * lighting)));
            if (seeThrough) {
                g.drawPolygon(P);
            } else {
                g.fillPolygon(P);
            }
            if (screen.isOutLined()) {
                g.setColor(new Color(0, 0, 0));
                g.drawPolygon(P);
            }
        }
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public double getLighting() {
        return lighting;
    }

    public void setLighting(double lighting) {
        this.lighting = lighting;
    }

    public boolean isDraw() {
        return draw;
    }

    public void setDraw(boolean draw) {
        this.draw = draw;
    }
}
