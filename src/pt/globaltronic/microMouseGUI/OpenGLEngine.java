package pt.globaltronic.microMouseGUI;

import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;
import com.sun.javafx.geom.Vec3f;

import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.Grid;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.MouseInputs;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.Position;
import pt.globaltronic.microMouseGUI.models.graphics.services.MouseInputsTranslator;
import pt.globaltronic.microMouseGUI.models.graphics.viewObjects.HorizontalWalls;
import pt.globaltronic.microMouseGUI.models.graphics.viewObjects.Mouse;
import pt.globaltronic.microMouseGUI.models.graphics.viewObjects.VerticalWalls;
import pt.globaltronic.microMouseGUI.openGL.Loader;
import pt.globaltronic.microMouseGUI.openGL.MasterRenderer;
import pt.globaltronic.microMouseGUI.openGL.OBJLoader;
import pt.globaltronic.microMouseGUI.openGL.entity.Camera;
import pt.globaltronic.microMouseGUI.openGL.entity.Entity;
import pt.globaltronic.microMouseGUI.openGL.entity.Light;
import pt.globaltronic.microMouseGUI.openGL.entity.MouseGFX;
import pt.globaltronic.microMouseGUI.openGL.models.RawModel;
import pt.globaltronic.microMouseGUI.openGL.models.TexturedModel;
import pt.globaltronic.microMouseGUI.openGL.terrain.Terrain;
import pt.globaltronic.microMouseGUI.openGL.textures.ModelTexture;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;

public class OpenGLEngine implements GLEventListener, KeyListener, MouseListener, MouseMotionListener, MouseWheelListener, Runnable {

    //openGL variables
    private GLJPanel canvas;
    private JPanel panel;
    private MasterRenderer renderer;
    private Loader loader;
    private RawModel model;
    private ModelTexture texture;
    private TexturedModel texturedModel;
    private Camera camera;
    private Light sun;
    private Terrain terrain;
    private MouseGFX mouseGFX;

    //eventlistener variables
    private boolean rightClickDown;
    private Robot r;

    //"gameloop" variables
    private Thread thread;
    private Grid grid;
    private Mouse mouse;
    private MouseInputs mouseInputs;
    private HorizontalWalls[][] hWalls;
    private VerticalWalls[][] vWalls;
    private int cols;
    private int rows;
    private int cellSize;
    private int width;
    private int height;
    public static HashSet<Entity> VISIBLE_WALLS;
    private boolean mouseAnimationFinished = true;
    float xInitial;
    float zInitial;
    float xFinal;
    float zFinal;
    private String previousDirection = "N";
    private float yAngle;
    float yAngleIncrement;
    float xIncrement;
    float zIncrement;
    int numbOfRotations = 0;
    int numbOfTranslation = 0;

    boolean firstPersonView = true;
    boolean cleared = true;

    private static long lastFrameTime = 0;
    private static float delta;

    public OpenGLEngine(JPanel panel, MouseInputs mouseInputs, int cols, int rows, int cellSize, int size) {
        this.canvas = new GLJPanel();
        this.panel = panel;
        this.cellSize = cellSize;
        this.mouseInputs = mouseInputs;
        this.cols = cols;
        this.rows = rows;
        this.width = cols * size;
        this.height = rows * size;


        //add to implement because lost focus would not register.
        canvas.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                e.getComponent().requestFocus();
            }
        });
    }

    @Override
    public void display(GLAutoDrawable glad) {

        if(!firstPersonView){
            camera.move(!firstPersonView);
        }
        if (!cleared) {
            clear();
            cleared = true;
        }
        tick();
        if(firstPersonView) {
            camera.move();
        }

        for (Entity wall : VISIBLE_WALLS){
            renderer.processEntity(wall);
        }
        renderer.processTerrain(terrain);
        renderer.processEntity(mouseGFX);
        renderer.render(sun, camera);
        long currentFrameTime = System.currentTimeMillis();
        delta = (currentFrameTime - lastFrameTime) / 1000f;
        lastFrameTime = currentFrameTime;
    }

    public static float getFrameTimeSeconds() {
        return delta;
    }


    @Override
    public void dispose(GLAutoDrawable arg0) {
        renderer.cleanUp();
        loader.cleanUpMemory();
    }

    @Override
    public void init(GLAutoDrawable glad) {


        grid = new Grid(cols,rows,cellSize);
        VISIBLE_WALLS = new HashSet<Entity>();

        renderer = new MasterRenderer(glad.getGL().getGL3(), width, height);
        loader = new Loader(glad.getGL().getGL3());
        createWalls();

        model = OBJLoader.loadObjectModel("E 45 Aircraft", loader);
        //use the loader to get the id of the texture and pass it to the new texture
        texture = new ModelTexture(loader.loadTexture("image"));
        texturedModel = new TexturedModel(model, texture);
        texturedModel.getModelTexture().setReflectivity(1);
        texturedModel.getModelTexture().setShineDamper(10);
        mouseGFX = new MouseGFX(texturedModel, new Vec3f(155f, 3, 5f), 0, 180, 0, 1.0f);
        mouse = new Mouse(grid.getPosition(0,0), mouseGFX, cellSize);
        camera = new Camera(mouseGFX);
        sun = new Light(new Vec3f(2000, 2000, 000), new Vec3f(1, 1, 1));
        terrain = new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture("blacktiles")));

    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

    }


    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W)
            camera.setCurrentSpeed(-1.0f);
        if (e.getKeyCode() == KeyEvent.VK_A)
            camera.setCurrentTurnSpeed(1.0f);
        if (e.getKeyCode() == KeyEvent.VK_S)
            camera.setCurrentSpeed(1.0f);
        if (e.getKeyCode() == KeyEvent.VK_D)
            camera.setCurrentTurnSpeed(-1.0f);

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W)
            camera.setCurrentSpeed(0f);
        if (e.getKeyCode() == KeyEvent.VK_A)
            camera.setCurrentTurnSpeed(0f);
        if (e.getKeyCode() == KeyEvent.VK_S)
            camera.setCurrentSpeed(0f);
        if (e.getKeyCode() == KeyEvent.VK_D)
            camera.setCurrentTurnSpeed(0f);
    }


    void CenterMouse(MouseEvent e) {
        try {
            r = new Robot();
            r.mouseMove((int)(e.getComponent().getLocationOnScreen().getX() + (width)/2), (int)(e.getComponent().getLocationOnScreen().getY() + (height)/2));
        } catch (AWTException ex) {
            ex.printStackTrace();
        }
    }

    public void mouseDragged(MouseEvent arg0) {

        if (rightClickDown) {
            float pitch = camera.getPitch();
            float angleAroundPlayer = camera.getAngleAroundPlayer();

            float pitchChange = (arg0.getY() - height/2) * 0.1f;
            camera.setPitch(pitch + pitchChange);

            float angleChange = (arg0.getX() -width/2) * 0.1f;
            if(firstPersonView){camera.setAngleAroundPlayer(angleAroundPlayer + angleChange);}
            if(!firstPersonView) {camera.setAngleAroundPlayer(angleAroundPlayer - angleChange);}

            CenterMouse(arg0);
        }
    }



    public void mouseMoved(MouseEvent arg0) {

    }

    public void mouseClicked(MouseEvent arg0) {
    }

    public void mouseEntered(MouseEvent arg0) {
    }

    public void mouseExited(MouseEvent arg0) {
    }

    public void mousePressed(MouseEvent arg0) {
        if (arg0.getButton() == MouseEvent.BUTTON3) {
            rightClickDown = true;
        }

    }

    public void mouseReleased(MouseEvent arg0) {
        if (arg0.getButton() == MouseEvent.BUTTON3) {
            rightClickDown = false;
        }
    }

    public void mouseWheelMoved(MouseWheelEvent arg0) {
        float distanceFromPlayer = camera.getDistanceFromPlayer();
        if (arg0.getUnitsToScroll() > 0) {
                camera.setDistanceFromPlayer(distanceFromPlayer +1 * arg0.getUnitsToScroll());
        } else {
            if (distanceFromPlayer > 0) {
                camera.setDistanceFromPlayer(distanceFromPlayer + 1 * arg0.getUnitsToScroll());
            }
        }
    }

    private void createWalls() {

        //Loading models and textures to create the walls.
        RawModel wallRaw = OBJLoader.loadObjectModel("wall", loader);
        ModelTexture wallTexture = new ModelTexture(loader.loadTexture("myTexture"));
        TexturedModel texturedWall = new TexturedModel(wallRaw, wallTexture);
        texturedWall.getModelTexture().setReflectivity(1);
        texturedWall.getModelTexture().setShineDamper(10);


        //for the horizontal walls we need +1 row because to have 16squares you need 17 lines to close them.
        hWalls = new HorizontalWalls[cols][rows + 1];

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows + 1; j++) {
                hWalls[i][j] = new HorizontalWalls(grid.getHWallPosition(i, j));
                int x = cols -1 - hWalls[i][j].getPosition().getCol();
                int z = hWalls[i][j].getPosition().getRow();

                Entity hWall = new Entity(texturedWall, new Vec3f(x * cellSize, 0, z * cellSize -1),0,0,0,1);
                hWalls[i][j].setEntity(hWall);
                hWalls[i][j].setVisible(false);

                //always showing side walls.

                if (j == 0 || j == rows) {
                    hWalls[i][j].setVisible(true);
                }
            }
        }

        //likewise for the Vertical walls we need +1 col.
        vWalls = new VerticalWalls[cols + 1][rows];

        for (int i = 0; i < cols + 1; i++) {
            for (int j = 0; j < rows; j++) {
                vWalls[i][j] = new VerticalWalls(grid.getVWallPosition(i, j));
                int x = cols - vWalls[i][j].getPosition().getCol();
                int z = vWalls[i][j].getPosition().getRow()+1;

                Entity vWall = new Entity(texturedWall, new Vec3f(x * cellSize -1, 0, z * cellSize),0,90,0,1);
                vWalls[i][j].setEntity(vWall);
                vWalls[i][j].setVisible(false);

                //always showing side walls.
                if (i == 0 || i == cols) {
                    vWalls[i][j].setVisible(true);
                }
            }
        }
    }

    private void tick() {
        //get the mouse inputs from the 3d queue
        if (!mouseAnimationFinished){
            mouseAnimation();
            return;
        }
        String inputs = mouseInputs.getMouseInput3D();

        //checking that we actual got a result and that it matches the length we need before we try to parse and extract data
        if (inputs != null && (inputs.length() > 9)) {
            int col = MouseInputsTranslator.parseCol(inputs);
            int row = MouseInputsTranslator.parseRow(inputs);

            //getting the mouse current position
            Position pos = grid.getPosition(col, row);
            Position oldMousePosition = mouse.getPosition();
            pos.setVisited(true);
            //updating mouse position and its graphics
            mouse.setPosition(pos);

            //parsing the direction for "first person" camera view, and wall positioning
            String direction = MouseInputsTranslator.parseDirection(inputs);

            setupMouseAnimation(oldMousePosition, pos, direction);
            previousDirection = direction;


            boolean lWall = MouseInputsTranslator.parseLeftWall(inputs);
            boolean fWall = MouseInputsTranslator.parseFrontWall(inputs);
            boolean rWall = MouseInputsTranslator.parseRightWall(inputs);

            switch (direction) {
                case "N":
                    //left wall when facing up is a vertical wall with its position same as mouse position
                    vWalls[col][row].setVisible(lWall);
                    //front wall when facing up is a horizontal wall at col and +1 row than current position
                    hWalls[col][row + 1].setVisible(fWall);
                    //right wall when facing up is a vertical wall at +1 col and same row as position
                    vWalls[col + 1][row].setVisible(rWall);
                    break;
                case "E":
                    //facing east left wall will be horizontal wall at same col +1 row from position
                    hWalls[col][row + 1].setVisible(lWall);
                    //facing east front wall will be a vertical wall at +1 col and row as position
                    vWalls[col + 1][row].setVisible(fWall);
                    //facing east right wall will be a horizontal wall at position same  as mouse position
                    hWalls[col][row].setVisible(rWall);
                    break;
                case "S":
                    // similar to N facing but reverted left wall will be the right wall
                    vWalls[col + 1][row].setVisible(lWall);
                    //front wall will be a horizontal wall at same postion as mouse position
                    hWalls[col][row].setVisible(fWall);
                    //right wall will be left wall of N case
                    vWalls[col][row].setVisible(rWall);
                    break;
                case "W":
                    //reverse of case east
                    //left wall will be a horizontal wall when facing west, with same position as mouse position
                    hWalls[col][row].setVisible(lWall);
                    //facing west, front wall will be a vertical wall at col + 1 and same row as current position
                    vWalls[col][row].setVisible(fWall);
                    //facing west, right wall will be a horizontal wall at col and +1 row as position
                    hWalls[col][row + 1].setVisible(rWall);
                    break;
            }
        }
    }
    public void setupMouseAnimation(Position initialPos, Position finalPos, String direction){
        mouseAnimationFinished = false;
        xInitial = (-initialPos.getCol()) * cellSize;
        zInitial = initialPos.getRow() * cellSize;
        xFinal = (-finalPos.getCol()) * cellSize;
        zFinal = finalPos.getRow() * cellSize;
        float xDif = xFinal - xInitial;
        float zDif = zFinal - zInitial;
        xIncrement = xDif / 25;
        zIncrement = zDif / 25;
        yAngleIncrement = (calculateYRotationAngle(direction) / 25);
    }

    public void mouseAnimation() {
        if(numbOfRotations < 25){
            mouseGFX.increaseRotation(0, yAngleIncrement, 0);
            numbOfRotations++;
            return;
        }

        if(numbOfTranslation < 25) {
            xInitial += xIncrement;
            zInitial += zIncrement;
            mouseGFX.increasePosition(xIncrement, 0, zIncrement);
            numbOfTranslation++;
            return;
        }
        if(numbOfRotations == numbOfTranslation){
            numbOfRotations = 0;
            numbOfTranslation= 0;
            mouseAnimationFinished = true;
        }
    }

    public float calculateYRotationAngle(String direction) {
        if (previousDirection.equals("N")){
            yAngle = (direction.equals("E")) ? -90 : (direction.equals("S")) ? 180 : (direction.equals("W")) ? 90 : 0;
        }
        if (previousDirection.equals("E")){
            yAngle = (direction.equals("E")) ? 0 : (direction.equals("S")) ? -90 : (direction.equals("W")) ? 180 : 90;
        }
        if (previousDirection.equals("S")){
            yAngle = (direction.equals("E")) ? 90 : (direction.equals("S")) ? 0 : (direction.equals("W")) ? -90 : 180;
        }
        if (previousDirection.equals("W")){
            yAngle = (direction.equals("E")) ? 180 : (direction.equals("S")) ? 90 : (direction.equals("W")) ? 0 : -90;
        }
        return yAngle;
    }

    @Override
    public void run() {

        panel.setPreferredSize(new Dimension(width, height));
        panel.setMinimumSize(new Dimension(width, height));
        panel.setMaximumSize(new Dimension(width, height));


        //since the canvas is set as focusabled and requests focus, it needs to event listeners
        canvas.addGLEventListener(this);
        canvas.addKeyListener(this);
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
        canvas.addMouseWheelListener(this);
        canvas.setFocusable(true);
        canvas.requestFocus();
        panel.add(canvas);

        //frame.setLocationRelativeTo(null);
        panel.setVisible(true);

        FPSAnimator animator = new FPSAnimator(canvas, 60);
        animator.start();
    }

    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
    }

    public void replay() {
        cleared = false;
    }

    void clear() {
        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                Position pos = grid.getPosition(x, y);
                pos.setVisited(false);
            }
        }
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows + 1; j++) {
                int x = hWalls[i][j].getPosition().getCol();
                int y = hWalls[i][j].getPosition().getRow();
                hWalls[i][j].setVisible(false);
                //always showing side walls.

                if (j == 0) {
                    hWalls[i][j].setVisible(true);
                }
                if (j == rows) {
                    hWalls[i][j].setVisible(true);
                }
            }
        }

        for (int i = 0; i < cols + 1; i++) {
            for (int j = 0; j < rows; j++) {
                int x = vWalls[i][j].getPosition().getCol();
                int y = vWalls[i][j].getPosition().getRow();
                vWalls[i][j].setVisible(false);

                //always showing side walls.
                if (i == 0) {
                    vWalls[i][j].setVisible(true);
                }
                if (i == cols) {
                    vWalls[i][j].setVisible(true);
                }
            }
        }
    }

    public void setFirstPersonView(boolean firstPersonView) {
        this.firstPersonView = firstPersonView;
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }


}