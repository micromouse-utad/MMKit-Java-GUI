package pt.globaltronic.microMouseGUI;

import pt.globaltronic.microMouseGUI.models.graphics.Graphics3D.*;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.Grid;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.MouseInputs;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.Position;
import pt.globaltronic.microMouseGUI.models.graphics.services.MouseInputsTranslator;
import pt.globaltronic.microMouseGUI.models.graphics.viewObjects.HorizontalWalls;
import pt.globaltronic.microMouseGUI.models.graphics.viewObjects.Mouse;
import pt.globaltronic.microMouseGUI.models.graphics.viewObjects.VerticalWalls;

import javax.microedition.io.StreamConnection;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.LinkedList;

public class Engine3D implements Runnable{

    private Display3D display3d;
    private String title;
    private StreamConnection connection;
    int cols;
    int rows;
    int cellSize;
    int width;
    int height;
    double Size = 10;
    private Thread thread;
    private boolean running;

    private Grid grid;
    private LinkedList<Position> visited;
    private Mouse mouse;
    private MouseInputs mouseInputs;
    private HorizontalWalls[][] hWalls;
    private VerticalWalls[][] vWalls;


    private BufferStrategy bs;
    private Graphics g;


    public Engine3D(String title, MouseInputs mouseInputs, int cols, int rows, int cellSize){
        this.title = title;
        this.mouseInputs = mouseInputs;
        this.cols = cols;
        this.rows = rows;
        this.cellSize = cellSize;
        this.width = cols * cellSize;
        this.height = rows * cellSize;

    }

    @Override
    public void run() {
        init();
        while(running){
            tick();
            render();
        }
    }

    public synchronized void start(){
        if(running){return;}

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

        display3d = new Display3D(title, width, height);
        visited = new LinkedList<Position>();
        grid = new Grid(cols, rows, cellSize);
        mouse = new Mouse(grid.getPosition(0,0), new Pyramid(2.5, 2.5, 0, 5, 5, 2, Color.BLUE));
        new Generate3dObjects();
        createWalls();
        createdPositionsGraphics();
    }

    private void tick(){
        String inputs = mouseInputs.getMouseInput3D();
        if (inputs != null && (inputs.length() > 9)) {
            int col = MouseInputsTranslator.parseCol(inputs);
            int row = MouseInputsTranslator.parseRow(inputs);
            Position pos = grid.getPosition(col, row);
            pos.setVisited(true);
            visited.add(pos);
            mouse.setPosition(pos);
            mouse.getMousePyr().setRotAdd();
            mouse.getMousePyr().updatePoly();

            String direction = MouseInputsTranslator.parseDirection(inputs);
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
                    vWalls[col+1][row].setVisible(rWall);
                    break;
                case "E":
                    //facing east left wall will be horizontal wall at same col +1 row from position
                    hWalls[col][row + 1].setVisible(lWall);
                    //facing east front wall will be a vertical wall at +1 col and row as position
                    vWalls[col+1][row].setVisible(fWall);
                    //facing east right wall will be a horizontal wall at position same  as mouse position
                    hWalls[col][row].setVisible(rWall);
                    break;
                case "S":
                    // similar to N facing but reverted left wall will be the right wall
                    vWalls[col+1][row].setVisible(lWall);
                    //front wall will be a horizontal wall at same postion as mouse position
                    hWalls[col][row].setVisible(fWall);
                    //right wall will be left wall of N case
                    vWalls[col][row].setVisible(rWall);
                    break;
                case "W":
                    //reverse of case east
                    //left wall will be a vertical wall when facing west, with same position as mouse position
                    hWalls[col][row].setVisible(lWall);
                    //facing west, front wall will be a vertical wall at current mouse position
                    vWalls[col][row].setVisible(fWall);
                    //facing west, right wall will be a horizontal wall at -1 col and +1 row as position
                    hWalls[col][row + 1].setVisible(rWall);
                    break;
            }
        }
    }

    private void render(){
        display3d.getScreen().SleepAndRefresh();
    }

    private void createWalls(){
        hWalls = new HorizontalWalls[cols][rows+1];

        for (int i = 0; i < cols; i++){
            for (int j = 0; j < rows+1; j++){
                hWalls[i][j] = new HorizontalWalls(grid.getHWallPosition(i,j));
                int x = hWalls[i][j].getPosition().getCol();
                int y = hWalls[i][j].getPosition().getRow();
                hWalls[i][j].setCube(new Cube(x * Size, y * Size, 0, Size, 0.2, 1, Color.BLACK));
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

        vWalls = new VerticalWalls[cols+1][rows];

        for (int i = 0; i < cols+1; i++){
            for (int j = 0; j < rows; j++){
                vWalls[i][j] = new VerticalWalls(grid.getVWallPosition(i,j));
                int x = vWalls[i][j].getPosition().getCol();
                int y = vWalls[i][j].getPosition().getRow();
                vWalls[i][j].setCube(new Cube(x * Size, y * Size, 0, 0.2, Size, 1, Color.BLACK));
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

    public void createdPositionsGraphics(){
        Position[][] arr = grid.getPositionsArray();
        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                arr[x][y].setPolygon2D(new Polygon2D(new double[]{x * Size, (x +1)* Size , (x+1) *Size, x* Size}, new double[]{y * Size, y* Size , (y+1) *Size, (y+1)* Size}, new double[]{0, 0, 0, 0}, Color.GRAY, false));
                arr[x][y].setVisited(false);
                Screen.Polygon2DS.add(arr[x][y].getPolygon2D());

            }
        }

    }
}
