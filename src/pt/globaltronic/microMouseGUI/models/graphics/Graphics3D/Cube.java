package pt.globaltronic.microMouseGUI.models.graphics.Graphics3D;
import java.awt.Color;

public class Cube {
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
    private double x1, x2, x3, x4;
    private double y1, y2, y3, y4;
    private Polygon2D[] Polys = new Polygon2D[6];
    private Boolean visible;
    private double[] angle;


    public Cube(Screen screen, double x, double y, double z, double width, double length, double height, Color c)
    {
        this.screen = screen;
        Polys[0] = new Polygon2D(screen, new double[]{x, x+width, x+width, x}, new double[]{y, y, y+length, y+length},  new double[]{z, z, z, z}, c, false);
        screen.getPolygon2DS().add(Polys[0]);
        Polys[1] = new Polygon2D(screen, new double[]{x, x+width, x+width, x}, new double[]{y, y, y+length, y+length},  new double[]{z+height, z+height, z+height, z+height}, Color.RED, false);
        screen.getPolygon2DS().add(Polys[1]);
        Polys[2] = new Polygon2D(screen, new double[]{x, x, x+width, x+width}, new double[]{y, y, y, y},  new double[]{z, z+height, z+height, z}, c, false);
        screen.getPolygon2DS().add(Polys[2]);
        Polys[3] = new Polygon2D(screen, new double[]{x+width, x+width, x+width, x+width}, new double[]{y, y, y+length, y+length},  new double[]{z, z+height, z+height, z}, c, false);
        screen.getPolygon2DS().add(Polys[3]);
        Polys[4] = new Polygon2D(screen, new double[]{x, x, x+width, x+width}, new double[]{y+length, y+length, y+length, y+length},  new double[]{z, z+height, z+height, z}, c, false);
        screen.getPolygon2DS().add(Polys[4]);
        Polys[5] = new Polygon2D(screen, new double[]{x, x, x, x}, new double[]{y, y, y+length, y+length},  new double[]{z, z+height, z+height, z}, c, false);
        screen.getPolygon2DS().add(Polys[5]);

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

    void setRotAdd()
    {
        angle = new double[4];

        double xdif = - width/2 + 0.00001;
        double ydif = - length/2 + 0.00001;

        angle[0] = Math.atan(ydif/xdif);

        if(xdif<0) {
            angle[0] += Math.PI;
        }

        xdif = width/2 + 0.00001;
        ydif = - length/2 + 0.00001;

        angle[1] = Math.atan(ydif/xdif);

        if(xdif<0) {
            angle[1] += Math.PI;
        }

        xdif = width/2 + 0.00001;
        ydif = length/2 + 0.00001;

        angle[2] = Math.atan(ydif/xdif);

        if(xdif<0) {
            angle[2] += Math.PI;
        }

        xdif = - width/2 + 0.00001;
        ydif = length/2 + 0.00001;

        angle[3] = Math.atan(ydif/xdif);

        if(xdif<0) {
            angle[3] += Math.PI;
        }

        RotAdd[0] = angle[0] + 0.25 * Math.PI;
        RotAdd[1] =	angle[1] + 0.25 * Math.PI;
        RotAdd[2] = angle[2] + 0.25 * Math.PI;
        RotAdd[3] = angle[3] + 0.25 * Math.PI;

    }

    void updatePoly()
    {
        //using a speciality of the arrayList to add and remove the Polygons of the cube, remove() removes the first instance found in the list,
        // the recently added will be at the end. The older instances will be deleted
        for(int i = 0; i < 6; i++)
        {
            screen.getPolygon2DS().remove(Polys[i]);
            screen.getPolygon2DS().add(Polys[i]);
        }

        double radius = Math.sqrt(width*width + length*length);

        x1 = x+width*0.5+radius*0.5*Math.cos(rotation + RotAdd[0]);
        x2 = x+width*0.5+radius*0.5*Math.cos(rotation + RotAdd[1]);
        x3 = x+width*0.5+radius*0.5*Math.cos(rotation + RotAdd[2]);
        x4 = x+width*0.5+radius*0.5*Math.cos(rotation + RotAdd[3]);

        y1 = y+length*0.5+radius*0.5*Math.sin(rotation + RotAdd[0]);
        y2 = y+length*0.5+radius*0.5*Math.sin(rotation + RotAdd[1]);
        y3 = y+length*0.5+radius*0.5*Math.sin(rotation + RotAdd[2]);
        y4 = y+length*0.5+radius*0.5*Math.sin(rotation + RotAdd[3]);

        Polys[0].setX(new double[]{x1, x2, x3, x4});
        Polys[0].setY(new double[]{y1, y2, y3, y4});
        Polys[0].setZ(new double[]{z, z, z, z});

        Polys[1].setX(new double[]{x4, x3, x2, x1});
        Polys[1].setY(new double[]{y4, y3, y2, y1});
        Polys[1].setZ(new double[]{z+height, z+height, z+height, z+height});

        Polys[2].setX(new double[]{x1, x1, x2, x2});
        Polys[2].setY(new double[]{y1, y1, y2, y2});
        Polys[2].setZ(new double[]{z, z+height, z+height, z});

        Polys[3].setX(new double[]{x2, x2, x3, x3});
        Polys[3].setY(new double[]{y2, y2, y3, y3});
        Polys[3].setZ(new double[]{z, z+height, z+height, z});

        Polys[4].setX(new double[]{x3, x3, x4, x4});
        Polys[4].setY(new double[]{y3, y3, y4, y4});
        Polys[4].setZ(new double[]{z, z+height, z+height, z});

        Polys[5].setX(new double[]{x4, x4, x1, x1});
        Polys[5].setY(new double[]{y4, y4, y1, y1});
        Polys[5].setZ(new double[]{z, z+height, z+height, z});

    }

    public void removeCube()
    {
        for(int i = 0; i < 6; i ++) {
            screen.getPolygon2DS().remove(Polys[i]);
            screen.getCubes().remove(this);
        }
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
        for (int i = 0; i < Polys.length; i++){
            Polys[i].setVisible(visible);
        }
    }

    public void setColor(Color c) {
        this.c = c;
    }
}
