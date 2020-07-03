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
    private OBJLoader objectLoader;
    private RawModel model;
    private ModelTexture texture;
    private TexturedModel texturedModel;
    private Camera camera;
    private Light sun;
    private HashSet<Terrain> terrains;
    private Entity mouseGFX;

    //eventlistener variables
    private boolean rightClickDown;
    private Robot r;

    //"gameloop" variables
    private boolean only3D;
    private Thread thread;
    private Grid grid;
    private Mouse mouse;
    private MouseInputs mouseInputs;
    private HorizontalWalls[][] hWalls;
    private VerticalWalls[][] vWalls;
    private int cols;
    private int rows;
    private int cellSize;
    private int panelWidth = 480;
    private int panelHeight = 480;
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
    //used to calculate the camera speed when wasd keys are pressed
    private static long lastFrameTime = 0;
    private static float delta;


    public OpenGLEngine(JPanel panel, MouseInputs mouseInputs, Boolean only3D, int cols, int rows, int cellSize) {
        this.canvas = new GLJPanel();
        this.panel = panel;
        this.only3D = only3D;
        this.cellSize = cellSize;
        this.mouseInputs = mouseInputs;
        this.cols = cols;
        this.rows = rows;


        //implemented to combat errors when the mouse pointer leaves the canvas
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

    //JOGL GLeventListener draw method, acts as the defacto "gameloop". While the animator thread runs it will invoke display as if in a while loop
    @Override
    public void display(GLAutoDrawable glad) {
        //checking which view has been selected, default is first person. (3rd overshoulder)
        if (!firstPersonView) {
            camera.move(!firstPersonView);
        }
        //checking if the data has been cleared, used when replaying.
        if (!cleared) {
            clear();
            cleared = true;
        }
        //update the view
        tick();
        //move the camera using firstperson move method
        if (firstPersonView) {
            camera.move();
        }
        //render visible walls only
        for (Entity wall : VISIBLE_WALLS) {
            renderer.processEntity(wall);
        }
        //getting entities and terrain ready and rendering them
        for (Terrain terrain : terrains) {
            renderer.processTerrain(terrain);
        }
        renderer.processEntity(mouseGFX);
        renderer.render(sun, camera);
        //calculating the time between frames, for movement purposes
        long currentFrameTime = System.currentTimeMillis();
        delta = (currentFrameTime - lastFrameTime) / 1000f;
        lastFrameTime = currentFrameTime;
    }

    //JOGL GLeventlistener close method, here we just clean up the data from memory.
    @Override
    public void dispose(GLAutoDrawable arg0) {
        renderer.cleanUp();
        loader.cleanUpMemory();
    }

    //JOGL GLevenlistener initiation method, here we create our objects. init of the jogl sequence
    @Override
    public void init(GLAutoDrawable glad) {
        terrains = new HashSet<>();
        renderer = new MasterRenderer(glad.getGL().getGL3(), panelWidth, panelHeight);
        loader = new Loader(glad.getGL().getGL3());
        objectLoader = new OBJLoader();
        VISIBLE_WALLS = new HashSet<Entity>();
        createGrid();
        createWalls();
        createTerrain(cols, rows);
        createEntities();
    }

    //JOGL GLeventlistener invoked on screen resizing, our app is unresizable, not use.
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        //un-used our screen is never resized.
    }

    //keylistener interface
    @Override
    public void keyTyped(KeyEvent e) {
    }

    //on key press alter the current speed of the camera for w and s, or rotation speed with a d
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

    //on key release set 0 for the speeds to stop camera movement.
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

    //used to keep the mouse inside the canvas as the mouse is panned to alter camera angle
    void CenterMouse(MouseEvent e) {
        try {
            r = new Robot();
            r.mouseMove((int) (e.getComponent().getLocationOnScreen().getX() + (panelWidth) / 2), (int) (e.getComponent().getLocationOnScreen().getY() + (panelHeight) / 2));
        } catch (AWTException ex) {
            ex.printStackTrace();
        }
    }

    //mouseMotionListener
    //mouse drag with right-click down will rotate the camera.
    public void mouseDragged(MouseEvent arg0) {
        if (rightClickDown) {
            float pitch = camera.getPitch();
            float angleAroundPlayer = camera.getAngleAroundMouse();

            float pitchChange = (arg0.getY() - panelHeight / 2) * 0.1f;
            camera.setPitch(pitch + pitchChange);

            float angleChange = (arg0.getX() - panelWidth / 2) * 0.1f;
            if (firstPersonView) {
                camera.setAngleAroundMouse(angleAroundPlayer + angleChange);
            }
            if (!firstPersonView) {
                camera.setAngleAroundMouse(angleAroundPlayer - angleChange);
            }

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

    //mouse eventlistener, used to differentiate natural mouse movement, to those used to pan the camera
    //on right click press over the GLcanvas camera is affected
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

    //mousewheel eventlistener, used to zoom the camera in and out
    public void mouseWheelMoved(MouseWheelEvent arg0) {
        float distanceFromPlayer = camera.getDistanceFromMouse();
        if (arg0.getUnitsToScroll() > 0) {
            camera.setDistanceFromPlayer(distanceFromPlayer + 1 * arg0.getUnitsToScroll());
        } else {
            if (distanceFromPlayer > 0) {
                camera.setDistanceFromPlayer(distanceFromPlayer + 1 * arg0.getUnitsToScroll());
            }
        }
    }

    private void createGrid() {
        grid = new Grid(cols, rows, cellSize, this);
    }

    private void createWalls() {

        //Loading models and textures to create the walls.
        RawModel wallRaw = objectLoader.loadObjectModel("wall", loader);
        ModelTexture wallTexture = new ModelTexture(loader.loadTexture("myTexture"));
        TexturedModel texturedWall = new TexturedModel(wallRaw, wallTexture);
        texturedWall.getModelTexture().setReflectivity(1);
        texturedWall.getModelTexture().setShineDamper(10);

        //for the horizontal walls we need +1 row because to have 16squares you need 17 lines to close them.
        hWalls = new HorizontalWalls[cols][rows + 1];

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows + 1; j++) {
                hWalls[i][j] = new HorizontalWalls(grid.getHWallPosition(i, j));
                int x = cols - 1 - hWalls[i][j].getPosition().getCol();
                int z = hWalls[i][j].getPosition().getRow();

                Entity hWall = new Entity(texturedWall, new Vec3f(x * cellSize, 0, z * cellSize - 1), 0, 0, 0, 1);
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
                int z = vWalls[i][j].getPosition().getRow() + 1;

                Entity vWall = new Entity(texturedWall, new Vec3f(x * cellSize - 1, 0, z * cellSize), 0, 90, 0, 1);
                vWalls[i][j].setEntity(vWall);
                vWalls[i][j].setVisible(false);

                //always showing side walls.
                if (i == 0 || i == cols) {
                    vWalls[i][j].setVisible(true);
                }
                //always showing starting wall
                if (i == 1 && j == 0) {
                    vWalls[i][j].setVisible(true);
                }
            }
        }
    }

    private void createEntities() {
        model = objectLoader.loadObjectModel("E 45 Aircraft", loader);
        //use the loader to get the id of the texture and pass it to the new texture
        texture = new ModelTexture(loader.loadTexture("grey"));
        texturedModel = new TexturedModel(model, texture);
        texturedModel.getModelTexture().setReflectivity(1);
        texturedModel.getModelTexture().setShineDamper(10);
        mouseGFX = new Entity(texturedModel, new Vec3f(cols * cellSize - cellSize / 2, 3, 5f), 0, 180, 0, 1.0f);
        mouse = new Mouse(grid.getPosition(0, 0), mouseGFX);
        camera = new Camera(mouseGFX);
        sun = new Light(new Vec3f(2000, 2000, 000), new Vec3f(1, 1, 1));
    }

    private void createTerrain(int cols, int rows) {
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                if (cols == rows && cols == 16) {
                    if (((i == 7) || (i == 8)) && ((j == 7) || (j == 8))) {
                        terrains.add(new Terrain(i, j, loader, new ModelTexture(loader.loadTexture("green"))));
                        continue;
                    }
                }
                terrains.add(new Terrain(i, j, loader, new ModelTexture(loader.loadTexture("black"))));
            }
        }

    }

    private void tick() {
        //if the mouse animation was started update the micromouse motion
        if (!mouseAnimationFinished) {
            mouseAnimation();
            return;
        }
        //get inputs from the mouseInputs 3d queue.
        String inputs = mouseInputs.getMouseInput3D();

        //checking that we actually got a result and that it matches the length we need before we try to parse and extract data
        if (inputs != null && (inputs.length() > 9)) {
            int col = MouseInputsTranslator.parseCol(inputs);
            int row = MouseInputsTranslator.parseRow(inputs);

            //getting the "live" micromouse current position in the maze
            Position pos = grid.getPosition(col, row);
            //getting the old position from the modeled micromouse
            Position oldMousePosition = mouse.getPosition();
            //updating modeled micromouse position
            mouse.setPosition(pos);

            //parsing the direction for "first person" camera view, and wall positioning
            String direction = MouseInputsTranslator.parseDirection(inputs);

            //launches the mouse animation
            setupMouseAnimation(oldMousePosition, pos, direction);

            //saving previous direction for angle calculation (rotation of the mouseAnimation)
            previousDirection = direction;

            //parsing wall presence from inputs, and applying them to the model
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
                    //facing west, front wall will be a vertical wall with the same position as current position
                    vWalls[col][row].setVisible(fWall);
                    //facing west, right wall will be a horizontal wall at col and +1 row as position
                    hWalls[col][row + 1].setVisible(rWall);
                    break;
            }
        }
    }

    //setting up the mouse animation, setting its boolean to false, and calculating the pixel locations and distances it will need to move
    //dived in 25 increments, fps is set to be around 60fps. it will take 25 frames for the mouse animation, just under half second
    public void setupMouseAnimation(Position initialPos, Position finalPos, String direction) {
        mouseAnimationFinished = false;
        xInitial = (-initialPos.getCol()) * cellSize;
        zInitial = initialPos.getRow() * cellSize;
        xFinal = (-finalPos.getCol()) * cellSize;
        zFinal = finalPos.getRow() * cellSize;
        float xDif = xFinal - xInitial;
        float zDif = zFinal - zInitial;
        xIncrement = xDif / 10;
        zIncrement = zDif / 10;
        yAngleIncrement = (calculateYRotationAngle(direction) / 10);
    }

    //cut into separated if statements and 2 variables, leaving the option to separate rotation from translation
    public void mouseAnimation() {
        if (numbOfRotations < 10) {
            mouseGFX.increaseRotation(0, yAngleIncrement, 0);
            numbOfRotations++;
            return;
        }
        if (numbOfTranslation < 10) {
            xInitial += xIncrement;
            zInitial += zIncrement;
            mouseGFX.increasePosition(xIncrement, 0, zIncrement);
            numbOfTranslation++;
            return;
        }
        //end the animation and resets the variables to default 0 0 and true
        if (numbOfRotations == numbOfTranslation) {
            numbOfRotations = 0;
            numbOfTranslation = 0;
            mouseAnimationFinished = true;
        }
    }

    //calculating the angle the animation will rotate based on previous direction and current.
    //clockwise turn is negative, counter positive.
    public float calculateYRotationAngle(String direction) {
        if (previousDirection.equals("N")) {
            yAngle = (direction.equals("E")) ? -90 : (direction.equals("S")) ? 180 : (direction.equals("W")) ? 90 : 0;
        }
        if (previousDirection.equals("E")) {
            yAngle = (direction.equals("E")) ? 0 : (direction.equals("S")) ? -90 : (direction.equals("W")) ? 180 : 90;
        }
        if (previousDirection.equals("S")) {
            yAngle = (direction.equals("E")) ? 90 : (direction.equals("S")) ? 0 : (direction.equals("W")) ? -90 : 180;
        }
        if (previousDirection.equals("W")) {
            yAngle = (direction.equals("E")) ? 180 : (direction.equals("S")) ? 90 : (direction.equals("W")) ? 0 : -90;
        }
        return yAngle;
    }

    //runnable method run, invoked when the thread is created. we use it to set up the panel and canvas
    @Override
    public void run() {
        //sizing the panel

        panelWidth = panel.getWidth();
        panelHeight = panel.getHeight();


        Dimension panelSize = new Dimension(panelWidth, panelHeight);
        panel.setPreferredSize(panelSize);
        panel.setMinimumSize(panelSize);
        panel.setMaximumSize(panelSize);


        //setting up the canvas, requesting focus will make sure the canvas can listen to the mouse/key inputs
        canvas.addGLEventListener(this);
        canvas.addKeyListener(this);
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
        canvas.addMouseWheelListener(this);
        canvas.setFocusable(true);
        canvas.requestFocus();
        panel.add(canvas);

        panel.setVisible(true);

        //the animator class is what will keep the JOGL "loop" alive and keep invoking the necessary methods in our case display
        // and dispose when the window is closed
        FPSAnimator animator = new FPSAnimator(canvas, 60);
        animator.start();
    }

    //start method, when the thread starts the run method will be inoked.
    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
    }

    //replay method used to for the "game loop" to invoke the clear method
    public void replay() {
        cleared = false;
    }

    //clear method used to setup for a replay of a live run, a re-replay.
    void clear() {
        //erasing walls
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows + 1; j++) {
                hWalls[i][j].setVisible(false);
                //always showing side walls.
                if (j == 0 || j == rows) {
                    hWalls[i][j].setVisible(true);
                }
            }
        }

        for (int i = 0; i < cols + 1; i++) {
            for (int j = 0; j < rows; j++) {
                vWalls[i][j].setVisible(false);
                //always showing side walls.
                if (i == 0 || i == cols) {
                    vWalls[i][j].setVisible(true);
                }
                //always showing starting wall
                if (i == 1 && j == 0) {
                    vWalls[i][j].setVisible(true);
                }
            }
        }
    }

    public static float getFrameTimeSeconds() {
        return delta;
    }

    public HashSet<Terrain> getTerrains() {
        return terrains;
    }

    public Loader getLoader() {
        return loader;
    }

    public HorizontalWalls[][] gethWalls() {
        return hWalls;
    }

    public VerticalWalls[][] getvWalls() {
        return vWalls;
    }

    //set up fake firstperson view aka 3rd person over the shoulder, setting up the distance from players to 5,
    // to not enter the model, true first person shows the model
    public void setFirstPersonView(boolean firstPersonView) {
        this.firstPersonView = firstPersonView;
        if (firstPersonView) {
            camera.setDistanceFromPlayer(5.0f);
            camera.setPitch(30);
        }
    }

    //set up the over the top view, the rotation is there to match 3d view and 2d view orientations
    public void setTopDownView() {
        camera.setPitch(89);
        camera.setPosition(new Vec3f(80f, 125, 75f));
        camera.setRotY(180);
        camera.setAngleAroundMouse(180);
    }
}