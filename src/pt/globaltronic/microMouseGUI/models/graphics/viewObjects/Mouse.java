package pt.globaltronic.microMouseGUI.models.graphics.viewObjects;

import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.Position;

import java.awt.*;

public class Mouse {

    Position position;
    Image image;


    public Mouse (Position position, Image image){
        this.position = position;
        this.image = image;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public Image getImage() {
        return image;
    }

    public void move(String direction){
        switch (direction){
            case "N":

        }
    }

}
