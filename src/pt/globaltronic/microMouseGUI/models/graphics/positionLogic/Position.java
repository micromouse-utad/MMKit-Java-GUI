package pt.globaltronic.microMouseGUI.models.graphics.positionLogic;

public class Position {

    private int col;
    private int row;
    private boolean visited;


    public Position(int col, int row){
        this.col = col;
        this.row = row;
        visited = false;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

}
