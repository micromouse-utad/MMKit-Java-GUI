package pt.globaltronic.microMouseGUI;

import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.Grid;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.MouseInputs;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.Position;
import pt.globaltronic.microMouseGUI.models.graphics.services.MouseInputsTranslator;
import pt.globaltronic.microMouseGUI.models.graphics.viewObjects.Display;
import pt.globaltronic.microMouseGUI.models.graphics.services.ImageLoader;
import pt.globaltronic.microMouseGUI.models.graphics.viewObjects.HorizontalWalls;
import pt.globaltronic.microMouseGUI.models.graphics.viewObjects.Mouse;
import pt.globaltronic.microMouseGUI.models.graphics.viewObjects.VerticalWalls;

import javax.microedition.io.StreamConnection;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class Engine implements Runnable{

    private Display display;
    String title;
    StreamConnection connection;
    int cols;
    int rows;
    int cellSize;
    int width;
    int height;
    private Thread thread;
    private boolean running;

    private Grid grid;
    LinkedList<Position> visited;
    Mouse mouse;
    MouseInputs mouseInputs;
    HorizontalWalls[][] hWalls;
    VerticalWalls[][] vWalls;

    BufferStrategy bs;
    private Graphics g;
    private BufferedImage mouseImg;
    private BufferedImage hWallImg;
    private BufferedImage vWallImg;




    public Engine(String title, MouseInputs mouseInputs, int cols, int rows, int cellSize){
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

        display = new Display(title, width, height);
        visited = new LinkedList<Position>();
        grid = new Grid(cols, rows, cellSize);
        mouseImg = ImageLoader.loadImage("/mouse.png");
        mouse = new Mouse(grid.getPosition(0,0),mouseImg);
        createWalls();
    }

    private void tick(){
        String inputs = mouseInputs.getMouseInput();
        if (inputs != null && (inputs.length() > 9)) {
            int col = MouseInputsTranslator.parseCol(inputs);
            int row = MouseInputsTranslator.parseRow(inputs);
            Position pos = grid.getPosition(col, row);
            pos.setVisited(true);
            visited.add(pos);
            mouse.setPosition(pos);

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
        //getting or creating the buffer strategy of our display, setting to triple buffered
        bs = display.getCanvas().getBufferStrategy();
        if(bs == null){
            display.getCanvas().createBufferStrategy(3);
            return;
        }
        //initialize the drawing tools, and clearing the screen;
        g = bs.getDrawGraphics();
        g.clearRect(0,0, width, height);

        visited.forEach((position -> {
            g.setColor(Color.RED);
            if (position.isVisited()) {
                g.fillRect(grid.colToX(position.getCol()), grid.rowToY(position.getRow()), cellSize, cellSize);
            }
        }));

        for (int i = 0; i < cols; i++){
            for (int j = 0; j < rows+1; j++){
                if (hWalls[i][j].isVisible()) {
                    Image image1 = hWalls[i][j].getImage();
                    int xPixel = grid.colToX(hWalls[i][j].getPosition().getCol());
                    int yPixel = grid.colToX(hWalls[i][j].getPosition().getRow());
                    g.drawImage(image1, xPixel, yPixel, null);
                }
            }
        }

        for (int i = 0; i < cols+1; i++){
            for (int j = 0; j < rows; j++){
                if(vWalls[i][j].isVisible()) {
                    Image image2 = vWalls[i][j].getImage();
                    int xPixel = grid.colToX(vWalls[i][j].getPosition().getCol());
                    int yPixel = grid.colToX(vWalls[i][j].getPosition().getRow());
                    g.drawImage(image2, xPixel, yPixel, null);
                }
            }
        }



        // render the visited position array only. need an image for positions, mouse and walls.
        g.drawImage(mouse.getImage(), grid.colToX(mouse.getPosition().getCol()), grid.rowToY(mouse.getPosition().getRow()), null);

        bs.show();
        g.dispose();
    }

    private void createWalls(){
        hWalls = new HorizontalWalls[cols][rows+1];
        hWallImg = ImageLoader.loadImage("/wallhorizontal.png");

        for (int i = 0; i < cols; i++){
            for (int j = 0; j < rows+1; j++){
                hWalls[i][j] = new HorizontalWalls(hWallImg, grid.getHWallPosition(i,j));
            }
        }

        vWalls = new VerticalWalls[cols+1][rows];
        vWallImg = ImageLoader.loadImage("/verticalwall.png");

        for (int i = 0; i < cols+1; i++){
            for (int j = 0; j < rows; j++){
                vWalls[i][j] = new VerticalWalls(vWallImg, grid.getVWallPosition(i,j));
            }
        }
    }

}
