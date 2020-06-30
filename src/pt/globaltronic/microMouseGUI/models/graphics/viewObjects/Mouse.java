package pt.globaltronic.microMouseGUI.models.graphics.viewObjects;

import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.Position;
import pt.globaltronic.microMouseGUI.openGL.entity.Entity;

import java.awt.*;

public class Mouse {

    private Position position;
    private Image image;
    private Entity mouseGFX;
    private int correction;
    private double cellSize;


    public Mouse (Position position, Image image){
        this.position = position;
        this.image = image;
    }

    public Mouse (Position position, int correction, double cellSize){
        this.position = position;
        this.correction = correction;
        this.cellSize = cellSize;
    }

    public Mouse(Position position, Entity mouseGFX, double cellSize){
        this.position = position;
        this.mouseGFX = mouseGFX;
        this.cellSize = cellSize;
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

    public Entity getMouseGFX() {
        return mouseGFX;
    }

    public void setMouseGFX(Entity mouseGFX) {
        this.mouseGFX = mouseGFX;
    }
}
