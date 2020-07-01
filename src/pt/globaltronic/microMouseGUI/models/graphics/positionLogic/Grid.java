package pt.globaltronic.microMouseGUI.models.graphics.positionLogic;

import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.walls.HorizontalWallPosition;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.walls.VerticalWallPosition;

public class Grid {

    private int cols;
    private int rows;
    private int cellSize; //size of cells in pixels
    private Position[][] positions;
    private HorizontalWallPosition[][] hWallPositions;
    private VerticalWallPosition[][] vWallPositions;

    public Grid(int cols, int rows, int cellSize){
        this.cols = cols;
        this.rows = rows;
        this.cellSize = cellSize;
        init();
    }

    public void init(){
        createPosition();
        createHWallPositions();
        createVWallPositions();
    }

    public void createPosition(){
        positions = new Position[cols][rows];

        for (int i = 0; i < cols; i++){
            for (int j = 0; j < rows; j++){
                positions[i][j] = new Position(i,j);
            }
        }
    }

    public void createHWallPositions(){
      hWallPositions = new HorizontalWallPosition[cols][rows+1];

      for (int i = 0; i < cols; i++){
          for (int j = 0; j < rows+1; j++){
              hWallPositions[i][j] = new HorizontalWallPosition(i,j);
          }
      }
    }

    public void createVWallPositions(){
        vWallPositions = new VerticalWallPosition[cols+1][rows];

        for (int i = 0; i < cols+1; i++){
            for (int j = 0; j < rows; j++){
                vWallPositions[i][j] = new VerticalWallPosition(i,j);
            }
        }
    }

    //convert a position col to X pixels
    public int colToX(int col){
        return col * cellSize;
    }

    //convert a position row to Y pixels
    public int rowToY(int row){
        return row * cellSize;
    }

    public Position getPosition(int col, int row){
        return positions[col][row];
    }

    public HorizontalWallPosition getHWallPosition(int col, int row){
        return hWallPositions[col][row];
    }

    public VerticalWallPosition getVWallPosition(int col, int row){
        return vWallPositions[col][row];
    }
}
