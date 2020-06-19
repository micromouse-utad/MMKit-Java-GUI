package pt.globaltronic.microMouseGUI.models.graphics.Graphics3D;
import java.awt.Color;

public class Pyramid {
    private Screen screen;
    private double x;
    private double y;
    private double z;
    private double width;
    private double length;
    private double height;
    private double rotation = Math.PI*0.75;
    private double[] RotAdd = new double[4];
    private Color c;
    private double x1, x2, x3, x4, x5;
    private double y1, y2, y3, y4, y5;
    private Polygon2D[] Polys = new Polygon2D[5];
    private double[] angle;
    private boolean visible;

    public Pyramid(Screen screen, double x, double y, double z, double width, double length, double height, Color c)
    {
        this.screen = screen;
        Polys[0] = new Polygon2D(screen, new double[]{x, x+width, x+width, x}, new double[]{y, y, y+length, y+length},  new double[]{z, z, z, z}, c, false);
        screen.getPolygon2DS().add(Polys[0]);
        Polys[1] = new Polygon2D(screen, new double[]{x, x, x+width}, new double[]{y, y, y, y},  new double[]{z, z+height, z+height}, c, false);
        screen.getPolygon2DS().add(Polys[1]);
        Polys[2] = new Polygon2D(screen, new double[]{x+width, x+width, x+width}, new double[]{y, y, y+length},  new double[]{z, z+height, z+height}, c, false);
        screen.getPolygon2DS().add(Polys[2]);
        Polys[3] = new Polygon2D(screen, new double[]{x, x, x+width}, new double[]{y+length, y+length, y+length},  new double[]{z, z+height, z+height}, c, false);
        screen.getPolygon2DS().add(Polys[3]);
        Polys[4] = new Polygon2D(screen, new double[]{x, x, x}, new double[]{y, y, y+length},  new double[]{z, z+height, z+height}, c, false);
        screen.getPolygon2DS().add(Polys[4]);

        this.c = c;
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = width;
        this.length = length;
        this.height = height;

        setRotAdd();
        updatePoly();
    }

    public void setRotAdd()
    {
        angle = new double[6];

        double xdif = - width + 0.00001;
        double ydif = - length + 0.00001;

        angle[0] = Math.atan(ydif/xdif);

        if(xdif<0) {
            angle[0] += Math.PI;
        }


        xdif = width + 0.00001;
        ydif = - length + 0.00001;

        angle[1] = Math.atan(ydif/xdif);

        if(xdif<0) {
            angle[1] += Math.PI;
        }

        xdif = width + 0.00001;
        ydif = length + 0.00001;

        angle[2] = Math.atan(ydif/xdif);

        if(xdif<0) {
            angle[2] += Math.PI;
        }


        xdif = - width + 0.00001;
        ydif = length + 0.00001;

        angle[3] = Math.atan(ydif/xdif);

        if(xdif<0) {
            angle[3] += Math.PI;
        }

        RotAdd[0] = angle[0] + 0.25 * Math.PI;
        RotAdd[1] =	angle[1] + 0.25 * Math.PI;
        RotAdd[2] = angle[2] + 0.25 * Math.PI;
        RotAdd[3] = angle[3] + 0.25 * Math.PI;
    }

    void UpdateDirection(double toX, double toY)
    {
        double xdif = toX - (x + width/2) + 0.00001;
        double ydif = toY - (y + length/2) + 0.00001;

        double anglet = Math.atan(ydif/xdif) + 0.75 * Math.PI;

        if(xdif<0)
            anglet += Math.PI;

        rotation = anglet;
        updatePoly();
    }

    public void updatePoly()
    {
        //using arraylist add and remove properties, add puts a new instance at the end of the list, remove removes the first instance found at the smallest index
        for(int i = 0; i < 5; i++)
        {
            screen.getPolygon2DS().add(Polys[i]);
            screen.getPolygon2DS().remove(Polys[i]);
        }

        double radius = Math.sqrt(width*width + length*length);

        x1 = x+width*0.5+radius*0.5*Math.cos(rotation + RotAdd[0]);
        x2 = x+width*0.5+radius*0.5*Math.cos(rotation + RotAdd[1]);
        x3 = x+width*0.5+radius*0.5*Math.cos(rotation + RotAdd[2]);
        x4 = x+width*0.5+radius*0.5*Math.cos(rotation + RotAdd[3]);
        x5 = x+width*0.5;

        y1 = y+length*0.5+radius*0.5*Math.sin(rotation + RotAdd[0]);
        y2 = y+length*0.5+radius*0.5*Math.sin(rotation + RotAdd[1]);
        y3 = y+length*0.5+radius*0.5*Math.sin(rotation + RotAdd[2]);
        y4 = y+length*0.5+radius*0.5*Math.sin(rotation + RotAdd[3]);
        y5 = y+length*0.5;

        Polys[0].setX(new double[]{x1, x2, x3, x4});
        Polys[0].setY(new double[]{y1, y2, y3, y4});
        Polys[0].setZ(new double[]{z, z, z, z});

        Polys[1].setX(new double[]{x1, x5, x2});
        Polys[1].setY(new double[]{y1, y5, y2});
        Polys[1].setZ(new double[]{z, z+height, z});

        Polys[2].setX(new double[]{x3, x2, x5});
        Polys[2].setY(new double[]{y3, y2, y5});
        Polys[2].setZ(new double[]{z, z, z+height});

        Polys[3].setX(new double[]{x3, x5, x4});
        Polys[3].setY(new double[]{y3, y5, y4});
        Polys[3].setZ(new double[]{z, z+height, z});

        Polys[4].setX(new double[]{x1, x4, x5});
        Polys[4].setY(new double[]{y1, y4, y5});
        Polys[4].setZ(new double[]{z, z, z+height});

    }

    void removePyramid()
    {
        for(int i = 0; i < 5; i ++)
            screen.getPolygon2DS().remove(Polys[i]);
            screen.getPyramids().remove(this);
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
        for (int i = 0; i < Polys.length; i++){
            Polys[i].setVisible(visible);
        }
    }

    public void setRotation(double rotation) {
        this.rotation += rotation;
    }
}
