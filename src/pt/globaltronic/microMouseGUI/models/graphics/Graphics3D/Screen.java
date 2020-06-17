package pt.globaltronic.microMouseGUI.models.graphics.Graphics3D;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.Position;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Screen extends JPanel implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener{

    //ArrayList of all the 3D polygons - each 3D polygon has a 2D 'PolygonObject' inside called 'DrawablePolygon'
    private ArrayList<Polygon2D> polygon2DS;
    private ArrayList<Cube> cubes;
    private ArrayList<Pyramid> pyramids;

    private Calculator calculator;

    private double Width;
    private double Height;
    private double correction;


    //Used for keeping mouse in center
    private Robot r;
    //used to allow free camera look (cursor look)
    private boolean rightClickDown = false;
    //to play around with the position of the camera, change viewfrom and view to, zoom, horlook, vertlook
    private double[] viewFrom = new double[] {81.898, 318.847, 155};
    private double[] viewTo = new double[] {81.88396, 318.01312, 154.4482};
    private double[] lightDir = new double[] {1, 1, 1};


    //The smaller the zoom the more zoomed out you are and visa versa, although altering too far from 1000 will make it look pretty weird
    private double zoom = 620;
    private double MinZoom = 500;
    private double MaxZoom = 2500;
    private double MouseX = 0;
    private double MouseY = 0;
    private double MovementSpeed = 0.5;

    //FPS is a bit primitive, you can set the MaxFPS as high as u want
    private double drawFPS = 0;
    private double MaxFPS = 1000;
    private double SleepTime = 1000.0/MaxFPS;
    private double LastRefresh = 0;
    private double StartTime = System.currentTimeMillis();
    private double LastFPSCheck = 0;
    private double Checks = 0;
    //VertLook goes from 0.999 to -0.999, minus being looking down and + looking up, HorLook takes any number and goes round in radians
    //aimSight changes the size of the center-cross. The lower HorRotSpeed or VertRotSpeed, the faster the camera will rotate in those directions
    private double VertLook = -0.2;
    private double HorLook = 0;
    private double aimSight = 4;
    private double HorRotSpeed = 900;
    private double VertRotSpeed = 2200;
    private double SunPos = 0;


    //default view settings
    //double VertLook = -0.7, HorLook = 4.64, aimSight = 4, HorRotSpeed = 900, VertRotSpeed = 2200, SunPos = 0;

    private boolean OutLines = true;
    private boolean[] Keys = new boolean[4];

    public Screen(double width, double height, int correction)
    {
        polygon2DS = new ArrayList<Polygon2D>();
        cubes = new ArrayList<Cube>();
        pyramids = new ArrayList<Pyramid>();

        this.calculator = new Calculator(this);

        Width = width;
        Height = height;
        this.correction = correction;
        this.addKeyListener(this);
        setFocusable(true);

        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);

        invisibleMouse();
    }

    public void paintComponent(Graphics g)
    {
        //Clear screen and draw background color
        g.setColor(new Color(140, 180, 180));
        g.fillRect(0, 0, (int)Width, (int)Height);

        //adjust the cammera
        CameraMovement();

        //Calculated all that is general for this camera position
        calculator.SetPrederterminedInfo();


        //Updates each polygon for this camera position
        for(int i = 0; i < polygon2DS.size(); i++) {
            polygon2DS.get(i).updatePolygon();
        }

        //Set drawing order so closest polygons gets drawn last
        setOrder();

        //draw polygons in the Order that is set by the 'setOrder' function
        for(int i = 0; i < polygon2DS.size(); i++){
            if (polygon2DS.get(i).isVisible()) {
                polygon2DS.get(i).getDrawablePolygon().drawPolygon(g);
            }
        }

        //draw the cross in the center of the screen
        //drawMouseAim(g);

        //FPS display
        //g.drawString("FPS: " + (int)drawFPS + " (Benchmark)", 40, 40);

        SleepAndRefresh();
    }

    //set the order of the polygons to be drawn, based on their distance from the "view" the furthest away get sorted to the beginnign of the list
    //insertion sort.
    void setOrder(){
        int n = polygon2DS.size();
        for (int i = 1; i < n; ++i) {
            Polygon2D key = polygon2DS.get(i);
            int j = i - 1;

            while (j >= 0 && polygon2DS.get(j).getAvgDist() < key.getAvgDist()) {
                polygon2DS.set(j + 1, polygon2DS.get(j));
                j = j - 1;
            }
            polygon2DS.set(j + 1, key);
        }


    }

    //make the toolkit mouse skin disappear when moused over the 3d Jpanel.
    void invisibleMouse()
    {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        BufferedImage cursorImage = new BufferedImage(1, 1, BufferedImage.TRANSLUCENT);
        Cursor invisibleCursor = toolkit.createCustomCursor(cursorImage, new Point(0,0), "InvisibleCursor");
        setCursor(invisibleCursor);
    }

    //draw a mouse crosshair at screen center.
    void drawMouseAim(Graphics g)
    {
        g.setColor(Color.black);
        g.drawLine((int)(Width/2 - aimSight), (int)(Height/2), (int)(Width/2 + aimSight), (int)(Height/2));
        g.drawLine((int)(Width/2), (int)(Height/2 - aimSight), (int)(Width/2), (int)(Height/2 + aimSight));
    }

    //limiting fps to maxFPS preset and repainting
    public void SleepAndRefresh()
    {
        long timeSLU = (long) (System.currentTimeMillis() - LastRefresh);

        Checks ++;
        if(Checks >= 15)
        {
            drawFPS = Checks/((System.currentTimeMillis() - LastFPSCheck)/1000.0);
            LastFPSCheck = System.currentTimeMillis();
            Checks = 0;
        }

        if(timeSLU < 1000.0/MaxFPS)
        {
            try {
                Thread.sleep((long) (1000.0/MaxFPS - timeSLU));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        LastRefresh = System.currentTimeMillis();

        repaint();
    }

    void ControlSunAndLight()
    {
        SunPos += 0.005;
        double mapSize = Width;
        lightDir[0] = mapSize/2 - (mapSize/2 + Math.cos(SunPos) * mapSize * 10);
        lightDir[1] = mapSize/2 - (mapSize/2 + Math.sin(SunPos) * mapSize * 10);
        lightDir[2] = -200;
    }

    void CameraMovement()
    {
        Vector ViewVector = new Vector(viewTo[0] - viewFrom[0], viewTo[1] - viewFrom[1], viewTo[2] - viewFrom[2]);
        double xMove = 0, yMove = 0, zMove = 0;
        Vector VerticalVector = new Vector (0, 0, 1);
        Vector SideViewVector = ViewVector.CrossProduct(VerticalVector);

        if(Keys[0])
        {
            xMove += ViewVector.getX() ;
            yMove += ViewVector.getY() ;
            zMove += ViewVector.getZ() ;
        }

        if(Keys[2])
        {
            xMove -= ViewVector.getX() ;
            yMove -= ViewVector.getY() ;
            zMove -= ViewVector.getZ() ;
        }

        if(Keys[1])
        {
            xMove += SideViewVector.getX() ;
            yMove += SideViewVector.getY() ;
            zMove += SideViewVector.getZ() ;
        }

        if(Keys[3])
        {
            xMove -= SideViewVector.getX() ;
            yMove -= SideViewVector.getY() ;
            zMove -= SideViewVector.getZ() ;
        }

        Vector MoveVector = new Vector(xMove, yMove, zMove);
        MoveTo(viewFrom[0] + MoveVector.getX() * MovementSpeed, viewFrom[1] + MoveVector.getY() * MovementSpeed, viewFrom[2] + MoveVector.getZ() * MovementSpeed);
    }

    void MoveTo(double x, double y, double z)
    {
        viewFrom[0] = x;
        viewFrom[1] = y;
        viewFrom[2] = z;
        updateView();
    }

    void MouseMovement(double NewMouseX, double NewMouseY)
    {
        double difX = (NewMouseX - Width/2);
        double difY = (NewMouseY - Height/2);
        difY *= 6 - Math.abs(VertLook) * 5;
        VertLook -= difY  / VertRotSpeed;
        HorLook += difX / HorRotSpeed;

        if(VertLook>0.999) {
            VertLook = 0.999;
        }

        if(VertLook<-0.999) {
            VertLook = -0.999;
        }
        updateView();
    }

    void updateView()
    {
        double r = Math.sqrt(1 - (VertLook * VertLook));
        viewTo[0] = viewFrom[0] + r * Math.cos(HorLook);
        viewTo[1] = viewFrom[1] + r * Math.sin(HorLook);
        viewTo[2] = viewFrom[2] + VertLook;
    }

    void CenterMouse(MouseEvent e)
    {
        //centering on the absolute position of the window
        try {
            r = new Robot();
            r.mouseMove((int)(e.getComponent().getLocationOnScreen().getX() + (Width)/2), (int)(e.getComponent().getLocationOnScreen().getY() + (Height)/2));
        } catch (AWTException ex) {
            ex.printStackTrace();
        }
    }

    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_W)
            Keys[0] = true;
        if(e.getKeyCode() == KeyEvent.VK_A)
            Keys[1] = true;
        if(e.getKeyCode() == KeyEvent.VK_S)
            Keys[2] = true;
        if(e.getKeyCode() == KeyEvent.VK_D)
            Keys[3] = true;
        if(e.getKeyCode() == KeyEvent.VK_O)
            OutLines = !OutLines;
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
            System.exit(0);
    }

    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_W)
            Keys[0] = false;
        if(e.getKeyCode() == KeyEvent.VK_A)
            Keys[1] = false;
        if(e.getKeyCode() == KeyEvent.VK_S)
            Keys[2] = false;
        if(e.getKeyCode() == KeyEvent.VK_D)
            Keys[3] = false;
    }

    public void keyTyped(KeyEvent e) {
    }

    public void mouseDragged(MouseEvent arg0) {
        if(rightClickDown) {
            MouseMovement(arg0.getX(), arg0.getY());
            MouseX = arg0.getX();
            MouseY = arg0.getY();
            CenterMouse(arg0);
        }
    }

    public void mouseMoved(MouseEvent arg0) {
        if(rightClickDown) {
            MouseMovement(arg0.getX(), arg0.getY());
            MouseX = arg0.getX();
            MouseY = arg0.getY();
            CenterMouse(arg0);
        }
    }

    public void mouseClicked(MouseEvent arg0) {
    }

    public void mouseEntered(MouseEvent arg0) {
    }

    public void mouseExited(MouseEvent arg0) {
    }

    public void mousePressed(MouseEvent arg0) {
        if(arg0.getButton() == MouseEvent.BUTTON3){
            rightClickDown = true;
        }
    }

    public void mouseReleased(MouseEvent arg0) {
        if(arg0.getButton() == MouseEvent.BUTTON3){
            rightClickDown = false;
        }
    }

    public void mouseWheelMoved(MouseWheelEvent arg0) {
        if(arg0.getUnitsToScroll()>0)
        {
            if(zoom > MinZoom)
                zoom -= 25 * arg0.getUnitsToScroll();
        }
        else
        {
            if(zoom < MaxZoom)
                zoom -= 25 * arg0.getUnitsToScroll();
        }
    }

    public void setViewFrom(double[] viewFrom) {
        this.viewFrom = viewFrom;
    }

    public void setHorLook(double horLook) {
        HorLook = horLook;
    }

    public void setCameraPositionForMouseView(String direction, Position pos, double size) {
        switch (direction){
            case "N":
                setViewFrom(new double[]{((pos.getCol()+ 0.5)*size), ((correction - pos.getRow() + 1.5)*size), 4});
                setHorLook(4.712388);
                break;
            case "E":
                setViewFrom(new double[]{((pos.getCol() - 0.5)*size), ((correction - pos.getRow() + 0.5)*size), 4});
                setHorLook(0);
                break;
            case "S":
                setViewFrom(new double[]{((pos.getCol()+ 0.5)*size), ((correction - pos.getRow() - 0.5)*size), 4});
                setHorLook(1.570796);
                break;
            case "W":
                setViewFrom(new double[]{((pos.getCol() + 1.5)*size), ((correction - pos.getRow() + 0.5)*size), 4});
                setHorLook(3.141592);
                break;
        }
        updateView();
    }

    public ArrayList<Polygon2D> getPolygon2DS() {
        return polygon2DS;
    }

    public ArrayList<Pyramid> getPyramids() {
        return pyramids;
    }

    public ArrayList<Cube> getCubes() {
        return cubes;
    }

    public double getScreenWidth() {
        return Width;
    }

    public double getScreenHeight() {
        return Height;
    }

    public double getCorrection() {
        return correction;
    }

    public double[] getViewFrom() {
        return viewFrom;
    }

    public double[] getViewTo() {
        return viewTo;
    }

    public double[] getLightDir() {
        return lightDir;
    }

    public double getZoom() {
        return zoom;
    }

    public boolean isOutLined() {
        return OutLines;
    }

    public Calculator getCalculator() {
        return calculator;
    }
}
