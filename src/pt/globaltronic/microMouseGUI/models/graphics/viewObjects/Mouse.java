package pt.globaltronic.microMouseGUI.models.graphics.viewObjects;

import pt.globaltronic.microMouseGUI.models.graphics.Graphics3D.Pyramid;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.Position;

import java.awt.*;

public class Mouse {

    Position position;
    Image image;
    Pyramid mousePyr;


    public Mouse (Position position, Image image){
        this.position = position;
        this.image = image;
    }

    public Mouse (Position position, Pyramid mousePyr){
        this.position = position;
        this.mousePyr = mousePyr;
    }


    public void setPosition(Position position) {
        this.position = position;
        if(mousePyr != null) {
            mousePyr.setX(position.getCol() * 10 + 2.5);
            mousePyr.setY((15 - position.getRow()) * 10 - 2.5);
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
