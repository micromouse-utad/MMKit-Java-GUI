package pt.globaltronic.microMouseGUI.models.graphics.positionLogic.walls;

import java.awt.*;

public abstract class WallPosition {

    int col;
    int row;
    boolean visible;


    public WallPosition(int col, int row){
        this.col = col;
        this.row = row;
        visible = false;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

}
