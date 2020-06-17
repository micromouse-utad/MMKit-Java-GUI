package pt.globaltronic.microMouseGUI.models.graphics.Graphics3D;
import java.awt.Color;

public class Polygon2D {
    private Screen screen;
    private Calculator calculator;
    private Color c;
    private double[] x;
    private double[] y;
    private double[] z;
    private boolean draw = true;
    private boolean seeThrough;
    private boolean visible;
    private double[] CalcPos;
    private double[] newX;
    private double[] newY;
    private PolygonObject DrawablePolygon;
    private double AvgDist;

    public Polygon2D(Screen screen, double[] x, double[] y, double[] z, Color c, boolean seeThrough)
    {
        this.screen = screen;
        this.calculator = screen.getCalculator();
        this.x = x;
        this.y = y;
        this.z = z;
        this.c = c;
        this.seeThrough = seeThrough;
        this.visible = true;
        createPolygon();
    }

    void createPolygon()
    {
        DrawablePolygon = new PolygonObject(screen, new double[x.length], new double[x.length], c, screen.getPolygon2DS().size(), seeThrough);
    }

    void updatePolygon()
    {
        newX = new double[x.length];
        newY = new double[x.length];
        draw = true;
        for(int i=0; i<x.length; i++)
        {
            CalcPos = calculator.calculatePositionP(screen.getViewFrom(), screen.getViewTo(), x[i], y[i], z[i]);
            newX[i] = (screen.getScreenWidth()/2 - calculator.getCalcFocusPos()[0]) + CalcPos[0] * screen.getZoom();
            newY[i] = (screen.getScreenHeight()/2 - calculator.getCalcFocusPos()[1]) + CalcPos[1] * screen.getZoom();
            if(calculator.getT() < 0)
                draw = false;
        }

        calcLighting();

        DrawablePolygon.setDraw(draw);
        DrawablePolygon.updatePolygon(newX, newY);
        AvgDist = GetDist();
    }

    void calcLighting()
    {
        Plane lightingPlane = new Plane(this);
        double angle = Math.acos(((lightingPlane.getNV().getX() * screen.getLightDir()[0]) +
                (lightingPlane.getNV().getY() * screen.getLightDir()[1]) + (lightingPlane.getNV().getZ() * screen.getLightDir()[2]))
                /(Math.sqrt(screen.getLightDir()[0] * screen.getLightDir()[0] + screen.getLightDir()[1] * screen.getLightDir()[1] + screen.getLightDir()[2] * screen.getLightDir()[2])));

        DrawablePolygon.setLighting(0.2 + 1 - Math.sqrt(Math.toDegrees(angle)/180));

        if(DrawablePolygon.getLighting() > 1)
            DrawablePolygon.setLighting(1);
        if(DrawablePolygon.getLighting() < 0)
            DrawablePolygon.setLighting(0);
    }

    double GetDist()
    {
        double total = 0;
        for(int i=0; i<x.length; i++)
            total += GetDistanceToP(i);
        return total / x.length;
    }

    double GetDistanceToP(int i)
    {
        return Math.sqrt((screen.getViewFrom()[0]-x[i])*(screen.getViewFrom()[0]-x[i]) +
                (screen.getViewFrom()[1]-y[i])*(screen.getViewFrom()[1]-y[i]) +
                (screen.getViewFrom()[2]-z[i])*(screen.getViewFrom()[2]-z[i]));
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        DrawablePolygon.setVisible(visible);
    }

    public double[] getX() {
        return x;
    }

    public void setX(double[] x) {
        this.x = x;
    }

    public double[] getY() {
        return y;
    }

    public void setY(double[] y) {
        this.y = y;
    }

    public double[] getZ() {
        return z;
    }

    public void setZ(double[] z) {
        this.z = z;
    }

    public double getAvgDist() {
        return AvgDist;
    }

    public PolygonObject getDrawablePolygon() {
        return DrawablePolygon;
    }

    public boolean isVisible() {
        return visible;
    }
}
