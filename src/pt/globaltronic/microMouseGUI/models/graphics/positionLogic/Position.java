package pt.globaltronic.microMouseGUI.models.graphics.positionLogic;

import pt.globaltronic.microMouseGUI.models.graphics.Graphics3D.Polygon2D;

public class Position {

    private int col;
    private int row;
    private boolean visited;
    private Polygon2D polygon2D;


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

    public void setCol(int col) {
        this.col = col;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
        if (polygon2D != null) {
            polygon2D.setVisible(visited);
        }
    }

    public void setPolygon2D(Polygon2D polygon2D) {
        this.polygon2D = polygon2D;
    }

    public Polygon2D getPolygon2D() {
        return polygon2D;
    }
}
