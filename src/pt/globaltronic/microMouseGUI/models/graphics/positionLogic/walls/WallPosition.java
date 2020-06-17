package pt.globaltronic.microMouseGUI.models.graphics.positionLogic.walls;

public abstract class WallPosition {

    private int col;
    private int row;



    public WallPosition(int col, int row){
        this.col = col;
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

}
