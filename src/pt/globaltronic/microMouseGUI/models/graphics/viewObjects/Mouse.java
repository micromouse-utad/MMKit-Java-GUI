package pt.globaltronic.microMouseGUI.models.graphics.viewObjects;

import pt.globaltronic.microMouseGUI.models.graphics.Graphics3D.Pyramid;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.Position;

import java.awt.*;

public class Mouse {

    Position position;
    Image image;
    Pyramid mousePyr;
    int correction;
    double cellSize;


    public Mouse (Position position, Image image){
        this.position = position;
        this.image = image;
    }

    public Mouse (Position position, Pyramid mousePyr, int correction, double cellSize){
        this.position = position;
        this.mousePyr = mousePyr;
        this.correction = correction;
        this.cellSize = cellSize;
    }


    public void setPosition(Position position) {
        this.position = position;
        if(mousePyr != null) {
            mousePyr.setX(position.getCol() * cellSize + cellSize/4 );
            mousePyr.setY((correction - position.getRow()) * cellSize + cellSize/4);
        }
    }

    public Position getPosition() {
        return position;
    }

    public Image getImage() {
        return image;
    }

    public Pyramid getMousePyr() {
        return mousePyr;
    }
}
