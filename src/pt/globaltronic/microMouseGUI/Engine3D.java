package pt.globaltronic.microMouseGUI;

import pt.globaltronic.microMouseGUI.models.graphics.Graphics3D.*;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.Grid;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.MouseInputs;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.Position;
import pt.globaltronic.microMouseGUI.models.graphics.services.MouseInputsTranslator;
import pt.globaltronic.microMouseGUI.models.graphics.viewObjects.HorizontalWalls;
import pt.globaltronic.microMouseGUI.models.graphics.viewObjects.Mouse;
import pt.globaltronic.microMouseGUI.models.graphics.viewObjects.VerticalWalls;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class Engine3D implements Runnable{

    private JPanel panel;
    private Screen screen;
    private String title;
    int cols;
    int rows;
    int cellSize;
    int width;
    int height;
    int correction;
    double size = 10;
    private Thread thread;
    private boolean running;

    private Grid grid;
    private LinkedList<Position> visited;
    private Mouse mouse;
    private MouseInputs mouseInputs;
    private HorizontalWalls[][] hWalls;
    private VerticalWalls[][] vWalls;

    public Engine3D(JPanel panel, String title, MouseInputs mouseInputs, int cols, int rows, int cellSize, double Size){
        this.panel = panel;
        this.title = title;
        this.mouseInputs = mouseInputs;
        this.cols = cols;
        this.rows = rows;
        this.cellSize = cellSize;
        this.width = cols * cellSize;
        this.height = rows * cellSize;
        this.correction = rows -1;
        init();
    }

    @Override
    public void run() {
        //basic run loop, update with the tick method and render the graphics.
        while(running){
            tick();
            render();
        }
    }

    public synchronized void start(){
        //preventing thread to start if it is already running
        if(running){
            return;
        }
        //create the thread and set running to true
        thread = new Thread(this);
        running = true;
        thread.start();
    }

    public synchronized void stop(){

        if (!running){return;}
        try{
            thread.join();
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    private void init(){

        screen = new Screen(width, height, correction);
        panel.add(screen);
        panel.setPreferredSize(new Dimension(16*30, 16*30));
        panel.setMinimumSize(new Dimension(16*30, 16*30));
        panel.setMaximumSize(new Dimension(16*30, 16*30));
        visited = new LinkedList<Position>();
        grid = new Grid(cols, rows, cellSize);
        mouse = new Mouse(grid.getPosition(0,correction), new Pyramid(2.5, correction * size - 2.5, 0, 5, 5, 2, Color.BLUE), correction, size);
        createWalls();
        createPositionsGraphics();
    }

    private void tick(){
        //get the mouse inputs from the 3d queue
        String inputs = mouseInputs.getMouseInput3D();

        //checking that we actual got a result and that it matches the length we need before we try to parse and extract data
        if (inputs != null && (inputs.length() > 9)) {
            int col = MouseInputsTranslator.parseCol(inputs);
            int row = MouseInputsTranslator.parseRow(inputs);

            //getting the mouse current position
            Position pos = grid.getPosition(col, row);
            pos.setVisited(true);
            visited.add(pos);
            //updating mouse position and its graphics
            mouse.setPosition(pos);
            mouse.getMousePyr().setRotAdd();
            mouse.getMousePyr().updatePoly();

            //parsing the direction for "first person" camera view, and wall positioning
            String direction = MouseInputsTranslator.parseDirection(inputs);

            screen.setCameraPositionForMouseView(direction, pos, size);

            Boolean lWall = MouseInputsTranslator.parseLeftWall(inputs);
            Boolean fWall = MouseInputsTranslator.parseFrontWall(inputs);
            Boolean rWall = MouseInputsTranslator.parseRightWall(inputs);

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

    private void render(){
        //invoke the screen paint behavior
        screen.SleepAndRefresh();
    }

    //initiate the walls and show the outskirts of the grid
    private void createWalls(){
        //for the horizontal walls we need +1 row because to have 16squares you need 17 lines to close them.
        hWalls = new HorizontalWalls[cols][rows+1];

        for (int i = 0; i < cols; i++){
            for (int j = 0; j < rows+1; j++){
                hWalls[i][j] = new HorizontalWalls(grid.getHWallPosition(i,j));
                int x = hWalls[i][j].getPosition().getCol();
                int y = hWalls[i][j].getPosition().getRow();
                hWalls[i][j].setCube(new Cube(x * size, (correction + 1 - y) * size, 0, size, 0.5, 1, Color.BLACK));
                hWalls[i][j].setVisible(false);
                //always showing side walls.

                if(j == 0){
                    hWalls[i][j].setVisible(true);
                }
                if(j == rows){
                    hWalls[i][j].setVisible(true);
                }
            }
        }

        //likewise for the Vertical walls we need +1 col.
        vWalls = new VerticalWalls[cols+1][rows];

        for (int i = 0; i < cols+1; i++){
            for (int j = 0; j < rows; j++){
                vWalls[i][j] = new VerticalWalls(grid.getVWallPosition(i,j));
                int x = vWalls[i][j].getPosition().getCol();
                int y = vWalls[i][j].getPosition().getRow();
                vWalls[i][j].setCube(new Cube(x * size, (correction - y) * size, 0, 0.5, size, 1, Color.BLACK));
                vWalls[i][j].setVisible(false);

                //always showing side walls.
                if(i == 0){
                    vWalls[i][j].setVisible(true);
                }
                if(i == cols){
                    vWalls[i][j].setVisible(true);
                }
            }
        }
    }

    public void createPositionsGraphics(){
        //creating the 2d polygons for the grid, and adding them to the screen's polygon arraylist to be drawn
        //we set the default position to be false, so that they will not be drawn on the screen right away
        Position[][] arr = grid.getPositionsArray();
        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                arr[x][y].setPolygon2D(new Polygon2D(new double[]{x * size, (x +1)* size, (x+1) * size, x* size}, new double[]{(correction + 1 - y) * size, (correction +1 - y)* size, (correction +1 - (y+1)) * size, (correction +1 -(y+1))* size}, new double[]{0, 0, 0, 0}, Color.GRAY, false));
                arr[x][y].setVisited(false);
                screen.Polygon2DS.add(arr[x][y].getPolygon2D());
            }
        }

    }
}
