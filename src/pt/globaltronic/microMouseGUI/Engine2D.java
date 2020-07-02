package pt.globaltronic.microMouseGUI;

import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.Grid;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.MouseInputs;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.Position;
import pt.globaltronic.microMouseGUI.models.graphics.services.MouseInputsTranslator;
import pt.globaltronic.microMouseGUI.models.graphics.viewObjects.Display;
import pt.globaltronic.microMouseGUI.models.graphics.viewObjects.HorizontalWalls;
import pt.globaltronic.microMouseGUI.models.graphics.viewObjects.Mouse;
import pt.globaltronic.microMouseGUI.models.graphics.viewObjects.VerticalWalls;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.LinkedList;


public class Engine2D implements Runnable{

    private JPanel panel;
    private Display display;
    private String title;
    private int cols;
    private int rows;
    private int cellSize;
    private int width = 480;
    private int height = 480;
    private int correction;
    private Thread thread;
    private boolean running;
    private boolean cleared = true;
    private boolean replay = false;


    private Grid grid;
    private LinkedList<Position> visited;
    private LinkedList<Position> goal;
    private Mouse mouse;
    private MouseInputs mouseInputs;
    private HorizontalWalls[][] hWalls;
    private VerticalWalls[][] vWalls;

    private BufferStrategy bs;
    private Graphics g;

    public Engine2D(JPanel panel, String title, MouseInputs mouseInputs, int cols, int rows){
        this.title = title;
        this.panel = panel;
        this.mouseInputs = mouseInputs;
        this.cols = cols;
        this.rows = rows;
        this.cellSize = Math.min(480 / cols, 480 / rows);
        this.correction = rows -1;
    }

    @Override
    public void run() {
        init();
        while (running) {
            if (!cleared) {
                clear();
                cleared = true;
            }
            try {
                tick();
            }catch (NumberFormatException | ArrayIndexOutOfBoundsException ex){
                JOptionPane.showMessageDialog(panel, "Critical error, erroneous grid sizing and/or erroneous micro-mouse input caused the program to abruptly shutdown." +
                        "\n Error Message: " + ex.getClass());
                System.exit(-1);
            }
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
            running = false;
            thread.join();
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    private void init(){
        display = new Display(panel, title, width, height);
        visited = new LinkedList<Position>();
        goal = new LinkedList<Position>();
        grid = new Grid(cols, rows, cellSize);
        if(cols == rows && cols == 16) {
            goal.add(grid.getPosition(7, 7));
            goal.add(grid.getPosition(7, 8));
            goal.add(grid.getPosition(8, 7));
            goal.add(grid.getPosition(8, 8));
        }
        mouse = new Mouse(grid.getPosition(0,0));
        createWalls();
    }

    private void tick(){
        //getting inputs from the 2d queue
        String inputs = mouseInputs.getMouseInput();
        //checking the length of the line to see if it is elligible for parsing
        if (inputs != null && (inputs.length() > 9)) {
            //parsing positional data
            int col = MouseInputsTranslator.parseCol(inputs);
            int row = MouseInputsTranslator.parseRow(inputs);

            //getting the given position, setting it as visited and setting it as the new mouse position
            Position pos = grid.getPosition(col, row);
            pos.setVisited(true);
            visited.add(pos);
            mouse.setPosition(pos);

            //getting directional and wall presence inputs
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
                    //facing east left wall will be horizontal wall at same col, +1 row from position
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
                    //left wall will be a horizontal wall when facing west, with same position as mouse position
                    hWalls[col][row].setVisible(lWall);
                    //facing west, front wall will be a vertical wall at same col and row as current position
                    vWalls[col][row].setVisible(fWall);
                    //facing west, right wall will be a horizontal wall at col and +1 row as position
                    hWalls[col][row + 1].setVisible(rWall);
                    break;
            }
        }
    }

    //our drawing method
    private void render(){
        //getting or creating the buffer strategy of our display, setting to triple buffered
        bs = display.getCanvas().getBufferStrategy();
        if(bs == null){
            display.getCanvas().createBufferStrategy(3);
            return;
        }
        //initialize the drawing tools, and clearing the screen;
        g = bs.getDrawGraphics();
        g.clearRect(0,0, width, height);
        g.setColor(Color.BLACK);
        //since adapting for cols / rows selection the size of the rectangle display needs to adapt to non square setups
        int xOffset = ((width - (cols * cellSize)) / 2);
        int yOffset = ((height - (rows * cellSize)) / 2);
        g.fillRect(xOffset , yOffset, cols * cellSize, rows * cellSize);

        if(cols == rows && cols == 16) {
            goal.forEach((position -> {
                g.setColor(Color.GREEN);
                g.fillRect(xOffset+ grid.colToX(position.getCol()), yOffset + grid.rowToY(correction - position.getRow()), cellSize, cellSize);
            }));
        }
        visited.forEach((position -> {
            g.setColor(Color.lightGray);
            if (position.isVisited()) {
                g.fillRect(xOffset + grid.colToX(position.getCol()), yOffset + grid.rowToY(correction - position.getRow()), cellSize, cellSize);
            }
        }));

        for (int i = 0; i < cols; i++){
            for (int j = 0; j < rows+1; j++){
                if (hWalls[i][j].isVisible()) {
                    int xPixel = xOffset + grid.colToX(hWalls[i][j].getPosition().getCol());
                    int yPixel = yOffset + grid.rowToY(correction + 1 - hWalls[i][j].getPosition().getRow());
                    g.setColor(Color.RED);
                    //making last row of wall fit in the grid
                    if(j == 0){
                        g.fillRect(xPixel, yPixel-5, cellSize, 5);
                    }
                    g.fillRect(xPixel, yPixel, cellSize, 5);
                }
            }
        }

        for (int i = 0; i < cols+1; i++){
            for (int j = 0; j < rows; j++){
                if(vWalls[i][j].isVisible()) {
                    int xPixel = xOffset + grid.colToX(vWalls[i][j].getPosition().getCol());
                    int yPixel = yOffset + grid.colToX(correction - vWalls[i][j].getPosition().getRow());
                    //adjusting the last line of vertical walls to fit in the grid.
                    g.setColor(Color.RED);
                    if (i == cols){
                        g.fillRect(xPixel-5, yPixel, 5, cellSize);
                        continue;
                    }
                    g.fillRect(xPixel, yPixel, 5, cellSize);
                }
            }
        }

        g.setColor(Color.BLUE);
        g.fillRect(xOffset + grid.colToX(mouse.getPosition().getCol()) + cellSize/3, yOffset + grid.rowToY(correction - mouse.getPosition().getRow()) + cellSize/3, cellSize/2, cellSize/2);

        //show the buffered data
        bs.show();
        //dispose the drawing tool
        g.dispose();
    }

    private void createWalls(){
        hWalls = new HorizontalWalls[cols][rows+1];

        for (int i = 0; i < cols; i++){
            for (int j = 0; j < rows+1; j++){
                hWalls[i][j] = new HorizontalWalls(grid.getHWallPosition(i,j));
                //always showing side walls.
                if(j == 0 || j == rows){
                    hWalls[i][j].setVisible(true);
                }
            }
        }

        vWalls = new VerticalWalls[cols+1][rows];

        for (int i = 0; i < cols+1; i++){
            for (int j = 0; j < rows; j++){
                vWalls[i][j] = new VerticalWalls(grid.getVWallPosition(i,j));
                //always showing side walls.
                if(i == 0 || i == cols){
                    vWalls[i][j].setVisible(true);
                }
                //always showing starting wall
                if (i == 1 && j == 0){
                    vWalls[i][j].setVisible(true);
                }
            }
        }
    }

    public void replay(){
        replay = true;
    }

    public void reReplay(){
        cleared = false;
    }

    //clearing data, used when setting up for replay.
    void clear(){
        visited.forEach(position -> position.setVisited(false));
        visited.clear();
        //clearing walls
        for (int i = 0; i < cols; i++){
            for (int j = 0; j < rows+1; j++){
                //leaving side walls up.
                if(j == 0 || j == rows){
                    hWalls[i][j].setVisible(true);
                    continue;
                }
                hWalls[i][j].setVisible(false);
            }
        }
        for (int i = 0; i < cols+1; i++){
            for (int j = 0; j < rows; j++){
                //leaving side walls up
                if(i == cols || i == 0){
                    vWalls[i][j].setVisible(true);
                    continue;
                }
                //always showing starting wall
                if (i == 1 && j == 0){
                    vWalls[i][j].setVisible(true);
                    continue;
                }
                vWalls[i][j].setVisible(false);
            }
        }
    }
}
