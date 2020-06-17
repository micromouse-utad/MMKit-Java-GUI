package pt.globaltronic.microMouseGUI.models.graphics.viewObjects;

import pt.globaltronic.microMouseGUI.models.graphics.Graphics3D.Cube;

import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.walls.WallPosition;

import java.awt.*;

public abstract class Walls {

    private Image image;
    private Cube cube;
    private WallPosition position;

     public Walls(Image image, WallPosition position){
         this.image = image;
         this.position = position;
     }

    public Walls(WallPosition position){
        this.position = position;
    }

    public Image getImage() {
        return image;
    }

    public Cube getCube() {
        return cube;
    }

    public WallPosition getPosition() {
        return position;
    }

    public void setCube(Cube cube) {
        this.cube = cube;
    }

}
