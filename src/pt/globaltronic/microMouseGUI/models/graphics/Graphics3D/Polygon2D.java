package pt.globaltronic.microMouseGUI.models.graphics.Graphics3D;
import java.awt.Color;

public class Polygon2D {
    private Screen screen;
    private Calculator calculator;
    Color c;
    double[] x;
    double[] y;
    double[] z;
    boolean draw = true;
    boolean seeThrough;
    boolean visible;
    double[] CalcPos;
    double[] newX;
    double[] newY;
    PolygonObject DrawablePolygon;
    double AvgDist;

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

        DrawablePolygon.draw = draw;
        DrawablePolygon.updatePolygon(newX, newY);
        AvgDist = GetDist();
    }

    void calcLighting()
    {
        Plane lightingPlane = new Plane(this);
        double angle = Math.acos(((lightingPlane.NV.x * screen.getLightDir()[0]) +
                (lightingPlane.NV.y * screen.getLightDir()[1]) + (lightingPlane.NV.z * screen.getLightDir()[2]))
                /(Math.sqrt(screen.getLightDir()[0] * screen.getLightDir()[0] + screen.getLightDir()[1] * screen.getLightDir()[1] + screen.getLightDir()[2] * screen.getLightDir()[2])));

        DrawablePolygon.lighting = 0.2 + 1 - Math.sqrt(Math.toDegrees(angle)/180);

        if(DrawablePolygon.lighting > 1)
            DrawablePolygon.lighting = 1;
        if(DrawablePolygon.lighting < 0)
            DrawablePolygon.lighting = 0;
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
}
